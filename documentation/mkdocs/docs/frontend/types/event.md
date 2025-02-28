# Dokumentation des TypeScript-Typs

## `EventId`
- **Beschreibung**: Ein Union-Typ, der entweder eine `number` oder einen `string` repräsentiert. Wird verwendet, um die ID eines Ereignisses zu definieren.
- **Mögliche Werte**:
  - `number`: Eine numerische ID.
  - `string`: Eine alphanumerische ID.

## `Event`
- **Beschreibung**: Ein Interface, das die Struktur eines Ereignisses (Event) definiert.
- **Eigenschaften**:
  - `id` (`EventId`): Die eindeutige Identifikationsnummer des Ereignisses. Kann entweder eine Zahl oder ein String sein.
  - `start` (string): Der Startzeitpunkt des Ereignisses im String-Format.
  - `end` (string): Der Endzeitpunkt des Ereignisses im String-Format.
  - `title` (string, optional): Der Titel des Ereignisses.
  - `people` (Array von string, optional): Eine Liste von Personen, die mit dem Ereignis verbunden sind.
  - `location` (string, optional): Der Ort, an dem das Ereignis stattfindet.
  - `description` (string, optional): Eine Beschreibung des Ereignisses.
  - `calendarId` (string, optional): Die ID des Kalenders, zu dem das Ereignis gehört.
  - `_customContent` (Objekt, optional): Ein optionales Objekt, das benutzerdefinierte Inhalte für verschiedene Ansichten enthält:
    - `timeGrid` (string, optional): Benutzerdefinierter Inhalt für die Zeitraster-Ansicht.
    - `dateGrid` (string, optional): Benutzerdefinierter Inhalt für die Datumsraster-Ansicht.
    - `monthGrid` (string, optional): Benutzerdefinierter Inhalt für die Monatsraster-Ansicht.
    - `monthAgenda` (string, optional): Benutzerdefinierter Inhalt für die Monatsagenda-Ansicht.

## Zusammenfassung
- Das `Event`-Interface wird verwendet, um Ereignisse in der Anwendung zu modellieren.
- Es bietet Flexibilität durch optionale Eigenschaften wie Titel, Personen, Ort, Beschreibung und Kalender-ID.
- Die `_customContent`-Eigenschaft ermöglicht die Anpassung der Darstellung des Ereignisses in verschiedenen Kalenderansichten.
- Der Typ `EventId` sorgt dafür, dass die ID eines Ereignisses entweder eine Zahl oder ein String sein kann.