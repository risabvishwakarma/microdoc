package com.unitral.microdoc.model;

import com.unitral.microdoc.dto.DocumentObject;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface ParserModel {
    public DocumentObject parseFile(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException;

    public DocumentObject parseFileHTML(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException;

    DocumentObject parseFileFeatures(InputStream inputStream) throws TikaException, IOException, SAXException, ParserConfigurationException;

    ;
}
