package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.model.Product;
import com.nahudev.electronic_shop.request.AddProductRequest;
import com.nahudev.electronic_shop.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
public class PruevaController {

    @Autowired
    private IProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody AddProductRequest request) {
      return new ResponseEntity<>(productService.addProduct(request), HttpStatus.CREATED);
    }

}
