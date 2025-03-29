import kotlinx.coroutines.delay

// Модель данных для ответа
data class MessageResponse(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Класс для эмуляции отправки и получения сообщений через интернет
object MessageRepository {
    private var lastMessage: String = ""

    suspend fun sendMessage(message: String): MessageResponse {
        // Эмулируем задержку сети
        delay(500)
        lastMessage = message
        return MessageResponse(message)
    }

    suspend fun getMessage(): MessageResponse {
        // Эмулируем задержку сети
        delay(500)
        return MessageResponse(lastMessage)
    }
}