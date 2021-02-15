package de.otto.codingchallenge.ipranges;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.otto.codingchallenge.ipranges.service.AwsIpRangesService;
import de.otto.codingchallenge.ipranges.service.dto.IpRangeV4Dto;
import de.otto.codingchallenge.ipranges.service.dto.IpRangeV6Dto;
import de.otto.codingchallenge.ipranges.service.dto.IpRangesDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@RestClientTest(AwsIpRangesService.class)
public class ServiceTest {

    @Autowired
    AwsIpRangesService service;
    @Autowired
    MockRestServiceServer mockServer;
    @Autowired
    ObjectMapper mapper;
    @Value("${aws.ipranges.api.url}")
    private String apiUrl;

    @Test
    public void getSpecificRegion() throws Exception {
        List<IpRangeV4Dto> ipRangesV4 = new LinkedList<>();
        ipRangesV4.add(new IpRangeV4Dto("120.33.22.96/27", "eu-west-2"));
        ipRangesV4.add(new IpRangeV4Dto("120.52.21.234/27", "eu-west-3"));
        ipRangesV4.add(new IpRangeV4Dto("45.43.22.96/27", "ap-southeast-2"));
        ipRangesV4.add(new IpRangeV4Dto("144.52.343.96/27", "GLOBAL"));

        List<IpRangeV6Dto> ipRangesV6 = new LinkedList<>();
        ipRangesV6.add(new IpRangeV6Dto("2600:1f70:8000::/56", "eu-west-2"));
        ipRangesV6.add(new IpRangeV6Dto("2600:1f70:6000::/56", "eu-west-3"));
        ipRangesV6.add(new IpRangeV6Dto("2443:545:4556::/56", "ap-southeast-2"));
        ipRangesV6.add(new IpRangeV6Dto("2600:9000:1000::/36", "GLOBAL"));

        String apiResponse = this.mapper.writeValueAsString(new IpRangesDto(ipRangesV4, ipRangesV6));

        this.mockServer.expect(ExpectedCount.once(), requestTo(apiUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(apiResponse));

        String serviceResp = this.service.getIpRangesForRegion(AwsIpRangesService.Region.AP);

        assertThat(serviceResp).isEqualTo("45.43.22.96/27\n2443:545:4556::/56\n");
    }

    @Test
    public void getAllRegions() throws Exception {
        List<IpRangeV4Dto> ipRangesV4 = new LinkedList<>();
        ipRangesV4.add(new IpRangeV4Dto("120.33.22.96/27", "eu-west-2"));
        ipRangesV4.add(new IpRangeV4Dto("120.52.21.234/27", "eu-west-3"));
        ipRangesV4.add(new IpRangeV4Dto("45.43.22.96/27", "ap-southeast-2"));
        ipRangesV4.add(new IpRangeV4Dto("144.52.343.96/27", "GLOBAL"));

        List<IpRangeV6Dto> ipRangesV6 = new LinkedList<>();
        ipRangesV6.add(new IpRangeV6Dto("2600:1f70:8000::/56", "eu-west-2"));
        ipRangesV6.add(new IpRangeV6Dto("2600:1f70:6000::/56", "eu-west-3"));
        ipRangesV6.add(new IpRangeV6Dto("2443:545:4556::/56", "ap-southeast-2"));
        ipRangesV6.add(new IpRangeV6Dto("2600:9000:1000::/36", "GLOBAL"));

        String apiResponse = this.mapper.writeValueAsString(new IpRangesDto(ipRangesV4, ipRangesV6));

        this.mockServer.expect(ExpectedCount.once(), requestTo(apiUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(apiResponse));

        String serviceResp = this.service.getIpRangesForRegion(AwsIpRangesService.Region.ALL);

        assertThat(serviceResp).isEqualTo("120.33.22.96/27\n120.52.21.234/27\n45.43.22.96/27\n144.52.343.96/27\n" +
                "2600:1f70:8000::/56\n2600:1f70:6000::/56\n2443:545:4556::/56\n2600:9000:1000::/36\n");
    }

    @Test
    public void non200Response() {
        this.mockServer.expect(ExpectedCount.once(), requestTo(apiUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(""));

        String serviceResp = this.service.getIpRangesForRegion(AwsIpRangesService.Region.ALL);

        assertThat(serviceResp).isEqualTo("");
    }

    /**
     * This test leads to the creation of empty DTO instances that could potentially lead to null pointer exceptions
     *
     * @throws Exception
     */
    @Test
    public void emptyJsonBodyResponse() {
        this.mockServer.expect(ExpectedCount.once(), requestTo(apiUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}"));

        String serviceResp = this.service.getIpRangesForRegion(AwsIpRangesService.Region.ALL);

        assertThat(serviceResp).isEqualTo("");
    }

    @Test
    public void emptyBodyResponse() {
        this.mockServer.expect(ExpectedCount.once(), requestTo(apiUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(""));

        String serviceResp = this.service.getIpRangesForRegion(AwsIpRangesService.Region.ALL);

        assertThat(serviceResp).isEqualTo("");
    }
}
