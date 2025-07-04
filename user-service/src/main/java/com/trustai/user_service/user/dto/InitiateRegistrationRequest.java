package com.trustai.user_service.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class InitiateRegistrationRequest {
    public String email;
    public String username;
    public String mobile;
}
