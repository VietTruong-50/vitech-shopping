package vn.vnpt.api.service.impl;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.cart.AddUpdateItemIn;
import vn.vnpt.api.dto.out.cart.CartDetailOut;
import vn.vnpt.api.model.User;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.CartService;
import vn.vnpt.common.Common;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service

public class CartServiceImpl implements CartService {

    private final HashOperations<Object, Object, Object> hashOperations;
    private final String CART_KEY = "user:cart";
    private final UserRepository userRepository;
    private final RedisTemplate<Object, Object> redisTemplate;

    public CartServiceImpl(RedisTemplate<Object, Object> template, UserRepository userRepository) {
        this.hashOperations = template.opsForHash();
        this.userRepository = userRepository;
        this.redisTemplate = template;
    }

    @Override
    public void addItemToCart(AddUpdateItemIn addUpdateItemIn) {
        String uuidItem;

        Map<Object, Object> entries = getCartDetail(addUpdateItemIn.getSessionToken()).getCart();

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            AddUpdateItemIn item = (AddUpdateItemIn) entry.getValue();
            if (Objects.equals(item.getProductId(), addUpdateItemIn.getProductId()) && Objects.equals(item.getAttribute(), addUpdateItemIn.getAttribute())) {
                this.update((String) entry.getKey(), addUpdateItemIn.getSessionToken(), item.getQuantity() + 1);
                return;
            }
        }

        if (!Common.isNullOrEmpty(addUpdateItemIn.getSessionToken())) {
            hashOperations.put(CART_KEY + ":" + addUpdateItemIn.getSessionToken(), addUpdateItemIn.getProductId(), addUpdateItemIn);
            return;
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        uuidItem = UUID.randomUUID().toString();

        hashOperations.put(CART_KEY + ":" + user.get().getId(), uuidItem, addUpdateItemIn);
    }

    @Override
    public void removeItemFromCart(String itemId, String sessionToken) {

        if (!Common.isNullOrEmpty(sessionToken)) {
            hashOperations.delete(CART_KEY + ":" + sessionToken, itemId);
            return;
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        hashOperations.delete(CART_KEY + ":" + user.get().getId(), itemId);
    }

    @Override
    public CartDetailOut getCartDetail(String sessionToken) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        return user.map(value -> new CartDetailOut(hashOperations.entries(CART_KEY + ":" + value.getId()))).orElseGet(() -> new CartDetailOut(hashOperations.entries(CART_KEY + ":" + sessionToken)));
    }

    @Override
    public void loadSessionCartIntoUserCart(String userId, String sessionToken) {
        var sessionCart = new CartDetailOut(hashOperations.entries(CART_KEY + ":" + sessionToken));

        if (Boolean.TRUE.equals(redisTemplate.hasKey(CART_KEY + ":" + userId))) {
            redisTemplate.delete(CART_KEY + ":" + userId);
        }

        // Load session cart data into user's cart
        for (var entry : sessionCart.getCart().entrySet()) {
            var it = ((AddUpdateItemIn) entry.getValue());
            it.setSessionToken(null);

            hashOperations.put(CART_KEY + ":" + userId, it.getProductId(), entry.getValue());
        }
        redisTemplate.delete(CART_KEY + ":" + sessionToken);
    }

    @Override
    public AddUpdateItemIn get(String itemId, String sessionToken) {
        if (!Common.isNullOrEmpty(sessionToken)) {
            return (AddUpdateItemIn) hashOperations.get(CART_KEY + ":" + sessionToken, itemId);
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        return (AddUpdateItemIn) hashOperations.get(CART_KEY + ":" + user.get().getId(), itemId);
    }

    @Override
    public void update(String itemId, String sessionToken, Integer quantity) {
        var item = get(itemId, sessionToken);

        if (!Common.isNullOrEmpty(item)) {
            int currentQuantity = item.getQuantity();

            if (!Common.isNullOrEmpty(quantity)) {
                currentQuantity = quantity;
            }

            item.setQuantity(currentQuantity);

            if (item.getQuantity() == 0) {
                removeItemFromCart(item.getProductId(), sessionToken);
                return;
            }

            if (!Common.isNullOrEmpty(sessionToken)) {
                hashOperations.put(CART_KEY + ":" + sessionToken, itemId, item);
                return;
            }

            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            Optional<User> user = userRepository.findByEmail(authentication.getName());

            hashOperations.put(CART_KEY + ":" + user.get().getId(), itemId, item);
        }
    }
}
