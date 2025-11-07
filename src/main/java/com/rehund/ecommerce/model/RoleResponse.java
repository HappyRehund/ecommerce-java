package com.rehund.ecommerce.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rehund.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class RoleResponse {
    private Long roleId;
    private String name;
    private String description;

    public static RoleResponse fromRole(Role role){
        return RoleResponse.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}
