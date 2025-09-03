package eb.service;

import eb.dao.imagedao;
import eb.dto.imagedto;
import eb.dto.imagefiledto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class uploadfile_service {

    private final String uploadFolder;

    public uploadfile_service(String absoluteUploadDir) throws IOException {
        if (absoluteUploadDir == null) {
            throw new IOException("Upload folder path is null.");
        }
        this.uploadFolder = absoluteUploadDir;
        Files.createDirectories(Paths.get(uploadFolder));
    }

    /**
     * Saves multiple images for a property and returns the successfully saved list.
     */
    public List<imagedto> saveImages(String propertyId, List<imagefiledto> files) throws IOException {
        List<imagedto> savedImages = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            // Insert NULL record when no images are uploaded
            saveMetadataToDb(new imagedto(null, null, propertyId));
            return savedImages;
        }

        for (imagefiledto file : files) {
            if (!isValidImage(file.getContentType(), file.getSize())) {
                continue;
            }

            String storedFileName = generateUniqueFileName(file.getOriginalName());
            Path destination = saveFileToServer(file.getInputStream(), storedFileName);
            String dbPath = buildDbPath(storedFileName);

            imagedto dto = new imagedto(storedFileName, dbPath, propertyId);

            if (!saveMetadataToDb(dto)) {
                Files.deleteIfExists(destination); // rollback if DB insert fails
                continue;
            }

            savedImages.add(dto);
        }

        return savedImages;
    }

    // ---------------- Helper Methods ----------------

    private boolean isValidImage(String contentType, long size) {
        return contentType != null 
                && contentType.toLowerCase().startsWith("image/") 
                && size > 0;
    }

    private String generateUniqueFileName(String original) {
        int dot = original.lastIndexOf('.');
        String ext = (dot >= 0) ? original.substring(dot) : "";
        return UUID.randomUUID() + ext;
    }

    private Path saveFileToServer(InputStream in, String storedFileName) throws IOException {
        Path destination = Paths.get(uploadFolder).resolve(storedFileName);
        try (InputStream input = in) {
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        return destination;
    }

    private String buildDbPath(String storedFileName) {
        return "uploads/" + storedFileName;
    }

    private boolean saveMetadataToDb(imagedto dto) {
        try {
            imagedao dao = new imagedao();
            return dao.add_image(dto.getProperty_id(), dto.getImage_id(), dto.getImage_path());
        } catch (Exception e) {
            e.printStackTrace(); // TODO: replace with proper logging
            return false;
        }
    }
}
