package org.filelander.scanner;

import java.util.List;
import java.util.Map;

public interface Scanner {

    Map<Long, List<String>> scan(String path);

}
