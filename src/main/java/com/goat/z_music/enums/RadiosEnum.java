package com.goat.z_music.enums;

import lombok.Getter;

@Getter
public enum RadiosEnum {
    CRISTIANA("Cristiana", "8610"),
    LA_REINA_CARTAGENA("La Reina Cartagena", "29152"),
    MIX("Mix Radio 103.9", "29156"),
    OLIMPICA_FM_BUCARAMANGA("Olimpica FM Bucaramanga", "29122"),
    OLIMPICA_FM_MEDELLIN("Olimpica FM Medellin", "29128"),
    OLIMPICA_FM_PEREIRA("Olimpica FM Pereira", "27353"),
    RADIO_CHARANGA_LATINA("Radio Charanga Latina", "8236"),
    RADIO_MIL_PANAMA("RadioMil Panama", "9186"),
    RADIO_TIEMPO_BAQ("Radio Tiempo Barranquilla", "9160"),
    RADIO_TIEMPO_BAQ_2("Radio Tiempo Barranquilla #2", "29100"),
    RADIO_TIEMPO_CALI("Radio Tiempo Cali", "29164");

    private final String title;
    private final String code;

    RadiosEnum(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static RadiosEnum fromCode(String code) {
        for (RadiosEnum radiosEnum : RadiosEnum.values()) {
            if (radiosEnum.code.equals(code)) {
                return radiosEnum;
            }
        }
        return null;
    }

}
