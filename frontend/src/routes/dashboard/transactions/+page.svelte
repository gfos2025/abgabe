<script lang="ts">
	// Importe von Icons und Komponenten
	import ListFilter from 'lucide-svelte/icons/list-filter';
	import PlusCircled from 'svelte-radix/PlusCircled.svelte';
	import ChevronsUpDown from 'lucide-svelte/icons/chevrons-up-down';
	import Check from 'lucide-svelte/icons/check';
	import Ellipsis from 'lucide-svelte/icons/ellipsis'
	import { Edit, History } from 'lucide-svelte';

	import { Badge } from '$lib/components/ui/badge/index.js';
	import * as Sheet from "$lib/components/ui/sheet/index.js";
	import { buttonVariants } from "$lib/components/ui/button/index.js";
	import { Button } from '$lib/components/ui/button/index.js';
	import * as Card from '$lib/components/ui/card/index.js';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js';
	import * as Table from '$lib/components/ui/table/index.js';
	import { onMount } from 'svelte';
	import { TransactionStatus, type Transaction } from '$lib/types/transactions';
	import type { Customer } from '$lib/types/customer';
	import * as Command from '$lib/components/ui/command/index.js';
	import * as Popover from '$lib/components/ui/popover/index.js';
	import Input from '$lib/components/ui/input/input.svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import Label from '$lib/components/ui/label/label.svelte';
	import { Textarea } from "$lib/components/ui/textarea/index.js";
	import { toast } from 'svelte-sonner';
	import { cn } from '$lib/utils.js';
	import { t } from 'svelte-i18n';

	// Zustandsvariablen
	let selectedCustomer: Customer | null = $state(null);
	let amount:number = $state(0.0);
	let description:string = $state('');
	let transactionStatus:TransactionStatus = $state(TransactionStatus.Pending);
	let openTransactionStatus:boolean = $state(false);
	let openCustomer:boolean = $state(false);
	let openCreateDialog:boolean = $state(false);
	let transactionData:Transaction[] = $state([]);
	let customerData:Customer[] = $state([]);
	let isSheetOpen = $state(false);
	let selectedTransaction: Transaction | null = $state(null);

	// Funktionen
	function handleEdit(transaction: Transaction) {
		selectedTransaction = { ...transaction };
		isSheetOpen = true;
	}

	function handleHistory(transaction: Transaction) {
		window.location.href = `transactions/details?id=${transaction.customerId}`;
	}

	export function getEnumAsArray(enumType: any): { key: string; value: number }[] {
		return Object.keys(enumType)
			.filter((key) => isNaN(Number(key)))
			.map((key) => ({ key, value: enumType[key] }));
	}

	const isFormValid:boolean = $derived(selectedCustomer && amount);

	const mappedTransactionData = $derived.by(() => {
		return transactionData.map((transaction) => {
			const customer = customerData.find((cust) => cust?.id === transaction.customerId);
			return {
				...transaction,
				customer: customer || null
			};
		});
	});

	async function saveEditedTransaction() {
		try {
			if (!selectedTransaction) return;

			const res = await fetch(`/api/v1/transactions/${selectedTransaction.id}`, {
				method: 'PATCH',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					amount: selectedTransaction.amount,
					description: selectedTransaction.description,
					status: selectedTransaction.status,
				}),
				credentials: 'include',
			});

			if (!res.ok) {
				toast.error('Failed to update transaction', {
					description: `Error: ${res.status}`,
				});
				throw new Error('Failed to update transaction');
			}

			isSheetOpen = false;
			toast.success('Transaction updated', {
				description: 'The transaction details have been successfully updated.',
			});
			await getTransactionData();
		} catch (error) {
			console.error('Error updating transaction:', error);
		}
	}

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
			const dataArray: Transaction[] = Object.values(responseData);
			transactionData = dataArray;
		} catch (error) {
			console.error('Error during fetching transaction data:', error);
		}
	};

	const createTransaction = async () => {
		try {
			const toLocalISOString = (date: Date) => {
				const offset = date.getTimezoneOffset();
				const adjustedDate = new Date(date.getTime() - offset * 60 * 1000);
				return adjustedDate.toISOString().replace('Z', '');
			};
			if (!isFormValid) {
				toast.warning('All fields are required', {
					description: 'All fields need to be filled in to add a Transaction.',
				});
				return;
			}
			const currentDateTime = toLocalISOString(new Date());
			const res = await fetch(`/api/v1/transactions`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					userId: localStorage.getItem('id'),
					customerId: selectedCustomer?.id,
					amount,
					description,
					timestamp: currentDateTime,
					status: transactionStatus,
				}),
				credentials: 'include',
			});

			if (!res.ok) {
				toast.error('Transaction creation failed!', {
					description: `"Error adding Transaction:", ${res.status}`,
				});
				throw new Error('Failed to add Transaction');
			}

			openCreateDialog = false;
			toast.success('Transaction created', {
				description: `${selectedCustomer?.name} ${selectedCustomer?.surname}'s transaction has been added.`,
			});
			selectedCustomer = null;
			amount = 0.0;
			description = '';
			transactionStatus = TransactionStatus.Pending;

			console.log('Transaction added successfully.');
		} catch (error) {
			console.error('Error adding Transaction:', error);
		}
	};

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
			customerData = dataArray;
		} catch (error) {
			console.error('Error during fetching customer data:', error);
		}
	};

	// Lifecycle-Hooks
	onMount(() => {
		getCustomerData();
		getTransactionData();
	});
