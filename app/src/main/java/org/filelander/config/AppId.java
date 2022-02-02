package org.filelander.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Component
public class AppId {

    private Long appID;

    @PostConstruct
    public void generateID() {
        appID = ThreadLocalRandom.current().nextLong();
    }
}
