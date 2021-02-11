package de.otto.codingchallenge.ipranges.controller;

import de.otto.codingchallenge.ipranges.service.AwsIpRangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
public class IpRangesController {

    private final AwsIpRangesService ipRangesService;

    @Autowired
    public IpRangesController(AwsIpRangesService ipRangesService) {
        this.ipRangesService = ipRangesService;
    }

    @GetMapping(value = "/", produces = "text/plain;charset=utf-8")
    public String ipRanges(@RequestParam AwsIpRangesService.Region region) {
        return this.ipRangesService.getIpRangesForRegion(region);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String param = ex.getParameterName();
        return String.format("missing query string parameter: '%s'", param);
    }

}
