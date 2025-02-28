<script lang="ts">
	// Importiere benötigte Komponenten und Funktionen
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente aus dem UI-Library
	import { t } from 'svelte-i18n'; // i18n-Funktion für Übersetzungen
</script>

<!-- Hauptcontainer für die Startseite -->
<div class="relative flex items-center justify-center min-h-screen bg-background overflow-hidden">
	<!-- Container für den zentralen Inhalt -->
	<div class="relative z-10 text-center mb-24">
		<!-- Überschrift der Startseite -->
		<h1 class="text-7xl md:text-9xl font-semibold">Worksale</h1>

		<!-- Beschreibungstext unter der Überschrift -->
		<p class="mt-4 text-lg text-foreground max-w-md mx-auto">
			{$t('start.desc')} <!-- Übersetzter Text für die Beschreibung -->
		</p>

		<!-- Container für die Buttons -->
		<div class="mt-6 space-x-4">
			<!-- Button zum Navigieren zur Login-Seite -->
			<Button variant="default" class="rounded-full" href="/login">
				{$t('start.getStarted')} <!-- Übersetzter Text für den Button -->
			</Button>

			<!-- Button zum Anzeigen von weiteren Informationen -->
			<Button variant="ghost" href="#">
				{$t('start.learnMore')} <!-- Übersetzter Text für den Button -->
			</Button>
		</div>
	</div>
</div>