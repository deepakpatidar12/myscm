package com.myScm.scm.Dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchContactForm {

    private String searchField;
    private String fieldValue;
    private String fromRes;
    private int page;

    public SearchContactForm(String fromRes, int page) {
        this.fromRes = fromRes;
        this.page = page;
    }

}
