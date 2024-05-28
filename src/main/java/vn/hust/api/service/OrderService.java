package vn.hust.api.service;

import vn.hust.api.dto.enums.OrderStatusEnum;
import vn.hust.api.dto.in.order.UpdateOrderStatus;
import vn.hust.api.dto.out.order.OrderInformationOut;
import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

public interface OrderService {
    PagingOut<OrderListOut> getCurrentOrders(OrderStatusEnum status, SortPageIn sortPageIn);

    OrderInformationOut getOrderDetail(String orderCode);

    void updateOrderStatus(UpdateOrderStatus updateOrderStatus);
}
