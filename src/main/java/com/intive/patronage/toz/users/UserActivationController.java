package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.proposals.ProposalService;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserActivationRequestView;
import com.intive.patronage.toz.users.model.view.UserActivationResponseView;
import com.intive.patronage.toz.users.model.view.UserActivationActivateUsingTokenView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;


@Api(tags = "Users", description = "User management operations (CRUD, activating etc.).")
@RestController
@RequestMapping(value = ApiUrl.REGISTER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class UserActivationController {

    private static final String API_METHOD_NOTES = "Required TOZ role";
    private static final String ACTIVATE_SEND_TOKEN = "activateSent";
    private static final String ACTIVATE_USER_PATH = "/activate";
    private static final String USER_ACTIVATED = "userActivated";

    private final MessageSource messageSource;

    private UserActivationService userActivationService;
    private ProposalService proposalService;

    @Autowired
    UserActivationController(UserActivationService userActivationService, ProposalService proposalService, MessageSource messageSource) {
            this.userActivationService = userActivationService;
            this.proposalService = proposalService;
            this.messageSource = messageSource;
    }

    @ApiOperation(value = "Send registration email with token activation", notes = API_METHOD_NOTES)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "Proposal not found", response = ErrorResponse.class),
    })
    @PreAuthorize("hasAuthority('TOZ')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserActivationResponseView sendRegistrationEmail(@Valid @RequestBody UserActivationRequestView userActivationRequestView) throws IOException, MessagingException {

        this.userActivationService.sendActivationEmailIfProposalExists(userActivationRequestView.getUuid());

        final String message = messageSource.getMessage(
                ACTIVATE_SEND_TOKEN, null, LocaleContextHolder.getLocale());

        return new UserActivationResponseView(message);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user using activation token", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(value = ACTIVATE_USER_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserActivationResponseView activateUser(@Valid @RequestBody UserActivationActivateUsingTokenView userActivationActivateUsingTokenView) {

        User user = userActivationService.checkActivationToken(userActivationActivateUsingTokenView.getToken(), userActivationActivateUsingTokenView.getPassword());

        final String message = messageSource.getMessage(
                USER_ACTIVATED, null, LocaleContextHolder.getLocale());

        return new UserActivationResponseView(message);
    }
}
