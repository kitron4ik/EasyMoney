package com.dockix.easymoney

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dockix.easymoney.ui.theme.EasyMoneyTheme

class IntentExamplesActivity : ComponentActivity() {
    private val TAG = "IntentExamplesActivity"

    // Регистрируем обработчик результата для неявного Intent (выбор изображения)
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d(TAG, "Выбрано изображение: $it")
            // Здесь можно обработать выбранное изображение
        }
    }

    // Регистрируем обработчик результата для явного Intent (получение результата из BudgetCategoryActivity)
    private val categoryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val categoryName = result.data?.getStringExtra("CATEGORY_NAME") ?: "Категория не выбрана"
            val categoryAmount = result.data?.getDoubleExtra("CATEGORY_AMOUNT", 0.0) ?: 0.0
            Log.d(TAG, "Получен результат: категория=$categoryName, сумма=$categoryAmount")
            // Здесь можно обработать полученные данные
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContent {
            EasyMoneyTheme {
                IntentExamplesScreen(
                    onExplicitIntent = { launchExplicitIntent() },
                    onImplicitIntent = { launchImplicitIntent() },
                    onActivityResult = { launchActivityForResult() },
                    onBackClick = { finish() }
                )
            }
        }
    }

    // Явный Intent - запуск конкретной активности
    private fun launchExplicitIntent() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("EXAMPLE_DATA", "Данные из явного Intent")
        startActivity(intent)
        Log.d(TAG, "Запущен явный Intent")
    }

    // Неявный Intent - запуск действия без указания конкретного компонента
    private fun launchImplicitIntent() {
        // Пример 1: Открытие веб-страницы
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
        startActivity(browserIntent)
        Log.d(TAG, "Запущен неявный Intent для открытия веб-страницы")

        // Пример 2: Выбор изображения из галереи
        getContent.launch("image/*")
        Log.d(TAG, "Запущен неявный Intent для выбора изображения")
    }

    // Запуск активности с ожиданием результата
    private fun launchActivityForResult() {
        val intent = Intent(this, BudgetCategoryActivity::class.java)
        categoryResult.launch(intent)
        Log.d(TAG, "Запущена активность для получения результата")
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
fun IntentExamplesScreen(
    onExplicitIntent: () -> Unit,
    onImplicitIntent: () -> Unit,
    onActivityResult: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Примеры работы с Intent")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onExplicitIntent) {
            Text("Явный Intent (Explicit)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onImplicitIntent) {
            Text("Неявный Intent (Implicit)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onActivityResult) {
            Text("Получить результат активности")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBackClick) {
            Text("Назад")
        }
    }
}