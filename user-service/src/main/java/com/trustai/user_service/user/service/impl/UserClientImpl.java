package com.trustai.user_service.user.service.impl;

import com.trustai.common.client.UserClient;
import com.trustai.common.dto.UserInfo;
import com.trustai.user_service.user.mapper.UserMapper;
import com.trustai.user_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {
    private final UserProfileService profileService;
    private final UserMapper mapper;

    @Override
    public List<UserInfo> getUsers(List<Long> userIds) {
        return profileService.getUserByIds(userIds).stream().map(mapper::mapTo).toList();
    }

    @Override
    public UserInfo getUserById(Long userId) {
        var user = profileService.getUserById(userId);
        return mapper.mapTo(user);
    }

    @Override
    public String getRankCode(Long userId) {
        return profileService.getUserById(userId).getRankCode();
    }

    @Override
    public void updateRank(Long userId, String newRankCode) {

    }

}
