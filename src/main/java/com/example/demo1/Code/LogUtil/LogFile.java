package com.example.demo1.Code.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志输出工具 <br/><br/>
 *
 * 可以输出到控制台和指定的文件中, 分为4个级别, 由低到高分别为: debug, info, warn, error
 *
 * <br/><br/>
 *
 * 输出级别:
 *
 * <ul>
 *     <li> debug: 输出 debug, info, warn, error </li>
 *     <li> info: 输出 info, warn, error </li>
 *     <li> warn: 输出 warn, error </li>
 *     <li> error: 输出 error </li>
 * </ul>
 *
 * 默认为 info 输出级别
 *
 * <p/>
 *
 * Demo:
 *
 * <pre>{@code
 *     // (可选) 设置日志输出级别, 默认为 INFO 级别
 *     LogFile.setLogOutLevel(LogFile.Level.DEBUG);
 *
 *     // (可选) 设置日志输出文件(追加到文件尾部)
 *     LogFile.setLogOutFile(new File("MyLog.log"));
 *
 *     // (可选) 设置日志输出位置(是否输出到控制台 和 是否输出到文件), 默认只输出到控制台, 不输出到文件
 *     LogFile.setLogOutTarget(true, true);
 *
 *     // 输出日志
 *     LogFile.debug("TAG", "The debug log.");
 *     LogFile.info("TAG", "The info log.");
 *     LogFile.warn("TAG", "The warn log.");
 *     LogFile.error("TAG", "The error log.");
 * }</pre>
 *
 * @author xietansheng
 */
public class LogFile {

    /** 每条 Log 的 tag 输出的最大长度, 超过部分将被截断 */
    private static final int TAG_MAX_LENGTH = 20;

    /** 每条 Log 的 message 输出的最大长度, 超过部分将被截断 */
    private static final int MESSAGE_MAX_LENGTH = 1024;

    /** 日期前缀格式化 */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");

    /** 日志当前的输出级别, 默认为 INFO 级别 */
    private static Level logOutLevel = Level.INFO;

    /** 是否输出到控制台, 默认输出 */
    private static boolean isOutToConsole = false;

    /** 是否输出到文件 */
    private static boolean isOutToFile = true;

    /** 日志输出文件, 追加到文件尾 */
    private static File logOutFile = new File("MyLog");

    /** 日志文件输出流, 追加到文件尾  */
    private static RandomAccessFile logOutFileStream;
    private String message;//详细输出数据
    public static void setLogOutLevel(Level currentLevel) {
        if (currentLevel == null) {
            currentLevel = Level.INFO;
        }
        LogFile.logOutLevel = currentLevel;
    }

    public static synchronized void setLogOutFile(File logOutFile) throws IOException {
        LogFile.logOutFile = logOutFile;

        if (logOutFileStream != null) {
            closeStream(logOutFileStream);
            logOutFileStream = null;
        }

        if (LogFile.logOutFile != null) {
            try {
                logOutFileStream = new RandomAccessFile(LogFile.logOutFile, "rw");
                logOutFileStream.seek(LogFile.logOutFile.length());
            } catch (IOException e) {
                closeStream(logOutFileStream);
                logOutFileStream = null;
                throw e;
            }
        }
    }

    public static void setLogOutTarget(boolean isOutToConsole, boolean isOutToFile) {
        LogFile.isOutToConsole = isOutToConsole;
        LogFile.isOutToFile = isOutToFile;
    }

    public static void debug(String tag, String message) {
        printLog(Level.DEBUG, tag, "The debug log."+message, false);
    }

    public static void info(String tag, String message) {
        printLog(Level.INFO, tag, "The info log."+message, false);
    }

    public static void warn(String tag, String message) {
        printLog(Level.WARN, tag, "The warn log."+message, false);
    }

    public static void error(String tag, String message) {
        printLog(Level.ERROR, tag, "The error log."+message, true);
    }

    public static void error(String tag, Exception e) {
        if (e == null) {
            error(tag, (String) null);
            return;
        }

        PrintStream printOut = null;

        try {
            ByteArrayOutputStream bytesBufOut = new ByteArrayOutputStream();
            printOut = new PrintStream(bytesBufOut);
            e.printStackTrace(printOut);
            printOut.flush();
            error(tag, bytesBufOut.toString("UTF-8"));

        } catch (Exception e1) {
            e1.printStackTrace();

        } finally {
            closeStream(printOut);
        }
    }

    private static void printLog(Level level, String tag, String message, boolean isOutToErr) {
        if (level.getLevelValue() >= logOutLevel.getLevelValue()) {
            String log = DATE_FORMAT.format(new Date()) +
                    " " +
                    level.getTag() +
                    "/" +
                    checkTextLengthLimit(tag, TAG_MAX_LENGTH) +
                    ": " +
                    checkTextLengthLimit(message, MESSAGE_MAX_LENGTH);

            if (isOutToConsole) {
                outLogToConsole(isOutToErr, log);
            }
            if (isOutToFile) {
                outLogToFile(log);
            }
        }
    }

    private static void outLogToConsole(boolean isOutToErr, String log) {
        if (isOutToErr) {
            // System.err 和 System.out 是两个不同的输出流通道, 如果极短时间内连
            // 续输出 log 到 err 和 out, 控制台上的打印顺序可能会不完全按时序打印.
            System.err.println(log);
        } else {
            System.out.println(log);
        }
    }

    private static synchronized void outLogToFile(String log) {
        if (logOutFileStream != null) {
            try {
                logOutFileStream.write((log + "\n").getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String checkTextLengthLimit(String text, int maxLength) {
        if ((text != null) && (text.length() >  maxLength)) {
            text = text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public static enum Level {
        DEBUG("D", 1), INFO("I", 2), WARN("W", 3), ERROR("E", 4);

        private String tag;

        private int levelValue;

        private Level(String tag, int levelValue) {
            this.tag = tag;
            this.levelValue = levelValue;
        }

        public String getTag() {
            return tag;
        }

        public int getLevelValue() {
            return levelValue;
        }
    }

    public static void main(String args[]) {
        final String TAG = "Main";
            // (可选) 设置日志输出级别, 默认为 INFO 级别
            LogFile.setLogOutLevel(Level.DEBUG);
            // (可选) 设置日志输出文件(追加到文件尾部)
        try {
            LogFile.setLogOutFile(new File("MyLog.log"));
        } catch (IOException e) {
            LogFile.error(TAG, "The error log.无法打开日志输出文件");
            e.printStackTrace();
        }
        // (可选) 设置日志输出位置(是否输出到控制台 和 是否输出到文件), 默认只输出到控制台, 不输出到文件
            LogFile.setLogOutTarget(true, true);
            // 输出日志
            LogFile.debug(TAG, "The debug log.");
            LogFile.info(TAG, "The info log.");
            LogFile.warn(TAG, "The warn log.");
            LogFile.error(TAG, "The error log.无法打开日志输出文件");
            LogFile.info("Student","查询课程"+1);

        }

}
