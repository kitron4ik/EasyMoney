package com.dockix.easymoney.models

import java.util.*

// Типы транзакций
enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

// Модель транзакции
data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val description: String,
    val categoryId: String,
    val date: Date = Date(),
    val type: TransactionType,
    val accountId: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

// Модель категории
data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: TransactionType,
    val iconName: String = "default_icon",
    val color: String = "#000000"
)

// Модель счета
data class Account(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val balance: Double,
    val currency: String = "RUB",
    val iconName: String = "default_icon",
    val isDefault: Boolean = false
)

// Модель бюджета
data class Budget(
    val id: String = UUID.randomUUID().toString(),
    val categoryId: String,
    val amount: Double,
    val period: String, // MONTHLY, WEEKLY, etc.
    val startDate: Date = Date(),
    val endDate: Date? = null
)