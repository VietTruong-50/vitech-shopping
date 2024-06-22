package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.enums.OrderStatusEnum;
import vn.hust.api.dto.out.notification.NotificationListOut;
import vn.hust.api.dto.out.order.OrderInformationOut;
import vn.hust.api.model.Notification;
import vn.hust.api.model.User;
import vn.hust.api.repository.NotificationRepository;
import vn.hust.api.repository.OrderRepository;
import vn.hust.api.repository.UserRepository;
import vn.hust.api.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationListOut> listAllNotification() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        List<Notification> listNotification = notificationRepository.findAllByCustomerId(user.get().getId());

        List<NotificationListOut> notificationListOuts = new ArrayList<>();

        for (var it: listNotification){
            NotificationListOut notificationListOut = new NotificationListOut();

            OrderInformationOut orderInfOut = orderRepository.getOrderDetail(it.getOrderCode(), user.get().getId());

            notificationListOut.setOrderCode(orderInfOut.getOrderCode());
            notificationListOut.setOrderId(orderInfOut.getOrderId());
            notificationListOut.setOrderStatusEnum(OrderStatusEnum.valueOf(orderInfOut.getStatus()));
            notificationListOut.setMessage(it.getMessageContent());
            notificationListOut.setCreatedDate(it.getCreatedDate());

            notificationListOuts.add(notificationListOut);
        }

        return notificationListOuts;
    }
}
