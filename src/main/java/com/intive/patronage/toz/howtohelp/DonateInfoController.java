package com.intive.patronage.toz.howtohelp;


import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.howtohelp.model.View.HelpInfoView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType.HOW_TO_DONATE;

@RestController
@RequestMapping(value = ApiUrl.HOW_TO_DONATE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class DonateInfoController extends HelpInfoController {

    @Autowired
    DonateInfoController(HelpInfoService helpInfoService) {
        super(helpInfoService, HOW_TO_DONATE);
    }

    @ApiOperation(value = "Get information on how to donate.", notes =
            "Required roles: SA, TOZ, VOLUNTEER, ANONYMOUS.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "How to donate information not found",
                    response = ErrorResponse.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS')")
    @Override
    public HelpInfoView getHowToHelpInfo() {
        return super.getHowToHelpInfo();
    }

    @ApiOperation(value = "Create how to donate information.", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "How to donate information already exists",
                    response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    @Override
    public ResponseEntity<HelpInfoView> createHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        return super.createHowToHelpInfo(helpInfoView);
    }

    @ApiOperation(value = "Update how to donate information", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "How to donate information not found",
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
