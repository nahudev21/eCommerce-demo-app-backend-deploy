package com.nahudev.electronic_shop.service.category;

import com.nahudev.electronic_shop.model.Category;

import java.util.List;

public interface ICategoryService {

    public Category getCategoryById(Long categoryId);

    public Category getCategoryByName(String name);

    public List<Category> getAllCategory();

    public Category addCategories(Category category);

    public Category updateCategory(Category category, Long categoryId);

    public void deleteCategory(Long categoryId);

}
