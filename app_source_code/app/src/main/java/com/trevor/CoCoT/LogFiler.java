package com.trevor.CoCoT;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LogFiler implements Runnable{
    private Context context;

    private int participantID;
    private String logFileName;
    private FileOutputStream logFileStream;
    private File completedLogFile;

    private Queue<String> logQueue;
    private int logFileLineCount;
    private List<File> logFileList;
    private List<File> uploadedLogFileList;

    public LogFiler(Context context) {
        this.context = context;
        this.logFileList = new java.util.ArrayList<>();
        this.uploadedLogFileList = new java.util.ArrayList<>();

        this.logQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
    }
    @Override
    public void run() {
        while (true){
            if (!logQueue.isEmpty()){
                logLineToFile(logQueue.poll());
            }
        }
    }

    protected void logLineToFile(String logData) {
        if (logFileStream == null) {
            createLogBuffer();
        }
        try {
            logFileStream.write(logData.getBytes());
            logFileLineCount++;
        } catch (IOException e) {
            Log.e(constants.LOG_TAG, "error in log file writing:\n" + e.getMessage());
        }

        if (logFileLineCount >= constants.MAX_LOG_LINES){
            File closedLogFile = closeLogFile(true);
            logFileList.add(closedLogFile);
            uploadLogFile(closedLogFile);
        }
    }

    protected void logLine(String logData) {
        //check logData ends in NewLine
        if (!logData.endsWith("\n")) {
            logData += "\n";
        }

        //prepend logData with timestamp
        String timeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        logData = timeStamp + "," + logData;

        logQueue.add(logData);
    }
    protected File createNamedFileForLog() {
        String currentDay = Calendar.getInstance().getTime().toString();
        String newLogName = "Participant" + participantID + "_Finger" + FingerprintGenerator.getFingerPrint() + "_" + currentDay.replace(" ", "_") + ".csv";
        File newLogFile = new File(context.getFilesDir(), newLogName);
        return newLogFile;
    }

    protected void createLogBuffer() {

        logFileName = "CoCoTLogFile.csv";
        File logFile = new File(context.getFilesDir(), logFileName);
        if (logFile.exists()){
            Log.d(constants.LOG_TAG,"Moving previous logfile");

            logFile.renameTo(createNamedFileForLog());
        }

        try {
            logFileStream = context.openFileOutput(logFileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e(constants.LOG_TAG, "error in log file creation:\n" + e.getMessage());
        }

        logFileLineCount = 0;
    }

    protected void writeLogFileHeader() {
        String model = Build.MODEL;
        logLine(context.getString(R.string.LogFile_deviceModel, model));
        logLine(context.getString(R.string.LogFile_fingerprint, FingerprintGenerator.getFingerPrint()));
        logLine(context.getString(R.string.LogFile_participantID, participantID));
    }

    protected File closeLogFile(boolean keepLogFile) {
        if (isLogFileStreamOpen()) {
            logLine("End of log file");
            try {
                logFileStream.close();
                logFileStream = null;
                if (keepLogFile) {
                    File logFile = new File(context.getFilesDir(), logFileName);
                    completedLogFile = createNamedFileForLog();
                    logFile.renameTo(completedLogFile);
                    Log.d(constants.LOG_TAG, "Moved log file to " + completedLogFile.getName());

                    logFileList.add(completedLogFile);

                    return completedLogFile;
                } else {
                    context.deleteFile(logFileName);
                }
            } catch (IOException e) {
                Log.e(constants.LOG_TAG, "error closing logfile:\n" + e.getMessage());
            }
        }
        return null;
    }

    protected void setParticipantID(int ID){
        this.participantID = ID;
    }

    protected boolean isLogFileStreamOpen(){
        return logFileStream != null;
    }


    public List<File> getLogFileList() {
        return logFileList;
    }

    public File getCompletedLogFile(){
        return completedLogFile;
    }

    public List<File> getUnuploadedLogFileList() {
        List<File> unuploadedLogFileList = new java.util.ArrayList<>();
        for (File file : logFileList) {
            if (!uploadedLogFileList.contains(file)) {
                unuploadedLogFileList.add(file);
            }
        }
        return unuploadedLogFileList;
    }

    private void uploadLogFile(File closedLogFile) {
        Log.d(constants.LOG_TAG, "Uploading log file " + closedLogFile.getName());
        FileUploadService uploadService = new FileUploadService();
        try {
            uploadService.uploadFile(getCompletedLogFile(), participantID,
                    new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.d(constants.LOG_TAG, "Log file upload failed");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            Log.d(constants.LOG_TAG, "Log file upload successful");
                            uploadedLogFileList.add(closedLogFile);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(constants.LOG_TAG, "failed to upload log file: " + e.getMessage());
        }

    }

    public void uploadUnuploadedLogFiles() {
        if (isLogFileStreamOpen()){
            closeLogFile(true);
            logFileList.add(completedLogFile);
        }
        for (File file : getUnuploadedLogFileList()) {
            uploadLogFile(file);
        }
    }



}

