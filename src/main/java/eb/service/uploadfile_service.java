package eb.service;

import eb.dao.imagedao;
import eb.dto.imagedto;
import eb.dto.imagefiledto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class uploadfile_service {

    private final String uploadFolder;   // absolute path on disk
    private final String dbPrefix = "uploads/"; // what to store in DB

    public uploadfile_service(String uploadDir, boolean isRelativeToWebApp) throws IOException {
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new IOException("Upload folder path is null/blank.");
        }

        if (isRelativeToWebApp) {
            // Save into your project’s webapp/uploads folder
            this.uploadFolder = Paths.get(System.getProperty("catalina.base"),
                    "webapps", "yourAppName", uploadDir).toString();
        } else {
            // Use absolute path directly
            this.uploadFolder = uploadDir;
        }

        Files.createDirectories(Paths.get(this.uploadFolder));
    }

    /**
     * Saves multiple images for a property and returns the successfully saved list.
     */
    public List<imagedto> saveImages(String propertyId, List<imagefiledto> files) throws IOException {
        List<imagedto> savedImages = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            // Property has no image
            saveMetadataToDb(new imagedto(null, null, propertyId));
            return savedImages;
        }

        for (imagefiledto file : files) {
            if (!isValidImage(file.getContentType(), file.getSize())) {
                continue;
            }

            String storedFileName = generateUniqueFileName(file.getOriginalName());
            Path destination = saveFileToServer(file.getInputStream(), storedFileName);

            // Always store with "uploads/" prefix in DB
            String dbPath = dbPrefix + storedFileName;

            // image_id = filename, image_path = "uploads/filename"
            imagedto dto = new imagedto(storedFileName, dbPath, propertyId);

            if (!saveMetadataToDb(dto)) {
                Files.deleteIfExists(destination); // rollback on DB failure
                continue;
            }
            savedImages.add(dto);
        }
        return savedImages;
    }

    // ---------------- Helper Methods ----------------
    private boolean isValidImage(String contentType, long size) {
        return contentType != null && contentType.toLowerCase().startsWith("image/") && size > 0;
    }

    private String generateUniqueFileName(String original) {
        int dot = (original != null) ? original.lastIndexOf('.') : -1;
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

    private boolean saveMetadataToDb(imagedto dto) {
        try {
            imagedao dao = new imagedao();
            return dao.add_image(dto.getProperty_id(), dto.getImage_id(), dto.getImage_path());
        } catch (Exception e) {
            e.printStackTrace(); // replace with logging in prod
            return false;
        }
    }
}
