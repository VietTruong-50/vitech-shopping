package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.notification.NotificationListOut;

import java.util.List;

public interface NotificationService {
    List<NotificationListOut> listAllNotification();
}
