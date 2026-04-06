const primitiveTemplates = {
  circle: '<circle cx="40" cy="40" r="24" stroke="black" stroke-width="2" fill="none" />',
  rect: '<rect x="20" y="20" width="80" height="50" stroke="black" stroke-width="2" fill="none" />',
  line: '<line x1="10" y1="10" x2="110" y2="60" stroke="black" stroke-width="2" />',
  ellipse: '<ellipse cx="60" cy="40" rx="30" ry="18" stroke="black" stroke-width="2" fill="none" />',
  polygon: '<polygon points="20,70 60,10 100,70" stroke="black" stroke-width="2" fill="none" />',
  path: '<path d="M 20 70 L 60 20 L 100 70" stroke="black" stroke-width="2" fill="none" />'
};

export function createSvgDocumentTemplate() {
  return [
    '<svg xmlns="http://www.w3.org/2000/svg" width="120" height="80" viewBox="0 0 120 80">',
    "  <title>New tactile graphic</title>",
    "  <desc>A starter SVG document for shapeCrafter.</desc>",
    "  <rect x=\"1\" y=\"1\" width=\"118\" height=\"78\" stroke=\"black\" stroke-width=\"1\" fill=\"none\" />",
    "</svg>"
  ].join("\n");
}

export function listSupportedPrimitives() {
  return Object.keys(primitiveTemplates);
}

export function buildPrimitiveInsert(primitiveName) {
  const template = primitiveTemplates[primitiveName];

  if (!template) {
    throw new Error(`Unsupported primitive: ${primitiveName}`);
  }

  return template;
}
