package de.otto.codingchallenge.ipranges.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpRangeV4Dto implements IpRangeDto {

    @JsonProperty("ip_prefix")
    private String range;
    private String region;

    public IpRangeV4Dto(String range, String region) {
        this.range = range;
        this.region = region;
    }

    @Override
    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
