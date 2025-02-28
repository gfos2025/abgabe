<script lang="ts">
	// Importe von Svelte-Stores, Tabellenfunktionen und UI-Komponenten
	import { get, writable, derived } from 'svelte/store'; // Svelte-Stores
	import { Render, Subscribe, createRender, createTable } from 'svelte-headless-table'; // Tabellenfunktionen
	import { onMount } from 'svelte'; // Lifecycle-Hook
	import {
		addColumnFilters,
		addHiddenColumns,
		addPagination,
		addSelectedRows,
		addSortBy,
		addTableFilter
	} from 'svelte-headless-table/plugins'; // Plugins für die Tabelle
	import type { Customer } from '$lib/types/customer'; // Typdefinition für Kunden
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion
	import {
		DataTableCheckbox,
		DataTableColumnHeader,
		DataTablePagination,
		DataTableRowActions,
		DataTableToolbar
	} from './index.js'; // Benutzerdefinierte Tabellenkomponenten
	import * as Table from '$lib/components/ui/table/index.js'; // UI-Komponenten für die Tabelle

	// Zustandsvariable für die Kundendaten
	const data = writable<Customer[]>([]);

	// Erstellen der Tabelle mit Plugins
	const table = createTable(
		derived(data, ($data) => $data), // Datenquelle
		{
			select: addSelectedRows(), // Plugin für Zeilenauswahl
			sort: addSortBy({ // Plugin für Sortierung
				toggleOrder: ['asc', 'desc']
			}),
			page: addPagination(), // Plugin für Paginierung
			filter: addTableFilter({ // Plugin für globale Filterung
				fn: ({ filterValue, value }) => {
					return value.toLowerCase().includes(filterValue.toLowerCase());
				}
			}),
			colFilter: addColumnFilters(), // Plugin für Spaltenfilter
			hide: addHiddenColumns() // Plugin für das Ausblenden von Spalten
		}
	);

	// Definition der Tabellenspalten
	const columns = table.createColumns([
		// Spalte für die Zeilenauswahl
		table.display({
			id: 'select',
			header: (_, { pluginStates }) => {
				const { allPageRowsSelected } = pluginStates.select;
				return createRender(DataTableCheckbox, {
					checked: allPageRowsSelected,
					'aria-label': 'Select all'
				});
			},
			cell: ({ row }, { pluginStates }) => {
				const { getRowState } = pluginStates.select;
				const { isSelected } = getRowState(row);
				return createRender(DataTableCheckbox, {
					checked: isSelected,
					'aria-label': 'Select row',
					class: 'translate-y-[2px]'
				});
			},
			plugins: {
				sort: {
					disable: true // Deaktiviert die Sortierung für diese Spalte
				}
			}
		}),
		// Spalte für den Namen
		table.column({
			accessor: 'name',
			header: () => {
				return $t('customers.name');
			},
			id: 'name',
			plugins: {
				sort: {
					disable: true // Deaktiviert die Sortierung für diese Spalte
				}
			}
		}),
		// Spalte für den Nachnamen
		table.column({
			accessor: 'surname',
			header: () => {
				return $t('customers.lastname');
			},
			id: 'lastName'
		}),
		// Spalte für die Telefonnummer
		table.column({
			accessor: 'phoneNumber',
			id: 'phoneNumber',
			header: () => {
				return $t('customers.phoneNumber');
			},
			plugins: {
				colFilter: {
					fn: ({ filterValue, value }) => {
						if (filterValue.length === 0) return true;
						if (!Array.isArray(filterValue) || typeof value !== 'string') return true;
						return filterValue.some((filter) => {
							return value.includes(filter);
						});
					},
					initialFilterValue: [],
					render: ({ filterValue }) => {
						return get(filterValue);
					}
				}
			}
		}),
		// Spalte für den Kontaktstatus
		table.column({
			accessor: 'contactStatus',
			header: () => {
				return $t('customers.contactStatus');
			},
			id: 'contactStatus',
			plugins: {
				colFilter: {
					fn: ({ filterValue, value }) => {
						if (filterValue.length === 0) return true;
						if (!Array.isArray(filterValue) || typeof value !== 'string') return true;
						return filterValue.some((filter) => {
							return value.includes(filter);
						});
					},
					initialFilterValue: [],
					render: ({ filterValue }) => {
						return get(filterValue);
					}
				}
			}
		}),
		// Spalte für Aktionen (z. B. Bearbeiten, Löschen)
		table.display({
			id: 'actions',
			header: () => {
				return '';
			},
			cell: ({ row }) => {
				if (row.isData() && row.original) {
					return createRender(DataTableRowActions, {
						row: row.original
					});
				}
				return '';
			}
		})
	]);

	// Erstellen des Tabellenmodells
	const tableModel = table.createViewModel(columns);

	// Destrukturierung der Tabellenattribute und -zustände
	const { headerRows, pageRows, tableAttrs, tableBodyAttrs } = tableModel;

	// Funktion zum Abrufen der Kundendaten
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
			const dataArray: Customer[] = Object.values(responseData);
			data.set(dataArray);
		} catch (error) {
			console.error('Error during fetching customer data:', error);
		}
	};

	// Lifecycle-Hook: Wird beim Laden der Komponente ausgeführt
	onMount(() => {
		getCustomerData();
	});
