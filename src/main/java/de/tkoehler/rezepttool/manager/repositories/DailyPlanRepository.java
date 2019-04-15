package de.tkoehler.rezepttool.manager.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.DailyPlan;

public interface DailyPlanRepository extends CrudRepository<DailyPlan, String> {

	@Query("select p from DailyPlan p where p.date >= :from and p.date <= :to")
	List<DailyPlan> findAllByTimespan(Date from, Date to);

	Optional<DailyPlan> findByDate(Date date);
}
