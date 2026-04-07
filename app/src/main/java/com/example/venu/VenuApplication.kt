package com.example.venu

import android.app.Application
import com.example.venu.core.core_common.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class VenuApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            AppGraph.initialize(this@VenuApplication)
        }
    }
}