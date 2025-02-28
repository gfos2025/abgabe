<script lang="ts">
	// Importe von Icons, Typen und Hilfsfunktionen
	import EyeNone from 'svelte-radix/EyeNone.svelte'; // Icon für "Verstecken"
	import ArrowDown from 'svelte-radix/ArrowDown.svelte'; // Icon für absteigende Sortierung
	import ArrowUp from 'svelte-radix/ArrowUp.svelte'; // Icon für aufsteigende Sortierung
	import CaretSort from 'svelte-radix/CaretSort.svelte'; // Icon für Sortierung
	import type { TableViewModel } from 'svelte-headless-table'; // Typdefinition für das Tabellenmodell
	import type { Customer } from '$lib/types/customer'; // Typdefinition für Kunden
	import { cn } from '$lib/utils.js'; // Hilfsfunktion für Klassenname-Management
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js'; // Dropdown-Menü-Komponente
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Zustandsvariable für den Klassenname
	let className: string | undefined | null = undefined;

	// Destrukturierung der übergebenen Props
	let { props, tableModel, cellId }: {
		className?: string | null;
		props: {
			select: never;
			sort: {
				order: 'desc' | 'asc' | undefined; // Sortierreihenfolge
				toggle: (_: Event) => void; // Funktion zum Umschalten der Sortierung
				clear: () => void; // Funktion zum Zurücksetzen der Sortierung
				disabled: boolean; // Deaktivierungsstatus der Sortierung
			};
			filter: never;
		};
		tableModel: TableViewModel<Customer>; // Tabellenmodell
		cellId: string; // ID der Zelle
	} = $props();

	// Zugriff auf die versteckten Spalten-IDs aus dem Tabellenmodell
	const { hiddenColumnIds } = tableModel.pluginStates.hide;

	// Funktion zum Sortieren in aufsteigender Reihenfolge
	function handleAscSort(e: Event) {
		if (props.sort.order === 'asc') {
			return; // Wenn bereits aufsteigend sortiert, nichts tun
		}
		props.sort.toggle(e); // Sortierung umschalten
	}

	// Funktion zum Sortieren in absteigender Reihenfolge
	function handleDescSort(e: Event) {
		if (props.sort.order === 'desc') {
			return; // Wenn bereits absteigend sortiert, nichts tun
		}
		if (props.sort.order === undefined) {
			props.sort.toggle(e); // Wenn keine Sortierung aktiv, Sortierung umschalten
		}
		props.sort.toggle(e); // Sortierung umschalten
	}

	// Funktion zum Verstecken der Spalte
	function handleHide() {
		hiddenColumnIds.update((ids: string[]) => {
			if (ids.includes(cellId)) {
				return ids; // Wenn die Spalte bereits versteckt ist, nichts ändern
			}
			return [...ids, cellId]; // Spalte zur Liste der versteckten Spalten hinzufügen
		});
	}
</script>

<!-- UI-Struktur -->
{#if !props.sort.disabled}
	<!-- Wenn die Sortierung nicht deaktiviert ist, zeige das Dropdown-Menü an -->
	<div class={cn('flex items-center', className)}>
		<DropdownMenu.Root>
			<DropdownMenu.Trigger asChild let:builder>
				<Button
					variant="ghost"
					builders={[builder]}
					class="data-[state=open]:bg-accent -ml-3 h-8"
					size="sm"
				>
					<slot /> <!-- Platzhalter für den Inhalt des Buttons -->
					{#if props.sort.order === 'desc'}
						<ArrowDown class="ml-2 h-4 w-4" /> <!-- Icon für absteigende Sortierung -->
					{:else if props.sort.order === 'asc'}
						<ArrowUp class="ml-2 h-4 w-4" /> <!-- Icon für aufsteigende Sortierung -->
					{:else}
						<CaretSort class="ml-2 h-4 w-4" /> <!-- Icon für Sortierung -->
					{/if}
				</Button>
			</DropdownMenu.Trigger>
			<DropdownMenu.Content align="start">
				<!-- Menüpunkt für aufsteigende Sortierung -->
				<DropdownMenu.Item on:click={handleAscSort}>
					<ArrowUp class="text-muted-foreground/70 mr-2 h-3.5 w-3.5" />
					{$t('customers.ascending')} <!-- Text für aufsteigende Sortierung -->
				</DropdownMenu.Item>
				<!-- Menüpunkt für absteigende Sortierung -->
				<DropdownMenu.Item on:click={handleDescSort}>
					<ArrowDown class="text-muted-foreground/70 mr-2 h-3.5 w-3.5" />
					{$t('customers.descending')} <!-- Text für absteigende Sortierung -->
				</DropdownMenu.Item>
				<!-- Trennlinie im Menü -->
				<DropdownMenu.Separator />
				<!-- Menüpunkt zum Verstecken der Spalte -->
				<DropdownMenu.Item on:click={handleHide}>
					<EyeNone class="text-muted-foreground/70 mr-2 h-3.5 w-3.5" />
					{$t('customers.hide')} <!-- Text für "Verstecken" -->
				</DropdownMenu.Item>
			</DropdownMenu.Content>
		</DropdownMenu.Root>
	</div>
{:else}
	<!-- Wenn die Sortierung deaktiviert ist, zeige nur den Slot-Inhalt an -->
	<slot />
{/if}