package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.constant.PetValues;
import com.intive.patronage.toz.model.db.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("SpellCheckingInspection")
public class PetView extends IdentifiableView {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    @ApiModelProperty(example = "Burek", position = 1)
    private String name;

    @ApiModelProperty(example = "DOG", position = 2, required = true)
    private PetValues.Type type;

    @ApiModelProperty(example = "MALE", position = 3)
    private PetValues.Sex sex;

    @ApiModelProperty(example = "Jamnik niskopodłogowy", position = 4)
    private String description;

    @ApiModelProperty(example = "Most cłowy", position = 5)
    private String address;

    @ApiModelProperty(example = "14-03-17 13:36:19", position = 6)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date created;

    @ApiModelProperty(example = "16-03-17 19:45:12", position = 7)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date lastModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetValues.Type getType() {
        return type;
    }

    public void setType(PetValues.Type type) {
        this.type = type;
    }

    public PetValues.Sex getSex() {
        return sex;
    }

    public void setSex(PetValues.Sex sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
