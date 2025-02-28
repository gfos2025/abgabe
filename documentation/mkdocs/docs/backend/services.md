# Dienste
Worksale ist MVC/MVSC basiert was bedeutet, dass die Anwendung in Model, Services (Dienste) und Controller unterteilt ist. Die Services sind eine Erweiterung des MVC/MVSC Musters und enthalten die Kernlogik der Anwendung. Sie koordinieren den Datenaustausch zwischen Models und Controllers.

## Übersicht

### UserService
Der UserService ist zuständig für alle Benutzeroperationen. Er ermöglicht das Erstellen, Lesen, Aktualisieren und Löschen von Benutzerdaten. 

!![UserService UML](../../images/uml/backend/services/UserService.svg)

### TransactionService
Der `TransactionService` übernimmt die Verwaltung von Transaktionen. Er führt Transaktionsoperationen wie das Erstellen, Abrufen und Aktualisieren von Transaktionen durch.

!![TransactionService UML](../../images/uml/backend/services/TransactionService.svg)

### AppointmentService
Der `AppointmentService` ist verantwortlich für die Verwaltung von Terminen. Er ermöglicht das Erstellen, Abrufen und Aktualisieren von Terminen.
!![AppointmentService UML](../../images/uml/backend/services/AppointmentService.svg)

### CustomerService
Der `CustomerService` kümmert sich um die Verwaltung von Kunden. Er ermöglicht das Erstellen, Abrufen und Aktualisieren von Kundendaten.

!![CustomerService UML](../../images/uml/backend/services/CustomerService.svg)

### LogService
Der `LogService` ist verantwortlich für die Verwaltung von Log-Daten. Er ermöglicht das Abrufen sowie Erstellen von Log-Daten.
!![LogService UML](../../images/uml/backend/services/LogService.svg)

## Zusammenfassung
Jeder Service in diesem System hat eine klare Aufgabe und interagiert mit den entsprechenden Modellen oder anderen Diensten, um die notwendigen CRUD-Vorgänge durchzuführen.