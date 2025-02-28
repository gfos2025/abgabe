<script lang="ts">
	// Importiere benötigte Komponenten und Funktionen
	import SidebarNav from '$lib/components/ui/sidebar-nav/sidebar-nav.svelte'; // Sidebar-Navigationskomponente
	import MobileNav from '$lib/components/ui/mobile-nav/mobile-nav.svelte'; // Mobile-Navigationskomponente
	import { onMount } from 'svelte'; // Lifecycle-Hook für das Mounting
	import { browser } from '$app/environment'; // Für SSR-Kompatibilität

	// Zustandsvariablen
	let userRole: string = $state(''); // Speichert die Rolle des Benutzers (z. B. ADMIN oder EMPLOYEE)
	let sidebarNavItems = $state([ // Liste der Navigationspunkte für die Sidebar
		{
			title: 'dashboard.title',
			href: '/dashboard',
			icon: 'chart-bar'
		},
		{
			title: 'calendar.title',
			href: '/dashboard/calendar',
			icon: 'calendar'
		},
		{
			title: 'customers.title',
			href: '/dashboard/customers',
			icon: 'users'
		},
		{
			title: 'transactions.title',
			href: '/dashboard/transactions',
			icon: 'arrow-left-right'
		},
		{
			title: 'settings.title',
			href: '/dashboard/settings',
			icon: 'settings'
		}
	]);

	// Admin-spezifische Navigationspunkte
	const adminNavItems = [
		{
			title: 'users.title',
			href: '/dashboard/users',
			icon: 'user-search'
		},
		{
			title: 'logs.title',
			href: '/dashboard/logs',
			icon: 'bell'
		}
	];

	// Zustandsvariable für die Bildschirmgröße
	let isMobile = $state(false);

	// Funktion, um zu überprüfen, ob ein bestimmter Cookie vorhanden ist
	function isCookiePresent(name: string): boolean {
		return document.cookie.split(';').some((cookie) => cookie.trim().startsWith(`${name}=`));
	}

	// Funktion, um die Bildschirmgröße zu überprüfen
	function checkScreenSize() {
		isMobile = window.innerWidth < 1024; // Setzt isMobile auf true, wenn die Bildschirmbreite kleiner als 1024px ist
	}

	// Funktion, um die Navigationspunkte basierend auf der Benutzerrolle zu aktualisieren
	function updateNavItems() {
		// Basis-Navigationspunkte
		let navItems = [
			{
				title: 'dashboard.title',
				href: '/dashboard',
				icon: 'chart-bar'
			},
			{
				title: 'calendar.title',
				href: '/dashboard/calendar',
				icon: 'calendar'
			},
			{
				title: 'customers.title',
				href: '/dashboard/customers',
				icon: 'users'
			},
			{
				title: 'transactions.title',
				href: '/dashboard/transactions',
				icon: 'arrow-left-right'
			},
			{
				title: 'settings.title',
				href: '/dashboard/settings',
				icon: 'settings'
			}
		];

		// Fügt Admin-spezifische Navigationspunkte hinzu, wenn der Benutzer ein Admin ist
		if (userRole === 'ADMIN') {
			navItems = [...navItems, ...adminNavItems];
		}

		// Aktualisiert die Sidebar-Navigationspunkte
		sidebarNavItems = navItems;
	}

	// Lifecycle-Hook, der nach dem Mounting der Komponente ausgeführt wird
	onMount(() => {
		if (browser) {
			// Überprüft, ob der Benutzer eingeloggt ist (basierend auf dem Vorhandensein eines Cookies)
			if (!isCookiePresent('JSESSIONID')) {
				window.location.href = '/login'; // Leitet den Benutzer zur Login-Seite weiter, wenn kein Cookie vorhanden ist
			}

			// Holt die Benutzerrolle aus dem LocalStorage
			userRole = localStorage.getItem('role') || '';

			// Aktualisiert die Navigationspunkte basierend auf der Benutzerrolle
			updateNavItems();

			// Setzt den initialen Wert für die Bildschirmgröße
			checkScreenSize();

			// Fügt einen Event-Listener für das Resize-Event hinzu
			window.addEventListener('resize', checkScreenSize);

			// Cleanup-Funktion, um den Event-Listener zu entfernen
			return () => {
				window.removeEventListener('resize', checkScreenSize);
			};
		}
	});

	// Debugging: Gibt den Wert von isMobile in der Konsole aus
	$effect(() => {
		console.log(isMobile);
	});
</script>

<!-- Hauptcontainer für das Layout -->
<div class="space-y-6 md:p-4 pb-16 block">
	<!-- Flex-Container für die Navigation und den Hauptinhalt -->
	<div class="flex flex-col space-y-4 lg:flex-row lg:space-x-6 lg:space-y-0">
		<!-- Zeigt die mobile Navigation auf kleinen Bildschirmen an -->
		{#if isMobile}
			<MobileNav items={sidebarNavItems} />
		{:else}
			<!-- Zeigt die Sidebar-Navigation auf größeren Bildschirmen an -->
			<aside class="-mx-4 lg:w-1/6 mt-8">
				<SidebarNav items={sidebarNavItems} />
			</aside>
		{/if}

		<!-- Hauptinhalt der Seite -->
		<div class="flex-1 w-full">
			<slot /> <!-- Rendert die Kinderkomponenten -->
		</div>
	</div>
</div>