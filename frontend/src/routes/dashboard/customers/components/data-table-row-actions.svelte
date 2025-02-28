<script lang="ts">
	// Importe von Icons, Hilfsfunktionen, Typen und UI-Komponenten
	import DotsHorizontal from 'svelte-radix/DotsHorizontal.svelte'; // Icon für Menüpunkte
	import ChevronsUpDown from 'lucide-svelte/icons/chevrons-up-down'; // Icon für Dropdowns
	import PlusCircled from 'svelte-radix/PlusCircled.svelte'; // Icon für "Hinzufügen"
	import Check from 'lucide-svelte/icons/check'; // Icon für "Ausgewählt"
	import { cn } from '$lib/utils.js'; // Hilfsfunktion für Klassenname-Management
	import { goto } from '$app/navigation'; // Funktion für Navigation
	import { priorities, statuses } from '../data/data.js'; // Daten für Prioritäten und Status
	import { type Customer, customerSchema } from '$lib/types/customer'; // Typdefinitionen für Kunden
	import { Button } from '$lib/components/ui/button/index.js'; // Button-Komponente
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js'; // Dropdown-Menü-Komponente
	import * as Command from '$lib/components/ui/command/index.js'; // Command-Komponente für Eingaben
	import * as Popover from '$lib/components/ui/popover/index.js'; // Popover-Komponente für Dropdowns
	import * as Dialog from '$lib/components/ui/dialog/index.js'; // Dialog-Komponente für Modals
	import Label from '$lib/components/ui/label/label.svelte'; // Label-Komponente
	import Input from '$lib/components/ui/input/input.svelte'; // Input-Komponente
	import { StatusEnum } from '$lib/types/status'; // Enum für Status
	import { buttonVariants } from "$lib/components/ui/button/index.js"; // Button-Varianten
	import { Delete } from 'lucide-svelte'; // Icon für "Löschen"
	import { toast } from 'svelte-sonner'; // Toast-Benachrichtigungen
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion

	// Typdefinition für Mitarbeiter
	type Employee = {
		id: string;
		name: string;
		surname: string;
		email: string;
		role: string;
	};

	// Destrukturierung der übergebenen Props
	let { row }: { row: Customer } = $props();
	const customer: Customer = $derived(customerSchema.parse(row)); // Validierung und Ableitung des Kundenobjekts

	// Zustandsvariablen für Dialoge und Formulardaten
	let openDeleteDialog = $state(false); // Zustand für das Lösch-Dialogfeld
	let openEditDialog = $state(false); // Zustand für das Bearbeitungs-Dialogfeld
	let name: string = $state(customer.name); // Zustand für den Namen
	let surname: string = $state(customer.surname); // Zustand für den Nachnamen
	let email: string = $state(customer.email); // Zustand für die E-Mail
	let phoneNumber: string = $state(customer.phoneNumber); // Zustand für die Telefonnummer
	let address: string = $state(customer.address); // Zustand für die Adresse
	let company: string = $state(customer.company); // Zustand für das Unternehmen
	let contactState: StatusEnum | null = $state(customer?.contactStatus); // Zustand für den Kontaktstatus
	let openManager: boolean = $state(false); // Zustand für das Manager-Dropdown
	let openContact: boolean = $state(false); // Zustand für das Status-Dropdown

	// Zustandsvariablen für Mitarbeiterdaten und ausgewählten Manager
	let coworkerData: Employee[] = $state([]); // Liste der Mitarbeiter
	let selectedManager: Employee | null = $state(null); // Ausgewählter Manager

	// Effekt zum Abrufen der Mitarbeiterdaten, wenn das Bearbeitungs-Dialogfeld geöffnet wird
	$effect(() => {
		if (openEditDialog) {
			getCoworkerData();
		}
	});

	// Funktion zum Abrufen der Mitarbeiterdaten
	const getCoworkerData = async () => {
		try {
			const response = await fetch(`/api/v1/users/employees/`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			});
			if (!response.ok) {
				throw new Error(`Error fetching employee data: ${response.status}`);
			}
			const responseData = await response.json();
			const dataArray: Employee[] = Object.values(responseData);
			coworkerData = dataArray;
		} catch (error) {
			console.error('Error during fetching employee data:', error);
		}
	};

	// Referenz für den Trigger-Button
	let triggerRef: HTMLButtonElement = null!;

	// Mapping für Status-Enum zu numerischen Werten
	const statusMap: Record<StatusEnum, number> = {
		[StatusEnum.Contacted]: 0,
		[StatusEnum.NotContacted]: 1,
		[StatusEnum.WaitingForCallback]: 2,
		[StatusEnum.Interested]: 3
	};

	// Funktion zur Umwandlung eines Enums in ein Array
	export function getEnumAsArray(enumType: any): { key: string; value: number }[] {
		return Object.keys(enumType)
			.filter((key) => isNaN(Number(key)))
			.map((key) => ({ key, value: enumType[key] }));
	}

	// Abgeleiteter Wert für den ausgewählten Status
	const selectedStatus = $derived(
		statuses.find((status) => status.value === contactState)?.label ?? 'customers.selectStatus'
	);

	// Abgeleiteter Wert für den Namen des ausgewählten Managers
	const selectedManagerName = $derived(
		selectedManager
			? `${selectedManager.name} ${selectedManager.surname}`
			: $t('customers.selectManager')
	);

	// Abgeleiteter Wert zur Überprüfung, ob das Formular gültig ist
	const isFormValid = $derived(
		name && surname && email && phoneNumber && address &&
		company && contactState !== null && selectedManager !== null
	);

	// Funktion zum Löschen eines Kunden
	const deleteCustomer = async () => {
		try {
			const response = await fetch(`/api/v1/customers/${row?.id}`, {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			});
			if (!response.ok) {
				toast.error("Couldn't delete customer", {
					description: `Error deleting customer: ${response.status}`
				});
				throw new Error(`Error deleting customer: ${response.status}`);
			}
			openDeleteDialog = false;
			toast.warning('Customer has been deleted', {
				description: 'This cannot be undone.'
			});
		} catch (error) {
			console.error('Error during deletion of customer:', error);
		}
	};

	// Funktion zum Bearbeiten eines Kunden
	const editCustomer = async (customerId: string) => {
		try {
			if (!isFormValid) {
				toast.warning('All fields are required', {
					description: 'All fields need to be filled in to edit a customer.'
				});
				return;
			}

			const mappedStatus = statusMap[contactState] ?? 0;
			const res = await fetch(`/api/v1/customers/${customerId}`, {
				method: 'PATCH',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					name,
					surname,
					email,
					phoneNumber,
					address,
					company,
					contactStatus: mappedStatus,
					assignedManagerId: selectedManager?.id // Einbeziehen der ID des ausgewählten Managers
				}),
				credentials: 'include'
			});

			if (!res.ok) {
				toast.error('Customer update failed!', {
					description: `Error updating customer: ${res.status}`
				});
				throw new Error('Failed to update customer');
			}

			openEditDialog = false;
			toast.success('Customer updated', {
				description: `${name} ${surname} has been updated.`
			});
		} catch (error) {
			console.error('Error updating customer:', error);
		}
	};
