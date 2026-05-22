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

## Entry 2: Replacing the terminal prototype with a real Monarch Android scaffold

The project now has access to the real Monarch SDK path through the local `WordBopper` app, and that changed the right next step.

Instead of continuing with a generic JavaScript prototype, the project now moves into a real Android app structure that matches the existing Monarch integration pattern already working locally. That matters because it teaches an important beginner lesson: once better information becomes available, a good project adapts. Early prototypes are helpful, but they are not sacred.

## What changed in this step

1. The temporary Node prototype files were removed.
2. The repo was reshaped into an Android and Gradle project.
3. The app now uses the same private KeySoft Maven repository pattern as the existing Monarch sample.
4. A real `Application` class writes the Monarch `commands.xml` file into internal storage.
5. A content provider exposes that command file to KeySoft.
6. A native SVG editor screen was added for direct code editing.
7. Quick insert buttons were added for starter SVG primitives.
8. A Monarch display controller was added to take over the screen for tactile rendering.
9. AndroidSVG was added so SVG can be rendered into a bitmap before converting to tactile dots.

## Why this is the right shift

At first, the project only knew the public story of the Monarch platform. Later, the real local SDK integration became available. That changed the level of certainty we could work from.

This is normal in software projects. Sometimes the best next step is not adding one more feature. Sometimes it is replacing a temporary scaffold with the real structure the product actually needs.

## Vocabulary

### Gradle

Gradle is the build system used by Android projects. It knows how to compile the app, download dependencies, and produce APK files.

### Content provider

A content provider is an Android component that lets one part of the system expose data to another part in a controlled way. In this project, it is used so KeySoft can read the app's command mapping file.

### Dot matrix

A dot matrix is a grid of raised and lowered pins. In this app, the rendered SVG bitmap is converted into that dot grid so the Monarch can display it tactilely.

## What comes next

The next useful milestone is to test the real toggle loop on hardware:

1. Open the editor on Monarch.
2. Type or paste SVG.
3. Trigger the mapped command chord.
4. Confirm the graphic renders on the tactile display.
5. Trigger the same command again and return to the editor cleanly.
