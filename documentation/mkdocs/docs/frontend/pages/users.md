# Dokumentation der Benutzerverwaltungsseite

Die Benutzerverwaltungsseite bietet eine Übersicht über alle Benutzer und Kunden, die im System registriert sind. Sie ermöglicht das Filtern und Anzeigen von Benutzern nach ihrer Rolle (z. B. Mitarbeiter oder Administrator) sowie das Hinzufügen neuer Benutzer. Die Seite ist in verschiedene Abschnitte unterteilt, die eine effiziente Verwaltung der Benutzerdaten ermöglichen.

## Zweck der Seite

Die Seite dient dazu, alle Benutzer und Kunden in einer übersichtlichen Tabelle anzuzeigen. Sie ermöglicht es Administratoren, Benutzer nach ihrer Rolle zu filtern, neue Benutzer hinzuzufügen und die zugehörigen Details (Name, E-Mail, Rolle, Firma, Kontaktstatus und zugewiesener Manager) einzusehen.

## UI/UX-Überlegungen

### Design
- Die Seite ist in drei Hauptabschnitte unterteilt:
  1. **Titel und Beschreibung**: Zeigt den Titel der Seite und eine kurze Beschreibung an.
  2. **Tabs**: Ermöglicht das Filtern der Daten nach **Alle**, **Mitarbeiter** oder **Kunden**.
  3. **Tabelle**: Zeigt die gefilterten Daten in einer strukturierten Tabelle an.

- Die Seite ist responsiv und passt sich verschiedenen Bildschirmgrößen an.

### Interaktive Elemente
- **Button "Add User"**: Öffnet ein Dialogfenster zum Hinzufügen eines neuen Benutzers.
- **Tabs**: Ermöglicht das Filtern der Daten nach Benutzertyp (Alle, Mitarbeiter, Kunden).
- **Tabelle**: Zeigt die gefilterten Daten an und unterstützt die Anzeige von Details wie Name, E-Mail, Rolle, Firma, Kontaktstatus und zugewiesenem Manager.

## Funktionalitäten

### 1. **Anzeigen von Benutzern und Kunden**
- Die Seite lädt beim Start alle Benutzer und Kunden aus der API.
- Die Daten werden in einer Tabelle angezeigt, die nach Name, E-Mail, Rolle, Firma, Kontaktstatus und zugewiesenem Manager sortiert ist.

### 2. **Filtern der Daten**
- Die Seite bietet drei Tabs:
  - **Alle**: Zeigt alle Benutzer und Kunden an.
  - **Mitarbeiter**: Filtert die Daten, um nur Mitarbeiter (Rolle: `EMPLOYEE`) anzuzeigen.
  - **Kunden**: Filtert die Daten, um nur Kunden anzuzeigen.

### 3. **Hinzufügen eines neuen Benutzers**
- Über den Button "Add User" wird ein Dialogfenster geöffnet, in dem ein neuer Benutzer hinzugefügt werden kann.
- Der Benutzer kann Name, Nachname, E-Mail, Passwort und Rolle (Mitarbeiter oder Administrator) eingeben.
- Nach dem Hinzufügen wird die Benutzerliste automatisch aktualisiert.

### 4. **Anzeige von Rollen**
- Die Rolle eines Benutzers wird durch ein farbiges Badge dargestellt:
  - **Mitarbeiter**: Blau (`EMPLOYEE`).
  - **Administrator**: Lila (`ADMIN`).
  - **Kunde**: Grün (`CUSTOMER`).

## Dialoge und Komponenten

### 1. **Dialog zum Hinzufügen eines Benutzers**
- **Zweck**: Ermöglicht das Hinzufügen eines neuen Benutzers.
- **Felder**:
  - Name
  - Nachname
  - E-Mail
  - Passwort
  - Rolle (Mitarbeiter oder Administrator)
- **Funktionen**:
  - Überprüft, ob alle Felder ausgefüllt sind.
  - Sendet die Daten an die API, um den Benutzer zu registrieren.
  - Schließt das Dialogfenster und aktualisiert die Benutzerliste nach erfolgreicher Registrierung.

### 2. **Tabs für Filterung**
- **Zweck**: Ermöglicht das Filtern der Daten nach Benutzertyp.
- **Optionen**:
  - **Alle**: Zeigt alle Benutzer und Kunden an.
  - **Mitarbeiter**: Zeigt nur Benutzer mit der Rolle `EMPLOYEE` an.
  - **Kunden**: Zeigt nur Kunden an.

### 3. **Tabelle zur Anzeige der Daten**
- **Zweck**: Zeigt die gefilterten Daten in einer strukturierten Tabelle an.
- **Spalten**:
  - Name
  - E-Mail
  - Rolle (als Badge dargestellt)
  - Firma (nur für Kunden)
  - Kontaktstatus (nur für Kunden)
  - Zugewiesener Manager (nur für Kunden)

## Zusammenfassung

Die Benutzerverwaltungsseite bietet eine umfassende und benutzerfreundliche Oberfläche zur Verwaltung von Benutzern und Kunden. Sie ermöglicht das Filtern der Daten nach Rolle, das Hinzufügen neuer Benutzer und die Anzeige detaillierter Informationen in einer strukturierten Tabelle. Die Seite ist ideal für Administratoren, die Benutzerdaten effizient verwalten und neue Benutzer hinzufügen möchten.