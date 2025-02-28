# Anfragen / Endpunkte

## Überblick

Dieses Verzeichnis enthält die Dokumentation für die verschiedenen API-Endpunkte, die im Backend verfügbar sind. Jede Datei beschreibt die spezifischen Anfragen, die für einen bestimmten Bereich der Anwendung relevant sind, wie z.B. Terminverwaltung, Benutzerauthentifizierung, Kundenverwaltung und mehr.

## Verfügbare Anfragen

- **[Termine](appointments.md)**: Dokumentation der API-Endpunkte für die Verwaltung von Terminen.
- **[Authentifizierung](auth.md)**: Beschreibung der Endpunkte für Benutzerauthentifizierung und -registrierung.
- **[Kunden](customers.md)**: API-Dokumentation für die Verwaltung von Kunden.
- **[Logs](logs.md)**: Endpunkte für die Verwaltung und Abfrage von Log-Daten.
- **[Transaktionen](transactions.md)**: Dokumentation der API-Endpunkte für die Verwaltung von Transaktionen.
- **[Benutzer](users.md)**: Beschreibung der Endpunkte für die Verwaltung von Benutzerdaten.

## Verwendung

Jede Datei in diesem Verzeichnis enthält detaillierte Informationen zu den verfügbaren Endpunkten, einschließlich der erforderlichen Parameter, möglicher Rückgabewerte sowie Beispielantworten. Bitte konsultieren Sie die entsprechende Datei für den spezifischen Bereich, den Sie benötigen.

## Globale Rückgabewerte
Das Backend verfügt über globale Exception-Handler, die sicherstellen, dass alle registrierten Routen konsistente Fehlermeldungen zurückgeben. Diese Handler fangen verschiedene Arten von Fehlern ab die in den Anfragen auftreten können und geben entsprechende Fehlerantworten zurück, welche ein standardisiertes Format haben.

```typescript
interface JsonBodyErrorResponse {
  title: string; // Fehlermeldung
  status: number; // HTTP-Statuscode
  type: string; // Link zur Dokumentation des HTTP-Statuscodes
  details: Record<any, any>;
}
```

### IllegalArgumentException / MismatchedInputException / JsonParseException
Fängt Fehler ab, die durch ungültige Anfrageparameter oder einen ungültigen Request-Body verursacht werden, wie z.B. ungültige IDs. Der Handler gibt einen 500 Internal Server Error zurück.


### ServiceException
Fängt Jederlei Fehler ab, der in den Services auftritt. Der Handler gibt einen 500 Internal Server Error zurück.