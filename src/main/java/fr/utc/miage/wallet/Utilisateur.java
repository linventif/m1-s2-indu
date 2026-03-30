package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur disposant d'un portefeuille d'actions et d'un
 * montant de liquidités.
 * <p>
 * Un utilisateur peut alimenter son solde, acheter des actions si ses
 * liquidités le permettent et vendre des actions déjà présentes dans son
 * portefeuille.
 */
public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;

  /** Montant initial de liquidités attribué à chaque utilisateur. */
  public static final Double INITIAL_CASH_AMOUNT = 200.00;

  private Double cashAmount;
  private final List<String> historiMouvementSold = new ArrayList<>();

  /**
   * Crée un nouvel utilisateur avec un portefeuille vide et un solde initial.
   *
   * @param firstName le prénom de l'utilisateur
   * @param name      le nom de l'utilisateur
   * @param birthday  la date de naissance de l'utilisateur
   */
  public Utilisateur(final String firstName, final String name, final Date birthday) {
    this.firstName = firstName;
    this.name = name;
    this.birthday = birthday;
    this.wallet = new Wallet();
    this.cashAmount = INITIAL_CASH_AMOUNT;
  }

  /**
   * Retourne le nom de famille de l'utilisateur.
   *
   * @return le nom de famille
   */
  public String getName() {
    return this.name;
  }

  /**
   * Retourne le prénom de l'utilisateur.
   *
   * @return le prénom
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Retourne la date de naissance de l'utilisateur.
   *
   * @return la date de naissance
   */
  public Date getBirthday() {
    return birthday;
  }

  /**
   * Retourne le portefeuille courant de l'utilisateur.
   *
   * @return le portefeuille courant
   */
  public Wallet getWallet() {
    return wallet;
  }

  /**
   * Retourne le montant de liquidités disponible.
   *
   * @return le montant disponible
   */
  public Double getCashAmount() {
    return cashAmount;
  }

  /**
   * Ajoute de l'argent au solde de l'utilisateur.
   *
   * @param amount le montant à ajouter
   * @throws IllegalArgumentException si le montant n'est pas strictement positif
   */
  public void addCashAmount(final Double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }

    this.cashAmount += amount;
    historiMouvementSold.add("Ajout d'argent:" + amount + " Current sold :" + this.cashAmount);
  }

  /**
   * Remplace le portefeuille courant de l'utilisateur.
   *
   * @param wallet le nouveau portefeuille
   */
  public void setWallet(final Wallet wallet) {
    this.wallet = wallet;
  }

  /**
   * Achète une quantité d'une action si l'utilisateur dispose de suffisamment
   * de liquidités.
   *
   * @param action   l'action à acheter
   * @param quantity la quantité à acheter
   * @throws IllegalArgumentException si l'action ou la quantité est nulle
   */
  public void buyAction(final Action action, final Integer quantity) {
    if (this.iBelieveICanBuy(action, quantity)) {
      this.cashAmount -= action.getPrice() * quantity;
      this.getWallet().addAction(action, quantity);
      historiMouvementSold.add("Action acheté :" + action + this.cashAmount);
    }
  }

  /**
   * Vérifie si l'utilisateur peut acheter une quantité donnée d'une action.
   *
   * @param action   l'action à évaluer
   * @param quantity la quantité souhaitée
   * @return {@code true} si le montant disponible couvre l'achat,
   *         {@code false} sinon
   * @throws IllegalArgumentException si l'action ou la quantité est nulle
   */
  public boolean iBelieveICanBuy(final Action action, final Integer quantity) {
    if (action == null || quantity == null) {
      throw new IllegalArgumentException("action ou quantity can not be null");
    }
    return action.getPrice() * quantity <= this.cashAmount;
  }

  /**
   * Vend une quantité d'une action présente dans le portefeuille de
   * l'utilisateur.
   *
   * @param action   l'action à vendre
   * @param quantity la quantité à vendre
   * @throws IllegalArgumentException si l'action ou la quantité est nulle, si
   *                                  la quantité n'est pas strictement positive
   *                                  ou si le portefeuille ne
   *                                  contient pas assez d'actions
   */
  public void sellAction(final Action action, final Integer quantity) {
    if (action == null || quantity == null) {
      throw new IllegalArgumentException("action ou quantity can not be null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be positive");
    }

    this.getWallet().removeAction(action, quantity);
    this.cashAmount += action.getPrice() * quantity;
  }

  public List<String> getHistoriqueMouvementSold() {
    return this.historiMouvementSold;
  }
}
