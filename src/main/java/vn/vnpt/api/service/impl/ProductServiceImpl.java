package vn.vnpt.api.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.product.ProductFilterIn;
import vn.vnpt.api.dto.mapper.ProductMapper;
import vn.vnpt.api.dto.out.customter.CustomerListOut;
import vn.vnpt.api.dto.out.product.ProductAttributeOut;
import vn.vnpt.api.dto.out.product.ProductDetailOut;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.api.dto.out.recommend.ClickData;
import vn.vnpt.api.dto.out.recommend.RecommendProducts;
import vn.vnpt.api.model.ErrorLog;
import vn.vnpt.api.model.User;
import vn.vnpt.api.model.UserActivity;
import vn.vnpt.api.repository.CustomerRepository;
import vn.vnpt.api.repository.ProductRepository;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.ProductService;
import vn.vnpt.api.service.helper.CollaborativeFiltering;
import vn.vnpt.api.service.helper.KafkaProducerService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    @Override
    public PagingOut<ProductListOut> listAllProducts(SortPageIn sortPageIn) {
        return productRepository.getAllProducts(sortPageIn);
    }


    @Override
    public ProductDetailOut getProductDetail(String productId) {
        var rs = productRepository.getProductDetail(productId);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        try {
            rs.setParameters(objectMapper.readValue(rs.getParametersJson(), new TypeReference<>() {
            }));

            if (user.isPresent()) {
                UserActivity userActivity = UserActivity.builder()
                        .customerId(user.get().getId())
                        .eventType("view_product")
                        .eventDetail(productId)
                        .build();
                kafkaProducerService.sendMessage("user-activity", gson.toJson(userActivity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    @Override
    public PagingOut<ProductListOut> findProductsByCategory(String categoryId, SortPageIn sortPageIn) {
        return productRepository.getAllProductByCategory(categoryId, sortPageIn);
    }

    @Override
    public PagingOut<ProductListOut> findAllBySubCategory(String subCategoryId, SortPageIn sortPageIn) {
        return productRepository.getAllProductBySubCategory(subCategoryId, sortPageIn);

    }

    @Override
    public PagingOut<ProductListOut> dynamicFilter(ProductFilterIn productFilterIn, SortPageIn sortPageIn) {
        return productRepository.dynamicFilter(productFilterIn, sortPageIn);
    }

    @Override
    public PagingOut<ProductListOut> listAll(SortPageIn sortPageIn) {
        return null;
    }

    @Override
    public Map<String, List<ProductAttributeOut>> listProductAttribute(String productId) {
        var rs = productRepository.getProductAttribute(productId);

        Map<String, List<ProductAttributeOut>> groupedAttributes = new HashMap<>();
        for (ProductAttributeOut attribute : rs) {
            String name = attribute.getName();
            if (!groupedAttributes.containsKey(name)) {
                groupedAttributes.put(name, new ArrayList<>());
            }
            groupedAttributes.get(name).add(attribute);
        }

        return groupedAttributes;
    }

    @Override
    public Object recommend(String subcategoryId, String categoryId, String productId) {
        // Lấy danh sách sản phẩm theo subcategory và category
        List<ProductListOut> prdsBySubcategory = productRepository.getAllProductBySubCategory(subcategoryId, new SortPageIn()).getData();
        List<ProductListOut> prdsByCategory = productRepository.getAllProductByCategory(categoryId, new SortPageIn()).getData();

        // Loại bỏ sản phẩm có productId khỏi danh sách
        prdsByCategory.removeIf(it -> Objects.equals(it.getProductId(), productId));
        prdsBySubcategory.removeIf(it -> Objects.equals(it.getProductId(), productId));

        // Kết hợp danh sách sản phẩm từ subcategory và category thành một Set
        Set<ProductListOut> combinedSet = new HashSet<>(prdsBySubcategory);
        combinedSet.addAll(prdsByCategory);

        // Lấy thông tin người dùng hiện tại
        Optional<User> user = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .flatMap(userRepository::findByEmail);

        // Lấy danh sách top seller
        var topSeller = productRepository.getTopSellerProducts(5);

        Map<String, Object> result = new HashMap<>();
        result.put("topSellers", topSeller);
        result.put("prdsBySubcategory", productMapper.toRecommendProducts(prdsBySubcategory));
        result.put("prdsByCategory", productMapper.toRecommendProducts(prdsByCategory));

        // Nếu người dùng hiện tại tồn tại
        user.ifPresent(u -> {
//            List<ProductListOut> list = productRepository.dynamicFilter(new ProductFilterIn(), new SortPageIn()).getData();
            // Lấy dữ liệu của khách hàng
            Map<String, Map<String, Double>> viewData = customerRepository.findAllCustomers(new SortPageIn()).getData().stream()
                    .collect(Collectors.toMap(CustomerListOut::getId, cus -> buildClickProductMap(cus.getId())));

            // Tạo một đối tượng CollaborativeFiltering và lấy ra các sản phẩm được đề xuất cho người dùng
            CollaborativeFiltering cf = new CollaborativeFiltering(viewData, null);
            Map<String, Double> recommendations = cf.getUserRecommendations(u.getId());

            // Lấy ra top 5 sản phẩm được đề xuất
            List<ProductListOut> matchedProducts = recommendations.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .flatMap(id -> combinedSet.stream().filter(product -> product.getProductId().equals(id)))
                    .collect(Collectors.toList());

            System.out.println(matchedProducts);

            // Nếu có sản phẩm được đề xuất, thêm vào kết quả
            if (!matchedProducts.isEmpty()) {
                result.put("recommends", productMapper.toRecommendProducts(matchedProducts));
            }
        });

        return result;
    }

    @Override
    public Object getTopSeller() {
        return productRepository.getTopSellerProducts(10);
    }


    private Map<String, Double> buildClickProductMap(String userId) {
        Map<String, Double> map = new HashMap<>();
        List<ClickData> clickData = customerRepository.getUserClickProductDetail(userId);

        for (var it : clickData) {
            String productId = it.getProductId();

            if (map.containsKey(productId)) {
                map.put(productId, map.get(productId) + 1);
            } else {
                map.put(productId, 1.0);
            }
        }

        return map;
    }

}
