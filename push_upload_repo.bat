@echo off
REM Script to add, commit and push all files to the remote repo
REM Usage: open CMD in this folder and run: push_upload_repo.bat

cd /d "%~dp0"
:: Check git availabilityngit --version >nul 2>&1if %errorlevel% neq 0 (    echo Git no encontrado. Instala Git: https://git-scm.com/downloads    pause    exit /b 1)
:: Initialize repo if neededif not exist ".git" (    git init    git branch -M main):: Add remote if not existsngit remote get-url origin >nul 2>&1if %errorlevel% neq 0 (    git remote add origin https://github.com/rojaserick-g/simpleTestAutomation.git):: Stage all changesgit add .:: Commit (allow failure if nothing to commit)git commit -m "Agregar escenario negativo de login y steps" -m "Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>" || (    echo No hay cambios nuevos para commitear o el commit falló (posible duplicado).):: Push to main (will prompt for credentials if needed)git push -u origin mainpause