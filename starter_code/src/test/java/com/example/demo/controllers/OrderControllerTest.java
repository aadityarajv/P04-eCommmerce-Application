package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrderByValidUsernameTest() {
        Cart cartMock = mock(Cart.class);
        cartMock.setItems(new ArrayList<>());

        User userMock = new User();
        userMock.setCart(cartMock);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(userMock);

        ResponseEntity<UserOrder> responseEntity = orderController.submit("iamalsoauser");
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void submitOrderByInvalidUsernameTest() {
        Cart cartMock = mock(Cart.class);
        cartMock.setItems(new ArrayList<>());

        User userMock = new User();
        userMock.setCart(cartMock);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<UserOrder> responseEntity = orderController.submit("iamnotanuser");
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrderHistoryByValidUsernameTest() {
        Cart cartMock = mock(Cart.class);
        cartMock.setItems(new ArrayList<>());

        User userMock = new User();
        userMock.setCart(cartMock);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(userMock);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("iamalsoauser");
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrderHistoryByInvalidUsername() {
        Cart cartMock = mock(Cart.class);
        cartMock.setItems(new ArrayList<>());

        User userMock = new User();
        userMock.setCart(cartMock);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("iamnotanuser");
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

}

