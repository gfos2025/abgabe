### **Installation und Konfiguration**

#### **1. Installieren Sie die erforderlichen Softwarekomponenten**
- **Java (OpenJDK)**: Stellen Sie sicher, dass OpenJDK Version 21.0.6 oder höher installiert ist.
  - Überprüfen Sie die Installation mit dem Befehl:  
    ```bash
    java --version
    ```
- **PostgreSQL**: Installieren Sie PostgreSQL Version 17.2 oder höher.
  - Starten Sie den Datenbankdienst nach der Installation.
  - Überprüfen Sie die Installation mit dem Befehl:  
    ```bash
    psql --version
    ```

#### **2. Erstellen Sie einen PostgreSQL-Benutzer und eine Datenbank**
- Öffnen Sie die PostgreSQL-Konsole (`psql`) oder verwenden Sie ein grafisches Tool wie pgAdmin.
- Führen Sie folgende Befehle aus, um einen neuen Benutzer und eine neue Datenbank zu erstellen:
  ```sql
  CREATE USER dein_username WITH PASSWORD 'dein_passwort';
  CREATE DATABASE deine_datenbank OWNER dein_username;
  ```

#### **3. Konfigurieren Sie die Umgebungsvariablen**
- Erstellen Sie eine `.env`-Datei im Projektverzeichnis.
- Fügen Sie die folgenden Werte hinzu und passen Sie sie an Ihre Konfiguration an:
  ```
  HOST=localhost
  PORT=8888
  DATABASE_URL=jdbc:postgresql://localhost:5432/deine_datenbank?user=dein_username&password=dein_passwort
  ```

  - `Host`: Legt den Hostnamen für die Anwendung fest.
  - `Port`: Legt den Port fest, auf dem die Anwendung läuft.
  - `Database_URL`: Legt die URL für die Verbindung zur PostgreSQL-Datenbank fest.

#### **4. Konfigurieren Sie Hibernate**
- Bearbeiten Sie die `hibernate.properties`-Datei im Verzeichnis `app/src/main/resources`.
- Setzen Sie die folgenden Eigenschaften, oder passen Sie sie an Ihre Konfiguration an:
  ```
  hibernate.connection.url=jdbc:postgresql://localhost:5432/your_database
  hibernate.hbm2ddl.auto=create-drop
  hibernate.hbm2ddl.import_files=init.sql
  ```

#### **5. Importieren Sie Testdaten**
- Laden Sie die Datei `init.sql` mit den Testdaten neben der `hibernate.properties`-Datei ab.
- Die Datei sollte den folgenden Inhalt enthalten (je nach Bedarf können Sie zwischen den beiden Optionen wählen):

**Option 1: Nur Admin-Benutzer erstellen**
```sql
INSERT INTO users(darkmode, islanguagegerman, id, name, surname, email, passwordhash, role) 
VALUES (true, true, '57f8a1cd-82ed-49e9-bc00-51008a017f7a', 'admin', 'admin', 'admin@admin.com', '$argon2i$v=19$m=16,t=2,p=1$V3ZZRHplRENXWXNmbkdGcw$+fFtK+BCZI7Y1AMRoC6xMw', 3);
```

