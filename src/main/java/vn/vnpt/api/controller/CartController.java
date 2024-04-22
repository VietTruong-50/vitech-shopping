package vn.vnpt.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.cart.AddUpdateItemIn;
import vn.vnpt.api.service.CartService;
import vn.vnpt.common.AbstractResponseController;

import java.util.Collections;

@RequiredArgsConstructor
@RequestMapping("/v1/shopping/cart")
@RestController
@Slf4j
public class CartController extends AbstractResponseController {
    private final CartService cartService;

    @PostMapping(value = "/add", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> addItemToCart(@RequestBody AddUpdateItemIn addUpdateItemIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/cart/add");
            cartService.addItemToCart(addUpdateItemIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/update", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> updateCart(@RequestParam(required = false) String sessionToken, @RequestParam String productId, @RequestParam(required = false) int quantity) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/cart/update");
            cartService.update(productId ,sessionToken, quantity);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/delete", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> removeItemFromCart(@RequestParam String productId, @RequestParam(required = false) String sessionToken) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/cart/delete");
            cartService.removeItemFromCart(productId, sessionToken);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/detail", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getCartDetail(@RequestParam(required = false) String sessionToken) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/cart/detail");
            var rs = cartService.getCartDetail(sessionToken);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }
}
