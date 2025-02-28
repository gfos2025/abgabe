# Dokumentation der TypeScript-Interfaces

## `ReportData`
- **Beschreibung**: Ein Interface, das die Struktur von Berichtsdaten für Transaktionen definiert.
- **Eigenschaften**:
  - `totalTransactions` (number): Die Gesamtanzahl der Transaktionen.
  - `totalRevenue` (number): Der Gesamtumsatz, der durch die Transaktionen generiert wurde.
  - `averageTransactionAmount` (number): Der durchschnittliche Betrag pro Transaktion.
  - `transactionsByStatus` (Objekt): Ein Objekt, das die Anzahl der Transaktionen nach ihrem Status aufschlüsselt:
    - `Completed` (number): Die Anzahl der abgeschlossenen Transaktionen.
    - `Failed` (number): Die Anzahl der fehlgeschlagenen Transaktionen.
    - `Pending` (number): Die Anzahl der ausstehenden Transaktionen.

## `MonthlyReport`
- **Beschreibung**: Ein Interface, das die Struktur eines monatlichen Berichts definiert.
- **Eigenschaften**:
  - `month` (string): Der Monat, auf den sich der Bericht bezieht (z. B. `"January 2023"`).
  - `reportData` (`ReportData`): Die Berichtsdaten für den entsprechenden Monat, basierend auf dem `ReportData`-Interface.

## Zusammenfassung
- `ReportData` bietet eine detaillierte Übersicht über Transaktionsdaten, einschließlich Gesamtumsatz, durchschnittlichem Betrag und Statusverteilung.
- `MonthlyReport` erweitert dies um eine monatliche Zuordnung, sodass Berichte für spezifische Zeiträume erstellt werden können.
- Diese Interfaces sind nützlich für die Analyse und Darstellung von Transaktionsdaten in der Anwendung.