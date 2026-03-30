package fr.utc.miage.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class WalletTest {
  @Test
  void testAddAction() {
    Wallet wallet = new Wallet();
    Action action1 = new Action("Action 1", 10.0);
    wallet.addAction(action1, 5);
    assertEquals(5, wallet.getActions().get(action1));
  }

  @Test
  void testAddActionNegativeQuantityShouldThrow() {
    Wallet wallet = new Wallet();
    Action action = new Action("Negative quantity action", 10.0);

    assertThrows(IllegalArgumentException.class, () -> wallet.addAction(action, -1));
  }

  @Test
  void testGetTotalValue() {
    Wallet wallet = new Wallet();
    Action action1 = new Action("Action 1", 10.0);
    Action action2 = new Action("Action 2", 20.0);
    wallet.addAction(action1, 5);
    wallet.addAction(action2, 3);
    assertEquals(110.0, wallet.getTotalValue(), 0.001);
  }

  @Test
  void testRemoveActionShouldKeepZeroQuantity() {
    Wallet wallet = new Wallet();
    Action action = new Action("Action 3", 10.0);
    wallet.addAction(action, 2);

    wallet.removeAction(action, 2);

    assertEquals(0, wallet.getActions().get(action));
  }

  @Test
  void testRemoveActionNullShouldThrow() {
    Wallet wallet = new Wallet();

    assertThrows(IllegalArgumentException.class, () -> wallet.removeAction(null, 1));
  }

  @Test
  void testRemoveActionQuantityZeroShouldThrow() {
    Wallet wallet = new Wallet();
    Action action = new Action("Action 4", 10.0);

    assertThrows(IllegalArgumentException.class, () -> wallet.removeAction(action, 0));
  }

  @Test
  void testRemoveActionShouldThrowWhenActionIsMissing() {
    Wallet wallet = new Wallet();
    Action action = new Action("Action 5", 10.0);

    assertThrows(IllegalArgumentException.class, () -> wallet.removeAction(action, 1));
  }

  @Test
  void testRemoveActionShouldThrowWhenQuantityIsInsufficient() {
    Wallet wallet = new Wallet();
    Action action = new Action("Action 6", 10.0);
    wallet.addAction(action, 1);

    assertThrows(IllegalArgumentException.class, () -> wallet.removeAction(action, 2));
  }

  @Test
  void testWalletToStringEqualsAndHashCode() {
    Wallet wallet1 = new Wallet();
    Wallet wallet2 = new Wallet();
    Wallet wallet3 = new Wallet();
    Action action = new Action("Action 7", 10.0);
    wallet1.addAction(action, 2);
    wallet2.addAction(action, 2);

    assertEquals("Wallet actions: " + wallet1.getActions(), wallet1.toString());
    assertEquals(wallet1, wallet1);
    assertEquals(wallet1, wallet2);
    assertEquals(wallet1.hashCode(), wallet2.hashCode());
    assertNotEquals(null, wallet1);
    assertNotEquals("wallet", wallet1);
    assertNotEquals(wallet1, wallet3);
  }
}
