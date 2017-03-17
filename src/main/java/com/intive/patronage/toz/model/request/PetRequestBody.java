package com.intive.patronage.toz.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intive.patronage.toz.model.view.PetView;

import java.util.UUID;

public class PetRequestBody extends PetView {

    @JsonIgnore
    @Override
    public void setId(UUID id) {
        super.setId(id);
    }
}
