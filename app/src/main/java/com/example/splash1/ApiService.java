package com.example.splash1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/api/Auth/Login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/Auth/Register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

   /* @POST("/api/sendMessage")
    Call<MessageResponse> sendMessage(@Body MessageRequest request);*/

    @POST("/api/auth/editprofile")
    Call<UserResponse> updateUserProfile(
            @Query("userid") String userId,  // Query parameter for user ID
            @Body UserProfile userProfile
    );

    @GET("/api/auth/UserDetail")
    Call<UserDetailResponse> getUserDetailById(@Query("UserId") String userId);

    @POST("/api/auth/updatepreferences")
    Call<ApiResponse> updatePreferences(@Query("userid") String userId, @Body UserPreferenceRequest request);

    @POST("/api/Message/Send") //
    Call<Void> sendMessage(@Body SendMessageRequest messageRequest);

    /*@POST("/api/message/save") //
    Call<Void> saveMessage(@Body MessageRequest messageRequest);*/
}
