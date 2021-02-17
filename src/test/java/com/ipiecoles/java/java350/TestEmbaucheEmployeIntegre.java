package com.ipiecoles.java.java350;

import java.time.LocalDate;

import javax.persistence.EntityExistsException;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestEmbaucheEmployeIntegre {
	@Autowired
	private EmployeRepository employeRepository;

	@Autowired
	private EmployeService employeService;

	private String nom = "Sébastien";
	private String prenom = "Patrick";
	private Double tmpsPart = 1.0;

	@BeforeEach
	@AfterEach
	public void setup() {
		employeRepository.deleteAll();
	}

	@Test
	void testEmbuahceEmployeIntegre() throws EntityExistsException, EmployeException {
		Employe employe = employeService.embaucheEmploye(nom, prenom, Poste.COMMERCIAL, NiveauEtude.LICENCE, tmpsPart);

		Assertions.assertThat(employe.getMatricule()).isEqualTo("C00001");
		Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
		Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
		Assertions.assertThat(employe.getNom()).isEqualTo(nom);
		Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tmpsPart);
		Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tmpsPart);
		Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
	}
}
