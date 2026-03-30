package fr.utc.miage.wallet;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ActionTest {

  private static final String CORRECT_LABEL = "OVH";
  private static final String OTHER_CORRECT_LABEL = "Google";
  private static final Double CORRECT_PRICE = 10.0;
  private static final Double OTHER_CORRECT_PRICE = 15.0;

  public static Action getCorrectAction() {
    return new Action(CORRECT_LABEL, CORRECT_PRICE);
  }

  @Test
  void actionConstructorTest() {
    assertDoesNotThrow(() -> new Action(CORRECT_LABEL, CORRECT_PRICE));
  }

  @Test
  void actionConstructorWithCategoryTest() {
    Action act = assertDoesNotThrow(() -> new Action(CORRECT_LABEL, CORRECT_PRICE, ActionCategory.INDUSTRIAL));
    assertEquals(ActionCategory.INDUSTRIAL, act.getCategory());
  }

  @Test
  void actionConstructorInvalidLabelTest() {

    assertThrows(IllegalArgumentException.class, () -> {
      new Action(null, CORRECT_PRICE);
    }, "Name shoudn't be null");
    assertThrows(IllegalArgumentException.class, () -> {
      new Action("", CORRECT_PRICE);
    }, "Name shoudn't be null");

  }

  @Test
  void actionConstructorInvalidPriceTest() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Action(CORRECT_LABEL, -10.0);
    }, "Price shoudn't be negative");
  }

  @Test
  void actionGettersTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertEquals(CORRECT_LABEL, act.getLabel());
    assertEquals(CORRECT_PRICE, act.getPrice());
  }

  @Test
  void actionSetPriceTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    act.setPrice(OTHER_CORRECT_PRICE);
    assertEquals(OTHER_CORRECT_PRICE, act.getPrice());
  }

  @Test
  void actionToStringTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertEquals("Action: " + CORRECT_LABEL + " (" + CORRECT_PRICE + "€)", act.toString());
  }

  @Test
  void actionDeleteTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    act.delete();
    Action act2 = Action.getActionByLabel(CORRECT_LABEL);
    assertNull(act2);
  }

  @Test
  void actionEqualsTest() {
    Action act1 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    Action act2 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    Action act3 = new Action(OTHER_CORRECT_LABEL, CORRECT_PRICE);
    assertNotEquals(act2, act3);
    assertNotEquals(act1, act3);
    assertEquals(act1, act2);
    assertEquals(act1.getLabel(), act2.getLabel());
    assertEquals(act1.getPrice(), act2.getPrice());
  }

  // hashCode test
  @Test
  void actionHashCodeTest() {
    Action act1 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    Action act2 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertEquals(act1.hashCode(), act2.hashCode());
  }

  /**
   * addHistoricalPrices should throw an exception if the price is negative or if the date is null.
   */
  @Test
  void addHistoricalPricesInvalidInputTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertThrows(IllegalArgumentException.class, () -> {
      act.addHistoricalPrice(null, 100.0);
    }, "Date cannot be null");
    assertThrows(IllegalArgumentException.class, () -> {
      act.addHistoricalPrice(Date.valueOf("2023-01-01"), -100.0);
    }, "Price cannot be negative");
  }
  
  /**
   * Test the retrieval of historical prices.
   */
  @Test
  void getHistoricalPricesTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    act.addHistoricalPrice(Date.valueOf("2023-01-01"), 100.0);
    act.addHistoricalPrice(Date.valueOf("2023-01-02"), 101.0);
    Map<Date, Double> historicalPrices = act.getHistoricalPrices();
    assertEquals(2, historicalPrices.size());
    assertEquals(100.0, historicalPrices.get(Date.valueOf("2023-01-01")));
    assertEquals(101.0, historicalPrices.get(Date.valueOf("2023-01-02")));
  } 

  @Test
  void getActionsByCategoryTest() {
    final String industrialLabel = "Industrial Test Action";
    final String otherLabel = "Other Test Action";

    Action existingIndustrial = Action.getActionByLabel(industrialLabel);
    if (existingIndustrial != null) {
      existingIndustrial.delete();
    }
    Action existingOther = Action.getActionByLabel(otherLabel);
    if (existingOther != null) {
      existingOther.delete();
    }

    Action industrialAction = new Action(industrialLabel, CORRECT_PRICE, ActionCategory.INDUSTRIAL);
    Action otherAction = new Action(otherLabel, CORRECT_PRICE, ActionCategory.OTHER);

    Map<String, Action> industrialActions = Action.getActionsByCategory(ActionCategory.INDUSTRIAL);

    assertTrue(industrialActions.containsKey(industrialLabel));
    assertEquals(ActionCategory.INDUSTRIAL, industrialActions.get(industrialLabel).getCategory());
    assertFalse(industrialActions.containsKey(otherLabel));

    industrialAction.delete();
    otherAction.delete();
  }

  @Test
 void getHistoricalPricesStringEmptyTest() {
   Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
   String expected = null;
   assertEquals(expected, act.getHistoricalPricesString());
 }


 @Test
 void getHistoricalPricesStringSingleEntryTest() {
   Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
   Date date = Date.valueOf("2023-01-01");
   act.addHistoricalPrice(date, 123.45);
   String result = act.getHistoricalPricesString();
   assertTrue(result.contains("Action: " + CORRECT_LABEL));
   assertTrue(result.contains("Historical Prices:"));
   assertTrue(result.contains(date.toString() + ": 123.45€"));
 }


 @Test
 void getHistoricalPricesStringMultipleEntriesTest() {
   Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
   Date date1 = Date.valueOf("2023-01-01");
   Date date2 = Date.valueOf("2023-01-02");
   act.addHistoricalPrice(date1, 100.0);
   act.addHistoricalPrice(date2, 200.0);
   String result = act.getHistoricalPricesString();
   assertTrue(result.contains(date1.toString() + ": 100.0€"));
   assertTrue(result.contains(date2.toString() + ": 200.0€"));
   assertTrue(result.startsWith("Action: " + CORRECT_LABEL));
 }

}
