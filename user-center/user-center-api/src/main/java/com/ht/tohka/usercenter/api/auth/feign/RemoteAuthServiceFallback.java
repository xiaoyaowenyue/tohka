package com.ht.tohka.usercenter.api.auth.feign;

import com.ht.tohka.common.core.Result;
import org.springframework.stereotype.Component;

@Component
public class RemoteAuthServiceFallback implements RemoteAuthService {
    @Override
    public Result decide(String bearerToken, String path, String method) {
        return Result.error(503, "服务超时");
    }
}
