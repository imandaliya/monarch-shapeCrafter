package com.marconius.shapecrafter.svg

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException

data class RenderedTactileGraphic(
    val dots: Array<ByteArray>,
    val width: Int,
    val height: Int,
    val summary: String
)

data class RenderResult<T>(
    val ok: Boolean,
    val value: T,
    val error: String
)

class SvgRenderService {
    fun renderSvgToDots(svgText: String, width: Int, height: Int): RenderResult<RenderedTactileGraphic> {
        val validationError = validateSvg(svgText)
        if (validationError != null) {
            return RenderResult(
                ok = false,
                value = emptyGraphic(width, height),
                error = validationError
            )
        }

        return try {
            val svg = SVG.getFromString(svgText)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            svg.renderToCanvas(canvas)

            val dots = BitmapToDotsConverter.convert(bitmap)
            RenderResult(
                ok = true,
                value = RenderedTactileGraphic(
                    dots = dots,
                    width = width,
                    height = height,
                    summary = "Rendered the current SVG into a tactile dot matrix."
                ),
                error = ""
            )
        } catch (exception: SVGParseException) {
            RenderResult(
                ok = false,
                value = emptyGraphic(width, height),
                error = "The SVG could not be parsed: ${exception.message.orEmpty()}"
            )
        } catch (exception: Exception) {
            RenderResult(
                ok = false,
                value = emptyGraphic(width, height),
                error = "The SVG could not be rendered: ${exception.message.orEmpty()}"
            )
        }
    }

    private fun validateSvg(svgText: String): String? {
        val trimmedSvg = svgText.trim()

        if (!trimmedSvg.startsWith("<svg")) {
            return "The document must start with an <svg> element."
        }

        if (!trimmedSvg.endsWith("</svg>")) {
            return "The document must end with a closing </svg> tag."
        }

        return null
    }

    private fun emptyGraphic(width: Int, height: Int): RenderedTactileGraphic {
        return RenderedTactileGraphic(
            dots = Array(height) { ByteArray(width) { 0 } },
            width = width,
            height = height,
            summary = ""
        )
    }
}

private object BitmapToDotsConverter {
    private const val PIN_DOWN: Byte = 0
    private const val PIN_UP: Byte = 1
    private const val THRESHOLD = 200

    fun convert(bitmap: Bitmap): Array<ByteArray> {
        val dots = Array(bitmap.height) { ByteArray(bitmap.width) { PIN_DOWN } }

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                val brightness = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                if (brightness < THRESHOLD) {
                    dots[y][x] = PIN_UP
                }
            }
        }

        return dots
    }
}
