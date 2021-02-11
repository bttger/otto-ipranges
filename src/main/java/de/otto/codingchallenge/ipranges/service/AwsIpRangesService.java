package de.otto.codingchallenge.ipranges.service;

import de.otto.codingchallenge.ipranges.service.dto.IpRangesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class AwsIpRangesService {

    public enum Region {
        EU, US, AP, CN, SA, AF, CA, ALL
    }

    @Value("${aws.ipranges.api.url}")
    private String apiUrl;
    @Value("${aws.ipranges.api.timeout}")
    private int timeout;
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(this.timeout))
                .setReadTimeout(Duration.ofMillis(this.timeout))
                .build();
    }

    public String getIpRangesForRegion(Region region) {
        IpRangesDto resp = this.restTemplate.getForObject(this.apiUrl, IpRangesDto.class);
        if (resp == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        resp.getIpRanges().forEach(range -> builder.append(range.getRange()).append(":").append(range.getRegion()).append("\n"));

        return builder.toString();
    }
}
