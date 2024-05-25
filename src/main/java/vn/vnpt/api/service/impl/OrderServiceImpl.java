package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.enums.OrderStatusEnum;
import vn.vnpt.api.dto.in.order.UpdateOrderStatus;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.dto.out.order.OrderListOut;
import vn.vnpt.api.model.Notification;
import vn.vnpt.api.model.User;
import vn.vnpt.api.repository.NotificationRepository;
import vn.vnpt.api.repository.OrderRepository;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.OrderService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public PagingOut<OrderListOut> getCurrentOrders(OrderStatusEnum status, SortPageIn sortPageIn) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        return orderRepository.getCurrentOrders(status, user.get().getId(), sortPageIn);
    }

    @Override
    public OrderInformationOut getOrderDetail(String orderCode) {
        var rs = orderRepository.getOrderDetail(orderCode);
        rs.setOrderDetailOuts(orderRepository.getOrderDetails(orderCode));

        return rs;
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        orderRepository.updateOrderStatus(updateOrderStatus);

       String messageContent = switch (updateOrderStatus.getOrderStatusEnum()) {
            case PENDING:
                yield "Đơn hàng đang chờ xác nhận và xử lý";
            case CONFIRMED_AND_PROCESSING:
                yield "Đơn hàng đã xác nhận và chờ xử lý";
            case SHIPPED:
                yield "Đơn hàng đang trong quá trình giao hàng";
            case COMPLETED:
                yield "Đơn hàng đã được giao thành công";
            case CANCELLED:
                yield "Đơn hàng đã bị hủy";
            case REFUND:
                yield "Đơn hàng đã được hoàn hàng trở lại cửa hàng";
            default:
                yield "Trạng thái đơn hàng không xác định";
        };

        var order = orderRepository.getOrderDetail(updateOrderStatus.getOrderId());

        Notification notification = new Notification();

        notification.setOrderId(order.getOrderId());
        notification.setOrderCode(order.getOrderCode());
        notification.setCustomerId(order.getUserCreated());
        notification.setMessageContent(messageContent);
        notification.setCreatedDate(LocalDate.now().toString());
        notification.setUpdatedDate(LocalDate.now().toString());

        notificationRepository.save(notification);

    }

}
