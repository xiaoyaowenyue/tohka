package com.ht.tohka.usercenter;

import com.ht.tohka.usercenter.sys.event.PmChangeTopic;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

// 消息队列
@EnableBinding(PmChangeTopic.class)
@SpringCloudApplication
@MapperScan({"com.ht.tohka.**.mapper"})
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
