package com.cat.pianopatienttracker.network;

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
import retrofit2.http.QueryMap;

public interface ServiceInterface {

    @GET("auth/metest")
    Call<ResponseBody> getMyProfile(@Header("Authorization") String auth);

    @GET("admin/get-brands")
    Call<ResponseBody> getBrands(@Header("Authorization") String auth);

    @GET("admin/get-countries")
    Call<ResponseBody> getCountries(@Header("Authorization") String auth);

    @GET("get-health-sectors")
    Call<ResponseBody> getSectors(@Header("Authorization") String auth);

    @GET("dashboard")
    Call<ResponseBody> getDashboard(@Header("Authorization") String auth, @Query("country_id") int countryId, @Query("brand_id") int brandId);


    @GET("ranking-slice")
    Call<ResponseBody> getRankingFlm(@Header("Authorization") String auth,@Query("brand_id") int brandId, @Query("type") String type,@QueryMap Map<String, String> filters);


    @GET("ranking-slice")
    Call<ResponseBody> getRanking(@Header("Authorization") String auth, @Query("country_id") int countryId, @Query("brand_id") int brandId, @Query("type") String type);

    @GET("ranking-slice")
    Call<ResponseBody> getRankingFiltered(@Header("Authorization") String auth, @Query("country_id") int countryId, @Query("brand_id") int brandId, @Query("type") String type,@QueryMap Map<String, String> filters);

    @GET("get-progress")
    Call<ResponseBody> getProgress(@Header("Authorization") String auth, @Query("country_id") int countryId, @Query("brand_id") int brandId);

    @GET("get-progress")
    Call<ResponseBody> getProgressFlm(@Header("Authorization") String auth, @Query("country_id") int countryId, @Query("brand_id") int brandId, @Query("flm_id") int flmId);

    @GET("team-leader/get-patient-process")
    Call<ResponseBody> getRequests(@Header("Authorization") String auth ,@Query("brand_id") int brandId);

    @POST("team-leader/approve-patient-process")
    @FormUrlEncoded
    Call<ResponseBody> confirRequest(@Header("Authorization") String auth ,@FieldMap Map<String, String> map);

    @GET("jakavi-patient?dropped=0&switched=0")
    Call<ResponseBody> getJakaviPatients(@Header("Authorization") String auth);

    @POST("auth/login")
    @FormUrlEncoded
    Call<ResponseBody> login(@FieldMap Map<String, String> map);


}