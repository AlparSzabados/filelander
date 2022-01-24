package org.filelander.scanner;

import java.util.List;
import java.util.Map;

public interface FileCompare {

    Map<String, List<String>> compare(List<String> filePaths);

}
