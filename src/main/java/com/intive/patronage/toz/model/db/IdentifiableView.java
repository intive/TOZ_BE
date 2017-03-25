package com.intive.patronage.toz.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

public abstract class IdentifiableView {

    @ApiModelProperty(example = "c5296892-347f-4b2e-b1c6-6faff971f767", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
