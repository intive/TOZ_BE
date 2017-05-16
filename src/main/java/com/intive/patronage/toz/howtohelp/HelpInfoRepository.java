package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;
import org.springframework.data.jpa.repository.JpaRepository;

interface HelpInfoRepository extends JpaRepository<HelpInfo, HelpInfoType> {
}
