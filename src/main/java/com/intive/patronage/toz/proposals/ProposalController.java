package com.intive.patronage.toz.proposals;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.exception.WrongProposalRoleException;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.proposals.model.ProposalView;
import com.intive.patronage.toz.users.model.db.Role;
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

import static com.intive.patronage.toz.util.ModelMapper.convertToView;

@Api(description = "Operations for proposal resource")
@RestController
@RequestMapping(value = ApiUrl.PROPOSAL_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @ApiOperation(value = "Get all proposals", responseContainer = "List", notes =
            "Required roles: SA, TOZ.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public List<ProposalView> getAllProposals() {
        final List<Proposal> proposals = proposalService.findAll();
        return convertToView(proposals, ProposalView.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new proposal", response = ProposalView.class, notes =
            "Required roles: SA, TOZ, ANONYMOUS.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'ANONYMOUS')")
    public ResponseEntity<ProposalView> createProposal(@Valid @RequestBody ProposalView proposalView) {
        validateProposalRoles(proposalView);
        final Proposal proposal = ModelMapper.convertToModel(proposalView, Proposal.class);
        final ProposalView createdProposal = convertToView(proposalService.create(proposal), ProposalView.class);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String proposalLocationString = String.format("%s/%s", baseLocation, createdProposal.getId());
        final URI location = UriComponentsBuilder.fromUriString(proposalLocationString).build().toUri();
        return ResponseEntity.created(location).body(createdProposal);
    }

    @ApiOperation(value = "Update proposal information", response = ProposalView.class, notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Proposal not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ProposalView updateProposal(@PathVariable UUID id, @Valid @RequestBody ProposalView proposalView) {
        final Proposal proposal = ModelMapper.convertToModel(proposalView, Proposal.class);
        return ModelMapper.convertToView(proposalService.update(id, proposal), ProposalView.class);
    }

    @ApiOperation(value = "Delete proposal", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Proposal not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<?> deleteProposal(@PathVariable UUID id) {
        proposalService.delete(id);
        return ResponseEntity.ok().build();
    }

    private void validateProposalRoles(ProposalView proposalView) {
        for (Role role : proposalView.getRoles()) {
            if (!role.equals(Role.VOLUNTEER) && !role.equals(Role.TEMP_HOUSE)) {
                throw new WrongProposalRoleException();
            }
        }
    }
}
