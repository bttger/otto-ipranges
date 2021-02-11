package de.otto.codingchallenge.ipranges.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpRangesDto {

    @JsonProperty("prefixes")
    private List<IpRangeV4Dto> ipRangesV4;
    @JsonProperty("ipv6_prefixes")
    private List<IpRangeV6Dto> ipRangesV6;

    public List<IpRangeV4Dto> getIpRangesV4() {
        return ipRangesV4;
    }

    public void setIpRangesV4(List<IpRangeV4Dto> ipRangesV4) {
        this.ipRangesV4 = ipRangesV4;
    }

    public List<IpRangeV6Dto> getIpRangesV6() {
        return ipRangesV6;
    }

    public void setIpRangesV6(List<IpRangeV6Dto> ipRangesV6) {
        this.ipRangesV6 = ipRangesV6;
    }
}
