package org.filelander.scanner;

import lombok.Data;

@Data
public class DuplicateFile {

    private String path;
    boolean main;

}
