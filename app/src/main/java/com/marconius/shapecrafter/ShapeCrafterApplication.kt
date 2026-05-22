package com.marconius.shapecrafter

import android.app.Application
import com.humanware.keysoftsdk.contextmenu.WriteCommandsXmlFileToInternalMemoryStorageExecutor

class ShapeCrafterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        WriteCommandsXmlFileToInternalMemoryStorageExecutor(this).execute()
    }
}
