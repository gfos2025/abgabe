<script lang="ts">
	import { scaleLinear } from 'd3-scale';
	import { t } from 'svelte-i18n';
	import * as Sheet from '$lib/components/ui/sheet/index.js';
	import { buttonVariants } from '$lib/components/ui/button/index.js';
	import { Label } from '$lib/components/ui/label/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import type { MonthlyReport } from '$lib/types/report';

	interface Props {
		monthlyReports: MonthlyReport[];
		selectedView: 'total' | 'average' | 'transactions';
	}

	let { monthlyReports, selectedView }: Props = $props();
	let open: boolean = $state(false);
	const data = $derived(
		monthlyReports.map((report) => ({
			name: monthsMap[report.month],
			total:
				selectedView === 'total'
					? report.reportData.totalRevenue
					: selectedView === 'average'
					? report.reportData.averageTransactionAmount
					: report.reportData.totalTransactions,
			reportData: report.reportData
		}))
	);
	const xTicks = $derived(data.map((d) => d.name));
	const yMax = $derived(data.length > 0 ? Math.max(...data.map((d) => d.total)) : 0);
	const yTicks = $derived(yMax > 0 ? [0, yMax / 4, yMax / 2, (3 * yMax) / 4, yMax] : []);
	const padding = { top: 20, right: 0, bottom: 20, left: 45 };
	let width: number = $state(500);
	let height: number = $state(200);
	const xScale = $derived(scaleLinear().domain([0, xTicks.length]).range([padding.left, width - padding.right]));
	const yScale = $derived(scaleLinear().domain([0, Math.max.apply(null, yTicks)]).range([height - padding.bottom, padding.top]));
	const innerWidth = $derived(width - (padding.left + padding.right));
	const barWidth = $derived(innerWidth / xTicks.length);
	let selectedBar: {
		name: string;
		reportData: {
			totalRevenue: number;
			averageTransactionAmount: number;
			totalTransactions: number;
		};
	} | null = $state(null);

	const monthsMap = {
		Januar: 'dashboard.barchart.jan',
		Februar: 'dashboard.barchart.feb',
		März: 'dashboard.barchart.mar',
		April: 'dashboard.barchart.apr',
		Mai: 'dashboard.barchart.may',
		Juni: 'dashboard.barchart.jun',
		Juli: 'dashboard.barchart.jul',
		August: 'dashboard.barchart.aug',
		September: 'dashboard.barchart.sep',
		Oktober: 'dashboard.barchart.oct',
		November: 'dashboard.barchart.nov',
		Dezember: 'dashboard.barchart.dec'
	};

	function formatMonthName(name: string, width: number): string {
		return width > 380 ? $t(name) : $t(name).slice(0, 3);
	}

	function openSheet(bar: typeof selectedBar) {
		selectedBar = bar;
		open = true;
	}

	function closeSheet() {
		selectedBar = null;
		open = false;
	}
</script>

{#if data.length === 0}
	<div class="loading">Loading...</div>
{:else}
	<div class="chart-container">
		<div class="chart" bind:clientWidth={width} bind:clientHeight={height}>
			<svg>
				<g class="axis y-axis">
					{#each yTicks as tick}
						<g class="text-xs" transform="translate(0, {yScale(tick)})">
							<text
								stroke="none"
								font-size="12"
								orientation="left"
								width="60"
								height="310"
								x="57"
								y="-4"
								fill="#94908a"
								text-anchor="end"
							>
								<tspan x="36" dy="0.355em">{tick.toFixed(0)}</tspan>
							</text>
						</g>
					{/each}
				</g>
				<g class="axis x-axis">
					{#each data as point, i}
						<g class="text-xs" transform="translate({xScale(i)},{height})">
							<text
								stroke="none"
								font-size="12"
								orientation="bottom"
								width="531"
								height="30"
								x={barWidth / 2}
								y="-15"
								fill="#94908a"
								text-anchor="middle"
							>
								<tspan x={barWidth / 2} dy="0.71em">
									{formatMonthName(point.name, width)}
								</tspan>
							</text>
						</g>
					{/each}
				</g>
				<g>
					{#each data as point, i}
						<rect
							class="bar"
							role="button"
							tabindex={i}
							aria-pressed="false"
							x={xScale(i) + 2}
							y={yScale(point.total)}
							width={barWidth - 8}
							height={yScale(0) - yScale(point.total)}
							fill="#e5e3df"
							rx="4"
							ry="4"
							on:click={() => openSheet(point)}
						/>
					{/each}
				</g>
			</svg>
		</div>
	</div>

	<Sheet.Root bind:open>
		<Sheet.Content side="right">
			<Sheet.Header>
				<Sheet.Title>{$t('dashboard.detailedData')} ({$t(selectedBar?.name)})</Sheet.Title>
				<Sheet.Description>{$t('dashboard.detailedDataDesc')}</Sheet.Description>
			</Sheet.Header>
			<div class="grid gap-4 py-4">
				{#if selectedBar}
					<div class="grid grid-cols-4 items-center gap-4">
						<Label for="month" class="text-right">{$t('dashboard.month')}</Label>
						<Input id="month" value={$t(selectedBar.name)} class="col-span-3" readonly />
					</div>
					<div class="grid grid-cols-4 items-center gap-4">
						<Label for="totalRevenue" class="text-right">{$t('dashboard.totalRevenue')}</Label>
						<Input
							id="totalRevenue"
							value={`€${selectedBar.reportData.totalRevenue.toFixed(2)}`}
							class="col-span-3"
							readonly
						/>
					</div>
					<div class="grid grid-cols-4 items-center gap-4">
						<Label for="averageTransactionAmount" class="text-right"
							>{$t('dashboard.averageTransaction')}</Label
						>
						<Input
							id="averageTransactionAmount"
							value={`€${selectedBar.reportData.averageTransactionAmount.toFixed(2)}`}
							class="col-span-3"
							readonly
						/>
					</div>
					<div class="grid grid-cols-4 items-center gap-4">
						<Label for="totalTransactions" class="text-right">Total Transactions</Label>
						<Input
							id="totalTransactions"
							value={selectedBar.reportData.totalTransactions}
							class="col-span-3"
							readonly
						/>
					</div>
				{:else}
					<div class="text-center p-4">No data selected</div>
				{/if}
			</div>
			<Sheet.Footer>
				<Sheet.Close class={buttonVariants({ variant: 'outline' })} on:click={closeSheet}>
					Close
				</Sheet.Close>
			</Sheet.Footer>
		</Sheet.Content>
	</Sheet.Root>
{/if}

<style>
	.chart-container {
		width: 100%;
		overflow-x: auto;
	}
	.chart {
		width: max-content;
		min-width: 100%;
		margin: 0 auto;
	}
	.loading {
		text-align: center;
		padding: 20px;
		font-size: 16px;
		color: #94908a;
	}
	svg {
		position: relative;
		width: 100%;
		height: 250px;
	}
	rect {
		max-width: 51px;
	}
	.bar {
		fill: #e5e3df;
		transition: fill 0.3s ease;
	}
	.bar:hover {
		fill: #5e5ce6;
	}
</style>