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
import vn.vnpt.api.dto.out.product.ProductAttributeOut;
import vn.vnpt.api.dto.out.product.ProductDetailOut;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.api.dto.out.recommend.ClickData;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final KafkaProducerService kafkaProducerService;
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
    public Object recommend(String categoryId) {
        var productData = productRepository.getAllProductByCategory(categoryId, new SortPageIn()).getData();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        if (user.isPresent()) {
            var customerData = customerRepository.findAllCustomers(new SortPageIn()).getData();

            Map<String, Map<String, Double>> viewData = new HashMap<>();

            for (var cus : customerData) {
                Map<String, Double> map = buildClickProductMap(cus.getId());
                viewData.put(cus.getId(), map);
            }

            CollaborativeFiltering cf = new CollaborativeFiltering(viewData, null);

            var recommendations = cf.getUserRecommendations(user.get().getId());

            var list = recommendations.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .toList();

            List<ProductListOut> matchedProducts = new ArrayList<>();

            for (Map.Entry<String, Double> entry : list) {
                String productId = entry.getKey();
                Optional<ProductListOut> matchingProduct = productData.stream()
                        .filter(product -> product.getProductId().equals(productId))
                        .findFirst();
                matchingProduct.ifPresent(matchedProducts::add);
            }

            return matchedProducts;
        } else {
            // If user is not authenticated, return list of products from the same category with limit 5
            return productData.stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }
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
