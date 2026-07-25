# Framework

Framework Java qui genere un fichier `build/framework.jar` a partir du code source
place dans `src/main/java`.

## Prerequis

- Java 17
- Bash

## Generer le jar

Depuis la racine du projet :

```bash
./deploy.sh
```

Le script compile les fichiers Java, genere `build/framework.jar`, puis copie ce jar
dans l'application de test configuree dans `deploy.sh`.

Si seule la generation du jar est necessaire, le fichier attendu se trouve ici apres
execution :

```text
build/framework.jar
```
