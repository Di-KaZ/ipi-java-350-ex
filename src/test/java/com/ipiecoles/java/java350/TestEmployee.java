package com.ipiecoles.java.java350;

import java.time.LocalDate;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TestEmployee {

	@Test
	public void testNbAneeAncieneteNoyears() {
		Employe employee = new Employe();

		employee.setDateEmbauche(LocalDate.now());
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isZero();
	}

	@Test
	public void testNbAneeAncieneteNullyears() {
		Employe employee = new Employe();

		employee.setDateEmbauche(null);
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isZero();
	}

	@Test
	public void testNbAneeAncieneteMinus2years() {
		Employe employee = new Employe();

		employee.setDateEmbauche(LocalDate.now().minusYears(2L));
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isEqualTo(2);
	}

	@Test
	public void testNbAneeAncienetePlus2years() {
		Employe employee = new Employe();

		employee.setDateEmbauche(LocalDate.now().plusYears(2L));
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isZero();
	}

	@ParameterizedTest
	@CsvSource({ "1, 'T12345', 0, 1.0, 1000.0", "1, 'T12345', 2, 0.5, 600.0", "1, 'T12345', 2, 1.0, 1200.0",
			"2, 'T12345', 0, 1.0, 2300.0", "2, 'T12345', 1, 1.0, 2400.0", "1, 'M12345', 0, 1.0, 1700.0",
			"1, 'M12345', 5, 1.0, 2200.0", "2, 'M12345', 0, 1.0, 1700.0", "2, 'M12345', 8, 1.0, 2500.0", })
	void testPrimeAnnuel(Integer perf, String matricule, Long ancienete, Double tempPartiel, Double primeAnnuel) {
		Employe employe = new Employe("GET", "MOUSSED", matricule, LocalDate.now().minusYears(ancienete),
				Entreprise.SALAIRE_BASE, perf, tempPartiel);
		Assertions.assertThat(employe.getPrimeAnnuelle()).isEqualTo(primeAnnuel);
	}

	@ParameterizedTest
	@CsvSource({ "-1.0", "-1.1", "-1.7" })
	void testAugmenterSalaireNeg(double pourcentage) {
		Employe employe = new Employe("GET", "MOUSSED", "M00001", LocalDate.now().minusYears(5),
				Entreprise.SALAIRE_BASE, 1, 1.0);
		Assertions.assertThat(employe.getSalaire()).isEqualTo(Entreprise.SALAIRE_BASE);

		Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourcentage))
				.hasMessage("Impossible d'augementer le salaire avec un pourcentage negatif");
	}
	@ParameterizedTest
	@CsvSource({ "0.1", "0.5", "0.99" })
	void testAugementerSalaireZeros(double pourcentage) {
		Employe employe = new Employe("GET", "MOUSSED", "M00001", LocalDate.now().minusYears(5),
				Entreprise.SALAIRE_BASE, 1, 1.0);
		Assertions.assertThat(employe.getSalaire()).isEqualTo(Entreprise.SALAIRE_BASE);

		Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourcentage))
				.hasMessage("Impossible d'augementer le salaire avec un pourcentage inferieur a 1");
	}

	@ParameterizedTest
	@CsvSource({ "1.1, 1673.34", "1.5,2281.83", "1.99, 3027.23" })
	void testAugementerSalaires(double pourcentage, Double salaireAttendu) throws EmployeException {
		Employe employe = new Employe("GET", "MOUSSED", "M00001", LocalDate.now().minusYears(5),
				Entreprise.SALAIRE_BASE, 1, 1.0);
		Assertions.assertThat(employe.getSalaire()).isEqualTo(Entreprise.SALAIRE_BASE);
		employe.augmenterSalaire(pourcentage);
		Assertions.assertThat(employe.getSalaire()).isEqualTo(salaireAttendu);
	}

	@ParameterizedTest
	@CsvSource({ "'2019-01-01', 8", "'2021-01-01', 10", "'2022-01-01', 10", "'2032-01-01', 12" })
	void testNbRTT(LocalDate date, Integer nbDeRTTAttendu) {
		Employe employe = new Employe("Hello", "Wolrd", "M00567", LocalDate.now(), 1200.0, 1, 1.0);

		Integer nbRtt = employe.getNbRtt(date);
		Assertions.assertThat(nbRtt).isEqualTo(nbDeRTTAttendu);
	}
}
