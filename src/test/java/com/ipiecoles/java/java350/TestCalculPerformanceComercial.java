package com.ipiecoles.java.java350;

import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class TestCalculPerformanceComercial {
	@InjectMocks
	public EmployeService employeService;

	@Mock
	public EmployeRepository employeRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCalcPerfCommercialNulls() throws EntityExistsException, EmployeException {
		employeService.embaucheEmploye("Zeubi", "La Mouche", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);

		ArgumentCaptor<Employe> eCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository, Mockito.times(1)).save(eCaptor.capture());

		Assertions.assertThatThrownBy(
				() -> employeService.calculPerformanceCommercial(eCaptor.getValue().getMatricule(), null, 100000L))
				.hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

		Assertions.assertThatThrownBy(
				() -> employeService.calculPerformanceCommercial(eCaptor.getValue().getMatricule(), 10000L, null))
				.hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

		Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(null, 10000L, 10000L))
				.hasMessage("Le matricule ne peut être null et doit commencer par un C !");

	}

	@Test
	void testCalcPerfCommercialNeg() throws EntityExistsException, EmployeException {
		employeService.embaucheEmploye("Zeubi", "La Mouche", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);

		ArgumentCaptor<Employe> eCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository, Mockito.times(1)).save(eCaptor.capture());

		Assertions.assertThatThrownBy(
				() -> employeService.calculPerformanceCommercial(eCaptor.getValue().getMatricule(), -1000L, 100000L))
				.hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");

		Assertions.assertThatThrownBy(
				() -> employeService.calculPerformanceCommercial(eCaptor.getValue().getMatricule(), 10000L, -1000L))
				.hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");

		when(employeRepository.findByMatricule("C00001")).thenReturn(null);
		Assertions.assertThatThrownBy(
				() -> employeService.calculPerformanceCommercial(eCaptor.getValue().getMatricule(), 10000L, 1000L))
				.hasMessage("Le matricule " + "C00001" + " n'existe pas !");

	}

	@Test
	void testEmployeNotComercial() throws EntityExistsException, EmployeException {
		employeService.embaucheEmploye("Zeubi", "La Mouche", Poste.TECHNICIEN, NiveauEtude.MASTER, 1.0);

		ArgumentCaptor<Employe> eCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository, Mockito.times(1)).save(eCaptor.capture());

		Assertions.assertThatThrownBy(
				() -> employeService.calculPerformanceCommercial(eCaptor.getValue().getMatricule(), 100000L, 100000L))
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
			"1145, 1000, 6, 7",
			// ! Cas 4 : +20% => +4 perf (Quel monstre)
			"1545, 1000, 6, 10", })
	void testCalcPerfCommercial(Long caTraite, Long objectifCa, Integer perfBase, Integer perfAttendu)
			throws EntityExistsException, EmployeException {

		String matricule = "C00001";
		when(employeRepository.findByMatricule(matricule)).thenReturn(
				new Employe("Zeubi", "La Mouche", matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, perfBase, 1.0));

		// set average to null permit to have no variance in those test
		when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);

		employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

		ArgumentCaptor<Employe> eCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository, Mockito.times(1)).save(eCaptor.capture());
		Assertions.assertThat(eCaptor.getValue().getPerformance()).isEqualTo(perfAttendu);
	}
}
