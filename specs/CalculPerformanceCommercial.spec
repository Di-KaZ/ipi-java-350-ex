# Calcul Performance Comercial

## Cas 1 : -20% en dessous => reset perf

* Embaucher "Test" "Employe" en tant que "COMMERCIAL" avec un niveau d'étude à "BAC"
* La performance actuelle de l'employé est de "200"
* L'employe à realisé "1000" de chiffre et avait un objectif fixé à "1520"
* La performance de l'employé apres recalcul doit etre egal à "1"

## Cas 2 : entre -5% et -20% => -2 perf

* Embaucher "Le Sang" "Nono" en tant que "COMMERCIAL" avec un niveau d'étude à "BAC"
* La performance actuelle de l'employé est de "6"
* L'employe à realisé "1000" de chiffre et avait un objectif fixé à "1220"
* La performance de l'employé apres recalcul doit etre egal à "4"


## Cas 3 : entre -5% et +5% => aucun changement

// Negatif
* Embaucher "Test" "Employe" en tant que "COMMERCIAL" avec un niveau d'étude à "BAC"
* La performance actuelle de l'employé est de "6"
* L'employe à realisé "1000" de chiffre et avait un objectif fixé à "1045"
* La performance de l'employé apres recalcul doit etre egal à "6"

// Positif
* Embaucher "Test" "Employe" en tant que "COMMERCIAL" avec un niveau d'étude à "BAC"
* La performance actuelle de l'employé est de "6"
* L'employe à realisé "1045" de chiffre et avait un objectif fixé à "1000"
* La performance de l'employé apres recalcul doit etre egal à "6"

## Cas 4 : entre +5% et +20% => +1 perf

* Embaucher "Test" "Employe" en tant que "COMMERCIAL" avec un niveau d'étude à "BAC"
* La performance actuelle de l'employé est de "6"
* L'employe à realisé "1145" de chiffre et avait un objectif fixé à "1000"
* La performance de l'employé apres recalcul doit etre egal à "8"

## Cas 5 : +20% => +4 perf Quel monstre

* Embaucher "Test" "Employe" en tant que "COMMERCIAL" avec un niveau d'étude à "BAC"
* La performance actuelle de l'employé est de "6"
* L'employe à realisé "1545" de chiffre et avait un objectif fixé à "1000"
* La performance de l'employé apres recalcul doit etre egal à "11"