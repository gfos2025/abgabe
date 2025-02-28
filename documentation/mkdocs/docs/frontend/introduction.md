# Frontend-Einführung

Dieses Kapitel bietet eine kurze Einführung in die Frontend-Struktur des Worksale-Projekts.

## Technologien

Das Frontend wird mit den folgenden Technologien erstellt:

- **SvelteKit**: Eine moderne, reaktive Framework für Single-Page-Anwendungen.
- **TailwindCSS**: Ein Utility-First CSS-Framework für schnelle Entwicklung.
- **Vite**: Ein Build-Tool, das schnelle Entwicklung und optimierte Builds ermöglicht.
- **ESLint**: Ein statisches Codeanalyse-Tool zur Erkennung von Fehlern und Stilverletzungen.

## Projektstruktur

Die Hauptordner im Frontend-Projekt sind wie folgt aufgebaut:

```
src/
├── lib/
│   ├── i18n/
│   ├── utils.ts
│   ├── types/
│   ├── hooks/
│   ├── components/
├── routes/
├── hooks.server.ts
└── app.d.ts
```

### Lib Ordner

Der `lib`-Ordner enthält verschiedene Unterordner:

- **i18n**: Dateien für Mehrsprachigkeit.
- **utils.ts**: Hilfsfunktionen.
- **types**: Schnittstellen und Enums.
- **hooks**: React-Hooks.
- **components**: UI-Komponenten.

### Routes Ordner

Der `routes`-Ordner enthält die verschiedenen Routen der Anwendung, z.B. Dashboard, Kalender, Kunden, Transaktionen, etc.

### Weitere Dateien

- **hooks.server.ts**: Serverseitige Hooks.
- **app.d.ts**: Typdefinitionen.
- **browsertesting.ts**: Testdaten für Browser-Szenarien.