package vn.hust.api.dto.out.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CartDetailOut {
    private Map<Object, Object> cart;
}
