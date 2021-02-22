package com.ipiecoles.java.java350;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
class TestEmbaucheEmploye {
	private String nom = "Toe";
	private String prenom = "Jhon";

	@InjectMocks
	public EmployeService employeService;

	@Mock
	public EmployeRepository employeRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@ParameterizedTest
	@CsvSource({ "TECHNICIEN, CAP, 1.0, T00001, 1521.22", "MANAGER, BAC, 1.0, M00001, 1673.34",
			"TECHNICIEN, BTS_IUT, 1.0, T00001, 1825.46", "COMMERCIAL, LICENCE, 0.5, C00001, 912.73",
			"TECHNICIEN, MASTER, 1.0, T00001, 2129.71", "MANAGER, INGENIEUR, 1.0, M00001, 2433.95",
			"TECHNICIEN, DOCTORAT, 0.5, T00001, 1293.04", "COMMERCIAL, CAP, 1.0, C00001, 1521.22",
			"MANAGER, BAC, 1.0, M00001, 1673.34", "MANAGER, MASTER, 1.0, M00001, 2129.71", })
	void testEmbaucheEmploye(Poste poste, NiveauEtude nivEtu, Double tmpsPart, String matriculeFinal,
			Double salaireFinal) {
		Employe employe = null;
		try {
			employe = employeService.embaucheEmploye(nom, prenom, poste, nivEtu, tmpsPart);
		} catch (Exception e) {

		}
		ArgumentCaptor<Employe> eCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository, Mockito.times(1)).save(eCaptor.capture());
		Assertions.assertThat(employe).isEqualTo(eCaptor.getValue());
		Assertions.assertThat(eCaptor.getValue().getMatricule()).isEqualTo(matriculeFinal);
		Assertions.assertThat(eCaptor.getValue().getSalaire()).isEqualTo(salaireFinal);
		Assertions.assertThat(eCaptor.getValue().getPrenom()).isEqualTo(prenom);
		Assertions.assertThat(eCaptor.getValue().getNom()).isEqualTo(nom);
		Assertions.assertThat(eCaptor.getValue().getTempsPartiel()).isEqualTo(tmpsPart);
		Assertions.assertThat(eCaptor.getValue().getTempsPartiel()).isEqualTo(tmpsPart);
		Assertions.assertThat(eCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
	}

	@Test
	public void testLimitEmbauche() {
		when(employeRepository.findLastMatricule()).thenReturn("99999");
		Assertions
				.assertThatThrownBy(
						() -> employeService.embaucheEmploye(nom, prenom, Poste.MANAGER, NiveauEtude.DOCTORAT, 1.0))
				.hasMessage("Limite des 100000 matricules atteinte !");

		when(employeRepository.findLastMatricule()).thenReturn("100000");

		Assertions
				.assertThatThrownBy(
						() -> employeService.embaucheEmploye(nom, prenom, Poste.MANAGER, NiveauEtude.DOCTORAT, 1.0))
				.hasMessage("Limite des 100000 matricules atteinte !");
	}

	@Test
	public void testMatriculeExist() {
		String matricule = "M00001";

		when(employeRepository.findByMatricule(matricule)).thenReturn(new Employe());
		Assertions
				.assertThatThrownBy(
						() -> employeService.embaucheEmploye(nom, prenom, Poste.MANAGER, NiveauEtude.DOCTORAT, 1.0))
				.hasMessage("L'employé de matricule " + matricule + " existe déjà en BDD");
	}
}
