# Adaptive apps samples

Adaptive apps provide an optimal user experience on all display sizes and form factors, including phones, tablets, foldables, desktops, TV, Automotive, XR, and display modes such as split screen and desktop windowing. 

The adaptive app samples demonstrate best practices for building Android apps that dynamically adjust their layout and functionality to make the most of available display space.

## Samples

### [Canonical layouts](./CanonicalLayouts)

Proven design patterns for large screens:
- **List-Detail**: Optimal for browsing content and seeing details side-by-side
- **Feed**: Ideal for content discovery
- **Supporting Pane**: Best for showing related content or tools alongside primary content

See the [Canonical Layouts README](./CanonicalLayouts/README.md) for more details.

### [Adaptive JetStream](./AdaptiveJetStream)

A media streaming app that adapts to various form factors using a single binary. The sample demonstrates how to use Compose Material3 to create experiences optimized for all display sizes.

See the [Adaptive JetStream README](./AdaptiveJetStream/README.md) to learn more.

### [Adaptive navigation](./AdaptiveNavigationSample)

A navigation app that uses `NavigationSuiteScaffold` to align navigation icons vertically (top, center, or bottom) within a navigation rail, adapting to different screen sizes and user preferences.

See the [Adaptive navigation README](./AdaptiveNavigationSample/README.md) for all the details.

### [FlexBox](./FlexBox)

Demonstrates dynamic wrapping, resizing, basis and shrink properties using Jetpack Compose FlexBox layout APIs for responsive content flow.

See the [FlexBox README](./FlexBox/README.md) for more details.

### [Grid](./Grid)

Demonstrates a responsive, multi-column content layout that dynamically adjusts column spans and weights based on the device's screen dimensions using Compose Grid layout APIs.

See the [Grid README](./Grid/README.md).

### [MediaQuery](./MediaQuery)

Demonstrates real-time layout adaptation utilizing the experimental `mediaQuery` API in Jetpack Compose. Adapts layout elements based on screen size, device fold posture, precision of pointer devices, physical viewing distance, and active hardware sensors.

See the [MediaQuery README](./MediaQuery/README.md) to learn more.

## Get started

To explore the samples:
1. Clone this repository.
2. Open the specific sample directory (for example `CanonicalLayouts` or `AdaptiveJetStream`) as an Android Studio project.

## License

This project is licensed under the Apache License, Version 2.0.

See the [LICENSE](LICENSE.md) file for details.
