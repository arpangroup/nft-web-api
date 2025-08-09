/*
package com.trustai.nft_app.client;

import com.trustai.common.api.TransactionApi;
import com.trustai.common.api.WalletApi;
import com.trustai.common.config.FeignConfig;
import com.trustai.common.constants.CommonConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = CommonConstants.TRANSACTION_SERVICE,
        url = CommonConstants.BASE_URL,
        path = CommonConstants.PATH_TRANSACTION_SERVICE,
        configuration = FeignConfig.class
)
@ConditionalOnProperty(name = "feign.client.enabled.core", havingValue = "true")
public interface CoreTransactionClient extends TransactionApi {
}
*/
