package pingis.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SubmissionPackagingServiceTest {

  private static final int NUM_RANDOM_FILES = 5;
  private static final int RANDOM_FILE_MAX_SIZE = 2 * 1024; // 2KB

  // NOTE: This list should be updated when changing the exercise template!
  private static final Set<String> templateEntries = Stream.of(
      "tmc-langs.jar",
      "manifest.mf",
      "nbproject/private/private.properties",
      "nbproject/genfiles.properties",
      "nbproject/build-impl.xml",
      "nbproject/project.properties",
      "nbproject/project.xml",
      "tmc-run",
      "src/Paaohjelma.java",
      "build.xml",
      ".tmcparams",
      "lib/javassist-3.17.1-GA.jar",
      "lib/cglib-nodep-2.2.2.jar",
      "lib/junit-4.10.jar",
      "lib/objenesis-1.2.jar",
      "lib/powermock-easymock-1.5-full.jar",
      "lib/easymock-3.1.jar",
      "lib/edu-test-utils-0.4.2.jar",
      "lib/testrunner/tmc-junit-runner.jar"
  ).collect(Collectors.toSet());

  private SubmissionPackagingService packagingService;
  private ArchiveInputStream inputArchive;
  private ByteArrayInputStream inputStream;

  private void createStreams(byte[] packaged) throws ArchiveException {
    inputStream = new ByteArrayInputStream(packaged);

    inputArchive = new ArchiveStreamFactory()
        .createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
  }

  private String generateRandomName() {
    Random random = new Random();

    return String.format("testfile%08X", random.nextInt());
  }

  private byte[] generateRandomData(int maxSize) {
    Random random = new Random();

    int size = random.nextInt(maxSize);

    byte[] result = new byte[size];
    random.nextBytes(result);

    return result;
  }

  private Map<String, byte[]> generateRandomFiles(int count) {
    Map<String, byte[]> files = new HashMap<>();

    for (int i = 0; i < count; i++) {
      String name = generateRandomName();
      byte[] data = generateRandomData(RANDOM_FILE_MAX_SIZE);

      files.put(name, data);
    }

    return files;
  }

  @Before
  public void setUpService() {
    packagingService = new SubmissionPackagingService();
  }

  @After
  public void cleanup() throws IOException {
    if (inputArchive != null) {
      inputArchive.close();
      inputArchive = null;
    }

    if (inputStream != null) {
      inputStream.close();
      inputStream = null;
    }
  }

  @Test
  public void packageSizeIsNotZero() throws IOException, ArchiveException {
    byte[] packaged = packagingService.packageSubmission(new HashMap<>());
    assertNotEquals(0, packaged.length);
  }

  @Test
  public void packageContainsAllTemplateFiles() throws ArchiveException, IOException {
    // Create a package with only template files
    byte[] packaged = packagingService.packageSubmission(new HashMap<>());
    createStreams(packaged);

    ArchiveEntry entry = inputArchive.getNextEntry();

    Set<String> foundEntries = new HashSet<>();

    for (; entry != null; entry = inputArchive.getNextEntry()) {
      assertNotEquals(0, entry.getSize());
      foundEntries.add(entry.getName());
    }

    assertEquals(templateEntries, foundEntries);
  }

  @Test
  public void testAdditionalFiles() throws IOException, ArchiveException {
    Map<String, byte[]> randomFiles = generateRandomFiles(NUM_RANDOM_FILES);
    byte[] packaged = packagingService.packageSubmission(randomFiles);

    createStreams(packaged);

    ArchiveEntry entry = inputArchive.getNextEntry();

    for (; entry != null; entry = inputArchive.getNextEntry()) {
      // Skip template files
      if (templateEntries.contains(entry.getName())) {
        continue;
      }

      assertTrue(randomFiles.containsKey(entry.getName()));

      byte[] expectedContent = randomFiles.get(entry.getName());
      byte[] actualContent = new byte[(int) entry.getSize()];
      inputArchive.read(actualContent);

      assertArrayEquals(expectedContent, actualContent);
    }

  }
}
