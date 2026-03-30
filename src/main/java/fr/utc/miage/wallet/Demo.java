package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Fournit une démonstration exécutable du fonctionnement principal du projet.
 * <p>
 * Cette classe illustre la création d'actions simples et composées, la
 * gestion de l'historique des prix, l'utilisation directe d'un portefeuille et
 * les opérations d'achat et de vente réalisées par un utilisateur.
 */
@SuppressWarnings({ "java:S106", "java:S1192" })
public final class Demo {
  /** Empêche l'instanciation de cette classe utilitaire. */
  private Demo() {}

  /**
   * Point d'entrée de la démonstration.
   *
   * @param args arguments de ligne de commande non utilisés
   */
  public static void main(final String[] args) {
    System.out.println();
    System.out.println("========================================");
    System.out.println("DEMONSTRATION COMPLETE WALLET");
    System.out.println("========================================");

    Action actionOvh = new Action("OVH Demo", 10.0, ActionCategory.INDUSTRIAL);
    Action actionGoogle = new Action("Google Demo", 20.0);
    Action actionInteger = new Action("Integer Demo", 12);

    Map<String, Float> composition = new HashMap<>();
    composition.put(actionOvh.getLabel(), 0.4f);
    composition.put(actionGoogle.getLabel(), 0.6f);
    Action actionTechFund = new Action("Tech Fund Demo", 17.0, composition);

    actionGoogle.setCategory(ActionCategory.TRANSPORT);
    actionGoogle.setPrice(22.0);

    Map<String, Float> updatedComposition = new HashMap<>(composition);
    updatedComposition.put(actionInteger.getLabel(), 0.1f);
    actionTechFund.setComposition(updatedComposition);

    System.out.println("1. CONSTRUCTEURS ET PROPRIETES");
    System.out.println("Action simple avec categorie : " + actionOvh);
    System.out.println("Categorie OVH : " + actionOvh.getCategory());
    System.out.println("Type OVH : " + actionOvh.getType());
    System.out.println("Action simple categorie par defaut puis modifiee : " + actionGoogle);
    System.out.println("Categorie Google apres modification : " + actionGoogle.getCategory());
    System.out.println("Prix Google apres modification : " + actionGoogle.getPrice());
    System.out.println("Action creee avec prix entier : " + actionInteger);
    System.out.println("Composition action simple Integer Demo : " + actionInteger.getComposition());
    System.out.println("Action composee : " + actionTechFund.toStringC());
    System.out.println("Composition actuelle du fond : " + actionTechFund.getComposition());
    System.out.println("Action retrouvee par libelle : " + Action.getActionByLabel("OVH Demo"));
    System.out.println("OVH egale a l'action du registre : " + actionOvh.equals(Action.getActionByLabel("OVH Demo")));
    System.out.println("Hash OVH : " + actionOvh.hashCode());

    actionOvh.addHistoricalPrice(Date.valueOf("2026-01-01"), 50.0);
    actionOvh.addHistoricalPrice(Date.valueOf("2026-01-02"), 20.0);
    actionOvh.addHistoricalPrice(Date.valueOf("2026-01-03"), 70.0);
    actionGoogle.addHistoricalPrice(Date.valueOf("2026-01-01"), 60.0);
    actionGoogle.addHistoricalPrice(Date.valueOf("2026-01-02"), 80.0);
    actionTechFund.addHistoricalPrice(Date.valueOf("2026-01-01"), 80.0);

    actionOvh.importHistoricalPrices("src/main/resources/ovh_historical_prices.csv");

    System.out.println();
    System.out.println("2. HISTORIQUE DES PRIX");
    System.out.println("Historique OVH sous forme de map : " + actionOvh.getHistoricalPrices());
    System.out.println("Prix OVH au 2026-01-02 : " + actionOvh.getHistoricalPrice(Date.valueOf("2026-01-02")));
    System.out.println("Prix OVH au 2026-01-04 via getPriceAtDate : "
        + actionOvh.getPriceAtDate(Date.valueOf("2026-01-04")));
    System.out.println("Historique OVH sous forme textuelle : ");
    System.out.println(actionOvh.getHistoricalPricesString());
    System.out.println("Analyse OVH :");
    actionOvh.getActionAnalyse();

    Wallet wallet = new Wallet();
    wallet.addAction(actionOvh, 2);
    wallet.addAction(actionGoogle, 1);
    wallet.addAction(actionInteger, 0);

    System.out.println();
    System.out.println("3. UTILISATION DIRECTE DU PORTEFEUILLE");
    System.out.println("Contenu brut du portefeuille : " + wallet.getActions());
    System.out.println("toString portefeuille : " + wallet);
    System.out.println("Valeur totale portefeuille : " + wallet.getTotalValue());
    wallet.removeAction(actionGoogle, 1);
    System.out.println("Portefeuille apres retrait de Google : " + wallet.getActions());
    System.out.println("Valeur totale apres retrait : " + wallet.getTotalValue());

    Utilisateur utilisateur = new Utilisateur("John", "Doe", Date.valueOf("2000-01-01"));
    utilisateur.addCashAmount(50.0);
    utilisateur.setWallet(wallet);

    System.out.println();
    System.out.println("4. UTILISATEUR ET OPERATIONS");
    System.out.println("Utilisateur : " + utilisateur.getFirstName() + " " + utilisateur.getName());
    System.out.println("Date de naissance : " + utilisateur.getBirthday());
    System.out.println("Cash initial apres ajout : " + utilisateur.getCashAmount());
    System.out.println("Portefeuille assigne : " + utilisateur.getWallet());
    System.out.println("Peut acheter 1 Tech Fund Demo ? " + utilisateur.iBelieveICanBuy(actionTechFund, 1));
    utilisateur.buyAction(actionTechFund, 1);
    utilisateur.sellAction(actionOvh, 1);
    System.out.println("Cash apres achat puis vente : " + utilisateur.getCashAmount());
    System.out.println("Portefeuille utilisateur : " + utilisateur.getWallet().getActions());
    System.out.println("Historique des mouvements utilisateur : " + utilisateur.getHistoriqueMouvementSold());

    System.out.println();
    System.out.println("5. FILTRES ET NETTOYAGE");
    System.out.println("Actions industrielles connues : " + Action.getActionsByCategory(ActionCategory.INDUSTRIAL));
    actionInteger.delete();
    System.out.println("Action Integer Demo apres suppression du registre : "
        + Action.getActionByLabel("Integer Demo"));
  }
}
