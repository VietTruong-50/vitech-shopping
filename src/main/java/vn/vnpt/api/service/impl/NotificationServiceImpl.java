package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.enums.OrderStatusEnum;
import vn.vnpt.api.dto.out.notification.NotificationListOut;
import vn.vnpt.api.dto.out.order.OrderInformationOut;
import vn.vnpt.api.model.Notification;
import vn.vnpt.api.model.User;
import vn.vnpt.api.repository.NotificationRepository;
import vn.vnpt.api.repository.OrderRepository;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.NotificationService;

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

            OrderInformationOut orderInfOut = orderRepository.getOrderDetail(it.getOrderCode());

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
