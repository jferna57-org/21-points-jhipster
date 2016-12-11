package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.Weight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Weight entity.
 */
@SuppressWarnings("unused")
public interface WeightRepository extends JpaRepository<Weight,Long> {

    @Query("select weight from Weight weight where weight.user.login = ?#{principal.username} order by weight.dateTime desc")
    Page<Weight> findByUserIsCurrentUser(Pageable pageable);

    Page<Weight> findAllByOrderByDateTimeDesc(Pageable pageable);

    List<Weight> findAllByDateTimeBetweenAndUserLoginOrderByDateTimeDesc(ZonedDateTime firstDate, ZonedDateTime lastDate, String login);

}
