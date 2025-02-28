<script lang="ts">
	import type { TableViewModel } from 'svelte-headless-table';
	import Cross2 from 'svelte-radix/Cross2.svelte';
	import Check from 'lucide-svelte/icons/check';
	import ChevronsUpDown from 'lucide-svelte/icons/chevrons-up-down';
	import PlusCircled from 'svelte-radix/PlusCircled.svelte';
	import { priorities, statuses } from '../data/data.js';
	import type { Customer } from '$lib/types/customer';
	import { StatusEnum } from '$lib/types/status';
	import { DataTableFacetedFilter, DataTableViewOptions } from './index.js';
	import Button from '$lib/components/ui/button/button.svelte';
	import Input from '$lib/components/ui/input/input.svelte';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import Label from '$lib/components/ui/label/label.svelte';
	import { cn } from '$lib/utils.js';
	import * as Command from '$lib/components/ui/command/index.js';
	import * as Popover from '$lib/components/ui/popover/index.js';
	import { toast } from 'svelte-sonner';
	import { t } from 'svelte-i18n';
	import { buttonVariants } from '$lib/components/ui/button/index.js';
	

	const { tableModel, data }: { tableModel: TableViewModel<Customer>; data: Customer[]} = $props();

	let name:string = $state('');
	let surname:string = $state('');
	let email:string = $state('');
	let phoneNumber:string = $state('');
	let address:string = $state('');
	let company:string = $state('');
	let contactState = $state(null as string | null);
	let openPopover:boolean = $state(false);
	let openCreateDialog:boolean = $state(false);
	const isFormValid = $derived(name && surname && email && phoneNumber && address && company && contactState);
	const selectedValue = $derived(statuses.find((status) => status.value === contactState)?.label ?? $t('customers.selectStatus'));


	const statusMap: Record<StatusEnum, number> = {
		[StatusEnum.Contacted]: 0,
		[StatusEnum.NotContacted]: 1,
		[StatusEnum.WaitingForCallback]: 2,
		[StatusEnum.Interested]: 3
	};

	const addCustomer = async () => {
		try {
			if (!isFormValid) {
				toast.warning('All fields are required', {
					description: 'All fields need to be filled in to add a customer.'
				});
				return;
			}
			const mappedStatus = statusMap[contactState] ?? 0;
			const res = await fetch(`/api/v1/customers`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					assignedManagerId: localStorage.getItem('id'),
					name,
					surname,
					email,
					phoneNumber,
					address,
					company,
					contactStatus: mappedStatus
				}),
				credentials: 'include'
			});
			if (!res.ok) {
				toast.error('Customer creation failed!', {
					description: `"Error adding customer:", ${res.status}`
				});
				throw new Error('Failed to add customer');
			}
			openCreateDialog = false;
			toast.success('Customer created', {
				description: `${name} ${surname} has been added as a customer.`
			});
			name = '';
			surname = '';
			email = '';
			phoneNumber = '';
			address = '';
			company = '';
			contactState = null;
		} catch (error) {
			console.error('Error adding customer:', error);
		}
	};

	const counts = $derived(
	$data.reduce<{
		status: { [index: string]: number };
		priority: { [index: string]: number };
	}>(
		(acc, { status, priority }) => {
			acc.status[status] = (acc.status[status] || 0) + 1;
			acc.priority[priority] = (acc.priority[priority] || 0) + 1;
			return acc;
		},
		{
			status: {},
			priority: {}
		}
	)
);

	const { pluginStates } = tableModel;
	const { filterValue } = pluginStates.filter;
	const { filterValues } = pluginStates.colFilter;

	const showReset = $derived(Object.values({ ...filterValues, filterValue }).some((v: unknown) => {
			if (typeof v === 'string') {
				return v.length > 0;
			}
			if (Array.isArray(v)) {
				return v.length > 0;
			}
			return false;
		}));
</script>

<div class="flex items-center justify-between">
	<div class="flex flex-1 items-center space-x-2">
		<Input
			placeholder={$t('customers.filterCustomers')}
			class="h-8 w-[150px] lg:w-[250px]"
			type="search"
			bind:value={$filterValue}
		/>
		<DataTableFacetedFilter
			bind:filterValues={$filterValues.status}
			title={$t('customers.status')}
			options={statuses}
			counts={counts.status}
		/>
		<DataTableFacetedFilter
			bind:filterValues={$filterValues.priority}
			title={$t('customers.priority')}
			options={priorities}
			counts={counts.priority}
		/>
		{#if showReset}
			<Button
				on:click={() => {
					filterValue.set('');
					filterValues.status = [];
					filterValues.priority = [];
				}}
				variant="ghost"
				class="h-8 px-2 lg:px-3"
			>
				{$t('customers.reset')}
				<Cross2 class="ml-2 h-4 w-4" />
			</Button>
		{/if}
	</div>
	<div class="flex flex-1 justify-end items-end space-x-2">
		<Dialog.Root bind:open={openCreateDialog}>
			<Dialog.Trigger
				class={cn(buttonVariants({ variant: "default", size: "sm" }), "ml-auto h-8 flex")}
				>
				<PlusCircled class="mr-2 h-4 w-4" />
				{$t('customers.addCustomer')}
			</Dialog.Trigger>
			<Dialog.Content>
				<Dialog.Header>
					<Dialog.Title>{$t('customers.addNewCustomer')}</Dialog.Title>
				</Dialog.Header>
				<div class="grid gap-4 py-4">
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
					<div class="grid grid-cols-4 items-center gap-4">
						<Label for="contactState" class="text-right">{$t('customers.contactStatus')}</Label>
						<Popover.Root bind:open={openPopover}>
							<Popover.Trigger class="col-span-3 h-full" id="contactState">
							  <Button
								variant="outline"
								class="w-full h-full justify-between"
								role="combobox"
								aria-expanded={openPopover}
							  >
								{$t(selectedValue)}
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
										  contactState = status.value;
										  openPopover = false;
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
				<Dialog.Footer>
					<Button
						variant="default"
						size="sm"
						class="ml-auto h-8 flex"
						disabled={!isFormValid}
						on:click={addCustomer}
					>
						<PlusCircled class="mr-2 h-4 w-4" />
						{$t('customers.addCustomer')}
					</Button>
				</Dialog.Footer>
			</Dialog.Content>
		</Dialog.Root>
		<DataTableViewOptions {tableModel} />
	</div>
</div>
