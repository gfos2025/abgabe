<script lang="ts">
	// Importe von Icons, Daten, Typen und UI-Komponenten
	import DotsHorizontal from 'svelte-radix/DotsHorizontal.svelte'; // Icon für Menüpunkte
	import { priorities } from '../data/data.js'; // Liste der Prioritäten
	import { type Customer, customerSchema } from '$lib/types/customer'; // Typdefinitionen für Kunden
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js'; // Dropdown-Menü-Komponente
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Destrukturierung der übergebenen Props
	let { row }: { row: Customer } = $props();

	// Validierung und Ableitung des Kundenobjekts
	const customer = customerSchema.parse(row);
</script>

<!-- Dropdown-Menü für Aktionen -->
<DropdownMenu.Root>
	<!-- Trigger-Button für das Dropdown-Menü -->
	<DropdownMenu.Trigger asChild let:builder>
		<Button
			variant="ghost"
			builders={[builder]}
			class="data-[state=open]:bg-primary flex h-8 w-8 p-0"
		>
			<DotsHorizontal class="h-4 w-4" /> <!-- Icon für Menüpunkte -->
			<span class="sr-only">{$t('customers.openMenu')}</span> <!-- Screenreader-Text -->
		</Button>
	</DropdownMenu.Trigger>

	<!-- Inhalt des Dropdown-Menüs -->
	<DropdownMenu.Content class="w-[160px]" align="end">
		<!-- Menüpunkt zum Bearbeiten -->
		<DropdownMenu.Item>{$t('customers.edit')}</DropdownMenu.Item>

		<!-- Trennlinie -->
		<DropdownMenu.Separator />

		<!-- Untermenü für Prioritäten -->
		<DropdownMenu.Sub>
			<!-- Trigger für das Untermenü -->
			<DropdownMenu.SubTrigger>{$t('customers.priority')}</DropdownMenu.SubTrigger>

			<!-- Inhalt des Untermenüs -->
			<DropdownMenu.SubContent>
				<!-- Radio-Gruppe für die Auswahl der Priorität -->
				<DropdownMenu.RadioGroup value={customer?.priority}>
					{#each priorities as priority}
						<!-- Radio-Item für jede Priorität -->
						<DropdownMenu.RadioItem value={priority.value}>
							'{$t(priority.label)}' <!-- Anzeige des übersetzten Labels -->
						</DropdownMenu.RadioItem>
					{/each}
				</DropdownMenu.RadioGroup>
			</DropdownMenu.SubContent>
		</DropdownMenu.Sub>

		<!-- Trennlinie -->
		<DropdownMenu.Separator />

		<!-- Menüpunkt zum Löschen -->
		<DropdownMenu.Item>
			{$t('customers.delete')} <!-- Text für "Löschen" -->
			<DropdownMenu.Shortcut>⌘⌫</DropdownMenu.Shortcut> <!-- Tastenkürzel -->
		</DropdownMenu.Item>
	</DropdownMenu.Content>
</DropdownMenu.Root>