package com.myapp.service.util.file;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static final String PropConstant_FILE_DOWNLOAD_URL = "https://fileapi.adas.com";
    public static final String PropConstant_CACHE_IMAGE_ROOT = "/root/adas/image/";

    public static void downloadFile(String fileUrl, String localPath) throws IOException {
        Path path = Paths.get(localPath);
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        try (DataInputStream dataInputStream = new DataInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(path.toFile())) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "//Upload/Upload/Images/20191121/1911176507100001.jpg";
        String fileUrl = PropConstant_FILE_DOWNLOAD_URL + filePath;
        String localPath = PropConstant_CACHE_IMAGE_ROOT + filePath;
        downloadFile(fileUrl, localPath);
    }

}
