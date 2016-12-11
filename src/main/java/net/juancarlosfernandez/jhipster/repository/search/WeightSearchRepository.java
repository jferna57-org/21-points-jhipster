package net.juancarlosfernandez.jhipster.repository.search;

import net.juancarlosfernandez.jhipster.domain.Weight;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Weight entity.
 */
public interface WeightSearchRepository extends ElasticsearchRepository<Weight, Long> {
}
