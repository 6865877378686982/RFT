package com.zzootalinktracker.rft.Database

import com.google.gson.JsonObject
import com.zzootalinktracker.rft.UI.Activity.Model.*
import com.zzootalinktracker.rft.UI.Fragment.Model.GetDeviceDriverInfoModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTagsStatusHistoryModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerIdsHavingCurrentTripNotNullModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    /*LOGIN API*/
    @FormUrlEncoded
    @POST("login_with_imei")
    fun loginWithImei(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("imei") imei: String,
        @Field("versionAbove28") versionAbove28: String
    ): retrofit2.Call<LoginModel>


    @POST("RFT/PushNotificationUpdateToken")
    fun pushNotificatiionUpdateToken(@Body postNotificationData: PushNotificationDataModel
    ): retrofit2.Call<ResponseModel>

    companion object {
        fun create(): ApiInterface {
            val retrofit = ZzootaLinkClientInstance.retrofitInstance
            return retrofit!!.create(ApiInterface::class.java)
        }

        fun createForRFT(): ApiInterface {

            val retrofit = EdgeClientInstance.retrofitInstance
            return retrofit!!.create(ApiInterface::class.java)
        }
    }

    @FormUrlEncoded
    @POST("store_ailloy_login_logs")
    fun insertDeviceLoginHistroy(
        @Field("user_api_hash") user_api_hash: String,
        @Field("device_sdk_version") device_sdk_version: String,
        @Field("last_login_time") last_login_time: String,
        @Field("version_name") version_name: String,
        @Field("sygic_app_version") sygic_app_version: String,
        @Field("device_id") device_id: String,
        @Field("user_id") user_id: String,
    ): Call<UpdateCheck_Response>

    @GET("get_last_login_device_history")
    fun getLastLoginDeviceHistory(
        @Query("user_api_hash") user_api_hash: String, @Query("device_id") device_id: String
    ): Call<GetLasLoginDeviceHistoryModel>

    @FormUrlEncoded
    @POST("update_user_timezone")
    fun updateUserTimeZone(
        @Field("user_api_hash") user_api_hash: String,
        @Field("time_zone") time_zone: String,
        @Field("user_id") user_id: String
    ): retrofit2.Call<UpdateVehicleAttributeResponse>

    @POST("RFT/AddStoredMissingStatusData")
    fun addSpace10XBTData(
        @Body body: ArrayList<AddSpace10XBTDataModel.TrailersData>
    ): Call<AddSpace10XBTDataModel>

    // Get Trailer, Tags & Status

    @GET("RFT/TrailerTagsAndStatus")
    fun getTrailerTagsStatus(
        @Query("RFTDriverId") RFTDriverId: String
    ): Call<GetTrailerTagsStatusModel>


    @GET("rft_login")
    fun rftLogin(
        @Query("imei") imei: String, @Query("versionAbove28") versionAbove28: Int
    ): Call<RftLoginModel>

    @GET("RFT/TagsStatusHistory")
    fun getTagsStatusHistory(
        @Query("RFTDriverId") RFTDriverId: String,
        @Query("StartDate") StartDate: String,
        @Query("EndDate") EndDate: String

    ): Call<GetTagsStatusHistoryModel>

    @GET("RFT/GetDeviceDriverInfo")
    fun getDeviceDriverInfo(
        @Query("Version") Version: Int,
        @Query("IMEI") IMEI: String,
        @Query("AndroidId") AndroidId: String
    ): Call<GetDeviceDriverInfoModel>

   /* @GET("RFT/GetDeviceDriverInfo")
    fun getDeviceDriverInfo(
        @Query("TrailerId") TrailerId: String
    ): Call<GetDeviceDriverInfoModel>*/



    @GET("RFT/GetTrailerIdsHavingCurrentTripNotNull")
    fun getTrailerIdsHavingCurrentTripNotNull(
    ): Call<GetTrailerIdsHavingCurrentTripNotNullModel>

}