package com.marconius.shapecrafter.monarch

import android.os.Build
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import com.humanware.keysoftsdk.selfbrailling.SelfBraillingManager
import com.humanware.keysoftsdk.selfbrailling.aidl.DotsMatrix
import com.humanware.keysoftsdk.selfbrailling.widget.SelfBraillingWidget
import com.marconius.shapecrafter.svg.RenderedTactileGraphic

class MonarchDisplayController(
    private val activity: ComponentActivity
) {
    private val viewedImage = MutableLiveData<Array<ByteArray>>()
    private val liveDots = MutableLiveData<Array<ByteArray>>()

    private lateinit var manager: SelfBraillingManager
    private lateinit var widget: SelfBraillingWidget
    private lateinit var screenDimensions: Size
    private var servicesBound = false
    private var widgetCreated = false

    fun create() {
        ensureServiceBound()
        ensureWidget()
    }

    fun resume() {
        if (!servicesBound) {
            manager.bindService()
            servicesBound = true
        }
    }

    fun stop() {
        if (servicesBound) {
            servicesBound = false
            manager.unbindService()
        }
    }

    fun destroy() {
        stop()
    }

    fun displaySize(): Size {
        ensureServiceBound()
        return screenDimensions
    }

    fun showGraphic(graphic: RenderedTactileGraphic) {
        ensureServiceBound()
        ensureWidget()
        activity.title = ""
        activity.setContentView(widget)
        widget.requestFocus()
        viewedImage.value = graphic.dots
        liveDots.value = DotsMatrix(graphic.dots).matrix
        manager.announceText("Tactile graphic")
    }

    private fun ensureServiceBound() {
        if (::manager.isInitialized) {
            return
        }

        manager = SelfBraillingManager(activity).apply { bindService() }
        servicesBound = true
        screenDimensions = Size(manager.brailleDisplayDotsSizeX, manager.brailleDisplayDotsSizeY)
    }

    private fun ensureWidget() {
        if (widgetCreated) {
            return
        }

        widget = SelfBraillingWidget(activity)
        viewedImage.observe(activity) { dots -> dots?.let { widget.refresh(it) } }
        liveDots.observe(activity) { dots -> dots?.let { manager.displayDots(it) } }
        widgetCreated = true
    }

    companion object {
        fun shouldUseMonarchMode(): Boolean {
            val deviceInfo = listOf(
                Build.MANUFACTURER,
                Build.BRAND,
                Build.MODEL,
                Build.DEVICE,
                Build.PRODUCT
            ).joinToString(" ").lowercase()

            return listOf("monarch", "humanware", "keysoft", "aph").any { marker ->
                deviceInfo.contains(marker)
            }
        }
    }
}
