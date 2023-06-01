package com.zzootalinktracker.rft.Database

import android.telecom.Call
import com.airbnb.lottie.L
import com.zzootalinktracker.rft.UI.Activity.Model.LoginModel
import com.zzootalinktracker.rft.UI.Activity.Model.UpdateCheck_Response
import com.zzootalinktracker.rft.UI.Activity.Model.UpdateVehicleAttributeResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


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

    companion object {
        fun create(): ApiInterface {
            val retrofit = ZzootaLinkClientInstance.retrofitInstance
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
        @Field("device_id") device_id: String
    ): retrofit2.Call<UpdateCheck_Response>

    @FormUrlEncoded
    @POST("update_user_timezone")
    fun updateUserTimeZone(
        @Field("user_api_hash") user_api_hash: String,
        @Field("time_zone") time_zone: String,
        @Field("user_id") user_id: String
    ): retrofit2.Call<UpdateVehicleAttributeResponse>

    /*Get Docket List*/
//    @GET("h4h/get_dockets")
//    fun getDocketList(
//        @Query("user_id") user_id: String,
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("limit") limit: String,
//        @Query("page") page: String
//    ): Call<DocketListModel>
//
//    /*LOGIN API*/
//    @GET("get_user_api_hash_by_email")
//    fun loginWithEmail(@Query("email") email: String): Call<LoginModel>
//
//    @GET("h4h/get_docket_items_by_docket_id")
//    fun getDocketItemsList(
//        @Query("id") docket_id: String,
//        @Query("driver_type") driver_type: String
//    ): Call<JobActivityModel>
//
//    /*Update Docket Start Time*/
//    @FormUrlEncoded
//    @POST("h4h/update_docket_start_time")
//    fun updateDocketStartTime(
//        @Field("id") id: String,
//        @Field("user_api_hash") user_api_hash: String,
//        @Field("job_started_date_time") job_started_date_time: String,
//        @Field("docket_completed_status") docket_completed_status: String,
//        @Field("driver_type") driver_type: String,
//        @Field("device_id") device_id: String
//    ): Call<UpdateStartEndJobResponse>
//
//    /*POST VEHICLE ATTRIBUTE DATE*/
//    @POST("post_sygic_vehicle_attribute_data")
//    fun updateVehicleAttributeData(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("device_id") device_id: String,
//        @Query("attribute") attribute: String
//    ): Call<UpdateVehicleAttributeResponse>
//
//    /*POST VEHICLE ATTRIBUTE DATE*/
//    @FormUrlEncoded
//    @POST("update_user_timezone")
//    fun updateUserTimeZone(
//        @Field("user_api_hash") user_api_hash: String,
//        @Field("time_zone") time_zone: String,
//        @Field("user_id") user_id: String
//    ): Call<UpdateVehicleAttributeResponse>
//
//    @FormUrlEncoded
//    @POST("h4h/send_sms")
//    fun sendSMS(
//        @Field("docket_id") docket_id: String,
//        @Field("current_latitude") current_latitude: String,
//        @Field("current_longitude") current_longitude: String,
//        @Field("delivery_latitude") delivery_latitude: String,
//        @Field("delivery_longitude") delivery_longitude: String,
//        @Field("estimate_remaining_time") estimate_remaining_time: String,
//        @Field("sms_text") sms_text: String
//    ): Call<SuccessResponse>
//
//    /*Update job start time sygic*/
//    @FormUrlEncoded
//    @POST("h4h/update_job_start_time")
//    fun updateJobStartTimeSygic(
//        @Field("docket_id") docket_id: String,
//        @Field("docket_number") docket_number: String,
//        @Field("job_start_time") job_start_time: String,
//        @Field("job_start_time_route") job_start_time_route: String
//    ): Call<SuccessResponse>
//
//    @FormUrlEncoded
//    @POST("h4h/update_job_detail_data")
//    fun updateJobLocation(
//        @Field("docket_id") docket_id: String,
//        @Field("device_latitude") device_latitude: String,
//        @Field("device_longitude") device_longitude: String,
//        @Field("device_speed") device_speed: String,
//        @Field("sygic_latitude") sygic_latitude: String,
//        @Field("sygic_longitude") sygic_longitude: String,
//        @Field("sygic_speed") sygic_speed: String,
//        @Field("sygic_time_remaining_in_sec") sygic_time_remaining_in_sec: String,
//        @Field("sygic_distance_passed_in_meters") sygic_distance_passed_in_meters: String,
//        @Field("sygic_distance_remaining_in_meters") sygic_distance_remaining_in_meters: String
//    ): Call<SuccessResponse>
//
//    /*Update job start time sygic*/
//    @FormUrlEncoded
//    @POST("h4h/update_job_end_time")
//    fun updateJobEndTimeSygic(
//        @Field("docket_id") docket_id: String,
//        @Field("job_end_time") job_end_time: String,
//        @Field("job_end_time_route") job_end_time_route: String
//    ): Call<SuccessResponse>
//
//    /*Update job cancel time sygic*/
//    @FormUrlEncoded
//    @POST("h4h/cancel_route")
//    fun updateJobCancelTimeSygic(
//        @Field("docket_id") docket_id: String,
//        @Field("route_cancelled_time") job_end_time: String
//    ): Call<SuccessResponse>
//
//    /*add job route change time sygic*/
//    @FormUrlEncoded
//    @POST("h4h/add_job_route_change_time")
//    fun addRouteChangeData(
//        @Field("docket_id") docket_id: String,
//        @Field("docket_number") docket_number: String,
//        @Field("route_change_time") route_change_time: String,
//        @Field("route_coordinates") route_coordinates: String,
//        @Field("added_by") added_by: String
//    ): Call<SuccessResponse>
//
//    /*Update Docket End Time*/
//    @FormUrlEncoded
//    @POST("h4h/update_docket_end_time")
//    fun updateDocketEndTime(
//        @Field("id") id: String,
//        @Field("user_api_hash") user_api_hash: String,
//        @Field("job_completed_date_time") job_completed_date_time: String,
//        @Field("docket_completed_status") docket_completed_status: String,
//        @Field("docket_completed_by") docket_completed_by: String,
//        @Field("driver_type") driver_type: String,
//        @Field("device_id") device_id: String
//
//    ): Call<UpdateStartEndJobResponse>
//
//
//    @FormUrlEncoded
//    @POST("change_password_new")
//    fun changePassword(
//        @Field("user_api_hash") user_api_hash: String,
//        @Field("old_password") old_password: String,
//        @Field("new_password") new_password: String,
//        @Field("confirm_password") confirm_password: String,
//
//        ): Call<ChangePasswordModel>
//
//    /*Update Docket End Time*/
//    @FormUrlEncoded
//    @POST("h4h/update_yardman_fail")
//    fun yardManFailDocket(
//        @Field("id") id: String,
//        @Field("user_api_hash") user_api_hash: String,
//        @Field("job_completed_date_time") job_completed_date_time: String,
//        @Field("docket_completed_status") docket_completed_status: String,
//        @Field("docket_completed_by") docket_completed_by: String,
//        @Field("driver_type") driver_type: String
//
//    ): Call<UpdateStartEndJobResponse>
//
//    /*Save Check list Activities Api*/
//    @POST("h4h/update_docket_items")
//    fun saveJobActivities(@Body body: JobActivityRequest): Call<SuccessResponse>
//
//    /*Change Job Position*/
//    @POST("h4h/reorder_docket_positions")
//    fun changeJobPosition(@Body body: ChangePositonRequstModel): Call<SuccessResponse>
//
//    /*GET DEVICE LIST API*/
//
//    @GET("get_devices")
//    fun getDeviceList(
//        @Query("lang") lang: String,
//        @Query("user_api_hash") user_api_hash: String
//    ): Call<String>
//
//
//    @GET("get_customer_approach_alerts")
//    fun getCustomerApproachAlerts(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("device_id") device_id: String
//    ): Call<GetCustomerApproachAlertsModel>
//
//    @GET("jobs/docket_detail_by_docket_id_qr_code")
//    fun getQrCodeUrl(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("device_id") device_id: String,
//        @Query("docket_id") docket_id: String
//    ): Call<GenereateQrCodeUrl>
//
//    @GET("get_device_by_imei")
//    fun getDeviceByIMEI(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("device_imei") device_imei: String,
//        @Query("isVersionAbove28") isVersionAbove28: Int
//    ): Call<DeviceByIMEIModel>
//
//    @POST("chat/sendMessage")
//    fun sendMessage(
//        @Body messageModel: SendMessageModel
//    ): Call<SendMessageResponse>
//
//
//    @GET("chat/get_message_history")
//    fun getMessages(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("manager_id") manager_id: String,
//        @Query("device_id") device_id: String,
//        @Query("page") page: String
//    ): Call<ChatListModel>
//
//    companion object {
//        fun create(): ApiInterface {
//            val retrofit = ZzootaLinkClientInstance.retrofitInstance
//            return retrofit!!.create(ApiInterface::class.java)
//        }
//    }
//
//    /*GET TASK DETAIL BY ID*/
//    @GET("get_task/{task_id}")
//    fun getTaskDetailById(
//        @Path(value = "task_id") task_id: String,
//        @Query("lang") lang: String,
//        @Query("user_api_hash") user_api_hash: String
//    ): Call<TaskDetailModel>
//
//    /*GET USER DATA*/
//    @GET("edit_setup_data")
//    fun editSetupdata(
//        @Query("lang") lang: String,
//        @Query("user_api_hash") user_api_hash: String
//    ): Call<EditSetupModel>
//
//    /*GET SERVICE AND CHECK LISTS*/
//    @GET("get_all_services")
//    fun getServiceList(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("device_id") device_id: String,
//        @Query("device_time") device_time: String
//    ): Call<ServiceModel>
//
//    /*Upload Image Api*/
//    @Multipart
//    @POST("checklist_image_upload")
//    fun uploadImage(
//        @Part("row_id") row_id: RequestBody,
//        @Part("user_api_hash") user_api_hash: RequestBody,
//        @Part file: MultipartBody.Part
//    ): Call<ResponseImageUpload>
//
//
//    /*H4h Upload Image Api*/
//    @Multipart
//    @POST("h4h/update_docket_item_image")
//    fun uploadH4hActivityImage(
//        @Part("row_id") row_id: RequestBody,
//        @Part file: MultipartBody.Part
//    ): Call<ResponseH4HImageUpload>
//
//    @FormUrlEncoded
//    @POST("store_ailloy_login_logs")
//    fun insertDeviceLoginHistroy(
//        @Field("user_api_hash") user_api_hash: String,
//        @Field("device_sdk_version") device_sdk_version: String,
//        @Field("last_login_time") last_login_time: String,
//        @Field("version_name") version_name: String,
//        @Field("sygic_app_version") sygic_app_version: String,
//        @Field("device_id") device_id: String
//    ): Call<UpdateCheck_Response>
//
//
//    /*GET PRE START CHECK LIST*/
//    @GET("get_checklist_latest")
//    fun getPreStartCheckList(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("checklist_id") checklist_id: String
//    ): Call<PreCheckListModel>
//
//    /*Start/End Break Api*/
//    @POST("store_break_time_new")
//    fun startEndBreak(@Body breakModel: StartEndModel): Call<PostBreakResponse>
//
//
//    /*Get breaks list*/
//    @GET("h4h/break_histroy")
//    fun getbreakslist(
//        @Query("user_api_hash") user_api_hash: String
//
//    ): Call<BreakTimingsModel>
//
//    /*Save Check list Activities Api*/
//    @POST("update_checklist")
//    fun saveActivities(@Body body: UpdateCheckListModel): Call<UpdateCheck_Response>
//
//
//    @GET("h4h/get_last_break_notification_time")
//    fun getLastBreakNotificationTime(
//        @Query("user_api_hash") user_api_hash: String
//    ): Call<BreakNotificationTimeModel>
//
//    @POST("h4h/update_last_break_notification_time")
//    fun updateLastBreakNotificationTime(
//        @Query("user_api_hash") user_api_hash: String,
//        @Query("user_id") user_id: String,
//    ): Call<UpdateLastBreakNotificationTimeModel>
}