package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import com.intive.patronage.toz.exception.InvalidImageFileException;
import com.intive.patronage.toz.model.ModelMapper;
import com.intive.patronage.toz.model.constant.ApiUrl;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.model.db.UploadedFile;
import com.intive.patronage.toz.model.request.PetRequestBody;
import com.intive.patronage.toz.model.view.PetView;
import com.intive.patronage.toz.model.view.UrlView;
import com.intive.patronage.toz.service.PetsService;
import com.intive.patronage.toz.service.StorageProperties;
import com.intive.patronage.toz.service.StorageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping(value = ApiUrl.PET_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public List<PetView> getAllPets(@RequestParam(value = "admin", required = false) boolean isAdmin) {
        if (isAdmin) {
            final List<Pet> pets = petsService.findAllPets();
            return ModelMapper.convertToView(pets, PetView.class);
        }
        final List<Pet> pets = petsService.findPetsWithFilledFields();
        return ModelMapper.convertToView(pets, PetView.class);
    }

    @ApiOperation(value = "Get single pet by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    public PetView getPetById(@ApiParam(required = true) @PathVariable UUID id) {
        return convertToView(petsService.findById(id));
    }

    private static PetView convertToView(final Pet pet) {
        return ModelMapper.convertToView(pet, PetView.class);
    }

    @ApiOperation(value = "Create new pet", response = PetView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetView> createPet(@Valid @RequestBody PetRequestBody petView) {
        final Pet createdPet = petsService.createPet(convertFromView(petView));
        final PetView createdPetView = convertToView(createdPet);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String petLocationString = String.format("%s%s", baseLocation, createdPetView.getId());
        final URI location = UriComponentsBuilder.fromUriString(petLocationString).build().toUri();
        return ResponseEntity.created(location)
                .body(createdPetView);
    }

    private static Pet convertFromView(final PetView petView) {
        return ModelMapper.convertToModel(petView, Pet.class);
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
    public PetView updatePet(@PathVariable UUID id, @Valid @RequestBody PetRequestBody petView) {
        final Pet pet = convertFromView(petView);
        final Pet updatedPet = petsService.updatePet(id, pet);
        return convertToView(updatedPet);
    }

    @ApiOperation("Upload image")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 422, message = "Invalid image file")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UrlView> uploadFile(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        validateImageArgument(file);
        final UploadedFile uploadedFile = storageService.store(file);
        UrlView urlView = new UrlView();
        urlView.setUrl(String.format("%s/%s", this.storageProperties.getStoragePathRoot(), uploadedFile.getPath()));
        petsService.updatePetImageUrl(id, urlView.getUrl());
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location)
                .body(urlView);
    }

    private void validateImageArgument(MultipartFile multipartFile) {
        try (InputStream input = multipartFile.getInputStream()) {
            try {
                ImageIO.read(input).toString();
            } catch (Exception e) {
                throw new InvalidImageFileException(String.format("%s: %s", "Images ", e.getMessage()));
            }
        } catch (IOException e) {
            throw new InvalidImageFileException(String.format("%s: %s", "Images ", e.getMessage()));
        }
    }
}
