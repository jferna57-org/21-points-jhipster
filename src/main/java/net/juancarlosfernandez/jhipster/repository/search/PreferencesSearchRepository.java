package net.juancarlosfernandez.jhipster.repository.search;

import net.juancarlosfernandez.jhipster.domain.Preferences;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Preferences entity.
 */
public interface PreferencesSearchRepository extends ElasticsearchRepository<Preferences, Long> {
}
