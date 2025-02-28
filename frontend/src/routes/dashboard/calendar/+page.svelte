<script lang="ts">
  // Importe von Icons und UI-Komponenten
  import PlusCircled from 'svelte-radix/PlusCircled.svelte';
  import CalendarIcon from "lucide-svelte/icons/calendar";
  import ChevronsUpDown from 'lucide-svelte/icons/chevrons-up-down';
  import Check from 'lucide-svelte/icons/check';
  import { Delete } from 'lucide-svelte';

  // Importe für Kalenderfunktionen und Plugins
  import { ScheduleXCalendar } from '@schedule-x/svelte';
  import { createCalendar, createViewDay, createViewWeek } from '@schedule-x/calendar';
  import { createCurrentTimePlugin } from '@schedule-x/current-time';
  import '@schedule-x/theme-shadcn/dist/index.css';
  import '@schedule-x/theme-default/dist/index.css';
  import { createEventsServicePlugin } from '@schedule-x/events-service';

  // Importe von UI-Komponenten
  import { Button } from '$lib/components/ui/button/index.js';
  import * as Dialog from "$lib/components/ui/dialog/index.js";
  import { Label } from "$lib/components/ui/label/index.js";
  import { Calendar } from "$lib/components/ui/calendar/index.js";
  import * as Popover from "$lib/components/ui/popover/index.js";
  import { ScrollArea, Scrollbar } from "$lib/components/ui/scroll-area/index.js";
  import { Textarea } from "$lib/components/ui/textarea/index.js";
  import * as Command from '$lib/components/ui/command/index.js';
  import { toast } from 'svelte-sonner';
  import * as Sheet from "$lib/components/ui/sheet/index.js";
  import { Input } from "$lib/components/ui/input/index.js";

  // Importe von Hilfsfunktionen und Typen
  import { format, parse } from "date-fns";
  import { cn } from '$lib/utils.js';
  import type { Appointment } from '$lib/types/appointment';
  import type { Customer } from '$lib/types/customer';
  import type { Employee } from '$lib/types/employee';
  import type { CalendarEventExternal } from '@schedule-x/calendar/dist/core';
  import {
    type DateValue,
    getLocalTimeZone,
    fromDate
  } from "@internationalized/date";

  import { onMount } from 'svelte';
  import { t } from 'svelte-i18n';
  import { tick } from 'svelte';

  // Zustandsvariablen
  let startValue: DateValue | undefined = $state(undefined);
  let endValue: DateValue | undefined = $state(undefined);
  let startDate: Date | undefined = $derived(startValue?.toDate(getLocalTimeZone()));
  let endDate: Date | undefined = $derived(endValue?.toDate(getLocalTimeZone()));
  let openCreateDialog: boolean = $state(false);
  let openDeleteDialog: boolean = $state(false);
  let openSheet: boolean = $state(false);
  let openEditSheet: boolean = $state(false);
  let description: string = $state('');
  let events: CalendarEventExternal[] = $state([]);
  let customerData: Customer[] = $state([]);
  let selectedCustomers: Customer[] = $state([]);
  let coworkerData: Employee[] = $state([]);
  let selectedCoworkers: Employee[] = $state([]);
  let coworkerDropdown: boolean = $state(false);
  let customerDropdown: boolean = $state(false);
  let coworkerRef: HTMLButtonElement = null!;
  let customerRef: HTMLButtonElement = null!;
  let appointments: Appointment[] = $state([]);
  let selectedAppointment = $state(null);
  const hours = Array.from({ length: 24 }, (_, i) => i);
  const eventsServicePlugin = createEventsServicePlugin();

  // Kalenderinitialisierung
  const calendarApp = createCalendar({
    plugins: [eventsServicePlugin, createCurrentTimePlugin()],
    views: [createViewDay(), createViewWeek()],
    callbacks: {
      async onEventClick(calendarEvent) {
        const appointment = appointments.find(app => app.id === calendarEvent.id);
        if (appointment) {
          selectedAppointment = appointment;
          triggerSheet(true);
        }
      }
    }
  });

  // Funktionen zum Abrufen von Daten
  const getCustomerData = async () => {
    try {
      const response = await fetch(`/api/v1/customers/`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include'
      });
      if (!response.ok) {
        throw new Error(`Error fetching customer data: ${response.status}`);
      }
      const responseData = await response.json();
      const dataArray: Customer[] = Object.values(responseData);
      customerData = dataArray;
    } catch (error) {
      console.error('Error during fetching customer data:', error);
    }
  };

  const getCoworkerData = async () => {
    try {
      const response = await fetch(`/api/v1/users/employees/`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include'
      });
      if (!response.ok) {
        throw new Error(`Error fetching customer data: ${response.status}`);
      }
      const responseData = await response.json();
      const dataArray: Employee[] = Object.values(responseData);
      coworkerData = dataArray;
    } catch (error) {
      console.error('Error during fetching customer data:', error);
    }
  };

  // Funktion zum Erstellen von Terminen
  const createUserAppointments = async () => {
    const id: string | null = localStorage?.getItem('id');
    if (!id) {
      throw new Error('User ID not found in localStorage');
    }

    try {
      const userIds = [
        ...selectedCoworkers.map(coworker => coworker?.id),
        id
      ];
      const toLocalISOString = (date: Date) => {
        const offset = date.getTimezoneOffset();
        const adjustedDate = new Date(date.getTime() - (offset * 60 * 1000));
        return adjustedDate.toISOString();
      };
      const response = await fetch(`/api/v1/appointments`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          startDate: startDate ? toLocalISOString(startDate) : null,
          endDate: endDate ? toLocalISOString(endDate) : null,
          userIds: userIds,
          customerIds: selectedCustomers.map(customer => customer?.id),
          organizerId: id,
          description: description
        }),
        credentials: 'include'
      });

      if (!response.ok) {
        toast.error("Couldn't create appointment", {
          description: `Error creating appointment: ${response.status}`
        });
        throw new Error(`Error creating appointment: ${response.status}`);
      }
      getUserAppointments();
      triggerCreateDialog(false);
      toast.success('Appointment created', {
        description: `Appointment has been successfully created.`
      });
      const responseData = await response.json();
      return responseData;
    } catch (error) {
      console.error('Error during creating user appointment:', error);
      throw error;
    }
  };

  // Hilfsfunktionen
  function closeAndFocusTrigger(triggerRef: HTMLButtonElement) {
    coworkerDropdown = false;
    customerDropdown = false;
    tick().then(() => {
      triggerRef?.focus();
    });
  }

  function triggerCreateDialog(bool: boolean) {
    openCreateDialog = bool;
  }

  function triggerDeleteDialog(bool: boolean) {
    openDeleteDialog = bool;
  }

  function triggerSheet(bool: boolean) {
    openSheet = bool;
    if (bool && selectedAppointment) {
      const start = parse(selectedAppointment.startTime, "dd.MM.yyyy HH:mm:ss", new Date());
      const end = parse(selectedAppointment.endTime, "dd.MM.yyyy HH:mm:ss", new Date());

      if (isNaN(start.getTime()) || isNaN(end.getTime())) {
        console.error('Invalid start or end date in selectedAppointment');
        return;
      }

      startValue = fromDate(start, getLocalTimeZone());
      endValue = fromDate(end, getLocalTimeZone());

      const { customers, coworkers } = separateParticipants(selectedAppointment.participants);

      selectedCustomers = [...new Map(customers.map(customer => [customer.id, customer])).values()];
      selectedCoworkers = [...new Map(coworkers.map(coworker => [coworker.id, coworker])).values()];
    }
  }

  function handleTimeChange(dateVariant: "start" | "end", type: "hour" | "minute", valueString: string) {
    if (valueString) {
      const newDate = dateVariant === "start" ? new Date(startDate) : new Date(endDate);
      if (type === "hour") {
        newDate.setHours(parseInt(valueString));
      } else if (type === "minute") {
        newDate.setMinutes(parseInt(valueString));
      }
      dateVariant === "start" ? startValue = fromDate(newDate, getLocalTimeZone()) : endValue = fromDate(newDate, getLocalTimeZone());
    }
  }

  function toggleSelection(selection: "customer" | "coworker", customer?: Customer, coworker?: Employee) {
    if (selection === "customer" && customer) {
      const index = selectedCustomers.findIndex(cust => cust?.id === customer?.id);
      if (index === -1) {
        selectedCustomers = [...selectedCustomers, customer];
      } else {
        selectedCustomers = selectedCustomers.filter(cust => cust?.id !== customer?.id);
      }
    } else if (selection === "coworker" && coworker) {
      const index = selectedCoworkers.findIndex(cow => cow?.id === coworker?.id);
      if (index === -1) {
        selectedCoworkers = [...selectedCoworkers, coworker];
      } else {
        selectedCoworkers = selectedCoworkers.filter(cow => cow?.id !== coworker?.id);
      }
    }
  }

  function separateParticipants(participants: any[]) {
    const customers = participants
      .filter(participant => participant.customerId !== null)
      .map(participant => ({
        id: participant.customerId,
        name: participant.name,
      }));

    const coworkers = participants
      .filter(participant => participant.userId !== null)
      .map(participant => ({
        id: participant.userId,
        name: participant.name,
      }));

    return { customers, coworkers };
  }

  const parseAppointmentsToEvents = (appointments: Appointment[]): CalendarEventExternal[] => {
    return appointments.map(appointment => {
      const formattedStartDate = format(parse(appointment.startTime, "dd.MM.yyyy HH:mm:ss", new Date()), "yyyy-MM-dd HH:mm");
      const formattedEndDate = format(parse(appointment.endTime, "dd.MM.yyyy HH:mm:ss", new Date()), "yyyy-MM-dd HH:mm");
      return {
        id: appointment.id,
        start: formattedStartDate,
        end: formattedEndDate,
        title: appointment.description,
        people: appointment.participants.map((participant: any) => participant.name)
      };
    });
  };

  const getUserAppointments = async () => {
    const id: string = localStorage?.getItem('id');
    try {
      const response = await fetch(`/api/v1/users/${id}/appointments`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      });
      if (!response.ok) {
        throw new Error(`Error fetching user appointments: ${response.status}`);
      }
      appointments = await response.json();
      events = parseAppointmentsToEvents(appointments);
      events.forEach(event => {
        calendarApp.eventsService.add(event);
      });
    } catch (error) {
      console.error('Error during fetching user appointments:', error);
      throw error;
    }
  };

  const deleteAppointment = async (appointment_id: string) => {
    try {
      calendarApp.eventsService.remove(calendarApp.eventsService.get(appointment_id));
      const response = await fetch(`/api/v1/appointments/${appointment_id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include'
      });
      if (!response.ok) {
        toast.error("Couldn't delete appointment", {
          description: `Error delete appointment: ${response.status}`
        });
        throw new Error(`Error fetching user appointments: ${response.status}`);
      }
      toast.success("Successfully deleted appointment", {
        description: `Deleted appointment: ${selectedAppointment?.id}`
      });
    } catch (error) {
      console.error('Error during fetching user appointments:', error);
      throw error;
    }
  };

  const editAppointment = async () => {
    try {
      if (!selectedAppointment) {
        throw new Error('No appointment selected');
      }

      const toLocalISOString = (date: Date) => {
        const offset = date.getTimezoneOffset();
        const adjustedDate = new Date(date.getTime() - offset * 60 * 1000);
        return adjustedDate.toISOString();
      };

      // Ensure unique userIds
      const userIds = [
        localStorage.getItem('id'), // Include the organizer
        ...selectedAppointment.participants
          .filter(participant => participant.userId !== null)
          .map(participant => participant.userId),
        ...selectedCoworkers.map(coworker => coworker?.id),
      ].filter((id, index, self) => self.indexOf(id) === index); // Remove duplicates

      // Ensure unique customerIds
      const customerIds = [
        ...selectedAppointment.participants
          .filter(participant => participant.customerId !== null)
          .map(participant => participant.customerId),
        ...selectedCustomers.map(customer => customer?.id),
      ].filter((id, index, self) => self.indexOf(id) === index); // Remove duplicates

      const updatedData = {
        startDate: startDate ? toLocalISOString(startDate) : null,
        endDate: endDate ? toLocalISOString(endDate) : null,
        organizerId: localStorage.getItem('id'),
        userIds: userIds,
        customerIds: customerIds,
        description: selectedAppointment.description,
      };

      const response = await fetch(`/api/v1/appointments/${selectedAppointment.id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
        credentials: 'include',
      });

      if (!response.ok) {
        toast.error("Couldn't update appointment", {
          description: `Error updating appointment: ${response.status}`,
        });
        throw new Error(`Error updating appointment: ${response.status}`);
      }

      toast.success('Appointment updated', {
        description: `Appointment has been successfully updated.`,
      });

      getUserAppointments();
      openEditSheet = false;
      triggerSheet(false);
    } catch (error) {
      console.error('Error during updating appointment:', error);
      throw error;
    }
  };

  // Lifecycle-Hooks
  onMount(() => {
    getCustomerData();
    getCoworkerData();
    if (typeof window !== 'undefined' && window.localStorage) {
      getUserAppointments();
    }
  });
