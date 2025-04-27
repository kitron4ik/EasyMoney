package com.dockix.myapplication.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dockix.myapplication.data.database.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Long): Transaction?
    
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE isIncome = :isIncome ORDER BY date DESC")
    fun getTransactionsByType(isIncome: Boolean): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE syncedWithCloud = 0")
    fun getUnsyncedTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 1")
    fun getTotalIncome(): Flow<Double?>
    
    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 0")
    fun getTotalExpense(): Flow<Double?>
} 