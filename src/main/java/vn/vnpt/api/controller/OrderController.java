package vn.vnpt.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.enums.OrderStatusEnum;
import vn.vnpt.api.dto.in.order.UpdateOrderStatus;
import vn.vnpt.api.service.OrderService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.util.Collections;


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

    @PostMapping(value = "/update-status")
    public DeferredResult<ResponseEntity<?>> updateOrderStatus(@RequestBody @Valid UpdateOrderStatus updateOrderStatus) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/order/update-status");
            orderService.updateOrderStatus(updateOrderStatus);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }
}
