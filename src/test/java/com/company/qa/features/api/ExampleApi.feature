Feature: ExampleApi

  @ApiTest
  Scenario: Scenariusz REST
    When Wyślę zapytanie "GET" na endpoint "" z parametrami z pliku ".txt"
    Then Otrzymam odpowiedź o kodzie statusu "200"


  @ApiTest2
  Scenario: Scenariusz SOAP
    When Przygotuję ciało zapytania na bazie szablonu z pliku ".xml" i zapiszę do pliku "request.xml"
    And Wyślę zapytanie na endpoint "" z ciałem z pliku "request.xml" i zapiszę odpowiedź do pliku "response.xml"

