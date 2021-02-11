package de.otto.codingchallenge.ipranges.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class IpRangesController {

    @GetMapping("/")
    public String ipranges(@RequestParam String region) {
        return region;
    }

}
