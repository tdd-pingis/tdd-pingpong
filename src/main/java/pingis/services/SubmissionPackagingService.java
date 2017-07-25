package pingis.services;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dwarfcrank on 7/25/17.
 */
@Service
public class SubmissionPackagingService {
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

    private void addTemplateFiles(ArchiveOutputStream archive) throws IOException {
        // TODO: maybe get the template path from config?
        Path templateDirectory = FileSystems.getDefault().getPath("tmc-assets", "submission-template");
        List<Path> templateEntries = getDirectoryEntries(templateDirectory);

        for (Path entry : templateEntries) {
            // Paths must be relativized against the template directory root before including them into the Tar
            // archive. I.e. a file at ./tmc-assets/submission-template/directory/file.txt will be added to
            // the archive as ./directory/file.txt.
            String tarEntryName = templateDirectory.relativize(entry).toString();

            // TODO: special case for build.xml for renaming the project.
            ArchiveEntry archiveEntry = archive.createArchiveEntry(entry.toFile(), tarEntryName);

            archive.putArchiveEntry(archiveEntry);
            Files.copy(entry, archive);
            archive.closeArchiveEntry();
        }
    }

    private void addAdditionalFiles(ArchiveOutputStream archive, Map<String, byte[]> additionalFiles)
            throws IOException {
        for (String entryName : additionalFiles.keySet()) {
            byte[] content = additionalFiles.get(entryName);

            TarArchiveEntry tarEntry = new TarArchiveEntry(entryName);
            tarEntry.setSize(content.length);

            archive.putArchiveEntry(tarEntry);
            archive.write(content);
            archive.closeArchiveEntry();
        }
    }

    public byte[] packageSubmission(Map<String, byte[]> additionalFiles) throws IOException, ArchiveException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (ArchiveOutputStream archive = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream)) {

            addTemplateFiles(archive);
            addAdditionalFiles(archive, additionalFiles);

            archive.finish();
        }

        return outputStream.toByteArray();
    }
}
