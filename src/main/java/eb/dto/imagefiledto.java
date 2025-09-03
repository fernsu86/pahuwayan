/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dto;

import java.io.InputStream;

/**
 *
 * @author ACER
 */
public class imagefiledto {
    private final String originalName;
    private final String contentType;
    private final long size;
    private final InputStream inputStream;

    public imagefiledto(String originalName, String contentType, long size, InputStream inputStream) {
        this.originalName = originalName;
        this.contentType = contentType;
        this.size = size;
        this.inputStream = inputStream;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
    
}
