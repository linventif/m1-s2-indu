package fr.utc.miage.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class WallerTest {
  @Test
  void testAddAction() {
    Wallet wallet = new Wallet();
    Action action1 = new Action("Action 1", 10.0);
    wallet.addAction(action1, 5);
    assertEquals(5, wallet.getListAction().get(action1));
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
}
