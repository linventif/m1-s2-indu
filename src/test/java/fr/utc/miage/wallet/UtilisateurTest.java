package fr.utc.miage.wallet;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class UtilisateurTest {
  private final String NAME = "Doe";
  private final String FIRST_NAME = "John";
  private final Integer CORRECT_QUANTITY = 3;
  private final Date BIRTHDAY = Date.valueOf("2000-01-01");

  public Utilisateur getCorrectUtilisateur() {
    return new Utilisateur(FIRST_NAME, NAME, BIRTHDAY);
  }

  @Test
  void testGetBirthday() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Date result = utilisateur.getBirthday();
    assertEquals(BIRTHDAY, result);
  }

  @Test
  void testGetFirstName() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    String result = utilisateur.getFirstName();
    assertEquals(FIRST_NAME, result);
  }

  @Test
  void testGetName() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    String result = utilisateur.getName();
    assertEquals(NAME, result);
  }

  @Test
  void testGetWallet() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Wallet result = utilisateur.getWallet();
    assertEquals(new Wallet(), result);
  }

  @Test
  void testSetWallet() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Wallet newWallet = new Wallet();
    utilisateur.setWallet(newWallet);
    assertEquals(newWallet, utilisateur.getWallet());
  }

  @Test
  void testBuyActionNullShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    assertThrows(IllegalArgumentException.class, () -> {
      utilisateur.buyAction(null, CORRECT_QUANTITY);
    });
  }

  @Test
  void testBuyActionQuantityNullShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    assertThrows(IllegalArgumentException.class, () -> {
      utilisateur.buyAction(action, null);
    });
  }

  @Test
  void testBuyActionShouldNWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    assertDoesNotThrow(() -> {
      utilisateur.buyAction(action, CORRECT_QUANTITY);
    });
  }

  @Test
  void testGetCashAmount() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Double result = utilisateur.getCashAmount();
    Double expected = Utilisateur.INITIAL_CASH_AMOUNT;
    assertEquals(expected, result, 0.001);
  }

  @Test
  void testAddCashAmount100euro() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Double initialAmount = utilisateur.getCashAmount();
    Double amountToAdd = 100.00;
    utilisateur.addCashAmount(amountToAdd);
    Double result = utilisateur.getCashAmount();
    Double expected = initialAmount + amountToAdd;
    assertEquals(expected, result, 0.001);
  }

  @Test
  void testAddCashAmount1cent() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Double initialAmount = utilisateur.getCashAmount();
    Double amountToAdd = 0.01;
    utilisateur.addCashAmount(amountToAdd);
    Double result = utilisateur.getCashAmount();
    Double expected = initialAmount + amountToAdd;
    assertEquals(expected, result, 0.001);
  }

  @Test
  void testAddCashAmount0euro() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Double amountToAdd = 0.00;
    assertThrows(IllegalArgumentException.class, () -> utilisateur.addCashAmount(amountToAdd));
  }

  @Test
  void testAddCashAmountNegative100euro() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Double amountToAdd = -100.00;
    assertThrows(IllegalArgumentException.class, () -> utilisateur.addCashAmount(amountToAdd));
  }

  @Test
  void testSellActionShouldWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, CORRECT_QUANTITY);

    assertDoesNotThrow(() -> utilisateur.sellAction(action, 1));
  }

  @Test
  void testSellActionShouldAddCashAmount() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, CORRECT_QUANTITY);
    Double cashAfterBuy = utilisateur.getCashAmount();

    utilisateur.sellAction(action, 2);

    assertEquals(cashAfterBuy + (action.getPrice() * 2), utilisateur.getCashAmount(), 0.001);
  }

  @Test
  void testSellActionShouldDecreaseWalletQuantity() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, CORRECT_QUANTITY);

    utilisateur.sellAction(action, 2);

    assertEquals(1, utilisateur.getWallet().getActions().get(action));
  }

  @Test
  void testSellActionShouldKeepActionWithZeroQuantityWhenSellingAllOwnedQuantity() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, CORRECT_QUANTITY);

    utilisateur.sellAction(action, CORRECT_QUANTITY);

    assertEquals(0, utilisateur.getWallet().getActions().get(action));
  }

  @Test
  void testSellActionActionNull81ShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();

    assertThrows(IllegalArgumentException.class, () -> utilisateur.sellAction(null, CORRECT_QUANTITY));
  }

  @Test
  void testSellActionQuantiteNull82ShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();

    assertThrows(IllegalArgumentException.class, () -> utilisateur.sellAction(action, null));
  }

  @Test
  void testSellActionQuantityZeroShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();

    assertThrows(IllegalArgumentException.class, () -> utilisateur.sellAction(action, 0));
  }

  @Test
  void testSellActionShouldNotWorkWhenQuantityOwnedIsInsufficient() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, 1);

    assertThrows(IllegalArgumentException.class, () -> utilisateur.sellAction(action, CORRECT_QUANTITY));
  }

}
