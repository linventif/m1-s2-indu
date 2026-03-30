package fr.utc.miage.wallet;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class UtilisateurTest {
  private final String NAME = "Doe";
  private final String FIRST_NAME = "John";
  private final Integer CORRECT_QUANTITY = 3;
  private final Integer OTHER_QUANTITY = 21;
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
  void testNameAndFirstNameAreStrings() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    assertTrue(utilisateur.getName() instanceof String);
    assertTrue(utilisateur.getFirstName() instanceof String);
  }

  @Test
  void testBirthdayIsDate() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    assertTrue(utilisateur.getBirthday() instanceof Date);
  }

  @Test
  void testUserIsOlderThan18Years() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    LocalDate today = LocalDate.now();
    LocalDate birthday = utilisateur.getBirthday().toLocalDate();
    int ageInYears = Period.between(birthday, today).getYears();
    assertTrue(ageInYears >= 18, "User must be at least 18 years old");
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
  void testIBelieveICanBuyShouldReturnTrue() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();

    assertTrue(utilisateur.iBelieveICanBuy(action, CORRECT_QUANTITY));
  }

  @Test
  void testIBelieveICanBuyShouldReturnFalseWhenCashIsInsufficient() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action expensiveAction = new Action("Expensive Action", 500.0);

    assertFalse(utilisateur.iBelieveICanBuy(expensiveAction, 1));
  }

  @Test
  void testIBelieveICanBuyActionNullShouldThrow() {
    Utilisateur utilisateur = getCorrectUtilisateur();

    assertThrows(IllegalArgumentException.class, () -> utilisateur.iBelieveICanBuy(null, CORRECT_QUANTITY));
  }

  @Test
  void testIBelieveICanBuyQuantityNullShouldThrow() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();

    assertThrows(IllegalArgumentException.class, () -> utilisateur.iBelieveICanBuy(action, null));
  }

  @Test
  void testBuyActionShouldNotChangeWalletOrCashWhenCashIsInsufficient() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action expensiveAction = new Action("Too expensive", 500.0);
    Double initialCashAmount = utilisateur.getCashAmount();

    utilisateur.buyAction(expensiveAction, 1);

    assertEquals(initialCashAmount, utilisateur.getCashAmount(), 0.001);
    assertFalse(utilisateur.getWallet().getActions().containsKey(expensiveAction));
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

  @Test
  void testGetHistoriqueMouvementWithAddCashAmoutShouldWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Double amountToAdd = 100.00;
    utilisateur.addCashAmount(amountToAdd);
    List<String> li = utilisateur.getHistoriqueMouvementSold();
    String ch = li.get(0);
    assertEquals(ch, "Ajout d'argent:" + amountToAdd + " Current sold :" + utilisateur.getCashAmount());
  }

  @Test
  void testGetHistoriqueMouvementWithBuyActionShouldWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, CORRECT_QUANTITY);
    List<String> li = utilisateur.getHistoriqueMouvementSold();
    String ch = li.get(0);
    assertEquals(ch, "Action acheté :" + action.toString() + " Current sold :" + utilisateur.getCashAmount());
  }

  @Test
  void testGetHistoriqueMouvementWithSellActionShouldWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    utilisateur.buyAction(action, CORRECT_QUANTITY);
    utilisateur.sellAction(action, CORRECT_QUANTITY);
    List<String> li = utilisateur.getHistoriqueMouvementSold();
    String ch = li.get(1);
    assertEquals(ch, "Action sold :" + action.toString() + " Current sold :" + utilisateur.getCashAmount());
  }

}
