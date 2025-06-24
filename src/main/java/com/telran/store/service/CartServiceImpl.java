package com.telran.store.service;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.CartMapper;
import com.telran.store.repository.CartItemRepository;
import com.telran.store.repository.CartRepository;
import com.telran.store.repository.ProductRepository;
import com.telran.store.repository.ShopUserRepository;
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
    public Cart add(Long userId, AddToCartRequest cartRequest) {
        ShopUser shopUser = shopUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Cart cart = create(shopUser);

        Optional<CartItem> existingCartItemOpt = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingCartItemOpt.isPresent()) {
            CartItem cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartRequest.getQuantity());
            newCartItem.setPrice(product.getPrice());
            cart.getCartItems().add(newCartItem);
        }
        return cartRepository.save(cart);
    }


    @Override
    public Cart edit(Cart cart) {
        return null;
    }

    @Override
    public List<CartItem> getById(Long id) {
        return List.of();
    }

    @Override
    public void clearCart(Long id) {

    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
