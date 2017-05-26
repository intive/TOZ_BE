package com.intive.patronage.toz.storage.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "Uploaded File")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UploadedFileView extends IdentifiableView {
    @ApiModelProperty(example = "1490134074968", position = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long createDate;

    @ApiModelProperty(example = "a5/0d/4d/a50d4d4c-ccd2-4747-8dec-d6d7f521336e.jpg", position = 2)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String path;

    @ApiModelProperty(example = "/storage/a5/0d/4d/a50d4d4c-ccd2-4747-8dec-d6d7f521336e.jpg", position = 3)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fileUrl;
}
