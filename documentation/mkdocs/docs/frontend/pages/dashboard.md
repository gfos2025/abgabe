# Dokumentation der Dashboard-Seite

Die Dashboard-Seite bietet einen umfassenden Überblick über die wichtigsten Kennzahlen, Transaktionen und Termine des Benutzers. Sie ist in mehrere Abschnitte unterteilt, die jeweils spezifische Informationen und Funktionen bereitstellen. Die Seite ist responsiv und unterstützt verschiedene Ansichten (z. B. Gesamt- oder persönliche Berichte).

## Zweck der Seite

Die Dashboard-Seite hat folgende Hauptziele:

1. **Übersicht über Finanzdaten**:
   - Zeigt die Gesamteinnahmen, die Anzahl der Transaktionen und den durchschnittlichen Transaktionsbetrag an.
   - Bietet eine monatliche Übersicht in Form eines Balkendiagramms.

2. **Terminverwaltung**:
   - Listet bevorstehende Termine auf und warnt den Benutzer, wenn ein Termin bald beginnt.

3. **Transaktionsverwaltung**:
   - Zeigt die neuesten Transaktionen an und ermöglicht den Zugriff auf eine detaillierte Transaktionsübersicht.

4. **Benutzerfreundlichkeit**:
   - Bietet eine intuitive Navigation und klare visuelle Darstellung der Daten.
   - Unterstützt die Anzeige von Gesamt- oder persönlichen Berichten.

## UI/UX-Überlegungen

### 1. **Finanzkennzahlen**
- **Design**:
  - Drei Karten zeigen die wichtigsten Finanzkennzahlen an:
    - **Gesamteinnahmen**: Der Gesamtbetrag der Einnahmen im aktuellen Monat.
    - **Anzahl der Transaktionen**: Die Gesamtzahl der Transaktionen im aktuellen Monat.
    - **Durchschnittlicher Transaktionsbetrag**: Der durchschnittliche Betrag pro Transaktion im aktuellen Monat.
  - Jede Karte zeigt den prozentualen Anstieg oder Rückgang im Vergleich zum Vormonat an.

- **Interaktive Elemente**:
  - Die Karten sind interaktiv und reagieren auf Hover-Effekte (Skalierung und Schatten).

### 2. **Monatliches Balkendiagramm**
- **Design**:
  - Ein Balkendiagramm zeigt die monatlichen Finanzdaten an.
  - Der Benutzer kann zwischen verschiedenen Ansichten wechseln:
    - **Gesamt**: Zeigt die Gesamteinnahmen pro Monat.
    - **Durchschnitt**: Zeigt den durchschnittlichen Transaktionsbetrag pro Monat.
    - **Transaktionen**: Zeigt die Anzahl der Transaktionen pro Monat.

- **Interaktive Elemente**:
  - Der Benutzer kann auf die Balken klicken, um detaillierte Informationen anzuzeigen.

### 3. **Bevorstehende Termine**
- **Design**:
  - Eine Tabelle listet die bevorstehenden Termine auf.
  - Die Termine sind nach Datum sortiert.
  - Die linke Rahmenfarbe der Tabellenzeile ändert sich basierend auf der verbleibenden Zeit bis zum Termin:
    - **Rot**: Der Termin beginnt in weniger als einer Stunde.
    - **Gelb**: Der Termin beginnt in weniger als drei Stunden.
    - **Grün**: Der Termin beginnt in mehr als drei Stunden.

- **Interaktive Elemente**:
  - Der Benutzer erhält eine Warnung (Toast), wenn ein Termin bald beginnt.

### 4. **Neueste Transaktionen**
- **Design**:
  - Eine Tabelle listet die neuesten Transaktionen auf.
  - Jede Zeile zeigt den Kundennamen und den Transaktionsbetrag an.

- **Interaktive Elemente**:
  - Der Benutzer kann auf "Alle anzeigen" klicken, um zur detaillierten Transaktionsübersicht zu gelangen.

## Zusammenfassung

Die Dashboard-Seite bietet eine umfassende Übersicht über Finanzdaten, Termine und Transaktionen. Sie ist benutzerfreundlich, responsiv und unterstützt verschiedene Ansichten und Berichtstypen. Die Seite ist ideal für Benutzer, die schnell auf wichtige Informationen zugreifen und ihre Aktivitäten verwalten möchten.