<script lang="ts">
  // Importe von Funktionen und UI-Komponenten
  import { resetMode, setMode } from "mode-watcher"; // Funktionen zur Steuerung des Farbmodus
  import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js"; // Dropdown-Menü-Komponente
  import { Button } from "$lib/components/ui/button/index.js"; // Button-Komponente
  import * as Card from "$lib/components/ui/card/index.js"; // Card-Komponente
  import { Input } from "$lib/components/ui/input/index.js"; // Input-Komponente
  import { locale } from 'svelte-i18n'; // Internationalisierungsfunktion für die Sprache
  import { t } from 'svelte-i18n'; // Internationalisierungsfunktion für Übersetzungen

  // Zustandsvariablen
  let darkMode: boolean = $state(false); // Zustand für den Dunkelmodus
  let languageGerman: boolean = $state(false); // Zustand für die Sprache (Deutsch/Englisch)

  // Effekt: Aktualisiert die Einstellungen, wenn sich der Dunkelmodus oder die Sprache ändert
  $effect(() => {
    updateSettings(darkMode, languageGerman);
  });

  // Funktion zum Aktualisieren der Benutzereinstellungen
  const updateSettings = async (darkMode: boolean, languageGerman: boolean) => {
    try {
      const response = await fetch('/api/v1/users/settings', {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          darkMode,
          languageGerman,
        }),
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to update settings: ${response.status}`);
      }

      const responseData = await response.json();
      console.log('Settings updated successfully:', responseData);
      return responseData;
    } catch (error) {
      console.error('Error updating settings:', error);
      throw error;
    }
  };
</script>

<!-- UI-Struktur -->
<div class="flex min-h-screen w-full flex-col">
  <main class="bg-transparent flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
    <!-- Titelbereich -->
    <div class="mx-auto grid w-full max-w-6xl gap-2">
      <h1 class="text-3xl font-semibold">{$t('settings.title')}</h1>
    </div>

    <!-- Hauptbereich mit Navigationsleiste und Einstellungen -->
    <div class="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
      <!-- Navigationsleiste -->
      <nav class="text-muted-foreground grid gap-4 text-sm">
        <a href="#security">{$t('settings.security')}</a>
        <a href="#appearance">{$t('settings.appearance')}</a>
        <a href="#language">{$t('settings.language')}</a>
      </nav>

      <!-- Einstellungsbereich -->
      <div class="grid gap-6">
        <!-- Sicherheitseinstellungen: Passwort ändern -->
        <div id="security">
          <Card.Root>
            <Card.Header>
              <Card.Title>{$t('settings.changePassword')}</Card.Title>
              <Card.Description>{$t('settings.changePasswordDesc')}</Card.Description>
            </Card.Header>
            <Card.Content>
              <form class="flex flex-col gap-4">
                <Input type="password" placeholder={$t('settings.newPassword')} />
                <Input type="password" placeholder={$t('settings.confirmNewPassword')} />
              </form>
            </Card.Content>
            <Card.Footer class="border-t px-6 py-4">
              <Button>{$t('settings.changePassword')}</Button>
            </Card.Footer>
          </Card.Root>
        </div>

        <!-- Sicherheitseinstellungen: E-Mail ändern -->
        <div id="security">
          <Card.Root>
            <Card.Header>
              <Card.Title>{$t('settings.changeEmail')}</Card.Title>
              <Card.Description>{$t('settings.changeEmailDesc')}</Card.Description>
            </Card.Header>
            <Card.Content>
              <form class="flex flex-col gap-4">
                <Input type="email" placeholder={$t('settings.newEmail')} />
                <Input type="email" placeholder={$t('settings.confirmNewEmail')} />
              </form>
            </Card.Content>
            <Card.Footer class="border-t px-6 py-4">
              <Button>{$t('settings.updateEmail')}</Button>
            </Card.Footer>
          </Card.Root>
        </div>

        <!-- Darstellungseinstellungen: Theme ändern -->
        <div id="appearance">
          <Card.Root>
            <Card.Header>
              <Card.Title>{$t('settings.themeSettings')}</Card.Title>
              <Card.Description>{$t('settings.themeSettingsDesc')}</Card.Description>
            </Card.Header>
            <Card.Content>
              <DropdownMenu.Root>
                <DropdownMenu.Trigger class="px-4 py-2 border border-border rounded-md text-sm bg-background">
                  {$t('settings.selectTheme')}
                </DropdownMenu.Trigger>
                <DropdownMenu.Content align="start" class="bg-background rounded-md shadow-lg w-40">
                  <!-- Option für den Hellmodus -->
                  <DropdownMenu.Item onclick={() => {
                    setMode("light");
                    darkMode = false;
                  }}>
                    {$t('settings.lightTheme')}
                  </DropdownMenu.Item>
                  <!-- Option für den Dunkelmodus -->
                  <DropdownMenu.Item onclick={() => {
                    setMode("dark");
                    darkMode = true;
                  }}>
                    {$t('settings.darkTheme')}
                  </DropdownMenu.Item>
                  <!-- Option für den Systemstandard -->
                  <DropdownMenu.Item onclick={() => resetMode()}>
                    {$t('settings.systemDefault')}
                  </DropdownMenu.Item>
                </DropdownMenu.Content>
              </DropdownMenu.Root>
            </Card.Content>
          </Card.Root>
        </div>

        <!-- Spracheinstellungen: Sprache ändern -->
        <div id="language">
          <Card.Root>
            <Card.Header>
              <Card.Title>{$t('settings.languageSettings')}</Card.Title>
              <Card.Description>{$t('settings.languageSettingsDesc')}</Card.Description>
            </Card.Header>
            <Card.Content>
              <DropdownMenu.Root>
                <DropdownMenu.Trigger class="px-4 py-2 border border-border rounded-md text-sm bg-background">
                  {$t('settings.selectLanguage')}
                </DropdownMenu.Trigger>
                <DropdownMenu.Content align="start" class="bg-background rounded-md shadow-lg w-40">
                  <!-- Option für Englisch -->
                  <DropdownMenu.Item onclick={() => {
                    locale.set('en-US');
                    languageGerman = false;
                  }}>
                    {$t('settings.english')}
                  </DropdownMenu.Item>
                  <!-- Option für Deutsch -->
                  <DropdownMenu.Item onclick={() => {
                    locale.set('de-DE');
                    languageGerman = true;
                  }}>
                    {$t('settings.german')}
                  </DropdownMenu.Item>
                </DropdownMenu.Content>
              </DropdownMenu.Root>
            </Card.Content>
          </Card.Root>
        </div>
      </div>
    </div>
  </main>
</div>