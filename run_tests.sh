#!/bin/zsh

# Build APKs
./gradlew assembleDebug assembleDebugAndroidTest 

# Clean up any pre-existing reports
rm -rf composer-output

# Execute tests
java -jar composer-0.6.0.jar \
--apk app/build/outputs/apk/debug/app-debug.apk \
--test-apk app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk

# View results
open composer-output/html-report/index.html
