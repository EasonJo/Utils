package com.eason.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopyUtil {

    public static void copyFile(File srcFile, File destFile) {
        try {
            copyFile(srcFile, destFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        FileInputStream fis;
        FileOutputStream fos;
        FileChannel input;
        FileChannel output;
        if (destFile.exists() && destFile.isDirectory())
            throw new IOException((new StringBuilder()).append("Destination '").append(destFile).append("' exists but is a directory").toString());
        fis = null;
        fos = null;
        input = null;
        output = null;
        fis = new FileInputStream(srcFile);
        fos = new FileOutputStream(destFile);
        input = fis.getChannel();
        output = fos.getChannel();
        long size = input.size();
        long pos = 0L;
        long count = 0L;
        for (; pos < size; pos += output.transferFrom(input, pos, count)) {
            count = size - pos <= 0x3200000L ? size - pos : 0x3200000L;
        }

        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(fis);
        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(fis);
        if (srcFile.length() != destFile.length()) {
            throw new IOException((new StringBuilder()).append("Failed to copy full contents from '").append(srcFile).append("' to '").append(destFile).append("'").toString());
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null)
            throw new NullPointerException("Source must not be null");
        if (destFile == null)
            throw new NullPointerException("Destination must not be null");
        if (!srcFile.exists())
            throw new FileNotFoundException((new StringBuilder()).append("Source '").append(srcFile).append("' does not exist").toString());
        if (srcFile.isDirectory())
            throw new IOException((new StringBuilder()).append("Source '").append(srcFile).append("' exists but is a directory").toString());
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath()))
            throw new IOException((new StringBuilder()).append("Source '").append(srcFile).append("' and destination '").append(destFile).append("' are the same").toString());
        if (destFile.getParentFile() != null && !destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs())
            throw new IOException((new StringBuilder()).append("Destination '").append(destFile).append("' directory cannot be created").toString());
        if (destFile.exists() && !destFile.canWrite()) {
            throw new IOException((new StringBuilder()).append("Destination '").append(destFile).append("' exists but is read-only").toString());
        } else {
            doCopyFile(srcFile, destFile, preserveFileDate);
            return;
        }
    }
}
