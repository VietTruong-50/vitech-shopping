package vn.hust.api.service;

import vn.hust.api.dto.in.cart.AddUpdateItemIn;
import vn.hust.api.dto.out.cart.CartDetailOut;

public interface CartService {
    void addItemToCart(AddUpdateItemIn addUpdateItemIn);

    void removeItemFromCart(String productId, String sessionToken);

    CartDetailOut getCartDetail(String sessionToken);

    void loadSessionCartIntoUserCart(String userId, String sessionToken);

    AddUpdateItemIn get(String productId, String sessionToken);

    void update(String productId, String sessionToken, Integer quantity);
}
