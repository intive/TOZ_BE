package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UrlView {

    @ApiModelProperty(example = "storage/a9\\2c\\cd\\a92ccd6a-f51c-4ff0-8645-02adff409051.jpg", position = 1)
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }
}
