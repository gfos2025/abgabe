# Dokumentation des TypeScript-Typs und Enums

## `Log`
- **Beschreibung**: Ein Typ, der die Struktur eines Protokolleintrags (Log) definiert.
- **Eigenschaften**:
  - `id` (string): Die eindeutige Identifikationsnummer des Protokolleintrags.
  - `message` (string): Die Nachricht oder Beschreibung des Protokolleintrags.
  - `type` (`LogType`): Der Typ des Protokolleintrags, definiert durch das `LogType`-Enum.
  - `timestamp` (string): Der Zeitstempel des Protokolleintrags im String-Format.
  - `userId` (string | null): Die ID des Benutzers, der die Aktion ausgeführt hat. Kann `null` sein, wenn die Aktion nicht von einem Benutzer ausgeführt wurde.

## `LogType`
- **Beschreibung**: Ein Enum, das die verschiedenen Typen von Protokolleinträgen definiert.
- **Werte**:
  - `REGISTERED_USER` (0): Protokolliert die Registrierung eines neuen Benutzers.
  - `CREATED_CUSTOMER` (1): Protokolliert die Erstellung eines neuen Kunden.
  - `UPDATED_CUSTOMER` (2): Protokolliert die Aktualisierung eines bestehenden Kunden.
  - `DELETED_CUSTOMER` (3): Protokolliert die Löschung eines Kunden.
  - `CREATED_TRANSACTION` (4): Protokolliert die Erstellung einer neuen Transaktion.
  - `UPDATED_TRANSACTION` (5): Protokolliert die Aktualisierung einer bestehenden Transaktion.
  - `CREATED_APPOINTMENT` (6): Protokolliert die Erstellung eines neuen Termins.
  - `UPDATED_APPOINTMENT` (7): Protokolliert die Aktualisierung eines bestehenden Termins.
  - `DELETED_APPOINTMENT` (8): Protokolliert die Löschung eines Termins.

## Zusammenfassung
- Der Typ `Log` wird verwendet, um Protokolleinträge in der Anwendung zu modellieren. Er enthält Informationen wie die Nachricht, den Typ, den Zeitstempel und die Benutzer-ID.
- Das `LogType`-Enum definiert die verschiedenen Arten von Protokolleinträgen, die in der Anwendung auftreten können.
- Diese Struktur ermöglicht eine klare und konsistente Protokollierung von Aktionen, die in der Anwendung durchgeführt werden.