**Option 2: Vollständige Testdaten importieren**
```sql
INSERT INTO users(darkmode, islanguagegerman, id, name, surname, email, passwordhash, role) values ( true, true, '57f8a1cd-82ed-49e9-bc00-51008a017f7a', 'admin', 'admin', 'admin@admin.com', '$argon2i$v=19$m=16,t=2,p=1$V3ZZRHplRENXWXNmbkdGcw$+fFtK+BCZI7Y1AMRoC6xMw', 3);


INSERT INTO users (darkmode, islanguagegerman, id, name, surname, email, passwordhash, role) VALUES (true, true, 'cb25da78-d6f4-4218-a40f-eb465844df76'::UUID, 'Markus', 'Weber', 'markus.weber@vertrieb.de', '$argon2i$v=19$m=16,t=2,p=1$V3ZZRHplRENXWXNmbkdGcw$+fFtK+BCZI7Y1AMRoC6xMw', 2), (true, true, 'f7dff772-3803-4946-8b5a-8f6605bcb9f3', 'Lisa', 'Klein', 'lisa.klein@vertrieb.de', '$argon2i$v=19$m=16,t=2,p=1$V3ZZRHplRENXWXNmbkdGcw$+fFtK+BCZI7Y1AMRoC6xMw', 2), (true, true, 'dfeb9edd-b750-4dca-8481-5ae5fa16704b'::UUID, 'Michael', 'Hoffmann', 'michael.hoffmann@vertrieb.de', '$argon2i$v=19$m=16,t=2,p=1$V3ZZRHplRENXWXNmbkdGcw$+fFtK+BCZI7Y1AMRoC6xMw', 2), (true, true, 'cf6c23b7-38d4-4db7-8b62-c4b28663af52'::UUID, 'Julia', 'Schneider', 'julia.schneider@vertrieb.de', '$argon2i$v=19$m=16,t=2,p=1$V3ZZRHplRENXWXNmbkdGcw$+fFtK+BCZI7Y1AMRoC6xMw', 2);

INSERT INTO customers (id, name, surname, email, phonenumber, address, company, contactstatus, assigned_manager_id) VALUES ('417cc0e3-4075-4c74-bfe7-e89e7f3662ff', 'Andreas', 'Schmidt', 'a.schmidt@technovertriebs.de', '+49 30 1234567', 'Berliner Straße 42, 10115 Berlin', 'TechnoVertrieb GmbH', 0, (SELECT id FROM users WHERE email = 'markus.weber@vertrieb.de')), ('a1fc7269-3ef5-46ec-869e-bd696cecd1ee'::UUID, 'Elena', 'Müller', 'e.mueller@industrieservice.com', '+49 89 9876543', 'Münchner Allee 17, 80331 München', 'IndustrieService AG', 1, (SELECT id FROM users WHERE email = 'lisa.klein@vertrieb.de')), ('4ba4e37f-95b2-4b45-b7f5-f81a70a5e023'::UUID, 'Thomas', 'Bauer', 'thomas.bauer@maschinenbau.de', '+49 211 5551212', 'Industriestraße 8, 40472 Düsseldorf', 'Präzisionsmaschinenbau e.K.', 2, (SELECT id FROM users WHERE email = 'michael.hoffmann@vertrieb.de')), ('3e1a9c81-0ad8-48a7-a6bf-2aa54acbbcf4'::UUID, 'Sarah', 'Wagner', 'sarah.wagner@softwarelösungen.de', '+49 69 7890123', 'Frankfurter Weg 55, 60313 Frankfurt', 'SoftwareLösungen Consulting', 1, (SELECT id FROM users WHERE email = 'julia.schneider@vertrieb.de'));

INSERT INTO transactions (id, customer_id, user_id, amount, status, timestamp, description) VALUES ('9065a6a0-7392-405b-acb0-f6468a7172ff', '417cc0e3-4075-4c74-bfe7-e89e7f3662ff', 'cb25da78-d6f4-4218-a40f-eb465844df76', 5999.99, 0, CURRENT_TIMESTAMP - INTERVAL '2 days 3 hours', 'Jahres-Abonnement für Cloud-Infrastruktur-Lösung mit erweiterter Sicherheitskomponente');
INSERT INTO transactions (id, customer_id, user_id, amount, status, timestamp, description) VALUES ('9d225cb2-545f-4a7d-a266-21e653ba89d4', 'a1fc7269-3ef5-46ec-869e-bd696cecd1ee', 'f7dff772-3803-4946-8b5a-8f6605bcb9f3', 12500.50, 1, CURRENT_TIMESTAMP - INTERVAL '1 day 6 hours', 'Vorläufige Vereinbarung für Unternehmensweite IT-Sicherheitsarchitektur und Beratungsleistungen');
INSERT INTO transactions (id, customer_id, user_id, amount, status, timestamp, description) VALUES ('c001934a-6137-4d79-a27e-6364235661dc', '4ba4e37f-95b2-4b45-b7f5-f81a70a5e023', 'dfeb9edd-b750-4dca-8481-5ae5fa16704b', 3750.75, 2, CURRENT_TIMESTAMP - INTERVAL '4 days 2 hours', 'Gescheitertes Netzwerk-Upgrade-Projekt aufgrund technischer Kompatibilitätsprobleme');
INSERT INTO transactions (id, customer_id, user_id, amount, status, timestamp, description) VALUES ('18086517-b4c2-4a72-a9bf-aef85c371944', '3e1a9c81-0ad8-48a7-a6bf-2aa54acbbcf4', 'cf6c23b7-38d4-4db7-8b62-c4b28663af52', 8250.00, 0, CURRENT_TIMESTAMP - INTERVAL '5 days 12 hours', 'Komplexe IT-Beratungsdienstleistung zur Optimierung der Unternehmens-Softwarearchitektur'); ('18086517-b4c2-4a72-a9bf-aef85c371944'::UUID, (SELECT id FROM customers WHERE email = 'sarah.wagner@softwarelösungen.de'), (SELECT id FROM users WHERE email = 'julia.schneider@vertrieb.de'), 8250.00, 0, CURRENT_TIMESTAMP - INTERVAL '5 days 12 hours', 'Komplexe IT-Beratungsdienstleistung zur Optimierung der Unternehmens-Softwarearchitektur');
INSERT INTO transactions (id, customer_id, user_id, amount, status, timestamp, description) VALUES ('18086517-b4c2-4a72-a9bf-aef85c371944', '417cc0e3-4075-4c74-bfe7-e89e7f3662ff', 'cb25da78-d6f4-4218-a40f-eb465844df76', 5999.99, 0, CURRENT_TIMESTAMP - INTERVAL '32 days 3 hours', 'Jahres-Abonnement für Cloud-Infrastruktur-Lösung mit erweiterter Sicherheitskomponente');


INSERT INTO appointments (endtime, starttime, id, description) VALUES ('2023-10-15 14:00:00', '2023-10-15 13:00:00', 'b8f2e0c9-6d8e-4b7f-9a6f-3d2e8c1a4f56'::UUID, 'Diskussion über die Einfuehrung neuer Produkte');
INSERT INTO appointments (endtime, starttime, id, description) VALUES ('2023-10-17 16:30:00', '2023-10-17 15:30:00', 'e2c8a1b6-8c6f-4d7e-a2d8-9e6f5c1b8a23'::UUID, 'Kundengespraech zum Servicevertrag');
INSERT INTO appointments (endtime, starttime, id, description) VALUES ('2023-10-20 11:00:00', '2023-10-20 10:00:00', '68479876-4edf-4d10-8d2e-d31b46bc4c33'::UUID, 'Rueckblick auf die Umsatzentwicklung');
INSERT INTO appointments (endtime, starttime, id, description) VALUES ('2023-10-22 13:30:00', '2023-10-22 12:30:00', '3cbab0ad-8927-472e-9959-333a0d207261'::UUID, 'Technische Support-Sitzung');

INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('b8f2e0c9-6d8e-4b7f-9a6f-3d2e8c1a4f56'::UUID, NULL, '2722bd27-d8f4-4d6f-9f9b-62582ee023ae'::UUID, 'cb25da78-d6f4-4218-a40f-eb465844df76'::UUID, 'ORGANIZER');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('b8f2e0c9-6d8e-4b7f-9a6f-3d2e8c1a4f56'::UUID, NULL, '63048879-b4c9-48df-bc36-7be71d9b6ccb'::UUID, 'f7dff772-3803-4946-8b5a-8f6605bcb9f3'::UUID, 'PARTICIPANT');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('e2c8a1b6-8c6f-4d7e-a2d8-9e6f5c1b8a23'::UUID, '417cc0e3-4075-4c74-bfe7-e89e7f3662ff'::UUID, '2e22c051-613c-42c7-a5a6-ea73e68defb1'::UUID, 'cb25da78-d6f4-4218-a40f-eb465844df76'::UUID, 'ORGANIZER');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('e2c8a1b6-8c6f-4d7e-a2d8-9e6f5c1b8a23'::UUID, NULL, '50fbea7d-5d7c-47e2-9379-c551f3b02567'::UUID, 'f7dff772-3803-4946-8b5a-8f6605bcb9f3'::UUID, 'PARTICIPANT');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('68479876-4edf-4d10-8d2e-d31b46bc4c33'::UUID, NULL, '3cbab0ad-8927-472e-9959-333a0d207261'::UUID, 'dfeb9edd-b750-4dca-8481-5ae5fa16704b'::UUID, 'ORGANIZER');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('68479876-4edf-4d10-8d2e-d31b46bc4c33'::UUID, NULL, 'fd312a80-1cf6-473f-abde-c7f4d1843fbe'::UUID, 'cf6c23b7-38d4-4db7-8b62-c4b28663af52'::UUID, 'PARTICIPANT');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('3cbab0ad-8927-472e-9959-333a0d207261'::UUID, '3e1a9c81-0ad8-48a7-a6bf-2aa54acbbcf4'::UUID, 'f48468fb-d95b-40c7-a39b-92502cfc6f05'::UUID, 'f7dff772-3803-4946-8b5a-8f6605bcb9f3'::UUID, 'ORGANIZER');
INSERT INTO appointment_participants (appointment_id, customer_id, id, user_id, "role") VALUES ('3cbab0ad-8927-472e-9959-333a0d207261'::UUID, NULL, 'd74f3a52-1db2-4fac-9a7a-07a8fe029c8d'::UUID, 'cb25da78-d6f4-4218-a40f-eb465844df76'::UUID, 'PARTICIPANT');
```

