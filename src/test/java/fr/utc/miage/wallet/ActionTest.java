package fr.utc.miage.wallet;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ActionTest {

  private static final String CORRECT_LABEL = "OVH";
  private static final String OTHER_CORRECT_LABEL = "Google";
  private static final Double CORRECT_PRICE = 10.0;
  private static final Double OTHER_CORRECT_PRICE = 15.0;

  // CSV file paths for testing
  private static final String VALID_CSV = "src/test/resources/prices_valid.csv";
  private static final String MALFORMED_CSV = "src/test/resources/prices_malformed.csv";
  private static final String EMPTY_CSV = "src/test/resources/only_header.csv";
  private static final String NON_EXISTENT_CSV = "src/test/resources/ghost.csv";

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
  void actionConstructorWithIntPriceTest() {
    Action act = new Action("Integer Price Action", 12);

    assertEquals("Integer Price Action", act.getLabel());
    assertEquals(12.0, act.getPrice());
    assertEquals(ActionCategory.OTHER, act.getCategory());
    assertEquals(Action.TypeAction.SIMPLE, act.getType());
    assertNull(act.getComposition());
  }

  @Test
  void actionConstructorInvalidLabelTest() {
    assertThrows(IllegalArgumentException.class, () -> new Action(null, CORRECT_PRICE), "Name shoudn't be null");
    assertThrows(IllegalArgumentException.class, () -> new Action("", CORRECT_PRICE), "Name shoudn't be null");
  }

  @Test
  void actionConstructorInvalidPriceTest() {
    assertThrows(IllegalArgumentException.class, () -> new Action(CORRECT_LABEL, -10.0), "Price shoudn't be negative");
    assertThrows(IllegalArgumentException.class, () -> new Action(CORRECT_LABEL, (Double) null),
        "Price shoudn't be null");
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
  void actionSetPriceNegativeShouldThrowTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);

    assertThrows(IllegalArgumentException.class, () -> act.setPrice(-1.0));
  }

  @Test
  void actionSetCategoryAndCompositionTest() {
    Action act = new Action("Action With Composition", CORRECT_PRICE);
    Map<String, Float> composition = new HashMap<>();
    composition.put("Part A", 1.0f);

    act.setCategory(ActionCategory.FOOD);
    act.setComposition(composition);

    assertEquals(ActionCategory.FOOD, act.getCategory());
    assertEquals(composition, act.getComposition());
    assertEquals("Action: Action With Composition (" + CORRECT_PRICE + "€) Composition " + composition, act.toStringC());
  }

  @Test
  void actionSetCategoryNullShouldWorkTest() {
    Action act = new Action("Nullable Category Action", CORRECT_PRICE);

    act.setCategory(null);

    assertNull(act.getCategory());
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

  @Test
  void actionEqualsEdgeCasesTest() {
    Action act = new Action("Edge Action", CORRECT_PRICE);

    assertEquals(act, act);
    assertNotEquals(null, act);
    assertNotEquals("not an action", act);
  }

  @Test
  void actionEqualsShouldDetectDifferentPriceTypeCategoryAndComposition() {
    Action simpleReference = new Action("Comparable Action", CORRECT_PRICE);
    Action differentPrice = new Action("Comparable Action", OTHER_CORRECT_PRICE);
    Action differentCategory = new Action("Comparable Action", CORRECT_PRICE, ActionCategory.INDUSTRIAL);

    Map<String, Float> compositionA = new HashMap<>();
    compositionA.put("Part A", 1.0f);
    Action differentType = new Action("Comparable Action", CORRECT_PRICE, compositionA);

    Map<String, Float> compositionB = new HashMap<>();
    compositionB.put("Part B", 1.0f);
    Action composedReference = new Action("Composed Action", CORRECT_PRICE, compositionA);
    Action differentComposition = new Action("Composed Action", CORRECT_PRICE, compositionB);

    assertNotEquals(simpleReference, differentPrice);
    assertNotEquals(simpleReference, differentCategory);
    assertNotEquals(simpleReference, differentType);
    assertNotEquals(composedReference, differentComposition);
  }

  @Test
  void actionEqualsShouldDetectNullAndNonNullCompositionDifference() {
    Action withoutComposition = new Action("Nullable Composition Action", CORRECT_PRICE);
    Action withComposition = new Action("Nullable Composition Action", CORRECT_PRICE);
    Map<String, Float> composition = new HashMap<>();
    composition.put("Part A", 1.0f);
    withComposition.setComposition(composition);

    assertNotEquals(withoutComposition, withComposition);
  }

  @Test
  void actionEqualsAndHashCodeShouldHandleNullInternalFields() throws ReflectiveOperationException {
    Action first = new Action("Reflection Action 1", CORRECT_PRICE);
    Action second = new Action("Reflection Action 2", CORRECT_PRICE);
    Action nonNullLabelAction = new Action("Reflection Action 3", CORRECT_PRICE);

    first.setCategory(null);
    second.setCategory(null);

    setField(first, "label", null);
    setField(second, "label", null);
    setField(first, "type", null);
    setField(second, "type", null);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
    assertNotEquals(first, nonNullLabelAction);
    assertDoesNotThrow(first::hashCode);
  }

  @Test
  void actionEqualsAndHashCodeShouldWorkWithSameNonNullComposition() {
    Map<String, Float> firstComposition = new HashMap<>();
    firstComposition.put("Part A", 1.0f);
    Map<String, Float> secondComposition = new HashMap<>();
    secondComposition.put("Part A", 1.0f);

    Action first = new Action("Equal Composed Action", CORRECT_PRICE, firstComposition);
    Action second = new Action("Equal Composed Action", CORRECT_PRICE, secondComposition);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  // hashCode test
  @Test
  void actionHashCodeTest() {
    Action act1 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    Action act2 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertEquals(act1.hashCode(), act2.hashCode());
  }

  @Test
  void actionHashCodeShouldWorkWithNullCategoryAndCompositionTest() {
    Action act = new Action("Hash Action", CORRECT_PRICE);
    act.setCategory(null);
    act.setComposition(null);

    assertDoesNotThrow(act::hashCode);
  }

  private static void setField(final Action action, final String fieldName, final Object value)
      throws ReflectiveOperationException {
    Field field = Action.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(action, value);
  }

  /**
   * addHistoricalPrices should throw an exception if the price is negative or if the date is null.
   */
  @Test
  void addHistoricalPricesInvalidInputTest() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertThrows(IllegalArgumentException.class, () -> act.addHistoricalPrice(null, 100.0), "Date cannot be null");
    Date testDate = Date.valueOf("2023-01-01");
    assertThrows(IllegalArgumentException.class, () -> act.addHistoricalPrice(testDate, -100.0),
        "Price cannot be negative");
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

  /**
    * Test the retrieval of price at a specific date, with and without historical prices.
    */
  @Test
  void getPriceAtDateTest() {
    Action act = new Action("Historical Price Action", CORRECT_PRICE);
    Date date = Date.valueOf("2023-01-01");
    act.addHistoricalPrice(date, 100.0);
    assertEquals(100.0, act.getPriceAtDate(date));
    assertEquals(CORRECT_PRICE, act.getPriceAtDate(Date.valueOf("2022-12-31")));
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

  /**
    * Test the csv import of historical prices.
    */
  @Test
  void testImportValidCsv() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    act.importHistoricalPrices(VALID_CSV);

    assertEquals(100.0, act.getPriceAtDate(Date.valueOf("2023-01-01")));
    assertEquals(101.0, act.getPriceAtDate(Date.valueOf("2023-01-02")));
    assertEquals(99.0, act.getPriceAtDate(Date.valueOf("2023-01-03")));
  }

  /**
    * Test the csv import of historical prices.
    */
  @Test
  void testImportMalformedCsv() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    act.importHistoricalPrices(MALFORMED_CSV);

    assertEquals(100.0, act.getPriceAtDate(Date.valueOf("2023-01-01")));
    assertEquals(99.0, act.getPriceAtDate(Date.valueOf("2023-01-02")));
  }

  /**
    * Test the csv import of historical prices with a non-existent file.
    */
  @Test
  void testImportEmptyCsv() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    act.importHistoricalPrices(EMPTY_CSV);
    assertEquals(0, act.getHistoricalPrices().size());
  }

  /**
    * Test the csv import of historical prices with a non-existent file.
    */
  @Test
  void testImportNonExistentFile() {
    Action act = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertDoesNotThrow(() -> act.importHistoricalPrices(NON_EXISTENT_CSV));
  }

}
