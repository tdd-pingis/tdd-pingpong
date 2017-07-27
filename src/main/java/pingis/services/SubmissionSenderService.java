package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SubmissionSenderService {
    private static final String SUBMISSION_FILENAME = "submission.tar";

    // TODO: These need to be read from configuration
    // When running in Docker, the sandbox root URI can't be localhost as the
    // sandbox doesn't run in a container.
    private static final String SANDBOX_ROOT_URI = "http://localhost:3001";
    private static final String NOTIFICATION_URI = "http://localhost:8080";

    private RestTemplate restTemplate;

    @Autowired
    public SubmissionSenderService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                // TODO: read this from configuration
                .rootUri("http://localhost:3001")
                .build();
    }

    private HttpEntity<MultiValueMap<String, Object>> buildRequestEntity(byte[] packaged, String notifyToken,
                                                                         String notifyUrl) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        HttpHeaders textHeaders = new HttpHeaders();
        textHeaders.setContentType(MediaType.TEXT_PLAIN);

        HttpHeaders binaryHeaders = new HttpHeaders();
        binaryHeaders.setContentDispositionFormData("file", SUBMISSION_FILENAME);

        map.add("file", new HttpEntity<>(new ByteArrayResource(packaged), binaryHeaders));
        map.add("token", new HttpEntity<>(notifyToken, textHeaders));
        map.add("notify", new HttpEntity<>(notifyUrl, textHeaders));

        return new HttpEntity<>(map);
    }

    public TmcSubmissionResponse sendSubmission(byte[] packaged) {
        // TODO: Token generation
        String token = "123456";

        // TODO: This needs to be configured
        String notifyUrl = NOTIFICATION_URI;

        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildRequestEntity(packaged, token,
                notifyUrl);

        return restTemplate.postForObject("/tasks.json", requestEntity,
                TmcSubmissionResponse.class);
    }
}
