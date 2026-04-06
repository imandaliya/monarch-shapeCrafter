import { createSvgDocumentTemplate } from "../svg/svgTemplates.js";

export function createDocumentState() {
  const svgText = createSvgDocumentTemplate();

  return {
    svgText,
    filePath: null,
    recentFiles: [],
    hasUnsavedChanges: false,
    lastRenderResult: null,
    lastRenderError: null,
    mode: "editor"
  };
}

export function updateSvgText(state, svgText) {
  return {
    ...state,
    svgText,
    hasUnsavedChanges: true,
    lastRenderError: null
  };
}

export function markSaved(state, filePath) {
  return {
    ...state,
    filePath,
    recentFiles: addRecentFile(state.recentFiles, filePath),
    hasUnsavedChanges: false
  };
}

export function recordRenderSuccess(state, renderResult) {
  return {
    ...state,
    lastRenderResult: renderResult,
    lastRenderError: null
  };
}

export function recordRenderError(state, message) {
  return {
    ...state,
    lastRenderError: message
  };
}

export function setMode(state, mode) {
  return {
    ...state,
    mode
  };
}

export function openDocument(state, filePath, svgText) {
  return {
    ...state,
    filePath,
    svgText,
    recentFiles: addRecentFile(state.recentFiles, filePath),
    hasUnsavedChanges: false,
    lastRenderError: null
  };
}

function addRecentFile(existingFiles, filePath) {
  if (!filePath) {
    return existingFiles;
  }

  const uniqueFiles = existingFiles.filter((existingFile) => existingFile !== filePath);

  return [filePath, ...uniqueFiles].slice(0, 10);
}