</script>

<!-- UI-Struktur -->
<Dialog.Root open={openDeleteDialog} onOutsideClick={() => openDeleteDialog = false}>
	<!-- Dropdown-Menü für Aktionen -->
	<DropdownMenu.Root>
		<DropdownMenu.Trigger class={cn(buttonVariants({ variant: "ghost" }), "data-[state=open]:bg-primary flex h-8 w-8 p-0")}>
			<DotsHorizontal class="h-4 w-4" />
			<span class="sr-only">{$t('customers.openMenu')}</span>
		</DropdownMenu.Trigger>
		<DropdownMenu.Content class="w-[160px] bg-background" align="end">
			<!-- Menüpunkt für Details -->
			<DropdownMenu.Item on:click={() => goto(`/dashboard/transactions/details?id=${customer.id}`)}>
				{$t('customers.details')}
			</DropdownMenu.Item>
			<DropdownMenu.Separator />
			<!-- Menüpunkt für Bearbeiten -->
			<DropdownMenu.Item on:click={() => openEditDialog = true}>
				{$t('customers.edit')}
			</DropdownMenu.Item>
			<DropdownMenu.Separator />
			<!-- Menüpunkt für Löschen -->
			<Dialog.Trigger on:click={() => openDeleteDialog = true} class="w-full">
				<DropdownMenu.Item>
					{$t('customers.delete')}
					<DropdownMenu.Shortcut>⌘⌫</DropdownMenu.Shortcut>
				</DropdownMenu.Item>
			</Dialog.Trigger>
		</DropdownMenu.Content>
	</DropdownMenu.Root>

	<!-- Dialogfeld für das Löschen eines Kunden -->
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>{$t('customers.deletingCustomer')}</Dialog.Title>
			<Dialog.Description>
				{$t('customers.deletionConfirmation')}
			</Dialog.Description>
		</Dialog.Header>
		<Dialog.Footer>
			<Button variant="destructive" size="sm" class="ml-auto h-8 flex" on:click={deleteCustomer}>
				<Delete class="mr-2 h-4 w-4" />
				{$t('customers.delete')}
			</Button>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>

