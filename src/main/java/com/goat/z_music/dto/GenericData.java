package com.goat.z_music.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class GenericData<T>  implements Serializable {

    private List<T> data;

    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

    @Serial
    private static final long serialVersionUID = -2299819084342696206L;
}
