package net.juancarlosfernandez.jhipster.repository.search;

import net.juancarlosfernandez.jhipster.domain.Points;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Points entity.
 */
public interface PointsSearchRepository extends ElasticsearchRepository<Points, Long> {
}
