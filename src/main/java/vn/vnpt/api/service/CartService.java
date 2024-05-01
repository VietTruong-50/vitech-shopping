package vn.vnpt.api.service;

import vn.vnpt.api.dto.in.cart.AddUpdateItemIn;
import vn.vnpt.api.dto.out.cart.CartDetailOut;

public interface CartService {
    void addItemToCart(AddUpdateItemIn addUpdateItemIn);

    void removeItemFromCart(String productId, String sessionToken);

    CartDetailOut getCartDetail(String sessionToken);

    void loadSessionCartIntoUserCart(String userId, String sessionToken);

    AddUpdateItemIn get(String productId, String sessionToken);

    void update(String productId, String sessionToken, Integer quantity);
}
