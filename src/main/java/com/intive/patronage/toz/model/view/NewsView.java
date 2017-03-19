package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.model.constant.NewsValues;
import com.intive.patronage.toz.model.db.IdentifiableView;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NewsView extends IdentifiableView {
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private String title;
    private String contents;
    private NewsValues.Type type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date lastModified;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date published;

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

    public NewsValues.Type getType() {
        return type;
    }

    public void setType(NewsValues.Type type) {
        this.type = type;
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

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }
}
