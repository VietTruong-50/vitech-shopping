package vn.hust.api.service;

import vn.hust.api.dto.in.address.CreateUpdateAddressIn;
import vn.hust.api.dto.in.auth.SignUpRequest;
import vn.hust.api.dto.in.auth.SigninRequest;
import vn.hust.api.dto.in.review.CreateReviewIn;
import vn.hust.api.dto.out.address.AddressDetailOut;
import vn.hust.api.dto.out.address.AddressListOut;
import vn.hust.api.dto.out.auth.ChangePasswordDtoIn;
import vn.hust.api.dto.out.auth.JwtAuthenticationResponse;
import vn.hust.api.dto.out.review.ReviewListOut;
import vn.hust.api.dto.out.customter.ProfileDetailOut;
import vn.hust.api.dto.out.customter.UpdateProfileIn;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;

public interface CustomerService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    void createNewAddress(CreateUpdateAddressIn addressRequest);

    void updateAddress(String addressId, CreateUpdateAddressIn addressRequest);

    void deleteAddress(String addressId);

    AddressListOut getDefaultAddress();

    List<AddressListOut> getAllAddress();

    AddressDetailOut getAddressDetail(String addressId);

    ProfileDetailOut getCurrentProfile();

    void updateProfile(UpdateProfileIn updateProfileIn);

    void createComment(CreateReviewIn createReviewIn);

    void deleteComment(String commentId);

    PagingOut<ReviewListOut> getProductComments(String productId, SortPageIn sortPageIn);

    void changePassword(ChangePasswordDtoIn changePasswordDtoIn);
}
