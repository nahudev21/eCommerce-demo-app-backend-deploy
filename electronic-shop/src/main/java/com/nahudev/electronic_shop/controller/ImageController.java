package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.dto.ImageDTO;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Image;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> savedImage(@RequestParam List<MultipartFile> files,
                                                @RequestParam Long productId) {


        try {
            List<ImageDTO> imageDTOS = imageService.savedImages(files, productId);

            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed!", e.getMessage()));
        }

    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {

        Image image = imageService.getImageById(imageId);

        ByteArrayResource resource = new ByteArrayResource(image.getImage()
                .getBytes(1, (int) image.getImage().length()));

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body(resource);

    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,
                                                   @RequestParam MultipartFile file) {

        try {

            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update success!", null));
            }

        } catch (ResourceNotFoundException e) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Update failed!", HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {

        try {

            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success!", null));
            }

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Delete failed!", HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @GetMapping("/image/download-all")
    public ResponseEntity<List<ByteArrayResource>> downloadImages() throws SQLException {
        List<Image> images = imageService.getImages();
        List<ByteArrayResource> resources = images.stream()
                .map(image -> {
                    try {
                        return new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resources);
    }
}


