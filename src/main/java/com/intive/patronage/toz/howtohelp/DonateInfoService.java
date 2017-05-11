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

    HelpInfo findHowToHelpInfo() {
        return super.findHowToHelpInfo();
    }

    HelpInfo createHowToHelpInfo(final HelpInfo helpInfo) {
        return super.createHowToHelpInfo(helpInfo);
    }

    HelpInfo updateHowToHelpInfo(final HelpInfo updatedHelpInfo) {
        return super.updateHowToHelpInfo(updatedHelpInfo);
    }
}
