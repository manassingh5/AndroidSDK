package com.example.splash1;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/api/auth/Login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/auth/Register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/sendMessage")
    Call<MessageResponse> sendMessage(@Body MessageRequest request);

    @POST("/api/auth/editprofile")
    Call<UserDetailResponse> updateUserProfile(
            @Query("userid") String userId,
            @Body UserDetailRequest userDetailRequest
    );

    @POST("api/schedule/addschedule")
    Call<ScheduleIdResponse> scheduleMessage(@Query("UserId") String userId, @Body ScheduledMessage scheduledMessage);
    @GET("/api/schedule/getschedule")
    Call<ScheduleResponse> getScheduledMessages(@Query("UserId") String userId);

//   @GET("api/schedule/getschedule")
//    Call<ScheduleDetailsResponse> getScheduledMessage(
//            @Query("scheduleId") String scheduleId
//    );
    @POST("/api/message/send") //
    Call<Void> sendMessage(@Body SendMessageRequest messageRequest);

    @GET("/api/auth/UserDetail")
    Call<UserDetailResponse> getUserDetailById(@Query("UserId") String userId);

    @POST("/api/auth/updatepreferences")
    Call<ApiResponse> updatePreferences(@Query("userid") String userId, @Body UserPreferenceRequest request);

    @POST("message/updateMessageCount") //
    Call<ResponseBody> updateMessageCount(@Body UpdateMessageCountRequest request);

    @POST("message/GetMessageByUserId")
    Call<List<Message>> getMessagesByUserId(@Query("userId") String userId);
}
