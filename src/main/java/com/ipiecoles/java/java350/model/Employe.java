package com.ipiecoles.java.java350.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ipiecoles.java.java350.exception.EmployeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Employe {

	private static final Logger LOGGER = LoggerFactory.getLogger(Employe.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nom;

	private String prenom;

	private String matricule;

	private LocalDate dateEmbauche;

	private Double salaire = Entreprise.SALAIRE_BASE;

	private Integer performance = Entreprise.PERFORMANCE_BASE;

	private Double tempsPartiel = 1.0;

	public Employe() {
	}

	public Employe(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire,
			Integer performance, Double tempsPartiel) {
		this.nom = nom;
		this.prenom = prenom;
		this.matricule = matricule;
		this.dateEmbauche = dateEmbauche;
		this.salaire = Double.parseDouble(String.format(Locale.ROOT, "%.2f", salaire));
		this.performance = performance;
		this.tempsPartiel = tempsPartiel;
	}

	/**
	 * Méthode calculant le nombre d'années d'ancienneté à partir de la date
	 * d'embauche
	 *
	 * @return
	 */
	public Integer getNombreAnneeAnciennete() {
		Integer ancienete = dateEmbauche != null ? LocalDate.now().getYear() - dateEmbauche.getYear() : 0;
		return ancienete < 0 ? 0 : ancienete;
	}

	public Integer getNbConges() {
		return Entreprise.NB_CONGES_BASE + this.getNombreAnneeAnciennete();
	}

	public Integer getNbRtt() {
		return getNbRtt(LocalDate.now());
	}

	public Integer getNbRtt(LocalDate d) {
		int nbDaysInCurrentYear = d.isLeapYear() ? 366 : 365;
		int nbOfweekenddays = 104;

		switch (LocalDate.of(d.getYear(), 1, 1).getDayOfWeek()) {
			case FRIDAY:
				if (d.isLeapYear()) {
					nbOfweekenddays += 2;
				} else {
					nbOfweekenddays += 1;
				}
				break;
			case SATURDAY:
				nbOfweekenddays += 1;
				break;
			default:
				break;
		}

		long nbJourFerieNotWeekend = Entreprise.joursFeries(d).stream()
				.filter(localDate -> localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue()).count();
		Double test = (nbDaysInCurrentYear - Entreprise.NB_JOURS_MAX_FORFAIT - nbOfweekenddays - nbJourFerieNotWeekend
				- Entreprise.NB_CONGES_BASE) * tempsPartiel;
		LOGGER.debug("{}, {} - {} - {} - {} - {} = {}", d, nbDaysInCurrentYear, Entreprise.NB_JOURS_MAX_FORFAIT,
				nbOfweekenddays, nbJourFerieNotWeekend, Entreprise.NB_CONGES_BASE, test);
		return test.intValue();
	}

	/**
	 * Calcul de la prime annuelle selon la règle : Pour les managers : Prime
	 * annuelle de base bonnifiée par l'indice prime manager Pour les autres
	 * employés, la prime de base plus éventuellement la prime de performance
	 * calculée si l'employé n'a pas la performance de base, en multipliant la prime
	 * de base par un l'indice de performance (égal à la performance à laquelle on
	 * ajoute l'indice de prime de base)
	 *
	 * Pour tous les employés, une prime supplémentaire d'ancienneté est ajoutée en
	 * multipliant le nombre d'année d'ancienneté avec la prime d'ancienneté. La
	 * prime est calculée au pro rata du temps de travail de l'employé
	 *
	 * @return la prime annuelle de l'employé en Euros et cents
	 */
	// Matricule, performance, date d'embauche, temps partiel, prime
	public Double getPrimeAnnuelle() {
		// Calcule de la prime d'ancienneté
		Double primeAnciennete = Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete();
		Double prime;
		// Prime du manager (matricule commençant par M) : Prime annuelle de base
		// multipliée par l'indice prime manager
		// plus la prime d'anciennté.
		if (matricule != null && matricule.startsWith("M")) {
			prime = Entreprise.primeAnnuelleBase() * Entreprise.INDICE_PRIME_MANAGER + primeAnciennete;
		}
		// Pour les autres employés en performance de base, uniquement la prime annuelle
		// plus la prime d'ancienneté.
		else if (this.performance == null || Entreprise.PERFORMANCE_BASE.equals(this.performance)) {
			prime = Entreprise.primeAnnuelleBase() + primeAnciennete;
		}
		// Pour les employés plus performance, on bonnifie la prime de base en
		// multipliant par la performance de l'employé
		// et l'indice de prime de base.
		else {
			prime = Entreprise.primeAnnuelleBase() * (this.performance + Entreprise.INDICE_PRIME_BASE)
					+ primeAnciennete;
		}
		// Au pro rata du temps partiel.
		return prime * this.tempsPartiel;
	}

	// Augmenter salaire
	public void augmenterSalaire(double pourcentage) throws EmployeException {
		if (pourcentage < 0.0) {
			throw new EmployeException("Impossible d'augementer le salaire avec un pourcentage negatif");
		} else if (pourcentage < 1.0) {
			throw new EmployeException("Impossible d'augementer le salaire avec un pourcentage inferieur a 1");
		}
		salaire *= pourcentage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public Employe setNom(String nom) {
		this.nom = nom;
		return this;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the matricule
	 */
	public String getMatricule() {
		return matricule;
	}

	/**
	 * @param matricule the matricule to set
	 */
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	/**
	 * @return the dateEmbauche
	 */
	public LocalDate getDateEmbauche() {
		return dateEmbauche;
	}

	/**
	 * @param dateEmbauche the dateEmbauche to set
	 */
	public void setDateEmbauche(LocalDate dateEmbauche) {
		this.dateEmbauche = dateEmbauche;
	}

	/**
	 * @return the salaire
	 */
	public Double getSalaire() {
		return Double.parseDouble(String.format(Locale.ROOT, "%.2f", salaire));
	}

	/**
	 * @param salaire the salaire to set
	 */
	public void setSalaire(Double salaire) {
		this.salaire = Double.parseDouble(String.format(Locale.ROOT, "%.2f", salaire));
	}

	public Integer getPerformance() {
		return performance;
	}

	public void setPerformance(Integer performance) {
		this.performance = performance;
	}

	public Double getTempsPartiel() {
		return tempsPartiel;
	}

	public void setTempsPartiel(Double tempsPartiel) {
		this.tempsPartiel = tempsPartiel;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", nom='" + getNom() + "'" + ", prenom='" + getPrenom() + "'"
				+ ", matricule='" + getMatricule() + "'" + ", dateEmbauche='" + getDateEmbauche() + "'" + ", salaire='"
				+ getSalaire() + "'" + ", performance='" + getPerformance() + "'" + ", tempsPartiel='"
				+ getTempsPartiel() + "'" + "}";
	}

}
