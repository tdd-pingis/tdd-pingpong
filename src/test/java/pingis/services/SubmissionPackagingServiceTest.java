package pingis.services;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SubmissionPackagingServiceTest {
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
            "test/KoiraTest.java",
            "src/Koira.java",
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

    private static final String TEST_FILE_NAME = "testfile1";
    private static final byte[] TEST_FILE_CONTENT = "test".getBytes();

    private static final Map<String, byte[]> additionalFiles = new HashMap<String, byte[]>() {{
        put(TEST_FILE_NAME, TEST_FILE_CONTENT);
    }};

    private byte[] packaged;
    private ArchiveInputStream inputArchive;
    private ByteArrayInputStream inputStream;

    @Before
    public void createPackage() throws IOException, ArchiveException {
        SubmissionPackagingService service = new SubmissionPackagingService();
        packaged = service.packageSubmission(additionalFiles);

        inputStream = new ByteArrayInputStream(packaged);
        inputArchive = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @After
    public void cleanup() throws IOException {
        inputArchive.close();
        inputStream.close();
    }

    @Test
    public void packageSizeIsNotZero() {
        assertNotEquals(0, packaged.length);
    }

    @Test
    public void packageContainsAllTemplateFiles() throws ArchiveException, IOException {
        ArchiveEntry entry = inputArchive.getNextEntry();

        Set<String> foundEntries = new HashSet<>();

        for (; entry != null; entry = inputArchive.getNextEntry()) {
            assertNotEquals(0, entry.getSize());
            foundEntries.add(entry.getName());
        }

        // Can't compare foundEntries and templateEntries for equality here, as it'd fail
        // when new non-template files are added into the package.
        assertTrue("foundEntries should contain all of templateEntries",
                foundEntries.containsAll(templateEntries));
    }

    @Test
    public void testAdditionalFiles() throws IOException {
        ArchiveEntry testFile = null;
        ArchiveEntry entry = inputArchive.getNextEntry();

        for (; entry != null; entry = inputArchive.getNextEntry()) {
            if (entry.getName().equals(TEST_FILE_NAME)) {
                testFile = entry;
                break;
            }
        }

        assertNotNull("Package should contain testfile1", testFile);
        assertEquals(TEST_FILE_CONTENT.length, testFile.getSize());

        byte[] content = new byte[(int)testFile.getSize()];
        inputStream.read(content);

        assertArrayEquals(TEST_FILE_CONTENT, content);
    }
}
