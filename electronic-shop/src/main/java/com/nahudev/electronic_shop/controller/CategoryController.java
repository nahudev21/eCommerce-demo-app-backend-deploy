package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.exceptions.AlreadyExistsException;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Category;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllCategories() {

      try {

          List<Category> categories = categoryService.getAllCategory();
          return ResponseEntity.ok(new ApiResponse("Found!", categories));

      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
      }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {

      try {

          Category category = categoryService.addCategories(name);
          return ResponseEntity.ok(new ApiResponse("Success!", category));
      } catch (AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse(e.getMessage(), null));
      }
  }

  @GetMapping("/category/{categoryId}/category")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {

      try {
          Category category = categoryService.getCategoryById(categoryId);
          return ResponseEntity.ok(new ApiResponse("Found!", category));
      } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), null));
      }
  }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {

        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found!", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/category/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {

        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(new ApiResponse("Deleted!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryId,
                                                      @RequestBody Category category) {

        try {
            Category updatedCategory = categoryService.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
