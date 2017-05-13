package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;

abstract class HelpInfoService {

    private final static String HOW_TO_HELP_INFO = "How to help information";
    private final HelpInfoRepository helpInfoRepository;
    private final HelpInfoType helpInfoType;

    HelpInfoService(HelpInfoRepository helpInfoRepository, HelpInfoType helpInfoType) {
        this.helpInfoRepository = helpInfoRepository;
        this.helpInfoType = helpInfoType;
    }

    HelpInfo findHelpInfo() {
        throwNotFoundIfDoesntExist(helpInfoType);
        return helpInfoRepository.findOne(helpInfoType);
    }

    HelpInfo createHelpInfo(final HelpInfo helpInfo) {
        throwAlreadyExistsIfFound(helpInfoType);
        helpInfo.setId(helpInfoType);
        return helpInfoRepository.save(helpInfo);
    }

    HelpInfo updateHelpInfo(final HelpInfo updatedHelpInfo) {
        throwNotFoundIfDoesntExist(helpInfoType);
        updatedHelpInfo.setId(helpInfoType);
        return helpInfoRepository.save(updatedHelpInfo);
    }

    private void throwNotFoundIfDoesntExist(HelpInfoType id) {
        if (!helpInfoRepository.exists(id)) {
            throw new NotFoundException(HOW_TO_HELP_INFO);
        }
    }

    private void throwAlreadyExistsIfFound(HelpInfoType id) {
        if (helpInfoRepository.exists(helpInfoType)) {
            throw new AlreadyExistsException(HOW_TO_HELP_INFO);
        }
    }
}