#### **6. Starten Sie die Anwendung**
- Navigieren Sie zum Projektverzeichnis.
- Führen Sie je nach Betriebssystem einen der folgenden Befehle aus:
  - **Linux/MacOS**:  
    ```bash
    ./gradlew run
    ```
  - **Windows**:  
    ```bash
    gradlew.bat run
    ```

---

Nachdem Sie diese Schritte abgeschlossen haben, sollte Ihre Backend-Anwendung bereit sein und laufen, falls sie die Testdaten importiert haben sollten sie sich mit den folgenden Anmeldedaten anmelden können:
- **E-Mail**: `admin@admin.com`
- **Passwort**: `tessa`

## Hinweise zur Verwendung
- Falls Sie die Anwendung in der Produktion verwenden möchten, stellen Sie sicher, dass
die CORS (Cross-Origin Resource Sharing) Einstellungen in der `Javalin.create` in `Main.java` angepasst wird, siehe [CORS Plugin Dokumentation](https://javalin.io/plugins/cors) von Javalin.
- Falls Fehler auftreten, überprüfen Sie die Konsolenausgaben auf Fehlermeldungen, falls
dieser nicht schluessig ist und kein [Stacktrace](https://de.wikipedia.org/wiki/Stacktrace) angezeigt wird suchen Sie die entsprechende Stelle wo `logger.error` aufgerufen wird und überprüfen Sie die dortige Fehlermeldung bzw. fuegen Sie einen
`exception.printStackTrace()` hinzu falls es sich um eine Exception handelt.
- Falls die Anwendung nicht startet, weil die `.ENV` Datei nicht gefunden wird, stellen Sie sicher, dass die Datei im Projektverzeichnis liegt und korrekt benannt ist, falls dies der fall ist und die Anwendung immer noch nicht startet, versuchen Sie den Pfad in der `Main.java` zu ändern