package com.cheers.framework.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private List<T> list;

    private Long total;

    public PageResult() {
        this.list = new ArrayList<>();
        this.total = 0L;
    }

    public PageResult(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public PageResult(PageResult<T> result) {
        this.list = result.getList();
        this.total = result.getTotal();
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>();
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(new ArrayList<>(), total);
    }

} 