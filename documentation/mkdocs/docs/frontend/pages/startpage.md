# Dokumentation der Startseite

Diese Dokumentation beschreibt die Startseite der Anwendung, ihren Zweck sowie die Überlegungen zur Benutzeroberfläche (UI) und Benutzererfahrung (UX).

## Zweck der Seite

Die Startseite dient als **Einstiegspunkt** für Benutzer in die Anwendung. Sie hat folgende Hauptziele:

1. **Erster Eindruck**: Die Seite soll einen positiven und professionellen ersten Eindruck der Anwendung vermitteln.
2. **Navigation**: Sie bietet klare Handlungsaufforderungen (Call-to-Actions, CTAs), um Benutzer zu weiteren Funktionen der Anwendung zu leiten.
3. **Informationsvermittlung**: Kurze, prägnante Informationen über die Anwendung werden bereitgestellt, um Neugier zu wecken und Interesse zu fördern.
4. **Internationalisierung**: Die Seite unterstützt mehrere Sprachen durch die Verwendung von `svelte-i18n`, um eine globale Zielgruppe anzusprechen.

## UI/UX-Überlegungen

### 1. **Design und Layout**
- **Zentrierung und Fokus**: 
  - Die Inhalte sind zentriert, um den Fokus auf die wichtigsten Elemente zu lenken: den Anwendungsnamen (`Worksale`) und die Beschreibung.
  - Die Verwendung von `min-h-screen` stellt sicher, dass die Seite die gesamte Höhe des Bildschirms einnimmt, was ein modernes und ansprechendes Design fördern soll.

### 2. **Typografie**
- **Schriftgrößen**:
  - Der Anwendungsname (`Worksale`) verwendet eine große Schrift (`text-7xl` bzw. `text-9xl` auf größeren Bildschirmen), um Aufmerksamkeit zu erregen und Markenbekanntheit zu fördern.
  - Die Beschreibung (`text-lg`) ist kleiner, aber gut lesbar, um eine klare Hierarchie zu schaffen.

- **Schriftstärke**:
  - Die Verwendung von `font-semibold` für den Anwendungsnamen betont dessen Bedeutung, während die Beschreibung in normaler Schriftstärke gehalten ist, um Lesbarkeit zu gewährleisten.

### 3. **Interaktive Elemente**
- **Buttons**:
  - **Primärer Button** (`Get Started`):
    - Der Button hat ein auffälliges Design (`variant="default"`) und ist abgerundet (`rounded-full`), um Benutzer zur Interaktion zu ermutigen.
    - Er führt Benutzer zur Login-Seite, um den Einstieg in die Anwendung zu erleichtern.
  - **Sekundärer Button** (`Learn More`):
    - Dieser Button hat ein dezenteres Design (`variant="ghost"`), um eine alternative Option zu bieten, ohne vom primären Call-to-Action abzulenken.
    - Er könnte zu einer Informationsseite oder einer Produktdokumentation führen.

### 4. **Internationalisierung**
- **Dynamische Texte**:
  - Alle Texte werden über `svelte-i18n` geladen (`$t('start.desc')`, `$t('start.getStarted')`, `$t('start.learnMore')`), um die Seite für verschiedene Sprachen und Regionen anpassbar zu machen.
  - Dies ermöglicht eine einfache Skalierung der Anwendung für internationale Märkte.

### 5. **Responsive Design**
- **Anpassung an verschiedene Bildschirmgrößen**:
  - Die Schriftgrößen (`text-7xl` auf Mobilgeräten und `text-9xl` auf Desktop-Geräten) passen sich dynamisch an, um eine konsistente Benutzererfahrung auf allen Geräten zu gewährleisten.
  - Die Beschreibung ist auf eine maximale Breite (`max-w-md`) begrenzt, um die Lesbarkeit auf großen Bildschirmen zu verbessern.

## Zusammenfassung

Die Startseite ist darauf ausgelegt, Benutzer effektiv zu begrüßen und sie durch klare Handlungsaufforderungen in die Anwendung zu führen. Das Design ist modern, minimalistisch und benutzerfreundlich, während die Internationalisierung eine globale Reichweite ermöglicht. Die Seite bietet eine solide Grundlage für die weitere Interaktion mit der Anwendung.