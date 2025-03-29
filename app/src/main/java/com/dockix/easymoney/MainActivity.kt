package com.dockix.easymoney

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.dockix.easymoney.ui.theme.EasyMoneyTheme
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private var savedText = ""

    // Регистрируем обработчик результата
    private val categoryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val categoryName = result.data?.getStringExtra("CATEGORY_NAME") ?: "Категория не выбрана"
            val categoryAmount = result.data?.getDoubleExtra("CATEGORY_AMOUNT", 0.0) ?: 0.0
            Log.d(TAG, "Получен результат: категория=$categoryName, сумма=$categoryAmount")

            // Обновляем UI с полученными данными
            savedText = "Категория: $categoryName, Бюджет: $categoryAmount ₽"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()

        setContent {
            EasyMoneyTheme {
                var text by remember { mutableStateOf(savedText) }

                MainScreen(
                    text = text,
                    onTextChange = {
                        text = it
                        savedText = it
                    },
                    onNavigate = { inputText ->
                        sendTextAndNavigate(inputText)
                    },
                    onSelectCategory = {
                        val intent = Intent(this@MainActivity, BudgetCategoryActivity::class.java)
                        categoryResult.launch(intent)
                    },
                    onLanguageSelected = { languageCode ->
                        switchLanguage(languageCode)
                    }
                )
            }
        }
    }

    private fun switchLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        // Обновляем конфигурацию для текущего контекста
        createConfigurationContext(config)

        // Обновляем конфигурацию для ресурсов
        resources.updateConfiguration(config, resources.displayMetrics)

        // Сохраняем выбранный язык в SharedPreferences
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selected_language", languageCode)
            apply()
        }

        // Перезапускаем активность для применения изменений
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context) {
        // Загружаем сохраненный язык при запуске активности
        val sharedPref = newBase.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val languageCode = sharedPref.getString("selected_language", "ru") ?: "ru"

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)

        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    private fun sendTextAndNavigate(text: String) {
        lifecycleScope.launch {
            try {
                // Эмулируем отправку текста через интернет
                val response = MessageRepository.sendMessage(text)
                Log.d(TAG, "Текст отправлен через интернет: ${response.message}")

                // Переходим в SecondActivity
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("TEXT_KEY", text)
                intent.putExtra("TIMESTAMP", System.currentTimeMillis())
                intent.putExtra("IS_EXPENSE", true)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при отправке текста", e)
            }
        }
    }

    // Остальные методы жизненного цикла без изменений...
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SAVED_TEXT", savedText)
        Log.d(TAG, "onSaveInstanceState: сохранен текст: $savedText")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString("SAVED_TEXT", "")
        Log.d(TAG, "onRestoreInstanceState: восстановлен текст: $savedText")
    }
}

@Composable
fun MainScreen(
    text: String,
    onTextChange: (String) -> Unit,
    onNavigate: (String) -> Unit,
    onSelectCategory: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    // Используем строковые ресурсы вместо жестко закодированных строк
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Language switcher at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            LanguageSwitcher(onLanguageSelected = onLanguageSelected)
        }

        Text(text = stringResource(R.string.enter_text))

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onNavigate(text) }) {
            Text(stringResource(R.string.go_to_second_activity))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onSelectCategory) {
            Text(stringResource(R.string.select_budget_category))
        }
    }
}

