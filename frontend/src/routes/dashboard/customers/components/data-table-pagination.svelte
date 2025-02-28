<script lang="ts">
	// Importe von Icons, Typen und UI-Komponenten
	import ChevronRight from 'svelte-radix/ChevronRight.svelte'; // Icon für "Nächste Seite"
	import ChevronLeft from 'svelte-radix/ChevronLeft.svelte'; // Icon für "Vorherige Seite"
	import DoubleArrowRight from 'svelte-radix/DoubleArrowRight.svelte'; // Icon für "Letzte Seite"
	import DoubleArrowLeft from 'svelte-radix/DoubleArrowLeft.svelte'; // Icon für "Erste Seite"
	import type { TableViewModel } from 'svelte-headless-table'; // Typdefinition für das Tabellenmodell
	import type { Customer } from '$lib/types/customer'; // Typdefinition für Kunden
	import * as Select from '$lib/components/ui/select/index.js'; // Select-Komponente für Dropdown-Menüs
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Destrukturierung der übergebenen Props
	let { tableModel }: { tableModel: TableViewModel<Customer> } = $props();

	// Zugriff auf Tabellenfunktionen und Zustände
	const { pageRows, pluginStates, rows } = tableModel;

	// Destrukturierung der Paginierungszustände
	const { hasNextPage, hasPreviousPage, pageIndex, pageCount, pageSize } = pluginStates.page;

	// Zugriff auf die ausgewählten Zeilen-IDs
	const { selectedDataIds } = pluginStates.select;
</script>

<!-- UI-Struktur -->
<div class="flex items-center justify-between px-2">
	<!-- Anzeige der ausgewählten Zeilen -->
	<div class="text-muted-foreground flex-1 text-sm">
		{Object.keys($selectedDataIds).length} <!-- Anzahl der ausgewählten Zeilen -->
		{$t('customers.of')} <!-- Text "von" -->
		{$rows.length} <!-- Gesamtanzahl der Zeilen -->
		{$t('customers.rowsSelected')}. <!-- Text "Zeilen ausgewählt" -->
	</div>

	<!-- Paginierungssteuerelemente -->
	<div class="flex items-center space-x-6 lg:space-x-8">
		<!-- Dropdown-Menü für die Anzahl der Zeilen pro Seite -->
		<div class="flex items-center space-x-2">
			<p class="text-sm font-medium">{$t('customers.rowsPerPage')}</p> <!-- Text "Zeilen pro Seite" -->
			<Select.Root
				onSelectedChange={(selected) => pageSize.set(Number(selected?.value))}
				selected={{ value: 10, label: '10' }}
			>
				<Select.Trigger class="h-8 w-[70px]">
					<Select.Value placeholder={$t('customers.selectPageSize')} /> <!-- Platzhaltertext -->
				</Select.Trigger>
				<Select.Content>
					<!-- Optionen für die Seitenlänge -->
					<Select.Item value="10">10</Select.Item>
					<Select.Item value="20">20</Select.Item>
					<Select.Item value="30">30</Select.Item>
					<Select.Item value="40">40</Select.Item>
					<Select.Item value="50">50</Select.Item>
				</Select.Content>
			</Select.Root>
		</div>

		<!-- Anzeige der aktuellen Seite und der Gesamtseiten -->
		<div class="flex w-[100px] items-center justify-center text-sm font-medium">
			{$t('customers.page')} <!-- Text "Seite" -->
			{$pageIndex + 1} <!-- Aktuelle Seite -->
			{$t('customers.of')} <!-- Text "von" -->
			{$pageCount} <!-- Gesamtanzahl der Seiten -->
		</div>

		<!-- Schaltflächen für die Paginierung -->
		<div class="flex items-center space-x-2">
			<!-- Schaltfläche für die erste Seite -->
			<Button
				variant="outline"
				class="hidden h-8 w-8 p-0 lg:flex"
				on:click={() => ($pageIndex = 0)}
				disabled={!$hasPreviousPage} 
			>
				<span class="sr-only">{$t('customers.goToFirstPage')}</span> <!-- Screenreader-Text -->
				<DoubleArrowLeft size={15} /> <!-- Icon für "Erste Seite" -->
			</Button>

			<!-- Schaltfläche für die vorherige Seite -->
			<Button
				variant="outline"
				class="h-8 w-8 p-0"
				on:click={() => ($pageIndex = $pageIndex - 1)}
				disabled={!$hasPreviousPage}
			>
				<span class="sr-only">{$t('customers.goToPreviousPage')}</span> <!-- Screenreader-Text -->
				<ChevronLeft size={15} /> <!-- Icon für "Vorherige Seite" -->
			</Button>

			<!-- Schaltfläche für die nächste Seite -->
			<Button
				variant="outline"
				class="h-8 w-8 p-0"
				disabled={!$hasNextPage}
				on:click={() => ($pageIndex = $pageIndex + 1)}
			>
				<span class="sr-only">{$t('customers.goToNextPage')}</span> <!-- Screenreader-Text -->
				<ChevronRight size={15} /> <!-- Icon für "Nächste Seite" -->
			</Button>

			<!-- Schaltfläche für die letzte Seite -->
			<Button
				variant="outline"
				class="hidden h-8 w-8 p-0 lg:flex"
				disabled={!$hasNextPage}
				on:click={() => ($pageIndex = Math.ceil($rows.length / $pageRows.length) - 1)}
			>
				<span class="sr-only">{$t('customers.goToLastPage')}</span> <!-- Screenreader-Text -->
				<DoubleArrowRight size={15} /> <!-- Icon für "Letzte Seite" -->
			</Button>
		</div>
	</div>
</div>