package vn.hust.api.service;

import vn.hust.api.dto.out.notification.NotificationListOut;

import java.util.List;

public interface NotificationService {
    List<NotificationListOut> listAllNotification();
}
