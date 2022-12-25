package com.yehia.miranewfilepiker

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface api {

    @Multipart
    @POST("uploadFile")
    fun uploadFileAsync(
        @Part file: MultipartBody.Part,
        @Header("Authorization") Authorization: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYWJyLWFsbW9kb24uNGhvc3RlLmNvbVwvYXBpXC91c2VyU2lnbkluIiwiaWF0IjoxNjQ4Mzg3NTg1LCJuYmYiOjE2NDgzODc1ODUsImp0aSI6ImRJT3JCTG5UMXRkMFpDdUgiLCJzdWIiOjEzOCwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.KpmhInpi2fcg2UCAlazlIY0OcfS-NfrEikkWTpKl2EE",
    ): Call<Any>
}