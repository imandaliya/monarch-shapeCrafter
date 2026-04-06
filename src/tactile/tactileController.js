import {
  recordRenderError,
  recordRenderSuccess,
  setMode
} from "../core/documentState.js";

export function switchToTactileMode(state, renderResult) {
  let nextState = recordRenderSuccess(state, renderResult);
  nextState = setMode(nextState, "tactile");
  return nextState;
}

export function switchToEditorMode(state) {
  return setMode(state, "editor");
}

export function failTactileRender(state, message) {
  return recordRenderError(state, message);
}
