<script lang="ts">
    // Importe von Lifecycle-Hooks, UI-Komponenten und Typen
    import { onMount } from 'svelte'; // Lifecycle-Hook
    import * as Tabs from "$lib/components/ui/tabs/index.js"; // Tabs-Komponente
    import * as Card from "$lib/components/ui/card/index.js"; // Card-Komponente
    import * as Popover from "$lib/components/ui/popover/index.js"; // Popover-Komponente
    import * as Command from "$lib/components/ui/command/index.js"; // Command-Komponente
    import * as Label from "$lib/components/ui/label/index.js"; // Label-Komponente
    import { Button } from "$lib/components/ui/button"; // Button-Komponente
    import { Check, ChevronsUpDown } from "lucide-svelte"; // Icons
    import LogTable from '$lib/components/ui/log-table/LogTable.svelte'; // Log-Tabelle-Komponente
    import type { Log } from '$lib/types/log'; // Typdefinition für Logs
    import { LogType } from '$lib/types/log'; // Enum für Log-Typen
    import { t } from 'svelte-i18n'; // Internationalisierungsfunktion
    import { cn } from "$lib/utils"; // Hilfsfunktion für Klassenname-Management

    // Zustandsvariablen
    let logs: Log[] = $state([]); // Liste der Logs
    let activeTab = $state<'week' | 'month' | 'year'>('week'); // Aktiver Tab (Woche, Monat, Jahr)
    let activeLogType = $state<number | null>(null); // Aktiver Log-Typ (oder null für alle Logs)
    let openLogTypeFilter = $state(false); // Zustand für das Log-Typ-Filter-Dropdown
    let searchQuery = $state(""); // Suchbegriff für Log-Typ-Filter

    // Liste der Log-Typen mit Übersetzungen und Werten
    const logTypes = [
        { label: 'logs.registered_user', value: LogType.REGISTERED_USER },
        { label: 'logs.created_customer', value: LogType.CREATED_CUSTOMER },
        { label: 'logs.updated_customer', value: LogType.UPDATED_CUSTOMER },
        { label: 'logs.deleted_customer', value: LogType.DELETED_CUSTOMER },
        { label: 'logs.created_transaction', value: LogType.CREATED_TRANSACTION },
        { label: 'logs.updated_transaction', value: LogType.UPDATED_TRANSACTION },
        { label: 'logs.created_appointment', value: LogType.CREATED_APPOINTMENT },
        { label: 'logs.updated_appointment', value: LogType.UPDATED_APPOINTMENT },
        { label: 'logs.deleted_appointment', value: LogType.DELETED_APPOINTMENT },
    ];

    // Füge "Alle Logs" zur Liste der Log-Typen hinzu
    const allLogTypes = [{ label: 'logs.allLogs', value: null }, ...logTypes];

    // Gefilterte Log-Typen basierend auf der Suchanfrage
    let filteredLogTypes = $state(allLogTypes);

    // Aktualisiere die gefilterten Log-Typen, wenn sich die Suchanfrage ändert
    $effect(() => {
        if (!searchQuery) {
            filteredLogTypes = allLogTypes;
        } else {
            const filtered = allLogTypes.filter(type =>
                $t(type.label).toLowerCase().includes(searchQuery.toLowerCase())
            );
            filteredLogTypes = filtered;
        }
    });

    // Funktion zur Berechnung des Datumsbereichs basierend auf dem ausgewählten Tab
    const getDateRange = (range: 'week' | 'month' | 'year') => {
        const now = new Date();
        let startDate: Date;

        switch (range) {
            case 'week':
                startDate = new Date(now);
                startDate.setDate(startDate.getDate() - 7);
                break;
            case 'month':
                startDate = new Date(now);
                startDate.setMonth(startDate.getMonth() - 1);
                break;
            case 'year':
                startDate = new Date(now);
                startDate.setFullYear(startDate.getFullYear() - 1);
                break;
            default:
                startDate = new Date(now);
        }

        startDate.setHours(0, 0, 0, 0);
        const endDate = new Date(now);
        endDate.setHours(23, 59, 59, 999);

        return { startDate, endDate };
    };

    // Funktion zum Abrufen der Logs von der API
    const fetchLogs = async (startDate?: Date, endDate?: Date, logType?: number): Promise<Log[]> => {
        try {
            const response = await fetch('/api/v1/logs', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    logType,
                    startDate,
                    endDate
                }),
            });

            if (!response.ok) {
                throw new Error(`Error fetching logs: ${response.status}`);
            }

            const data: Log[] = await response.json();
            // Sortiere die Logs nach Zeitstempel (neueste zuerst)
            return data.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
        } catch (error) {
            console.error('Error fetching logs:', error);
            return [];
        }
    };

    // Funktion zum Laden der Logs basierend auf dem ausgewählten Zeitraum und Log-Typ
    const loadLogs = async (range: 'week' | 'month' | 'year', logType?: number) => {
        const { startDate, endDate } = getDateRange(range);
        logs = await fetchLogs(startDate, endDate, logType);
    };

    // Funktion zur Rückgabe des Labels des aktiven Log-Typs
    const getActiveLogTypeLabel = () => {
        if (activeLogType === null) {
            return $t('logs.allLogs');
        }
        const selectedType = logTypes.find(type => type.value === activeLogType);
        return selectedType ? $t(selectedType.label) : $t('logs.allLogs');
    };

    // Lifecycle-Hook: Wird beim Laden der Komponente ausgeführt
    onMount(() => {
        loadLogs(activeTab, activeLogType);
    });

    // Effekt: Lädt die Logs neu, wenn sich der aktive Tab ändert
    $effect(() => {
        if (activeTab) {
            loadLogs(activeTab, activeLogType);
        }
    });

    // Effekt: Lädt die Logs neu, wenn sich der aktive Log-Typ ändert
    $effect(() => {
        loadLogs(activeTab, activeLogType);
    });
