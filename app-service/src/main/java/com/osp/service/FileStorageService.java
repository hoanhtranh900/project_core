package com.osp.service;

import com.osp.core.entity.FileAttachDocument;

import java.io.*;
import java.util.List;

public interface FileStorageService {

    File mergePdfFiles(File desFile, InputStream... inputStreams);

    String getSubFolder();

    File getLocalFile(String id, String subFolder);

    File getLocalTempFile();

    void writeFile(InputStream inputStream, OutputStream os) throws IOException;

    File saveFile(String id, String subFolder, InputStream inputStream, Boolean isEncrypt);

    File saveFile(String id, String subFolder, Boolean isEncrypt, FileISaveLogic iSaveFileLogic);

    String getFont(String path);

    void addWaterMark(InputStream inputStream, String waterMarkText, OutputStream outputStream);

    void deleteFile(File file);

    void deleteFile(List<File> files);

    InputStream getInputStream(InputStream inputStream, Boolean isEncrypt);

    InputStream getInputStream(FileAttachDocument attachDocument) throws FileNotFoundException;

}

