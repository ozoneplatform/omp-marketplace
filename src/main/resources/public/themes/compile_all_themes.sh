#!/bin/sh

#Usage: sh compile_all_themes.sh [compass_compile_args]


for file in `ls -d *.theme/sass`
do
    cd "$file"
    compass compile $@ --trace
    cd ../.. > /dev/null
done
