# Dokumentation der Login-Seite

Die Login-Seite ermöglicht es Benutzern, sich in die Anwendung einzuloggen. Sie bietet eine benutzerfreundliche Oberfläche zur Eingabe von E-Mail und Passwort, unterstützt die Anzeige des Passworts und zeigt Fehlermeldungen bei fehlgeschlagenen Anmeldeversuchen an. Zusätzlich enthält die Seite eine Vorschau des Dashboards, um Benutzer zu motivieren und einen Einblick in die Anwendung zu geben.

## Zweck der Seite

Die Login-Seite hat folgende Hauptziele:

1. **Benutzerauthentifizierung**:
    - Ermöglicht Benutzern die Anmeldung mit ihrer E-Mail und ihrem Passwort.
    - Überprüft die Anmeldeinformationen und leitet den Benutzer bei Erfolg zum Dashboard weiter.

2. **Benutzerfreundlichkeit**:
    - Bietet eine klare und intuitive Benutzeroberfläche.
    - Unterstützt die Anzeige des Passworts, um die Eingabe zu erleichtern.

3. **Fehlerbehandlung**:
    - Zeigt Fehlermeldungen an, wenn die Anmeldung fehlschlägt (z. B. falsche E-Mail oder Passwort).

4. **Internationalisierung**:
    - Unterstützt mehrere Sprachen durch die Verwendung von `svelte-i18n`.

5. **Visuelle Motivation**:
    - Zeigt eine Vorschau des Dashboards, um Benutzer zu motivieren und einen Einblick in die Anwendung zu geben.

## UI/UX-Überlegungen

### 1. **Design und Layout**
- **Zweispaltiges Layout**:
    - Die Seite ist in zwei Spalten unterteilt:
        - **Linke Spalte**: Enthält das Login-Formular.
        - **Rechte Spalte**: Zeigt eine Vorschau des Dashboards (nur auf größeren Bildschirmen sichtbar).
    - Dieses Layout schafft eine klare Trennung zwischen Funktionalität und visueller Darstellung.

- **Hintergrund**:
    - Der Hintergrund ist transparent (`bg-transparent`), um den Fokus auf das Formular und die Vorschau zu lenken.

### 2. **Login-Formular**
- **Eingabefelder**:
    - **E-Mail**: Ein Eingabefeld für die E-Mail-Adresse des Benutzers.
    - **Passwort**: Ein Eingabefeld für das Passwort mit der Möglichkeit, das Passwort anzuzeigen oder zu verbergen.

- **Passwort-Anzeige**:
    - Ein Button mit einem Augensymbol (`Icon`) ermöglicht es Benutzern, das Passwort anzuzeigen oder zu verbergen.
    - Dies verbessert die Benutzerfreundlichkeit, insbesondere auf mobilen Geräten.

- **Fehlermeldungen**:
    - Bei fehlgeschlagenen Anmeldeversuchen wird eine Fehlermeldung (`loginError`) angezeigt, um den Benutzer zu informieren.

- **Call-to-Action (CTA)**:
    - Der "Sign In"-Button ist prominent platziert und fordert den Benutzer zur Anmeldung auf.

### 3. **Dashboard-Vorschau**
- **Bild**:
    - Ein Bild (`LoginMockup.png`) zeigt eine Vorschau des Dashboards, um Benutzer zu motivieren und einen Einblick in die Anwendung zu geben.
    - Das Bild ist responsiv und passt sich der Bildschirmgröße an (`w-full h-full object-cover`).

- **Text**:
    - Ein kurzer Text ("Sales Without Borders" und "Single Dashboard") beschreibt die Vorteile der Anwendung und motiviert den Benutzer zur Anmeldung.

### 4. **Internationalisierung**
- **Dynamische Texte**:
    - Alle Texte werden über `svelte-i18n` geladen (`$t('login.welcomeBack')`, `$t('login.signInDesc')`, `$t('login.email')`, `$t('login.password')`, `$t('login.signIn')`, `$t('login.salesWithoutBorders')`, `$t('login.singleDashboard')`).
    - Dies ermöglicht eine einfache Anpassung der Texte für verschiedene Sprachen.

### 5. **Dunkler Modus und Spracheinstellungen**
- **Automatische Anpassung**:
    - Beim erfolgreichen Login werden die Benutzereinstellungen (dunkler Modus und Sprache) vom Server abgerufen und angewendet (`getSettings`).
    - Dies gewährleistet eine konsistente Benutzererfahrung.

## Zusammenfassung

Die Login-Seite bietet eine benutzerfreundliche und intuitive Oberfläche zur Benutzerauthentifizierung. Sie unterstützt die Anzeige des Passworts, zeigt Fehlermeldungen an und motiviert Benutzer durch eine Vorschau des Dashboards. Die Internationalisierung und die automatische Anpassung von Dunkelmodus und Spracheinstellungen gewährleisten eine konsistente und ansprechende Benutzererfahrung.