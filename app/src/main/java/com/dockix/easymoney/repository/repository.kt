package com.dockix.easymoney.repository

import com.dockix.easymoney.models.*
import kotlinx.coroutines.delay
import java.util.*

// Интерфейс для работы с данными
interface BudgetRepository {
    suspend fun getTransactions(): List<Transaction>
    suspend fun addTransaction(transaction: Transaction): Boolean
    suspend fun updateTransaction(transaction: Transaction): Boolean
    suspend fun deleteTransaction(transactionId: String): Boolean

    suspend fun getCategories(): List<Category>
    suspend fun addCategory(category: Category): Boolean

    suspend fun getAccounts(): List<Account>
    suspend fun addAccount(account: Account): Boolean
    suspend fun updateAccountBalance(accountId: String, newBalance: Double): Boolean

    suspend fun getBudgets(): List<Budget>
    suspend fun addBudget(budget: Budget): Boolean
}

// Реализация репозитория с эмуляцией облачного хранения
class CloudBudgetRepository : BudgetRepository {
    // Эмуляция хранилища данных
    private val transactions = mutableListOf<Transaction>()
    private val categories = mutableListOf(
        Category(id = "1", name = "Зарплата", type = TransactionType.INCOME),
        Category(id = "2", name = "Продукты", type = TransactionType.EXPENSE),
        Category(id = "3", name = "Транспорт", type = TransactionType.EXPENSE),
        Category(id = "4", name = "Развлечения", type = TransactionType.EXPENSE)
    )
    private val accounts = mutableListOf(
        Account(id = "1", name = "Наличные", balance = 10000.0, isDefault = true),
        Account(id = "2", name = "Дебетовая карта", balance = 25000.0)
    )
    private val budgets = mutableListOf<Budget>()

    override suspend fun getTransactions(): List<Transaction> {
        // Эмулируем задержку сети
        delay(500)
        return transactions
    }

    override suspend fun addTransaction(transaction: Transaction): Boolean {
        // Эмулируем задержку сети
        delay(500)
        transactions.add(transaction)

        // Обновляем баланс счета
        val account = accounts.find { it.id == transaction.accountId } ?: return false
        val newBalance = when (transaction.type) {
            TransactionType.INCOME -> account.balance + transaction.amount
            TransactionType.EXPENSE -> account.balance - transaction.amount
            TransactionType.TRANSFER -> account.balance // Для переводов нужна дополнительная логика
        }

        updateAccountBalance(account.id, newBalance)
        return true
    }

    override suspend fun updateTransaction(transaction: Transaction): Boolean {
        delay(500)
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            transactions[index] = transaction
            return true
        }
        return false
    }

    override suspend fun deleteTransaction(transactionId: String): Boolean {
        delay(500)
        val transaction = transactions.find { it.id == transactionId } ?: return false

        // Отменяем изменение баланса
        val account = accounts.find { it.id == transaction.accountId } ?: return false
        val newBalance = when (transaction.type) {
            TransactionType.INCOME -> account.balance - transaction.amount
            TransactionType.EXPENSE -> account.balance + transaction.amount
            TransactionType.TRANSFER -> account.balance // Для переводов нужна дополнительная логика
        }

        updateAccountBalance(account.id, newBalance)
        transactions.removeIf { it.id == transactionId }
        return true
    }

    override suspend fun getCategories(): List<Category> {
        delay(300)
        return categories
    }

    override suspend fun addCategory(category: Category): Boolean {
        delay(300)
        categories.add(category)
        return true
    }

    override suspend fun getAccounts(): List<Account> {
        delay(300)
        return accounts
    }

    override suspend fun addAccount(account: Account): Boolean {
        delay(300)
        accounts.add(account)
        return true
    }

    override suspend fun updateAccountBalance(accountId: String, newBalance: Double): Boolean {
        delay(300)
        val index = accounts.indexOfFirst { it.id == accountId }
        if (index != -1) {
            accounts[index] = accounts[index].copy(balance = newBalance)
            return true
        }
        return false
    }

    override suspend fun getBudgets(): List<Budget> {
        delay(300)
        return budgets
    }

    override suspend fun addBudget(budget: Budget): Boolean {
        delay(300)
        budgets.add(budget)
        return true
    }
}