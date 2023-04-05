package com.trevor.CoCoT;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadService {

    public FileUploadService() {
    }

    public void uploadFile(File file, int participantID, Callback callback) {

        Log.d(constants.LOG_TAG, "Starting file upload service...");
        try {
            String url = constants.SERVER_URL;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("text/csv"), file))
                    .addFormDataPart("participantID", String.valueOf(participantID))
                    .addFormDataPart("fingerPrint", FingerprintGenerator.getFingerPrint())
                    .build();

            Request requestBuilder = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = builder.build();

            client.newCall(requestBuilder).enqueue(new UploadCallback(callback));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(constants.LOG_TAG, "Error uploading file: " + e.getMessage());
        }
    }


    protected class UploadCallback implements Callback {
        Callback callback;
        public UploadCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(constants.LOG_TAG, "Error uploading file: " + e.getMessage());
            callback.onFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.v(constants.LOG_TAG, "Response: " + response.body().string());
            if (response.isSuccessful() || response.body().toString().contains("file uploaded successfully")) {
                Log.d(constants.LOG_TAG, "File upload successful");

                callback.onResponse(call, response);


            } else {
                Log.e(constants.LOG_TAG, "File upload failed");
            }
        }
    }
}
