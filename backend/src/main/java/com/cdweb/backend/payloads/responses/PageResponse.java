package com.cdweb.backend.payloads.responses;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int page;
    private int totalPage;
    private List<T> data = new ArrayList<>();
}
