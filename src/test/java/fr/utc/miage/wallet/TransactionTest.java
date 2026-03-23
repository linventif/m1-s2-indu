package fr.utc.miage.wallet;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.utc.miage.wallet.Transaction.TypeTransaction;

class TransactionTest {

  private static final TypeTransaction CORRECT_TYPE = Transaction.TypeTransaction.BUY;
  private static final Action CORRECT_ACTION = new Action("APPLE", 100);
  private static final Double CORRECT_AMOUNT = 12.2;
  private static final Integer CORRECT_QUANTITY = 20;

  public static Transaction getCorrectTransaction(){
    return new Transaction(CORRECT_TYPE, CORRECT_ACTION, CORRECT_AMOUNT, CORRECT_QUANTITY);
  }

  @Test
  void testConstructorShouldWork() {
    assertDoesNotThrow(() -> {
      new Transaction(CORRECT_TYPE, CORRECT_ACTION, CORRECT_AMOUNT, CORRECT_QUANTITY);
    }, "Should be ok");
  }

  @Test
  void testGetterShouldWork() {
    Transaction transaction = this.getCorrectTransaction();
    assertEquals(transaction.getType(), CORRECT_TYPE);
    assertEquals(transaction.getAction(), CORRECT_ACTION);
    assertEquals(transaction.getAmount(), CORRECT_AMOUNT);
    assertEquals(transaction.getQuantity(), CORRECT_QUANTITY);
    assertEquals(transaction.getDate(), LocalDate.now());
  }

}
