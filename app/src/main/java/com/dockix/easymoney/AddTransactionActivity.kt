package com.dockix.easymoney

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.dockix.easymoney.models.*
import com.dockix.easymoney.repository.CloudBudgetRepository
import com.dockix.easymoney.ui.theme.EasyMoneyTheme
import kotlinx.coroutines.launch
import java.util.*

class AddTransactionActivity : ComponentActivity() {
    private val TAG = "AddTransactionActivity"
    private val repository = CloudBudgetRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContent {
            EasyMoneyTheme {
                AddTransactionScreen(
                    repository = repository,
                    onTransactionAdded = {
                        Toast.makeText(this, "Транзакция добавлена", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onCancel = { finish() }
                )
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
fun AddTransactionScreen(
    repository: CloudBudgetRepository,
    onTransactionAdded: () -> Unit,
    onCancel: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    var selectedAccountId by remember { mutableStateOf<String?>(null) }

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Загружаем данные при создании экрана
    LaunchedEffect(Unit) {
        try {
            categories = repository.getCategories()
            accounts = repository.getAccounts()

            // Устанавливаем значения по умолчанию
            if (accounts.isNotEmpty()) {
                selectedAccountId = accounts.find { it.isDefault }?.id ?: accounts.first().id
            }

            isLoading = false
        } catch (e: Exception) {
            Log.e("AddTransaction", "Ошибка загрузки данных", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить транзакцию") }
            )
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Выбор типа транзакции
                Text(
                    text = "Тип транзакции",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TransactionTypeButton(
                        text = "Расход",
                        isSelected = selectedType == TransactionType.EXPENSE,
                        onClick = { selectedType = TransactionType.EXPENSE },
                        modifier = Modifier.weight(1f)  // Передаем weight здесь, внутри Row
                    )

                    TransactionTypeButton(
                        text = "Доход",
                        isSelected = selectedType == TransactionType.INCOME,
                        onClick = { selectedType = TransactionType.INCOME },
                        modifier = Modifier.weight(1f)  // Передаем weight здесь, внутри Row
                    )

                    TransactionTypeButton(
                        text = "Перевод",
                        isSelected = selectedType == TransactionType.TRANSFER,
                        onClick = { selectedType = TransactionType.TRANSFER },
                        modifier = Modifier.weight(1f)  // Передаем weight здесь, внутри Row
                    )

                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Категория",
                    style = MaterialTheme.typography.titleMedium
                )

                val filteredCategories = categories.filter {
                    it.type == selectedType || selectedType == TransactionType.TRANSFER
                }

                if (filteredCategories.isEmpty()) {
                    Text("Нет доступных категорий для выбранного типа")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        items(filteredCategories) { category ->
                            CategorySelectItem(
                                category = category,
                                isSelected = selectedCategoryId == category.id,
                                onClick = { selectedCategoryId = category.id }
                            )
                        }
                    }
                }

                Text(
                    text = "Счет",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    items(accounts) { account ->
                        AccountSelectItem(
                            account = account,
                            isSelected = selectedAccountId == account.id,
                            onClick = { selectedAccountId = account.id }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Кнопки действий
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            if (validateInput(amount, description, selectedCategoryId, selectedAccountId)) {
                                addTransaction(
                                    repository = repository,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    description = description,
                                    categoryId = selectedCategoryId!!,
                                    accountId = selectedAccountId!!,
                                    type = selectedType,
                                    onSuccess = onTransactionAdded
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}


@Composable
fun TransactionTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier  // Добавляем параметр modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier  // Используем переданный modifier вместо добавления weight здесь
    ) {
        Text(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Text(
            text = category.name,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelectItem(
    account: Account,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(account.name)
            Text("${account.balance} ${account.currency}")
        }
    }
}

fun validateInput(
    amount: String,
    description: String,
    categoryId: String?,
    accountId: String?
): Boolean {
    return amount.isNotEmpty() &&
            amount.toDoubleOrNull() != null &&
            amount.toDoubleOrNull()!! > 0 &&
            description.isNotEmpty() &&
            categoryId != null &&
            accountId != null
}

fun addTransaction(
    repository: CloudBudgetRepository,
    amount: Double,
    description: String,
    categoryId: String,
    accountId: String,
    type: TransactionType,
    onSuccess: () -> Unit
) {
    val transaction = Transaction(
        amount = amount,
        description = description,
        categoryId = categoryId,
        accountId = accountId,
        type = type,
        date = Date()
    )

    // Используем lifecycleScope в активности
    val activity = repository as? ComponentActivity
    activity?.lifecycleScope?.launch {
        try {
            val success = repository.addTransaction(transaction)
            if (success) {
                onSuccess()
            }
        } catch (e: Exception) {
            Log.e("AddTransaction", "Ошибка при добавлении транзакции", e)
        }
    }
}