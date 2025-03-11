package com.nahudev.electronic_shop.service.image;

import com.nahudev.electronic_shop.dto.ImageDTO;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Image;
import com.nahudev.electronic_shop.model.Product;
import com.nahudev.electronic_shop.repository.IImageRepository;
import com.nahudev.electronic_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final IImageRepository imageRepository;

    private final IProductService productService;

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + imageId));
    }

    @Override
    public List<Image> getImageByProductId(Long productId) {
        return imageRepository.findByProductId(productId);
    }

    @Override
    public List<Image> getImages() {
        return imageRepository.findAll();
    }

    @Override
    public void deleteImageById(Long imageId) {
      imageRepository.findById(imageId).ifPresentOrElse(imageRepository::delete, () -> {
          throw new ResourceNotFoundException("No image found with id: " + imageId);
      });
    }

    @Override
    public List<ImageDTO> savedImages(List<MultipartFile> files, Long productId) {

        Product product = productService.getProductById(productId);
        List<ImageDTO> savedImageDTO = new ArrayList<>();

        for (MultipartFile file : files) {

            try {

              Image image = new Image();
              image.setFileName(file.getOriginalFilename());
              image.setFileType(file.getContentType());
              image.setImage(new SerialBlob(file.getBytes()));
              image.setProduct(product);

              String buildDownloadUrl = "api/v1/images/image/download/";
              String downloadUrl = buildDownloadUrl + image.getId();
              image.setDownloadUrl(downloadUrl);
              Image savedImage = imageRepository.save(image);

              image.setDownloadUrl(buildDownloadUrl + savedImage.getId());
              imageRepository.save(savedImage);

              ImageDTO imageDTO = new ImageDTO();
              imageDTO.setImageId(savedImage.getId());
              imageDTO.setImageName(savedImage.getFileName());
              imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
              savedImageDTO.add(imageDTO);

            } catch (IOException | SQLException e) {
              throw new RuntimeException(e.getMessage());
            }
        }

        return savedImageDTO;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        Image image = getImageById(imageId);

        try {

          image.setFileName(file.getName());
          image.setFileType(file.getContentType());
          image.setImage(new SerialBlob(file.getBytes()));

        } catch (IOException | SQLException e) {
          throw new RuntimeException(e.getMessage());
        }

    }
}
