package com.intive.patronage.toz.howtohelp.model.db;

import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class HelpInfo {

    @Id
    @Enumerated(EnumType.STRING)
    private HelpInfoType id;

    private String howToHelpDescription;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
}
