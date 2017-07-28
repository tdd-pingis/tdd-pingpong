package pingis.services;

import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import pingis.config.SubmissionProperties;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(SubmissionSenderService.class)
@TestPropertySource(properties = {"tmc.sandboxUrl=http://localhost:3001", "tmc.notifyUrl=http://localhost:1337"})
@ContextConfiguration(classes = {SubmissionProperties.class, SubmissionSenderService.class})
public class SubmissionSenderServiceTest {
    @Autowired
    private SubmissionSenderService sender;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void testSubmit() throws IOException, ArchiveException {
        String packageData = "this_is_a_package";

        // This test is *very* limited as Spring doesn't have any easy way to check
        // requests with multipart form data...
        server.expect(requestTo("/tasks.json"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.MULTIPART_FORM_DATA))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        TmcSubmissionResponse response = sender.sendSubmission(packageData.getBytes());

        assertEquals(TmcSubmissionResponse.OK, response.getStatus());
    }
}