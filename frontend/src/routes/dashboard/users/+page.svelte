<script lang="ts">
	// Importe
	import { onMount } from 'svelte';
	import * as Tabs from "$lib/components/ui/tabs/index.js";
	import * as Card from "$lib/components/ui/card/index.js";
	import { Badge } from "$lib/components/ui/badge/index.js";
	import * as Table from "$lib/components/ui/table/index.js";
	import * as Dialog from "$lib/components/ui/dialog/index.js";
	import { Button } from "$lib/components/ui/button/index.js";
	import { Input } from "$lib/components/ui/input/index.js";
	import { Label } from "$lib/components/ui/label/index.js";
	import { t } from 'svelte-i18n';

	// Typdefinitionen
	type User = {
		id: string;
		name: string;
		surname: string;
		email: string;
		role: 'EMPLOYEE' | 'ADMIN';
		passwordHash?: string;
		darkMode?: boolean;
		languageGerman?: boolean;
	};

	type Customer = {
		id: string;
		name: string;
		surname: string;
		email: string;
		phoneNumber?: string;
		address?: string;
		company?: string;
		contactStatus?: string;
		assignedManager: User;
		notes?: any[];
	};

	type FilteredItem = 
		| { type: 'user'; id: string; name: string; surname: string; email: string; role: 'EMPLOYEE' | 'ADMIN' }
		| { type: 'customer'; id: string; name: string; surname: string; email: string; company?: string; contactStatus?: string; assignedManager: User };

	// Zustandsvariablen
	let users = $state<User[]>([]);
	let customers = $state<Customer[]>([]);
	let activeTab = $state<'all' | 'employees' | 'customers'>('all');
	let isAddUserDialogOpen = $state(false);
	let newUser = $state({
		name: '',
		surname: '',
		email: '',
		password: '',
		role: 'EMPLOYEE' as 'EMPLOYEE' | 'ADMIN',
	});

	// Funktionen
	async function fetchUsers() {
		try {
			const response = await fetch('/api/v1/users/', {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json',
				},
				credentials: 'include',
			});

			if (!response.ok) {
				throw new Error(`Error fetching users: ${response.status}`);
			}

			const data = await response.json();
			users = data;
			console.log('Fetched users:', users);
		} catch (error) {
			console.error('Error fetching users:', error);
		}
	}

	async function fetchCustomers() {
		try {
			const response = await fetch('/api/v1/customers/', {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json',
				},
				credentials: 'include',
			});

			if (!response.ok) {
				throw new Error(`Error fetching customers: ${response.status}`);
			}

			const data = await response.json();
			customers = data;
			console.log('Fetched customers:', customers);
		} catch (error) {
			console.error('Error fetching customers:', error);
		}
	}

	function getFilteredData() {
		if (activeTab === 'all') {
			return [
				...users.map(user => ({ ...user, type: 'user' as const })),
				...customers.map(customer => ({ ...customer, type: 'customer' as const }))
			];
		} else if (activeTab === 'employees') {
			return users
				.filter(user => user.role === 'EMPLOYEE')
				.map(user => ({ ...user, type: 'user' as const }));
		} else {
			return customers.map(customer => ({ ...customer, type: 'customer' as const }));
		}
	}

	async function addUser() {
		try {
			const response = await fetch('/api/v1/auth/register', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				credentials: 'include',
				body: JSON.stringify(newUser),
			});

			if (!response.ok) {
				throw new Error(`Error adding user: ${response.status}`);
			}

			await fetchUsers();
			isAddUserDialogOpen = false;
			newUser = { name: '', surname: '', email: '', password: '', role: 'EMPLOYEE' };
		} catch (error) {
			console.error('Error adding user:', error);
		}
	}

	// Lifecycle-Hook
	onMount(() => {
		fetchUsers();
		fetchCustomers();
	});
</script>

