package eb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serves uploaded images securely from a configured directory. Example: GET
 * /uploads/uuid.png
 */
@WebServlet("/uploads/*")
public class image_controller extends HttpServlet {

    private String uploadDir;

    @Override
    public void init() throws ServletException {
        uploadDir = getServletContext().getInitParameter("uploadDir");

        if (uploadDir == null || uploadDir.trim().isEmpty()) {
            throw new ServletException("uploadDir is not configured in web.xml");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // e.g. /file.png

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file specified.");
            return;
        }

        String filename = pathInfo.substring(1).trim();

        // Basic security check: disallow directory traversal
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid file path.");
            return;
        }

        Path file = Paths.get(uploadDir, filename);

        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
            return;
        }

        // Set MIME type (fallback to binary stream if unknown)
        String mime = getServletContext().getMimeType(filename);
        if (mime == null) {
            mime = "application/octet-stream";
        }
        resp.setContentType(mime);
        resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");

        // Stream file contents
        try ( OutputStream out = resp.getOutputStream()) {
            Files.copy(file, out);
        }
    }
}
