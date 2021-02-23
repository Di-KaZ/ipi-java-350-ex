package com.ipiecoles.java.java350.Gauge;

import javax.persistence.EntityExistsException;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import com.thoughtworks.gauge.AfterScenario;
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
	@AfterScenario
	public void setup() {
		employeRepository.deleteAll();
		employe = null;
	}

	public Employe employe = null;

	@Step("Embaucher <nom> <prenom> en tant que <poste> avec un niveau d'étude à <nivEtude>")
	public void testEmbaucheEmploye(String nom, String prenom, Poste poste, NiveauEtude nivEtude) {
		try {
			// utilisepour le testde la perf commercial il est reset à chaquenario
			employe = employeService.embaucheEmploye(nom, prenom, poste, nivEtude, 1.0);
		} catch (Exception e) {
			LOGGER.error("{}", e.getMessage());
		}
		Assertions.assertThat(employe.getNom()).isEqualTo(nom);
		Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
		Assertions.assertThat(employe.getMatricule().charAt(0)).isEqualTo(poste.name().charAt(0));
	}

	@Step("La performance actuelle de l'employé est de <perf>")
	public void testSetEmployePef(Integer perf) {
		LOGGER.info("perfomance de l'employé mise à {}", perf);
		employe.setPerformance(perf);
		employeRepository.save(employe);
	}

	@Step("L'employe à realisé <caTraite> de chiffre et avait un objectif fixé à <objectifCa>")
	public void testCalcPerfCommercial(Long caTraite, Long objectifCa) throws EntityExistsException, EmployeException {
		employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, objectifCa);
	}

	@Step("La performance de l'employé apres recalcul doit etre egal à <perfAttendu>")
	public void testPerfAttendu(Integer perfAttendu) {
		LOGGER.info("emplye avant {}", employe);
		employe = employeRepository.findByMatricule(employe.getMatricule());
		LOGGER.info("emplye apres {}", employe);
		Assertions.assertThat(employe.getPerformance()).isEqualTo(perfAttendu);
	}
}
