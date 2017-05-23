package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.proposals.ProposalService;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserActivationView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;


@Api(tags = "Users", description = "User management operations (CRUD, activating etc.).")
@RestController
@RequestMapping(value = ApiUrl.REGISTER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class UserActivationController {

    private static final String API_METHOD_NOTES = "Required TOZ role";

    private static final String ACTIVATE_USER_PATH = "/activate_user";

    private UserActivationService userActivationService;
    private ProposalService proposalService;

    @Autowired
    UserActivationController(UserActivationService userActivationService, ProposalService proposalService) {
            this.userActivationService = userActivationService;
            this.proposalService = proposalService;
    }

    @ApiOperation(value = "Send registration email", notes = API_METHOD_NOTES)
    @GetMapping(value = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "Proposal not found", response = ErrorResponse.class),
    })
    @PreAuthorize("hasAuthority('TOZ')")
    public Proposal sendRegistrationEmail(@PathVariable UUID id) throws IOException, MessagingException {
        Proposal proposal = proposalService.findOne(id);
        this.userActivationService.sendActivationEmail(proposal);
        return proposal;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user using activation token", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(value = ACTIVATE_USER_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'ANONYMOUS')")
    public ResponseEntity<User> activateUser(@Valid @RequestBody UserActivationView userActivationView) {

        User user = userActivationService.checkActivationToken(userActivationView.getToken(),userActivationView.getPassword());
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String userLocationString = String.format("%s/%s", baseLocation, user.getId());
        final URI location = UriComponentsBuilder.fromUriString(userLocationString).build().toUri();
        return ResponseEntity.created(location).body(user);
    }
}
