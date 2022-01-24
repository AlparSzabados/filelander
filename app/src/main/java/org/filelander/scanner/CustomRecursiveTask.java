package org.filelander.scanner;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Map<String, List<String>>> {
    private final List<String> filePaths;
    private final FileCompare fileCompare;

    private static final int THRESHOLD = 10;

    public CustomRecursiveTask(List<String> filePaths, FileCompare fileCompare) {
        this.filePaths = filePaths;
        this.fileCompare = fileCompare;
    }

    @Override
    protected Map<String, List<String>> compute() {
        if (filePaths.size() > THRESHOLD) {
            Map<String, List<String>> result = new HashMap<>();
            ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .forEach(result::putAll);
            return result;
        } else {
            return fileCompare.compare(filePaths);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks() {
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomRecursiveTask(filePaths.subList(0, filePaths.size()/2), fileCompare));
        dividedTasks.add(new CustomRecursiveTask(filePaths.subList(filePaths.size()/2, filePaths.size()), fileCompare));
        return dividedTasks;
    }

}