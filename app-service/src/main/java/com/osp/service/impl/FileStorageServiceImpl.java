package com.osp.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;
import com.osp.core.entity.FileAttachDocument;
import com.osp.core.exception.StorageException;
import com.osp.core.utils.H;
import com.osp.service.FileISaveLogic;
import com.osp.service.FileStorageService;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${upload.encrypt.algorithm}")
    private String encryptAlgorithm;
    @Value("${upload.encrypt.transformation}")
    private String encryptTransformation;
    @Value("${upload.encrypt.key}")
    private String encryptKey;
    @Value("${upload.folderPath}")
    private String uploadFolder;
    @Value("${upload.tempSubFolder}")
    private String tempSubFolder;
    @Value("${config.font.path}")
    private String baseFontPath;

    private void close(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (Exception ex) {
        }
    }

    @Override
    public File getLocalFile(String id, String subFolder) {
        String dir = this.uploadFolder + (subFolder != null ? ("/" + subFolder) : this.uploadFolder);
        File directory = new File(dir);
        if (!directory.exists()) directory.mkdirs();
        String targetFilePath = dir + "/" + id;
        return new File(targetFilePath);
    }

    @Override
    public File getLocalTempFile() {
        return getLocalFile(UUID.randomUUID().toString(), tempSubFolder);
    }

    @Override
    public void writeFile(InputStream inputStream, OutputStream os) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } finally {
            close(inputStream);
            close(os);
        }
    }

    @Override
    public File saveFile(String id, String subFolder, InputStream inputStream, Boolean isEncrypt) {
        return this.saveFile(id, subFolder, isEncrypt, outputStream -> {
            try {
                writeFile(inputStream, outputStream);
            } catch (Exception ex) {
                throw new StorageException("StorageException.saveFile", ex);
            }
        });
    }

    @Override
    public File saveFile(String id, String subFolder, Boolean isEncrypt, FileISaveLogic iSaveFileLogic) {
        OutputStream outputStream = null;
        try {
            File targetFile = getLocalFile(id, subFolder);
            if (isEncrypt) {
                Key k = new SecretKeySpec(encryptKey.getBytes(), encryptAlgorithm);
                Cipher aes = Cipher.getInstance(encryptTransformation);
                aes.init(Cipher.ENCRYPT_MODE, k);
                outputStream = new CipherOutputStream(new FileOutputStream(targetFile), aes);
            } else {
                outputStream = new FileOutputStream(targetFile);
            }
            iSaveFileLogic.do_(outputStream);
            return targetFile;
        } catch (StorageException e) {
            throw e;
        } catch (Exception e) {
            throw new StorageException("StorageException.saveFile", e);
        } finally {
            close(outputStream);
        }
    }

    @Override
    public String getFont(String path) {
        return baseFontPath + "/" + path;
    }

    @Override
    public void addWaterMark(InputStream inputStream, String waterMarkText, OutputStream outputStream) {

        PdfReader reader = null;
        PdfStamper stamper = null;

        try {

            reader = new PdfReader(inputStream);
            stamper = new PdfStamper(reader, outputStream);

            BaseFont baseFont = BaseFont.createFont(getFont("fonts/vuTimes.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12, Font.ITALIC, BaseColor.GRAY);
            Phrase phrase = new Phrase(waterMarkText, font);

            PdfContentByte over;

            int n = reader.getNumberOfPages();

            for (int i = 1; i <= n; i++) {

                // get page size and position
                over = stamper.getOverContent(i);
                over.saveState();

                // set transparency
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.5f);
                over.setGState(state);

                // add watermark text
                ColumnText.showTextAligned(over,
                        Element.ALIGN_CENTER, //Keep waterMark center aligned
                        phrase, 300f, 500f, 45f); // 45f is the rotation angle

                over.restoreState();
            }
        } catch (Exception ex) {
            throw new StorageException("StorageException.convertToPdf", ex);
        } finally {
            try {
                stamper.close();
            } catch (Exception ex) {
            }
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void deleteFile(File file) {
        if ((file != null) && file.exists()) {
            file.delete();
        }
    }

    @Override
    public void deleteFile(List<File> files) {
        H.each(files, (index, item) -> deleteFile(item));
    }

    @Override
    public InputStream getInputStream(InputStream inputStream, Boolean isEncrypt) {
        try {
            if (!isEncrypt) return inputStream;
            Key k = new SecretKeySpec(encryptKey.getBytes(), encryptAlgorithm);
            Cipher aes2 = Cipher.getInstance(encryptTransformation);
            aes2.init(Cipher.DECRYPT_MODE, k);
            return new CipherInputStream(inputStream, aes2);
        } catch (Exception ex) {
            throw new StorageException("StorageException.convertToPdf", ex);
        }
    }

    @Override
    public InputStream getInputStream(FileAttachDocument attachDocument) throws FileNotFoundException {
        return getInputStream(new FileInputStream(attachDocument.getFilePath()), H.isTrue(attachDocument.getIsEncrypt()));
    }

    @Override
    public File mergePdfFiles(File desFile, InputStream... inputStreams) {
        try {
            PDFMergerUtility utility = new PDFMergerUtility();
            for (InputStream inputStream : inputStreams) utility.addSource(inputStream);
            utility.setDestinationStream(new FileOutputStream(desFile));
            utility.mergeDocuments(null);
            return desFile;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getSubFolder() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}
