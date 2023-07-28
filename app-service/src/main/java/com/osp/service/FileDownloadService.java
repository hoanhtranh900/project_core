package com.osp.service;


import com.osp.service.impl.FileDownloadServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileDownloadService {
    void downloadFile(HttpServletResponse response, String fileName, String mediaType, FileDownloadServiceImpl.IDownloadProcessor processor) throws IOException;
}
