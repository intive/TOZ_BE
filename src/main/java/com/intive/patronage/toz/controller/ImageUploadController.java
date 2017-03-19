package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.exception.InvalidImageFileException;
import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.service.StorageProperties;
import com.intive.patronage.toz.service.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@RestController
@RequestMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageUploadController {

    private final static String IMAGES = "Images";

    private final StorageService storageService;

    private final StorageProperties storageProperties;

    @Autowired
    public ImageUploadController(StorageService storageService, StorageProperties storageProperties) {
        this.storageService = storageService;
        this.storageProperties = storageProperties;
    }


    @ApiOperation("Upload image")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 422, message = "Invalid image file")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        validateImageArgument(file);
        final UploadedFile uploadedFile = storageService.store(file);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String imageUrl = String.format("%s/%s/%s", location.toString().replace("/images", ""), this.storageProperties.getStoragePathRoot(), uploadedFile.getPath());

        return ResponseEntity.created(location)
                .body(imageUrl);
    }

    private void validateImageArgument(MultipartFile multipartFile) {
        try (InputStream input = multipartFile.getInputStream()) {
            try {
                ImageIO.read(input).toString();
            } catch (Exception e) {
                throw new InvalidImageFileException(IMAGES);
            }
        } catch (IOException e) {
            throw new InvalidImageFileException(IMAGES);
        }

    }
}
