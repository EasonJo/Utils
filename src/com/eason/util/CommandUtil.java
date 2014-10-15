/**
 这种常规的操作，如果执行一条Ping命令，当在某个ip下卡住ping不通时，就有问题了，会发现代码一直会阻塞在br.readLine()的地方，
 任何办法都不好解决，网上说的把操作放在另外一个Thread里进行，只是解决了process.waiFor()的阻塞问题。其实也解决不了当进行
 ping不通时readline阻塞的问题，在ping命令的操作下使用readline()并不像读取一个文件，当遇到换行时会结束，ping不通，只能一直阻塞着，
 除非在外部进行close等操作。
 结合国内外论坛，终于找到一个办法，我写成了一个方法类，供大家参考（可直接调用）
 */
package com.eason.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 执行命令行工具类,可以用来执行一些Android Shell的命令.
 */
public class CommandUtil {

    public static final String TAG = CommandUtil.class.getSimpleName();
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_LINE_END = "\n";
    public static final String COMMAND_EXIT = "exit\n";
    private static final boolean ISDEBUG = true;

    /**
     * 执行单条命令
     *
     * @param command
     * @return
     */
    public static List<String> execute(String command) {
        return execute(new String[]{command});
    }

    /**
     * 可执行多行命令（bat）
     *
     * @param commands
     * @return
     */
    public static List<String> execute(String[] commands) {
        List<String> results = new ArrayList<String>();
        int status = -1;
        if (commands == null || commands.length == 0) {
            return null;
        }
        debug("execute command start : " + commands);
        Process process = null;
        BufferedReader successReader = null;
        BufferedReader errorReader = null;
        StringBuilder errorMsg = null;

        DataOutputStream dos = null;
        try {
            // TODO
            process = Runtime.getRuntime().exec(COMMAND_SH);
            dos = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                dos.write(command.getBytes());
                dos.writeBytes(COMMAND_LINE_END);
                dos.flush();
            }
            dos.writeBytes(COMMAND_EXIT);
            dos.flush();

            status = process.waitFor();

            errorMsg = new StringBuilder();
            successReader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));
            String lineStr;
            while ((lineStr = successReader.readLine()) != null) {
                results.add(lineStr);
                debug(" command line item : " + lineStr);
            }
            while ((lineStr = errorReader.readLine()) != null) {
                errorMsg.append(lineStr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (successReader != null) {
                    successReader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        debug(String.format(Locale.CHINA,
            "execute command end,errorMsg:%s,and status %d: ", errorMsg,
            status));
        return results;
    }

    /**
     * DEBUG LOG
     *
     * @param message
     */
    private static void debug(String message) {
        if (ISDEBUG) {
            Log.d(TAG, message);
        }
    }
}