<!-- Dialogfeld für das Bearbeiten eines Kunden -->
<Dialog.Root open={openEditDialog} onOutsideClick={() => openEditDialog = false}>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>{$t('customers.editACustomer')}</Dialog.Title>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<!-- Eingabefeld für den zugewiesenen Manager -->
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="assignedManager" class="text-right">{$t('customers.assignedManager')}</Label>
				<Popover.Root bind:open={openManager}>
					<Popover.Trigger ref={triggerRef} class="col-span-3 h-full" id="assignedManager">
						<Button
							variant="outline"
							class="w-full h-full justify-between"
							role="combobox"
							aria-expanded={openManager}
						>
							{selectedManagerName}
							<ChevronsUpDown class="opacity-50" />
						</Button>
					</Popover.Trigger>
					<Popover.Content class="w-[250px] p-0">
						<Command.Root class="bg-background">
							<Command.Input placeholder={$t('customers.searchManager')} />
							<Command.List>
								<Command.Empty>{$t('customers.noManagerFound')}</Command.Empty>
								<Command.Group>
									{#each coworkerData as employee}
										<Command.Item
											value={`${employee.name} ${employee.surname}`}
											onSelect={() => {
												selectedManager = employee;
												openManager = false;
											}}
										>
											<Check
												class={cn(
													selectedManager?.id !== employee.id && 'text-transparent'
												)}
											/>
											{employee.name} {employee.surname}
										</Command.Item>
									{/each}
								</Command.Group>
							</Command.List>
						</Command.Root>
					</Popover.Content>
				</Popover.Root>
			</div>
			<!-- Eingabefelder für Kundeninformationen -->
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="name" class="text-right">{$t('customers.name')}</Label>
				<Input id="name" class="col-span-3" bind:value={name} />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="lastname" class="text-right">{$t('customers.lastname')}</Label>
				<Input id="lastname" class="col-span-3" bind:value={surname} />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="email" class="text-right">{$t('customers.email')}</Label>
				<Input id="email" class="col-span-3" bind:value={email} />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="phoneNumber" class="text-right">{$t('customers.phoneNumber')}</Label>
				<Input id="phoneNumber" class="col-span-3" bind:value={phoneNumber} />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="address" class="text-right">{$t('customers.address')}</Label>
				<Input id="address" class="col-span-3" bind:value={address} />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="company" class="text-right">{$t('customers.company')}</Label>
				<Input id="company" class="col-span-3" bind:value={company} />
			</div>
			<!-- Dropdown für den Kontaktstatus -->
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="contactState" class="text-right">{$t('customers.contactStatus')}</Label>
				<Popover.Root bind:open={openContact}>
					<Popover.Trigger ref={triggerRef} class="col-span-3 h-full" id="contactState">
						<Button
							variant="outline"
							class="w-full h-full justify-between"
							role="combobox"
							aria-expanded={openContact}
						>
							{$t(selectedStatus)}
							<ChevronsUpDown class="opacity-50" />
						</Button>
					</Popover.Trigger>
					<Popover.Content class="w-[200px] p-0">
						<Command.Root class="bg-background">
							<Command.Input placeholder={$t('customers.searchStatus')} />
							<Command.List>
								<Command.Empty>{$t('customers.noStatusFound')}</Command.Empty>
								<Command.Group>
									{#each statuses as status}
										<Command.Item
											value={status.value}
											onSelect={() => {
												contactState = status.value as StatusEnum;
												openContact = false;
											}}
										>
											<Check
												class={cn(contactState !== status.value && 'text-transparent')}
											/>
											{$t(status.label)}
										</Command.Item>
									{/each}
								</Command.Group>
							</Command.List>
						</Command.Root>
					</Popover.Content>
				</Popover.Root>
			</div>
		</div>
		<!-- Schaltfläche zum Speichern der Änderungen -->
		<Dialog.Footer>
			<Button
				variant="default"
				size="sm"
				class="ml-auto h-8 flex"
				disabled={!isFormValid}
				on:click={() => editCustomer(customer.id)}
			>
				<PlusCircled class="mr-2 h-4 w-4" />
				{$t('customers.editCustomer')}
			</Button>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>