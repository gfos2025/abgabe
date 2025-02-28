# Dokumentation des Dashboard-Layouts

Das Dashboard-Layout ist die übergeordnete Struktur für alle Dashboard-Seiten. Es besteht aus einer **Sidebar-Navigation** für größere Bildschirme und einer **mobilen Navigation** für kleinere Bildschirme. Das Layout passt sich dynamisch an die Bildschirmgröße und die Benutzerrolle an, um eine optimale Benutzererfahrung zu gewährleisten.

## Zweck des Layouts

Das Dashboard-Layout hat folgende Hauptziele:

1. **Konsistente Navigation**:
   - Bietet eine einheitliche Navigation für alle Dashboard-Seiten.
   - Passt die Navigation basierend auf der Benutzerrolle an (z. B. Admin-spezifische Links).

2. **Responsive Design**:
   - Zeigt eine **Sidebar-Navigation** auf größeren Bildschirmen und eine **mobile Navigation** auf kleineren Bildschirmen.
   - Passt sich dynamisch an die Bildschirmgröße an.

3. **Benutzerrollen**:
   - Unterscheidet zwischen normalen Benutzern und Administratoren, um rollenspezifische Navigationslinks anzuzeigen.

4. **Internationalisierung**:
   - Unterstützt mehrere Sprachen durch die Verwendung von `svelte-i18n`.

## UI/UX-Überlegungen

### 1. **Sidebar-Navigation**
- **Design**:
  - Die Sidebar ist auf der linken Seite des Bildschirms platziert und enthält Navigationslinks.
  - Aktive Links werden durch einen farbigen Hintergrund hervorgehoben.

- **Interaktive Elemente**:
  - Jeder Link zeigt ein Icon und einen Text an.
  - Die Links führen zu verschiedenen Dashboard-Seiten (z. B. Dashboard, Kalender, Kunden, Transaktionen, Einstellungen).

- **Rollenbasierte Links**:
  - Administratoren sehen zusätzliche Links (z. B. Benutzerverwaltung, Logs).

### 2. **Mobile Navigation**
- **Design**:
  - Die mobile Navigation wird als Hamburger-Menü dargestellt und öffnet sich als Overlay.
  - Das Menü kann durch Klicken auf das Hamburger-Icon geöffnet und durch Klicken auf das Schließen-Icon geschlossen werden.

- **Interaktive Elemente**:
  - Die Links sind vertikal angeordnet und enthalten Icons und Texte.
  - Beim Klicken auf einen Link wird das Menü automatisch geschlossen.

### 3. **Responsive Design**
- **Bildschirmgrößenabhängige Anpassung**:
  - Auf Bildschirmen kleiner als 1024px wird die mobile Navigation angezeigt.
  - Auf größeren Bildschirmen wird die Sidebar-Navigation angezeigt.

- **Dynamische Anpassung**:
  - Die Navigation passt sich dynamisch an Änderungen der Bildschirmgröße an.

### 4. **Internationalisierung**
- **Dynamische Texte**:
  - Alle Texte in der Navigation werden über `svelte-i18n` geladen (`$t('dashboard.title')`, `$t('calendar.title')`, usw.).
  - Dies ermöglicht eine einfache Anpassung der Texte für verschiedene Sprachen.

## Zusammenfassung

Das Dashboard-Layout bietet eine konsistente und benutzerfreundliche Navigation für alle Dashboard-Seiten. Es passt sich dynamisch an die Bildschirmgröße und die Benutzerrolle an und unterstützt mehrere Sprachen. Die Sidebar-Navigation und die mobile Navigation gewährleisten eine optimale Benutzererfahrung auf allen Geräten.