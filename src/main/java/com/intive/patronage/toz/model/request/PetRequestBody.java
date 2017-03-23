package com.intive.patronage.toz.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intive.patronage.toz.model.view.PetView;
import io.swagger.annotations.ApiModel;

import java.util.UUID;

@ApiModel(value = "Pet")
public class PetRequestBody extends PetView {

    @JsonIgnore
    @Override
    public void setId(UUID id) {
        super.setId(id);
    }
}
