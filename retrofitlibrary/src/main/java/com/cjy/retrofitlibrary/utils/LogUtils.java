package com.cjy.retrofitlibrary.utils;

import android.util.Log;


/**
 * log工具类
 * Data：2018/12/18
 *
 * @author yong
 */
public class LogUtils {

    private static final String TAG = "log_yong";

    LogUtils() {
        throw new IllegalStateException("LogUtils class");
    }

    /**
     * 输出debug调试信息
     *
     * @param msg
     */
    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String tag, Object msg) {
        d(tag + ":  " + msg);
    }

    /**
     * 输出数据资源信息
     *
     * @param msg
     */
    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(tag + ":  " + msg);
    }

    /**
     * 输出错误信息
     *
     * @param msg
     */
    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag + ":  " + msg);
    }

    /**
     * 输出警告信息
     *
     * @param msg
     */
    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void w(Throwable tr) {
        Log.w(TAG, tr);
    }

    public static void w(String msg, Throwable tr) {
        Log.w(TAG, msg, tr);
    }

    public static void w(String tag, Object msg) {
        w(tag + ":  " + msg);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        w(tag + ":  " + msg, tr);
    }

    public static void biglog(String responseInfo) {
        if (responseInfo.length() > 3000) {
            int chunkCount = responseInfo.length() / 3000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 3000 * (i + 1);
                if (max >= responseInfo.length()) {
                    Log.e(TAG, responseInfo.substring(3000 * i));
                } else {
                    Log.e(TAG, responseInfo.substring(3000 * i, max));
                }
            }
        } else {
            Log.e(TAG, responseInfo);
        }
    }
}