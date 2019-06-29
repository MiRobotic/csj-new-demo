@echo off 

git checkout develop

ping 127.0.0.1 -n 1

git pull

ping 127.0.0.1 -n 1

git checkout test

ping 127.0.0.1 -n 1

git pull

ping 127.0.0.1 -n 1

git merge develop

pause
