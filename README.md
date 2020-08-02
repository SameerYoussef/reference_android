# Nowhere Man App

Nowhere Man App is an Android app that subconsciously starts playing the 1965 Beatles song "Nowhere Man" in the heads of the users.

The unique design of the app leads users nowhere hence the name of the app ;)

# Automation Testing

The test follows the 'Customer Journey' of a typical user. The benefit of this approach is that only valuable tests are written - no writing tests for the sake of tests - only testing valuable customer flows. This requires some careful thinking of how users are likely to interact with the app in an end-to-end fashion as it is detailed in the test comment. This includes the naming of the class and method.

The tests are written in Kotlin to provide a clean and pleasurable reading and coding experience.

For the purpose of this exercise the tests are contained in one file (for ease of review via Github) - if the app was to ever grow however then a page object model might be used and helper classes would be refactored out.

Within the test method you will find the text that the user will actually see - this copy is not to be hidden in screen classes. This allows the test reader to see exactly what the user will be reading.

The overflow is opened using native Espresso functionality.

Assertions are done on UI elements to ensure they are completely displayed or no longer exist to avoid partially displayed elements existing.

In order to assert that the Snackbar was dismissed I called on some pre-written code that can be used to remove flakiness from the test.

# How to run test(s)

1. Have a device or emulator connected
2. Run run_tests.sh from the command line

![android_tests](https://user-images.githubusercontent.com/18099038/89117968-2ff74900-d4f6-11ea-8497-b337abaf7315.gif)

