package vn.hust.api.dto.in.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.hust.api.dto.enums.OrderStatusEnum;

@Data
public class UpdateOrderStatus {
    @NotBlank
    private String orderId;
    private OrderStatusEnum currentStatus;
    private OrderStatusEnum orderStatusEnum;
}