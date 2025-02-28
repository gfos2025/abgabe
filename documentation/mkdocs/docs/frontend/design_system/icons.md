# Dokumentation der verwendeten Icons

In diesem Projekt wurden hauptsächlich Icons aus zwei Bibliotheken verwendet: **Lucide-Svelte** und **Svelte-Radix**. Beide Bibliotheken bieten eine umfangreiche Sammlung von hochwertigen Icons, die einfach in Svelte-Komponenten integriert werden können. Hier ist eine Übersicht über die verwendeten Bibliotheken und deren Icons:

---

## **Lucide-Svelte**
Lucide ist eine moderne Icon-Bibliothek, die sich durch klare, minimalistische und gut erkennbare Icons auszeichnet. Die **Lucide-Svelte**-Bibliothek stellt diese Icons als Svelte-Komponenten bereit.

### **Verwendung**
Lucide-Icons können einfach als Svelte-Komponenten importiert und verwendet werden. Beispiel:

```svelte
<script>
  import { Home, User, Settings } from 'lucide-svelte';
</script>

<Home size={24} />
<User size={24} />
<Settings size={24} />
```

### **Häufig verwendete Icons**
- **Home**: Wird für Navigationslinks zur Startseite verwendet.
- **User**: Repräsentiert Benutzerprofile oder Account-Einstellungen.
- **Settings**: Wird für Einstellungsseiten oder Konfigurationsoptionen verwendet.
- **Check**: Zeigt erfolgreiche Aktionen oder Bestätigungen an.
- **X**: Wird für Schließen-Buttons oder Fehlermeldungen verwendet.
- **Search**: Repräsentiert Suchfunktionen.
- **ArrowLeft/ArrowRight**: Wird für Navigation oder Paginierung verwendet.
- **Plus**: Zeigt das Hinzufügen von Elementen an.
- **Trash**: Wird für Löschaktionen verwendet.

### **Vorteile**
- **Minimalistisches Design**: Klare und gut erkennbare Icons.
- **Einfache Integration**: Direkte Verwendung als Svelte-Komponenten.
- **Anpassbar**: Größe und Farbe können über Props angepasst werden.

---

## **Svelte-Radix**
Svelte-Radix ist eine Sammlung von UI-Komponenten und Icons, die auf dem Radix Design System basieren. Die Icons sind besonders für komplexe Benutzeroberflächen geeignet.

### **Verwendung**
Svelte-Radix-Icons werden ebenfalls als Svelte-Komponenten importiert. Beispiel:

```svelte
<script>
  import { Calendar, Clock, Info } from 'svelte-radix';
</script>

<Calendar size={24} />
<Clock size={24} />
<Info size={24} />
```

### **Häufig verwendete Icons**
- **Calendar**: Wird für Datumsauswahl oder Terminplanung verwendet.
- **Clock**: Repräsentiert Zeitangaben oder Timer.
- **Info**: Zeigt zusätzliche Informationen oder Tooltips an.
- **ChevronDown/ChevronUp**: Wird für Dropdown-Menüs oder Akkordeons verwendet.
- **Eye/EyeClosed**: Repräsentiert Sichtbarkeitstoggles (z. B. für Passwörter).
- **CheckCircle**: Zeigt erfolgreiche Aktionen oder Bestätigungen an.
- **CrossCircled**: Wird für Fehlermeldungen oder ungültige Eingaben verwendet.

### **Vorteile**
- **Konsistenz**: Icons sind Teil des Radix Design Systems und passen gut zu anderen UI-Komponenten.
- **Flexibilität**: Größe und Farbe können einfach angepasst werden.
- **Zusätzliche UI-Komponenten**: Neben Icons bietet Svelte-Radix auch andere nützliche Komponenten wie Dropdowns, Modals und Tooltips.

---

## **Zusammenfassung**
- **Lucide-Svelte**: Wurde für allgemeine, häufig verwendete Icons wie Home, User und Settings verwendet. Die Icons sind minimalistisch und einfach zu integrieren.
- **Svelte-Radix**: Wurde für spezifischere Icons wie Calendar, Clock und Info verwendet. Diese Icons sind besonders gut für komplexe Benutzeroberflächen geeignet.

Beide Bibliotheken bieten hochwertige Icons, die einfach in Svelte-Komponenten integriert werden können. Durch die Kombination beider Bibliotheken konnte eine konsistente und ansprechende Benutzeroberfläche geschaffen werden.