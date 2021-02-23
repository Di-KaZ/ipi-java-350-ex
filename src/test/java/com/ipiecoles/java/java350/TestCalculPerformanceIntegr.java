package com.ipiecoles.java.java350;

import java.time.LocalDate;

import javax.persistence.EntityExistsException;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestCalculPerformanceIntegr {
	@Autowired
	public EmployeService employeService;

	@Autowired
	public EmployeRepository employeRepository;

	@BeforeEach
	@AfterEach
	public void setup() {
		employeRepository.deleteAll();
	}

	@Test
	void testCalcPerfCommercialNulls() throws EntityExistsException, EmployeException {
		Employe employe = employeService.embaucheEmploye("Zeubi", "La Mouche", Poste.COMMERCIAL, NiveauEtude.MASTER,
				1.0);
		Assertions
				.assertThatThrownBy(
						() -> employeService.calculPerformanceCommercial(employe.getMatricule(), null, 100000L))
				.hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

		Assertions
				.assertThatThrownBy(
						() -> employeService.calculPerformanceCommercial(employe.getMatricule(), 10000L, null))
				.hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

		Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(null, 10000L, 10000L))
				.hasMessage("Le matricule ne peut être null et doit commencer par un C !");
	}

	@ParameterizedTest
	@CsvSource({
			// ! Cas 1 : -20% en dessous => reset perf
			"1000, 1520, 200, 1",
			// ! Cas 2 : entre -5% et -20% => -2 perf
			"1000, 1220, 6, 4",
			// ! Cas 3 : entre -5% et +5% => aucun changement
			"1000, 1045, 6, 6", "1045, 1000, 6, 6",
			// ! Cas 4 : entre +5% et +20% => +1 perf
			"1145, 1000, 6, 8", // + 1 car nouvel perf > avg perf
			// ! Cas 5 : +20% => +4 perf (Quel monstre)
			"1545, 1000, 6, 11", }) // +1 car nouvelle perf > avg perf
	void testCalcPerfCommercial(Long caTraite, Long objectifCa, Integer perfBase, Integer perfAttendu)
			throws EntityExistsException, EmployeException {

		String matricule = "C00001";
		Employe employe = new Employe("Zeubi", "La Mouche", matricule, LocalDate.now(), Entreprise.SALAIRE_BASE,
				perfBase, 1.0);
		employeRepository.save(employe);

		employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

		employe = employeRepository.findByMatricule(matricule); // on recup l'employe modif avec la nouvelle perf
		Assertions.assertThat(employe.getPerformance()).isEqualTo(perfAttendu);
	}

}
