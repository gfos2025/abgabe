<script lang="ts">
	// Importe von Lifecycle-Hooks, UI-Komponenten und Funktionen
	import { onMount } from 'svelte'; // Lifecycle-Hook
	import * as Card from '$lib/components/ui/card/index.js'; // Card-Komponente
	import { Badge } from '$lib/components/ui/badge/index.js'; // Badge-Komponente
	import { t } from 'svelte-i18n'; // Internationalisierungsfunktion
	import { browser } from '$app/environment'; // Prüfung, ob der Code im Browser läuft

	// Typdefinition für eine Transaktion
	type Transaction = {
		id: string;
		customerId: string;
		userId: string;
		amount: number;
		timestamp: string;
		status: string;
		description: string;
	};

	// Typdefinition für einen Kunden
	type Customer = {
		id: string;
		name: string;
		surname: string;
		email: string;
		phoneNumber?: string;
		address?: string;
	};

	// Zustandsvariablen für Transaktionen und Kunden
	let transactions: Transaction[] = $state([]); // Liste der Transaktionen
	let customer: Customer | null = $state(null); // Kundeninformationen

	// Kunden-ID aus dem URL-Parameter
	let customerId: string | null = $state(null);

	// Funktion zum Abrufen der Transaktionen für den Kunden
	async function fetchTransactions() {
		if (!customerId) return;

		try {
			const response = await fetch(`/api/v1/transactions?customer_id=${customerId}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json',
				},
				credentials: 'include',
			});

			if (!response.ok) {
				throw new Error(`Error fetching transactions: ${response.status}`);
			}

			const data = await response.json();
			// Sortiere die Transaktionen nach dem neuesten Datum
			transactions = Object.values(data).sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
		} catch (error) {
			console.error('Error fetching transactions:', error);
		}
	}

	// Funktion zum Abrufen der Kundeninformationen
	async function fetchCustomer() {
		if (!customerId) return;

		try {
			const response = await fetch(`/api/v1/customers/${customerId}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json',
				},
				credentials: 'include',
			});

			if (!response.ok) {
				throw new Error(`Error fetching customer: ${response.status}`);
			}

			const data = await response.json();
			customer = data;
		} catch (error) {
			console.error('Error fetching customer:', error);
		}
	}

	// Abgeleitete Werte für die Zusammenfassung der Transaktionen
	let totalSpent = $derived(
		transactions
			.filter((t) => t.status === 'Completed') // Filtere abgeschlossene Transaktionen
			.reduce((sum, t) => sum + t.amount, 0) // Summiere die Beträge
	);
	let totalTransactions = $derived(transactions.length); // Gesamtanzahl der Transaktionen
	let pendingTransactions = $derived(
		transactions.filter((t) => t.status === 'Pending').length // Anzahl der ausstehenden Transaktionen
	);

	// Lifecycle-Hook: Wird beim Laden der Komponente ausgeführt
	onMount(() => {
		// Nur im Browser ausführen
		if (browser) {
			const urlParams = new URLSearchParams(window.location.search);
			customerId = urlParams.get('id'); // Hole die Kunden-ID aus der URL
			fetchTransactions(); // Lade die Transaktionen
			fetchCustomer(); // Lade die Kundeninformationen
		}
	});
</script>

<!-- UI-Struktur -->
<div class="lg:p-8">
	<!-- Kundeninformationen -->
	{#if customer}
		<div class="mb-8">
			<h1 class="text-2xl font-bold mb-4">{$t('transactions.customerDetails')}</h1>
			<Card.Root>
				<Card.Header>
					<Card.Title>{customer.name} {customer.surname}</Card.Title>
					<Card.Description>{customer.email}</Card.Description>
				</Card.Header>
				<Card.Content>
					<div class="space-y-2">
						<p class="text-sm text-muted-foreground">
							<strong>{$t('customers.phoneNumber')}:</strong> {customer.phoneNumber || 'N/A'}
						</p>
						<p class="text-sm text-muted-foreground">
							<strong>{$t('customers.address')}:</strong> {customer.address || 'N/A'}
						</p>
					</div>
				</Card.Content>
			</Card.Root>
		</div>
	{/if}

	<!-- Zusammenfassung der Transaktionen -->
	<div class="mb-8">
		<h1 class="text-2xl font-bold mb-4">{$t('transactions.transactionHistory')}</h1>
		<div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
			<!-- Gesamtausgaben -->
			<Card.Root>
				<Card.Header>
					<Card.Title>{$t('transactions.totalSpent')}</Card.Title>
				</Card.Header>
				<Card.Content>
					<p class="text-2xl font-bold">€{totalSpent.toFixed(2)}</p>
				</Card.Content>
			</Card.Root>
			<!-- Gesamttransaktionen -->
			<Card.Root>
				<Card.Header>
					<Card.Title>{$t('transactions.totalTransactions')}</Card.Title>
				</Card.Header>
				<Card.Content>
					<p class="text-2xl font-bold">
						{totalTransactions} <span class="text-sm text-muted-foreground">({pendingTransactions} pending)</span>
					</p>
				</Card.Content>
			</Card.Root>
		</div>
	</div>

	<!-- Transaktionsliste -->
	<div class="space-y-4">
		{#each transactions as transaction}
			<Card.Root>
				<Card.Header>
					<Card.Title>Transaction #{transaction.id}</Card.Title>
					<Card.Description>{transaction.description}</Card.Description>
				</Card.Header>
				<Card.Content>
					<div class="space-y-2">
						<!-- Betrag der Transaktion -->
						<p class="text-sm text-muted-foreground">
							<strong>{$t('transactions.amount')}:</strong> €{transaction.amount.toFixed(2)}
						</p>
						<!-- Status der Transaktion -->
						<p class="text-sm text-muted-foreground">
							<strong>{$t('transactions.status')}:</strong>
							<Badge
								class={`ml-2 ${
									transaction.status === $t('status.completed')
										? 'bg-custom-green hover:bg-custom-green/80'
										: transaction.status === $t('status.pending')
											? 'bg-custom-yellow hover:bg-custom-yellow/80'
											: transaction.status === $t('status.failed')
												? 'bg-custom-red hover:bg-custom-red/80'
												: ''
								}`}
								variant="secondary"
							>
								{transaction.status}
							</Badge>
						</p>
						<!-- Datum der Transaktion -->
						<p class="text-sm text-muted-foreground">
							<strong>{$t('transactions.date')}:</strong> {transaction.timestamp}
						</p>
					</div>
				</Card.Content>
			</Card.Root>
		{/each}
	</div>
</div>