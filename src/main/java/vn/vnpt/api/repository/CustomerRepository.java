package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.in.address.CreateUpdateAddressIn;
import vn.vnpt.api.dto.in.review.CreateReviewIn;
import vn.vnpt.api.dto.out.address.AddressDetailOut;
import vn.vnpt.api.dto.out.address.AddressListOut;
import vn.vnpt.api.dto.out.customter.CustomerListOut;
import vn.vnpt.api.dto.out.recommend.ClickData;
import vn.vnpt.api.dto.out.review.ReviewListOut;
import vn.vnpt.api.dto.out.customter.UpdateProfileIn;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.Common;
import vn.vnpt.common.constant.DatabaseStatus;
import vn.vnpt.common.exception.NotFoundException;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CustomerRepository {
    private final ProcedureCallerV3 procedureCallerV3;


    public AddressListOut getDefaultAddress(String userId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("address_default_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), AddressListOut.class
        );
        List<AddressListOut> outList = (List<AddressListOut>) outputs.get("out_cur");

//        if(outList.isEmpty()){
//            throw new NotFoundException("Chưa có địa chỉ");
//        }

        return !Common.isNullOrEmpty(outList) ? outList.get(0) : null;
    }

    public List<AddressListOut> listAllAddress(String userId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("address_list",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), AddressListOut.class
        );

        return (List<AddressListOut>) outputs.get("out_cur");
    }

    public void createNewAddress(CreateUpdateAddressIn addressRequest, String userId) {
        Map<String, Object> outputs = procedureCallerV3.callNoRefCursor("address_create_new",
                List.of(
                        ProcedureParameter.inputParam("prs_receiver_name", String.class, addressRequest.getReceiverName()),
                        ProcedureParameter.inputParam("prs_phone", String.class, addressRequest.getPhone()),
                        ProcedureParameter.inputParam("prs_email", String.class, addressRequest.getEmail()),
                        ProcedureParameter.inputParam("prs_city", String.class, addressRequest.getCity()),
                        ProcedureParameter.inputParam("prs_district", String.class, addressRequest.getDistrict()),
                        ProcedureParameter.inputParam("prs_sub_district", String.class, addressRequest.getSubDistrict()),
                        ProcedureParameter.inputParam("prs_specific_address", String.class, addressRequest.getSpecificAddress()),
                        ProcedureParameter.inputParam("prs_is_default", Boolean.class, addressRequest.getIsLevant()),
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("address_create_new failed!");
    }

    public void updateAddress(String addressId, CreateUpdateAddressIn addressRequest, String userId) {
        Map<String, Object> outputs = procedureCallerV3.callNoRefCursor("address_update",
                List.of(
                        ProcedureParameter.inputParam("prs_address_id", String.class, addressId),
                        ProcedureParameter.inputParam("prs_receiver_name", String.class, addressRequest.getReceiverName()),
                        ProcedureParameter.inputParam("prs_phone", String.class, addressRequest.getPhone()),
                        ProcedureParameter.inputParam("prs_email", String.class, addressRequest.getEmail()),
                        ProcedureParameter.inputParam("prs_city", String.class, addressRequest.getCity()),
                        ProcedureParameter.inputParam("prs_district", String.class, addressRequest.getDistrict()),
                        ProcedureParameter.inputParam("prs_sub_district", String.class, addressRequest.getSubDistrict()),
                        ProcedureParameter.inputParam("prs_specific_address", String.class, addressRequest.getSpecificAddress()),
                        ProcedureParameter.inputParam("prs_is_default", Boolean.class, addressRequest.getIsLevant()),
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("address_update failed!");
    }

    public void deleteAddress(String addressId) {
        Map<String, Object> outputs = procedureCallerV3.callNoRefCursor("address_delete",
                List.of(
                        ProcedureParameter.inputParam("prs_address_id", String.class, addressId),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("address_delete failed!");
    }

    public AddressDetailOut getAddressDetail(String addressId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("address_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_address_id", String.class, addressId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), AddressDetailOut.class
        );

        List<AddressDetailOut> outList = (List<AddressDetailOut>) outputs.get("out_cur");

        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("Address not found!");
        }

        return outList.get(0);
    }

    public void updateProfile(UpdateProfileIn updateProfileIn, String userId) {
        Map<String, Object> outputs = procedureCallerV3.callNoRefCursor("customer_profile_update",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.inputParam("prs_user_name", String.class, updateProfileIn.getUsername()),
                        ProcedureParameter.inputParam("prs_email", String.class, updateProfileIn.getEmail()),
                        ProcedureParameter.inputParam("prs_first_name", String.class, updateProfileIn.getFirstName()),
                        ProcedureParameter.inputParam("prs_last_name", String.class, updateProfileIn.getLastName()),
                        ProcedureParameter.inputParam("prs_full_name", String.class, updateProfileIn.getFullName()),
                        ProcedureParameter.inputParam("prs_phone", String.class, updateProfileIn.getPhone()),
                        ProcedureParameter.inputParam("prs_dob", String.class, updateProfileIn.getDob()),
                        ProcedureParameter.inputParam("prs_gender", String.class, updateProfileIn.getGender()),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("user_update failed!");
    }

    public void createReview(CreateReviewIn createReviewIn, String userId) {
        Map<String, Object> outputs = procedureCallerV3.callNoRefCursor("review_create_new",
                List.of(
                        ProcedureParameter.inputParam("prs_content", String.class, createReviewIn.getContent()),
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.inputParam("prs_product_id", String.class, createReviewIn.getProductId()),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("review_create_new failed!");
    }

    public void deleteReview(String reviewId, String userId) {
        Map<String, Object> outputs = procedureCallerV3.callNoRefCursor("review_delete",
                List.of(
                        ProcedureParameter.inputParam("prs_review_id", String.class, reviewId),
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_result", String.class)
                )
        );
        String result = (String) outputs.get("out_result");
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("review_delete failed!");
    }

    public PagingOut<ReviewListOut> getProductReviews(String productId, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("review_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ReviewListOut.class
        );

        System.out.println(outputs);

        List<ReviewListOut> outList = (List<ReviewListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<CustomerListOut> findAllCustomers(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("customer_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), CustomerListOut.class
        );

        List<CustomerListOut> outList = (List<CustomerListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public List<ClickData> getUserClickProductDetail(String userId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("get_user_click_product_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                          ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ClickData.class
        );

        return (List<ClickData>) outputs.get("out_cur");
    }
}
