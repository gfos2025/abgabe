# Dokumentation der Enums, Konstanten und Typen

## `TransactionStatus`
- **Beschreibung**: Ein Enum, das die verschiedenen Status einer Transaktion definiert.
- **Werte**:
  - `Completed` (0): Die Transaktion wurde erfolgreich abgeschlossen.
  - `Pending` (1): Die Transaktion ist ausstehend und wurde noch nicht abgeschlossen.
  - `Failed` (2): Die Transaktion ist fehlgeschlagen.

## `transactionStatusMap`
- **Beschreibung**: Ein Objekt, das jeden `TransactionStatus`-Wert einem Übersetzungsschlüssel (String) zuordnet. Dies wird häufig für die Internationalisierung (i18n) verwendet.
- **Struktur**:
  ```typescript
  {
    [TransactionStatus.Completed]: 'status.completed',
    [TransactionStatus.Pending]: 'status.pending',
    [TransactionStatus.Failed]: 'status.failed'
  }
  ```
- **Verwendung**: Dieses Objekt kann verwendet werden, um den Status einer Transaktion in eine benutzerfreundliche Nachricht zu übersetzen.

## `Transaction`
- **Beschreibung**: Ein TypeScript-Typ, der die Struktur einer Transaktion definiert.
- **Eigenschaften**:
  - `id` (string): Die eindeutige Identifikationsnummer der Transaktion.
  - `customerId` (string): Die ID des Kunden, der mit der Transaktion verbunden ist.
  - `userId` (string): Die ID des Benutzers, der die Transaktion durchgeführt hat.
  - `amount` (number): Der Betrag der Transaktion.
  - `description` (string, optional): Eine optionale Beschreibung der Transaktion.
  - `timestamp` (string): Der Zeitstempel der Transaktion im String-Format.
  - `status` (`TransactionStatus`): Der Status der Transaktion, definiert durch das `TransactionStatus`-Enum.

## Zusammenfassung
- Das `TransactionStatus`-Enum definiert die möglichen Status einer Transaktion.
- Die `transactionStatusMap`-Konstante ordnet jeden Status einem Übersetzungsschlüssel zu, was für die Internationalisierung nützlich ist.
- Der Typ `Transaction` beschreibt die Struktur einer Transaktion in der Anwendung und enthält alle relevanten Informationen wie Betrag, Zeitstempel und Status.
- Diese Strukturen sorgen für Klarheit, Typsicherheit und Konsistenz bei der Verwaltung von Transaktionen in der Anwendung.