package com.intive.patronage.toz.helper;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.helper.model.db.Helper;
import com.intive.patronage.toz.helper.model.view.HelperView;
import com.intive.patronage.toz.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = ApiUrl.HELPERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class HelperController {
    private final HelperService helperService;

    @Autowired
    HelperController(HelperService helperService) {
        this.helperService = helperService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public List<HelperView> getAllHelpers(
            @RequestParam(value = "category", required = false) Helper.Category category) {
        return ModelMapper.convertIdentifiableToView(helperService
                .findAllHelpers(category), HelperView.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public HelperView getHelperById(@PathVariable UUID id) {
        return ModelMapper.convertToView(helperService.findById(id), HelperView.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<HelperView> createHelper(@Valid @RequestBody HelperView helperView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation).body(ModelMapper
                .convertToView(helperService.createHelper(ModelMapper
                        .convertToModel(helperView, Helper.class)), HelperView.class));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<?> deleteHelperById(@PathVariable UUID id) {
        helperService.deleteHelper(id);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<HelperView> updateHelper(@PathVariable UUID id,
                                                   @Valid @RequestBody HelperView helperView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation).body(ModelMapper
                .convertToView(helperService.updateHelper(id, ModelMapper
                        .convertToModel(helperView, Helper.class)), HelperView.class));
    }
}
