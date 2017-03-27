package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.db.IdentifiableView;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.model.validator.EnumValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "Pet")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("SpellCheckingInspection")
public class PetView extends IdentifiableView {

    @ApiModelProperty(example = "Burek", position = 1)
    @Size(max = 35)
    private String name;

    @ApiModelProperty(example = "DOG", position = 2, allowableValues = "DOG,CAT", required = true)
    @NotNull
    @EnumValidate(enumClass = Pet.Type.class)
    private String type;

    @ApiModelProperty(example = "MALE", position = 3, allowableValues = "MALE,FEMALE")
    @EnumValidate(enumClass = Pet.Sex.class)
    private String sex;

    @ApiModelProperty(example = "Jamnik niskopodłogowy", position = 4)
    @Size(max = 120)
    private String description;

    @ApiModelProperty(example = "Most cłowy", position = 5)
    @Size(max = 35)
    private String address;

    @ApiModelProperty(example = "1490134074968", position = 6)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @ApiModelProperty(example = "1490134074968", position = 7)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;

    @ApiModelProperty(example = "storage/a5/0d/4d/a50d4d4c-ccd2-4747-8dec-d6d7f521336e.jpg", position = 8)
    private String imageUrl;


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

    public String getImageUrl() {
        return imageUrl;
    }
}
