# Dokumentation der TypeScript-Typen

## `Participant`
- **Beschreibung**: Repräsentiert einen Teilnehmer eines Termins.
- **Eigenschaften**:
  - `id` (string): Eindeutige Identifikationsnummer des Teilnehmers.
  - `name` (string): Name des Teilnehmers.
  - `role` (string): Rolle des Teilnehmers. Kann entweder `'ORGANIZER'` (Organisator) oder `'PARTICIPANT'` (Teilnehmer) sein.

## `Appointment`
- **Beschreibung**: Repräsentiert einen Termin mit Details wie Start- und Endzeit, Beschreibung und Teilnehmern.
- **Eigenschaften**:
  - `id` (string): Eindeutige Identifikationsnummer des Termins.
  - `startTime` (string): Startzeit des Termins im String-Format.
  - `endTime` (string): Endzeit des Termins im String-Format.
  - `description` (string): Beschreibung des Termins.
  - `participants` (Array von `Participant`): Liste der Teilnehmer, die an dem Termin teilnehmen.

Diese Typen werden verwendet, um Termine und deren Teilnehmer in der Anwendung zu modellieren und zu verwalten.