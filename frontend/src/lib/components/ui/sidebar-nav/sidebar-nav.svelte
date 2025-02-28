<script lang="ts">
	import { cubicInOut } from 'svelte/easing';
	import { crossfade } from 'svelte/transition';
	import { cn } from '$lib/utils.js';
	import { page } from '$app/stores';
	import { Button } from '$lib/components/ui/button/index.js';
	import { t } from 'svelte-i18n';
	import { BarChart, Users, ArrowLeftRight, Bell, Settings, Calendar, UserSearch } from 'lucide-svelte';
  
	const icons = {
	  'chart-bar': BarChart,
	  calendar: Calendar,
	  users: Users,
	  'arrow-left-right': ArrowLeftRight,
	  bell: Bell,
	  'user-search': UserSearch,
	  settings: Settings
	};
  
	let className: string | undefined | null = undefined;
	export let items: { href: string; title: string; icon?: keyof typeof icons }[];
	export { className as class };
  
	const [send, receive] = crossfade({
	  duration: 250,
	  easing: cubicInOut
	});
  </script>
  
  <nav class={cn('flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-1', className)}>
	{#each items as item}
	  {@const isActive = $page.url.pathname === item.href}
	  {@const Icon = icons[item.icon]}
	  <Button
		href={item.href}
		variant="ghost"
		class={cn(
		  !isActive && 'hover:underline',
		  'relative justify-start hover:bg-transparent'
		)}
		data-sveltekit-noscroll
	  >
		{#if isActive}
		  <div
			class="bg-primary absolute inset-0 rounded-md"
			in:send={{ key: 'active-sidebar-tab' }}
			out:receive={{ key: 'active-sidebar-tab' }}
		  ></div>
		{/if}
		<div class="relative flex items-center space-x-2">
		  {#if Icon}
			<Icon class="w-5 h-5" />
		  {/if}
		  <span>{$t(item.title)}</span>
		</div>
	  </Button>
	{/each}
  </nav>