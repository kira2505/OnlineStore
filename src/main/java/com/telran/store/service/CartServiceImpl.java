package com.telran.store.service;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.CartItemNotFoundException;
import com.telran.store.exception.CartNotFoundException;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.repository.CartItemRepository;
import com.telran.store.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopUserService shopUserService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart create(ShopUser user) {
        Cart cart = cartRepository
                .findByUserId(user.getId())
                .orElse(new Cart(user));
        if (cart.getId() != null) {
            return cart;
        }
        log.debug("Creating new cart for user {}", user.getId());
        return cartRepository.save(cart);
    }

    @Override
    public CartItem add(AddToCartRequestDto cartRequest) {
        ShopUser shopUser = shopUserService.getById(shopUserService.getShopUser().getId());
        Product product = productService.getById(cartRequest.getProductId());

        Cart cart = create(shopUser);

        CartItem cartItem = null;
        for (CartItem cartItemEntity : cart.getCartItems() ) {
            if (cartItemEntity.getProduct().getId().equals(product.getId())) {
                cartItem = cartItemEntity;
                break;
            }
        }

        if (cartItem == null) {
            cartItem = new CartItem(cart,product, cartRequest.getQuantity());
            BigDecimal discount = cartItem.getProduct().getDiscountPrice();
            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                cartItem.setPrice(discount);
            } else {
                cartItem.setPrice(product.getPrice());
            }
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
        }
        cartItemRepository.save(cartItem);
        this.save(cart);
        log.debug("Added a new cart item for user {}", cart.getUser().getId());
        return cartItem;
    }


    @Override
    public Cart edit(AddToCartRequestDto request) {
        Cart cart = getById();

        CartItem cartItem = cart.getCartItems().stream()
                .filter(cartItems -> cartItems.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        cartItem.setQuantity(request.getQuantity());
        log.debug("Cart item edited: {}", cartItem);
        return save(cart);
    }

    @Override
    public Cart getById() {
        return cartRepository.findByUserId(shopUserService.getShopUser().getId())
                .orElseThrow(() -> new CartNotFoundException("Cart by user id " + shopUserService.getShopUser().getId() + " not found"));
    }

    @Override
    public void clearCart() {
        Cart cart = getById();
        cart.getCartItems().clear();
        cartRepository.save(cart);
        log.info("Deleted all cart items for user: {}", cart.getUser().getId());
    }

    @Override
    @Transactional
    public void deleteById() {
        Cart cart = getById();
        cartRepository.delete(cart);
        log.debug("Deleted carts for user: {}", cart.getUser().getId());
    }

    @Transactional
    @Override
    public void deleteCartItem(Long productId) {
        Cart cart = getById();
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                cartItem = item;
                break;
            }
        }
        if (cartItem == null) {
            throw new CartItemNotFoundException("Cart item not found in cart");
        }
        cartItems.remove(cartItem);
        log.info("Deleted a cart item for user {}", cart.getUser().getId());
    }

    @Override
    public Cart save(Cart cart) {
        log.debug("Saving cart for user {}", cart.getUser().getId());
        return cartRepository.save(cart);
    }
}
