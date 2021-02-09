package com.example.pianopatienttracker.network;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceInterface {

    @POST("auth/login")
    @FormUrlEncoded
    Call<ResponseBody> login(@FieldMap Map<String, String> map);

    @Headers({"Accept:application/json", "Content-Type:application/x-www-form-urlencoded;"})
    @POST("auth/register")
    @FormUrlEncoded
    Call<ResponseBody> register(@FieldMap Map<String, String> map);

    @POST("auth/forgotPass")
    @FormUrlEncoded
    Call<ResponseBody> forgotPassword(@FieldMap Map<String, String> map);

    @POST("auth/update-password")
    @FormUrlEncoded
    Call<ResponseBody> updatePassword(@FieldMap Map<String, String> map, @Header("Authorization") String auth);

    @GET("auth/me")
    Call<ResponseBody> getProfile(@Header("Authorization") String auth);


    @GET("homepage")
    Call<ResponseBody> getHomeData();

    @GET("news")
    Call<ResponseBody> getNews(@Query("page") int pageNum);

    @GET("products")
    Call<ResponseBody> getProducts(@Query("page") int pageNum);

    @GET("tournaments")
    Call<ResponseBody> getTournaments();

    @GET("tournaments/{tournamentId}")
    Call<ResponseBody> getTournamentDetails(@Path("tournamentId") int tournamentId);

    @GET("tournament/{tournamentId}/game/{gameId}")
    Call<ResponseBody> getTournamentGameDetails(@Path("tournamentId") int tournamentId,@Path("gameId") int gameId);

    @Multipart
    @POST("save-team-image")
    Call<ResponseBody> saveTeamData(@Part MultipartBody.Part image, @Part("team_name") RequestBody teamName);


    @POST("mobile-tournament-registration")
    @FormUrlEncoded
    Call<ResponseBody> registerTeamPlayers(@FieldMap Map<String, Object> map);


    //old
    @GET("showroom")
    Call<ResponseBody> getShowroom();

    @GET("tournament")
    Call<ResponseBody> getTournament();

    @GET("speakers")
    Call<ResponseBody> getSpeakers();

    @GET("dedicated-rooms")
    Call<ResponseBody> getDedicatedRooms();

    @GET("live-stream")
    Call<ResponseBody> getStream();

    @GET("events")
    Call<ResponseBody> getEvents();

    @POST("auth/update-profile")
    @FormUrlEncoded
    Call<ResponseBody> updateProfile(@FieldMap Map<String, String> map, @Header("Authorization") String auth);



}