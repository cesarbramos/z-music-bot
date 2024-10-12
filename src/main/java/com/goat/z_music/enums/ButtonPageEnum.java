package com.goat.z_music.enums;

import lombok.Getter;

@Getter
public enum ButtonPageEnum {
    PAGE_BACK("page_back"),
    PAGE_FIRST("page_first"),
    PAGE_CANCEL("page_cancel"),
    PAGE_NEXT("page_next"),
    PAGE_LAST("page_last");

    private final String code;

    ButtonPageEnum(String code) {
        this.code = code;
    }

    public static ButtonPageEnum fromCode(String code) {
        for (ButtonPageEnum buttonPageEnum : ButtonPageEnum.values()) {
            if (buttonPageEnum.getCode().equals(code))
                return buttonPageEnum;
        }
        return null;
    }

}
