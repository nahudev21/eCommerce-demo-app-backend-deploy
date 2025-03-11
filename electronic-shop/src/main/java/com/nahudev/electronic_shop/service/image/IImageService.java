package com.nahudev.electronic_shop.service.image;

import com.nahudev.electronic_shop.dto.ImageDTO;
import com.nahudev.electronic_shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    public Image getImageById(Long imageId);

    public List<Image> getImageByProductId(Long productId);

    public List<Image> getImages();

    public void deleteImageById(Long imageId);

    public List<ImageDTO> savedImages(List<MultipartFile> files, Long productId);

    public void updateImage(MultipartFile file, Long imageId);

}
