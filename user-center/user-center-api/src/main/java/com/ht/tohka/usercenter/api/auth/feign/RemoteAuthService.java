package com.ht.tohka.usercenter.api.auth.feign;

import com.ht.tohka.common.core.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteAuthService", value = "user-center-service", fallback = RemoteAuthServiceFallback.class)
public interface RemoteAuthService {

    @GetMapping("api/v1/sys/auth")
    public Result decide(@RequestHeader("Authorization") String bearerToken,
                         @RequestParam(value = "path") String path,
                         @RequestParam(value = "method") String method);
}
