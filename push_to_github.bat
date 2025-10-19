@echo off
echo ========================================
echo GitHub Push Helper Script
echo ========================================
echo.
echo This script will help you push your code to GitHub
echo Repository: https://github.com/ruhi96/startup_newapp
echo.
echo Before running this script, make sure you have:
echo 1. Created the repository on GitHub (if not already done)
echo 2. Generated a Personal Access Token from GitHub
echo    (Settings -^> Developer Settings -^> Personal Access Tokens)
echo.
pause
echo.
echo Pushing to GitHub...
echo.
git push -u origin main
echo.
if %ERRORLEVEL% EQU 0 (
    echo ========================================
    echo SUCCESS! Code pushed to GitHub!
    echo ========================================
) else (
    echo ========================================
    echo PUSH FAILED!
    echo ========================================
    echo.
    echo If you got a 403 error, you need to authenticate:
    echo.
    echo Option 1: Use GitHub CLI
    echo   gh auth login
    echo   git push -u origin main
    echo.
    echo Option 2: Use Personal Access Token
    echo   When prompted for password, use your Personal Access Token
    echo   (not your GitHub password)
    echo.
    echo Option 3: Use SSH
    echo   git remote set-url origin git@github.com:ruhi96/startup_newapp.git
    echo   git push -u origin main
    echo.
)
pause