</script>

<!-- UI-Komponenten -->
<div class="h-full flex-1 flex-col p-2 space-y-2 md:space-y-8 md:p-8 flex">
  <Sheet.Root bind:open={openSheet}>
    <Sheet.Content side="right">
      <Dialog.Root bind:open={openDeleteDialog}>
        <Dialog.Content>
          <Dialog.Header>
            <Dialog.Title> {$t('calendar.confirmDeletion')}</Dialog.Title>
            <Dialog.Description>
              {$t('calendar.confirmDeletionDesc')}
            </Dialog.Description>
          </Dialog.Header>
          <Dialog.Footer>
            <Button variant="destructive" size="sm" class="ml-auto h-8 flex" on:click={() => {
              deleteAppointment(selectedAppointment.id);
              triggerDeleteDialog(false);
              triggerSheet(false);
            }}>
              <Delete class="mr-2 h-4 w-4" />
              {$t('calendar.delete')}
            </Button>
          </Dialog.Footer>
        </Dialog.Content>
      </Dialog.Root>
      <Sheet.Header>
        <Sheet.Title>{openEditSheet ? $t('calendar.editAppointment') : $t('calendar.appointmentDetails')}</Sheet.Title>
        <Sheet.Description>
          {openEditSheet ? $t('calendar.editAppointmentDesc') : $t('calendar.detailsDesc')}
        </Sheet.Description>
      </Sheet.Header>
      <div class="grid gap-4 py-4">
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="appointmentid" class="text-right">{$t('calendar.id')}</Label>
          <Input id="appointmentid" bind:value={selectedAppointment.id} class="col-span-3" readonly={!openEditSheet} />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="start-time" class="text-right">{$t('calendar.startDate')}</Label>
          <Popover.Root>
            <Popover.Trigger class="col-span-3" id="start-time">
              <Button
                variant="outline"
                class={`w-full justify-start text-left font-normal ${!startDate ? 'text-muted-foreground' : ''}`}
              >
                <CalendarIcon class="mr-2 h-4 w-4" />
                {#if startDate}
                  {format(startDate, "dd/MM/yyyy HH:mm")}
                {:else}
                  DD/MM/YYYY hh:mm
                {/if}
              </Button>
            </Popover.Trigger>
            <Popover.Content class="w-auto p-0 bg-background">
              <div class="sm:flex">
                <Calendar
                  type="single"
                  bind:value={startValue}
                  initialFocus
                />
                <div class="flex flex-col sm:flex-row sm:h-[300px] divide-y sm:divide-y-0 sm:divide-x">
                  <ScrollArea class="w-64 sm:w-auto">
                    <div class="flex sm:flex-col p-2">
                      {#each hours.reverse() as hour}
                        <Button
                          size="icon"
                          variant={startDate && startDate.getHours() === hour ? "default" : "ghost"}
                          class="sm:w-full shrink-0 aspect-square"
                          on:click={() => handleTimeChange("start", "hour", hour.toString())}
                        >
                          {hour}
                        </Button>
                      {/each}
                    </div>
                    <Scrollbar orientation="horizontal" class="sm:hidden" />
                  </ScrollArea>
                  <ScrollArea class="w-64 sm:w-auto">
                    <div class="flex sm:flex-col p-2">
                      {#each Array.from({ length: 12 }, (_, i) => i * 5) as minute}
                        <Button
                          size="icon"
                          variant={startDate && startDate.getMinutes() === minute ? "default" : "ghost"}
                          class="sm:w-full shrink-0 aspect-square"
                          on:click={() => handleTimeChange("start", "minute", minute.toString())}
                        >
                          {String(minute).padStart(2, '0')}
                        </Button>
                      {/each}
                    </div>
                    <Scrollbar orientation="horizontal" class="sm:hidden" />
                  </ScrollArea>
                </div>
              </div>
            </Popover.Content>
          </Popover.Root>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="end-time" class="text-right">{$t('calendar.endDate')}</Label>
          <Popover.Root>
            <Popover.Trigger class="col-span-3" id="end-time">
              <Button
                variant="outline"
                class={`w-full justify-start text-left font-normal ${!endDate ? 'text-muted-foreground' : ''}`}
              >
                <CalendarIcon class="mr-2 h-4 w-4" />
                {#if endDate}
                  {format(endDate, "dd/MM/yyyy HH:mm")}
                {:else}
                  DD/MM/YYYY hh:mm
                {/if}
              </Button>
            </Popover.Trigger>
            <Popover.Content class="w-auto p-0 bg-background">
              <div class="sm:flex">
                <Calendar
                  type="single"
                  bind:value={endValue}
                  initialFocus
                />
                <div class="flex flex-col sm:flex-row sm:h-[300px] divide-y sm:divide-y-0 sm:divide-x">
                  <ScrollArea class="w-64 sm:w-auto">
                    <div class="flex sm:flex-col p-2">
                      {#each hours.reverse() as hour}
                        <Button
                          size="icon"
                          variant={endDate && endDate.getHours() === hour ? "default" : "ghost"}
                          class="sm:w-full shrink-0 aspect-square"
                          on:click={() => handleTimeChange("end", "hour", hour.toString())}
                        >
                          {hour}
                        </Button>
                      {/each}
                    </div>
                    <Scrollbar orientation="horizontal" class="sm:hidden" />
                  </ScrollArea>
                  <ScrollArea class="w-64 sm:w-auto">
                    <div class="flex sm:flex-col p-2">
                      {#each Array.from({ length: 12 }, (_, i) => i * 5) as minute}
                        <Button
                          size="icon"
                          variant={endDate && endDate.getMinutes() === minute ? "default" : "ghost"}
                          class="sm:w-full shrink-0 aspect-square"
                          on:click={() => handleTimeChange("end", "minute", minute.toString())}
                        >
                          {String(minute).padStart(2, '0')}
                        </Button>
                      {/each}
                    </div>
                    <Scrollbar orientation="horizontal" class="sm:hidden" />
                  </ScrollArea>
                </div>
              </div>
            </Popover.Content>
          </Popover.Root>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="description" class="text-right">{$t('calendar.description')}</Label>
          <Textarea id="description" bind:value={selectedAppointment.description} class="col-span-3" readonly={!openEditSheet} />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="coworkers" class="text-right">{$t('calendar.coworkers')}</Label>
          <Popover.Root bind:open={coworkerDropdown}>
            <Popover.Trigger ref={coworkerRef} class="col-span-3 h-full" id="coworkers">
              <Button
                variant="outline"
                class="w-full h-full justify-between"
                role="combobox"
                aria-expanded={coworkerDropdown}
                disabled={!openEditSheet}
              >
                {selectedCoworkers.length > 0
                  ? selectedCoworkers.map(cow => cow.name).join(", ")
                  : $t('calendar.selectCoworkers')}
                <ChevronsUpDown class="opacity-50" />
              </Button>
            </Popover.Trigger>
            <Popover.Content class="w-[200px] p-0">
              <Command.Root class="bg-background">
                <Command.Input placeholder={$t('calendar.searchCoworkers')} />
                <Command.List>
                  <Command.Empty>{$t('calendar.noCoworkersFound')}</Command.Empty>
                  <Command.Group>
                    {#each coworkerData as coworker}
                      <Command.Item
                        onSelect={() => {
                          toggleSelection("coworker", null, coworker);
                          closeAndFocusTrigger(coworkerRef);
                        }}
                      >
                        <Check
                          class={cn(!selectedCoworkers.some(cow => cow?.id === coworker?.id) ? 'text-transparent' : '')}
                        />
                        {coworker?.name} {coworker?.surname}
                      </Command.Item>
                    {/each}
                  </Command.Group>
                </Command.List>
              </Command.Root>
            </Popover.Content>
          </Popover.Root>
        </div>
        
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="customers" class="text-right">{$t('calendar.customers')}</Label>
          <Popover.Root bind:open={customerDropdown}>
            <Popover.Trigger ref={customerRef} class="col-span-3 h-full" id="customers">
              <Button
                variant="outline"
                class="w-full h-full justify-between"
                role="combobox"
                aria-expanded={customerDropdown}
                disabled={!openEditSheet}
              >
                {selectedCustomers.length > 0
                  ? selectedCustomers.map(cust => cust.name).join(", ")
                  : $t('calendar.selectCustomers')}
                <ChevronsUpDown class="opacity-50" />
              </Button>
            </Popover.Trigger>
            <Popover.Content class="w-[200px] p-0">
              <Command.Root class="bg-background">
                <Command.Input placeholder={$t('calendar.searchCustomers')} />
                <Command.List>
                  <Command.Empty>{$t('calendar.noCustomersFound')}</Command.Empty>
                  <Command.Group>
                    {#each customerData as customer}
                      <Command.Item
                        onSelect={() => {
                          toggleSelection("customer", customer);
                          closeAndFocusTrigger(customerRef);
                        }}
                      >
                        <Check
                          class={cn(!selectedCustomers.some(cust => cust?.id === customer?.id) ? 'text-transparent' : '')}
                        />
                        {customer?.name} {customer?.surname}
                      </Command.Item>
                    {/each}
                  </Command.Group>
                </Command.List>
              </Command.Root>
            </Popover.Content>
          </Popover.Root>
        </div>
      </div>
      <Sheet.Footer>
        {#if !openEditSheet}
          <Button
            variant="outline"
            on:click={() => {
              openEditSheet = true;
            }}
          >
            {$t('calendar.editAppointment')}
          </Button>
          <Button
            variant="destructive"
            on:click={() => {
              triggerDeleteDialog(true);
            }}
          >
            {$t('calendar.deleteAppointment')}
          </Button>
        {:else}
          <Button
            variant="default"
            on:click={() => {
              editAppointment();
            }}
          >
            {$t('calendar.saveChanges')}
          </Button>
          <Button
            variant="outline"
            on:click={() => {
              openEditSheet = false;
            }}
          >
            {$t('calendar.cancel')}
          </Button>
        {/if}
      </Sheet.Footer>
    </Sheet.Content>
  </Sheet.Root>
  <div class="flex items-center justify-between space-y-2">
    <div>
      <h2 class="text-2xl font-bold tracking-tight">{$t('calendar.title')}</h2>
      <p class="text-muted-foreground">{$t('calendar.desc')}</p>
    </div>
    <Button variant="default" size="sm" class="ml-auto h-8 flex" on:click={() => triggerCreateDialog(true)}>
      <PlusCircled class="mr-2 h-4 w-4" />
      {$t('calendar.addAppointment')}
    </Button>
  </div>
  <ScheduleXCalendar {calendarApp}/>
</div>

<Dialog.Root bind:open={openCreateDialog}>
  <Dialog.Content class="sm:max-w-[425px]">
    <Dialog.Header>
      <Dialog.Title>{$t('calendar.addAppointment')}</Dialog.Title>
      <Dialog.Description>
        {$t('calendar.addAppointmentDesc')}
      </Dialog.Description>
    </Dialog.Header>
    <div class="grid gap-4 py-4">
      <div class="grid grid-cols-4 items-center gap-4">
        <Label for="name" class="text-right">{$t('calendar.startDate')}</Label>
        <Popover.Root>
          <Popover.Trigger class="col-span-3">
            <Button
              variant="outline"
              class={`w-full justify-start text-left font-normal ${!startDate ? 'text-muted-foreground' : ''}`}
            >
              <CalendarIcon class="mr-2 h-4 w-4" />
              {#if startDate}
                {format(startDate, "dd/MM/yyyy HH:mm")}
              {:else}
                DD/MM/YYYY hh:mm
              {/if}
            </Button>
          </Popover.Trigger>
          <Popover.Content class="w-auto p-0 bg-background">
            <div class="sm:flex">
              <Calendar
                type="single"
                bind:value={startValue}
                initialFocus
              />
              <div class="flex flex-col sm:flex-row sm:h-[300px] divide-y sm:divide-y-0 sm:divide-x">
                <ScrollArea class="w-64 sm:w-auto">
                  <div class="flex sm:flex-col p-2">
                    {#each hours.reverse() as hour}
                      <Button
                        size="icon"
                        variant={startDate && startDate.getHours() === hour ? "default" : "ghost"}
                        class="sm:w-full shrink-0 aspect-square"
                        on:click={() => handleTimeChange("start" ,"hour", hour.toString())}
                      >
                        {hour}
                      </Button>
                    {/each}
                  </div>
                  <Scrollbar orientation="horizontal" class="sm:hidden" />
                </ScrollArea>
                <ScrollArea class="w-64 sm:w-auto">
                  <div class="flex sm:flex-col p-2">
                    {#each Array.from({ length: 12 }, (_, i) => i * 5) as minute}
                      <Button
                        size="icon"
                        variant={startDate && startDate.getMinutes() === minute ? "default" : "ghost"}
                        class="sm:w-full shrink-0 aspect-square"
                        on:click={() => handleTimeChange("start" ,"minute", minute.toString())}
                      >
                        {String(minute).padStart(2, '0')}
                      </Button>
                    {/each}
                  </div>
                  <Scrollbar orientation="horizontal" class="sm:hidden" />
                </ScrollArea>
              </div>
            </div>
          </Popover.Content>
        </Popover.Root>
      </div>
      <div class="grid grid-cols-4 items-center gap-4">
        <Label for="endtime" class="text-right">{$t('calendar.endDate')}</Label>
        <Popover.Root>
          <Popover.Trigger class="col-span-3">
            <Button
              variant="outline"
              class={`w-full justify-start text-left font-normal ${!endDate ? 'text-muted-foreground' : ''}`}
            >
              <CalendarIcon class="mr-2 h-4 w-4" />
              {#if endDate}
                {format(endDate, "dd/MM/yyyy HH:mm")}
              {:else}
                DD/MM/YYYY hh:mm
              {/if}
            </Button>
          </Popover.Trigger>
          <Popover.Content class="w-auto p-0 bg-background">
            <div class="sm:flex">
              <Calendar
                type="single"
                bind:value={endValue}
                initialFocus
              />
              <div class="flex flex-col sm:flex-row sm:h-[300px] divide-y sm:divide-y-0 sm:divide-x">
                <ScrollArea class="w-64 sm:w-auto">
                  <div class="flex sm:flex-col p-2">
                    {#each hours.reverse() as hour}
                      <Button
                        size="icon"
                        variant={endDate && endDate.getHours() === hour ? "default" : "ghost"}
                        class="sm:w-full shrink-0 aspect-square"
                        on:click={() => handleTimeChange("end", "hour", hour.toString())}
                      >
                        {hour}
                      </Button>
                    {/each}
                  </div>
                  <Scrollbar orientation="horizontal" class="sm:hidden" />
                </ScrollArea>
                <ScrollArea class="w-64 sm:w-auto">
                  <div class="flex sm:flex-col p-2">
                    {#each Array.from({ length: 12 }, (_, i) => i * 5) as minute}
                      <Button
                        size="icon"
                        variant={endDate && endDate.getMinutes() === minute ? "default" : "ghost"}
                        class="sm:w-full shrink-0 aspect-square"
                        on:click={() => handleTimeChange("end", "minute", minute.toString())}
                      >
                        {String(minute).padStart(2, '0')}
                      </Button>
                    {/each}
                  </div>
                  <Scrollbar orientation="horizontal" class="sm:hidden" />
                </ScrollArea>
              </div>
            </div>
          </Popover.Content>
        </Popover.Root>
      </div>
      <div class="grid grid-cols-4 items-center gap-4">
        <Label for="coworkers" class="text-right">{$t('calendar.coworkers')}</Label>
        <Popover.Root bind:open={coworkerDropdown}>
          <Popover.Trigger ref={coworkerRef} class="col-span-3 h-full" id="coworkers">
            <Button
              variant="outline"
              class="w-full h-full justify-between"
              role="combobox"
              aria-expanded={coworkerDropdown}
            >
              {selectedCoworkers.length > 0
                ? selectedCoworkers.map(cow => cow.name).join(", ")
                : $t('calendar.selectCoworkers')}
              <ChevronsUpDown class="opacity-50" />
            </Button>
          </Popover.Trigger>
          <Popover.Content class="w-[200px] p-0">
            <Command.Root class="bg-background">
              <Command.Input placeholder={$t('calendar.searchCoworkers')} />
              <Command.List>
                <Command.Empty>{$t('calendar.noCoworkersFound')}</Command.Empty>
                <Command.Group>
                  {#each coworkerData as coworker}
                    <Command.Item
                      onSelect={() => {
                        toggleSelection("coworker", null, coworker);
                        closeAndFocusTrigger(coworkerRef);
                      }}
                    >
                      <Check
                        class={cn(!selectedCoworkers.some(cow => cow?.id === coworker?.id) ? 'text-transparent' : '')}
                      />
                      {coworker?.name} {coworker?.surname}
                    </Command.Item>
                  {/each}
                </Command.Group>
              </Command.List>
            </Command.Root>
          </Popover.Content>
        </Popover.Root>
      </div>
      <div class="grid grid-cols-4 items-center gap-4">
        <Label for="customers" class="text-right">{$t('calendar.customers')}</Label>
        <Popover.Root bind:open={customerDropdown}>
          <Popover.Trigger ref={customerRef} class="col-span-3 h-full" id="customers">
            <Button
              variant="outline"
              class="w-full h-full justify-between"
              role="combobox"
              aria-expanded={customerDropdown}
            >
              {selectedCustomers.length > 0
                ? selectedCustomers.map(cust => cust.name).join(", ")
                : $t('calendar.selectCustomers')}
              <ChevronsUpDown class="opacity-50" />
            </Button>
          </Popover.Trigger>
          <Popover.Content class="w-[200px] p-0">
            <Command.Root class="bg-background">
              <Command.Input placeholder={$t('calendar.searchCustomers')} />
              <Command.List>
                <Command.Empty>{$t('calendar.noCustomersFound')}</Command.Empty>
                <Command.Group>
                  {#each customerData as customer}
                    <Command.Item
                      onSelect={() => {
                        toggleSelection("customer", customer);
                        closeAndFocusTrigger(customerRef);
                      }}
                    >
                      <Check
                        class={cn(!selectedCustomers.some(cust => cust?.id === customer?.id) ? 'text-transparent' : '')}
                      />
                      {customer?.name} {customer?.surname}
                    </Command.Item>
                  {/each}
                </Command.Group>
              </Command.List>
            </Command.Root>
          </Popover.Content>
        </Popover.Root>
      </div>
      <div class="grid grid-cols-4 items-center gap-4">
        <Label for="description" class="text-right">{$t('calendar.description')}</Label>
        <Textarea id="description" bind:value={description} class="col-span-3" placeholder={$t('calendar.textareaPlaceholder')}/>
      </div>
    </div>
    <Dialog.Footer>
      <Button on:click={() => createUserAppointments()}>{$t('calendar.saveChanges')}</Button>
    </Dialog.Footer>
  </Dialog.Content>
</Dialog.Root>