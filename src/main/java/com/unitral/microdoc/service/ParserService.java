package com.unitral.microdoc.service;

import com.unitral.microdoc.dto.DocumentObject;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface ParserService {

    String getFileType(InputStream inputStream) throws TikaException, IOException, SAXException;

    String getFileContent(InputStream inputStream) throws IOException, TikaException, SAXException;

    String getFileType2();

    DocumentObject parseFile(MultipartFile file) throws IOException, TikaException, SAXException, ParserConfigurationException;

    DocumentObject parseFileHTML(InputStream inputStream) throws IOException, TikaException, SAXException, ParserConfigurationException;

}
