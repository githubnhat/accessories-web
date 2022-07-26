package com.cdweb.backend.payloads.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PagesRequest {
    private int page;
    private int limit;
    private List<SortRequest> sort = new ArrayList<>();

}
