Hier ist die aktualisierte Dokumentation mit dem Hinweis zur `.env`-Datei:

---

# Dokumentation zur Installation des Frontends

## **Voraussetzungen**
- **Node.js**: Stellen Sie sicher, dass Node.js auf Ihrem System installiert ist. Sie können die neueste Version von [nodejs.org](https://nodejs.org/) herunterladen.
- **npm**: npm wird automatisch mit Node.js installiert.

## **Installation**

1. **Projekt klonen**:
   Klonen Sie das Projekt-Repository auf Ihren lokalen Rechner:
   ```bash
   git clone <repository-url>
   cd <projekt-ordner>
   ```

2. **Umgebungsvariablen konfigurieren**:
   Erstellen Sie eine `.env`-Datei im Stammverzeichnis des Projekts und fügen Sie die folgende Zeile hinzu, um die Backend-API-URL zu konfigurieren:
   ```env
   PUBLIC_BACKEND_URL='http://<url>/api'
   ```
   (Ersetzen Sie die URL durch die URL Ihres Backends.)

3. **Abhängigkeiten installieren**:
   Da das Projekt **Svelte 5** verwendet, das noch neu ist und viele Bibliotheken noch nicht vollständig dafür optimiert sind, müssen die Abhängigkeiten mit dem `--force`-Flag installiert werden. Dies ist möglich, da Svelte 5 abwärtskompatibel mit Svelte 4 ist und Bibliotheken, die für Svelte 4 entwickelt wurden, weiterhin verwendet werden können.
   ```bash
   npm i --force
   ```

4. **Projekt starten**:
   Nach der Installation der Abhängigkeiten können Sie das Projekt mit dem folgenden Befehl starten:
   ```bash
   npm run dev
   ```

5. **Anwendung aufrufen**:
   Öffnen Sie Ihren Browser und navigieren Sie zu:
   ```
   http://localhost:5173
   ```
   (Der Port kann je nach Konfiguration variieren. Überprüfen Sie die Ausgabe im Terminal.)

## **Hinweise**
- **Svelte 5**: Da Svelte 5 noch neu ist, kann es zu Kompatibilitätsproblemen mit einigen Bibliotheken kommen. Die Verwendung von `--force` ermöglicht die Installation, auch wenn Warnungen auftreten.
- **Backwards Compatibility**: Svelte 5 ist abwärtskompatibel mit Svelte 4, sodass die meisten Bibliotheken, die für Svelte 4 entwickelt wurden, weiterhin funktionieren.
- **Umgebungsvariablen**: Die `.env`-Datei ist erforderlich, um die Backend-API-URL zu konfigurieren. Stellen Sie sicher, dass die Datei korrekt erstellt und die Variable `PUBLIC_BACKEND_URL` gesetzt ist.

## **Zusammenfassung**
- Klonen Sie das Repository.
- Erstellen Sie eine `.env`-Datei und setzen Sie die `PUBLIC_BACKEND_URL`.
- Installieren Sie die Abhängigkeiten mit `npm i --force`.
- Starten Sie das Projekt mit `npm run dev`.
- Rufen Sie die Anwendung im Browser unter `http://localhost:5173` auf.

Das Projekt sollte nun erfolgreich gestartet sein und bereit für die Entwicklung oder den Testbetrieb.

--- 

Diese aktualisierte Dokumentation enthält nun den wichtigen Hinweis zur Erstellung der `.env`-Datei und zur Konfiguration der `PUBLIC_BACKEND_URL`.