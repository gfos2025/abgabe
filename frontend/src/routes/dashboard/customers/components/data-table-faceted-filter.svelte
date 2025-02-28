<script lang="ts">
	// Importe von Icons, Typen und UI-Komponenten
	import PlusCircled from 'svelte-radix/PlusCircled.svelte'; // Icon für "Hinzufügen"
	import Check from 'svelte-radix/Check.svelte'; // Icon für "Ausgewählt"
	import type { statuses } from '../data/data.js'; // Typdefinition für Statusoptionen
	import * as Command from '$lib/components/ui/command/index.js'; // Command-Komponente für Eingaben
	import * as Popover from '$lib/components/ui/popover/index.js'; // Popover-Komponente für Dropdown-Menüs
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import { cn } from '$lib/utils.js'; // Hilfsfunktion für Klassenname-Management
	import { Separator } from '$lib/components/ui/separator/index.js'; // Trennlinien-Komponente
	import { Badge } from '$lib/components/ui/badge/index.js'; // Badge-Komponente für Anzeige von Zählern
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Exportierte Variablen für die Filterwerte, den Titel, die Optionen und die Zähler
	export let filterValues: string[] = []; // Aktive Filterwerte
	export let title: string; // Titel des Filters
	export let options = [] as typeof statuses; // Verfügbare Optionen für den Filter
	export let counts: { [index: string]: number } = {}; // Zähler für die Optionen

	// Zustandsvariable für das Öffnen/Schließen des Popovers
	let open = false;

	// Funktion zum Hinzufügen/Entfernen von Filterwerten
	function handleSelect(currentValue: string) {
		if (Array.isArray(filterValues) && filterValues.includes(currentValue)) {
			// Wenn der Wert bereits ausgewählt ist, entferne ihn
			filterValues = filterValues.filter((v) => v !== currentValue);
		} else {
			// Andernfalls füge ihn hinzu
			filterValues = [...(Array.isArray(filterValues) ? filterValues : []), currentValue];
		}
	}
</script>

<!-- UI-Struktur -->
<Popover.Root bind:open>
	<!-- Trigger-Button für das Popover -->
	<Popover.Trigger asChild let:builder>
		<Button builders={[builder]} variant="outline" size="sm" class="h-8 border-dashed">
			<PlusCircled class="mr-2 h-4 w-4" /> <!-- Icon für "Hinzufügen" -->
			{title} <!-- Titel des Filters -->

			<!-- Anzeige der ausgewählten Filterwerte -->
			{#if filterValues.length > 0}
				<Separator orientation="vertical" class="mx-2 h-4" /> <!-- Trennlinie -->
				<Badge variant="secondary" class="rounded-sm px-1 font-normal lg:hidden">
					{filterValues.length} <!-- Anzahl der ausgewählten Filter (für kleine Bildschirme) -->
				</Badge>
				<div class="hidden space-x-1 lg:flex">
					{#if filterValues.length > 2}
						<!-- Wenn mehr als 2 Filter ausgewählt sind, zeige die Anzahl an -->
						<Badge variant="secondary" class="rounded-sm px-1 font-normal">
							{filterValues.length} {$t('customers.selected')}
						</Badge>
					{:else}
						<!-- Andernfalls zeige die einzelnen Filterwerte an -->
						{#each filterValues as option}
							<Badge variant="secondary" class="rounded-sm px-1 font-normal">
								{option}
							</Badge>
						{/each}
					{/if}
				</div>
			{/if}
		</Button>
	</Popover.Trigger>

	<!-- Inhalt des Popovers -->
	<Popover.Content class="w-[200px] p-0" align="start" side="bottom">
		<Command.Root>
			<Command.Input placeholder={title} /> <!-- Eingabefeld für die Suche -->
			<Command.List>
				<Command.Empty>{$t('customers.noResults')}</Command.Empty> <!-- Platzhalter für keine Ergebnisse -->
				<Command.Group>
					<!-- Liste der verfügbaren Optionen -->
					{#each options as option}
						{@const Icon = option.icon} <!-- Icon für die Option -->
						<Command.Item
							value={option.value}
							onSelect={(currentValue) => {
								handleSelect(currentValue); // Auswahl der Option verarbeiten
							}}
						>
							<!-- Checkbox für die Auswahl -->
							<div
								class={cn(
									'border-primary mr-2 flex h-4 w-4 items-center justify-center rounded-sm border',
									filterValues.includes(option.value)
										? 'bg-primary text-primary-foreground'
										: 'opacity-50 [&_svg]:invisible'
								)}
							>
								<Check className={cn('h-4 w-4')} /> <!-- Check-Icon -->
							</div>
							<Icon class="text-muted-foreground mr-2 h-4 w-4" /> <!-- Icon der Option -->
							<span class="text-foreground">
								{$t(option.label)} <!-- Beschriftung der Option -->
							</span>
							<!-- Anzeige der Zähler für die Option -->
							{#if counts[option.value]}
								<span
									class="ml-auto flex h-4 w-4 items-center justify-center font-mono text-xs text-foreground"
								>
									{counts[$t(option.label)]}
								</span>
							{/if}
						</Command.Item>
					{/each}
				</Command.Group>

				<!-- Option zum Zurücksetzen der Filter -->
				{#if filterValues.length > 0}
					<Command.Separator /> <!-- Trennlinie -->
					<Command.Item
						class="justify-center text-center"
						onSelect={() => {
							filterValues = []; // Filter zurücksetzen
						}}
					>
						{$t('customers.clearFilters')} <!-- Text für "Filter zurücksetzen" -->
					</Command.Item>
				{/if}
			</Command.List>
		</Command.Root>
	</Popover.Content>
</Popover.Root>