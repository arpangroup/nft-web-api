package com.trustai.common.client;

import com.trustai.common.dto.UserInfo;

import java.util.List;

public interface UserClient {
    List<UserInfo> getUsers(List<Long> userIds);
    UserInfo getUserById(Long userId);
    String getRankCode(Long userId);
    void updateRank(Long userId, String newRankCode);
}
