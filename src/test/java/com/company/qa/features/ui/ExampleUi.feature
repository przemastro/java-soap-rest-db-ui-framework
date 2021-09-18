Feature: Testy UI

  Background: Przejście na stronę główną
    Given Jestem na stronie głównej ""

  @SmokeTest
  Scenario Outline: Scenariusz z Examples i krokami commonowymi
    Given Ładuję dane testowe z pliku "<plik>"
    And Kliknę na przycisk customowy
    And Kliknę na przycisk commonowy

    Examples:
      | plik         |
      | testData.sql |


  @UiTest
  Scenario: Scenariusz testowy z wykorzystaniem scenario context
    And Sprawdzę czy na pewno jestem na głównej stornie

