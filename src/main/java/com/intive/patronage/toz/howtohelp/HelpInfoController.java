package com.intive.patronage.toz.howtohelp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.howtohelp.model.View.HelpInfoView;
import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

abstract class HelpInfoController {

    private final HelpInfoService helpInfoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    HelpInfoController(HelpInfoService helpInfoService) {
        this.helpInfoService = helpInfoService;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public HelpInfoView getHowToHelpInfo() {
        return convertToView(helpInfoService.findHowToHelpInfo());
    }

    public ResponseEntity<HelpInfoView> createHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        final HelpInfoView createdHelpInfoView =
                convertToView(helpInfoService.createHowToHelpInfo(
                        convertToModel(helpInfoView)));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location)
                .body(createdHelpInfoView);
    }

    public ResponseEntity<HelpInfoView> updateHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        final HelpInfoView updatedHelpInfoView =
                convertToView(helpInfoService.updateHowToHelpInfo(
                        convertToModel(helpInfoView)));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location)
                .body(updatedHelpInfoView);
    }

    private HelpInfoView convertToView(final HelpInfo helpInfo) {
        return objectMapper.convertValue(helpInfo, HelpInfoView.class);
    }

    private HelpInfo convertToModel(final HelpInfoView helpInfoView) {
        return objectMapper.convertValue(helpInfoView, HelpInfo.class);
    }
}
