package com.example.enablio

import com.example.enablio.QueryModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/find_match/")
    fun findMatch(@Body queryModel: QueryModel): Call<List<MatchResult>>

    @GET("/download/")
    fun downloadFile(@Query("path") path: String): Call<ResponseBody>
}
data class QueryModel(val query: String)

data class MatchResult(
    val word: String,
    val path: String,
    val label: String,
    val type: String
)