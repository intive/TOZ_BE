package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.view.HelpInfoView;
import com.intive.patronage.toz.util.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

abstract class HelpInfoController {

    private final HelpInfoService helpInfoService;

    HelpInfoController(HelpInfoService helpInfoService) {
        this.helpInfoService = helpInfoService;
    }

    public HelpInfoView getHowToHelpInfo() {
        return convertToView(helpInfoService.findHelpInfo());
    }

    public ResponseEntity<HelpInfoView> createHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        final HelpInfoView createdHelpInfoView =
                convertToView(helpInfoService.createHelpInfo(
                        convertToModel(helpInfoView)));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location)
                .body(createdHelpInfoView);
    }

    public ResponseEntity<HelpInfoView> updateHowToHelpInfo(@Valid @RequestBody HelpInfoView helpInfoView) {
        final HelpInfoView updatedHelpInfoView =
                convertToView(helpInfoService.updateHelpInfo(
                        convertToModel(helpInfoView)));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location)
                .body(updatedHelpInfoView);
    }

    private HelpInfoView convertToView(final HelpInfo helpInfo) {
        return ModelMapper.convertToView(helpInfo, HelpInfoView.class);
    }

    private HelpInfo convertToModel(final HelpInfoView helpInfoView) {
        return ModelMapper.convertToModel(helpInfoView, HelpInfo.class);
    }
}
