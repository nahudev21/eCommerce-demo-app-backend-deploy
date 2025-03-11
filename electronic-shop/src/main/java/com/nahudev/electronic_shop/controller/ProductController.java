package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.dto.ProductDTO;
import com.nahudev.electronic_shop.exceptions.AlreadyExistsException;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Product;
import com.nahudev.electronic_shop.request.AddProductRequest;
import com.nahudev.electronic_shop.request.ProductUpdateRequest;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDTO productDTO = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success!", productDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
       try {
           Product addedProduct = productService.addProduct(product);
           ProductDTO productDTO = productService.convertToDto(addedProduct);
           return ResponseEntity.ok(new ApiResponse("Add product success!", productDTO));
       } catch (AlreadyExistsException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body(new ApiResponse(e.getMessage(), null));
       }
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product,
                                                     @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProduct(product, productId);
            ProductDTO productDTO = productService.convertToDto(updatedProduct);
            return ResponseEntity.ok(new ApiResponse("Update product success!", productDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(new ApiResponse("Deleted product success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand,
                                                                @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,
                                                                     @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
                                                                   @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/filter/by/categories")
    public ResponseEntity<ApiResponse> getProductsByCategoryNames(@RequestParam List<String> categoryNames) {
        try {
            List<Product> products = productService.getProductsByCategoryNames(categoryNames);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
