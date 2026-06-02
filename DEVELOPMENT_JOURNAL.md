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

## Entry 3: Debugging the real hardware toggle path

The app reached an important milestone: it launched on the Monarch and the SVG editor was visible on the device. That proved the Android scaffold and basic Monarch deployment path were working.

But the next part did not work yet. Pressing the current toggle chord did not switch from the editor into the tactile graphics screen.

That kind of problem is very normal when hardware command mapping is involved. A command may fail for several different reasons:

1. The command file may not have been written correctly.
2. The focused view may not match the context expected by the device software.
3. The hardware chord may arrive as a different Android key code than expected.
4. The app may receive the event, but the handler may not recognize it.

## What changed in this step

1. Logging was added around incoming key events in the main activity.
2. The app now records the key code, scan code, action, and repeat count for device input.
3. The toggle handler was made a little more explicit so the real incoming hardware event can be compared against the expected one.

## Why this is useful

When a hardware feature fails, guessing is expensive. Logging narrows the problem quickly.

This is a good beginner lesson because it shows that debugging is not only about fixing mistakes after they happen. It is also about improving visibility so the next decision is based on evidence instead of hope.

## Entry 4: Adjusting the KeySoft command context

The first hardware logging pass taught us something important: the app was running, but the `Dots 7 and 8` toggle did not arrive in the activity as a key event.

That means the problem is probably not the render toggle logic itself. It is more likely happening one step earlier, where KeySoft decides whether the current focused view matches the command mapping context.

## What changed in this step

1. The command mapping stopped relying only on the custom editor id.
2. The toggle chord was added to broader editor-side contexts such as `AppCompatEditText`, `AndroidComposeView`, `ComposeView`, and `DecorView`.
3. The existing `SelfBraillingWidget` tactile context was kept.

## Why this matters

Hardware command systems often care deeply about focus and view identity. A command that looks correct on paper may still fail if it is attached to the wrong layer of the interface.

This is another good beginner lesson: when debugging input, sometimes the question is not "What key should this be?" but "Which part of the interface is actually receiving focus right now?"

## Entry 5: Switching the toggle to the Monarch Page Down button

After two failed attempts to use the `Dots 7 and 8` chord, the hardware debugging path finally gave a clearer answer. A raw input capture showed that the preferred physical button was arriving from the Monarch braille key device as a dedicated key event.

That is a much stronger foundation than guessing through shortcut chords.

## What changed in this step

1. The app toggle handler was updated to accept Android `KEYCODE_PAGE_DOWN`.
2. The command mapping file was updated to reflect `PageDown` with Android keycode `93`.
3. The toggle design now matches the preferred hardware button choice for this stage of the project.

## Why this matters

This step is a good reminder that the most user-friendly control is not always the one that sounds elegant in planning. A clear, reliable hardware button is often better than a clever shortcut if it works consistently and is easy to remember.