</script>

<!-- UI-Struktur -->
<div class="h-full flex-1 flex-col space-y-8 p-8 flex">
	<div class="flex items-center justify-between space-y-2">
		<div>
			<h2 class="text-2xl font-bold tracking-tight">{$t('transactions.title')}</h2>
			<p class="text-muted-foreground">{$t('transactions.desc')}</p>
		</div>
		<div class="flex items-center">
			<div class="ml-auto flex items-center gap-2">
				<Dialog.Root bind:open={openCreateDialog}>
					<Dialog.Trigger>
						<Button variant="default" size="sm" class="ml-auto h-8 flex">
							<PlusCircled class="mr-2 h-4 w-4" />
							{$t('transactions.addTransaction')}
						</Button>
					</Dialog.Trigger>
					<Dialog.Content>
						<Dialog.Header>
							<Dialog.Title>{$t('transactions.addNewTransaction')}</Dialog.Title>
						</Dialog.Header>
						<div class="grid gap-4 py-4">
							<div class="grid grid-cols-4 items-center gap-4">
								<Label for="customerID" class="text-right">{$t('transactions.customer')}</Label>
								<Popover.Root bind:open={openCustomer}>
									<Popover.Trigger
										class="col-span-3 h-full"
										id="customerID"
									>
										<Button
											variant="outline"
											class="w-full h-full justify-between"
											role="combobox"
											aria-expanded={openCustomer}
										>
											{selectedCustomer
												? `${selectedCustomer.name} ${selectedCustomer.surname}`
												: $t('transactions.selectCustomer')}
											<ChevronsUpDown class="opacity-50" />
										</Button>
									</Popover.Trigger>
									<Popover.Content class="w-[200px] p-0">
										<Command.Root>
											<Command.Input placeholder={$t('transactions.searchCustomer')} />
											<Command.List>
												<Command.Empty>{$t('transactions.noCustomerFound')}</Command.Empty>
												<Command.Group>
													{#each customerData as customer}
														<Command.Item
															onSelect={() => {
																selectedCustomer = customer
																openCustomer = false;
															}}
														>
															<Check
																class={cn(selectedCustomer !== customer && 'text-transparent')}
															/>
															{customer?.name} {customer?.surname}
														</Command.Item>
													{/each}
												</Command.Group>
											</Command.List>
										</Command.Root>
									</Popover.Content>
								</Popover.Root>
							</div>
							<div class="grid grid-cols-4 items-center gap-4">
								<Label for="amount" class="text-right">{$t('transactions.amount')}</Label>
								<Input id="amount" class="col-span-3" bind:value={amount} />
							</div>
							<div class="grid grid-cols-4 items-center gap-4">
								<Label for="description" class="text-right">{$t('transactions.description')}</Label>
								<Input id="description" class="col-span-3" bind:value={description} />
							</div>
							<div class="grid grid-cols-4 items-center gap-4">
								<Label for="transactionState" class="text-right"
									>{$t('transactions.transactionState')}</Label
								>
	
								<Popover.Root bind:open={openTransactionStatus}>
									<Popover.Trigger
										class="col-span-3 h-full"
										id="transactionState"
									>
										<Button
											variant="outline"
											class="w-full h-full justify-between"
											role="combobox"
											aria-expanded={openTransactionStatus}
										>
											{transactionStatus == 0
												? $t('status.completed')
												: transactionStatus == 1
													? $t('status.pending')
													: transactionStatus == 2
														? $t('status.failed')
														: $t('transactions.selectStatus')}
											<ChevronsUpDown class="opacity-50" />
										</Button>
									</Popover.Trigger>
									<Popover.Content class="w-[200px] p-0">
										<Command.Root>
											<Command.Input
												placeholder={transactionStatus == 0
													? $t('status.completed')
													: transactionStatus == 1
														? $t('status.pending')
														: transactionStatus == 2
															? $t('status.failed')
															: $t('transactions.searchStatus')}
											/>
											<Command.List>
												<Command.Empty>{$t('transactions.noStatusFound')}</Command.Empty>
												<Command.Group>
													{#each getEnumAsArray(TransactionStatus) as status}
														<Command.Item
															onSelect={() => {
																transactionStatus = status.value;
																openTransactionStatus = false;
															}}
														>
															<Check
																class={cn(transactionStatus !== status.value && 'text-transparent')}
															/>
															{status.key}
														</Command.Item>
													{/each}
												</Command.Group>
											</Command.List>
										</Command.Root>
									</Popover.Content>
								</Popover.Root>
							</div>
						</div>
						<Dialog.Footer>
							<Button
								variant="default"
								size="sm"
								class="ml-auto h-8 flex"
								disabled={!isFormValid}
								on:click={createTransaction}
							>
								<PlusCircled class="mr-2 h-4 w-4" />
								{$t('transactions.addTransaction')}
							</Button>
						</Dialog.Footer>
					</Dialog.Content>
				</Dialog.Root>
				<DropdownMenu.Root>
					<DropdownMenu.Trigger asChild let:builder>
						<Button
							variant="outline"
							size="sm"
							class="ml-auto h-8 flex"
							builders={[builder]}
						>
							<ListFilter class="mr-2 h-4 w-4" />
							<span class="sr-only sm:not-sr-only">{$t('transactions.filter')}</span>
						</Button>
					</DropdownMenu.Trigger>
					<DropdownMenu.Content align="end">
						<DropdownMenu.Label>{$t('transactions.filterBy')}</DropdownMenu.Label>
						<DropdownMenu.Separator />
						<DropdownMenu.CheckboxItem checked>{$t('status.completed')}</DropdownMenu.CheckboxItem>
						<DropdownMenu.CheckboxItem>{$t('status.failed')}</DropdownMenu.CheckboxItem>
						<DropdownMenu.CheckboxItem>{$t('status.pending')}</DropdownMenu.CheckboxItem>
					</DropdownMenu.Content>
				</DropdownMenu.Root>
			</div>
		</div>
	</div>
	<Card.Root
		data-x-chunk-name="dashboard-05-chunk-3"
		data-x-chunk-description="A table of recent orders showing the following columns: Customer, Type, Status, Date, and Amount."
	>
		<Card.Header class="px-7">
			<Card.Title>{$t('transactions.orders')}</Card.Title>
			<Card.Description>{$t('transactions.ordersDesc')}</Card.Description>
		</Card.Header>
		<Card.Content>
			<Table.Root>
				<Table.Header>
					<Table.Row>
						<Table.Head>{$t('transactions.customer')}</Table.Head>
						<Table.Head class="hidden sm:table-cell">{$t('transactions.status')}</Table.Head>
						<Table.Head class="hidden md:table-cell">{$t('transactions.date')}</Table.Head>
						<Table.Head class="hidden md:table-cell">{$t('transactions.amount')}</Table.Head>
						<Table.Head class="text-right">More</Table.Head>
					</Table.Row>
				</Table.Header>
				<Table.Body>
					{#each mappedTransactionData as transaction}
					<DropdownMenu.Root>
						<Table.Row>
							<Table.Cell>
								<div class="font-medium">
									{transaction.customer
										? `${transaction.customer.name} ${transaction.customer.surname}`
										: $t('transactions.unknownCustomer')}
								</div>
								<div class="text-muted-foreground hidden text-sm md:inline">
									{transaction.customer ? transaction.customer.email : $t('transactions.noEmail')}
								</div>
							</Table.Cell>
							<Table.Cell class="hidden sm:table-cell">
								<Badge
									class={`text-xs ${
										transaction.status === 'Completed'
											? 'bg-custom-green hover:bg-custom-green/80'
											: transaction.status === 'Pending'
												? 'bg-custom-yellow hover:bg-custom-yellow/80'
												: transaction.status === 'Failed'
													? 'bg-custom-red hover:bg-custom-red/80'
													: ''
									}`}
									variant="secondary"
								>
									{transaction.status}
								</Badge>
							</Table.Cell>
							<Table.Cell class="hidden md:table-cell">
								{transaction.timestamp.split(' ')[0]}
							</Table.Cell>
							<Table.Cell class="hidden md:table-cell">
								${transaction.amount}
							</Table.Cell>
							<Table.Cell class="flex justify-end">
								<DropdownMenu.Trigger class={buttonVariants({ variant: "ghost", size: "icon" })}>
									<Ellipsis />
								</DropdownMenu.Trigger>
							</Table.Cell>
						</Table.Row>
						<DropdownMenu.Content class="w-auto bg-background">
							<DropdownMenu.Group>
								<DropdownMenu.Item on:click={()=> handleEdit(transaction)}>
									<Edit class="mr-2 h-4 w-4" />
									<span>Edit</span>
								</DropdownMenu.Item>
								<DropdownMenu.Separator />
								<DropdownMenu.Item on:click={() => handleHistory(transaction)}>
									<History class="mr-2 h-4 w-4" />
									<span>History</span>
								</DropdownMenu.Item>
							</DropdownMenu.Group>
						</DropdownMenu.Content>
					</DropdownMenu.Root>
					{/each}
				</Table.Body>
			</Table.Root>
		</Card.Content>
	</Card.Root>
</div>

<!-- Sheet für die Bearbeitung von Transaktionen -->
<Sheet.Root bind:open={isSheetOpen}>
	<Sheet.Content>
		<Sheet.Header>
			<Sheet.Title>Edit Transaction</Sheet.Title>
			<Sheet.Description>
				Update the transaction details here. Click save when you're done.
			</Sheet.Description>
		</Sheet.Header>

		{#if selectedTransaction}
			<div class="grid gap-4 py-4">
				<div class="grid grid-cols-4 items-center gap-4">
					<Label for="amount" class="text-right">Amount</Label>
					<Input
						id="amount"
						class="col-span-3"
						bind:value={selectedTransaction.amount}
						type="number"
					/>
				</div>

				<div class="grid grid-cols-4 items-center gap-4">
					<Label for="description" class="text-right">Description</Label>
					<Textarea
						id="description"
						class="col-span-3"
						bind:value={selectedTransaction.description}
					/>
				</div>

				<div class="grid grid-cols-4 items-center gap-4">
					<Label for="status" class="text-right">Status</Label>
					<Popover.Root bind:open={openTransactionStatus}>
						<Popover.Trigger class="col-span-3 h-full" id="status">
							<Button
								variant="outline"
								class="w-full h-full justify-between"
								role="combobox"
								aria-expanded={openTransactionStatus}
							>
								{selectedTransaction.status === 0
									? $t('status.completed')
									: selectedTransaction.status === 1
										? $t('status.pending')
										: selectedTransaction.status === 2
											? $t('status.failed')
											: $t('transactions.selectStatus')}
								<ChevronsUpDown class="opacity-50" />
							</Button>
						</Popover.Trigger>
						<Popover.Content class="w-[200px] p-0">
							<Command.Root>
								<Command.Input placeholder={$t('transactions.searchStatus')} />
								<Command.List>
									<Command.Empty>No status found.</Command.Empty>
									<Command.Group>
										{#each getEnumAsArray(TransactionStatus) as status}
											<Command.Item
												onSelect={() => {
													selectedTransaction.status = status.value;
													openTransactionStatus = false;
												}}
											>
												<Check
													class={cn(
														selectedTransaction.status === status.value ? 'opacity-100' : 'opacity-0'
													)}
												/>
												{status.key}
											</Command.Item>
										{/each}
									</Command.Group>
								</Command.List>
							</Command.Root>
						</Popover.Content>
					</Popover.Root>
				</div>
			</div>
		{/if}

		<Sheet.Footer>
			<Button on:click={saveEditedTransaction}>Save changes</Button>
		</Sheet.Footer>
	</Sheet.Content>
</Sheet.Root>