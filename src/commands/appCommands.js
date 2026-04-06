import { switchToEditorMode, switchToTactileMode, failTactileRender } from "../tactile/tactileController.js";
import { rasterizeSvg } from "../svg/svgRasterizer.js";

export function toggleView(state) {
  if (state.mode === "tactile") {
    return {
      state: switchToEditorMode(state),
      message: "Returned to full-screen code view."
    };
  }

  const renderAttempt = rasterizeSvg(state.svgText);

  if (!renderAttempt.ok) {
    return {
      state: failTactileRender(state, renderAttempt.error),
      message: `Could not switch to tactile view: ${renderAttempt.error}`
    };
  }

  return {
    state: switchToTactileMode(state, renderAttempt.value),
    message: "Switched to full-screen tactile view."
  };
}
