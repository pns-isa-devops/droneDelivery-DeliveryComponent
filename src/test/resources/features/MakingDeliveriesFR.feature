#language: fr

  Fonctionnalité: Effectuer des livraison
    Contexte: un employé s'occupe d'effecuter les livraisons


      Scénario: Il n'y a pas de livraisons à faire
        Quand l'employé demande la prochaine livraison
        Alors Il y a 0 livraisons

      Scénario: Il y a qu'une seule livraison à faire
        Quand L'entreprise reçoit une livraison
        Et l'employé regarde la prochaine livraison
        Alors Il devrait y avoir 1 livraison
        Et après il n'y a plus de livraisons

      Scénario: Il y a plusieurs livraisons d'un seul fournisseur
         Quand Lentreprise doit livrer 2 colis dun seul fournisseur de nom AG1
         Et l'employé demande la prochaine livraison à envoyer
         Et après il devrait rester 1 livraison
         Et le fournisseur a 1 livraison à payer

      Scénario: Il y a plusieurs livraisons de plusieurs fournisseurs
        Quand Lentreprise doit livrer 2 colis de 2 fournisseurs de noms AG et PK
        Et l'employé effectue les 2 livraison de AG et une livraison de PK
        Alors AG devra devra payer 2 livraisons pour cette journée
        Et Pk devra en payer 1