package vn.vnpt.api.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.product.ProductFilterIn;
import vn.vnpt.api.dto.out.product.ProductAttributeOut;
import vn.vnpt.api.dto.out.product.ProductDetailOut;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.api.repository.ProductRepository;
import vn.vnpt.api.service.ProductService;
import vn.vnpt.api.service.helper.KafkaProducerService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    @Override
    public PagingOut<ProductListOut> listAllProducts(SortPageIn sortPageIn) {
        return productRepository.getAllProducts(sortPageIn);
    }


    @Override
    public ProductDetailOut getProductDetail(String productId) {
        var rs = productRepository.getProductDetail(productId);

        try {
            rs.setParameters(objectMapper.readValue(rs.getParametersJson(), new TypeReference<>() {}));

//            kafkaProducerService.sendMessage("", rs);
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
}
