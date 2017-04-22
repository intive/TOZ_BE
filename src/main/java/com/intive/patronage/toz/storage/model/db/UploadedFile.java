package com.intive.patronage.toz.storage.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@Entity
public class UploadedFile extends Identifiable {
    private Date createDate;
    private String path;
}
