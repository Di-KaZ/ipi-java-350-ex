package com.ipiecoles.java.java350.Gauge;

import javax.persistence.EntityExistsException;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaugeServices {
	private static final Logger LOGGER = LoggerFactory.getLogger(GaugeServices.class);
	@Autowired
	EmployeService employeService;

	@Autowired
	EmployeRepository employeRepository;

	@BeforeScenario
	public void setup() {
		employeRepository.deleteAll();
		employee = null;
	}

	private Employe employee = null;

	@Step("Embaucher <nom> <prenom> en tant que <poste> avec un niveau d'étude a <nivEtude>")
	public void testEmbaucheEmploye(String nom, String prenom, Poste poste, NiveauEtude nivEtude) {
		try {
			// utilisépour le testde la perf commercial il est reset a chaquenario
			employee = employeService.embaucheEmploye(nom, prenom, poste, nivEtude, 1.0);
		} catch (Exception e) {
			LOGGER.error("{}", e.getMessage());
		}
		Assertions.assertThat(employee.getNom()).isEqualTo(nom);
		Assertions.assertThat(employee.getPrenom()).isEqualTo(prenom);
		Assertions.assertThat(employee.getMatricule().charAt(0)).isEqualTo(poste.name().charAt(0));
		LOGGER.warn("commercial {}", employee);
	}

	@Step("La performance actuelle de l'employe est de <perf>")
	void testSetEmployePef(Integer perf) {
		LOGGER.warn("commercial {}", employee);
		employee.setPerformance(perf);
	}

	@Step("L'employe a réalisé <caTraite> de chiffre et avait un objectif fixé à <objectifCa>")
	void testCalcPerfCommercial(Long caTraite, Long objectifCa) throws EntityExistsException, EmployeException {
		LOGGER.warn("commercial {}", employee);
		employeService.calculPerformanceCommercial(employee.getMatricule(), caTraite, objectifCa);
	}

	@Step("La performance de l'employe apres recalcul doit etre egal à <perfAttendu>")
	void testPerfAttendu(Integer perfAttendu) {
		LOGGER.warn("commercial {}", employee);
		Assertions.assertThat(employee.getPerformance()).isEqualTo(perfAttendu);
	}
}
