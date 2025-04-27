package com.dockix.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dockix.myapplication.R
import com.dockix.myapplication.data.database.entity.Transaction
import com.dockix.myapplication.ui.components.AddTransactionForm
import com.dockix.myapplication.ui.components.TransactionItem
import com.dockix.myapplication.ui.viewmodel.TransactionViewModel

@Composable
fun TransactionsScreen(
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val transactions by transactionViewModel.transactions.collectAsState()
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nav_transactions),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Transaction list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(transactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onClick = { selectedTransaction = it }
                )
            }
        }
        
        // Add new transaction form
        AddTransactionForm(
            onSubmit = { title, amount, isIncome, category, date, description ->
                transactionViewModel.insertTransaction(
                    title = title,
                    amount = amount,
                    isIncome = isIncome,
                    category = category,
                    date = date,
                    description = description
                )
            }
        )
    }
} 