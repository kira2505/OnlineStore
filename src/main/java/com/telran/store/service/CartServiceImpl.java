package com.telran.store.service;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.CartItemNotFoundException;
import com.telran.store.exception.CartNotFoundException;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.CartMapper;
import com.telran.store.repository.CartItemRepository;
import com.telran.store.repository.CartRepository;
import com.telran.store.repository.ProductRepository;
import com.telran.store.repository.ShopUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private CartService cartService;

    @Override
    public Cart create(ShopUser user) {
        Cart cart = cartRepository
                .findByUserId(user.getId())
                .orElse(new Cart(user));
        if (cart.getId() != null) {
            return cart;
        }
        return cartRepository.save(cart);
    }

    @Override
    public CartItem add(Long userId, AddToCartRequest cartRequest) {
        ShopUser shopUser = shopUserService.getById(userId);

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
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartRequest.getQuantity());
            cartItem.setPrice(product.getPrice());
            cart.getCartItems().add(cartItem);
        }
        cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
        cart.getCartItems().add(cartItem);
        cartService.save(cart);
        return cartItem;
    }


    @Override
    public Cart edit(Long userId, AddToCartRequest request) {
        Cart cart = getById(userId);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(cartItems -> cartItems.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return cart;
    }

    @Override
    public Cart getById(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() ->
                new CartNotFoundException("Cart by user id " + userId + "not found"));
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getById(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        Cart cart = getById(userId);
        cartRepository.delete(cart);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}
