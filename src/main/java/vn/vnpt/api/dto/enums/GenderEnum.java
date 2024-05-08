package vn.vnpt.api.dto.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác");

    private final String value;

    GenderEnum(String value) {
        this.value = value;
    }
}
