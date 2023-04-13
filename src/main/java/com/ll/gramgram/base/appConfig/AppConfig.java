package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    public static long MAX_LIKE;

    @Value("${custom.likeablePerson.max-like}")
    public void setMaxLike(long value) {
        MAX_LIKE = value;
    }
}
