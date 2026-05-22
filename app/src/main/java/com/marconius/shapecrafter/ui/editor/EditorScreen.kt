package com.marconius.shapecrafter.ui.editor

import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.marconius.shapecrafter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    svgText: String,
    statusMessage: String,
    availablePrimitives: List<String>,
    onSvgTextChanged: (String) -> Unit,
    onInsertPrimitive: (String) -> Unit,
    onRenderToggle: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant.toArgb()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editor_title)) },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.editor_intro),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(R.string.toggle_hint),
                style = MaterialTheme.typography.bodyMedium
            )

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                factory = {
                    AppCompatEditText(context).apply {
                        id = R.id.svg_editor
                        minLines = 16
                        maxLines = 24
                        isSingleLine = false
                        typeface = Typeface.MONOSPACE
                        setText(svgText)
                        setTextColor(onSurfaceColor)
                        setBackgroundColor(surfaceVariantColor)
                        setPadding(32)
                        addTextChangedListener { editable ->
                            onSvgTextChanged(editable?.toString().orEmpty())
                        }
                        setOnFocusChangeListener { view, hasFocus ->
                            if (hasFocus) {
                                view.announceForAccessibility(context.getString(R.string.editor_focused))
                            }
                        }
                        requestFocus()
                    }
                },
                update = { editText ->
                    if (editText.text.toString() != svgText) {
                        editText.setText(svgText)
                        editText.setSelection(svgText.length)
                    }
                }
            )

            PrimitiveButtonRows(
                availablePrimitives = availablePrimitives,
                onInsertPrimitive = onInsertPrimitive
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRenderToggle
            ) {
                Text(stringResource(R.string.render_button))
            }

            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PrimitiveButtonRows(
    availablePrimitives: List<String>,
    onInsertPrimitive: (String) -> Unit
) {
    val rows = availablePrimitives.chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { primitive ->
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onInsertPrimitive(primitive) }
                    ) {
                        Text(primitive)
                    }
                }
            }
        }
    }
}
