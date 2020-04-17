#language: fr

Fonctionnalité: Effectuer des livraison
  Contexte: un employé s'occupe d'effecuter les livraisons

  Scénario: Il y a plusieurs livraisons de plusieurs fournisseurs
    Quand Lentreprise doit livrer 2 colis de 2 fournisseurs de noms AG et PK
    Et l'employé effectue les 2 livraison de AG et une livraison de PK
    Alors AG devra devra payer 2 livraisons pour cette journée
    Et Pk devra en payer 1