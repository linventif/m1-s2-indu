package fr.utc.miage.wallet;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

  // hascode test
  @Test
  void actionHashCodeTest() {
    Action act1 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    Action act2 = new Action(CORRECT_LABEL, CORRECT_PRICE);
    assertEquals(act1.hashCode(), act2.hashCode());
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
}
