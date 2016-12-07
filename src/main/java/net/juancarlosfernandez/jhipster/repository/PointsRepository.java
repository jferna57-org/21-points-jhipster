package net.juancarlosfernandez.jhipster.repository;

import net.juancarlosfernandez.jhipster.domain.Points;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Points entity.
 */
@SuppressWarnings("unused")
public interface PointsRepository extends JpaRepository<Points,Long> {

    @Query("select points from Points points where points.user.login = ?#{principal.username}")
    Page<Points> findByUserIsCurrentUser(Pageable pageable);

    Page<Points> findAllByOrderByDateDesc(Pageable pageable);

    List<Points> findAllByDateBetweenAndUserLogin(LocalDate firstDate, LocalDate lastDate, String login);


}
