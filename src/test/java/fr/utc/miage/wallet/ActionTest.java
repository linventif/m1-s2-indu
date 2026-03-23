package fr.utc.miage.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ActionTest {
  @Test
  void actionConstructorTest() {
    Action act = new Action("OVH", 10.0);
    assertNotNull(act);
  }

  @Test
  void actionGettersTest() {
    Action act = new Action("OVH", 10.0);
    assertEquals("OVH", act.getLabel());
    assertEquals(10.0, act.getPrice());
  }

  @Test
  void actionSetPriceTest() {
    Action act = new Action("OVH", 10.0);
    act.setPrice(15.0);
    assertEquals(15.0, act.getPrice());
  }

  @Test
  void actionToStringTest() {
    Action act = new Action("OVH", 10.0);
    assertEquals("Action: OVH (10.0€)", act.toString());
  }

  @Test
  void actionEqualsTest() {
    Action act1 = new Action("OVH", 10.0);
    Action act2 = new Action("OVH", 10.0);
    Action act3 = new Action("Google", 15.0);
    assertNotEquals(act2, act3);
    assertNotEquals(act1, act3);
    assertEquals(act1, act2);
    assertEquals(act1.getLabel(), act2.getLabel());
    assertEquals(act1.getPrice(), act2.getPrice());
  }

  // hascode test
  @Test
  void actionHashCodeTest() {
    Action act1 = new Action("OVH", 10.0);
    Action act2 = new Action("OVH", 10.0);
    assertEquals(act1.hashCode(), act2.hashCode());
  }
}
