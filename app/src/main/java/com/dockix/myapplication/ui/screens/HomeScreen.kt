package com.dockix.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dockix.myapplication.R
import com.dockix.myapplication.ui.components.AddTransactionForm
import com.dockix.myapplication.ui.components.BudgetSummaryCard
import com.dockix.myapplication.ui.components.CurrencyRateCard
import com.dockix.myapplication.ui.viewmodel.CurrencyViewModel
import com.dockix.myapplication.ui.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(
    currencyViewModel: CurrencyViewModel = viewModel(),
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val currencyRate by currencyViewModel.usdToRubRate.collectAsState()
    val totalIncome by transactionViewModel.totalIncome.collectAsState()
    val totalExpense by transactionViewModel.totalExpense.collectAsState()
    val balance by transactionViewModel.balance.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Currency rate widget
        CurrencyRateCard(
            usdToRubRate = currencyRate,
            onRefresh = { currencyViewModel.loadCurrencyRate() }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Budget summary
        BudgetSummaryCard(
            income = totalIncome,
            expense = totalExpense,
            balance = balance
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add transaction form
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