package com.trevor.CoCoT;

import android.content.Context;

public class LogFileAccesser {
    private static LogFiler logFiler;

    public static LogFiler getLogFiler(Context context){
        if (logFiler==null){
            logFiler = new LogFiler(context);
            Thread logFilerThread = new Thread(logFiler);
            logFilerThread.start();
        }
        return logFiler;
    }


}
