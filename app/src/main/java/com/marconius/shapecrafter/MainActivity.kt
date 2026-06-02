package com.marconius.shapecrafter

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import com.marconius.shapecrafter.monarch.MonarchDisplayController
import com.marconius.shapecrafter.svg.SvgRenderService
import com.marconius.shapecrafter.ui.editor.EditorScreen
import com.marconius.shapecrafter.ui.theme.ShapeCrafterTheme
import com.marconius.shapecrafter.viewmodel.EditorMode
import com.marconius.shapecrafter.viewmodel.ShapeCrafterViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ShapeCrafterViewModel by viewModels()
    private val svgRenderService = SvgRenderService()
    private var monarchController: MonarchDisplayController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (MonarchDisplayController.shouldUseMonarchMode()) {
            monarchController = MonarchDisplayController(
                activity = this
            ).also { it.create() }
        } else {
            enableEdgeToEdge()
        }

        showEditorScreen()
    }

    override fun onResume() {
        super.onResume()
        monarchController?.resume()
    }

    override fun onStop() {
        monarchController?.stop()
        super.onStop()
    }

    override fun onDestroy() {
        monarchController?.destroy()
        monarchController = null
        super.onDestroy()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        logKeyEvent("dispatchKeyEvent", event)
        if (
            event.action == KeyEvent.ACTION_DOWN &&
            isToggleKey(event) &&
            handleToggleCommand()
        ) {
            return true
        }

        return super.dispatchKeyEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            logKeyEvent("onKeyDown", event)
        }
        if (event != null && isToggleKey(event) && handleToggleCommand()) {
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            logKeyEvent("onKeyUp", event)
        }

        return super.onKeyUp(keyCode, event)
    }

    private fun handleToggleCommand(): Boolean {
        if (viewModel.mode == EditorMode.TACTILE) {
            viewModel.returnToEditor()
            showEditorScreen()
            return true
        }

        val controller = monarchController
        if (controller == null) {
            viewModel.reportRenderError("Tactile mode is only available on Monarch hardware.")
            return true
        }

        val displaySize = controller.displaySize()
        val renderResult = svgRenderService.renderSvgToDots(
            svgText = viewModel.svgText,
            width = displaySize.width,
            height = displaySize.height
        )

        if (!renderResult.ok) {
            viewModel.reportRenderError(renderResult.error)
            return true
        }

        val graphic = renderResult.value
        viewModel.showTactileGraphic(graphic)
        controller.showGraphic(graphic)
        return true
    }

    private fun showEditorScreen() {
        setContent {
            ShapeCrafterTheme {
                ShapeCrafterApp(
                    viewModel = viewModel,
                    onRenderToggle = ::handleToggleCommand
                )
            }
        }
    }

    private fun isToggleKey(event: KeyEvent): Boolean {
        return event.keyCode == KeyEvent.KEYCODE_PAGE_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_F10 ||
            event.keyCode == TOGGLE_COMMAND_KEY_CODE
    }

    private fun logKeyEvent(source: String, event: KeyEvent) {
        Log.d(
            TAG,
            "$source action=${event.action} keyCode=${event.keyCode} scanCode=${event.scanCode} metaState=${event.metaState} repeat=${event.repeatCount}"
        )
    }

    companion object {
        private const val TAG = "ShapeCrafterKeys"
        private const val TOGGLE_COMMAND_KEY_CODE = 140
    }

}

@Composable
fun ShapeCrafterApp(
    viewModel: ShapeCrafterViewModel,
    onRenderToggle: () -> Boolean
) {
    val rootView = LocalView.current

    LaunchedEffect(viewModel.statusMessage) {
        if (viewModel.statusMessage.isNotBlank()) {
            rootView.announceForAccessibility(viewModel.statusMessage)
        }
    }

    EditorScreen(
        svgText = viewModel.svgText,
        statusMessage = viewModel.statusMessage,
        availablePrimitives = viewModel.availablePrimitives,
        onSvgTextChanged = viewModel::updateSvgText,
        onInsertPrimitive = viewModel::insertPrimitive,
        onRenderToggle = { onRenderToggle() }
    )
}
