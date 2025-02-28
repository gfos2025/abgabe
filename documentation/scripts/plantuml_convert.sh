#!/bin/bash
SOURCE_DIR="intermediate/uml"
DEST_DIR="mkdocs/images/uml"
ABS_DEST_DIR=$(realpath "$DEST_DIR")
mkdir -p "$ABS_DEST_DIR"

convert_puml() {
    local source_file="$1"
    local relative_path="${source_file#$SOURCE_DIR/}"
    local dest_dir="$ABS_DEST_DIR/$(dirname "$relative_path")"
    mkdir -p "$dest_dir"
    local dest_file="$dest_dir/$(basename "${source_file%.*}").svg"
    plantuml -tsvg -o "$(dirname "$dest_file")" "$source_file"
}

find "$SOURCE_DIR" -type f -name "*.puml" | while read -r puml_file; do
    convert_puml "$puml_file"
done