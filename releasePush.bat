@echo off 

git checkout master-release

ping 127.0.0.1 -n 1

git pull


ping 127.0.0.1 -n 1

git merge test

git push

pause
