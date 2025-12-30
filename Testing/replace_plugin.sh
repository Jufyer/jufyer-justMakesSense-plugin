#!/bin/bash

# ==============================
# KONFIGURATION
# ==============================

# Pfad zur neuen Plugin-JAR (HIER anpassen!)
NEW_JAR="/home/julian-anders/Schreibtisch/CodeProjects/Minecraft Coding/Aquatic/jufyer-aquatic-plugin/build/libs/jufyer-aquatic-plugin-1.0.0-SNAPSHOT.jar"

# Basisordner
BASE_DIR="servers"

# ==============================
# SCRIPT
# ==============================

# Prüfen, ob die neue JAR existiert
if [ ! -f "$NEW_JAR" ]; then
  echo "❌ Die angegebene JAR existiert nicht:"
  echo "   $NEW_JAR"
  exit 1
fi

# Durch alle paper-* Ordner gehen
for SERVER_DIR in "$BASE_DIR"/paper-*; do
  PLUGIN_DIR="$SERVER_DIR/plugins"

  if [ -d "$PLUGIN_DIR" ]; then
    OLD_JAR=$(find "$PLUGIN_DIR" -maxdepth 1 -name "*.jar" | head -n 1)

    if [ -n "$OLD_JAR" ]; then
      echo "🔁 Ersetze $(basename "$OLD_JAR") in $PLUGIN_DIR"
      rm "$OLD_JAR"
      cp "$NEW_JAR" "$PLUGIN_DIR/"
    else
      echo "⚠️ Keine JAR gefunden in $PLUGIN_DIR"
    fi
  fi
done

echo "✅ Alle Plugins wurden ersetzt."
