package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItemsTest() {
        List<Item> mockItem = Arrays.asList(
                new Item(),
                new Item(),
                new Item()
        );

        when(itemRepository.findAll()).thenReturn(mockItem);

        ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertEquals(mockItem, responseEntity.getBody());
    }

    @Test
    public void getItemByIdTest() {
        Item mockItem = new Item();
        mockItem.setId(1L);
        mockItem.setName("SomeItemName");
        mockItem.setDescription("SomeDescription");
        mockItem.setPrice(new BigDecimal(24.25));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));

        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertEquals(mockItem, responseEntity.getBody());
    }

    @Test
    public void getItemListByNameTest() {
        List<Item> mockItemList = Arrays.asList(
                new Item(),
                new Item(),
                new Item(),
                new Item()
        );

        when(itemRepository.findByName(ArgumentMatchers.any())).thenReturn(mockItemList);

        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("SomeName");
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        verify(itemRepository, times(1)).findByName("SomeName");
    }
}
