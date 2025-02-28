<!-- components/mobile-nav.svelte -->
<script lang="ts">
	import { Menu, X } from 'lucide-svelte';
	import { Button } from '$lib/components/ui/button/index.js';
	import { slide } from 'svelte/transition';
	import { t } from 'svelte-i18n';
	import { BarChart, Users, ArrowLeftRight, Bell, Settings, Calendar } from 'lucide-svelte';

	const icons = {
		'chart-bar': BarChart,
		calendar: Calendar,
		users: Users,
		'arrow-left-right': ArrowLeftRight,
		bell: Bell,
		settings: Settings
	};

	export let items: Array<{
		title: string;
		href: string;
		icon: string;
	}>;

	let isOpen = false;

	function toggleMenu() {
		isOpen = !isOpen;
	}

	function closeMenu() {
		isOpen = false;
	}
</script>

<div class="mobile-navigation">
	<!-- Mobile header with hamburger menu -->
	<div class="flex items-center justify-end p-4 border-b">
		<Button on:click={toggleMenu} variant='ghost' size='icon'>
				<Menu size={24} />
		</Button>
	</div>

	<!-- Mobile navigation menu -->
	{#if isOpen}
		<div transition:slide={{ duration: 200 }} class="fixed inset-0 z-50 bg-background pt-16">
			<!-- Close button -->
			<Button
				on:click={closeMenu}
				class="absolute top-8 right-8 p-0.5"
				variant='ghost'
				size='icon'
			>
				<X size={24} />
			</Button>

			<nav class="p-4">
				<ul class="space-y-2">
					{#each items as item}
						{@const Icon = icons[item.icon]}
						<li>  
							<a
								href={item.href}
								class="flex flex-row space-x-2 items-center p-3 rounded-md hover:bg-accent"
								on:click={closeMenu}
							>
								{#if Icon}
									<Icon class="w-5 h-5" />
								{/if}
								<span>{$t(item.title)}</span>
							</a>
						</li>
					{/each}
				</ul>
			</nav>
		</div>
	{/if}
</div>