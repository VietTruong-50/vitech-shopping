package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.service.TagService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

@RequiredArgsConstructor
@RequestMapping("/tag")
@RestController
@Slf4j
public class TagController extends AbstractResponseController {
    private final TagService tagService;

    @GetMapping(value = "/list", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> listTag(@RequestParam(required = false) String categoryId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/shopping/tag/list");
            var rs = tagService.listTag(categoryId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

}
