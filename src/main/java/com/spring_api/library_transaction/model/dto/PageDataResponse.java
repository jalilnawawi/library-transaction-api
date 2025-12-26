package com.spring_api.library_transaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageDataResponse<T> {
    int page;
    int limit;
    Integer total;
    Integer lastPage;
    List<T> list;
}
