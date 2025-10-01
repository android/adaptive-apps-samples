# Navigation Suite Scaffold with icons vertical alignment

This sample showcases how to leverage the new version of the `NavigationSuiteScaffold` to align the
icons vertically to the top, center or bottom of the `NavRail`.

The core changes to an existing codebase are:
- Use the `navigationItems` parameter to provide the entries, together with the constructor `NavigationSuiteItem` rather than the `item()` helper function
- Pass the `Arrangement.Vertical` value to the `navigationItemVerticalArrangement` parameter in the `NavigationSuiteScaffold` declaration.

## Screenshot
![Animation of the Navigation Suite Scaffold changing arrangement based on user's choice](/resources/scaffold_animated.gif)

### License

```
Copyright 2025 The Android Open Source Project
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    https://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```