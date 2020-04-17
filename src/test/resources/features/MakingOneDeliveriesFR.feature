#language: fr

Fonctionnalité: Effectuer des livraison
  Contexte: un employé s'occupe d'effecuter les livraisons

    Scénario: Il y a plusieurs livraisons d'un seul fournisseur
    Quand Lentreprise doit livrer 2 colis dun seul fournisseur de nom AG1
    Et l'employé demande la prochaine livraison à envoyer
    Et après il devrait rester 1 livraison
    Et le fournisseur a 1 livraison à payer