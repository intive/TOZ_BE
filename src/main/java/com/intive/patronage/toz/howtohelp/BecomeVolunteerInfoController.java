package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.howtohelp.model.view.HelpInfoView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Become volunteer info", description = "Information about how to become volunteer.")
@RestController
@RequestMapping(value = ApiUrl.HOW_TO_BECOME_VOLUNTEER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class BecomeVolunteerInfoController extends HelpInfoController {

    BecomeVolunteerInfoController(BecomeVolunteerInfoService becomeVolunteerInfoService) {
        super(becomeVolunteerInfoService);
    }

    @ApiOperation(value = "Get information on how to become volunteer.", notes =
            "Required roles: SA, TOZ, VOLUNTEER, ANONYMOUS.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "How to become volunteer information not found",
                    response = ErrorResponse.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS')")
    @Override
    public HelpInfoView getHowToHelpInfo(
            @Valid @RequestParam(defaultValue = "false") Boolean isShortened) {
        return super.getHowToHelpInfo(isShortened);
    }

    @ApiOperation(value = "Create how to become volunteer information.", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "How to become volunteer information already exists",
                    response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    @Override
    public ResponseEntity<HelpInfoView> createHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        return super.createHowToHelpInfo(helpInfoView);
    }

    @ApiOperation(value = "Update how to become volunteer information", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "How to become volunteer information not found",
                    response = ErrorResponse.class)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    @Override
    public ResponseEntity<HelpInfoView> updateHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        return super.updateHowToHelpInfo(helpInfoView);
    }
}
