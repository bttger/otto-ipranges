package de.otto.codingchallenge.ipranges.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
public class IpRangesController {

    @GetMapping(value = "/", produces = "text/plain;charset=utf-8")
    public String ipRanges(@RequestParam String region) {
        return region;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String param = ex.getParameterName();
        return String.format("missing query string parameter: '%s'", param);
    }

}
