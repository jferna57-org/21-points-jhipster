package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.BloodPressure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the BloodPressure entity.
 */
@SuppressWarnings("unused")
public interface BloodPressureRepository extends JpaRepository<BloodPressure,Long> {

    @Query("select bloodPressure from BloodPressure bloodPressure where bloodPressure.user.login = ?#{principal.username} order by bloodPressure.dateTime desc")
    Page<BloodPressure> findByUserIsCurrentUser(Pageable pageable);

    Page<BloodPressure> findAllByOrderByDateTimeDesc(Pageable pageable);

    List<BloodPressure> findAllByDateTimeBetweenAndUserLoginOrderByDateTimeDesc(ZonedDateTime firstDate, ZonedDateTime secondDate, String login);
}