<!-- UI-Struktur -->
<main class="p-6">
	<!-- Titel und Beschreibung -->
	<div class="mb-6 flex justify-between items-center">
		<div>
			<h1 class="text-2xl font-bold">{$t('users.title')}</h1>
			<p class="text-sm text-muted-foreground">
				{$t('users.desc')}
			</p>
		</div>
		<Button on:click={() => isAddUserDialogOpen = true}>
			Add User
		</Button>
	</div>

	<!-- Dialog zum Hinzufügen eines Benutzers -->
	<Dialog.Root bind:open={isAddUserDialogOpen}>
		<Dialog.Content>
			<Dialog.Header>
				<Dialog.Title>Add New User</Dialog.Title>
				<Dialog.Description>
					Fill out the form below to create a new user.
				</Dialog.Description>
			</Dialog.Header>
			<div class="space-y-4">
				<div class="space-y-2">
					<Label for="name">Name</Label>
					<Input id="name" bind:value={newUser.name} placeholder="Enter name" />
				</div>
				<div class="space-y-2">
					<Label for="surname">Surname</Label>
					<Input id="surname" bind:value={newUser.surname} placeholder="Enter surname" />
				</div>
				<div class="space-y-2">
					<Label for="email">Email</Label>
					<Input id="email" bind:value={newUser.email} placeholder="Enter email" />
				</div>
				<div class="space-y-2">
					<Label for="password">Password</Label>
					<Input id="password" type="password" bind:value={newUser.password} placeholder="Enter password" />
				</div>
				<div class="space-y-2">
					<Label>Role</Label>
					<div class="grid grid-cols-2 gap-4">
						<div 
							class={`border rounded-md p-4 cursor-pointer transition-all ${newUser.role === 'EMPLOYEE' ? 'bg-accent' : 'bg-background'}`}
							on:click={() => newUser.role = 'EMPLOYEE'}
						>
							<div class="flex justify-between items-start mb-2">
								<div class="font-medium">Employee</div>
								{#if newUser.role === 'EMPLOYEE'}
									<div class="h-5 w-5 rounded-full bg-foreground flex items-center justify-center">
										<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-background">
											<polyline points="20 6 9 17 4 12"></polyline>
										</svg>
									</div>
								{/if}
							</div>
							<div class="text-sm text-muted-foreground">Regular staff member</div>
						</div>
						
						<div 
							class={`border border-border rounded-md p-4 cursor-pointer transition-all ${newUser.role === 'ADMIN' ? 'bg-accent' : 'bg-background'}`}
							on:click={() => newUser.role = 'ADMIN'}
						>
							<div class="flex justify-between items-start mb-2">
								<div class="font-medium">Admin</div>
								{#if newUser.role === 'ADMIN'}
									<div class="h-5 w-5 rounded-full bg-foreground flex items-center justify-center">
										<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-background">
											<polyline points="20 6 9 17 4 12"></polyline>
										</svg>
									</div>
								{/if}
							</div>
							<div class="text-sm text-muted-foreground">Full system access</div>
						</div>
					</div>
				</div>
			</div>
			<Dialog.Footer>
				<Button variant="outline" on:click={() => isAddUserDialogOpen = false}>
					Cancel
				</Button>
				<Button on:click={addUser}>
					Create User
				</Button>
			</Dialog.Footer>
		</Dialog.Content>
	</Dialog.Root>

	<!-- Tabs für Alle, Mitarbeiter und Kunden -->
	<div class="mb-6">
		<Tabs.Root value={activeTab}>
			<Tabs.List class="grid w-full grid-cols-3">
				<Tabs.Trigger value="all" on:click={() => activeTab = 'all'}>{$t('users.all')}</Tabs.Trigger>
				<Tabs.Trigger value="employees" on:click={() => activeTab = 'employees'}>{$t('users.employees')}</Tabs.Trigger>
				<Tabs.Trigger value="customers" on:click={() => activeTab = 'customers'}>{$t('users.customers')}</Tabs.Trigger>
			</Tabs.List>
		</Tabs.Root>
	</div>

	<!-- Debug-Informationen -->
	{#if getFilteredData().length === 0}
		<div class="mb-4 p-4 rounded">
			<p>No data to display. Users: {users.length}, Customers: {customers.length}</p>
		</div>
	{/if}

	<!-- Tabelle für Benutzer und Kunden -->
	<Card.Root>
		<Card.Content>
			<Table.Root>
				<Table.Header>
					<Table.Row>
						<Table.Head>{$t('users.name')}</Table.Head>
						<Table.Head>{$t('users.email')}</Table.Head>
						<Table.Head>{$t('users.role')}</Table.Head>
						<Table.Head>{$t('users.company')}</Table.Head>
						<Table.Head>{$t('users.contactStatus')}</Table.Head>
						<Table.Head>{$t('users.assignedManager')}</Table.Head>
					</Table.Row>
				</Table.Header>
				<Table.Body>
					{#each getFilteredData() as item}
						<Table.Row>
							<Table.Cell>{item.name} {item.surname}</Table.Cell>
							<Table.Cell>{item.email}</Table.Cell>
							<Table.Cell>
								<Badge
									class={`${
										item.type === 'user'
											? item.role === 'EMPLOYEE'
												? 'bg-custom-blue hover:bg-custom-blue/80'
												: 'bg-custom-purple hover:bg-custom-purple/80'
											: 'bg-custom-green hover:bg-custom-green/80'
									} text-white`}
								>
									{$t(
										item.type === 'user'
											? `users.${item.role.toLowerCase()}`
											: 'users.customer'
									)}
								</Badge>
							</Table.Cell>
							<Table.Cell>{item.type === 'customer' ? item.company : 'N/A'}</Table.Cell>
							<Table.Cell>{item.type === 'customer' ? item.contactStatus : 'N/A'}</Table.Cell>
							<Table.Cell>
								{item.type === 'customer'
									? `${item.assignedManager.name} ${item.assignedManager.surname}`
									: 'N/A'}
							</Table.Cell>
						</Table.Row>
					{/each}
				</Table.Body>
			</Table.Root>
		</Card.Content>
	</Card.Root>
</main>