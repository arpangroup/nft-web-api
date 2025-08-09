/*
package com.trustai.common.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            //return new ResourceNotFoundException("User not found for the given ID");
            return new ResourceNotFoundException("User", "<userId>");
        }

        // You can add more status-specific handling here (e.g., 400, 500)
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
*/
