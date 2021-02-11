package de.otto.codingchallenge.ipranges.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class IpRangesController {

    @GetMapping(value = "/", produces = "text/plain;charset=utf-8")
    public String ipRanges(@RequestParam String region) {
        return region;
    }

}
