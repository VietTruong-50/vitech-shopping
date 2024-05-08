package vn.vnpt.api.service;

import vn.vnpt.api.dto.enums.OrderStatusEnum;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

public interface OrderService {
    PagingOut<OrderListOut> getCurrentOrders(OrderStatusEnum status, SortPageIn sortPageIn);

    OrderInformationOut getOrderDetail(String orderCode);

}
