package com.dummy.wpb.product.reader;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ResourceAware;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.PROCESSED_FILE_NAMES;

@Slf4j
public class ImportFileItemReader<T> extends AbstractItemStreamItemReader<T> {

    private final String incomingPath;
    private final String filePattern;

    private static final String XML_FILE_NAME_SPLIT = "_";

    protected ResourceAwareItemReaderItemStream<? extends T> delegate;

    protected int currentResource = -1;
    protected List<Resource> resources;

    public ImportFileItemReader(String incomingPath, String filePattern, ResourceAwareItemReaderItemStream<? extends T> delegate) {
        this.incomingPath = incomingPath;
        this.filePattern = filePattern;
        this.delegate = delegate;
    }

    protected ExecutionContext executionContext;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;

        scanFile();

        super.open(executionContext);
    }

    @SneakyThrows
    @Override
    public void close() throws ItemStreamException {
        super.close();
        Iterator<Resource> iterator = resources.iterator();

        while (iterator.hasNext()) {
            File file = iterator.next().getFile();
            File bakFile = new File(file.getAbsolutePath() + ".bak");
            if (file.renameTo(bakFile)) {
                log.info("Rename batch file from {} to {} success!", file.getAbsolutePath(), bakFile.getAbsolutePath());
                iterator.remove();
            } else {
                log.warn("Can not Rename batch file from {} to {}!", file.getAbsolutePath(), bakFile.getAbsolutePath());
            }
        }
    }

    private void scanFile() {
        File incomingFile = new File(incomingPath);
        if (!incomingFile.exists()) {
            throw new IllegalArgumentException(String.format("The specified incoming path/file[%s] does not exist, please check", incomingPath));
        }

        List<File> files = Collections.emptyList();
        if (incomingFile.isFile()) {
            files = Collections.singletonList(incomingFile);
        } else if (incomingFile.isDirectory()) {
            files = Arrays.asList(incomingFile.listFiles());
        }

        resources = files.stream()
                .filter(file -> file.getName().matches(filePattern))
                .sorted(new productFileComparator())
                .map(FileSystemResource::new)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(resources)) {
            log.warn("Cannot match any files from: {}, pattern:{}", incomingPath, filePattern);
        } else {
            log.info("Matched files: {}", resources.stream().map(Resource::getFilename).collect(Collectors.joining(", ")));
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
        delegate.update(executionContext);
        super.update(executionContext);
    }

    @Override
    public synchronized T read() throws Exception {
        if (CollectionUtils.isEmpty(resources)) return null;

        // If there is no resource, then this is the first item, set the current
        // resource to 0 and open the first delegate.
        if (currentResource == -1) {

            currentResource = 0;
            addProcessedFileToContext();
            delegate.setResource(resources.get(currentResource));
            delegate.open(new ExecutionContext());
        }

        return readNextItem();
    }

    private T readNextItem() throws Exception {

        T item = readFromDelegate();

        while (item == null) {

            currentResource++;

            if (currentResource >= resources.size()) {
                return null;
            }

            delegate.close();

            addProcessedFileToContext();
            delegate.setResource(resources.get(currentResource));
            delegate.open(new ExecutionContext());

            item = readFromDelegate();
        }

        return item;
    }

    private T readFromDelegate() throws Exception {
        T item = delegate.read();
        if (item instanceof ResourceAware) {
            ((ResourceAware) item).setResource(getCurrentResource());
        }
        return item;
    }

    private void addProcessedFileToContext() {
        String[] processedFileNames;
        Object object = executionContext.get(PROCESSED_FILE_NAMES);
        if (object instanceof String[]) {
            processedFileNames = (String[]) object;
        } else {
            processedFileNames = new String[]{};
        }
        executionContext.put(PROCESSED_FILE_NAMES, ArrayUtils.add(processedFileNames, getCurrentResource().getFilename()));
    }

    public Resource getCurrentResource() {
        if (currentResource >= resources.size() || currentResource < 0) {
            return null;
        }
        return resources.get(currentResource);
    }

    protected static class productFileComparator implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            String fileName1 = file1.getName();
            String fileName2 = file2.getName();
            if (StringUtils.endsWithIgnoreCase(fileName1, ".xml") && StringUtils.endsWithIgnoreCase(fileName2, ".xml")) {
                // In most cases, the format of the XML file name is: ctryRecCde_grpMembrRecCde_systemCde_prodTypeCde_time.xml. Like: HK_HBAP_RBT_BOND_20231120095800.xml
                String creatTime1 = ArrayUtils.get(fileName1.split(XML_FILE_NAME_SPLIT), 4);
                String creatTime2 = ArrayUtils.get(fileName2.split(XML_FILE_NAME_SPLIT), 4);
                if (!StringUtils.isAllBlank(creatTime1, creatTime2)) {
                    return creatTime1.compareTo(creatTime2);
                }
            }

            try {
                BasicFileAttributes fileAttributes1 = Files.readAttributes(file1.toPath(), BasicFileAttributes.class);
                BasicFileAttributes fileAttributes2 = Files.readAttributes(file2.toPath(), BasicFileAttributes.class);
                return fileAttributes1.creationTime().compareTo(fileAttributes2.creationTime());
            } catch (IOException e) {
                return 0;
            }
        }
    }
}
