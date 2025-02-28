# Dokumentation des Layouts

Dieses Layout ist die übergeordnete Struktur, die sowohl die Startseite als auch die Login-Seite umfasst. Es enthält eine **Navigationleiste (Navbar)**, die je nach Benutzerstatus (angemeldet oder nicht angemeldet) unterschiedliche Optionen anzeigt.

## Zweck des Layouts

Das Layout hat folgende Hauptziele:

1. **Konsistente Navigation**:
   	- Es bietet eine einheitliche Navigation für alle Seiten, die dieses Layout verwenden.
   	- Die Navbar ermöglicht Benutzern den Zugriff auf die Startseite, das Dashboard (falls angemeldet) oder die Login-Seite (falls nicht angemeldet).

2. **Benutzerstatus-Erkennung**:
   	- Es überprüft, ob der Benutzer angemeldet ist, indem es das Vorhandensein eines Cookies (`JSESSIONID`) prüft.
   	- Basierend auf dem Benutzerstatus werden unterschiedliche Navigationsoptionen angezeigt.

3. **Dunkler Modus**:
   	- Es unterstützt den dunklen Modus, der basierend auf den Benutzereinstellungen aktiviert wird.

4. **Benachrichtigungen**:
   	- Es integriert eine Toaster-Komponente, um Benachrichtigungen anzuzeigen.

5. **Internationalisierung**:
   	- Es verwendet `svelte-i18n`, um Texte in verschiedenen Sprachen anzuzeigen.

## UI/UX-Überlegungen

### 1. **Navigationleiste (Navbar)**
- **Design**:
  	- Die Navbar hat einen schlichten Hintergrund (`bg-background`) und eine untere Rahmenlinie (`border-b border-border`), um sie vom Hauptinhalt abzugrenzen.
  	- Die Schrift ist klar und gut lesbar, mit einer großen Schriftgröße für den Anwendungsnamen (`Worksale`).

- **Interaktive Elemente**:
  	- **Startseite-Button**: Führt den Benutzer zurück zur Startseite.
  	- **Dashboard-Button**: Wird nur angezeigt, wenn der Benutzer angemeldet ist (`isLoggedIn`).
  	- **Login-Button**: Wird angezeigt, wenn der Benutzer nicht angemeldet ist.
  	- **UserNav-Komponente**: Zeigt benutzerspezifische Optionen an (z. B. Profil oder Abmelden), wenn der Benutzer angemeldet ist.

### 2. **Dunkler Modus**
- **Automatische Erkennung**:
  	- Beim Laden der Seite wird überprüft, ob der Benutzer den dunklen Modus aktiviert hat (`localStorage.getItem('mode') === 'dark'`).
  	- Die Klasse `dark` wird dem `<html>`-Element hinzugefügt, um den dunklen Modus zu aktivieren.

- **Benutzerfreundlichkeit**:
  	- Der dunkle Modus reduziert die Augenbelastung in dunklen Umgebungen und bietet eine alternative visuelle Erfahrung.

### 3. **Toaster-Komponente**
- **Zweck**:
  	- Zeigt Benachrichtigungen an, z. B. Erfolgsmeldungen, Fehler oder Warnungen.
  	- Die Positionierung und das Styling der Toaster-Komponente sind so gestaltet, dass sie nicht den Hauptinhalt blockieren.

### 4. **Internationalisierung**
- **Dynamische Texte**:
  	- Alle Texte in der Navbar und im Footer werden über `svelte-i18n` geladen (`$t('layout.home')`, `$t('layout.dashboard')`, `$t('layout.login')`, `$t('layout.rights')`).
  	- Dies ermöglicht eine einfache Anpassung der Texte für verschiedene Sprachen.

### 5. **Footer**
- **Design**:
  	- Der Footer hat einen schlichten Hintergrund (`bg-background`) und eine obere Rahmenlinie (`border-t border-border`), um ihn vom Hauptinhalt abzugrenzen.
  	- Der Text ist zentriert und klein gehalten (`text-sm`), um eine klare Hierarchie zu schaffen.

## Zusammenfassung

Das Layout bietet eine konsistente und benutzerfreundliche Struktur für die Anwendung. Es passt sich dynamisch an den Benutzerstatus an, unterstützt den dunklen Modus und zeigt Benachrichtigungen an. Die Navbar und der Footer sind klar und intuitiv gestaltet, um eine positive Benutzererfahrung zu gewährleisten. Die Internationalisierung ermöglicht eine einfache Anpassung für verschiedene Sprachen und Regionen.