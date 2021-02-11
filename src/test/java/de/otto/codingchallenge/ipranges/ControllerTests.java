package de.otto.codingchallenge.ipranges;

import de.otto.codingchallenge.ipranges.controller.IpRangesController;
import de.otto.codingchallenge.ipranges.service.AwsIpRangesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IpRangesController.class)
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AwsIpRangesService service;

    private static final String expectedContentType = "text/plain;charset=utf-8";

    @Test
    public void validRegionsMocked() throws Exception {
        String mockedResp = "150.222.81.0/24\n13.34.24.160/27\n";

        for (AwsIpRangesService.Region region : AwsIpRangesService.Region.values()) {
            when(service.getIpRangesForRegion(region)).thenReturn(mockedResp);

            this.mockMvc.perform(get("/").param("region", region.name()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(expectedContentType))
                    .andExpect(content().string(mockedResp));
        }
    }

    @Test
    public void invalidRegionsMocked() throws Exception {
        String mockedResp = "150.222.81.0/24\n13.34.24.160/27\n";
        AwsIpRangesService.Region region = AwsIpRangesService.Region.ALL;
        String[] invalidRegions = {"eu", "EUs", "u", "U", "yz"};

        when(service.getIpRangesForRegion(region)).thenReturn(mockedResp);

        for (String invalidRegion : invalidRegions) {
            this.mockMvc.perform(get("/").param("region", invalidRegion))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(expectedContentType));
        }
    }

    @Test
    public void missingRegionParamMocked() throws Exception {
        String mockedResp = "150.222.81.0/24\n13.34.24.160/27\n";
        AwsIpRangesService.Region region = AwsIpRangesService.Region.ALL;

        when(service.getIpRangesForRegion(region)).thenReturn(mockedResp);

        this.mockMvc.perform(get("/"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(expectedContentType));

    }

}
