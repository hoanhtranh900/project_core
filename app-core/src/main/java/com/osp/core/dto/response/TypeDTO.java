package com.osp.core.dto.response;

import lombok.*;

@Getter
@Setter
public class TypeDTO<T> {
    private String name;
    private T type;

    public TypeDTO(String name, T type) {
        this.name = name;
        this.type = type;
    }
}
