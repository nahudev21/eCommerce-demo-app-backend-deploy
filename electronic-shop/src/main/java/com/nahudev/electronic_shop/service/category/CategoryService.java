package com.nahudev.electronic_shop.service.category;

import com.nahudev.electronic_shop.exceptions.AlreadyExistsException;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Category;
import com.nahudev.electronic_shop.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final ICategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new
                ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategories(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save).orElseThrow(() -> new
                        AlreadyExistsException(category.getName()+" already Exists!"));
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(getCategoryById(categoryId)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategory(Long categoryId) {

        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });

    }
}
