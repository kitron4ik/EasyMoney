package com.dockix.easymoney

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.dockix.easymoney.ui.theme.EasyMoneyTheme
import kotlinx.coroutines.launch

class SecondActivity : ComponentActivity() {
    private val TAG = "SecondActivity"

    // В методе onCreate добавим получение данных из Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()

        // Получаем данные из Intent
        val textFromIntent = intent.getStringExtra("TEXT_KEY") ?: ""
        val timestamp = intent.getLongExtra("TIMESTAMP", 0L)
        val isExpense = intent.getBooleanExtra("IS_EXPENSE", false)

        Log.d(TAG, "Получены данные из Intent: text=$textFromIntent, timestamp=$timestamp, isExpense=$isExpense")

        setContent {
            EasyMoneyTheme {
                var receivedText by remember { mutableStateOf("Загрузка...") }

                // Получаем текст при создании экрана
                LaunchedEffect(Unit) {
                    try {
                        val response = MessageRepository.getMessage()
                        receivedText = response.message
                        Log.d(TAG, "Текст получен через интернет: $receivedText")
                    } catch (e: Exception) {
                        receivedText = "Ошибка при получении текста"
                        Log.e(TAG, "Ошибка при получении текста", e)
                    }
                }

                SecondScreen(
                    receivedText = receivedText,
                    textFromIntent = textFromIntent,
                    timestamp = timestamp,
                    isExpense = isExpense,
                    onBackClick = { finish() }
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

@Composable
fun SecondScreen(
    receivedText: String,
    textFromIntent: String,
    timestamp: Long,
    isExpense: Boolean,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Полученный текст через интернет:")
        Text(
            text = receivedText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Данные из Intent:")
        Text(text = "Текст: $textFromIntent")
        Text(text = "Время: ${java.util.Date(timestamp)}")
        Text(text = "Тип: ${if (isExpense) "Расход" else "Доход"}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBackClick) {
            Text("Вернуться назад")
        }
    }
}