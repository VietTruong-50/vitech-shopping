package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.address.CreateUpdateAddressIn;
import vn.vnpt.api.dto.in.auth.SignUpRequest;
import vn.vnpt.api.dto.in.auth.SigninRequest;
import vn.vnpt.api.dto.in.review.CreateReviewIn;
import vn.vnpt.api.dto.out.address.AddressDetailOut;
import vn.vnpt.api.dto.out.address.AddressListOut;
import vn.vnpt.api.dto.out.auth.JwtAuthenticationResponse;
import vn.vnpt.api.dto.out.review.ReviewListOut;
import vn.vnpt.api.dto.out.customter.ProfileDetailOut;
import vn.vnpt.api.dto.out.customter.UpdateProfileIn;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

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
}
