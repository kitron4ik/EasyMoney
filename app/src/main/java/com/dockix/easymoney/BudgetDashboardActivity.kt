package com.dockix.easymoney

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.dockix.easymoney.models.*
import com.dockix.easymoney.repository.CloudBudgetRepository
import com.dockix.easymoney.ui.theme.EasyMoneyTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BudgetDashboardActivity : ComponentActivity() {
    private val TAG = "BudgetDashboardActivity"
    private val repository = CloudBudgetRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContent {
            EasyMoneyTheme {
                BudgetDashboardScreen(repository)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDashboardScreen(repository: CloudBudgetRepository) {
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Загружаем данные при создании экрана
    LaunchedEffect(Unit) {
        try {
            accounts = repository.getAccounts()
            transactions = repository.getTransactions()
            categories = repository.getCategories()
            isLoading = false
        } catch (e: Exception) {
            Log.e("BudgetDashboard", "Ошибка загрузки данных", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Управление бюджетом") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Открыть экран добавления транзакции */ }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Отображение общего баланса
                TotalBalanceCard(accounts)

                Spacer(modifier = Modifier.height(16.dp))

                // Отображение счетов
                Text(
                    text = "Счета",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    items(accounts) { account ->
                        AccountItem(account)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Отображение последних транзакций
                Text(
                    text = "Последние транзакции",
                    style = MaterialTheme.typography.titleMedium
                )

                if (transactions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Нет транзакций")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(transactions.sortedByDescending { it.date }) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                category = categories.find { it.id == transaction.categoryId }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TotalBalanceCard(accounts: List<Account>) {
    val totalBalance = accounts.sumOf { it.balance }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Общий баланс",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "$totalBalance ₽",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun AccountItem(account: Account) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = account.name)
                Text(
                    text = "${account.balance} ${account.currency}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (account.isDefault) {
                Text(
                    text = "Основной",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, category: Category?) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(transaction.date)

    val textColor = when (transaction.type) {
        TransactionType.INCOME -> Color.Green
        TransactionType.EXPENSE -> Color.Red
        TransactionType.TRANSFER -> Color.Blue
    }

    val amountText = when (transaction.type) {
        TransactionType.INCOME -> "+${transaction.amount} ₽"
        TransactionType.EXPENSE -> "-${transaction.amount} ₽"
        TransactionType.TRANSFER -> "${transaction.amount} ₽"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = category?.name ?: "Без категории")
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}