package tn.fst.classroom_crb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Charge le contexte Spring pour les tests
 @SpringBootTest
// Active un profil Spring spécifique pour le test/configuration
 @ActiveProfiles("test")
class ClassroomCrbApplicationTests {

	// Indique une méthode de test (JUnit)
	 @Test
	void contextLoads() {
	}

}
