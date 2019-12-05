package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private UserController userController;


    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUserWithShortPassword() {

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("iamalsoauser");
        userRequest.setPassword("short");
        userRequest.setConfirmPassword("short");

        ResponseEntity<User> responseEntity = userController.createUser(userRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void createUserWithWrongConfirmPassword(){

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("iamalsoauser");
        userRequest.setPassword("wrongpassword");
        userRequest.setConfirmPassword("thisiswrong");

        ResponseEntity<User> responseEntity = userController.createUser(userRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void createUserWithSuccess() {

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("iamalsoauser");
        userRequest.setPassword("thisiscorrect");
        userRequest.setConfirmPassword("thisiscorrect");

        ResponseEntity<User> responseEntity = userController.createUser(userRequest);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assert user != null;
        Assert.assertEquals(user.getUsername(), userRequest.getUsername());
    }

    @Test
    public void findByUserName() {

        User user = new User();
        user.setId(1l);
        user.setUsername("iamalsoauser");
        user.setPassword("thisiscorrect");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("iamalsoauser")).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.findByUserName(user.getUsername());
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        User getUser = responseEntity.getBody();
        Assert.assertEquals(getUser.getUsername(), user.getUsername());
    }

    @Test
    public void findUserById() {

        User user = new User();
        user.setId(1L);
        user.setUsername("iamalsoauser");
        user.setPassword("thisiscorrect");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("iamalsoauser")).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.findById(1L);
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        User getUser = responseEntity.getBody();
        Assert.assertEquals(getUser.getUsername(), user.getUsername());
        Assert.assertEquals(getUser.getId(), user.getId());
    }

}
