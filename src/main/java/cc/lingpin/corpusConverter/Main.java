package cc.lingpin.corpusConverter;

import gate.Document;
import gate.util.GateException;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String CORPUS_FOLDER = "data/corpora";
    private static final String OUTPUT_FOLDER = "data/gateCorpus";

    public static void main(String[] args) throws IOException, SAXException, GateException {
        makeOutputFolder();
        Path folder = Paths.get(CORPUS_FOLDER);
        List<Path> corpora = Files.list(folder)
                .filter(path -> path.toString().endsWith("tsv"))
                .collect(Collectors.toList());
        corpusConverter converter = new corpusConverter();

        for (Path corpus : corpora) {
            Document doc = converter.convert(Files.readAllLines(corpus));

            Files.write(outputFilename(corpus, OUTPUT_FOLDER), doc.toXml().getBytes(), StandardOpenOption.CREATE);
        }
    }

    private static void makeOutputFolder() throws IOException {
        Path path = Paths.get(OUTPUT_FOLDER);
        if (!Files.exists(path))
            Files.createDirectory(path);
    }

    private static Path outputFilename(Path file, String outputFolder) {
        String baseName = FilenameUtils.getBaseName(file.toString());
        return Paths.get(outputFolder, baseName + ".xml");
    }
}