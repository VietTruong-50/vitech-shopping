package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.enums.OrderStatusEnum;
import vn.vnpt.api.service.OrderService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;


@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
@Slf4j
public class OrderController extends AbstractResponseController {
    private final OrderService orderService;

    @GetMapping(value = "/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getCurrentOrders(@RequestParam OrderStatusEnum status, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/order/list-filter, {}", status);
            var rs = orderService.getCurrentOrders(status, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/detail", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getOrderDetail(@RequestParam String orderCode) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/order/detail");
            var rs = orderService.getOrderDetail(orderCode);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }
}
