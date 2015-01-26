package com.eason.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Util to control IO Stream
 */
public class StreamUtil {

    public static StreamUtil instance = new StreamUtil();

    public static StreamUtil getInstance() {
        return instance;
    }

    public static void streamToFile(InputStream in, File file) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int length = 0;
            while ((length = in.read(buffer, 0, buffer.length)) != -1) {
                fileOut.write(buffer, 0, length);
            }
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        return os.toByteArray();
    }

    public String streamToString(InputStream inputStream, OnReadStreamHelper helper) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (helper != null) {
                    helper.onReaderLine(line);
                    if (helper.isTeminal()) {
                        break;
                    }
                }
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public interface OnReadStreamHelper {
        boolean isTeminal();

        void onReaderLine(String line);
    }

}
