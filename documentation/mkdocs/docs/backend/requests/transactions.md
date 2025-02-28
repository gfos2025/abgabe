## /api/v1/transactions/

### POST `/api/v1/transactions/`

#### Beschreibung
Diese Route ermöglicht es Benutzern, eine neue Transaktion zu erstellen. Es werden Validierungen durchgeführt, um sicherzustellen, dass die erforderlichen Daten korrekt und konsistent sind.

---

#### Request-Body
Das Request-Body muss ein JSON-Objekt mit den folgenden Feldern enthalten:

| Feldname      | Typ       | Pflichtfeld | Beschreibung                                                                 |
|---------------|-----------|-------------|-----------------------------------------------------------------------------|
| `userId`      | UUID      | Ja          | Die eindeutige ID des Benutzers, der die Transaktion initiiert.             |
| `customerId`  | UUID      | Ja          | Die eindeutige ID des Kunden, für den die Transaktion erstellt wird.        |
| `amount`      | Double    | Ja          | Der Betrag der Transaktion.                                                 |
| `description` | String    | Nein        | Eine optionale Beschreibung der Transaktion.                                |
| `timestamp`   | LocalDateTime  | Ja          | Das Datum und die Uhrzeit der Transaktion (muss in der Vergangenheit liegen).|
| `status`      | Enum      | Nein        | Der Status der Transaktion (`Pending`, `Completed`, `Failed`).               |

##### Beispiel-Request-Body
```json
{
  "userId": "57f8a1cd-82ed-49e9-bc00-51008a017f7a",
  "customerId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "amount": 100.0,
  "description": "Monatliche Abrechnung",
  "timestamp": "2023-09-15T14:30:00"
}
```

---

#### Antwort

##### Erfolgreiche Erstellung (201 Created)
Wenn die Transaktion erfolgreich erstellt wurde, wird eine HTTP-Statuscode `201 Created` zurückgegeben, und die Antwort enthält eine Bestätigungsnachricht.

###### Beispiel-Antwort
```json
{
  "result": "Transaction successfully created"
}
```

##### Ungültige Eingaben (400 Bad Request)
Wenn das Request-Body ungültige oder fehlende Informationen enthält, wird `400 Bad Request` zurückgegeben, und der Response-Body enthält eine Fehlermeldung.

###### Beispiel-Antwort
```json
{
  "title": "Invalid request data",
  "status": 400,
  "type": "https://javalin.io/documentation#badrequestresponse",
  "details": {
    "message": "Timestamp must be within the last 90 days"
  }
}
```

---

#### Implementierungshinweise
- Der `timestamp` muss innerhalb der letzten 90 Tage liegen und darf nicht in der Zukunft sein.
- Der `userId` und `customerId` müssen gültige IDs aus dem System sein.

---

### GET `/api/v1/transactions/`

#### Beschreibung
Diese Route ermöglicht es Benutzern, eine Liste von Transaktionen basierend auf optionalen Filterkriterien abzurufen.

---

#### Query-Parameter

| Parameter     | Typ       | Pflichtfeld | Beschreibung                                                                 |
|---------------|-----------|-------------|-----------------------------------------------------------------------------|
| `user_id`     | UUID      | Nein        | Filtert Transaktionen nach dem Benutzer.                                    |
| `customer_id` | UUID      | Nein        | Filtert Transaktionen nach dem Kunden.                                      |
| `status`      | Enum      | Nein        | Filtert Transaktionen nach ihrem Status (`Pending`, `Completed`, `Failed`). |

---

#### Antwort

##### Erfolgreiche Abfrage (200 OK)
Wenn die Abfrage erfolgreich ist, wird eine HTTP-Statuscode `200 OK` zurückgegeben, und der Response-Body enthält eine Liste der Transaktionen im JSON-Format.

###### Beispiel-Antwort
```json
[
  {
    "id": "57f8a1cd-82ed-49e9-bc00-51008a017f7a",
    "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "customerId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "amount": 100.0,
    "description": "Monatliche Abrechnung",
    "timestamp": "2023-09-15T14:30:00",
    "status": "Completed"
  },
  {
    "id": "1b9d6bcd-bbfd-4b2d-9b5d-ab8df3d422da",
    "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "customerId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "amount": 50.0,
    "description": "Kauf eines Produkts",
    "timestamp": "2023-09-10T10:00:00",
    "status": "Pending"
  }
]
```

---

### GET `/api/v1/transactions/{id}`

#### Beschreibung
Diese Route ermöglicht es Benutzern, eine bestimmte Transaktion anhand ihrer eindeutigen ID abzurufen.

