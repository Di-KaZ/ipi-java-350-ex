package com.ipiecoles.java.java350;

import java.time.LocalDate;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;

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
class TestFindLastMatricule {
	@Autowired
	private EmployeRepository employeRepository;

	private String nom = "Sébastien";
	private String prenom = "Patrick";
	private Double tmpsPart = 1.0;

	@BeforeEach
	@AfterEach
	public void setup() {
		employeRepository.deleteAll();
	}

	@Test
	void testwithOneEmployee() {
		employeRepository.save(new Employe(nom, prenom, "T00001", LocalDate.now(), 1200.0, 1, 1.0));
		Employe employe = employeRepository.findByMatricule("T00001");
		Assertions.assertThat(employe).isNotNull();
		Assertions.assertThat(employe.getNom()).isEqualTo(nom);
		Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
		Assertions.assertThat(employe.getSalaire()).isEqualTo(1200.0);
		Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tmpsPart);
		Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
		Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
	}

	@Test
	void testWithMutipleEmployee() {
		employeRepository.save(new Employe(nom, prenom, "T00001", LocalDate.now(), 1200.0, 1, 1.0));
		employeRepository.save(new Employe(nom, prenom, "C00002", LocalDate.now(), 1200.0, 1, 1.0));
		employeRepository.save(new Employe(nom, prenom, "M00003", LocalDate.now(), 1200.0, 1, 1.0));
		Employe employe = employeRepository.findByMatricule("M00003");
		Assertions.assertThat(employe).isNotNull();
		Assertions.assertThat(employe.getNom()).isEqualTo(nom);
		Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
		Assertions.assertThat(employe.getSalaire()).isEqualTo(1200.0);
		Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tmpsPart);
		Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
		Assertions.assertThat(employe.getMatricule()).isEqualTo("M00003");
	}

	@Test
	void testWithNoEmployee() {
		Employe employe = employeRepository.findByMatricule("T00001");
		Assertions.assertThat(employe).isNull();
	}
}