package com.intive.patronage.toz.util.validation;

import com.intive.patronage.toz.error.exception.InvalidImageFileException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public final class ImageValidator {
    private static final String IMAGES = "Images ";

    private ImageValidator() {
    }

    public static void validateImageArgument(MultipartFile multipartFile) {
        try (InputStream input = multipartFile.getInputStream()) {
            try {
                ImageIO.read(input).toString();
            } catch (Exception e) {
                throw new InvalidImageFileException(String.format("%s: %s", IMAGES,
                        e.getMessage()));
            }
        } catch (IOException e) {
            throw new InvalidImageFileException(String.format("%s: %s", IMAGES,
                    e.getMessage()));
        }
    }
}
