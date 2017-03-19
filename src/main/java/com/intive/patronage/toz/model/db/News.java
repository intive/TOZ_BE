package com.intive.patronage.toz.model.db;

import com.intive.patronage.toz.model.constant.NewsValues;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

@Entity
public class News extends Identifiable {
    private String title;
    private String contents;
    private NewsValues.Type type;

    @CreatedDate
    @Column(updatable = false)
    private Date created;

    @LastModifiedDate
    @Column(insertable = false)
    private Date lastModified;

    @Column(updatable = false)
    private Date published;

    public News() {
    }

    private News(Builder builder) {
        if (builder.id != null) {
            this.setId(builder.id);
        }
        this.title = builder.title;
        this.contents = builder.contents;
        this.type = builder.type;
        this.created = builder.created;
        this.lastModified = builder.lastModified;
        this.published = builder.published;
    }

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

    public static class Builder {
        private UUID id;
        private String title;
        private String contents;
        private NewsValues.Type type;
        private Date created;
        private Date lastModified;
        private Date published;

        public Builder() {
        }

        public Builder(UUID id) {
            this.id = id;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder setType(NewsValues.Type type) {
            this.type = type;
            return this;
        }

        public Builder setCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder setLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder setPublished(Date published) {
            this.published = published;
            return this;
        }

        public News build() {
            return new News(this);
        }
    }
}
