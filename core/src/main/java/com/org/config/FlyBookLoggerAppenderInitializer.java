package com.org.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FlyBookLoggerAppenderInitializer implements ApplicationListener<ApplicationReadyEvent> {

//    private final FlyBookLoggerAppenderConfigProperties properties;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
//        if (!this.properties.isEnabled()) {
//            return;
//        }
        // 添加飞书日志Appender
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        FlyBookAppender flyBookAppender = new FlyBookAppender();
        flyBookAppender.setName("FLY_BOOK");
        flyBookAppender.setAlertUrl("");
        flyBookAppender.setContext(loggerContext);
        flyBookAppender.start();

        Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(flyBookAppender);
    }
}