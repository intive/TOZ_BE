package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.db.IdentifiableView;
import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.validator.EnumValidate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NewsView extends IdentifiableView {
    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String contents;

    @NotNull
    @EnumValidate(enumClass = News.Type.class)
    private String type;

    private String photoUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;

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
