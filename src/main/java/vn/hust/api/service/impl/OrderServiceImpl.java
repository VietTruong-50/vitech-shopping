package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.enums.OrderStatusEnum;
import vn.hust.api.dto.in.order.UpdateOrderStatus;
import vn.hust.api.dto.out.order.OrderInformationOut;
import vn.hust.api.dto.out.order.OrderListOut;
import vn.hust.api.model.Notification;
import vn.hust.api.model.User;
import vn.hust.api.repository.NotificationRepository;
import vn.hust.api.repository.OrderRepository;
import vn.hust.api.repository.UserRepository;
import vn.hust.api.service.OrderService;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

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
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        var rs = orderRepository.getOrderDetail(orderCode, user.get().getId());
        rs.setOrderDetailOuts(orderRepository.getOrderDetails(orderCode));

        return rs;
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        orderRepository.updateOrderStatus(updateOrderStatus);

        var order = orderRepository.getOrderDetail(updateOrderStatus.getOrderId(), user.get().getId());

       String messageContent = switch (updateOrderStatus.getOrderStatusEnum()) {
            case PENDING:
                yield "Đơn hàng %s đang chờ xác nhận và xử lý".formatted(order.getOrderCode());
            case CONFIRMED_AND_PROCESSING:
                yield "Đơn hàng %s đã xác nhận và chờ xử lý".formatted(order.getOrderCode());
            case SHIPPED:
                yield "Đơn hàng %s đang trong quá trình giao hàng".formatted(order.getOrderCode());
            case COMPLETED:
                yield "Đơn hàng %s đã được giao thành công".formatted(order.getOrderCode());
            case CANCELLED:
                yield "Đơn hàng %s đã bị hủy";
            case REFUND:
                yield "Đơn hàng %s đã được hoàn hàng trở lại cửa hàng".formatted(order.getOrderCode());
            default:
                yield "Trạng thái đơn hàng %s không xác định".formatted(order.getOrderCode());
        };


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
