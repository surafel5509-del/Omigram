package com.example

import android.app.Application
import com.example.data.local.OmigoDatabase
import com.example.di.AppContainer
import com.example.di.DefaultAppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OmigoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)

        // Ensure database has sample data ready on first start
        val db = OmigoDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val user = db.profileDao().getProfile(com.example.data.local.SampleData.CURRENT_USER_ID)
            if (user == null) {
                OmigoDatabase.populateInitialData(db)
            }
        }
    }
}
