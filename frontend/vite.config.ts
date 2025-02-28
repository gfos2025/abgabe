import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig, loadEnv } from 'vite';
import path from "path";

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
  
	return {
	resolve: {
		alias: {
		  $lib: path.resolve("./src/lib"),
		},
	  },
	  server: {
		proxy: {
			'/api': {
				target: env.PUBLIC_BACKEND_URL,
				changeOrigin: true,
				rewrite: (path) => path.replace('/api/', ''),
			}
		  },
	  },
	  optimizeDeps: {
		include: [
		  'lucide-svelte/icons/arrow-up-right',
		  'lucide-svelte/icons/credit-card',
		  'lucide-svelte/icons/dollar-sign',
		  'lucide-svelte/icons/users',
		  'lucide-svelte/icons/file',
		  'lucide-svelte/icons/list-filter',
		  'lucide-svelte/icons/sun',
		  'lucide-svelte/icons/moon',
		  'lucide-svelte/icons/chevron-down',
		  'lucide-svelte/icons/search',
		  'lucide-svelte/icons/minus',
		  'lucide-svelte/icons/x',
		  'svelte-sonner',
		  'svelte-headless-table',
		  'svelte-headless-table/plugins',
		  'svelte-radix/DotsHorizontal.svelte',
		  'svelte-radix/EyeNone.svelte',
		  'svelte-radix/ArrowDown.svelte',
		  'svelte-radix/ArrowUp.svelte',
		  'svelte-radix/CaretSort.svelte',
		  'svelte-radix/Cross2.svelte',
		  'svelte-radix/ChevronRight.svelte',
		  'svelte-radix/ChevronLeft.svelte',
		  'svelte-radix/DoubleArrowRight.svelte',
		  'svelte-radix/DoubleArrowLeft.svelte',
		  'svelte-radix/MixerHorizontal.svelte',
		  'svelte-radix/PlusCircled.svelte',
		  'svelte-radix/Check.svelte',
		  'cmdk-sv',
		  'svelte-radix/ArrowRight.svelte',
		  'svelte-radix/CheckCircled.svelte',
		  'svelte-radix/Circle.svelte',
		  'svelte-radix/CrossCircled.svelte',
		  'svelte-radix/QuestionMarkCircled.svelte',
		  'svelte-radix/Stopwatch.svelte',
		  'devalue',
		  'd3-scale'
		],
	  },	  
	plugins: [sveltekit()]
	};
});
