package com.intive.patronage.toz.status.model;

import com.intive.patronage.toz.base.model.IdentifiableView;
import org.hibernate.validator.constraints.NotEmpty;

public class PetsStatusView extends IdentifiableView{

    @NotEmpty
    private String name;

    @NotEmpty
    private String rgb;

    private boolean isPublic = false;
}
