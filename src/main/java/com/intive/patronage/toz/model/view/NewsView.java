package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.db.IdentifiableView;
import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.validator.EnumValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "News")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NewsView extends IdentifiableView {
    @ApiModelProperty(example = "Do oddziału TOZ trafił nowy pies!", position = 1,
            required = true)
    @NotNull
    @NotEmpty
    private String title;

    @ApiModelProperty(example = "Dzisiaj do naszej placówki w Szczecinie trafił nowy " +
            "pies. Nazywa się Reksio i jest dwuletnim jamnikiem. Został znaleziony w " +
            "pobliżu ogrodów działkowych.", position = 2, required = true)
    @NotNull
    @NotEmpty
    private String contents;

    @ApiModelProperty(example = "RELEASED", allowableValues = "ARCHIVED, RELEASED, UNRELEASED",
            position = 3, required = true)
    @NotNull
    @EnumValidate(enumClass = News.Type.class)
    private String type;

    @ApiModelProperty(example = "storage/a9/2c/a92ccd6a-f51c-4ff0-8645-02adff409051.jpg", position = 4)
    private String photoUrl;

    @ApiModelProperty(example = "1222333444555", position = 5)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @ApiModelProperty(example = "1222333444555", position = 6)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;

    @ApiModelProperty(example = "1222333444555", position = 7)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Long published;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public Long getPublished() {
        return published;
    }

    public void setPublished(Long published) {
        this.published = published;
    }
}
