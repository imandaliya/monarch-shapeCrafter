import { createDocumentState } from "../core/documentState.js";
import { insertPrimitive } from "../editor/editorController.js";
import { saveDocument, createRecentFilesSummary } from "../files/fileManager.js";
import { toggleView } from "../commands/appCommands.js";
import { listSupportedPrimitives } from "../svg/svgTemplates.js";

export function runPrototypeWalkthrough() {
  let state = createDocumentState();
  const output = [];

  output.push("shapeCrafter prototype walkthrough");
  output.push("");
  output.push(`Current mode: ${state.mode}`);
  output.push("Starter SVG document:");
  output.push(state.svgText);
  output.push("");

  const supportedPrimitives = listSupportedPrimitives();
  output.push(`Supported starter primitives: ${supportedPrimitives.join(", ")}`);
  output.push("");

  state = insertPrimitive(state, "circle");
  output.push("Inserted a starter circle.");
  output.push(state.svgText);
  output.push("");

  const tactileToggle = toggleView(state);
  state = tactileToggle.state;
  output.push(tactileToggle.message);

  if (state.lastRenderResult) {
    output.push(`Rendered format: ${state.lastRenderResult.format}`);
    output.push(`Rendered size: ${state.lastRenderResult.width} x ${state.lastRenderResult.height}`);
    output.push(`Render summary: ${state.lastRenderResult.summary}`);
  }

  output.push("");

  const editorToggle = toggleView(state);
  state = editorToggle.state;
  output.push(editorToggle.message);
  output.push(`Current mode: ${state.mode}`);
  output.push("");

  state = saveDocument(state, "Files/new-tactile-graphic.svg");
  output.push("Saved the current document.");
  output.push(createRecentFilesSummary(state));

  return output.join("\n");
}
