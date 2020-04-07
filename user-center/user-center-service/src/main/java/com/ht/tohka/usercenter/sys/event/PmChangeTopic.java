package com.ht.tohka.usercenter.sys.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface PmChangeTopic {
    String INPUT = "pmChangeInput";
    String OUTPUT = "pmChangeOutput";

    @Input(INPUT)
    SubscribableChannel input();

    @Output(OUTPUT)
    MessageChannel output();
}
