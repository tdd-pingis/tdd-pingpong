package pingis.services.sandbox;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author juicyp
 */
interface SubmissionPackagingServiceInterface {

  /**
   * @param additionalFiles A map of filename -> content pairs to include in the package.
   * @return The packaged submission as a TAR archive.
   */
  byte[] packageSubmission(Map<String, byte[]> additionalFiles)
        throws IOException, ArchiveException;
  
}
