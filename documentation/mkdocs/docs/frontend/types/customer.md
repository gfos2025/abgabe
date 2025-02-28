# Dokumentation des Zod-Schemas und des TypeScript-Typs

## `customerSchema`
- **Beschreibung**: Ein Zod-Schema, das die Struktur und Validierungsregeln für ein Kundenobjekt definiert.
- **Eigenschaften**:
  - `id` (string): Eindeutige Identifikationsnummer des Kunden, validiert als UUID.
  - `name` (string): Der Name des Kunden. Standardwert: `"no-name"`.
  - `surname` (string): Der Nachname des Kunden. Standardwert: `"no-surname"`.
  - `email` (string): Die E-Mail-Adresse des Kunden. Standardwert: `"no-email"`.
  - `address` (string): Die Adresse des Kunden. Standardwert: `"no-address"`.
  - `phoneNumber` (string): Die Telefonnummer des Kunden. Standardwert: `"no-phoneNumber"`.
  - `company` (string): Die Firma des Kunden. Standardwert: `"no-company"`.
  - `contactStatus` (string): Der Kontaktstatus des Kunden. Standardwert: `"no-contactStatus"`.
- **Optionale Eigenschaften**: Das gesamte Schema ist optional (`optional()`), d. h., es kann auch `undefined` sein.

## `Customer`
- **Beschreibung**: Ein TypeScript-Typ, der aus dem `customerSchema` abgeleitet wird. Er repräsentiert die Struktur eines Kundenobjekts.
- **Eigenschaften**:
  - `id` (string): Eindeutige Identifikationsnummer des Kunden.
  - `name` (string): Der Name des Kunden.
  - `surname` (string): Der Nachname des Kunden.
  - `email` (string): Die E-Mail-Adresse des Kunden.
  - `address` (string): Die Adresse des Kunden.
  - `phoneNumber` (string): Die Telefonnummer des Kunden.
  - `company` (string): Die Firma des Kunden.
  - `contactStatus` (string): Der Kontaktstatus des Kunden.
- **Optionale Eigenschaften**: Der Typ `Customer` kann auch `undefined` sein, da das Schema optional ist.

## Zusammenfassung
- Das `customerSchema` wird verwendet, um Kundenobjekte zu validieren und sicherzustellen, dass sie die erforderliche Struktur und Datentypen haben.
- Der Typ `Customer` wird aus dem Schema abgeleitet und bietet TypeScript-Typsicherheit für Kundenobjekte in der Anwendung.
- Beide sind optional, d. h., sie können auch `undefined` sein, falls kein Kundenobjekt vorhanden ist.