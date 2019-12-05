package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);


    @Before
    public void setUp() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }

    @Test
    public void addToCartTest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("iamalsoauser");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("SomeItem");
        mockItem.setPrice(new BigDecimal(2.87));

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(mockItem));

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartWithInvalidUsername() {

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("iamnotanuser");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("SomeItem");
        mockItem.setPrice(new BigDecimal(2.87));

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(mockItem));

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername("iamnotanuser")).thenReturn(null);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartWithInvalidItemIdTest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("iamalsoauser");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("SomeItem");
        mockItem.setPrice(new BigDecimal(2.87));

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("iamalsoauser");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("SomeItem");
        mockItem.setPrice(new BigDecimal(2.87));

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(mockItem));

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartWithInvalidUsernameTest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("iamnotanuser");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("SomeItem");
        mockItem.setPrice(new BigDecimal(2.87));

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(mockItem));

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername("iamnotanuser")).thenReturn(null);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartWithInvalidItemIdTest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("iamalsoauser");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("SomeItem");
        mockItem.setPrice(new BigDecimal(2.87));

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }
}
