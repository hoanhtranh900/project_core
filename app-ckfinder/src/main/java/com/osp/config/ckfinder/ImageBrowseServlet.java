package com.osp.config.ckfinder;


import com.ckfinder.connector.ConnectorServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/ckfinder/core/connector/java/connector.java", initParams = {
        @WebInitParam(name = "XMLConfig", value = "classpath:/ckfinder.xml"),
        @WebInitParam(name = "debug", value = "true"),
        @WebInitParam(name = "configuration", value = "com.osp.config.ckfinder.CKFinderConfig")
})
public class ImageBrowseServlet extends ConnectorServlet {}
