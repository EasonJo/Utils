package com.eason.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    public static final char DIR_SEPARATOR_UNIX = 47;
    public static final char DIR_SEPARATOR_WINDOWS = 92;
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int SKIP_BUFFER_SIZE = 2048;
    private static char SKIP_CHAR_BUFFER[];
    private static byte SKIP_BYTE_BUFFER[];

    public IOUtils() {
    }

    public static void closeQuietly(Reader input) {
        closeQuietly(((Closeable) (input)));
    }

    public static void closeQuietly(Writer output) {
        closeQuietly(((Closeable) (output)));
    }

    public static void closeQuietly(InputStream input) {
        closeQuietly(((Closeable) (input)));
    }

    public static void closeQuietly(OutputStream output) {
        closeQuietly(((Closeable) (output)));
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(Socket sock) {
        if (sock != null)
            try {
                sock.close();
            } catch (IOException ioe) {
            }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, encoding);
        return output.toByteArray();
    }

    /**
     * @deprecated Method toByteArray is deprecated
     */

    public static byte[] toByteArray(String input) throws IOException {
        return input.getBytes();
    }

    public static char[] toCharArray(InputStream is) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream is, String encoding) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output, encoding);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(input, sw);
        return sw.toCharArray();
    }

    /**
     * @deprecated Method toString is deprecated
     */

    public static String toString(byte input[]) throws IOException {
        return new String(input);
    }

    /**
     * @deprecated Method toString is deprecated
     */

    public static String toString(byte input[], String encoding) throws IOException {
        if (encoding == null)
            return new String(input);
        else
            return new String(input, encoding);
    }

    public static List readLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        return readLines(((Reader) (reader)));
    }

    public static List readLines(InputStream input, String encoding) throws IOException {
        if (encoding == null) {
            return readLines(input);
        } else {
            InputStreamReader reader = new InputStreamReader(input, encoding);
            return readLines(((Reader) (reader)));
        }
    }

    public static List readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List list = new ArrayList();
        for (String line = reader.readLine(); line != null; line = reader.readLine())
            list.add(line);

        return list;
    }

    public static InputStream toInputStream(CharSequence input) {
        return toInputStream(input.toString());
    }

    public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
        return toInputStream(input.toString(), encoding);
    }

    public static InputStream toInputStream(String input) {
        byte bytes[] = input.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    public static InputStream toInputStream(String input, String encoding) throws IOException {
        byte bytes[] = encoding == null ? input.getBytes() : input.getBytes(encoding);
        return new ByteArrayInputStream(bytes);
    }

    public static void write(byte data[], OutputStream output) throws IOException {
        if (data != null)
            output.write(data);
    }

    public static void write(byte data[], Writer output) throws IOException {
        if (data != null)
            output.write(new String(data));
    }

    public static void write(byte data[], Writer output, String encoding) throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(new String(data, encoding));
    }

    public static void write(char data[], Writer output) throws IOException {
        if (data != null)
            output.write(data);
    }

    public static void write(char data[], OutputStream output) throws IOException {
        if (data != null)
            output.write((new String(data)).getBytes());
    }

    public static void write(char data[], OutputStream output, String encoding) throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write((new String(data)).getBytes(encoding));
    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null)
            write(data.toString(), output);
    }

    public static void write(CharSequence data, OutputStream output) throws IOException {
        if (data != null)
            write(data.toString(), output);
    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        if (data != null)
            write(data.toString(), output, encoding);
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null)
            output.write(data);
    }

    public static void write(String data, OutputStream output) throws IOException {
        if (data != null)
            output.write(data.getBytes());
    }

    public static void write(String data, OutputStream output, String encoding) throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(data.getBytes(encoding));
    }

    /**
     * @deprecated Method write is deprecated
     */

    public static void write(StringBuffer data, Writer output) throws IOException {
        if (data != null)
            output.write(data.toString());
    }

    /**
     * @deprecated Method write is deprecated
     */

    public static void write(StringBuffer data, OutputStream output) throws IOException {
        if (data != null)
            output.write(data.toString().getBytes());
    }

    /**
     * @deprecated Method write is deprecated
     */

    public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
        if (data != null)
            if (encoding == null)
                write(data, output);
            else
                output.write(data.toString().getBytes(encoding));
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 0x7fffffffL)
            return -1;
        else
            return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte buffer[] = new byte[4096];
        long count = 0L;
        for (int n = 0; -1 != (n = input.read(buffer)); ) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    public static void copy(InputStream input, Writer output) throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(((Reader) (in)), output);
    }

    public static void copy(InputStream input, Writer output, String encoding) throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, encoding);
            copy(((Reader) (in)), output);
        }
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 0x7fffffffL)
            return -1;
        else
            return (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        char buffer[] = new char[4096];
        long count = 0L;
        for (int n = 0; -1 != (n = input.read(buffer)); ) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    public static void copy(Reader input, OutputStream output) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, ((Writer) (out)));
        out.flush();
    }

    public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
        if (encoding == null) {
            copy(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encoding);
            copy(input, ((Writer) (out)));
            out.flush();
        }
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        if (!(input1 instanceof BufferedInputStream))
            input1 = new BufferedInputStream(input1);
        if (!(input2 instanceof BufferedInputStream))
            input2 = new BufferedInputStream(input2);
        int ch2;
        for (int ch = input1.read(); -1 != ch; ch = input1.read()) {
            ch2 = input2.read();
            if (ch != ch2)
                return false;
        }

        ch2 = input2.read();
        return ch2 == -1;
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        if (!(input1 instanceof BufferedReader))
            input1 = new BufferedReader(input1);
        if (!(input2 instanceof BufferedReader))
            input2 = new BufferedReader(input2);
        int ch2;
        for (int ch = input1.read(); -1 != ch; ch = input1.read()) {
            ch2 = input2.read();
            if (ch != ch2)
                return false;
        }

        ch2 = input2.read();
        return ch2 == -1;
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L)
            throw new IllegalArgumentException((new StringBuilder()).append("Skip count must be non-negative, actual: ").append(toSkip).toString());
        if (SKIP_BYTE_BUFFER == null)
            SKIP_BYTE_BUFFER = new byte[2048];
        long remain = toSkip;
        do {
            if (remain <= 0L)
                break;
            long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, 2048L));
            if (n < 0L)
                break;
            remain -= n;
        } while (true);
        return toSkip - remain;
    }

    public static long skip(Reader input, long toSkip) throws IOException {
        if (toSkip < 0L)
            throw new IllegalArgumentException((new StringBuilder()).append("Skip count must be non-negative, actual: ").append(toSkip).toString());
        if (SKIP_CHAR_BUFFER == null)
            SKIP_CHAR_BUFFER = new char[2048];
        long remain = toSkip;
        do {
            if (remain <= 0L)
                break;
            long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, 2048L));
            if (n < 0L)
                break;
            remain -= n;
        } while (true);
        return toSkip - remain;
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L)
            throw new IllegalArgumentException((new StringBuilder()).append("Bytes to skip must not be negative: ").append(toSkip).toString());
        long skipped = skip(input, toSkip);
        if (skipped != toSkip)
            throw new EOFException((new StringBuilder()).append("Bytes to skip: ").append(toSkip).append(" actual: ").append(skipped).toString());
        else
            return;
    }

    public static void skipFully(Reader input, long toSkip) throws IOException {
        long skipped = skip(input, toSkip);
        if (skipped != toSkip)
            throw new EOFException((new StringBuilder()).append("Bytes to skip: ").append(toSkip).append(" actual: ").append(skipped).toString());
        else
            return;
    }

}
