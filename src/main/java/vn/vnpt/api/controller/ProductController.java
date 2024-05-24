package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.product.ProductFilterIn;
import vn.vnpt.api.service.ProductService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

@RequiredArgsConstructor
@RequestMapping("/product")
@RestController
@Slf4j
public class ProductController extends AbstractResponseController {


    private final ProductService productService;

    @GetMapping(value = "/detail", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> showProductDetail(@RequestParam String productId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/detail");
            var rs = productService.getProductDetail(productId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/detail/attribute", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> listProductAttribute(@RequestParam String productId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/detail/attribute");
            var rs = productService.listProductAttribute(productId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/sub-category/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> findProductsBySubcategory(@RequestParam String subCategoryId,
                                                                       SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/subCategory/list-filter");
            var rs = productService.findAllBySubCategory(subCategoryId, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/category/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> findProductsByCategory(@RequestParam String categoryId,
                                                                    SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/products/category/list-filter");
            var rs = productService.findProductsByCategory(categoryId, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/search", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> search(SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/search");
            var rs = productService.listAllProducts(sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/dynamic-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> dynamicFilter(@RequestBody ProductFilterIn productFilterIn, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/dynamic-filter");
            var rs = productService.dynamicFilter(productFilterIn, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/recommend", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> recommend(@RequestParam String productId, @RequestParam String subcategoryId, @RequestParam String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/recommend");
            var rs = productService.recommend(subcategoryId, categoryId, productId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/top-seller", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getTopSeller() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/product/top-seller");
            var rs = productService.getTopSeller();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

}
