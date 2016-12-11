package net.juancarlosfernandez.jhipster.repository.search;

import net.juancarlosfernandez.jhipster.domain.BloodPressure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BloodPressure entity.
 */
public interface BloodPressureSearchRepository extends ElasticsearchRepository<BloodPressure, Long> {
}
