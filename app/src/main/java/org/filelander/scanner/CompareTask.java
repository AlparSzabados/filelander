package org.filelander.scanner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class CompareTask extends RecursiveTask<Map<String, List<String>>>{
    private final List<String> filePaths;
    private final FileCompare fileCompare;
    private final int id;
    private final int total;

    public CompareTask(List<String> filePaths, FileCompare fileCompare, int id, int total) {
        this.filePaths = filePaths;
        this.fileCompare = fileCompare;
        this.id = id;
        this.total = total;
    }

    @Override
    protected Map<String, List<String>> compute() {
        Map<String, List<String>> result = fileCompare.compare(filePaths);
        System.out.println("Done ["+id+"/"+total+"]" + filePaths.size() + ".");
        return result;
    }
}