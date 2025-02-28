<script lang="ts">
	// Importiere globale Styles und benötigte Komponenten
	import '../app.css'; // Globale CSS-Datei
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import UserNav from '$lib/components/ui/usernav/user-nav.svelte'; // Benutzer-Navigationskomponente
	import { ModeWatcher } from 'mode-watcher'; // Komponente zur Verwaltung des Dark/Light-Modes
	import { Toaster } from '$lib/components/ui/sonner/index.js'; // Toaster-Komponente für Benachrichtigungen
	import { onMount } from 'svelte'; // Lifecycle-Hook für das Mounting
	import { t } from 'svelte-i18n'; // i18n-Funktion für Übersetzungen

	// Zustandsvariable, um den Login-Status zu speichern
	let isLoggedIn: boolean = $state(false);

	// Funktion, um zu überprüfen, ob ein bestimmter Cookie vorhanden ist
	function isCookiePresent(name: string): boolean {
		return document.cookie.split(';').some((cookie) => cookie.trim().startsWith(`${name}=`));
	}

	// Props für die Kinderkomponenten
	let { children } = $props();

	// Lifecycle-Hook, der nach dem Mounting der Komponente ausgeführt wird
	onMount(() => {
		// Überprüft, ob der Benutzer eingeloggt ist, basierend auf dem Vorhandensein eines Cookies
		isLoggedIn = isCookiePresent('JSESSIONID');

		// Setzt den Dark/Light-Mode basierend auf dem gespeicherten Wert im LocalStorage
		if (localStorage.getItem('mode') === 'dark') {
			document.documentElement.classList.add('dark');
		} else {
			document.documentElement.classList.remove('light');
		}
	});
</script>

<!-- Hauptcontainer für das Layout -->
<div class="min-h-screen absolute w-full flex flex-col bg-background text-foreground overflow-x-hidden">
	<!-- ModeWatcher: Verwaltet den Dark/Light-Mode -->
	<ModeWatcher defaultMode={'dark'} />

	<!-- Toaster: Zeigt Benachrichtigungen an -->
	<Toaster />

	<!-- Navigationsleiste -->
	<nav class="bg-background border-b border-border">
		<div class="lg:container lg:mx-auto mr-4 flex justify-between items-center py-4">
			<!-- Logo-Button, der zur Startseite führt -->
			<Button variant="ghost" href="/" class="text-2xl font-bold">Worksale</Button>

			<!-- Container für die Navigationslinks und Benutzeraktionen -->
			<div class="flex items-center space-x-4">
				<!-- Button zur Startseite -->
				<Button variant="ghost" href="/">{$t('layout.home')}</Button>

				<!-- Wenn der Benutzer eingeloggt ist -->
				{#if isLoggedIn}
					<!-- Button zum Dashboard -->
					<Button variant="ghost" href="/dashboard">{$t('layout.dashboard')}</Button>

					<!-- Benutzer-Navigationskomponente -->
					<UserNav />
				{:else}
					<!-- Login-Button, wenn der Benutzer nicht eingeloggt ist -->
					<Button variant="default" href="/login">{$t('layout.login')}</Button>
				{/if}
			</div>
		</div>
	</nav>

	<!-- Hauptinhalt der Seite -->
	<main class="flex-grow container mx-auto overflow-hidden">
		<!-- Rendert die Kinderkomponenten -->
		{@render children()}
	</main>

	<!-- Footer -->
	<footer class="bg-background border-t border-border text-center py-4">
		<!-- Copyright-Hinweis -->
		<p class="text-sm text-foreground">© 2025 Worksale. {$t('layout.rights')}</p>
	</footer>
</div>