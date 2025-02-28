# Dokumentation des Enums

## `PriorityEnum`
- **Beschreibung**: Ein Enum, das die Prioritätsstufen für Aufgaben, Ereignisse oder andere Entitäten in der Anwendung definiert.
- **Werte**:
  - `Low` (`'Low'`): Geringe Priorität. Wird für weniger dringende oder weniger wichtige Aufgaben verwendet.
  - `Medium` (`'Medium'`): Mittlere Priorität. Wird für Aufgaben mit normaler Dringlichkeit oder Bedeutung verwendet.
  - `High` (`'High'`): Hohe Priorität. Wird für dringende oder kritische Aufgaben verwendet.

---

## Verwendung
- Das `PriorityEnum` kann verwendet werden, um die Priorität von Aufgaben, Tickets, Ereignissen oder anderen Entitäten in der Anwendung festzulegen.
- Es bietet eine klare und konsistente Möglichkeit, Prioritätsstufen zu definieren und zu verwenden.

---

## Beispiel
```typescript
const taskPriority: PriorityEnum = PriorityEnum.High;

if (taskPriority === PriorityEnum.High) {
    console.log("This task is urgent!");
}
```

---

## Zusammenfassung
- Das `PriorityEnum` bietet eine einfache und lesbare Möglichkeit, Prioritätsstufen in der Anwendung zu definieren.
- Es hilft dabei, die Bedeutung oder Dringlichkeit von Aufgaben klar zu kommunizieren und zu verwalten.