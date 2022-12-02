#!/usr/bin/env nix-shell
#!nix-shell -p bash -i bash

set -euo pipefail

readonly day="${1:-}"
readonly android_studio_path="/Users/jimmy/Library/Application Support/JetBrains/Toolbox/apps/AndroidStudio/ch-0/213.7172.25.2113.9014738/Android Studio.app"

if [ -z "${day}" ];  then
    echo "ERROR: day not provided"
    echo
    echo "USAGE: ${0} [day #]"
    exit 1
fi

main() {
    local part
    if [ -d "day_${day}/part_01" ]; then
        local part="02"
        cp -r "day_${day}/part_01" "day_${day}/part_02"
    else
        local part="01"
        local -r path="day_${day}/part_01"
        mkdir -p "${path}"
        touch "${path}/input_test.txt"
        touch "${path}/input.txt"
        cat << EOF  > "${path}/run_it.main.kts"
import java.io.File

fun loadInput(): String {
    return File("./input_test.txt").readText()
}

fun parseInput(input: String): List<String> {
    return input.split("\n")
}

fun main() {
    val input = parseInput(loadInput())
}

main()

EOF
    fi

    open "day_${day}/part_${part}" -a "${android_studio_path}"
}

main

