package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.service.SubCategoryService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

@RequiredArgsConstructor
@RequestMapping("/sub-category")
@RestController
@Slf4j
public class SubCategoryController extends AbstractResponseController {
    private final SubCategoryService subCategoryService;

    @GetMapping(value = "/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> showAllSubcategory(SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/sub-category/list-filter");
            var rs = subCategoryService.listAllSubCategories(sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/list/category", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> listSubcategoryByCategory(String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/sub-category/list/category");
            var rs = subCategoryService.listSubcategoryByCategory(categoryId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

}
