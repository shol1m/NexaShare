package com.example.nexashare;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class FCMDataNotificationSender {

    public static final String API_URL = "https://fcm.googleapis.com/v1/projects/nexashare/messages:send";
    public static final String YOUR_SERVER_KEY = "AAAAJglKiWM:APA91bHrLxBAvmeyS2xnQCdgxLctT3HI9B9Bc7URlDyk4WOJL7kkwcHmxyZjTO0YizVlXy_DUNZcIC4skvfkWmeTZuAspjMGIvlYIJZR9XYWS-t4dM8WAO4GkXDYSMPVKlZtbt_isKmH"; // Your Firebase server key

    public static void sendNotification(String deviceToken, String title, String message) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        JSONObject messageJson = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject target = new JSONObject();

        try {
            data.put("title", title);
            data.put("message", message);

            messageJson.put("data", data);
            messageJson.put("token", deviceToken);

            target.put("message", messageJson);

            json.put("message", target);

            RequestBody requestBody = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + YOUR_SERVER_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        System.out.println("Notification sent successfully: " + responseData);
                    } else {
                        System.out.println("Failed to send notification: " + response.body().string());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String deviceToken = "SPECIFIC_DEVICE_TOKEN"; // Replace with the FCM token of the target device
        String title = "Title of the Data Notification";
        String message = "Message of the Data Notification";

        sendNotification(deviceToken, title, message);
    }
}
