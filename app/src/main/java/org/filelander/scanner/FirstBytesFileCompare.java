package org.filelander.scanner;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FirstBytesFileCompare implements FileCompare {

    @AllArgsConstructor
    @Getter
    public static class FileBytes {
        private String filePath;
        private byte[] firstBytes;
    }

    @Override
    public Map<String, List<String>> compare(List<String> filePaths) {
        Map<String, List<String>> result = new HashMap<>();

        if (filePaths != null && !filePaths.isEmpty()) {
            List<FileBytes> fileBytesList = filePaths.stream().map(f->new FileBytes(f, readFile(f, 4096))).collect(Collectors.toList());
            result = compare1(fileBytesList);
        }
        return result;
    }

    private Map<String, List<String>> compare1(List<FileBytes> files) {
        Map<String, List<String>> result = new HashMap<>();
        if (files != null && !files.isEmpty()) {
            FileBytes file = files.get(0);
            if (files.size() == 1) {
                result.put(file.getFilePath(), Collections.singletonList(file.getFilePath()));
            } else {
                List<FileBytes> identicalFiles = files.stream()
                        .filter(f -> Arrays.equals(file.getFirstBytes(), f.getFirstBytes()))
                        .collect(Collectors.toList());
                result.put(file.getFilePath(), identicalFiles.stream().map(FileBytes::getFilePath).collect(Collectors.toList()));
                files.removeAll(identicalFiles);
                result.putAll(compare1(files));
            }
        }
        return result;
    }

    private byte[] readFile(String filePath, int byteLength) {
        File file = new File(filePath);
        byte[] bytes = new byte[byteLength];
        try (FileInputStream fis = new FileInputStream(file)){
            fis.read(bytes);
        } catch (IOException e) {
            System.err.println(filePath);
            e.printStackTrace();
            return null;
        }

        return bytes;
    }

}
