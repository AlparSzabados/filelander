package org.filelander.scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class ScannerTest {

    @InjectMocks
    private FilelanderService fs;

    @Test
    public void testScan() {
        Map<String, List<String>> files = fs.scan("e:\\");
        files.forEach((k,v)->{if (v.size()>1){System.out.println("Possible duplicate: " + k);v.forEach(System.out::println);}});
        assertFalse(files.isEmpty());
    }


}
