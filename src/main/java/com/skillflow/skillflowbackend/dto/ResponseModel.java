package com.skillflow.skillflowbackend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ResponseModel<T> {

    private final List<T> data;
    private final int pageNo;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean isLastPage;
}