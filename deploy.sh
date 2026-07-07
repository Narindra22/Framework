#!/bin/bash

APP_NAME="framework"
SRC_DIR="src/main/java"
BUILD_DIR="build"
LIB_DIR="lib"                  # Adapté à ton dossier '__lib'
JAVA_RELEASE="17"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# ==============================================================================
# CONFIGURATION DU CHEMIN VERS L'APPLICATION DE TEST
# ==============================================================================
# On remplace les "???" par le chemin qui monte d'un niveau pour aller dans l'application de test
APP_TEST_WEBAPPS="$SCRIPT_DIR/../Testapplication/src/main/webapp"
# ==============================================================================

echo "1-Nettoyage et création du répertoire temporaire"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/classes"
mkdir -p "$BUILD_DIR/lib"

echo " 2-Compilation des fichiers Java"
# On cherche tous les fichiers .java et on les compile en incluant le contenu de __lib
find "$SRC_DIR" -name "*.java" > sources.txt
javac --release "$JAVA_RELEASE" -cp ".:$LIB_DIR/*" -d "$BUILD_DIR/classes" @sources.txt

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation ! Le script s'arrête."
    rm -f sources.txt
    exit 1
fi
rm sources.txt

echo "3- Copie des Jars de dépendances vers build/lib"
cp -r $LIB_DIR/*.jar $BUILD_DIR/lib/ 2>/dev/null || true

echo "4-Génération du fichier .jar du Framework"
# On entre dans le build, on crée le jar à partir de 'classes', puis on revient
cd $BUILD_DIR || exit
jar -cvf $APP_NAME.jar -C classes .
cd ..

echo "5-Déploiement dans l'application de test"
# Création sécurisée du dossier WEB-INF/lib dans l'application de test
mkdir -p "$APP_TEST_WEBAPPS/WEB-INF/lib"

# Copie du jar du framework fraîchement généré (Correction du chemin d'accès)
cp -f "$BUILD_DIR/$APP_NAME.jar" "$APP_TEST_WEBAPPS/WEB-INF/lib/"

# Copier les autres dépendances de __lib (en ignorant l'API Servlet de Tomcat)
for j in $LIB_DIR/*.jar; do
  if [ -f "$j" ]; then
    base=$(basename "$j")
    if [[ "$base" != "servlet-api.jar" && "$base" != "jakarta.servlet-api.jar" ]]; then
      cp -f "$j" "$APP_TEST_WEBAPPS/WEB-INF/lib/"
    fi
  fi
done

echo ""
echo "========================================================================="
echo "Framework déployé avec succès dans $APP_TEST_WEBAPPS/WEB-INF/lib :"
echo "========================================================================="
ls -1 "$APP_TEST_WEBAPPS/WEB-INF/lib" || true
