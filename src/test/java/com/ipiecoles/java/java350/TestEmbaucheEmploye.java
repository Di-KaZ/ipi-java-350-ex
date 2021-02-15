package com.ipiecoles.java.java350;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestEmbaucheEmploye {
	@InjectMocks
	public EmployeService employeService;

	@Mock
	public EmployeRepository employeRepository;

	@Test
	@ParameterizedTest
	@CsvSource({ "TECHNICIEN, CAP, 1.0, T0001, 0.0", "MANAGER, BAC, 1.0, M0001, 0.0",
			"TECHNICIEN, BTS_IUT, 1.0, T0001, 0.0", "COMMERCIAL, LICENCE, 0.5, C0001, 0.0",
			"TECHNICIEN, MASTER, 1.0, T0001, 0.0", "MANAGER, INGENIEUR, 1.0, M0001, 0.0",
			"TECHNICIEN, DOCTORAT, 0.5, T0001, 0.0", "COMMERCIAL, CAP, 1.0, C0001, 0.0",
			"MANAGER, BAC, 1.0, M0001, 0.0", "MANAGER, MASTER, 1.0, M0001, 0.0", })
	public void testEmbaucheEmploye(Poste poste, NiveauEtude nivEtu, Double tmpsPart, String matriculeFinal,
			Double salaireFinal) {

		try {
			employeService.embaucheEmploye("Jhon", "Toe", poste, nivEtu, tmpsPart);
		} catch (Exception e) {

		}
		ArgumentCaptor<Employe> eCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository, Mockito.times(1)).save(eCaptor.capture());
		Assertions.assertThat(eCaptor.getValue().getMatricule()).isEqualTo(matriculeFinal);
		Assertions.assertThat(eCaptor.getValue().getSalaire()).isEqualTo(salaireFinal);
	}
}
