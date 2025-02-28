import { type VariantProps, tv } from "tailwind-variants";
export { default as Badge } from "./badge.svelte";

export const badgeVariants = tv({
	base: "focus:ring-ring inline-flex select-none items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2",
	variants: {
		variant: {
			default: "bg-primary text-primary-foreground hover:bg-primary/80 border-transparent",
			secondary:
				"bg-custom-green text-primary-foreground hover:bg-custom-green/80 border-transparent",
			destructive:
				"bg-custom-red text-primary-foreground hover:bg-destructive/80 border-transparent",
			outline: "text-foreground",
		},
	},
	defaultVariants: {
		variant: "default",
	},
});

export type Variant = VariantProps<typeof badgeVariants>["variant"];
