# Navigation Suite Scaffold with icons vertical alignment

This sample showcases how to leverage the new version of the `NavigationSuiteScaffold` to align the
icons vertically to the top, center or bottom of the `NavRail`.

The core changes to an existing codebase are:
- Use the `navigationItems` parameter to provide the entries, together with the constructor `NavigationSuiteItem` rather than the `item()` helper function
- Pass the `Arrangement.Vertical` value to the `navigationItemVerticalArrangement` parameter in the `NavigationSuiteScaffold` declaration.

## Screenshot
![Animation of the Navigation Suite Scaffold changing arrangement based on user's choice](/resources/scaffold_animated.gif)