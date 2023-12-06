
package com.unitral.microdoc.model.impl;

import com.azure.ai.translation.text.TextTranslationClient;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitral.microdoc.dto.DocumentObject;
import com.unitral.microdoc.model.ParserModel;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import com.azure.ai.translation.text.TextTranslationClientBuilder;


import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ApacheTika implements ParserModel {
    private BodyContentHandler textHandler = null;
    private ToXMLContentHandler xmlHandler = null;
    private Metadata metadata = null;
    private AutoDetectParser parser = null;
    private ParseContext context = null;
    private Tika tika = null;

    ApacheTika() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);
        parser = new AutoDetectParser();
        context = new ParseContext();
        tika = new Tika();
        context.set(TesseractOCRConfig.class, config);
        context.set(PDFParserConfig.class, pdfConfig);
        context.set(Parser.class, parser); // need to add this to make sure

        System.out.println("Apache Tika " + this.hashCode());

    }

    private static void processTable(List<XWPFTable> tables, StringBuilder formattedContent) {
        for (XWPFTable table : tables) {
            formattedContent.append("Table:\n");

            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    formattedContent.append(cell.getText()).append("\t");
                }
                formattedContent.append("\n");
            }

            formattedContent.append("\n");
        }
    }

    String FileContent = "";

    public DocumentObject _parseFileHTML(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException {
        xmlHandler = new ToXMLContentHandler();
        metadata = new Metadata();
        parser.parse(inputStream, xmlHandler, metadata, context);


        org.jsoup.nodes.Document document = Jsoup.parse(xmlHandler.toString());

        // Convert HTML to JSON
       /* Element rootElement = document.child(0);

        System.out.println("root document: "+rootElement);
        String json = convertToJson(rootElement);*/
        Elements metaElements = document.select("html");


        // Extract and print the content of 'meta name' elements
        Elements questionElements = metaElements.select("p.question_text");
        Elements answerElements = metaElements.select("p.answer_options");

// Extract and print the content of questions and answers
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < questionElements.size(); i++) {
            String question = questionElements.get(i).text();
            String answer = (i < answerElements.size()) ? answerElements.get(i).text() : "";
            // Check if there is a corresponding answer

            result.append("Question: ").append(question).append("\n")
                    .append("Answer: ").append(answer).append("\n\n");
        }

        System.out.println("result: " + result.toString());


        DocumentObject docObj = new DocumentObject(metadata.get("Content-Type"), xmlHandler.toString(), metadata);

        return docObj;
    }

    private String convertToJson(Element element) {
        ObjectMapper objectMapper = new ObjectMapper();

        // You might want to create a custom class to represent your HTML structure
        // For simplicity, this example uses a Map
        // Adjust the structure based on your actual HTML content
        Object json = parseElement(element);

        try {
            return objectMapper.writeValueAsString(json);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }

    private Object parseElement(Element element) {
        // This is a simple example; you might want to customize based on your HTML structure
        // For each HTML element, create a Map with attributes, text, and children
        // Recursively call parseElement for each child
        // Adjust the structure based on your actual HTML content

        // For simplicity, using a Map
        // You might want to create a custom class for better representation
        return Map.of(
                "tag", element.tagName(),
                "attributes", element.attributes(),
                "text", element.text(),
                "children", element.children().stream().map(this::parseElement).collect(Collectors.toList())
        );
    }

    public DocumentObject _parseFileFeatures(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException {
        textHandler = new BodyContentHandler(-1);
        metadata = new Metadata();

        XWPFDocument document = new XWPFDocument(inputStream);

        StringBuilder formattedContent = new StringBuilder();


        for (XWPFParagraph paragraph : document.getParagraphs()) {
//            formattedContent.append("Paragraph Text: ").append(paragraph.getText()).append("\n");
//            formattedContent.append("Content: ").append(paragraph.getFontAlignment()).append("\n\n");
//            formattedContent.append("Content: ").append(paragraph.getAlignment()).append("\n\n");
//            formattedContent.append("Content: ").append(paragraph.getRuns()).append("\n\n");


            List<XWPFTable> tables = paragraph.getBody().getTables();
            for (XWPFTable table : tables) {
                // Process each table
                printTable(table);
            }


//            System.out.println(paragraph.getCTP());


            // Features of XWPFParagraph
//            for (XWPFRun run : paragraph.getRuns()) {
//                formattedContent.append("Text: ").append(run.getText(0)).append("\n");
//
//
//                // Features of XWPFRun
//
//                formattedContent.append("Is Bold: ").append(run.isBold()).append("\n");
//                formattedContent.append("Is Italic: ").append(run.isItalic()).append("\n");
//                formattedContent.append("Is StrikeThrough: ").append(run.isStrikeThrough()).append("\n");
//                formattedContent.append("Is DoubleStrikeThrough: ").append(run.isDoubleStrikeThrough()).append("\n");
//                formattedContent.append("Underline: ").append(run.getUnderline()).append("\n");
//                formattedContent.append("Font Size: ").append(run.getFontSizeAsDouble()).append("\n");
//                formattedContent.append("Font Family: ").append(run.getFontFamily()).append("\n");
//                formattedContent.append("Font Color: ").append(run.getColor()).append("\n");
//                formattedContent.append("Character Spacing: ").append(run.getCharacterSpacing()).append("\n");
//                formattedContent.append("Strike Through: ").append(run.isStrikeThrough()).append("\n");
//                formattedContent.append("Double Strike Through: ").append(run.isDoubleStrikeThrough()).append("\n");
//                formattedContent.append("Embossed: ").append(run.isEmbossed()).append("\n");
//                formattedContent.append("Imprinted: ").append(run.isImprinted()).append("\n");
//                formattedContent.append("Shadowed: ").append(run.isShadowed()).append("\n");
//                formattedContent.append("Capitalized: ").append(run.isCapitalized()).append("\n");
//                formattedContent.append("Small Caps: ").append(run.isSmallCaps()).append("\n");
//                formattedContent.append("Vanish: ").append(run.isVanish()).append("\n");
//                formattedContent.append("Vertical Alignment: ").append(run.getVerticalAlignment()).append("\n");
//                formattedContent.append("Highlighted: ").append(run.isHighlighted()).append("\n");
//                formattedContent.append("Text Position: ").append(run.getTextPosition()).append("\n");
//
//
//
//
//
//                formattedContent.append("\n"); // Separate runs
//            }

//            formattedContent.append("\n"); // Separate paragraphs
        }


        // Optionally, you can print or store the formatted content
//        System.out.println(formattedContent.toString());

        // Create and return DocumentObject
        DocumentObject docObj = new DocumentObject(metadata.get("Content-Type"), formattedContent.toString(), metadata);
        return docObj;

    }



    private void printTable(XWPFTable table) {
        List<LinkedList<String>> tableData = new LinkedList<LinkedList<String>>();
        // Iterate through rows
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            // Iterate through cells
            List<XWPFTableCell> cells = row.getTableCells();
            LinkedList<String> rowData = new LinkedList<String>();
            for (XWPFTableCell cell : cells) {
                // Print cell content
//                System.out.print(cell.getText() + "\t");
                rowData.add(cell.getText());
            }
            tableData.add(rowData);
//            System.out.println();  // Move to the next line after each row
        }
//        System.out.println();  // Add an empty line between tables

        // Print table data
        for (LinkedList<String> rowData : tableData) {
            for (String cellData : rowData) {
                System.out.print(cellData + "\t");
            }
            System.out.println();
        }
    }


    public DocumentObject parseFileFeatures(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException {
        textHandler = new BodyContentHandler(-1);
        metadata = new Metadata();
        XWPFDocument document = new XWPFDocument(inputStream);
        return new DocumentObject(metadata.get("Content-Type"),document, metadata);

    }

    public DocumentObject parseFileHTML(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException {
        xmlHandler = new ToXMLContentHandler();
        metadata = new Metadata();
        parser.parse(inputStream, xmlHandler, metadata, context);
        return new DocumentObject(metadata.get("Content-Type"), xmlHandler, metadata);
    }

    public DocumentObject parseFile(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException {
        textHandler = new BodyContentHandler(-1);
        metadata = new Metadata();
        parser.parse(inputStream, textHandler, metadata, context);
        return new DocumentObject(metadata.get("Content-Type"),  textHandler, metadata);
    }

    public MediaType getTypeOfFile(InputStream inputStream) throws IOException {
        TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
        return tika.getDetector().detect(tikaInputStream, new Metadata());
    }

    public String getText(InputStream inputStream) throws IOException, TikaException, SAXException {
        TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
        return tika.parseToString(tikaInputStream);
    }

    public String getLanguage(InputStream inputStream) throws IOException, TikaException, SAXException {
        TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
        LanguageDetector detector = LanguageDetector.getDefaultLanguageDetector().loadModels();
        detector.addText(tikaInputStream.toString());
        List<LanguageResult> languageResults = detector.detectAll();
        return languageResults.toString();
    }

    public String _translate(InputStream inputStream) throws IOException, TikaException {
        TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
        TextTranslationClient translationClient = new TextTranslationClientBuilder()
                .credential(new AzureKeyCredential("2649c502cfbf46178361e75d7fe16e8c"))
                .endpoint("https://api.cognitive.microsofttranslator.com/")
                .buildClient();
        return null;
    }





}

