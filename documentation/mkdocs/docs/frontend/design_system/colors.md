# Dokumentation des Designsystems

In diesem Projekt wurde das **Geist Design System** von Vercel in Kombination mit **shadcn/ui** und **Tailwind CSS** verwendet. Die Farben und Design-Tokens wurden über Tailwind CSS konfiguriert, um eine konsistente und anpassbare Benutzeroberfläche zu schaffen. Hier ist eine Übersicht über die verwendeten Farben und Konfigurationen:

## **Farben**

### **Geist Design System**
- **Verwendete Farbe**: 
  - **Farbe 7 (High Contrast Background)**: Diese Farbe wurde für Hintergründe mit hohem Kontrast verwendet, um eine klare Trennung zwischen Elementen zu schaffen und die Lesbarkeit zu verbessern.

### **shadcn/ui mit Tailwind CSS**
Die Farben wurden über CSS-Variablen in der `:root`- und `.dark`-Klasse definiert. Diese Variablen werden in Tailwind CSS verwendet, um ein konsistentes Design für Light- und Dark-Mode zu gewährleisten.

#### **Light Mode (`:root`)**
- **Hintergrund und Text**:
  - `--background`: `0 0% 100%` (Weiß)
  - `--foreground`: `0 0% 3.9%` (Fast Schwarz)
- **Primärfarben**:
  - `--primary`: `241 61% 59%` (Indigo)
  - `--primary-foreground`: `0 0% 98%` (Hellgrau)
- **Sekundärfarben**:
  - `--secondary`: `0 0% 96.1%` (Sehr helles Grau)
  - `--secondary-foreground`: `0 0% 9%` (Dunkelgrau)
- **Akzentfarben**:
  - `--accent`: `0 0% 96.1%` (Sehr helles Grau)
  - `--accent-foreground`: `0 0% 9%` (Dunkelgrau)
- **Destruktive Aktionen**:
  - `--destructive`: `0 72.22% 50.59%` (Rot)
  - `--destructive-foreground`: `0 0% 98%` (Hellgrau)
- **Rahmen und Eingabefelder**:
  - `--border`: `0 0% 89.8%` (Hellgrau)
  - `--input`: `0 0% 89.8%` (Hellgrau)
- **Benutzerdefinierte Farben**:
  - `--custom-red`, `--custom-orange`, `--custom-yellow`, `--custom-green`, `--custom-teal`, `--custom-blue`, `--custom-indigo`, `--custom-purple`, `--custom-pink`, `--custom-gray`: Diese Farben wurden für spezifische UI-Elemente wie Badges, Buttons oder Icons verwendet.

#### **Dark Mode (`.dark`)**
- **Hintergrund und Text**:
  - `--background`: `210 6% 6%` (Sehr dunkel)
  - `--foreground`: `40 10% 89%` (Hellgrau)
- **Primärfarben**:
  - `--primary`: `241 73% 63%` (Hellindigo)
  - `--primary-foreground`: `40 10% 89%` (Hellgrau)
- **Sekundärfarben**:
  - `--secondary`: `240 3% 11%` (Dunkelgrau)
  - `--secondary-foreground`: `36 4% 56%` (Mittelgrau)
- **Akzentfarben**:
  - `--accent`: `0 0% 14.9%` (Dunkelgrau)
  - `--accent-foreground`: `0 0% 98%` (Hellgrau)
- **Destruktive Aktionen**:
  - `--destructive`: `0 62.8% 30.6%` (Dunkelrot)
  - `--destructive-foreground`: `0 0% 98%` (Hellgrau)
- **Rahmen und Eingabefelder**:
  - `--border`: `0 0% 14.9%` (Dunkelgrau)
  - `--input`: `0 0% 14.9%` (Dunkelgrau)
- **Benutzerdefinierte Farben**:
  - Die benutzerdefinierten Farben wurden ebenfalls für den Dark-Mode angepasst, um eine konsistente Benutzererfahrung zu gewährleisten.

### **Sidebar-Farben**
Die Sidebar verwendet separate Farbvariablen, um eine klare Abgrenzung zum Hauptinhalt zu schaffen.

#### **Light Mode**
- `--sidebar-background`: `0 0% 98%` (Sehr helles Grau)
- `--sidebar-foreground`: `240 5.3% 26.1%` (Dunkelgrau)
- `--sidebar-primary`: `240 5.9% 10%` (Sehr dunkel)
- `--sidebar-primary-foreground`: `0 0% 98%` (Hellgrau)
- `--sidebar-accent`: `240 4.8% 95.9%` (Sehr helles Grau)
- `--sidebar-accent-foreground`: `240 5.9% 10%` (Sehr dunkel)
- `--sidebar-border`: `220 13% 91%` (Hellgrau)

#### **Dark Mode**
- `--sidebar-background`: `240 5.9% 10%` (Sehr dunkel)
- `--sidebar-foreground`: `240 4.8% 95.9%` (Hellgrau)
- `--sidebar-primary`: `224.3 76.3% 48%` (Blau)
- `--sidebar-primary-foreground`: `0 0% 100%` (Weiß)
- `--sidebar-accent`: `240 3.7% 15.9%` (Dunkelgrau)
- `--sidebar-accent-foreground`: `240 4.8% 95.9%` (Hellgrau)
- `--sidebar-border`: `240 3.7% 15.9%` (Dunkelgrau)

## **Tailwind CSS-Konfiguration**
- **Basiseinstellungen**:
  - `@tailwind base`, `@tailwind components`, `@tailwind utilities`: Diese Direktiven importieren die Standard-Tailwind-Klassen.
  - `@layer base`: Definiert globale Stile für alle Elemente (`*`) und den `body`.
    - `border-border`: Wendet die `--border`-Farbe auf alle Rahmen an.
    - `bg-background text-foreground`: Setzt den Hintergrund und die Textfarbe des `body` basierend auf den CSS-Variablen.

## **Zusammenfassung**
- Das Designsystem kombiniert das **Geist Design System** von Vercel mit **shadcn/ui** und **Tailwind CSS**.
- Die Farben wurden über CSS-Variablen definiert, um eine konsistente Darstellung im Light- und Dark-Mode zu gewährleisten.
- Benutzerdefinierte Farben und Sidebar-spezifische Variablen ermöglichen eine flexible Anpassung der Benutzeroberfläche.
- Tailwind CSS wurde verwendet, um die Stile effizient und modular zu verwalten.

Diese Konfiguration sorgt für eine moderne, anpassbare und benutzerfreundliche Benutzeroberfläche.