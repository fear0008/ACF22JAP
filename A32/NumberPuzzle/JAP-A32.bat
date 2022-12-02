:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: ASSIGNMENTS - CST8221 - Fall 2022
:: ---------------------------------------------------------------------
:: Begin of Script (Assignments - F22)
:: ---------------------------------------------------------------------

:: LOCAL VARIABLES ....................................................
SET SRCDIR=src
SET BINDIR=bin
SET BINOUT=game-javac.out
SET BINERR=game-javac.err
SET JARNAME=NumberPuzzle.jar
SET JAROUT=game-jar.out
SET JARERR=game-jar.err
SET DOCDIR=doc
SET DOCPACK=game
SET DOCOUT=game-javadoc.out
SET DOCERR=game-javadoc.err
SET MAINCLASSSRC=src/game/Game.java
SET MAINCLASSBIN=game.Game
SET SECONDCLASSSRC=src/game/GameModel.java
SET THIRDCLASSSRC=src/game/GameView.java
SET FOURTHCLASSSRC=src/game/GameController.java
SET SERVERCLASSSRC=src/game/GameServer.java
SET CLIENTCLASSSRC=src/game/GameClient.java
SET CONFIGCLASSSRC=src/game/GameConfig.java
SET IMAGESOURCE=src\images
SET IMAGEDESTINATION=bin\images

@echo off

ECHO " _________________________________ "
ECHO "|     __    _  ___    ___  _      |"
ECHO "|    |  |  / \ \  \  /  / / \     |"
ECHO "|    |  | /   \ \  \/  / /   \    |"
ECHO "|    |  |/  _  \ \    / /  _  \   |"
ECHO "|  __|  |  / \  \ \  / /  / \  \  |"
ECHO "|  \____/_/   \__\ \/ /__/   \__\ |"
ECHO "|                                 |"
ECHO "| .. ALGONQUIN COLLEGE - 2022F .. |"
ECHO "|_________________________________|"
ECHO "                                   "
ECHO "[ASSIGNMENT SCRIPT ---------------]"

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%SRCDIR%;" %MAINCLASSSRC% %SECONDCLASSSRC% %THIRDCLASSSRC% %FOURTHCLASSSRC% %SERVERCLASSSRC% %CLIENTCLASSSRC% %CONFIGCLASSSRC% -d %BINDIR% > %BINOUT% 2> %BINERR%

ECHO "2. Creating Jar ..................."
mkdir %IMAGEDESTINATION%
copy %IMAGESOURCE% %IMAGEDESTINATION%
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% . > %JAROUT% 2> %JARERR%

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -cp ".;%BINDIR%;" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > %DOCOUT% 2> %DOCERR%

cd bin
ECHO "4. Running Jar ...................."
start java -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Assignments - F22)
:: ---------------------------------------------------------------------
