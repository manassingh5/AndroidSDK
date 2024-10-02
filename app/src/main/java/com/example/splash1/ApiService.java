package com.example.splash1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/Login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/updateProfile")
    Call<UserResponse> updateUserProfile(@Body UserProfile userProfile);


    @POST("/api/auth/updatepreferences")
    Call<ApiResponse> updatePreferences(@Query("userid") String userId, @Body UserPreferenceRequest request);

    @POST("/api/message/send") //
    Call<Void> sendMessage(@Body SendMessageRequest messageRequest);

    /*@POST("/api/message/save") //
    Call<Void> saveMessage(@Body MessageRequest messageRequest);*/

}