export function rasterizeSvg(svgText) {
  const validationError = validateSvg(svgText);

  if (validationError) {
    return {
      ok: false,
      error: validationError
    };
  }

  return {
    ok: true,
    value: {
      format: "png",
      width: extractDimension(svgText, "width") ?? 120,
      height: extractDimension(svgText, "height") ?? 80,
      summary: "Placeholder PNG render plan for the current SVG document."
    }
  };
}

function validateSvg(svgText) {
  const trimmedSvg = svgText.trim();

  if (!trimmedSvg.startsWith("<svg")) {
    return "The document must start with an <svg> element.";
  }

  if (!trimmedSvg.endsWith("</svg>")) {
    return "The document must end with a closing </svg> tag.";
  }

  return null;
}

function extractDimension(svgText, attributeName) {
  const pattern = new RegExp(`${attributeName}="(\\d+)"`);
  const match = svgText.match(pattern);

  if (!match) {
    return null;
  }

  return Number.parseInt(match[1], 10);
}
