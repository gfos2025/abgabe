# Dokumentation des Zod-Schemas und des TypeScript-Typs

## `employeeSchema`
- **Beschreibung**: Ein Zod-Schema, das die Struktur und Validierungsregeln für ein Mitarbeiterobjekt definiert.
- **Eigenschaften**:
  - `id` (string): Eindeutige Identifikationsnummer des Mitarbeiters, validiert als UUID.
  - `name` (string): Der Name des Mitarbeiters. Standardwert: `"no-name"`.
  - `surname` (string): Der Nachname des Mitarbeiters. Standardwert: `"no-surname"`.
  - `email` (string): Die E-Mail-Adresse des Mitarbeiters. Standardwert: `"no-email"`.
  - `role` (string): Die Rolle des Mitarbeiters. Standardwert: `"no-role"`.
- **Optionale Eigenschaften**: Das gesamte Schema ist optional (`optional()`), d. h., es kann auch `undefined` sein.

---

## `Employee`
- **Beschreibung**: Ein TypeScript-Typ, der aus dem `employeeSchema` abgeleitet wird. Er repräsentiert die Struktur eines Mitarbeiterobjekts.
- **Eigenschaften**:
  - `id` (string): Eindeutige Identifikationsnummer des Mitarbeiters.
  - `name` (string): Der Name des Mitarbeiters.
  - `surname` (string): Der Nachname des Mitarbeiters.
  - `email` (string): Die E-Mail-Adresse des Mitarbeiters.
  - `role` (string): Die Rolle des Mitarbeiters.
- **Optionale Eigenschaften**: Der Typ `Employee` kann auch `undefined` sein, da das Schema optional ist.

---

## Zusammenfassung
- Das `employeeSchema` wird verwendet, um Mitarbeiterobjekte zu validieren und sicherzustellen, dass sie die erforderliche Struktur und Datentypen haben.
- Der Typ `Employee` wird aus dem Schema abgeleitet und bietet TypeScript-Typsicherheit für Mitarbeiterobjekte in der Anwendung.
- Beide sind optional, d. h., sie können auch `undefined` sein, falls kein Mitarbeiterobjekt vorhanden ist.