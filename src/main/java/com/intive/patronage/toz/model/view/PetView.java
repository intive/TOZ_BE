package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.validator.EnumValidate;
import com.intive.patronage.toz.model.db.IdentifiableView;
import com.intive.patronage.toz.model.db.Pet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "Pet")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("SpellCheckingInspection")
public class PetView extends IdentifiableView {

    @ApiModelProperty(example = "Burek", position = 1)
    private String name;

    @ApiModelProperty(example = "DOG", position = 2, allowableValues = "DOG,CAT,UNKNOWN", required = true)
    @NotNull
    @EnumValidate(enumClass = Pet.Type.class)
    private String type;

    @ApiModelProperty(example = "MALE", position = 3, allowableValues = "MALE,FEMALE")
    @EnumValidate(enumClass = Pet.Sex.class)
    private String sex;

    @ApiModelProperty(example = "Jamnik niskopodłogowy", position = 4)
    private String description;

    @ApiModelProperty(example = "Most cłowy", position = 5)
    private String address;

    @ApiModelProperty(example = "1490134074968", position = 6)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @ApiModelProperty(example = "1490134074968", position = 7)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }
}
