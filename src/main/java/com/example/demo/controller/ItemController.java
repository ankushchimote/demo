package com.example.demo.controller;

import com.example.demo.dto.ItemRequest;
import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Item> createItem(
            @RequestBody ItemRequest itemRequest) {

        Item createdItem = itemService.createItem(itemRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdItem);
    }
}