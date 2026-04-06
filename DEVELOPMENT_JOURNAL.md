# Development Journal

## Entry 1: Starting shapeCrafter with a platform-ready core

shapeCrafter starts from a very specific accessibility goal: let a person write real SVG code and then switch to a full-screen tactile graphics view on the APH Monarch.

That idea sounds simple, but it includes an important design decision. The app is not trying to hide SVG from the user. Instead, it treats SVG as the main creative tool and gives the user a direct path from code to tactile output.

This first milestone does not build the complete Monarch app yet. Public APH sources show that the Monarch uses a custom software framework and SDK for multiline braille and tactile graphics. Since the exact public app package structure is not fully documented, the safest first step is to build the project core in a clean and beginner-friendly way.

In plain language, we are building the parts that define how the app thinks before we build the parts that talk to the device.

## What was added in this step

1. A project README that explains the goal and current approach.
2. A development journal so the build story stays readable for beginners.
3. A document state model for the current SVG, file path, recent files, and render status.
4. A code-mode controller for editing SVG text and inserting starter primitives.
5. A tactile-mode controller for handling mode switches and render results.
6. A simple app command layer for toggling between code and tactile modes.
7. A placeholder SVG rasterizer that validates the document and simulates conversion planning.
8. A small terminal walkthrough so the architecture can already be exercised.

## Why this is a useful beginner step

A lot of beginner projects jump straight into interface code. That can feel exciting, but it often hides the real structure of the app.

This project starts one layer deeper:

1. What document is open
2. What mode the app is in
3. When conversion should happen
4. What happens if conversion fails
5. How file history should be remembered

That is valuable because these rules will still matter later, even after a real Monarch interface is added.

## Vocabulary

### Document state

Document state is the app's memory for the current file. It includes the SVG text itself, whether the file has unsaved changes, and information about the last successful render.

### Raster image

A raster image is a picture made from pixels or dots. Formats like PNG and JPG are raster formats. In this project, SVG is the editable source, while a raster image is the likely device-friendly result that gets shown in tactile mode.

### Adapter layer

An adapter layer is a small section of code that connects a general app design to a specific platform. Here, it means the future code that will connect this project core to the real Monarch SDK.

## What comes next

The next useful milestone is turning this scaffold into a more realistic app shell:

1. Add real local file save and open flows.
2. Replace placeholder raster conversion with a real SVG-to-image implementation.
3. Build a true accessible editor experience instead of the terminal walkthrough.
4. Confirm the real Monarch SDK integration points when those APIs are available.
