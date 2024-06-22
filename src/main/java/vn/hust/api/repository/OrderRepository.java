package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.enums.OrderStatusEnum;
import vn.hust.api.dto.in.order.UpdateOrderStatus;
import vn.hust.api.dto.out.order.OrderDetailOut;
import vn.hust.api.dto.out.order.OrderInformationOut;
import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.constant.DatabaseStatus;
import vn.hust.common.exception.ApiErrorException;
import vn.hust.common.exception.NotFoundException;
import vn.hust.common.exception.model.ApiError;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class OrderRepository {

    private final ProcedureCallerV3 procedureCallerV3;


    public PagingOut<OrderListOut> getCurrentOrders(OrderStatusEnum status, String userId, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("order_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_status", String.class, status),
                        ProcedureParameter.inputParam("prs_customer_id", String.class, userId),
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.inputParam("prs_create_date_from", String.class, null),
                        ProcedureParameter.inputParam("prs_create_date_to", String.class, null),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), OrderListOut.class
        );

        var outList = (List<OrderListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public OrderInformationOut getOrderDetail(String orderCode, String userId) {
        var outputs = procedureCallerV3.callOneRefCursor("order_detail_by_code",
                List.of(
                        ProcedureParameter.inputParam("prs_order_code", String.class, orderCode),
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                OrderInformationOut.class
        );
        List<OrderInformationOut> outList = (List<OrderInformationOut>) outputs.get("out_cur");
        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("call order_code_detail failed!");
        }

        return outList.get(0);
    }

    public List<OrderDetailOut> getOrderDetails(String orderCode) {
        var outputs = procedureCallerV3.callOneRefCursor("order_detail_list",
                List.of(
                        ProcedureParameter.inputParam("prs_order_code", String.class, orderCode),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                OrderDetailOut.class
        );
        List<OrderDetailOut> outList = (List<OrderDetailOut>) outputs.get("out_cur");
        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("call order_detail_list failed!");
        }
        return outList;
    }

    public void updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        Boolean confirmRefund = null;

        if (updateOrderStatus.getOrderStatusEnum().equals(OrderStatusEnum.REFUND)) {
            confirmRefund = false;
        }

        var outputs = procedureCallerV3.callNoRefCursor("order_update_status", List.of(
                ProcedureParameter.inputParam("prs_order_id", String.class, updateOrderStatus.getOrderId()),
                ProcedureParameter.inputParam("prs_status", Integer.class, updateOrderStatus.getOrderStatusEnum().value),
                ProcedureParameter.inputParam("prs_confirm_refund", Boolean.class, confirmRefund),
                ProcedureParameter.outputParam("out_result", String.class))
        );
        String result = (String) outputs.get("out_result");
        if (DatabaseStatus.isExist(result)) throw new ApiErrorException(ApiError.fieldExist(result));
        if (!DatabaseStatus.Success.equals(result)) throw new RuntimeException("order_update_status failed!");
    }
}
