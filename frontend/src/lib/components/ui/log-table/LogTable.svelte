<script lang="ts">
  import { format, parse } from 'date-fns';
  import type { Log } from '$lib/types/log';
  import * as Table from "$lib/components/ui/table/index.js";
  import { t } from 'svelte-i18n';
  
  export let logs: Log[];
  
  // Define message patterns and their corresponding translation keys
  const messagePatterns = [
    {
      regex: /User (\S+) (\S+) created a new transaction with amount (\S+)/,
      translationKey: 'logs.created_transaction_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        amount: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) updated transaction with ID (\S+)/,
      translationKey: 'logs.updated_transaction_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        id: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) created a new appointment/,
      translationKey: 'logs.created_appointment_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2]
      })
    },
    {
      regex: /User (\S+) (\S+) created a new customer with email (\S+)/,
      translationKey: 'logs.created_customer_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        email: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) updated customer with ID (\S+)/,
      translationKey: 'logs.updated_customer_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        id: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) deleted customer with ID (\S+)/,
      translationKey: 'logs.deleted_customer_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        id: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) updated appointment with ID (\S+)/,
      translationKey: 'logs.updated_appointment_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        id: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) deleted appointment with ID (\S+)/,
      translationKey: 'logs.deleted_appointment_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        id: matches[3]
      })
    },
    {
      regex: /User (\S+) (\S+) registered with email (\S+)/,
      translationKey: 'logs.registered_user_message',
      valueMapper: (matches: RegExpMatchArray) => ({
        name: matches[1],
        surname: matches[2],
        email: matches[3]
      })
    }
  ];
  
  // Improved translateMessage function using the pattern definitions
  const translateMessage = (message: string): string => {
    for (const pattern of messagePatterns) {
      const match = message.match(pattern.regex);
      if (match) {
        const values = pattern.valueMapper(match);
        return $t(pattern.translationKey, { values });
      }
    }
    
    // Return original message if no patterns match
    return message;
  };
  
  // Format date for display
  const formatDate = (timestamp: string): string => {
    try {
      return format(parse(timestamp, 'dd.MM.yyyy HH:mm:ss', new Date()), 'dd.MM.yyyy HH:mm');
    } catch (error) {
      console.error('Error parsing date:', error);
      return timestamp;
    }
  };
  
  // Ensure logs are sorted with newest first (in case they're not already)
  $: sortedLogs = [...logs].sort((a, b) => 
    new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
  );
</script>

<Table.Root>
  <Table.Caption>{$t('logs.listOfLogs')}</Table.Caption>
  <Table.Header>
    <Table.Row>
      <Table.Head>{$t('logs.timestamp')}</Table.Head>
      <Table.Head>{$t('logs.userId')}</Table.Head>
      <Table.Head>{$t('logs.type')}</Table.Head>
      <Table.Head>{$t('logs.message')}</Table.Head>
    </Table.Row>
  </Table.Header>
  <Table.Body>
    {#each sortedLogs as log}
      <Table.Row>
        <Table.Cell>{formatDate(log.timestamp)}</Table.Cell>
        <Table.Cell>{log.userId}</Table.Cell>
        <Table.Cell>{$t(`logs.${log.type.toLowerCase()}`)}</Table.Cell>
        <Table.Cell>{translateMessage(log.message)}</Table.Cell>
      </Table.Row>
    {/each}
  </Table.Body>
</Table.Root>