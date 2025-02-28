<script lang="ts">
	// Importe von Prioritäten-Daten und Internationalisierungsfunktion
	import { priorities } from '../data/data.js'; // Liste der Prioritäten
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Destrukturierung der übergebenen Props
	let { value }: { value: string } = $props();

	// Finde die Priorität, die dem übergebenen Wert entspricht
	const priority = priorities.find((priority) => priority.value === value);

	// Extrahiere das Icon der gefundenen Priorität
	const Icon = priority?.icon;
</script>

<!-- UI-Struktur -->
{#if priority}
	<!-- Wenn eine Priorität gefunden wurde, zeige sie an -->
	<div class="flex items-center">
		{#if Icon}
			<!-- Wenn ein Icon vorhanden ist, zeige es an -->
			<Icon class="text-muted-foreground mr-2 h-4 w-4" />
		{/if}
		<!-- Zeige das Label der Priorität an (mit Übersetzung) -->
		<span>{$t(priority.label)}</span>
	</div>
{/if}