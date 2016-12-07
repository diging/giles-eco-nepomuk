package edu.asu.diging.gilesecosystem.nepomuk.rest;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.diging.gilesecosystem.nepomuk.rest.domain.Ping;

@RestController
public class PingController {

    @RequestMapping("/ping")
    public Ping ping() {
        Ping ping = new Ping();
        ping.setMessage("Hi, this is Nepomuk. I'm up and running.");
        ping.setTime(new Date().getTime());
        return ping;
    }
}
