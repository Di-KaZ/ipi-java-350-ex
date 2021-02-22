package com.ipiecoles.java.java350.Gauge;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.BeforeSpec;
import com.thoughtworks.gauge.Step;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaugeEmbaucheEmployeTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(GaugeEmbaucheEmployeTests.class);
	@Autowired
	EmployeService employeService;

	@Autowired
	EmployeRepository employeRepository;

	@BeforeScenario
	public void setup() {
		employeRepository.deleteAll();
	}

	@Step("Embaucher <nom> <prenom> en tant que <poste> avec un niveau d'étude a <nivEtude>")
	public void testEmbaucheEmploye(String nom, String prenom, Poste poste, NiveauEtude nivEtude) {
		LOGGER.warn("Britney bitch");
		try {
			employeService.embaucheEmploye(nom, prenom, poste, nivEtude, 1.0);
		} catch (Exception e) {
			LOGGER.error("{}", e.getMessage());
		}
		Employe employe = employeRepository.findByMatricule(poste.name().charAt(0) + "00001");
		Assertions.assertThat(employe.getNom()).isEqualTo(nom);
		Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
	}
}
