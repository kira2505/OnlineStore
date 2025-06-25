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
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart create(ShopUser user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public CartItem add(Long userId, AddToCartRequest cartRequest) {
        ShopUser shopUser = shopUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Cart cart = create(shopUser);

        Optional<CartItem> existingCartItemOpt = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        CartItem cartItem;
        if (existingCartItemOpt.isPresent()) {
            cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartRequest.getQuantity());
            cartItem.setPrice(product.getPrice());
            cart.getCartItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        return cartItem;
    }


    @Override
    public Cart edit(Long userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new CartNotFoundException("Cart by user id " + userId + " not found");
        }

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
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new CartNotFoundException("Cart by user id " + userId + "not found");
        }
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new CartNotFoundException("Cart by user id " + userId + "not found");
        }
        cartRepository.delete(cart);
    }
}
