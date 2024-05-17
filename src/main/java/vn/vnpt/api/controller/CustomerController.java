package vn.vnpt.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import vn.vnpt.api.dto.in.address.CreateUpdateAddressIn;
import vn.vnpt.api.dto.in.auth.SignUpRequest;
import vn.vnpt.api.dto.in.auth.SigninRequest;
import vn.vnpt.api.dto.in.review.CreateReviewIn;
import vn.vnpt.api.dto.out.auth.ChangePasswordDtoIn;
import vn.vnpt.api.dto.out.customter.UpdateProfileIn;
import vn.vnpt.api.service.CustomerService;
import vn.vnpt.api.service.NotificationService;
import vn.vnpt.common.AbstractResponseController;
import vn.vnpt.common.model.SortPageIn;

import java.util.Collections;

@RequiredArgsConstructor
@RequestMapping("/customer")
@RestController
@Slf4j
public class CustomerController extends AbstractResponseController {

    private final CustomerService customerService;
    private final NotificationService notificationService;

    @PostMapping(value = "/auth/login", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> login(@RequestBody SigninRequest signinRequest){
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/comment/add");
            var rs = customerService.signin(signinRequest);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/auth/register", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> register(@RequestBody SignUpRequest signUpRequest){
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/register");
            var rs = customerService.signup(signUpRequest);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }


    @PostMapping(value = "/auth/change-password", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> changePassword(@RequestBody ChangePasswordDtoIn changePasswordDtoIn){
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/auth/register");
            customerService.changePassword(changePasswordDtoIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }


    @PostMapping(value = "/comment/add", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> createComment(@RequestBody CreateReviewIn createReviewIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/comment/add");
            customerService.createComment(createReviewIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/comment/delete", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> deleteComment(@RequestParam String commentId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/comment/delete");
            customerService.deleteComment(commentId);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/comment/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getProductComments(@RequestParam String productId, SortPageIn sortPageIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/comment/list-filter");
            var rs = customerService.getProductComments(productId, sortPageIn);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        }); }

    @GetMapping(value = "/profile", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getCurrentProfile() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/profile");
            var rs = customerService.getCurrentProfile();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @PostMapping(value = "/profile/update", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> updateProfile(@RequestBody UpdateProfileIn updateProfileIn) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/profile/update");
            customerService.updateProfile(updateProfileIn);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/address/add", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> createNewAddress(@Valid @RequestBody CreateUpdateAddressIn addressRequest) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/address/add");
            customerService.createNewAddress(addressRequest);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/address/update", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> editAddress(@RequestParam String addressId, @Valid @RequestBody CreateUpdateAddressIn addressRequest) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/address/update");
            customerService.updateAddress(addressId, addressRequest);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @PostMapping(value = "/address/delete", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> deleteAddress(@RequestParam String addressId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/address/delete");
            customerService.deleteAddress(addressId);
            log.info("[RESPONSE]: res: Success!");
            return Collections.emptyMap();
        });
    }

    @GetMapping(value = "/address/default", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getDefaultAddress() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/address/default");
            var rs = customerService.getDefaultAddress();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/address/list-filter", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getAllAddress() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/address/list-filter");
            var rs = customerService.getAllAddress();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/address/detail", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getAddressDetail(@RequestParam String addressId) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/address/detail");
            var rs = customerService.getAddressDetail(addressId);
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }

    @GetMapping(value = "/notification/list", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> getAllNotification() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]: path: /v1/customer/notification/list");
            var rs = notificationService.listAllNotification();
            log.info("[RESPONSE]: res: Success!");
            return rs;
        });
    }
}
