package de.otto.codingchallenge.ipranges.service;

import de.otto.codingchallenge.ipranges.service.dto.IpRangesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Locale;
import java.util.Optional;

@Service
public class AwsIpRangesService {

    /**
     * Possible regions for the AWS IP-ranges API.
     */
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

    /**
     * Fetches the IP-ranges (in CIDR notation) for a corresponding region from AWS.
     *
     * @param region For which region it should fetch the IP-ranges.
     * @return A String containing all IP-ranges of a particular {@link Region}. Each entry ends with a new line.
     */
    public String getIpRangesForRegion(Region region) {
        IpRangesDto resp = this.restTemplate.getForObject(this.apiUrl, IpRangesDto.class);
        if (resp == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        resp.getIpRanges().stream().filter(range -> {
            if (Region.ALL == region) {
                return true;
            } else {
                Optional<Region> entryRegion = extractRegionFromString(range.getRegion());
                return entryRegion.isPresent() && entryRegion.get() == region;
            }
        }).forEach(range -> builder.append(range.getRange()).append("\n"));

        return builder.toString();
    }

    /**
     * Extracts a {@link Region} from a string, which describes a Region within its first two letters. It can only
     * extract a {@link Region} if the enum holds a corresponding entry.
     *
     * @param region A string with the first two letters describing the region. E.g. 'us-east-2'.
     * @return An Optional object which holds a {@link Region} and that isPresent if a {@link Region} could be
     * extracted from the region parameter. If not, the Optional return value isEmpty.
     */
    private Optional<Region> extractRegionFromString(String region) {
        if (region.equals("GLOBAL")) {
            return Optional.empty();
        }
        String regionPrepared = region.toUpperCase(Locale.ENGLISH).substring(0, 2);

        Region regionReturn;
        try {
            regionReturn = Region.valueOf(regionPrepared);
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }

        return Optional.of(regionReturn);
    }
}
