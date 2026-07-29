package com.petlife.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;

    private Long total;

    private Integer current;

    private Integer size;

    public static <T> PageResult<T> of(List<T> records, Long total, Integer current, Integer size) {
        return new PageResult<>(records, total, current, size);
    }
}