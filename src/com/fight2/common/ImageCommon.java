package com.fight2.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;

import com.fight2.model.CardImage;
import com.fight2.util.MD5;

public class ImageCommon {
    private final File imgFile;

    private final String imgFileName;

    private final String storeDir = System.getProperty("Fight2Server.root") + "upload";

    public ImageCommon(final File imgFile, final String imgFileName) {
        this.imgFile = imgFile;
        this.imgFileName = imgFileName;
    }

    public CardImage doUpload() {
        if (imgFile == null) {
            throw new RuntimeException("Attachment cannot be null!");
        }

        final String fileDir = makeFileDir();
        final String origFileName = makeFileName();
        final String origFilePath = fileDir + origFileName;

        final CardImage originalImg = saveImage(imgFile, origFilePath);

        return originalImg;
    }

    private String makeFileDir() {
        final Calendar calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final StringBuffer dir = new StringBuffer(256);
        dir.append('/').append(year).append('/').append(month).append('/').append(day).append('/');

        new File(storeDir + dir.toString()).mkdirs();
        return dir.toString();
    }

    private String makeFileName() {
        final StringBuffer fileName = new StringBuffer(256);

        final int extensionIndex = imgFileName.lastIndexOf(".");
        final String extension = imgFileName.substring(extensionIndex, imgFileName.length()).toLowerCase();
        fileName.append(MD5.crypt(imgFileName + System.currentTimeMillis()));
        fileName.append(extension);

        return fileName.toString();
    }

    private CardImage saveImage(final File file, final String filePath) {
        final String storyPath = storeDir + filePath;

        FileOutputStream outputStream = null;
        FileInputStream fileIn = null;
        try {
            outputStream = new FileOutputStream(storyPath);
            fileIn = new FileInputStream(file);
            final byte[] buffer = new byte[1024];
            int len;
            while ((len = fileIn.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fileIn.close();
                outputStream.close();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        return loadImageVo(filePath);
    }

    private CardImage loadImageVo(final String filePath) {
        final String storyPath = storeDir + filePath;
        final String imgUrlPrefix = "/upload";

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(storyPath));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final int width = bi.getWidth();
        final int height = bi.getHeight();

        final CardImage imageVo = new CardImage();
        imageVo.setUrl(imgUrlPrefix + filePath);
        imageVo.setWidth(width);
        imageVo.setHeight(height);

        return imageVo;
    }
}
