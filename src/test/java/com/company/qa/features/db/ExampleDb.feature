Feature: Testy DB

  @ApiTest
  Scenario: Scenariusz MSSQL
    Given Ładuję dane testowe z pliku ""
    And Wartość w kolumnie "" tabeli "" jest równa ""
    Then Czyszczę bazę skryptem "" z ustawionym filtrem ""
