package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UtilisateurTest {
  private final String NAME = "Doe";
  private final String FIRST_NAME = "John";
  private final Date BIRTHDAY = Date.valueOf("2000-01-01");
  private final Wallet WALLET = new Wallet();

  public Utilisateur getCorrectUtilisateur(){
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
    assertEquals(WALLET, result);
  }

  @Test
  void testSetWallet() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Wallet newWallet = new Wallet();
    utilisateur.setWallet(newWallet);
    assertEquals(newWallet, utilisateur.getWallet());
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

}
