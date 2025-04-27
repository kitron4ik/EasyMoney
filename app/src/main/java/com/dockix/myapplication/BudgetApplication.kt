package com.dockix.myapplication

import android.app.Application
import android.content.Intent
import android.util.Log
import com.dockix.myapplication.data.service.SyncService
// import com.google.firebase.FirebaseApp

class BudgetApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        // FirebaseApp.initializeApp(this)
        
        // Start the sync service
        try {
            val intent = Intent(this, SyncService::class.java)
            startService(intent)
        } catch (e: Exception) {
            Log.e("BudgetApplication", "Failed to start sync service: ${e.message}")
        }
    }
} 