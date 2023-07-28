package com.osp.service.impl;

import com.osp.service.FileDownloadService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {

    @Override
    public void downloadFile(HttpServletResponse response, String fileName, String mediaType, IDownloadProcessor processor) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodeFileName(fileName));
        response.setContentType(mediaType);
        processor.do_(response.getOutputStream());
    }

    private String encodeFileName(String fileName) {
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return encodedFileName;
    }

    public interface IDownloadProcessor {
        void do_(OutputStream os);
    }
}
