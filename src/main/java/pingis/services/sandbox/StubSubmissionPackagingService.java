package pingis.services.sandbox;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 *
 * @author juicyp
 */
@Service
@Profile("dev")
public class StubSubmissionPackagingService implements SubmissionPackagingServiceInterface {

  @Override
  public byte[] packageSubmission(Map<String, byte[]> additionalFiles)
        throws IOException, ArchiveException {
    return new byte[0];
  }
  
}
