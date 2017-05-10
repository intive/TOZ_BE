package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class HelpInfoService {

    private final static String HOW_TO_HELP_INFO = "How to help information";
    private final HelpInfoRepository helpInfoRepository;

    @Autowired
    HelpInfoService(HelpInfoRepository helpInfoRepository) {
        this.helpInfoRepository = helpInfoRepository;
    }

    HelpInfo findHowToHelpInfo(HelpInfoType helpType) {
        if (!helpInfoRepository.exists(helpType)) {
            throw new NotFoundException(HOW_TO_HELP_INFO);
        }
        return helpInfoRepository.findOne(helpType);
    }

    HelpInfo createHowToHelpInfo(final HelpInfo helpInfo, HelpInfoType helpType) {
        helpInfo.setId(helpType);
        if (helpInfoRepository.exists(helpType)) {
            throw new AlreadyExistsException(HOW_TO_HELP_INFO);
        }
        return helpInfoRepository.save(helpInfo);
    }

    HelpInfo updateHowToHelpInfo(final HelpInfo updatedHelpInfo, HelpInfoType helpType) {
        updatedHelpInfo.setId(helpType);
        if (!helpInfoRepository.exists(helpType)) {
            throw new NotFoundException(HOW_TO_HELP_INFO);
        }
        return helpInfoRepository.save(updatedHelpInfo);
    }
}
