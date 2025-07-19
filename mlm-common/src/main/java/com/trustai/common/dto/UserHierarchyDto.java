package com.trustai.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserHierarchyDto {
    private Long id;
    private Long ancestor;
    private Long descendant;
    private int depth;
    private boolean active;
}
