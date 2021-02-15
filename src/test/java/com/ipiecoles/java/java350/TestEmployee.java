package com.ipiecoles.java.java350;

import java.time.LocalDate;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestEmployee {

	@Test
	public void testNbAneeAncieneteNoyears() {
		Employe employee = new Employe();

		employee.setDateEmbauche(LocalDate.now());
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isEqualTo(0);
	}

	@Test
	public void testNbAneeAncieneteNullyears() {
		Employe employee = new Employe();

		employee.setDateEmbauche(null);
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isEqualTo(0);
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
		Assertions.assertThat(employee.getNombreAnneeAnciennete()).isEqualTo(0);
	}

	@ParameterizedTest
	@CsvSource({ "1, 'T12345', 0, 1.0, 1000.0", "1, 'T12345', 2, 0.5, 600.0", "1, 'T12345', 2, 1.0, 1200.0",
			"2, 'T12345', 0, 1.0, 2300.0", "2, 'T12345', 1, 1.0, 2400.0", "1, 'M12345', 0, 1.0, 1700.0",
			"1, 'M12345', 5, 1.0, 2200.0", "2, 'M12345', 0, 1.0, 1700.0", "2, 'M12345', 8, 1.0, 2500.0", })
	public void testPrimeAnnuel(Integer perf, String matricule, Long ancienete, Double tempPartiel,
			Double primeAnnuel) {
		Employe employe = new Employe("GET", "MOUSSED", matricule, LocalDate.now().minusYears(ancienete),
				Entreprise.SALAIRE_BASE, perf, tempPartiel);
		Assertions.assertThat(employe.getPrimeAnnuelle()).isEqualTo(primeAnnuel);
	}
}
