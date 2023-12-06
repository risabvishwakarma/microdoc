package com.unitral.microdoc.service.impl;


import com.unitral.microdoc.dto.DocumentObject;
import com.unitral.microdoc.model.ParserModel;
import com.unitral.microdoc.service.ParserService;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;


@Service
public class DocumentParserService implements ParserService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentParserService.class);
    private final ParserModel parserModel;

    DocumentParserService(ParserModel parserModel){
        this.parserModel = parserModel;
        logger.info("DocumentParserService "+this.hashCode());
    }

    @Override
    public String getFileType(InputStream inputStream) throws TikaException, IOException, SAXException {
        return null;
    }

    @Override
    public String getFileContent(InputStream inputStream) throws IOException, TikaException, SAXException {
        return null;
    }

    @Override
    public String getFileType2() {
        return null;
    }

    @Override
    public DocumentObject parseFile(MultipartFile file) throws IOException, TikaException, SAXException, ParserConfigurationException {
        return parserModel.parseFileHTML(file.getInputStream());
    }

    @Override
    public DocumentObject parseFileHTML(InputStream inputStream) throws IOException, TikaException, SAXException, ParserConfigurationException {
        return parserModel.parseFile(inputStream);
    }


}
