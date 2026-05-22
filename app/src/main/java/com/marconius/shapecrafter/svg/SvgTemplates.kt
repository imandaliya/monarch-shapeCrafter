package com.marconius.shapecrafter.svg

object SvgTemplates {
    private val primitiveTemplates = mapOf(
        "circle" to """<circle cx="40" cy="40" r="24" stroke="black" stroke-width="2" fill="none" />""",
        "rect" to """<rect x="20" y="20" width="80" height="50" stroke="black" stroke-width="2" fill="none" />""",
        "line" to """<line x1="10" y1="10" x2="110" y2="60" stroke="black" stroke-width="2" />""",
        "ellipse" to """<ellipse cx="60" cy="40" rx="30" ry="18" stroke="black" stroke-width="2" fill="none" />""",
        "polygon" to """<polygon points="20,70 60,10 100,70" stroke="black" stroke-width="2" fill="none" />""",
        "path" to """<path d="M 20 70 L 60 20 L 100 70" stroke="black" stroke-width="2" fill="none" />"""
    )

    fun documentTemplate(): String {
        return listOf(
            """<svg xmlns="http://www.w3.org/2000/svg" width="120" height="80" viewBox="0 0 120 80">""",
            "  <title>New tactile graphic</title>",
            "  <desc>A starter SVG document for shapeCrafter.</desc>",
            """  <rect x="1" y="1" width="118" height="78" stroke="black" stroke-width="1" fill="none" />""",
            "</svg>"
        ).joinToString("\n")
    }

    fun availablePrimitives(): List<String> = primitiveTemplates.keys.toList()

    fun insertPrimitive(svgText: String, primitiveName: String): String {
        val primitive = primitiveTemplates[primitiveName]
            ?: error("Unsupported primitive: $primitiveName")
        val closingTagIndex = svgText.lastIndexOf("</svg>")
        if (closingTagIndex == -1) {
            return "$svgText\n$primitive"
        }
        val beforeClosingTag = svgText.substring(0, closingTagIndex).trimEnd()
        val afterClosingTag = svgText.substring(closingTagIndex)
        return "$beforeClosingTag\n  $primitive\n$afterClosingTag"
    }
}
