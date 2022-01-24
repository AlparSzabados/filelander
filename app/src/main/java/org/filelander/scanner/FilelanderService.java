package org.filelander.scanner;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FilelanderService {

    public Map<String, List<String>> scan(String path) {
        Map<String, List<DuplicateFile>> result = new HashMap<>();

        System.out.println("Scan...");
        Scanner scanner = new LocalScanner();
        Map<Long, List<String>> files = scanner.scan(path);
        files.remove(0L);

        System.out.println("Compare..");

        final Map<Long, List<String>> compfiles = files.entrySet().stream().filter(e->e.getValue().size()>1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        ForkJoinPool forkJoinPool = new ForkJoinPool(1000);

        Map<String, List<String>> x = new HashMap<>();
        final AtomicInteger i = new AtomicInteger(1);
        List<CompareTask> tasks = compfiles.values().stream()
                .sorted(Comparator.comparingInt(List::size))
                .map(l->new CompareTask(l, new FirstBytesFileCompare(), i.getAndIncrement(), compfiles.size()))
                .collect(Collectors.toList());
        tasks.forEach(forkJoinPool::submit);
        tasks.forEach(t-> x.putAll(t.join()));

        return  x;
    }
}
