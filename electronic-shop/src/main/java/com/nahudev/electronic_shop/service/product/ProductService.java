package com.nahudev.electronic_shop.service.product;

import com.nahudev.electronic_shop.dto.ImageDTO;
import com.nahudev.electronic_shop.dto.ProductDTO;
import com.nahudev.electronic_shop.exceptions.AlreadyExistsException;
import com.nahudev.electronic_shop.exceptions.ProductNotFoundException;
import com.nahudev.electronic_shop.model.Category;
import com.nahudev.electronic_shop.model.Image;
import com.nahudev.electronic_shop.model.Product;
import com.nahudev.electronic_shop.repository.ICategoryRepository;
import com.nahudev.electronic_shop.repository.IImageRepository;
import com.nahudev.electronic_shop.repository.IProductRepository;
import com.nahudev.electronic_shop.request.AddProductRequest;
import com.nahudev.electronic_shop.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final IProductRepository productRepository;

    private final ICategoryRepository categoryRepository;

    private final IImageRepository imageRepository;

    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {

        if (productExists(request.getName(), request.getBrand())) {
            throw new AlreadyExistsException(request.getBrand()+ " " + request.getName()+ " already exists!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(category);

        return productRepository.save(createProduct(request, category));
    }

    public boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    public Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getId(),
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getSellingPrice(),
                request.getInventory(),
                request.getStatus(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {

      return  productRepository.findById(productId)
              .map(existingProduct -> updateExistingProduct(existingProduct, request))
              .map(productRepository :: save)
              .orElseThrow(() -> new ProductNotFoundException("Product not found"));

    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setSellingPrice(request.getSellingPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setStatus(request.getStatus());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public void deleteProduct(Long productId) {
      Product productFound = this.getProductById(productId);
      productRepository.delete(productFound);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameIgnoreCaseContaining(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByCategoryNames(List<String> categoryNames) {
        return productRepository.findProductsByCategoryNames(categoryNames);
    }

    @Override
    public List<ProductDTO> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOS = images.stream().map(image -> modelMapper.map(image, ImageDTO.class)).toList();
        productDTO.setImages(imageDTOS);
        return productDTO;
    }

}
