# DEZSYS_GK771_WAREHOUSE_REST

Dieses Projekt implementiert eine REST-Schnittstelle für ein Warenlager mittels Spring Boot. Die API stellt Lagerdaten in den Formaten JSON und XML bereit. Ein client-seitiger Consumer in Form einer Webseite dient zur Visualisierung und Filterung der Daten.

## Verwendete Technologien

*   Java & Spring Boot
*   Gradle
*   HTML, CSS, JavaScript
*   jQuery

## Inbetriebnahme

#### 1. Backend (REST API) starten

Die Spring Boot Anwendung wird über das Gradle-Wrapper-Skript gestartet.

```bash
./gradlew bootRun
```
Die API ist anschließend unter `http://localhost:8080` verfügbar.

#### 2. Frontend (Consumer) öffnen

Die Datei `index.html` aus dem Verzeichnis `consumer-frontend` kann direkt in einem Webbrowser geöffnet werden, um mit der laufenden API zu interagieren.

## Wichtige Arbeitsschritte und Implementierungsdetails

#### 1. Definition der REST-Endpunkte

Der `WarehouseController` dient als Einstiegspunkt für alle HTTP-Anfragen. Die Annotation `@RestController` kennzeichnet die Klasse als REST-Schnittstelle, die direkt Datenobjekte serialisiert zurückgibt.

```java
@RestController
public class WarehouseController {
    // ...
}
```
Mittels `@RequestMapping` werden URL-Pfade an Controller-Methoden gebunden. Die Verwendung von Path-Variablen wie `{inID}` ermöglicht die Erstellung dynamischer Endpunkte.

```java
@RequestMapping(value="/warehouse/{inID}/data", ...)
public WarehouseData warehouseData(@PathVariable String inID) {
    return service.getWarehouseData(inID);
}
```

#### 2. Simulation der Datenquelle

Die `WarehouseSimulation`-Klasse agiert als Mock-Datenquelle. Sie generiert je nach angefragter `inID` unterschiedliche Datensätze für verschiedene Standorte. Dies demonstriert, dass die API dynamisch auf Parameter reagieren kann.

```java
public WarehouseData getData(String inID) {
    WarehouseData data = new WarehouseData();
    if ("001".equals(inID)) {
        data.setWarehouseName("Linz Bahnhof");
        // ... Logik zur Erzeugung von Linz-spezifischen Daten
    } else if ("002".equals(inID)) {
        data.setWarehouseName("Wien Westbahnhof");
        // ... Logik zur Erzeugung von Wien-spezifischen Daten
    }
    return data;
}
```

#### 3. Aktivierung der XML-Unterstützung

Um neben JSON auch XML als Ausgabeformat zu ermöglichen, wurde die `jackson-dataformat-xml`-Bibliothek zur `build.gradle` hinzugefügt. Spring Boot erkennt diese Abhängigkeit und konfiguriert die XML-Serialisierung automatisch.

```groovy
// in build.gradle
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'
```
Die `produces`-Eigenschaft der `@RequestMapping` wurde erweitert. Dies ermöglicht Content Negotiation, sodass der Client über den `Accept`-Header das gewünschte Format (JSON oder XML) anfordern kann.

```java
@RequestMapping(value="/warehouse/{inID}/data", produces = { 
    MediaType.APPLICATION_JSON_VALUE, 
    MediaType.APPLICATION_XML_VALUE 
})
```

#### 4. Entwicklung des API-Consumers

Das Frontend nutzt jQuery, um asynchrone `AJAX`-Anfragen an die API zu senden. Nach erfolgreicher Antwort (`success`) werden die empfangenen JSON-Daten verarbeitet, um dynamisch die HTML-Tabelle mit den Lagerbeständen zu erstellen.

```javascript
$.ajax({
    url: `http://localhost:8080/warehouse/${locationId}/data`, // Dynamische URL
    method: "GET",
    dataType: "json",
    success: function(data) {
        // Logik zur Darstellung der Daten in der Tabelle
        renderTable(data.productData);
    }
});
```

## Aufgetretene Probleme und deren Lösungen

1.  **Problem: `406 Not Acceptable` Fehler bei XML-Anfragen.**
    *   **Ursache:** Dem Server fehlte ein Mechanismus (ein Serializer), um Java-Objekte in das vom Client angeforderte XML-Format zu konvertieren.
    *   **Lösung:** Hinzufügen der `jackson-dataformat-xml`-Abhängigkeit. Die Auto-Konfiguration von Spring Boot hat die Bibliothek erkannt und den notwendigen Serializer bereitgestellt.

2.  **Problem: API-Anfragen vom Frontend wurden vom Browser blockiert (CORS).**
    *   **Ursache:** Die "Same-Origin Policy" des Browsers verhinderte, dass ein von `file://` geladenes Dokument auf eine Ressource von `http://localhost:8080` zugreift.
    *   **Lösung:** Einsatz der `@CrossOrigin(origins = "*")`-Annotation im `WarehouseController`. Diese Anweisung konfiguriert das Backend so, dass es den `Access-Control-Allow-Origin`-Header sendet, wodurch der Browser die Anfrage zulässt.

## Neue Erkenntnisse

*   **3-Schichten-Architektur:** Die Strukturierung in **Controller** (Präsentationsschicht), **Service** (Logikschicht) und **Simulation** (Datenschicht) sorgt für eine klare Trennung der Verantwortlichkeiten (Separation of Concerns) und eine hohe Wartbarkeit.

*   **Dependency Injection:** Das Spring-Framework verwaltet den Lebenszyklus von Objekten. Durch `@Autowired` werden Abhängigkeiten (wie der `WarehouseService`) automatisch in die benötigenden Klassen "injiziert", was die Kopplung zwischen den Komponenten reduziert.

*   **Content Negotiation:** Eine REST-API sollte eine Ressource über eine einzige, eindeutige URL identifizieren. Die Repräsentation dieser Ressource (JSON, XML) wird idealerweise über HTTP-Header (`Accept`) ausgehandelt, anstatt separate URLs für jedes Format zu erstellen.