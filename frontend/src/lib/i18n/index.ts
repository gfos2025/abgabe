import { browser } from '$app/environment'
import { init, register } from 'svelte-i18n'

const defaultLocale = 'de-DE'

register('en-US', () => import('./locales/en-US.json'))
register('de-DE', () => import('./locales/de-DE.json'))

init({
	fallbackLocale: defaultLocale,
	initialLocale: browser ? window.navigator.language : defaultLocale,
})