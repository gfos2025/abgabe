<script lang="ts">
	import * as Avatar from "$lib/components/ui/avatar/index.js";
	import { Button } from "$lib/components/ui/button/index.js";
	import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
	import { t } from 'svelte-i18n';

	const handleSignOut = () => {
    try {
        localStorage.clear();

        document.cookie = "JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";

        window.location.href = '/login';
    } catch (error) {
        console.error('Error during sign-out:', error);
        alert('An error occurred during logout. Please try again.');
    }
};



</script>

<DropdownMenu.Root>
	<DropdownMenu.Trigger asChild let:builder>
		<Button variant="ghost" builders={[builder]} class="relative h-8 w-8 rounded-full">
			<Avatar.Root class="h-9 w-9">
				<Avatar.Image src="https://randomuser.me/api/portraits/lego/6.jpg" alt="profile picture" />
				<Avatar.Fallback>WS</Avatar.Fallback>
			</Avatar.Root>
		</Button>
	</DropdownMenu.Trigger>
	<DropdownMenu.Content class="w-56" align="end">
		<DropdownMenu.Label class="font-normal">
			<div class="flex flex-col space-y-1">
				<p class="text-sm font-medium leading-none">{localStorage.getItem('name')}</p>
				<p class="text-muted-foreground text-xs leading-none">{localStorage.getItem('email')}</p>
			</div>
		</DropdownMenu.Label>
		<DropdownMenu.Separator />
		<DropdownMenu.Group>
			<DropdownMenu.Item href="dashboard">
				{$t('dashboard.title')}
				<DropdownMenu.Shortcut>⌘D</DropdownMenu.Shortcut>
			</DropdownMenu.Item>
			<DropdownMenu.Item href="dashboard/calendar">
				{$t('calendar.title')}
				<DropdownMenu.Shortcut>⌘C</DropdownMenu.Shortcut>
			</DropdownMenu.Item>
			<DropdownMenu.Item href="dashboard/customers">{$t('customers.title')}
				<DropdownMenu.Shortcut>⌘C</DropdownMenu.Shortcut>
			</DropdownMenu.Item>
			<DropdownMenu.Item href="dashboard/transactions">{$t('transactions.title')}
				<DropdownMenu.Shortcut>⌘T</DropdownMenu.Shortcut>
			</DropdownMenu.Item>
			<DropdownMenu.Item href="dashboard/settings">
				{$t('settings.title')}
				<DropdownMenu.Shortcut>⌘S</DropdownMenu.Shortcut>
			</DropdownMenu.Item>
		</DropdownMenu.Group>
		<DropdownMenu.Separator />
		<DropdownMenu.Item on:click={handleSignOut}>
			{$t('layout.logout')}
		</DropdownMenu.Item>
	</DropdownMenu.Content>
</DropdownMenu.Root>