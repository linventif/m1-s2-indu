package fr.utc.miage.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ActionTest {
  @Test
  void actionConstructorTest() {
    Action act = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    assertNotNull(act);
  }

  @Test
  void actionConstructorWithCategoryTest() {
    Action act = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    assertNotNull(act);
    assertEquals("INDUSTRIAL", act.getCategory());
  }

  @Test
  void actionConstructorInvalidLabelTest() {
    try {
      new Action(null, 10.0, ActionCategory.OTHER);
    } catch (IllegalArgumentException e) {
      assertEquals("Label cannot be null or empty", e.getMessage());
    }
    try {
      new Action("", 10.0, ActionCategory.OTHER);
    } catch (IllegalArgumentException e) {
      assertEquals("Label cannot be null or empty", e.getMessage());
    }
  }

  @Test
  void actionConstructorInvalidPriceTest() {
    try {
      new Action("OVH", -10.0, ActionCategory.INDUSTRIAL);
    } catch (IllegalArgumentException e) {
      assertEquals("Price cannot be negative", e.getMessage());
    }
  }

  @Test
  void actionGettersTest() {
    Action act = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    assertEquals("OVH", act.getLabel());
    assertEquals(10.0, act.getPrice());
    assertEquals("INDUSTRIAL", act.getCategory());
  }

  @Test
  void actionSetPriceTest() {
    Action act = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    act.setPrice(15.0);
    assertEquals(15.0, act.getPrice());
  }

  @Test
  void actionToStringTest() {
    Action act = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    assertEquals("Action: OVH (10.0€), Category: INDUSTRIAL", act.toString());
  }

  @Test
  void actionDeleteTest() {
    Action act = new Action("OVH", 10, ActionCategory.INDUSTRIAL);
    act.delete();
    Action act2 = Action.getActionByLabel("OVH");
    assertNull(act2);
  }

  @Test
  void actionEqualsTest() {
    Action act1 = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    Action act2 = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    Action act3 = new Action("Google", 15.0, ActionCategory.INDUSTRIAL);
    assertNotEquals(act2, act3);
    assertNotEquals(act1, act3);
    assertEquals(act1, act2);
    assertEquals(act1.getLabel(), act2.getLabel());
    assertEquals(act1.getPrice(), act2.getPrice());
  }

  // hascode test
  @Test
  void actionHashCodeTest() {
    Action act1 = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    Action act2 = new Action("OVH", 10.0, ActionCategory.INDUSTRIAL);
    assertEquals(act1.hashCode(), act2.hashCode());
  }
}
