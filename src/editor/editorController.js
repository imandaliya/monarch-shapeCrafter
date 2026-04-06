import { buildPrimitiveInsert } from "../svg/svgTemplates.js";
import { updateSvgText } from "../core/documentState.js";

export function insertPrimitive(state, primitiveName) {
  const primitiveMarkup = buildPrimitiveInsert(primitiveName);
  const nextSvgText = insertBeforeClosingSvg(state.svgText, primitiveMarkup);

  return updateSvgText(state, nextSvgText);
}

export function replaceDocumentText(state, svgText) {
  return updateSvgText(state, svgText);
}

function insertBeforeClosingSvg(svgText, markup) {
  const closingTag = "</svg>";
  const closingTagIndex = svgText.lastIndexOf(closingTag);

  if (closingTagIndex === -1) {
    return `${svgText}\n${markup}\n`;
  }

  const beforeClosingTag = svgText.slice(0, closingTagIndex).trimEnd();
  const afterClosingTag = svgText.slice(closingTagIndex);

  return `${beforeClosingTag}\n  ${markup}\n${afterClosingTag}`;
}
