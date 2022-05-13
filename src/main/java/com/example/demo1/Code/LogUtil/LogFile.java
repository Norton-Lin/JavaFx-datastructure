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
 * ��־������� <br/><br/>
 *
 * �������������̨��ָ�����ļ���, ��Ϊ4������, �ɵ͵��߷ֱ�Ϊ: debug, info, warn, error
 *
 * <br/><br/>
 *
 * �������:
 *
 * <ul>
 *     <li> debug: ��� debug, info, warn, error </li>
 *     <li> info: ��� info, warn, error </li>
 *     <li> warn: ��� warn, error </li>
 *     <li> error: ��� error </li>
 * </ul>
 *
 * Ĭ��Ϊ info �������
 *
 * <p/>
 *
 * Demo:
 *
 * <pre>{@code
 *     // (��ѡ) ������־�������, Ĭ��Ϊ INFO ����
 *     LogFile.setLogOutLevel(LogFile.Level.DEBUG);
 *
 *     // (��ѡ) ������־����ļ�(׷�ӵ��ļ�β��)
 *     LogFile.setLogOutFile(new File("MyLog.log"));
 *
 *     // (��ѡ) ������־���λ��(�Ƿ����������̨ �� �Ƿ�������ļ�), Ĭ��ֻ���������̨, ��������ļ�
 *     LogFile.setLogOutTarget(true, true);
 *
 *     // �����־
 *     LogFile.debug("TAG", "The debug log.");
 *     LogFile.info("TAG", "The info log.");
 *     LogFile.warn("TAG", "The warn log.");
 *     LogFile.error("TAG", "The error log.");
 * }</pre>
 *
 * @author xietansheng
 */
public class LogFile {

    /** ÿ�� Log �� tag �������󳤶�, �������ֽ����ض� */
    private static final int TAG_MAX_LENGTH = 20;

    /** ÿ�� Log �� message �������󳤶�, �������ֽ����ض� */
    private static final int MESSAGE_MAX_LENGTH = 1024;

    /** ����ǰ׺��ʽ�� */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");

    /** ��־��ǰ���������, Ĭ��Ϊ INFO ���� */
    private static Level logOutLevel = Level.INFO;

    /** �Ƿ����������̨, Ĭ����� */
    private static boolean isOutToConsole = false;

    /** �Ƿ�������ļ� */
    private static boolean isOutToFile = true;

    /** ��־����ļ�, ׷�ӵ��ļ�β */
    private static File logOutFile = new File("MyLog");

    /** ��־�ļ������, ׷�ӵ��ļ�β  */
    private static RandomAccessFile logOutFileStream;
    private String message;//��ϸ�������
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
            // System.err �� System.out ��������ͬ�������ͨ��, �������ʱ������
            // ����� log �� err �� out, ����̨�ϵĴ�ӡ˳����ܻ᲻��ȫ��ʱ���ӡ.
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
            // (��ѡ) ������־�������, Ĭ��Ϊ INFO ����
            LogFile.setLogOutLevel(Level.DEBUG);
            // (��ѡ) ������־����ļ�(׷�ӵ��ļ�β��)
        try {
            LogFile.setLogOutFile(new File("MyLog.log"));
        } catch (IOException e) {
            LogFile.error(TAG, "The error log.�޷�����־����ļ�");
            e.printStackTrace();
        }
        // (��ѡ) ������־���λ��(�Ƿ����������̨ �� �Ƿ�������ļ�), Ĭ��ֻ���������̨, ��������ļ�
            LogFile.setLogOutTarget(true, true);
            // �����־
            LogFile.debug(TAG, "The debug log.");
            LogFile.info(TAG, "The info log.");
            LogFile.warn(TAG, "The warn log.");
            LogFile.error(TAG, "The error log.�޷�����־����ļ�");
            LogFile.info("Student","��ѯ�γ�"+1);

        }

}
