package com.dockix.myapplication.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("daily_json.js")
    suspend fun getLatestRates(): CurrencyResponse
}

data class CurrencyResponse(
    val Date: String,
    val PreviousDate: String,
    val PreviousURL: String,
    val Timestamp: String,
    val Valute: ValuteData
)

data class ValuteData(
    val USD: CurrencyInfo,
    val EUR: CurrencyInfo,
    val GBP: CurrencyInfo
)

data class CurrencyInfo(
    val ID: String,
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Double,
    val Previous: Double
) 