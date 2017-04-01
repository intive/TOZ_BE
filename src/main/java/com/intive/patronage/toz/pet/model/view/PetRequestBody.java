package com.intive.patronage.toz.pet.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
