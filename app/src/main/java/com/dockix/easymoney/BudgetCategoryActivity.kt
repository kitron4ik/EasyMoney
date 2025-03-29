package com.dockix.easymoney

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dockix.easymoney.ui.theme.EasyMoneyTheme

class BudgetCategoryActivity : ComponentActivity() {
    private val TAG = "BudgetCategoryActivity"

    // Список категорий бюджета
    private val categories = listOf(
        BudgetCategory("Продукты", 15000.0),
        BudgetCategory("Транспорт", 5000.0),
        BudgetCategory("Развлечения", 7000.0),
        BudgetCategory("Коммунальные услуги", 8000.0),
        BudgetCategory("Здоровье", 6000.0),
        BudgetCategory("Одежда", 4000.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContent {
            EasyMoneyTheme {
                BudgetCategoryScreen(
                    categories = categories,
                    onCategorySelected = { category ->
                        returnCategoryResult(category)
                    },
                    onCancelClick = {
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }

    private fun returnCategoryResult(category: BudgetCategory) {
        val resultIntent = Intent()
        resultIntent.putExtra("CATEGORY_NAME", category.name)
        resultIntent.putExtra("CATEGORY_AMOUNT", category.budgetAmount)
        setResult(RESULT_OK, resultIntent)
        Log.d(TAG, "Возвращаем результат: ${category.name}, ${category.budgetAmount}")
        finish()
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

data class BudgetCategory(val name: String, val budgetAmount: Double)

@Composable
fun BudgetCategoryScreen(
    categories: List<BudgetCategory>,
    onCategorySelected: (BudgetCategory) -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Выберите категорию бюджета",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        Button(
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Отмена")
        }
    }
}

@Composable
fun CategoryItem(
    category: BudgetCategory,
    onClick: () -> Unit
) {
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
                Text(text = category.name)
                Text(
                    text = "Бюджет: ${category.budgetAmount} ₽",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(onClick = onClick) {
                Text("Выбрать")
            }
        }
    }
}