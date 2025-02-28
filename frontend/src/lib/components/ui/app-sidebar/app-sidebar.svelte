<script lang="ts">
    import Calendar from "lucide-svelte/icons/calendar";
     import House from "lucide-svelte/icons/house";
     import Inbox from "lucide-svelte/icons/inbox";
    import Search from "lucide-svelte/icons/search";
    import Settings from "lucide-svelte/icons/settings";
    import * as Sidebar from "$lib/components/ui/sidebar/index.js";

    const items = [
	 {
	  title: 'dashboard.title',
	  url: '/dashboard',
	  icon: House
	 },
	 {
	  title: 'calendar.title',
	  url: '/dashboard/calendar',
	  icon: Calendar
	 },
	 {
	  title: 'customers.title',
	  url: '/dashboard/customers',
	  icon: Search
	 },
	 {
	  title: 'transactions.title',
	  url: '/dashboard/transactions',
	  icon: Search
	 },
	 {
	  title: 'logs.title',
	  url: '/dashboard/logs',
	  icon: Inbox
	 },
	 {
	  title: 'settings.title',
	  url: '/dashboard/settings',
	  icon: Settings
	 }
	];
   </script>
    
    <Sidebar.Root>
        <Sidebar.Content>
          <Sidebar.Group>
            <Sidebar.GroupLabel>Application</Sidebar.GroupLabel>
            <Sidebar.GroupContent>
              <Sidebar.Menu>
                {#each items as item (item.title)}
                  <Sidebar.MenuItem>
                    <Sidebar.MenuButton>
                      {#snippet child({ props })}
                        <a href={item.url} {...props}>
                          <item.icon />
                          <span>{item.title}</span>
                        </a>
                      {/snippet}
                    </Sidebar.MenuButton>
                  </Sidebar.MenuItem>
                {/each}
              </Sidebar.Menu>
            </Sidebar.GroupContent>
          </Sidebar.Group>
        </Sidebar.Content>
      </Sidebar.Root>