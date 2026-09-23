#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVAFX_LIB="/Users/ibrahimabudalah/javafx-sdk-21.0.9/lib"

mkdir -p "$DIR/out/production/COMP2522 Project"

# Copy resources to output directory
if [ -d "$DIR/resources" ]; then
    cp -r "$DIR/resources" "$DIR/out/production/COMP2522 Project/"
fi

# Compile all source files and tests
javac --module-path "$JAVAFX_LIB" \
      --add-modules javafx.controls,javafx.fxml \
      -cp "$DIR/lib/*:$DIR/out/production/COMP2522 Project" \
      -d "$DIR/out/production/COMP2522 Project" \
      "$DIR/Main.java" \
      "$DIR"/MyGame/*.java \
      "$DIR"/NumberGame/*.java \
      "$DIR"/WordGame/*.java \
      "$DIR"/Tests/*.java

# Run Main
java --module-path "$JAVAFX_LIB" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "$DIR/lib/*:$DIR/out/production/COMP2522 Project" \
     ca.bcit.comp2522.project.Main
