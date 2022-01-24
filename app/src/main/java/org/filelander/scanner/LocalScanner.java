package org.filelander.scanner;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.FileVisitResult.CONTINUE;

public class LocalScanner implements Scanner {

    @AllArgsConstructor
    public static class FileVisitor extends SimpleFileVisitor<Path> {

        private final Map<Long, List<String>> result;

        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attrs) {
            addFile(file.toFile());
            return CONTINUE;
        }

        private void addFile(File f) {
            Long fileSize = f.length();
            if (!result.containsKey(fileSize)) {
                result.put(fileSize, new ArrayList<>());
            }
            result.get(fileSize).add(f.getAbsolutePath());
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            exc.printStackTrace();
            return CONTINUE;
        }
    }

    @Override
    public Map<Long, List<String>> scan(String path){
        Map<Long, List<String>> result = new HashMap<>();
        try {
            Files.walkFileTree(Paths.get(path), new FileVisitor(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
