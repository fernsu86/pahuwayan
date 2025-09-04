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

@WebServlet("/uploads/*")
public class image_controller extends HttpServlet {

    private String uploadDir;

    @Override
    public void init() throws ServletException {
        // Always load from context-param
        uploadDir = getServletContext().getInitParameter("uploadDir");

        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new ServletException("uploadDir not configured in web.xml");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // e.g. /uuid.png
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file specified.");
            return;
        }

        String filename = pathInfo.substring(1);

        // Security: disallow path traversal
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid path.");
            return;
        }

        Path file = Paths.get(uploadDir, filename);

        if (Files.exists(file) && Files.isRegularFile(file)) {
            String mime = getServletContext().getMimeType(filename);
            if (mime == null) {
                mime = "application/octet-stream";
            }
            resp.setContentType(mime);
            resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");

            try (OutputStream out = resp.getOutputStream()) {
                Files.copy(file, out);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
        }
    }
}
