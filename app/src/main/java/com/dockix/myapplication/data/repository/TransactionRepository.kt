package com.dockix.myapplication.data.repository

import com.dockix.myapplication.data.database.dao.TransactionDao
import com.dockix.myapplication.data.database.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TransactionRepository(private val transactionDao: TransactionDao) {
    
    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }
    
    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }
    
    suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction)
    }
    
    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
    
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
    
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }
    
    fun getTransactionsByType(isIncome: Boolean): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(isIncome)
    }
    
    fun getUnsyncedTransactions(): Flow<List<Transaction>> {
        return transactionDao.getUnsyncedTransactions()
    }
    
    fun getTotalIncome(): Flow<Double?> {
        return transactionDao.getTotalIncome()
    }
    
    fun getTotalExpense(): Flow<Double?> {
        return transactionDao.getTotalExpense()
    }
} 