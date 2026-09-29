package com.example.demo.service;

import com.example.demo.dto.ItemRequest;
import com.example.demo.model.Item;
import com.example.demo.model.Product;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;

    public ItemService(ItemRepository itemRepository,
                       ProductRepository productRepository) {
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
    }

    public Item createItem(ItemRequest itemRequest) {

        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + itemRequest.getProductId()));

        Item item = new Item();

        item.setQuantity(itemRequest.getQuantity());
        item.setProduct(product);

        return itemRepository.save(item);
    }
}