---

#### Pfadparameter

| Parameter | Typ       | Pflichtfeld | Beschreibung                                                                 |
|-----------|-----------|-------------|-----------------------------------------------------------------------------|
| `id`      | UUID      | Ja          | Die eindeutige ID der gesuchten Transaktion.                                |

---

#### Antwort

##### Erfolgreiche Abfrage (200 OK)
Wenn die Transaktion gefunden wird, wird eine HTTP-Statuscode `200 OK` zurückgegeben, und der Response-Body enthält die Details der Transaktion im JSON-Format.

###### Beispiel-Antwort
```json
{
  "id": "57f8a1cd-82ed-49e9-bc00-51008a017f7a",
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "customerId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "amount": 100.0,
  "description": "Monatliche Abrechnung",
  "timestamp": "2023-09-15T14:30:00",
  "status": "Completed"
}
```

##### Transaktion nicht gefunden (404 Not Found)
Wenn keine Transaktion mit der angegebenen ID existiert, wird `404 Not Found` zurückgegeben, und der Response-Body enthält eine Fehlermeldung.

###### Beispiel-Antwort
```json
{
  "title": "Transaction not found",
  "status": 404,
  "type": "https://javalin.io/documentation#notfoundresponse",
  "details": {}
}
```

---

### PATCH `/api/v1/transactions/{id}`

#### Beschreibung
Diese Route ermöglicht es Benutzern, den Status einer bestehenden Transaktion zu aktualisieren.

---

#### Pfadparameter

| Parameter | Typ       | Pflichtfeld | Beschreibung                                                                 |
|-----------|-----------|-------------|-----------------------------------------------------------------------------|
| `id`      | UUID      | Ja          | Die eindeutige ID der zu aktualisierenden Transaktion.                      |

#### Request-Body
Das Request-Body muss ein JSON-Objekt mit den folgenden Feldern enthalten:

| Feldname | Typ       | Pflichtfeld | Beschreibung                                                                 |
|----------|-----------|-------------|-----------------------------------------------------------------------------|
| `status` | Enum      | Ja          | Der neue Status der Transaktion (`Pending`, `Completed`, `Failed`).         |

##### Beispiel-Request-Body
```json
{
  "status": "Completed"
}
```

---

#### Antwort

##### Erfolgreiche Aktualisierung (200 OK)
Wenn die Transaktion erfolgreich aktualisiert wurde, wird eine HTTP-Statuscode `200 OK` zurückgegeben, und die Antwort enthält eine Bestätigungsnachricht.

###### Beispiel-Antwort
```json
{
  "result": "Transaction successfully updated"
}
```

##### Ungültiger Statusübergang (400 Bad Request)
Wenn der Statusübergang ungültig ist, wird `400 Bad Request` zurückgegeben, und der Response-Body enthält eine Fehlermeldung.

###### Beispiel-Antwort
```json
{
  "title": "Invalid status transition",
  "status": 400,
  "type": "https://javalin.io/documentation#badrequestresponse",
  "details": {}
}
```

---

### POST `/api/v1/transactions/report`

#### Beschreibung
Diese Route ermöglicht es Benutzern, einen Bericht über Transaktionen basierend auf angegebenen Kriterien zu generieren.

---

#### Request-Body
Das Request-Body muss ein JSON-Objekt mit den folgenden Feldern enthalten:

| Feldname     | Typ       | Pflichtfeld | Beschreibung                                                                 |
|--------------|-----------|-------------|-----------------------------------------------------------------------------|
| `startDate`  | LocalDateTime  | Nein        | Startdatum für die Berichtsperiode.                                         |
| `endDate`    | LocalDateTime  | Nein        | Enddatum für die Berichtsperiode.                                           |
| `userId`     | UUID      | Nein        | Filtert Transaktionen nach dem Benutzer.                                    |
| `customerId` | UUID      | Nein        | Filtert Transaktionen nach dem Kunden.                                      |
| `status`     | Enum      | Nein        | Filtert Transaktionen nach ihrem Status (`Pending`, `Completed`, `Failed`). |

---

#### Antwort

##### Erfolgreicher Bericht (200 OK)
Wenn der Bericht erfolgreich generiert wurde, wird eine HTTP-Statuscode `200 OK` zurückgegeben, und der Response-Body enthält den Bericht im JSON-Format.

###### Beispiel-Antwort
```json
{
  "totalTransactions": 10,
  "totalRevenue": 500.0,
  "averageTransactionAmount": 50.0,
  "transactionsByStatus": {
    "Pending": 3,
    "Completed": 7
  }
}
``` 

---