<script lang="ts">
	// Importe von Komponenten, Funktionen und Typen
	import DataTable from './components/data-table.svelte'; // Import der DataTable-Komponente
	import { onMount } from 'svelte'; // Lifecycle-Hook für das Laden der Komponente
	import { writable } from 'svelte/store'; // Writable-Store für reaktive Daten
	import type { Customer } from '$lib/types/customer'; // Typdefinition für Kunden
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Zustandsverwaltung mit einem writable-Store für Kundendaten
	const customerData = writable<Customer[]>([]);

	// Funktion zum Abrufen der Kundendaten von der API
	const getCustomerData = async () => {
		try {
			const response = await fetch(`/api/v1/customers/`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			});
			if (!response.ok) {
				throw new Error(`Error fetching customer data: ${response.status}`);
			}
			const responseData = await response.json();
			const dataArray: Customer[] = Object.values(responseData); // Umwandeln der Daten in ein Array
			customerData.set(dataArray); // Speichern der Daten im Store
		} catch (error) {
			console.error('Error during fetching customer data:', error); // Fehlerbehandlung
		}
	};

	// Lifecycle-Hook: Wird beim Laden der Komponente ausgeführt
	onMount(() => {
		getCustomerData(); // Kundendaten abrufen
	});
</script>

<!-- UI-Struktur -->
<div class="h-full flex-1 flex-col p-2 space-y-2 md:space-y-8 md:p-8 flex">
	<!-- Überschrift und Beschreibung -->
	<div class="flex items-center justify-between space-y-2">
		<div>
			<h2 class="text-2xl font-bold tracking-tight">{$t('customers.title')}</h2>
			<p class="text-muted-foreground">{$t('customers.desc')}</p>
		</div>
	</div>

	<!-- DataTable-Komponente zur Anzeige der Kundendaten -->
	<DataTable data={customerData} />
</div>