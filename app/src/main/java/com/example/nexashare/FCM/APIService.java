package com.example.nexashare.FCM;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAJglKiWM:APA91bHrLxBAvmeyS2xnQCdgxLctT3HI9B9Bc7URlDyk4WOJL7kkwcHmxyZjTO0YizVlXy_DUNZcIC4skvfkWmeTZuAspjMGIvlYIJZR9XYWS-t4dM8WAO4GkXDYSMPVKlZtbt_isKmH"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

