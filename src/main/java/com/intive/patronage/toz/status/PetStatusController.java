package com.intive.patronage.toz.status;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.status.model.PetStatus;
import com.intive.patronage.toz.status.model.PetStatusView;
import com.intive.patronage.toz.util.ModelMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.util.ModelMapper.convertIdentifiableToView;
import static com.intive.patronage.toz.util.ModelMapper.convertToView;

@Api(tags = "PetStatus", description = "PetStatus management operations (CRUD).")
@RestController
@RequestMapping(value = ApiUrl.PETS_STATUS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PetStatusController {

    private final PetStatusService petStatusService;

    @Autowired
    PetStatusController(PetStatusService petStatusService) {
        this.petStatusService = petStatusService;
    }

    @ApiOperation(value = "Get all pets status", responseContainer = "List", notes =
            "Required roles: SA, TOZ.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public List<PetStatusView> getAllPetStatuses() {
        final List<PetStatus> statuses = petStatusService.findAll();
        return convertIdentifiableToView(statuses, PetStatusView.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new pets status", response = PetStatusView.class, notes =
            "Required roles: SA, TOZ")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<PetStatusView> createPetStatus(@Valid @RequestBody PetStatusView petStatusView) {
        final PetStatus petStatus = ModelMapper.convertToModel(petStatusView, PetStatus.class);
        final PetStatusView createdStatus = convertToView(petStatusService.create(petStatus), PetStatusView.class);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String statusLocationString = String.format("%s/%s", baseLocation, createdStatus.getId());
        final URI location = UriComponentsBuilder.fromUriString(statusLocationString).build().toUri();
        return ResponseEntity.created(location).body(createdStatus);
    }

    @ApiOperation(value = "Update pets status information", response = PetStatusView.class, notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pets status not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public PetStatusView updatePetStatus(@PathVariable UUID id, @Valid @RequestBody PetStatusView petStatusView) {
        final PetStatus petStatus = ModelMapper.convertToModel(petStatusView, PetStatus.class);
        return ModelMapper.convertToView(petStatusService.update(id, petStatus), PetStatusView.class);
    }

    @ApiOperation(value = "Delete pets status", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "PetsStatus not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<?> deletePetStatus(@PathVariable UUID id) {
        petStatusService.delete(id);
        return ResponseEntity.ok().build();
    }
}
