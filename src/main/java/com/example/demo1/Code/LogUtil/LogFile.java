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
 * 日志输出类
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
    /**
     * 设置日志输出等级
     */
    public static void setLogOutLevel(Level currentLevel) {
        if (currentLevel == null) {
            currentLevel = Level.INFO;
        }
        LogFile.logOutLevel = currentLevel;
    }

    /**
     * 异常检查
     * @param logOutFile 日志文件
     */
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
    /**
     * 设置日志输出方式
     */
    public static void setLogOutTarget(boolean isOutToConsole, boolean isOutToFile) {
        LogFile.isOutToConsole = isOutToConsole;
        LogFile.isOutToFile = isOutToFile;
    }

    /**
     * DEBUG级日志输出
     * @param tag 标志
     * @param message 信息
     */
    public static void debug(String tag, String message) {
        printLog(Level.DEBUG, tag, "The debug log."+message, false);
    }
    /**
     * INFO级日志输出
     * @param tag 标志
     * @param message 信息
     */
    public static void info(String tag, String message) {
        printLog(Level.INFO, tag, "The info log."+message, false);
    }
    /**
     * WARN级日志输出
     * @param tag 标志
     * @param message 信息
     */
    public static void warn(String tag, String message) {
        printLog(Level.WARN, tag, "The warn log."+message, false);
    }
    /**
     * ERROR级日志输出
     * @param tag 标志
     * @param message 信息
     */
    public static void error(String tag, String message) {
        printLog(Level.ERROR, tag, "The error log."+message, true);
    }
    /**
     * ERROR级日志输出
     * @param tag 标志
     * @param e 异常
     */
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

    /**
     * 打印日志
     * @param level 日志等级
     * @param tag 标志
     * @param message 信息
     * @param isOutToErr 是否输出到控制台
     */
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

    /**
     * 将控制台信息打印到日志上
     * @param isOutToErr 是否输出到控制台
     * @param log 日志信息
     */
    private static void outLogToConsole(boolean isOutToErr, String log) {
        if (isOutToErr) {
            // System.err 和 System.out 是两个不同的输出流通道, 如果极短时间内连
            // 续输出 log 到 err 和 out, 控制台上的打印顺序可能会不完全按时序打印.
            System.err.println(log);
        } else {
            System.out.println(log);
        }
    }
    /**
     * 将控制台信息打印到文件内
     * @param log 日志信息
     */
    private static synchronized void outLogToFile(String log) {
        if (logOutFileStream != null) {
            try {
                logOutFileStream.write((log + "\n").getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 日志长度检查
     * @param text 日志信息
     * @param maxLength 限定长度
     * @return  符合规范的新信息
     */
    private static String checkTextLengthLimit(String text, int maxLength) {
        if ((text != null) && (text.length() >  maxLength)) {
            text = text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

    /**
     * 关闭输出流
     * @param stream 输出流
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

    /**
     * 日志等级
     */
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
