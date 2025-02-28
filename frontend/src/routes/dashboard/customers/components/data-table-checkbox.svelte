<script lang="ts">
	// Importe von Typen und Komponenten
	import type { HTMLButtonAttributes } from "svelte/elements"; // Typdefinition für HTML-Button-Attribute
	import type { Writable } from "svelte/store"; // Typdefinition für einen schreibbaren Store
	import { Checkbox } from "$lib/components/ui/checkbox/index.js"; // Import der Checkbox-Komponente

	// Typdefinition für die Props der Komponente
	type $$Props = HTMLButtonAttributes & {
		checked: Writable<boolean>; // Ein schreibbarer Store für den Zustand der Checkbox
	};

	// Exportierte Variable für den Zustand der Checkbox
	export let checked: Writable<boolean>;
</script>

<!-- Checkbox-Komponente mit gebundenem Zustand und restlichen Props -->
<Checkbox bind:checked={$checked} {...$$restProps} />