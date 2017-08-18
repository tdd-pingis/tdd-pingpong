package pingis.services.sandbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by dwarfcrank on 7/25/17.
 */
@Service
@Profile(value = {"prod", "sandbox"})
public class SubmissionPackagingService implements SubmissionPackagingServiceInterface {

  private final Logger logger = LoggerFactory.getLogger(SubmissionPackagingService.class);

  private List<Path> getDirectoryEntries(Path directory) throws IOException {
    List<Path> entries = new ArrayList<>();

    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
      for (Path entry : directoryStream) {
        if (Files.isDirectory(entry)) {
          List<Path> subEntries = getDirectoryEntries(entry);
          entries.addAll(subEntries);
        } else {
          entries.add(entry);
        }
      }
    }

    return entries;
  }

  private void addFile(ArchiveOutputStream archive, String tarFileName, Path filePath)
      throws IOException {
    ArchiveEntry archiveEntry = archive.createArchiveEntry(filePath.toFile(), tarFileName);

    archive.putArchiveEntry(archiveEntry);
    Files.copy(filePath, archive);
    archive.closeArchiveEntry();

    logger.debug("Added file {} as {}", filePath.toAbsolutePath(), tarFileName);
  }

  private void addFile(ArchiveOutputStream archive, String tarFileName, byte[] fileContent)
      throws IOException {
    TarArchiveEntry tarEntry = new TarArchiveEntry(tarFileName);
    tarEntry.setSize(fileContent.length);

    archive.putArchiveEntry(tarEntry);
    archive.write(fileContent);
    archive.closeArchiveEntry();

    logger.debug("Added file {}", tarFileName);
  }

  private void addTemplateFiles(ArchiveOutputStream archive) throws IOException {
    // TODO: maybe get the template path from config?
    Path templateDirectory = FileSystems.getDefault().getPath("tmc-assets", "submission-template");
    List<Path> templateEntries = getDirectoryEntries(templateDirectory);

    for (Path entry : templateEntries) {
      // Paths must be relativized against the template directory root
      // before including them into the Tar archive.
      // I.e. a file at ./tmc-assets/submission-template/directory/file.txt will be added to
      // the archive as ./directory/file.txt.
      String tarEntryName = templateDirectory.relativize(entry).toString();

      // TODO: special case for build.xml for renaming the project.
      addFile(archive, tarEntryName, entry);
    }
  }

  private void addAdditionalFiles(ArchiveOutputStream archive, Map<String, byte[]> additionalFiles)
      throws IOException {
    for (String entryName : additionalFiles.keySet()) {
      byte[] content = additionalFiles.get(entryName);

      addFile(archive, entryName, content);
    }
  }

  /**
   * @param additionalFiles A map of filename -> content pairs to include in the package.
   * @return The packaged submission as a TAR archive.
   */
  @Override
  public byte[] packageSubmission(Map<String, byte[]> additionalFiles)
      throws IOException, ArchiveException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try (ArchiveOutputStream archive = new ArchiveStreamFactory()
        .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream)) {

      addTemplateFiles(archive);
      addAdditionalFiles(archive, additionalFiles);

      archive.finish();
    }

    logger.debug("Finished packaging submission, size {} bytes.", outputStream.size());

    return outputStream.toByteArray();
  }
}
