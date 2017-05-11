package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType.HOW_TO_DONATE;

@Service
class DonateInfoService extends HelpInfoService {

    @Autowired
    DonateInfoService(HelpInfoRepository helpInfoRepository) {
        super(helpInfoRepository, HOW_TO_DONATE);
    }

    HelpInfo findHelpInfo() {
        return super.findHelpInfo();
    }

    HelpInfo createHelpInfo(final HelpInfo helpInfo) {
        return super.createHelpInfo(helpInfo);
    }

    HelpInfo updateHelpInfo(final HelpInfo updatedHelpInfo) {
        return super.updateHelpInfo(updatedHelpInfo);
    }
}
