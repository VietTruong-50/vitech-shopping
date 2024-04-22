package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.service.CategoryService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

@RequiredArgsConstructor
@RequestMapping("/v1/shopping/category")
@RestController
@Slf4j
public class CategoryController extends AbstractResponseController {

    private final CategoryService categoryService;

    @GetMapping(value = "/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> showAllCategory() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /list-filter");
            var result = categoryService.listAllCategory();
            log.info("[RESPONSE]: res: Success!");
            return result;
        });
    }

    @GetMapping(value = "/menu", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> showMenuCategory() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /menu");
            var result = categoryService.showMenu();
            log.info("[RESPONSE]: res: Success!");
            return result;
        });
    }
}