</script>

<!-- UI-Struktur -->
<div class="space-y-4">
	<!-- Toolbar für die Tabelle -->
	<DataTableToolbar {tableModel} {data} />

	<!-- Tabelle -->
	<div class="rounded-md border">
		<Table.Root {...$tableAttrs}>
			<!-- Tabellenkopf -->
			<Table.Header>
				{#each $headerRows as headerRow}
					<Subscribe rowAttrs={headerRow.attrs()}>
						<Table.Row>
							{#each headerRow.cells as cell (cell.id)}
								<Subscribe attrs={cell.attrs()} let:attrs props={cell.props()} let:props>
									<Table.Head {...attrs}>
										{#if cell.id !== 'select' && cell.id !== 'actions'}
											<!-- Spaltenheader mit Sortierfunktion -->
											<DataTableColumnHeader {props} {tableModel} cellId={cell.id}>
												<Render of={cell.render()} />
											</DataTableColumnHeader>
										{:else}
											<!-- Standard-Rendering für Checkbox und Aktionen -->
											<Render of={cell.render()} />
										{/if}
									</Table.Head>
								</Subscribe>
							{/each}
						</Table.Row>
					</Subscribe>
				{/each}
			</Table.Header>

			<!-- Tabellenkörper -->
			<Table.Body {...$tableBodyAttrs}>
				{#if $pageRows.length}
					<!-- Zeilen der Tabelle -->
					{#each $pageRows as row (row.id)}
						<Subscribe rowAttrs={row.attrs()} let:rowAttrs>
							<Table.Row {...rowAttrs}>
								{#each row.cells as cell (cell.id)}
									<Subscribe attrs={cell.attrs()} let:attrs>
										<Table.Cell {...attrs}>
											{#if cell.id === 'name' || cell.id === 'surname'}
												<!-- Zellen für Name und Nachname mit fester Breite -->
												<div class="w-[80px]">
													<Render of={cell.render()} />
												</div>
											{:else}
												<!-- Standard-Rendering für andere Zellen -->
												<Render of={cell.render()} />
											{/if}
										</Table.Cell>
									</Subscribe>
								{/each}
							</Table.Row>
						</Subscribe>
					{/each}
				{:else}
					<!-- Platzhalter, wenn keine Daten vorhanden sind -->
					<Table.Row>
						<Table.Cell colspan={columns.length} class="h-24 text-center">
							{$t('customers.noResults')}
						</Table.Cell>
					</Table.Row>
				{/if}
			</Table.Body>
		</Table.Root>
	</div>

	<!-- Paginierung -->
	<DataTablePagination {tableModel} />
</div>