# Dokumentation der Kalender-Seite

Die Kalender-Seite bietet eine umfassende Übersicht über Termine und ermöglicht die Verwaltung von Terminen (Erstellen, Bearbeiten, Löschen). Sie verwendet **Schedule-X** für die Kalenderansicht und integriert verschiedene UI-Komponenten wie Dialoge, Popover und Dropdowns für eine benutzerfreundliche Oberfläche.

## Zweck der Seite

Die Kalender-Seite hat folgende Hauptziele:

1. **Terminverwaltung**:
   - Ermöglicht das Erstellen, Bearbeiten und Löschen von Terminen.
   - Zeigt Termine in einer übersichtlichen Kalenderansicht an.

2. **Benutzerfreundlichkeit**:
   - Bietet eine intuitive Oberfläche für die Terminplanung.
   - Unterstützt die Auswahl von Kunden und Mitarbeitern für Termine.

3. **Integration mit Schedule-X**:
   - Nutzt **Schedule-X** für die Kalenderansicht und Terminverwaltung.
   - Zeigt Termine in Tages- und Wochenansicht an.

4. **Internationalisierung**:
   - Unterstützt mehrere Sprachen durch die Verwendung von `svelte-i18n`.

## UI/UX-Überlegungen

### 1. **Kalenderansicht**
- **Design**:
  - Die Kalenderansicht wird mit **Schedule-X** implementiert.
  - Termine werden als Ereignisse im Kalender dargestellt.
  - Die Ansicht kann zwischen **Tages-** und **Wochenansicht** umgeschaltet werden.

- **Interaktive Elemente**:
  - Beim Klicken auf einen Termin öffnet sich ein Sidebar-Panel mit den Termindetails.
  - Termine können bearbeitet oder gelöscht werden.

### 2. **Terminerstellung**
- **Design**:
  - Ein Dialog ermöglicht das Erstellen neuer Termine.
  - Der Benutzer kann Start- und Endzeit, Beschreibung, Kunden und Mitarbeiter auswählen.

- **Interaktive Elemente**:
  - Die Zeitauswahl erfolgt über ein Popover mit einer Kombination aus Kalender und Uhrzeitauswahl.
  - Kunden und Mitarbeiter können über Dropdowns ausgewählt werden.

### 3. **Terminbearbeitung**
- **Design**:
  - Ein Sidebar-Panel zeigt die Details eines ausgewählten Termins an.
  - Der Benutzer kann die Termindetails bearbeiten und speichern.

- **Interaktive Elemente**:
  - Die Bearbeitung von Terminen erfolgt über die gleichen Komponenten wie die Terminerstellung.
  - Der Benutzer kann den Termin auch löschen.

### 4. **Benachrichtigungen**
- **Design**:
  - Erfolgs- und Fehlermeldungen werden mit **svelte-sonner** angezeigt.
  - Benachrichtigungen informieren den Benutzer über den Erfolg oder Misserfolg von Aktionen (z. B. Terminerstellung, -bearbeitung, -löschung).

## Zusammenfassung

Die Kalender-Seite bietet eine umfassende und benutzerfreundliche Oberfläche für die Terminverwaltung. Sie ermöglicht das Erstellen, Bearbeiten und Löschen von Terminen und zeigt Termine in einer übersichtlichen Kalenderansicht an. Die Integration von **Schedule-X** und die Verwendung moderner UI-Komponenten sorgen für eine intuitive und effiziente Benutzererfahrung.