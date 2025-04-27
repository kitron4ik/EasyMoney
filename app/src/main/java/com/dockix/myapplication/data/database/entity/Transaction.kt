package com.dockix.myapplication.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val isIncome: Boolean,
    val category: String,
    val date: Date,
    val description: String? = null,
    val syncedWithCloud: Boolean = false
) 