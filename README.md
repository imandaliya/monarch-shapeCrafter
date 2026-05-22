# shapeCrafter

shapeCrafter is an Android app project built specifically for the APH Monarch.

Its job is simple to explain:

1. Let a user write real SVG code in an editor screen.
2. Let the user press a Monarch command to switch to a full-screen tactile graphics screen.
3. Render the current SVG into a tactile dot matrix when that toggle happens.
4. Let the same command switch back to the SVG editor.

This project is not targeting ordinary Android phones as the main product. It uses the HumanWare KeySoft SDK patterns already proven in the local Monarch integration work from `WordBopper`.

## Current milestone

This milestone is about proving the core end-to-end interaction:

1. A native editor screen exists.
2. SVG text can be changed directly.
3. Starter primitives can be inserted with default values.
4. A Monarch chord can trigger rendering.
5. The rendered tactile output takes over the full screen.
6. The same command returns to the editor.

## Project structure

1. `app/src/main/java/com/marconius/shapecrafter/` holds the Android app code.
2. `app/src/main/java/com/marconius/shapecrafter/ui/` holds the editor screen and theme.
3. `app/src/main/java/com/marconius/shapecrafter/svg/` holds SVG templates and rendering logic.
4. `app/src/main/java/com/marconius/shapecrafter/monarch/` holds Monarch SDK integration code.
5. `app/src/main/res/raw/commands.xml` holds the KeySoft command mapping.
6. `DEVELOPMENT_JOURNAL.md` explains the build in plain language.

## Monarch-specific design

The app follows the same broad SDK shape used in the existing local Monarch integration sample:

1. `ShapeCrafterApplication` writes the `commands.xml` file into app-private storage for KeySoft.
2. `XmlResource` exposes that file through a content provider.
3. `MainActivity` listens for the mapped toggle command as a normal Android key event.
4. `MonarchDisplayController` binds to the self-brailling service and displays tactile dots through `SelfBraillingWidget`.

## SVG rendering approach

SVG stays the editable source format.

When the user toggles into tactile mode:

1. The current SVG text is parsed with AndroidSVG.
2. The SVG is rendered into a bitmap sized for the Monarch dot surface.
3. The bitmap is converted into a black-and-white tactile dot matrix.
4. The dot matrix is sent to the Monarch display.

This means rendering happens only on the mode change, not on every edit.

## Build requirements

1. Android Studio or the Android command-line tools
2. A local Android SDK
3. Access to the KeySoft SDK Maven repository
4. A local `keystore.properties` file for the private KeySoft token and any release signing values

## Local setup

Create a local `keystore.properties` file in the project root when building against the Monarch SDK.

The file should contain the same kinds of private values used by the existing local Monarch project:

1. release signing values when needed
2. the private KeySoft Maven deploy token

Do not commit this file.

## Build commands

Build a debug APK:

```bash
./gradlew --no-configuration-cache :app:assembleDebug
```

Install on a connected Monarch:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Next steps

1. Add file open and save support for SVG documents.
2. Add better tactile-friendly SVG validation messages.
3. Add a proper primitive insertion menu instead of only quick insert buttons.
4. Tune raster-to-tactile conversion for line thickness and dense graphics.
