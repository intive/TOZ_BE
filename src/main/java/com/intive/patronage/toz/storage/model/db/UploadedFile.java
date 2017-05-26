package com.intive.patronage.toz.storage.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Entity
public class UploadedFile extends Identifiable {

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    private String path;
    private String fileUrl;
}
