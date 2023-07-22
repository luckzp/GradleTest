package com.org.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class FlyBookAppender extends AppenderBase<ILoggingEvent> {

    private String alertUrl;

    @Override
    protected void append(ILoggingEvent event) {
        if (!Level.ERROR.equals(event.getLevel())) {
            return;
        }
        String loggerName = event.getLoggerName();
        String message = event.getFormattedMessage();
        String threadName = event.getThreadName();
        Throwable throwable = event.getThrowableProxy() != null ? ((ThrowableProxy) event.getThrowableProxy()).getThrowable() : null;
        String stack = "无";
        if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter, true));
            stack = stringWriter.toString();
        }

        FlyBookCardMessage cardMessage = new FlyBookCardMessage(new FlyBookCardMessageContent(
                FlyBookCardMessageConfig.DEFAULT,
                new FlyBookCardMessageHeader(
                        new FlyBookCardMessageHeaderTitle("错误日志告警"),
                        FlyBookCardMessageHeader.ERROR
                ),

                Lists.newArrayList(
                        new FlyBookCardMessageTextElement(
                                new FlyBookCardMessageTextElementText("**日志类名：**" + loggerName)
                        ),
                        new FlyBookCardMessageTextElement(
                                new FlyBookCardMessageTextElementText("线程名称：" + threadName)
                        ),
                        new FlyBookCardMessageTextElement(
                                new FlyBookCardMessageTextElementText("**日志消息：**" + message)
                        ),
                        new FlyBookCardMessageTextElement(
                                new FlyBookCardMessageTextElementText("异常堆栈：" + stack)
                        )
                )
        ));
        // 这个工具类源码未提供，也就是一个发送POST请求的工具方法
        // 各位根据喜好随便找个工具方法能发POST请求就行了🤣🤣

        HttpRequest.post(this.alertUrl)
                .header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
                .body(JSON.toJSONString(cardMessage))//表单内容//超时，毫秒
                .execute().body();
    }

    @Data
    static abstract class FlyBookBootMessage {
        @JSONField(name = "msg_type")
        private final String msgType;
    }

    @EqualsAndHashCode(callSuper = true)
    static class FlyBookCardMessage extends FlyBookBootMessage {
        private final FlyBookCardMessageContent card;

        public FlyBookCardMessage(FlyBookCardMessageContent card) {
            super("interactive");
            this.card = card;
        }

        public FlyBookCardMessageContent getCard() {
            return card;
        }
    }

    @Data
    static class FlyBookCardMessageContent {
        private final FlyBookCardMessageConfig config;
        private final FlyBookCardMessageHeader header;
        private final List<FlyBookCardMessageElement> elements;
    }

    @Data
    @AllArgsConstructor
    static class FlyBookCardMessageConfig {
        public static final FlyBookCardMessageConfig DEFAULT = new FlyBookCardMessageConfig(true, true);
        private boolean wideScreenMode;
        private boolean enableForward;
    }

    @Data
    static class FlyBookCardMessageHeader {
        public static final String ERROR = "red";
        public static final String WARNING = "orange";
        public static final String SUCCESS = "green";
        public static final String PRIMARY = "blue";
        public static final String GREY = "grey";
        private final FlyBookCardMessageHeaderTitle title;
        private final String template;
    }

    @Data
    static class FlyBookCardMessageHeaderTitle {
        private final String tag = "plain_text";
        private final String content;
    }

    @Data
    static abstract class FlyBookCardMessageElement {
        private final String tag;
    }

    @EqualsAndHashCode(callSuper = true)
    static class FlyBookCardMessageTextElement extends FlyBookCardMessageElement {

        private final FlyBookCardMessageTextElementText text;

        public FlyBookCardMessageTextElement(FlyBookCardMessageTextElementText text) {
            super("div");
            this.text = text;
        }

        public FlyBookCardMessageTextElementText getText() {
            return text;
        }
    }

    @Data
    static class FlyBookCardMessageTextElementText {
        private final String tag = "lark_md";
        private final String content;
    }
}
