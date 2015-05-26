package cc.lingpin.corpusConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gate.*;
import gate.Document;
import gate.corpora.DocumentContentImpl;
import gate.corpora.DocumentImpl;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

public class corpusConverter {

    public Document convert(List<String> content) throws ResourceInstantiationException, InvalidOffsetException {

        splitContentAndAnnotation(content);
        DocumentContentImpl dc = new DocumentContentImpl(context);
        DocumentImpl doc = new DocumentImpl();
        doc.setContent(dc);
        AnnotationSet gold = doc.getAnnotations("gold");
        FeatureMap emptyFeature = Factory.newFeatureMap();
        for (Annot a : annotations) {
            gold.add(a.start, a.end, a.type, emptyFeature);
        }
        return doc;
    }

    private void splitContentAndAnnotation(List<String> contents) {
        context = contents.stream().map(s -> s.split("\\t")[0]).collect(Collectors.joining("\n"));
        Long start = 0L;
        annotations = new ArrayList<>();

        for (String line : contents) {
            Annot a = new Annot();
            String[] splitted = line.split("\\t");

            Long end = start + splitted[0].length();
            a.start = start;
            a.end = end;
            start = end + 1;

            a.type = splitted[1];

            annotations.add(a);
        }
    }

    class Annot {
        public Long start;
        public Long end;
        public String type;
    }

    private String context;
    private List<Annot> annotations;
}
