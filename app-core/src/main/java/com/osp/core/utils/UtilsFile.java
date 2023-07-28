package com.osp.core.utils;

import java.io.*;
import java.util.Calendar;

public class UtilsFile {

    private static final String SUFFIX = ".dat";
    private static final String SUFFIX_DOC = ".doc";
    private static final String SUFFIX_DOCX = ".docx";

    public static File createNewFileWithOrigName(String directory, String fileName) throws Exception {
        File dirs = new File(directory);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        return new File(directory+fileName);
    }

    // đọc nội dung file
    public String getContentFile(String path) {
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            String data = readFromInputStream(inputStream);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        inputStream.close();
        return resultStringBuilder.toString();
    }

    public static void deleteLogFile() {
        File logFolder = new File("/web/log");
        if (!logFolder.exists()) {
            return;
        }

        FileFilter ff = new FileFilter() {

            public boolean accept(File f) {
                //Loc file qua 10 ngay
                if (Calendar.getInstance().getTimeInMillis() - f.lastModified() > 864000000) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] logFiles = logFolder.listFiles(ff);

        for (File file : logFiles) {
            file.delete();
        }
    }
}
