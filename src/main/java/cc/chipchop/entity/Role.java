package cc.chipchop.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER,
    ADMIN;

//    @JsonCreator
//    public static Role fromString(String value) {
//        return Role.valueOf(value.toUpperCase());
//    }
//
//    @JsonValue
//    public String toValue() {
//        return this.name();
//    }
}
