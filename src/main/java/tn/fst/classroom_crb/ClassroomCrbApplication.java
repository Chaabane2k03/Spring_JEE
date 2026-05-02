package tn.fst.classroom_crb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

// Indique l'application Spring Boot principale
 @SpringBootApplication
// Active les tâches planifiées
 @EnableScheduling
// Active la gestion de retry Spring
 @EnableRetry
public class ClassroomCrbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomCrbApplication.class, args);
	}

}
