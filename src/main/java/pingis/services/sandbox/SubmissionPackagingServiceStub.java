package pingis.services.sandbox;

import static pingis.entities.sandbox.RequestedStatus.COMPILE_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TASK_PASSING;
import static pingis.entities.sandbox.RequestedStatus.TESTS_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TESTS_PASSED;

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
public class SubmissionPackagingServiceStub implements SubmissionPackagingServiceInterface {

  @Override
  public byte[] packageSubmission(Map<String, byte[]> additionalFiles)
        throws IOException, ArchiveException {

    for (String fileName : additionalFiles.keySet()) {
      if (fileName.contains("src/Passing")) {
        return TESTS_PASSED.asBytes();

      } else if (fileName.contains("src/Failing")) {
        return TESTS_FAILED.asBytes();

      } else if (fileName.contains("src/Erroring")) {
        return COMPILE_FAILED.asBytes();
      }
    }

    return TASK_PASSING.asBytes();
  }

}
