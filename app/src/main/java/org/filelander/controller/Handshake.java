package org.filelander.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.filelander.multicast.MulticastReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Handshake {
    private final MulticastReceiver mr;

    @PostMapping(path = "/handshake")
    @ResponseStatus(HttpStatus.CREATED)
    public void handshake(@RequestBody String resource) {
        log.info("Found Controller");
        mr.getEnabled().getAndSet(false);
    }
}
