package com.marconius.shapecrafter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.marconius.shapecrafter.svg.RenderedTactileGraphic
import com.marconius.shapecrafter.svg.SvgTemplates

enum class EditorMode {
    EDITOR,
    TACTILE
}

class ShapeCrafterViewModel : ViewModel() {
    var svgText by mutableStateOf(SvgTemplates.documentTemplate())
        private set

    var statusMessage by mutableStateOf("SVG editor ready.")
        private set

    var mode by mutableStateOf(EditorMode.EDITOR)
        private set

    var lastRenderedGraphic: RenderedTactileGraphic? by mutableStateOf(null)
        private set

    val availablePrimitives: List<String> = SvgTemplates.availablePrimitives()

    fun updateSvgText(newText: String) {
        svgText = newText
    }

    fun insertPrimitive(primitiveName: String) {
        svgText = SvgTemplates.insertPrimitive(svgText, primitiveName)
        statusMessage = "Inserted $primitiveName."
    }

    fun showTactileGraphic(graphic: RenderedTactileGraphic) {
        lastRenderedGraphic = graphic
        mode = EditorMode.TACTILE
        statusMessage = "Rendered tactile graphic."
    }

    fun returnToEditor() {
        mode = EditorMode.EDITOR
        statusMessage = "Returned to SVG editor."
    }

    fun reportRenderError(message: String) {
        mode = EditorMode.EDITOR
        statusMessage = message
    }
}