</script>

<!-- UI-Struktur -->
<main class="p-6">
    <!-- Titel und Beschreibung -->
    <div class="mb-6">
        <h1 class="text-2xl font-bold">{$t('logs.title')}</h1>
        <p class="text-sm text-muted-foreground">
            {$t('logs.description')}
        </p>
    </div>

    <!-- Filter- und Tabs-Bereich -->
    <div class="flex flex-col sm:flex-row sm:items-end gap-4 mb-4">
        <!-- Tabs für den Zeitraum (Woche, Monat, Jahr) -->
        <div class="grow">
            <Tabs.Root bind:value={activeTab}>
                <Tabs.List class="grid w-full grid-cols-3">
                    <Tabs.Trigger value="week" on:click={() => activeTab = 'week'}>{$t('logs.thisWeek')}</Tabs.Trigger>
                    <Tabs.Trigger value="month" on:click={() => activeTab = 'month'}>{$t('logs.thisMonth')}</Tabs.Trigger>
                    <Tabs.Trigger value="year" on:click={() => activeTab = 'year'}>{$t('logs.thisYear')}</Tabs.Trigger>
                </Tabs.List>
            </Tabs.Root>
        </div>

        <!-- Dropdown-Filter für Log-Typen -->
        <div class="w-full sm:w-64">
            <div class="flex flex-col">
                <Label.Root for="logTypeFilter" class="mb-2 hidden sm:block">{$t('logs.filterByLogType')}</Label.Root>
                <Popover.Root bind:open={openLogTypeFilter}>
                    <Popover.Trigger 
                        class="w-full" 
                        id="logTypeFilter"
                    >
                        <Button
                            variant="outline"
                            class="w-full justify-between"
                            role="combobox"
                            aria-expanded={openLogTypeFilter}
                        >
                            {$t(getActiveLogTypeLabel())}
                            <ChevronsUpDown class="opacity-50 h-4 w-4 ml-2" />
                        </Button>
                    </Popover.Trigger>
                    <Popover.Content class="w-[250px] p-0">
                        <Command.Root>
                            <Command.Input 
                                placeholder={$t('logs.searchLogType')} 
                                bind:value={searchQuery}
                            />
                            <Command.List>
                                <Command.Empty>{$t('logs.noLogTypeFound')}</Command.Empty>
                                <Command.Group>
                                    {#each filteredLogTypes as logType}
                                        <Command.Item
                                            onSelect={() => {
                                                activeLogType = logType.value;
                                                openLogTypeFilter = false;
                                                searchQuery = "";
                                            }}
                                        >
                                            <Check
                                                class={cn(
                                                    "mr-2 h-4 w-4", 
                                                    activeLogType !== logType.value && "text-transparent"
                                                )}
                                            />
                                            <span>{$t(logType.label)}</span>
                                        </Command.Item>
                                    {/each}
                                </Command.Group>
                            </Command.List>
                        </Command.Root>
                    </Popover.Content>
                </Popover.Root>
            </div>
        </div>
    </div>

    <!-- Log-Inhalt (Tabs für Woche, Monat, Jahr) -->
    <Tabs.Root value={activeTab}>
        <Tabs.Content value="week">
            <Card.Root>
                <Card.Header>
                    <Card.Title>{$t('logs.thisWeeksLogs')}</Card.Title>
                    <Card.Description>{$t('logs.logsFromPastWeek')}</Card.Description>
                </Card.Header>
                <Card.Content>
                    <LogTable {logs} />
                </Card.Content>
            </Card.Root>
        </Tabs.Content>

        <Tabs.Content value="month">
            <Card.Root>
                <Card.Header>
                    <Card.Title>{$t('logs.thisMonthsLogs')}</Card.Title>
                    <Card.Description>{$t('logs.logsFromPastMonth')}</Card.Description>
                </Card.Header>
                <Card.Content>
                    <LogTable {logs} />
                </Card.Content>
            </Card.Root>
        </Tabs.Content>

        <Tabs.Content value="year">
            <Card.Root>
                <Card.Header>
                    <Card.Title>{$t('logs.thisYearsLogs')}</Card.Title>
                    <Card.Description>{$t('logs.logsFromPastYear')}</Card.Description>
                </Card.Header>
                <Card.Content>
                    <LogTable {logs} />
                </Card.Content>
            </Card.Root>
        </Tabs.Content>
    </Tabs.Root>
</main>