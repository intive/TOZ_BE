package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import com.intive.patronage.toz.model.constant.PetConst;
import com.intive.patronage.toz.exception.InvalidImageFileException;
import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.model.request.PetRequestBody;
import com.intive.patronage.toz.model.view.PetView;
import com.intive.patronage.toz.model.view.UrlView;
import com.intive.patronage.toz.service.PetsService;
import com.intive.patronage.toz.service.StorageProperties;
import com.intive.patronage.toz.service.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Api(description = "Operations for pet resources")
@RestController
@RequestMapping(value = PetConst.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class PetsController {

    private final PetsService petsService;
    private final StorageService storageService;
    private final StorageProperties storageProperties;

    @Autowired
    PetsController(PetsService service, StorageService storageService, StorageProperties storageProperties) {
        this.petsService = service;
        this.storageService = storageService;
        this.storageProperties = storageProperties;
    }

    @ApiOperation(value = "Get all pets", responseContainer = "List")
    @GetMapping
    public List<PetView> getAllPets() {
        return petsService.findAllPets();
    }

    @ApiOperation(value = "Get single pet by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    public PetView getPetById(@ApiParam(required = true) @PathVariable UUID id) {
        return petsService.findById(id);
    }

    @ApiOperation(value = "Create new pet", response = PetView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetView> createPet(@Valid @RequestBody PetRequestBody pet) {
        PetView createdPet = petsService.createPet(pet);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String petLocationString = String.format("%s%s", baseLocation, createdPet.getId());
        final URI location = UriComponentsBuilder.fromUriString(petLocationString).build().toUri();
        return ResponseEntity.created(location)
                .body(createdPet);
    }

    @ApiOperation("Delete pet")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePetById(@PathVariable UUID id) {
        petsService.deletePet(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update pet information", response = PetView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PetView updatePet(@PathVariable UUID id, @Valid @RequestBody PetRequestBody pet) {
        return petsService.updatePet(id, pet);
    }

    @ApiOperation("Upload image")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 422, message = "Invalid image file")
    })
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UrlView> uploadFile(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        validateImageArgument(file);
        final UploadedFile uploadedFile = storageService.store(file);
        petsService.updatePetImageUrl(id, uploadedFile.getPath());
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        UrlView urlView = new UrlView();
        urlView.setUrl(String.format(this.storageProperties.getStoragePathRoot(), uploadedFile.getPath()));
        return ResponseEntity.created(location)
                .body(urlView);
    }

    private void validateImageArgument(MultipartFile multipartFile) {
        try (InputStream input = multipartFile.getInputStream()) {
            if (ImageIO.read(input) == null) {
                throw new InvalidImageFileException("Images");
            }
        } catch (IOException e) {
            throw new InvalidImageFileException(String.format("%s: %s", "Images: ", e.getMessage()));
        }
    }
}
