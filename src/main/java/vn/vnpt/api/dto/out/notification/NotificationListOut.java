package vn.vnpt.api.dto.out.notification;

import lombok.Data;
import vn.vnpt.api.dto.enums.OrderStatusEnum;

@Data
public class NotificationListOut {
    private String orderId;
    private String orderCode;
    private String image;
    private OrderStatusEnum orderStatusEnum;
    private String message;
    private String createdDate;
}
