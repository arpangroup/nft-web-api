/*
package com.trustai.nft_app.client;

import com.trustai.common.api.UserApi;
import com.trustai.common.config.FeignConfig;
import com.trustai.common.constants.CommonConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = CommonConstants.USER_SERVICE,
        url = CommonConstants.BASE_URL,
        path = CommonConstants.PATH_USER_SERVICE,
        configuration = FeignConfig.class
)
@ConditionalOnProperty(name = "feign.client.enabled.core", havingValue = "true")
public interface CoreUserClient extends UserApi {
}
*/
