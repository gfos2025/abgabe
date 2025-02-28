// +layout.ts
import { browser } from '$app/environment'; // Importiert die browser-Variable, um zu überprüfen, ob der Code im Browser läuft
import '$lib/i18n'; // Importiert die i18n-Initialisierung (wichtig für die Übersetzungen)
import { locale, waitLocale } from 'svelte-i18n'; // Importiert die locale-Funktion und waitLocale aus svelte-i18n
import type { LayoutLoad } from './$types'; // Importiert den LayoutLoad-Typ aus den automatisch generierten Typen

// Die load-Funktion wird beim Laden des Layouts ausgeführt
export const load: LayoutLoad = async () => {
	// Überprüft, ob der Code im Browser ausgeführt wird
	if (browser) {
		// Setzt die Sprache basierend auf der Browser-Sprache
		locale.set(window.navigator.language);
	}

	// Wartet darauf, dass die locale-Initialisierung abgeschlossen ist
	await waitLocale();
};