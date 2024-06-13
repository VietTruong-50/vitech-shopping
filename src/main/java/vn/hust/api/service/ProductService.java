package vn.hust.api.service;

import vn.hust.api.dto.in.product.ProductFilterIn;
import vn.hust.api.dto.out.product.ProductAttributeOut;
import vn.hust.api.dto.out.product.ProductDetailOut;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

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

    Object recommend(String subcategoryId, String categoryId, String productId);

    Object getTopSeller();

    Object getRecentView();
}
