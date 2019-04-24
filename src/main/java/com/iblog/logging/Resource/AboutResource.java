package com.iblog.logging.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/about")
public class AboutResource {
    private static final Logger logger = LoggerFactory.getLogger(AboutResource.class);

    @Value("${spring.application.name}")
    private String serverName;
    private final About about = new About.Builder(
            serverName, System.currentTimeMillis())
            .build();

    public static class About {
        private final String serverName;
        private final long startedAt;

        public About(Builder builder) {
            this.serverName = builder.serverName;
            this.startedAt = builder.startedAt;
        }

        public static class Builder {
            public final String serverName;
            public final long startedAt;

            public Builder(String serverName, long startedAt) {
                this.serverName = serverName;
                this.startedAt = startedAt;
            }

            public About build() {
                return new About(this);
            }
        }

        public String getServerName() {
            return serverName;
        }

        public long getStartedAt() {
            return startedAt;
        }
    }

    @GetMapping
    public Mono<About> about() {
        logger.info("server:" + about.serverName
                + " lived:" + (System.currentTimeMillis() - about.startedAt));
        return Mono.justOrEmpty(about);
    }
}
