package com.dockix.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dockix.myapplication.data.database.AppDatabase
import com.dockix.myapplication.data.database.entity.Transaction
import com.dockix.myapplication.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: TransactionRepository
    
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()
    
    private val _totalIncome = MutableStateFlow<Double>(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()
    
    private val _totalExpense = MutableStateFlow<Double>(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()
    
    private val _balance = MutableStateFlow<Double>(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()
    
    init {
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactions ->
                _transactions.value = transactions
            }
        }
        
        viewModelScope.launch {
            combine(
                repository.getTotalIncome(),
                repository.getTotalExpense()
            ) { income, expense ->
                val totalIncome = income ?: 0.0
                val totalExpense = expense ?: 0.0
                val balance = totalIncome - totalExpense
                
                _totalIncome.value = totalIncome
                _totalExpense.value = totalExpense
                _balance.value = balance
            }.collect {}
        }
    }
    
    fun insertTransaction(
        title: String,
        amount: Double,
        isIncome: Boolean,
        category: String,
        date: Date = Date(),
        description: String? = null
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                title = title,
                amount = amount,
                isIncome = isIncome,
                category = category,
                date = date,
                description = description
            )
            repository.insertTransaction(transaction)
        }
    }
    
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }
    
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
    
    fun getTransactionsByType(isIncome: Boolean) {
        viewModelScope.launch {
            repository.getTransactionsByType(isIncome).collect { transactions ->
                _transactions.value = transactions
            }
        }
    }
    
    fun getAllTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactions ->
                _transactions.value = transactions
            }
        }
    }
} 