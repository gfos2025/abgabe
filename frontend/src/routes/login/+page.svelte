<script lang="ts">
	// Importiere benötigte Komponenten und Funktionen
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import { Input } from '$lib/components/ui/input/index.js'; // Input-Komponente
	import Icon from '@iconify/svelte'; // Icon-Komponente für Icons
	import { t } from 'svelte-i18n'; // i18n-Funktion für Übersetzungen
	import { locale } from 'svelte-i18n'; // locale-Funktion zur Sprachsteuerung
	import { setMode } from "mode-watcher"; // Funktion zur Einstellung des Dark/Light-Modes

	// Zustandsvariablen
	let email: string = $state(''); // Speichert die E-Mail-Eingabe
	let password: string = $state(''); // Speichert das Passwort
	let showPassword: boolean = $state(false); // Steuert die Sichtbarkeit des Passworts
	let loginError: string | null = $state(null); // Speichert Fehlermeldungen beim Login
	let jSessionId: string | null = null; // Speichert die JSESSIONID nach erfolgreichem Login

	// Funktion zur Verarbeitung des Login-Formulars
	const handleSignIn = async (event: Event) => {
		event.preventDefault(); // Verhindert das Standardverhalten des Formulars
		loginError = null; // Setzt die Fehlermeldung zurück

		try {
			// Sendet eine Anfrage zum Login
			const res = await fetch(`/api/v1/auth/login`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({ email, password }), // Sendet E-Mail und Passwort
				credentials: 'include' // Berücksichtigt Cookies
			});

			// Überprüft, ob die Anfrage erfolgreich war
			if (!res.ok) {
				throw new Error('Login failed');
			}

			// Verarbeitet die Antwort bei erfolgreichem Login
			if (res.ok) {
				const data = await res.json();
				// Speichert Benutzerdaten im LocalStorage
				localStorage.setItem('id', data.id);
				localStorage.setItem('name', data.name);
				localStorage.setItem('surname', data.surname);
				localStorage.setItem('email', data.email);
				localStorage.setItem('role', data.role);

				// Extrahiert die JSESSIONID aus den Cookies
				const cookies = res.headers.get('set-cookie');
				if (cookies) {
					const jSessionIdMatch = cookies.match(/JSESSIONID=([^;]+)/);
					if (jSessionIdMatch) {
						jSessionId = jSessionIdMatch[1];
					}
				}

				// Lädt die Benutzereinstellungen
				await getSettings();

				// Leitet den Benutzer zum Dashboard weiter
				window.location.href = '/dashboard';
			} else {
				// Fehlermeldung bei falschen Anmeldedaten
				loginError = 'Wrong password or email';
			}
		} catch (error) {
			// Fehlerbehandlung bei Problemen während des Logins
			console.error('Error during sign-in:', error);
			loginError = 'An error occurred during login. Please try again.';
		}
	};

	// Funktion zum Abrufen der Benutzereinstellungen
	const getSettings = async () => {
		try {
			// Sendet eine Anfrage, um die Benutzereinstellungen abzurufen
			const response = await fetch(`/api/v1/users/settings/`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include' // Berücksichtigt Cookies
			});

			// Überprüft, ob die Anfrage erfolgreich war
			if (!response.ok) {
				throw new Error(`Error fetching customer data: ${response.status}`);
			}

			// Verarbeitet die Antwort
			const responseData = await response.json();

			// Setzt den Dark/Light-Mode basierend auf den Benutzereinstellungen
			if (responseData.darkMode == true) {
				setMode("dark");
			} else {
				setMode("light");
			}

			// Setzt die Sprache basierend auf den Benutzereinstellungen
			if (responseData.languageGerman == true) {
				locale.set("de-DE");
			} else {
				locale.set("en-US");
			}
		} catch (error) {
			// Fehlerbehandlung bei Problemen beim Abrufen der Einstellungen
			console.error('Error during fetching customer data:', error);
		}
	};
</script>

<!-- Hauptcontainer für das Login-Layout -->
<div class="flex flex-row space-x-24 items-center justify-center min-h-screen bg-transparent">
	<!-- Linke Seite: Login-Formular -->
	<div class="w-full md:w-1/3 h-screen bg-transparent flex flex-col items-center justify-center p-8">
		<!-- Logo-Button, der zur Startseite führt -->
		<Button variant="ghost" href="/" class="text-foreground text-4xl font-bold mb-2">
			Worksale
		</Button>

		<!-- Begrüßungstext -->
		<h2 class="text-foreground text-2xl font-semibold mb-2">{$t('login.welcomeBack')}</h2>
		<p class="text-foreground mb-6 text-center">{$t('login.signInDesc')}</p>

		<!-- Login-Formular -->
		<form onsubmit={handleSignIn} class="w-full">
			<!-- E-Mail-Eingabefeld -->
			<Input
				type="email"
				placeholder={$t('login.email')}
				bind:value={email}
				required
				class="w-full mb-4"
			/>

			<!-- Passwort-Eingabefeld mit Toggle für die Sichtbarkeit -->
			<div class="relative mb-4 w-full">
				<Input
					type={showPassword ? 'text' : 'password'}
					placeholder={$t('login.password')}
					bind:value={password}
					required
					class="w-full pr-12"
				/>
				<!-- Button zum Ein-/Ausblenden des Passworts -->
				<Button
					variant="link"
					type="button"
					on:click={() => (showPassword = !showPassword)}
					class="absolute right-2 top-0 text-accent p-1 group"
				>
					<Icon
						icon={showPassword ? 'mdi:eye' : 'mdi:eye-off'}
						class="group-hover:text-primary transition-colors duration-150"
						width="20"
						height="20"
					/>
				</Button>
			</div>

			<!-- Fehlermeldung, falls vorhanden -->
			{#if loginError}
				<p class="text-custom-red text-sm mb-4">{loginError}</p>
			{/if}

			<!-- Login-Button -->
			<Button type="submit" class="w-full mb-4">{$t('login.signIn')}</Button>
		</form>
	</div>

	<!-- Rechte Seite: Bild und Text -->
	<div class="hidden md:w-2/3 h-screen bg-transparent border-x border-border md:flex items-center justify-center relative">
		<!-- Hintergrundbild -->
		<img src="./LoginMockup.png" alt="Dashboard Preview" class="w-full h-full object-cover" />

		<!-- Text über dem Bild -->
		<div class="absolute top-8 left-8">
			<p class="font-bold text-primary text-3xl">{$t('login.salesWithoutBorders')}</p>
			<p class="mt-2 text-lg text-foreground">{$t('login.singleDashboard')}</p>
		</div>
	</div>
</div>