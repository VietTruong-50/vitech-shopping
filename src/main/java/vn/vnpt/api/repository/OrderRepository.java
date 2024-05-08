package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.Enum.OrderStatusEnum;
import vn.vnpt.api.dto.out.order.OrderDetailOut;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.exception.NotFoundException;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class OrderRepository {

    private final ProcedureCallerV3 procedureCallerV3;


    public PagingOut<OrderListOut> getCurrentOrders(OrderStatusEnum status, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("order_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_status", String.class, status),
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

    public OrderInformationOut getOrderDetail(String orderCode) {
        var outputs = procedureCallerV3.callOneRefCursor("order_detail_by_code",
                List.of(
                        ProcedureParameter.inputParam("prs_order_code", String.class, orderCode),
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
}
