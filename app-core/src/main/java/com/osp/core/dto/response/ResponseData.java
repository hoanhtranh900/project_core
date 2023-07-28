package com.osp.core.dto.response;

import com.osp.core.exception.Result;
import lombok.*;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

    private int code;
    private String message;
    private T data;
    private T typeOfBody;
    private String path;
    private Date time;

    public ResponseData(Result result) {
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    public ResponseData(T data, Result result) {
        this.data = data;
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    public T getTypeOfBody() {
        if (data != null) {
            T a = (T) data.getClass().getName();
            if(a.equals("org.springframework.data.domain.PageImpl")) {
                if(((org.springframework.data.domain.PageImpl) data).getContent().size()>0){
                    this.typeOfBody = (T) Arrays.asList(((org.springframework.data.domain.PageImpl) data).getContent().get(0).getClass().getDeclaredFields()).stream().map(field -> new TypeDTO( field.getName(), field.getType())).collect(Collectors.toList());
                }
            }
            else {
                this.typeOfBody = (T) Arrays.asList(data.getClass().getDeclaredFields()).stream().map(field -> new TypeDTO( field.getName(), field.getType())).collect(Collectors.toList());
            }
        }
        return typeOfBody;
    }
}

