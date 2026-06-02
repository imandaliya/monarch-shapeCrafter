# shapeCrafter

shapeCrafter is an Android app project built specifically for the APH Monarch.

The long-term goal is to give Monarch users a way to write real SVG code in an accessible editor and then switch directly into a tactile rendering of that graphic on the Monarch display.

This project is meant to support both creation and learning:

1. creation, by letting someone write and test tactile graphics directly
2. learning, by keeping the code and documentation readable for beginners who want to understand how the app is built

## Overall application goal

shapeCrafter is designed around one main workflow:

1. Let a user write real SVG code in an editor screen.
2. Let the user press a Monarch command to switch to a full-screen tactile graphics screen.
3. Render the current SVG into a tactile dot matrix when that toggle happens.
4. Let the same command switch back to the SVG editor.

The app is not being built as a general Android phone app. It is a Monarch-first project, and the tactile graphics experience is the center of the design.

## Project progress

The project is in an early but real device-tested stage.

What is working now:

1. the Android app builds successfully
2. the app installs and launches on a connected Monarch
3. the SVG editor screen appears on device
4. the app includes starter SVG content for immediate testing
5. the app can render SVG to a bitmap and convert that bitmap into a tactile dot matrix in code
6. the Monarch integration layer is in place through the KeySoft SDK

What is still in progress:

1. finalizing the best hardware toggle for switching between editor and tactile view
2. confirming the full screen-to-screen toggle loop on hardware
3. adding open, save, recent files, and local files folder support
4. improving tactile rendering quality for line weight and dense graphics
5. expanding the SVG authoring helpers beyond the current starter buttons

## Current milestone

This milestone is about proving the core end-to-end interaction:

1. A native editor screen exists.
2. SVG text can be changed directly.
3. Starter primitives can be inserted with default values.
4. A Monarch hardware control can trigger rendering.
5. The rendered tactile output takes over the full screen.
6. The same command returns to the editor.

## Monarch SDK usage

This project uses the HumanWare KeySoft SDK to connect the app to Monarch-specific behavior.

The SDK is currently used in these ways:

1. `ShapeCrafterApplication` writes the app command file into internal storage so KeySoft can read it
2. `XmlResource` exposes that command file through a content provider
3. `MonarchDisplayController` binds to `SelfBraillingManager`
4. `SelfBraillingWidget` is used as the full-screen tactile graphics surface
5. the app reads the Monarch tactile display dimensions from the SDK before rendering
6. rendered dot matrices are sent to the tactile display through the SDK

This means the project is not just simulating Monarch support on a desktop. It is using the real local SDK path already proven in the related `WordBopper` project.

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

## Starter editor content

The current default SVG document opens with a complete SVG root element and starter shapes already in place so a user can test rendering immediately.

That startup document currently includes:

1. a root `<svg>` tag with `width`, `height`, and `viewBox`
2. a default circle
3. a default filled rectangle
4. a closing `</svg>` tag

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

Launch on a connected Monarch:

```bash
adb shell monkey -p com.marconius.ShapeCrafter 1
```

## Educational notes

This project intentionally keeps a plain-language development journal in [DEVELOPMENT_JOURNAL.md](/Users/pallas/Documents/coding/monarch/shapeCrafter/DEVELOPMENT_JOURNAL.md).

The journal exists so beginners can follow:

1. what changed
2. why it changed
3. what problems came up
4. how the hardware and software pieces fit together

## Next steps

1. Add file open and save support for SVG documents.
2. Add better tactile-friendly SVG validation messages.
3. Add a proper primitive insertion menu instead of only quick insert buttons.
4. Tune raster-to-tactile conversion for line thickness and dense graphics.

## License

This project is released under the MIT License. See [LICENSE](/Users/pallas/Documents/coding/monarch/shapeCrafter/LICENSE).
