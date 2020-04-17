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