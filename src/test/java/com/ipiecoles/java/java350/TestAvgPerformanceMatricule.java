package com.ipiecoles.java.java350;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.repository.EmployeRepository;

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
class TestAvgPerformanceMatricule {
	@Autowired
	private EmployeRepository employeRepository;

	private String nom = "Sébastien";
	private String prenom = "Patrick";

	@BeforeEach
	@AfterEach
	public void setup() {
		employeRepository.deleteAll();
	}

	@ParameterizedTest
	@CsvSource({ "C00001, C", "T00001, T", "M00001, M" })
	void testAvgOneEmployee(String matricule, String posteLetter) throws EntityExistsException, EmployeException {
		Integer performance = 200;
		Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, performance,
				1.0);
		employeRepository.save(employe);

		Assertions.assertThat(employeRepository.avgPerformanceWhereMatriculeStartsWith(posteLetter))
				.isEqualTo((double) performance);
	}

	@ParameterizedTest
	@CsvSource({ "C00001, C", "T00001, T", "M00001, M" })
	void testAvgMultipleEmployeSamePoste(String matricule, String posteLetter)
			throws EntityExistsException, EmployeException {
		List<Integer> performances = new ArrayList<>();
		performances.add(45);
		performances.add(3);
		performances.add(23);
		performances.add(12);
		performances.add(52);
		performances.add(44);
		performances.add(12);
		performances.add(33);

		for (Integer perf : performances) {
			Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, perf, 1.0);
			employeRepository.save(employe);
		}

		/*
		 * Double perfAvg = performances.stream().mapToDouble(perf ->
		 * perf).average().orElse(-1.0);
		 */
		Double perfAvg = 28.0D;
		Assertions.assertThat(employeRepository.avgPerformanceWhereMatriculeStartsWith(posteLetter)).isEqualTo(perfAvg);
	}

	@Test
	void testAvgMultipleEmploye() throws EntityExistsException, EmployeException {
		Map<String, Integer> performances = new HashMap<>();
		performances.put("C00001", 45);
		performances.put("T00002", 3);
		performances.put("M00003", 23);
		performances.put("C00004", 12);
		performances.put("C00005", 52);
		performances.put("M00006", 44);
		performances.put("M00007", 12);
		performances.put("T00008", 33);

		performances.forEach((matricule, perf) -> {
			Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, perf, 1.0);
			employeRepository.save(employe);
		});

		Double perfAvgCommercial = 36.333333333333336D; // un arrondi serais peut etre une bonne idée 😂
		Assertions.assertThat(employeRepository.avgPerformanceWhereMatriculeStartsWith("C"))
				.isEqualTo(perfAvgCommercial);
	}
}
