import { markSaved, openDocument } from "../core/documentState.js";

export function saveDocument(state, filePath) {
  return markSaved(state, filePath);
}

export function openExistingDocument(state, filePath, svgText) {
  return openDocument(state, filePath, svgText);
}

export function createRecentFilesSummary(state) {
  if (state.recentFiles.length === 0) {
    return "No recent files yet.";
  }

  return state.recentFiles
    .map((filePath, index) => `${index + 1}. ${filePath}`)
    .join("\n");
}
