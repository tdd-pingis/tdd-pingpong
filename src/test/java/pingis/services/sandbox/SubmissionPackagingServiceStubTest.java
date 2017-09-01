package pingis.services.sandbox;

import static org.junit.Assert.assertEquals;
import static pingis.entities.sandbox.RequestedStatus.COMPILE_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TASK_PASSING;
import static pingis.entities.sandbox.RequestedStatus.TESTS_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TESTS_PASSED;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.sandbox.RequestedStatus;

/**
 *
 * @author authority
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SubmissionPackagingServiceStub.class})
public class SubmissionPackagingServiceStubTest {

  @Autowired
  SubmissionPackagingServiceStub submissionPackagingServiceStub;

  @Test
  public void returnsTestsPassedWhenFileContainsSrcPassing()
          throws IOException, ArchiveException {

    drivePackagingStubTest("src/Passing", TESTS_PASSED);
  }

  @Test
  public void returnsTestsFailedWhenFileContainsSrcFailing()
          throws IOException, ArchiveException {

    drivePackagingStubTest("src/Failing", TESTS_FAILED);
  }

  @Test
  public void returnsCompileFailedWhenFileContainSrcErroring()
          throws IOException, ArchiveException {

    drivePackagingStubTest("src/Erroring", COMPILE_FAILED);
  }

  @Test
  public void returnsTaskPassingOtherwise()
          throws IOException, ArchiveException {

    drivePackagingStubTest("any", TASK_PASSING);
  }

  private void drivePackagingStubTest(String filename, RequestedStatus requestedStatus)
          throws IOException, ArchiveException {

    Map<String,byte[]> map = new HashMap<>();
    map.put("SampleFile", new byte[] {1,2,3,4,5});
    map.put("any" + filename + "any", new byte[] {5,4,3,2,1});

    byte[] res = submissionPackagingServiceStub.packageSubmission(map);
    assertEquals(new String(res), new String(requestedStatus.asBytes()));
  }
}
