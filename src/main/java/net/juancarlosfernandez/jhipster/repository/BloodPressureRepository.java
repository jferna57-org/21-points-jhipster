package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.BloodPressure;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the BloodPressure entity.
 */
@SuppressWarnings("unused")
public interface BloodPressureRepository extends JpaRepository<BloodPressure,Long> {

    @Query("select bloodPressure from BloodPressure bloodPressure where bloodPressure.user.login = ?#{principal.username}")
    List<BloodPressure> findByUserIsCurrentUser();

    List<BloodPressure> findAllByDateTimeBetweenAndUserLoginOrderByDateTimeDesc(ZonedDateTime firstDate, ZonedDateTime secondDate, String login);
}
