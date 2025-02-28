import ArrowDown from "svelte-radix/ArrowDown.svelte";
import ArrowRight from "svelte-radix/ArrowRight.svelte";
import ArrowUp from "svelte-radix/ArrowUp.svelte";
import Clock from "svelte-radix/Clock.svelte";
import ValueNone from "svelte-radix/ValueNone.svelte";
import Checkbox from "svelte-radix/Checkbox.svelte";
import Person from "svelte-radix/Person.svelte";
import { StatusEnum } from '$lib/types/status';
import { PriorityEnum } from '$lib/types/priority';

export const statuses = [
	{
		value: StatusEnum.Contacted.toString(),
		label: 'status.contacted',
		icon: Checkbox,
	},
	{
		value: StatusEnum.NotContacted.toString(),
		label: 'status.not_contacted',
		icon: ValueNone,
	},
	{
		value: StatusEnum.WaitingForCallback.toString(),
		label: 'status.waiting_for_callback',
		icon: Clock,
	},
	{
		value: StatusEnum.Interested.toString(),
		label: 'status.interested',
		icon: Person,
	},
];



export const priorities = [
	{
		value: PriorityEnum.Low,
		label: 'priority.low',
		icon: ArrowDown,
	},
	{
		value: PriorityEnum.Medium,
		label: 'priority.medium',
		icon: ArrowRight,
	},
	{
		value: PriorityEnum.High,
		label: 'priority.high',
		icon: ArrowUp,
	},
];