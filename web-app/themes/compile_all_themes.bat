@echo off

for /d %%d in ("*.theme") do (
    cd %%d\sass
    compass compile -f %1
    cd ..\..
)
