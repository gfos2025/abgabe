# Dokumentation der Protokollseite

Die Protokollseite bietet eine Übersicht über alle Aktionen, die in der Anwendung durchgeführt wurden. Diese Aktionen werden unabhängig vom ausführenden Benutzer protokolliert und können nach Zeitraum und Typ gefiltert werden. Die Seite ist in verschiedene Abschnitte unterteilt, die eine effiziente Analyse und Überwachung der Aktivitäten ermöglichen.

## Zweck der Seite

Die Protokollseite dient dazu, alle Aktionen in der Anwendung zu protokollieren und in einer übersichtlichen Tabelle anzuzeigen. Sie ermöglicht es Administratoren, Aktivitäten nach Zeitraum (Woche, Monat, Jahr) und Typ (z. B. Benutzerregistrierung, Kundenaktualisierung, Transaktionen usw.) zu filtern. Dies hilft bei der Überwachung und Analyse von Systemaktivitäten.

## UI/UX-Überlegungen

### Design
- Die Seite ist in drei Hauptabschnitte unterteilt:
  1. **Titel und Beschreibung**: Zeigt den Titel der Seite und eine kurze Beschreibung an.
  2. **Filteroptionen**: Ermöglicht das Filtern der Protokolle nach Zeitraum und Typ.
  3. **Tabelle**: Zeigt die gefilterten Protokolle in einer strukturierten Tabelle an.

- Die Seite ist responsiv und passt sich verschiedenen Bildschirmgrößen an.

### Interaktive Elemente
- **Tabs für Zeitraumfilter**: Ermöglicht das Filtern der Protokolle nach Woche, Monat oder Jahr.
- **Popover für Log-Typ-Filter**: Ein Dropdown-Menü, das die Auswahl des Protokolltyps (z. B. Benutzerregistrierung, Kundenaktualisierung) ermöglicht.
- **Suchfunktion**: Ermöglicht die Suche nach spezifischen Protokolltypen.
- **Tabelle**: Zeigt die gefilterten Protokolle an, sortiert nach dem neuesten Ereignis.

## Funktionalitäten

### 1. **Anzeigen von Protokollen**
- Die Seite lädt beim Start alle Protokolle aus der API.
- Die Protokolle werden in einer Tabelle angezeigt, die nach Zeitstempel sortiert ist (neueste zuerst).

### 2. **Filtern nach Zeitraum**
- Die Seite bietet drei Tabs zur Auswahl des Zeitraums:
  - **Diese Woche**: Zeigt Protokolle der letzten 7 Tage an.
  - **Diesen Monat**: Zeigt Protokolle der letzten 30 Tage an.
  - **Dieses Jahr**: Zeigt Protokolle der letzten 365 Tage an.

### 3. **Filtern nach Protokolltyp**
- Über ein Popover-Menü kann der Benutzer den Protokolltyp auswählen, z. B.:
  - Benutzerregistrierung
  - Kundenaktualisierung
  - Transaktionserstellung
  - Terminerstellung
  - Alle Protokolle (Standard)

### 4. **Suche nach Protokolltyp**
- Der Benutzer kann nach spezifischen Protokolltypen suchen, indem er einen Suchbegriff eingibt. Die Liste der verfügbaren Typen wird dynamisch gefiltert.

## Dialoge und Komponenten

### 1. **Tabs für Zeitraumfilter**
- **Zweck**: Ermöglicht das Filtern der Protokolle nach einem bestimmten Zeitraum.
- **Optionen**:
  - **Diese Woche**: Protokolle der letzten 7 Tage.
  - **Diesen Monat**: Protokolle der letzten 30 Tage.
  - **Dieses Jahr**: Protokolle der letzten 365 Tage.

### 2. **Popover für Log-Typ-Filter**
- **Zweck**: Ermöglicht die Auswahl des Protokolltyps.
- **Funktionen**:
  - Zeigt eine Liste aller verfügbaren Protokolltypen an.
  - Unterstützt die Suche nach spezifischen Typen.
  - Aktualisiert die Tabelle basierend auf der Auswahl.

### 3. **Tabelle zur Anzeige der Protokolle**
- **Zweck**: Zeigt die gefilterten Protokolle in einer strukturierten Tabelle an.
- **Spalten**:
  - **Zeitstempel**: Das Datum und die Uhrzeit der Aktion.
  - **Typ**: Der Typ der Aktion (z. B. Benutzerregistrierung, Kundenaktualisierung).
  - **Details**: Weitere Informationen zur Aktion (z. B. betroffener Benutzer oder Kunde).

## Zusammenfassung

Die Protokollseite bietet eine umfassende und benutzerfreundliche Oberfläche zur Überwachung aller Aktivitäten in der Anwendung. Sie ermöglicht das Filtern der Protokolle nach Zeitraum und Typ sowie die Suche nach spezifischen Aktionen. Die Seite ist ideal für Administratoren, die Systemaktivitäten überwachen und analysieren möchten. Durch die klare Struktur und die interaktiven Filteroptionen ist die Seite effizient und einfach zu bedienen.