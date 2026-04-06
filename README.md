# shapeCrafter

shapeCrafter is a beginner-friendly prototype for an APH Monarch app that helps people write SVG by hand and then switch to a full-screen tactile graphics view.

The long-term goal is to support two full-screen modes:

1. Code view for writing and editing SVG text.
2. Tactile view for showing a rasterized version of that SVG on the Monarch display.

This first project scaffold does not claim to be a finished Monarch SDK app yet. Public APH material confirms the Monarch uses a custom SDK and app framework, but the exact public package structure is not broadly documented. Because of that, this repo starts with a platform-ready core that can later connect to the real device layer.

## Version 1 goals

1. Keep SVG text as the main authoring format.
2. Let a user toggle between full-screen code mode and full-screen tactile mode.
3. Convert SVG to a raster image only when that toggle happens.
4. Support new, open, save, save as, and recent SVG files.
5. Offer beginner-friendly insertion of SVG primitives with editable defaults.
6. Keep a development journal that explains the project in plain language.

## Current prototype shape

The current codebase focuses on the app "brain" first:

1. Document state
2. Mode switching
3. Primitive insertion
4. File bookkeeping
5. SVG validation and raster conversion planning

The included `npm start` command runs a small terminal walkthrough. That terminal view is not the final user interface. It is only a simple way to exercise the project structure while the real Monarch integration remains to be built.

## Why not build the full Monarch app immediately

Public APH sources make it clear that the Monarch is not a standard Android tablet workflow. That matters because guessing the SDK APIs too early would create brittle code that might need to be thrown away later.

So this project uses a safer layering approach:

1. Keep core app logic separate from device-specific integration.
2. Build the SVG editing model and toggle behavior first.
3. Add a thin Monarch adapter layer once the real SDK interfaces are available.

## Project layout

1. `src/app/` starts the prototype and owns top-level app flow.
2. `src/core/` holds the current document and app session state.
3. `src/editor/` handles code-mode editing behavior.
4. `src/tactile/` handles tactile-mode transitions and render state.
5. `src/svg/` handles SVG templates, validation, and raster conversion planning.
6. `src/files/` handles local file paths and recent files.
7. `src/commands/` defines app-level commands such as mode toggle.
8. `DEVELOPMENT_JOURNAL.md` tells the build story in plain language.

## Running the prototype

```bash
npm start
```

The current terminal walkthrough shows:

1. A starter SVG document
2. Primitive insertion
3. A toggle from code mode to tactile mode
4. A simulated raster conversion result
5. A toggle back to code mode

## Near-term next steps

1. Replace the terminal walkthrough with a real accessible editor shell.
2. Add a local files folder flow and real save/open behavior.
3. Swap the simulated rasterizer for a real SVG-to-PNG conversion path.
4. Connect the app shell to the real Monarch SDK when available.
