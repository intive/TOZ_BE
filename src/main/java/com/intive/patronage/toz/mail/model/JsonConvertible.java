package com.intive.patronage.toz.mail.model;

import com.intive.patronage.toz.util.ModelMapper;

public class JsonConvertible {
    public String toJson(){
            return ModelMapper.convertToJsonString(this);
    }
}
