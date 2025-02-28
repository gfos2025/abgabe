<script lang="ts">
	// Importiere benötigte Icons, Komponenten und Funktionen
	import ArrowUpRight from 'lucide-svelte/icons/arrow-up-right';
	import CreditCard from 'lucide-svelte/icons/credit-card';
	import DollarSign from 'lucide-svelte/icons/dollar-sign';
	import { Button } from '$lib/components/ui/button/index.js';
	import * as Card from '$lib/components/ui/card/index.js';
	import { t } from 'svelte-i18n'; // i18n-Funktion für Übersetzungen
	import * as Table from '$lib/components/ui/table/index.js';
	import Barchart from '$lib/components/ui/barchart/barchart.svelte'; // Diagramm-Komponente
	import { onMount } from 'svelte'; // Lifecycle-Hook für das Mounting
	import { format, parse, differenceInHours } from "date-fns"; // Funktionen zur Datumsmanipulation
	import type { Transaction } from '$lib/types/transactions'; // Typ für Transaktionen
	import type { Customer } from '$lib/types/customer'; // Typ für Kunden
	import type { ReportData, MonthlyReport } from '$lib/types/report'; // Typen für Berichte
	import type { Appointment } from '$lib/types/appointment'; // Typ für Termine
	import { toast } from 'svelte-sonner'; // Benachrichtigungsbibliothek
	
	// Zustandsvariablen
	let transactionData:Transaction[] = $state([]); // Speichert die Transaktionsdaten
	let monthlyReports:MonthlyReport[] = $state([]); // Speichert die monatlichen Berichte
	let currentMonthReport:ReportData | null = $state(null); // Bericht für den aktuellen Monat
	let previousMonthReport:ReportData | null = $state(null); // Bericht für den vorherigen Monat
	let selectedView: 'total' | 'average' | 'transactions' = $state('total'); // Ausgewählte Ansicht im Diagramm
	let reportFor: 'total' | 'personal' = $state('total'); // Bericht für alle oder persönliche Transaktionen
	let appointments: Appointment[] = $state([]); // Speichert die Termine des Benutzers

	// Funktion zum Abrufen der Transaktionsdaten
	const getTransactionData = async () => {
		try {
			const response = await fetch(`/api/v1/transactions/`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			});
			if (!response.ok) {
				throw new Error(`Error fetching transaction data: ${response.status}`);
			}
			const responseData = await response.json();
			const transactionsArray: Transaction[] = Object.values(responseData);
			const customerPromises = transactionsArray.map(async (transaction) => {
				const customer = await getCustomerByID(transaction.customerId);
				return { ...transaction, customerName: `${customer.name} ${customer.surname}` };
			});
			const transactionsWithCustomerNames = await Promise.all(customerPromises);
			transactionData = (transactionsWithCustomerNames);
		} catch (error) {
			console.error('Error during fetching transaction data:', error);
		}
	};

	// Funktion zum Abrufen der Termine des Benutzers mit Kundendaten
	const getUserAppointmentsWithCustomerData = async () => {
		const id = localStorage?.getItem('id');
		
		try {
			const response = await fetch(`/api/v1/users/${id}/appointments`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},
			credentials: 'include'
			});
			if (!response.ok) {
			throw new Error(`Error fetching user appointments: ${response.status}`);
			}
			const appointmentsData = await response.json();
			const enhancedAppointments = await Promise.all(appointmentsData.map(async (appointment) => {
			const enhancedParticipants = await Promise.all(appointment.participants.map(async (participant) => {
				try {
				const customerData = await getCustomerByID(participant.id);
				return {
					...participant,
					customerData
				};
				} catch (error) {
				console.error(`Error fetching data for participant ${participant.id}:`, error);
				return participant;
				}
			}));
			return {
				...appointment,
				participants: enhancedParticipants
			};
			}));
			appointments = enhancedAppointments;
			
		} catch (error) {
			console.error('Error during fetching user appointments:', error);
			throw error;
		}
		};

	// Funktion zum Abrufen von Kundendaten anhand der ID
	const getCustomerByID = async (id: string): Promise<Customer> => {
		try {
			const response = await fetch(`/api/v1/customers/${id}`, {
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
			return responseData;
		} catch (error) {
			console.error('Error during fetching customer data:', error);
			throw error;
		}
	};

	// Funktion zum Abrufen des Transaktionsberichts für einen bestimmten Monat
	const getTransactionReportForMonth = async (
		startDate: string,
		endDate: string,
	): Promise<ReportData> => {
		try {
			let requestBody: { startDate: string, endDate: string, userId?: string };

			if (reportFor === 'total') {
				requestBody = { startDate, endDate };
			} else if (reportFor === 'personal') {
				const userId = localStorage.getItem('id');
				if (!userId) {
					throw new Error('User ID not found in local storage');
				}
				requestBody = { startDate, endDate, userId: userId };
			} else {
				throw new Error('Invalid report type specified');
			}

			const response = await fetch(`/api/v1/transactions/report`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(requestBody),
				credentials: 'include'
			});
			if (!response.ok) {
				throw new Error(`Error fetching report data: ${response.status}`);
			}
			const responseData = await response.json();
			return responseData;
		} catch (error) {
			console.error('Error during fetching report data:', error);
			throw error;
		}
	};

	// Funktion zum Abrufen der monatlichen Berichte
	const fetchMonthlyReports = async () => {
		const reports: MonthlyReport[] = [];
		const currentDate = new Date();
		for (let month = 0; month < 12; month++) {
			const year =
			month <= currentDate.getMonth() ? currentDate.getFullYear() : currentDate.getFullYear() - 1;
			const start_date = new Date(year, month, 1).toISOString();
			const end_date = new Date(year, month + 1, 0, 23, 59, 59).toISOString();
			const reportData = await getTransactionReportForMonth(start_date, end_date);
			reports.push({
			month: new Date(year, month).toLocaleString('default', { month: 'long' }),
			reportData
			});
		}
		monthlyReports = reports;
		const currentMonthIndex = currentDate.getMonth();
		currentMonthReport = reports[currentMonthIndex].reportData;
		previousMonthReport = reports[currentMonthIndex - 1]?.reportData || null;
	};

	// Funktion zur Bestimmung der Rahmenfarbe basierend auf der Zeit bis zum Termin
	function getBorderColor(startTime: string): string {
    const appointmentDate = parse(startTime, 'dd.MM.yyyy HH:mm', new Date());
    const now = new Date();
    const hoursUntilAppointment = differenceInHours(appointmentDate, now);
    
    if (hoursUntilAppointment < 1) {
		toast.warning(`Appointment is starting soon!`, {
					description: `The appointment is scheduled to start at ${format(appointmentDate, 'HH:mm')}.`,
					duration: 5000, 
					action: {
						label: 'Dismiss',
						onClick: () => { /* Optional: Add action if needed */ }
					}
				});
      return 'border-custom-red';
    } else if (hoursUntilAppointment < 3) {
      return 'border-custom-amber';
    } else {
      return 'border-custom-green';
    }
  }

	// Lifecycle-Hook, der nach dem Mounting der Komponente ausgeführt wird
	onMount(() => {
			fetchMonthlyReports();
			getTransactionData();
			getUserAppointmentsWithCustomerData();
	});

	// Funktion zur Berechnung der prozentualen Veränderung
	const calculatePercentageChange = (current: number, previous: number | null): string => {
		if (previous === null || previous === 0) return '+0%';
		const change = ((current - previous) / previous) * 100;
		return change >= 0 ? `+${change.toFixed(1)}%` : `${change.toFixed(1)}%`;
	};

	// Funktion zum Wechseln der Diagrammansicht
	const toggleView = (view: 'total' | 'average' | 'transactions') => {
		selectedView = view;
	};

	// Funktion zum Wechseln zwischen Gesamt- und persönlichen Berichten
	const toggleFor = (reportType: 'total' | 'personal') => {
		reportFor = reportType;
		fetchMonthlyReports();
	};
</script>

<!-- Hauptcontainer für das Dashboard -->
<div class="flex min-h-screen w-full flex-col">
	<main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-8">
		<!-- Schaltflächen zum Wechseln zwischen Gesamt- und persönlichen Berichten -->
		<div class="flex flex-row justify-end space-x-2">
			<Button on:click={() => toggleFor('total')} variant="outline" size="sm">
				{$t('dashboard.total')}
			</Button>
			<Button on:click={() => toggleFor('personal')} variant="outline" size="sm">
				{$t('dashboard.personal')}
			</Button>
		</div>

		<!-- Karten für die wichtigsten Kennzahlen -->
		<div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-3">
			<!-- Karte für den Gesamtumsatz -->
			<Card.Root
				class="hover:bg-secondary hover:scale-105 shadow-lg shadow-transparent hover:shadow-secondary transition-all duration-300 group"
			>
				<Card.Header class="flex flex-row items-center justify-between space-y-0 pb-2">
					<Card.Title class="text-sm font-medium">{$t('dashboard.totalRevenue')}</Card.Title>
					<DollarSign
						class="text-muted-foreground group-hover:text-primary transition-colors duration-300 h-4 w-4"
					/>
				</Card.Header>
				<Card.Content>
					<div class="text-2xl font-bold">
						{#if currentMonthReport}
							€{currentMonthReport.totalRevenue.toFixed(2)}
						{:else}
							Loading...
						{/if}
					</div>
					<p class="text-custom-green text-xs">
						{#if currentMonthReport && previousMonthReport}
							{calculatePercentageChange(
								currentMonthReport?.totalRevenue,
								previousMonthReport?.totalRevenue
							)}
						{:else}
							N/A
						{/if}
						{$t('dashboard.fromLastMonth')}
					</p>
				</Card.Content>
			</Card.Root>

			<!-- Karte für die Anzahl der Transaktionen -->
			<Card.Root
				class="hover:bg-secondary hover:scale-105 shadow-lg shadow-transparent hover:shadow-secondary transition-all duration-300 group"
			>
				<Card.Header class="flex flex-row items-center justify-between space-y-0 pb-2">
					<Card.Title class="text-sm font-medium">{$t('dashboard.transactions')}</Card.Title>
					<CreditCard
						class="text-muted-foreground group-hover:text-primary transition-colors duration-300 h-4 w-4"
					/>
				</Card.Header>
				<Card.Content>
					<div class="text-2xl font-bold">
						{#if currentMonthReport}
							{currentMonthReport.totalTransactions}
						{:else}
							Loading...
						{/if}
					</div>
					<p class="text-custom-green text-xs">
						{#if currentMonthReport && previousMonthReport}
							{calculatePercentageChange(
								currentMonthReport.totalTransactions,
								previousMonthReport.totalTransactions
							)}
						{:else}
							N/A
						{/if}
						{$t('dashboard.fromLastMonth')}
					</p>
				</Card.Content>
			</Card.Root>

			<!-- Karte für den durchschnittlichen Transaktionsbetrag -->
			<Card.Root
				class="hover:bg-secondary hover:scale-105 shadow-lg shadow-transparent hover:shadow-secondary transition-all duration-300 group"
			>
				<Card.Header class="flex flex-row items-center justify-between space-y-0 pb-2">
					<Card.Title class="text-sm font-medium">{$t('dashboard.averageTransaction')}</Card.Title>
					<CreditCard
						class="text-muted-foreground group-hover:text-primary transition-colors duration-300 h-4 w-4"
					/>
				</Card.Header>
				<Card.Content>
					<div class="text-2xl font-bold">
						{#if currentMonthReport}
							€{currentMonthReport.averageTransactionAmount.toFixed(2)}
						{:else}
							Loading...
						{/if}
					</div>
					<p class="text-custom-green text-xs">
						{#if currentMonthReport && previousMonthReport}
							{calculatePercentageChange(
								currentMonthReport.averageTransactionAmount,
								previousMonthReport.averageTransactionAmount
							)}
						{:else}
							N/A
						{/if}
						{$t('dashboard.fromLastMonth')}
					</p>
				</Card.Content>
			</Card.Root>
		</div>

		<!-- Diagramm für die monatlichen Verkaufsdaten -->
		<div class="">
			<Card.Root class="h-[25rem] lg:h-[22rem]">
				<Card.Header>
					<Card.Title class="flex flex-col sm:flex-row items-start sm:items-center justify-between">
						<span>{$t('dashboard.sales')}</span>
						<div class="flex flex-col sm:flex-row items-start sm:items-center space-y-2 sm:space-y-0 sm:space-x-2 mt-2 sm:mt-0">
							<p class="text-border font-medium text-sm sm:mr-2">
								{$t('dashboard.clickBar')}
							</p>
							<div class="flex flex-row space-x-2 sm:space-y-0 sm:space-x-2 items-center justify-center">
								<Button on:click={() => toggleView('total')} variant="outline" size="sm">
									{$t('dashboard.total')}
								</Button>
								<Button on:click={() => toggleView('average')} variant="outline" size="sm">
									{$t('dashboard.average')}
								</Button>
								<Button on:click={() => toggleView('transactions')} variant="outline" size="sm">
									{$t('transactions.title')}
								</Button>
							</div>
						</div>
					</Card.Title>
				</Card.Header>
				<Card.Content>
					<Barchart {monthlyReports} {selectedView} />
				</Card.Content>
			</Card.Root>
		</div>

		<!-- Tabelle für die bevorstehenden Termine und die letzten Transaktionen -->
		<div class="grid gap-4 md:gap-8 lg:grid-cols-2 xl:grid-cols-8">
			<!-- Tabelle für bevorstehende Termine -->
			<Card.Root class="xl:col-span-5 max-h-[22rem] overflow-y-auto">
				<Card.Header>
					<Card.Title>{$t('dashboard.upcomingAppointments')}</Card.Title>
				</Card.Header>
				<Card.Content>
					<Table.Root>
						<Table.Header>
							<Table.Row>
								<Table.Head>{$t('dashboard.date')}</Table.Head>
								<Table.Head>{$t('dashboard.name')}</Table.Head>
								<Table.Head>{$t('dashboard.description')}</Table.Head>
							</Table.Row>
						</Table.Header>
						<Table.Body>
							{#each appointments
								.filter(appointment => {
									const startTime = parse(appointment.startTime, "dd.MM.yyyy HH:mm:ss", new Date());
									return startTime > new Date();
								})
								.sort((a, b) => {
									const dateA = parse(a.startTime, "dd.MM.yyyy HH:mm:ss", new Date());
									const dateB = parse(b.startTime, "dd.MM.yyyy HH:mm:ss", new Date());
									return dateA.getTime() - dateB.getTime();
								}) as appointment}
								<Table.Row>
									<Table.Cell class={`border-l-4 ${getBorderColor(format(parse(appointment.startTime, "dd.MM.yyyy HH:mm:ss", new Date()), "dd.MM.yyyy HH:mm"))}`}>
										{format(parse(appointment.startTime, "dd.MM.yyyy HH:mm:ss", new Date()), "dd.MM.yyyy HH:mm")}
									</Table.Cell>
									<Table.Cell>
										{#each appointment.participants as participant, index}
											{participant.name}{index < appointment.participants.length - 1 ? ', ' : ''}
										{/each}
									</Table.Cell>
									<Table.Cell>{appointment.description}</Table.Cell>
								</Table.Row>
							{/each}
						</Table.Body>
					</Table.Root>
				</Card.Content>
			</Card.Root>

			<!-- Tabelle für die letzten Transaktionen -->
			<Card.Root class="xl:col-span-3 max-h-[22rem] overflow-y-auto">
				<Card.Header class="flex flex-row items-center">
					<div class="grid gap-2">
						<Card.Title>{$t('dashboard.recentTransactions')}</Card.Title>
						<Card.Description>{$t('dashboard.recentTransactionsDesc')}</Card.Description>
					</div>
					<Button href="/dashboard/transactions" size="sm" class="ml-auto gap-1 group">
						{$t('dashboard.viewAll')}
						<ArrowUpRight
							class="h-4 w-4 group-hover:rotate-45 transition-all duration-150 ease-in"
						/>
					</Button>
				</Card.Header>
				<Card.Content>
					<Table.Root>
						<Table.Header>
							<Table.Row>
								<Table.Head>{$t('dashboard.customer')}</Table.Head>
								<Table.Head class="text-right">{$t('dashboard.amount')}</Table.Head>
							</Table.Row>
						</Table.Header>
						<Table.Body>
							{#each transactionData as transaction}
								<Table.Row>
									<Table.Cell>{transaction.customerName}</Table.Cell>
									<Table.Cell class="text-right">€{transaction.amount}</Table.Cell>
								</Table.Row>
							{/each}
						</Table.Body>
					</Table.Root>
				</Card.Content>
			</Card.Root>
		</div>
	</main>
</div>