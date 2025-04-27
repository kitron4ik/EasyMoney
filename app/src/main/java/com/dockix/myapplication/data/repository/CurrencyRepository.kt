package com.dockix.myapplication.data.repository

import com.dockix.myapplication.data.api.CurrencyApi
import com.dockix.myapplication.data.api.CurrencyInfo
import com.dockix.myapplication.di.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CurrencyRepository {
    private val api = NetworkModule.currencyApi
    
    fun getUsdToRubRate(): Flow<Result<CurrencyInfo>> = flow {
        try {
            val response = api.getLatestRates()
            emit(Result.success(response.Valute.USD))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
} 