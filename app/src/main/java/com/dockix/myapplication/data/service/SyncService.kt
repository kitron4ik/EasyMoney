package com.dockix.myapplication.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.dockix.myapplication.data.database.AppDatabase
import com.dockix.myapplication.data.database.entity.Transaction
import com.dockix.myapplication.data.repository.TransactionRepository
// import com.google.firebase.firestore.ktx.firestore
// import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SyncService : Service() {
    
    private val TAG = "SyncService"
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    
    private lateinit var repository: TransactionRepository
    // private val firestore = Firebase.firestore
    private val COLLECTION_NAME = "transactions"
    
    override fun onCreate() {
        super.onCreate()
        val dao = AppDatabase.getDatabase(applicationContext).transactionDao()
        repository = TransactionRepository(dao)
        
        startSync()
    }
    
    private fun startSync() {
        scope.launch {
            while (true) {
                try {
                    syncDataWithCloud()
                } catch (e: Exception) {
                    Log.e(TAG, "Error syncing data: ${e.message}")
                }
                // Sync every 15 minutes
                delay(15 * 60 * 1000)
            }
        }
    }
    
    private suspend fun syncDataWithCloud() {
        try {
            val unsyncedTransactions = repository.getUnsyncedTransactions().first()
            
            if (unsyncedTransactions.isNotEmpty()) {
                Log.d(TAG, "Syncing ${unsyncedTransactions.size} transactions")
                
                unsyncedTransactions.forEach { transaction ->
                    // Имитация успешной синхронизации
                    Log.d(TAG, "Simulating sync for transaction ${transaction.id}")
                    scope.launch {
                        // Mark as synced
                        repository.updateTransaction(
                            transaction.copy(syncedWithCloud = true)
                        )
                        Log.d(TAG, "Transaction ${transaction.id} marked as synced")
                    }
                    
                    /*
                    val transactionMap = mapOf(
                        "id" to transaction.id,
                        "title" to transaction.title,
                        "amount" to transaction.amount,
                        "isIncome" to transaction.isIncome,
                        "category" to transaction.category,
                        "date" to transaction.date,
                        "description" to (transaction.description ?: "")
                    )
                    
                    firestore.collection(COLLECTION_NAME)
                        .document(transaction.id.toString())
                        .set(transactionMap)
                        .addOnSuccessListener {
                            scope.launch {
                                // Mark as synced
                                repository.updateTransaction(
                                    transaction.copy(syncedWithCloud = true)
                                )
                                Log.d(TAG, "Transaction ${transaction.id} synced successfully")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error syncing transaction ${transaction.id}: ${e.message}")
                        }
                    */
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sync error: ${e.message}")
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
} 