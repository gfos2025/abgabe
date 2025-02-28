# Dokumentation der Kunden-Seite (erweitert)

Die Kunden-Seite bietet eine Übersicht über alle Kunden und ermöglicht die Verwaltung von Kundendaten. Sie verwendet eine **DataTable**-Komponente, um die Kundenliste anzuzeigen, und bietet Funktionen wie Sortieren, Filtern, Paginierung sowie das Erstellen, Bearbeiten und Löschen von Kunden. Die Seite ist benutzerfreundlich und unterstützt die effiziente Verwaltung von Kundendaten.

## Zweck der Seite

Die Kunden-Seite hat folgende Hauptziele:

1. **Kundenverwaltung**:
   - Zeigt eine Liste aller Kunden an.
   - Ermöglicht das Filtern, Sortieren und Durchsuchen von Kundendaten.
   - Bietet Funktionen zum Erstellen, Bearbeiten und Löschen von Kunden.

2. **Benutzerfreundlichkeit**:
   - Bietet eine intuitive Oberfläche für die Verwaltung von Kundendaten.
   - Unterstützt die Anzeige von Details wie Name, Nachname, Telefonnummer, E-Mail, Adresse, Unternehmen und Kontaktstatus.

3. **Datenverwaltung**:
   - Ermöglicht die Auswahl mehrerer Kunden für Batch-Operationen.
   - Bietet eine Paginierung für die einfache Navigation durch große Datenmengen.

4. **Internationalisierung**:
   - Unterstützt mehrere Sprachen durch die Verwendung von `svelte-i18n`.

## UI/UX-Überlegungen

### 1. **Kundenliste**
- **Design**:
  - Die Kundenliste wird in einer Tabelle dargestellt.
  - Jede Zeile enthält die Details eines Kunden (Name, Nachname, Telefonnummer, E-Mail, Adresse, Unternehmen, Kontaktstatus).

- **Interaktive Elemente**:
  - Der Benutzer kann die Tabelle nach verschiedenen Spalten sortieren.
  - Die Tabelle unterstützt die Filterung nach Telefonnummer, Kontaktstatus und Priorität.

### 2. **Toolbar**
- **Design**:
  - Eine Toolbar oberhalb der Tabelle bietet Funktionen zum Filtern, Durchsuchen und Hinzufügen von Kunden.

- **Interaktive Elemente**:
  - Der Benutzer kann nach bestimmten Kriterien filtern (z. B. Name, Telefonnummer, Kontaktstatus).
  - Die Toolbar ermöglicht die Auswahl mehrerer Kunden für Batch-Operationen.
  - Ein Button zum Hinzufügen eines neuen Kunden ist in der Toolbar integriert.

### 3. **Paginierung**
- **Design**:
  - Eine Paginierungsleiste unterhalb der Tabelle ermöglicht die Navigation durch die Kundendaten.

- **Interaktive Elemente**:
  - Der Benutzer kann zwischen den Seiten der Kundendaten navigieren.
  - Die Anzahl der angezeigten Kunden pro Seite kann angepasst werden.

### 4. **Aktionen**
- **Design**:
  - Jede Zeile der Tabelle enthält eine Aktionenspalte mit Optionen zum Bearbeiten oder Löschen des Kunden.

- **Interaktive Elemente**:
  - Der Benutzer kann einen Kunden bearbeiten oder löschen, indem er auf die entsprechenden Aktionen klickt.

## Funktionalitäten

### 1. **Kundendaten anzeigen**
- Die Seite lädt die Kundendaten beim Start und zeigt sie in einer Tabelle an.
- Die Tabelle enthält Spalten für Name, Nachname, Telefonnummer, E-Mail, Adresse, Unternehmen und Kontaktstatus.

### 2. **Sortieren**
- Der Benutzer kann die Tabelle nach verschiedenen Spalten sortieren (z. B. Name, Nachname, Kontaktstatus).

### 3. **Filtern**
- Der Benutzer kann die Tabelle nach Telefonnummer, Kontaktstatus und Priorität filtern.
- Die Filterung erfolgt über die Toolbar oberhalb der Tabelle.

### 4. **Paginierung**
- Die Tabelle unterstützt die Paginierung, um große Datenmengen effizient anzuzeigen.
- Der Benutzer kann zwischen den Seiten navigieren und die Anzahl der angezeigten Kunden pro Seite anpassen.

### 5. **Aktionen**
- Der Benutzer kann einen Kunden bearbeiten oder löschen, indem er auf die entsprechenden Aktionen in der Aktionenspalte klickt.

### 6. **Kunden erstellen**
- Der Benutzer kann einen neuen Kunden erstellen, indem er auf den "Kunden hinzufügen"-Button in der Toolbar klickt.
- Ein Dialog öffnet sich, in dem der Benutzer die Kundendaten eingeben kann (Name, Nachname, Telefonnummer, E-Mail, Adresse, Unternehmen, Kontaktstatus).

### 7. **Kunden bearbeiten**
- Der Benutzer kann die Details eines Kunden bearbeiten, indem er auf die Bearbeiten-Aktion in der Aktionenspalte klickt.
- Ein Dialog öffnet sich, in dem der Benutzer die Kundendaten aktualisieren kann.

### 8. **Kunden löschen**
- Der Benutzer kann einen Kunden löschen, indem er auf die Löschen-Aktion in der Aktionenspalte klickt.
- Ein Bestätigungsdialog wird angezeigt, um sicherzustellen, dass der Benutzer den Kunden wirklich löschen möchte.

## Dialoge

### 1. **Dialog zum Löschen eines Kunden**
- **Zweck**: Bestätigt die Löschung eines Kunden.
- **Funktionen**:
  - Zeigt eine Warnmeldung an, um sicherzustellen, dass der Benutzer den Kunden wirklich löschen möchte.
  - Der Benutzer kann die Löschung bestätigen oder abbrechen.

### 2. **Dialog zum Bearbeiten eines Kunden**
- **Zweck**: Ermöglicht das Bearbeiten der Kundendaten.
- **Funktionen**:
  - Der Benutzer kann die Kundendaten (Name, Nachname, Telefonnummer, E-Mail, Adresse, Unternehmen, Kontaktstatus) aktualisieren.
  - Der Dialog überprüft, ob alle erforderlichen Felder ausgefüllt sind, bevor die Änderungen gespeichert werden.

### 3. **Dialog zum Erstellen eines Kunden**
- **Zweck**: Ermöglicht das Hinzufügen eines neuen Kunden.
- **Funktionen**:
  - Der Benutzer kann die Kundendaten (Name, Nachname, Telefonnummer, E-Mail, Adresse, Unternehmen, Kontaktstatus) eingeben.
  - Der Dialog überprüft, ob alle erforderlichen Felder ausgefüllt sind, bevor der Kunde erstellt wird.

## Zusammenfassung

Die Kunden-Seite bietet eine umfassende und benutzerfreundliche Oberfläche für die Verwaltung von Kundendaten. Sie ermöglicht das Anzeigen, Sortieren, Filtern, Erstellen, Bearbeiten und Löschen von Kundendaten in einer übersichtlichen Tabelle. Die Integration von Funktionen wie Paginierung, Batch-Operationen und Dialogen sorgt für eine effiziente Datenverwaltung. Die Seite ist ideal für Benutzer, die schnell auf Kundendaten zugreifen und diese verwalten möchten.