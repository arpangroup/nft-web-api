package com.trustai.common.api;
import com.trustai.common.dto.UserHierarchyDto;
import com.trustai.common.dto.UserInfo;
import com.trustai.common.dto.UserMetrics;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/*@FeignClient(
        name = "ServiceName.UserClient",
        url = CommonConstants.BASE_URL,
        path = CommonConstants.PATH_USER_SERVICE,
        configuration = FeignConfig.class
)*/
public interface UserApi {

    @GetMapping
    List<UserInfo> getUsers();


    @GetMapping("/batch")
    List<UserInfo> getUsers(List<Long> userIds);

    @GetMapping("/{userId}")
    UserInfo getUserById(Long userId);

    @PutMapping("/{userId}/{rankCode}")
    void updateRank(@PathVariable Long userId, String rankCode);

    @PutMapping("/updateWalletBalance/{userId}/{updatedNewBalance}")
    void updateWalletBalance(@PathVariable Long userId, @PathVariable BigDecimal updatedNewBalance);

    @GetMapping("/hierarchy/{descendant}")
    List<UserHierarchyDto> findByDescendant(@PathVariable Long descendant);

    @GetMapping("/metrics/{userId}")
    UserMetrics computeMetrics(@PathVariable Long userId);
}
