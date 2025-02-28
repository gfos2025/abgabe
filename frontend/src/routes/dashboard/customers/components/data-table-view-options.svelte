<script lang="ts">
	// Importe von Icons, Typen und UI-Komponenten
	import MixerHorizontal from 'svelte-radix/MixerHorizontal.svelte'; // Icon für "Ansicht anpassen"
	import type { TableViewModel } from 'svelte-headless-table'; // Typdefinition für das Tabellenmodell
	import type { Customer } from '$lib/types/customer'; // Typdefinition für Kunden
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js'; // Dropdown-Menü-Komponente
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Destrukturierung der übergebenen Props
	let { tableModel }: { tableModel: TableViewModel<Customer> } = $props();

	// Zugriff auf die Plugin-Zustände und flachen Spalten des Tabellenmodells
	const { pluginStates, flatColumns } = tableModel;
	const { hiddenColumnIds } = pluginStates.hide;

	// Funktion zum Ein- oder Ausblenden einer Spalte
	function handleHide(id: string) {
		hiddenColumnIds.update((ids: string[]) => {
			if (ids.includes(id)) {
				// Wenn die Spalte bereits ausgeblendet ist, entferne sie aus der Liste
				return ids.filter((i) => i !== id);
			}
			// Andernfalls füge sie zur Liste hinzu
			return [...ids, id];
		});
	}

	// Liste der Spalten, die ein- und ausgeblendet werden können
	const hidableCols = ['lastName', 'phoneNumber', 'contactStatus'];
</script>

<!-- Dropdown-Menü für die Ansichtsanpassung -->
<DropdownMenu.Root>
	<!-- Trigger-Button für das Dropdown-Menü -->
	<DropdownMenu.Trigger asChild let:builder>
		<Button variant="outline" size="sm" class="ml-auto hidden h-8 lg:flex" builders={[builder]}>
			<MixerHorizontal class="mr-2 h-4 w-4" /> <!-- Icon für "Ansicht anpassen" -->
			{$t('customers.view')} <!-- Text für "Ansicht" -->
		</Button>
	</DropdownMenu.Trigger>

	<!-- Inhalt des Dropdown-Menüs -->
	<DropdownMenu.Content>
		<!-- Überschrift für das Menü -->
		<DropdownMenu.Label>{$t('customers.toggleColumns')}</DropdownMenu.Label>

		<!-- Trennlinie -->
		<DropdownMenu.Separator />

		<!-- Liste der ein- und ausblendbaren Spalten -->
		{#each flatColumns as col}
			{#if hidableCols.includes(col.id)}
				<!-- Checkbox-Item für jede Spalte -->
				<DropdownMenu.CheckboxItem
					checked={!$hiddenColumnIds.includes(col.id)} 
					on:click={() => handleHide(col.id)} 
				>
					{col.header()} <!-- Anzeige des Spaltenheaders -->
				</DropdownMenu.CheckboxItem>
			{/if}
		{/each}
	</DropdownMenu.Content>
</DropdownMenu.Root>