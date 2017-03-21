package com.intive.patronage.toz.model.db;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class News extends Identifiable {
    private String title;
    private String contents;
    private NewsType type;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;

    private LocalDateTime published;

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

    public NewsType getType() {
        return type;
    }

    public void setType(NewsType type) {
        this.type = type;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public static class Builder {
        private UUID id;
        private String title;
        private String contents;
        private NewsType type;
        private LocalDateTime created;
        private LocalDateTime lastModified;
        private LocalDateTime published;

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

        public Builder setType(NewsType type) {
            this.type = type;
            return this;
        }

        public Builder setCreated(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public Builder setLastModified(LocalDateTime lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder setPublished(LocalDateTime published) {
            this.published = published;
            return this;
        }

        public News build() {
            return new News(this);
        }
    }
}
