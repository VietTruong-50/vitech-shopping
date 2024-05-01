package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.product.ProductFilterIn;
import vn.vnpt.api.dto.out.product.ProductAttributeOut;
import vn.vnpt.api.dto.out.product.ProductDetailOut;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

public interface ProductService {
    PagingOut<ProductListOut> listAllProducts(SortPageIn sortPageIn);
    ProductDetailOut getProductDetail(String productId) ;

    PagingOut<ProductListOut> findProductsByCategory(String categoryId, SortPageIn sortPageIn);

    PagingOut<ProductListOut> findAllBySubCategory(String subCategoryId, SortPageIn sortPageIn);

    PagingOut<ProductListOut> dynamicFilter(ProductFilterIn productFilterIn, SortPageIn sortPageIn);

    PagingOut<ProductListOut>  listAll(SortPageIn sortPageIn);

    Map<String, List<ProductAttributeOut>> listProductAttribute(String productId);

    Object recommend(String categoryId);
}
