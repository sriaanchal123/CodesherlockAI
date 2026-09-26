package com.example.codesherlockai.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface CodeSherlockApi {
    @POST("investigate")
    suspend fun investigate(
        @Body request: InvestigationRequest
    ): InvestigationResponse
}
