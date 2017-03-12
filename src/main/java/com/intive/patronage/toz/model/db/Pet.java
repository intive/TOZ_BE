package com.intive.patronage.toz.model.db;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Pet extends DbModel {

    private String name;
    private Type type;
    private Sex sex;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public enum Type {
        DOG, CAT;

        @JsonCreator
        public static Type fromString(String key) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum Sex {
        MALE, FEMALE
    }
}
