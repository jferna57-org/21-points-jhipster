package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.Application;

import net.juancarlosfernandez.jhipster.domain.BloodPressure;
import net.juancarlosfernandez.jhipster.repository.BloodPressureRepository;
import net.juancarlosfernandez.jhipster.repository.search.BloodPressureSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static net.juancarlosfernandez.jhipster.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BloodPressureResource REST controller.
 *
 * @see BloodPressureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BloodPressureResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;

    @Inject
    private BloodPressureRepository bloodPressureRepository;

    @Inject
    private BloodPressureSearchRepository bloodPressureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBloodPressureMockMvc;

    private BloodPressure bloodPressure;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodPressureResource bloodPressureResource = new BloodPressureResource();
        ReflectionTestUtils.setField(bloodPressureResource, "bloodPressureSearchRepository", bloodPressureSearchRepository);
        ReflectionTestUtils.setField(bloodPressureResource, "bloodPressureRepository", bloodPressureRepository);
        this.restBloodPressureMockMvc = MockMvcBuilders.standaloneSetup(bloodPressureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BloodPressure createEntity(EntityManager em) {
        BloodPressure bloodPressure = new BloodPressure()
                .dateTime(DEFAULT_DATE_TIME)
                .systolic(DEFAULT_SYSTOLIC)
                .diastolic(DEFAULT_DIASTOLIC);
        return bloodPressure;
    }

    @Before
    public void initTest() {
        bloodPressureSearchRepository.deleteAll();
        bloodPressure = createEntity(em);
    }

    @Test
    @Transactional
    public void createBloodPressure() throws Exception {
        int databaseSizeBeforeCreate = bloodPressureRepository.findAll().size();

        // Create the BloodPressure

        restBloodPressureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
            .andExpect(status().isCreated());

        // Validate the BloodPressure in the database
        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeCreate + 1);
        BloodPressure testBloodPressure = bloodPressures.get(bloodPressures.size() - 1);
        assertThat(testBloodPressure.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testBloodPressure.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testBloodPressure.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);

        // Validate the BloodPressure in ElasticSearch
        BloodPressure bloodPressureEs = bloodPressureSearchRepository.findOne(testBloodPressure.getId());
        assertThat(bloodPressureEs).isEqualToComparingFieldByField(testBloodPressure);
    }

    @Test
    @Transactional
    public void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        bloodPressure.setDateTime(null);

        // Create the BloodPressure, which fails.

        restBloodPressureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
            .andExpect(status().isBadRequest());

        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        bloodPressure.setSystolic(null);

        // Create the BloodPressure, which fails.

        restBloodPressureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
            .andExpect(status().isBadRequest());

        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiastolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        bloodPressure.setDiastolic(null);

        // Create the BloodPressure, which fails.

        restBloodPressureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodPressure)))
            .andExpect(status().isBadRequest());

        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBloodPressures() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);

        // Get all the bloodPressures
        restBloodPressureMockMvc.perform(get("/api/blood-pressures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodPressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @Test
    @Transactional
    public void getBloodPressure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);

        // Get the bloodPressure
        restBloodPressureMockMvc.perform(get("/api/blood-pressures/{id}", bloodPressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bloodPressure.getId().intValue()))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC));
    }

    @Test
    @Transactional
    public void getNonExistingBloodPressure() throws Exception {
        // Get the bloodPressure
        restBloodPressureMockMvc.perform(get("/api/blood-pressures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBloodPressure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);
        bloodPressureSearchRepository.save(bloodPressure);
        int databaseSizeBeforeUpdate = bloodPressureRepository.findAll().size();

        // Update the bloodPressure
        BloodPressure updatedBloodPressure = bloodPressureRepository.findOne(bloodPressure.getId());
        updatedBloodPressure
                .dateTime(UPDATED_DATE_TIME)
                .systolic(UPDATED_SYSTOLIC)
                .diastolic(UPDATED_DIASTOLIC);

        restBloodPressureMockMvc.perform(put("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBloodPressure)))
            .andExpect(status().isOk());

        // Validate the BloodPressure in the database
        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeUpdate);
        BloodPressure testBloodPressure = bloodPressures.get(bloodPressures.size() - 1);
        assertThat(testBloodPressure.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testBloodPressure.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testBloodPressure.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);

        // Validate the BloodPressure in ElasticSearch
        BloodPressure bloodPressureEs = bloodPressureSearchRepository.findOne(testBloodPressure.getId());
        assertThat(bloodPressureEs).isEqualToComparingFieldByField(testBloodPressure);
    }

    @Test
    @Transactional
    public void deleteBloodPressure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);
        bloodPressureSearchRepository.save(bloodPressure);
        int databaseSizeBeforeDelete = bloodPressureRepository.findAll().size();

        // Get the bloodPressure
        restBloodPressureMockMvc.perform(delete("/api/blood-pressures/{id}", bloodPressure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bloodPressureExistsInEs = bloodPressureSearchRepository.exists(bloodPressure.getId());
        assertThat(bloodPressureExistsInEs).isFalse();

        // Validate the database is empty
        List<BloodPressure> bloodPressures = bloodPressureRepository.findAll();
        assertThat(bloodPressures).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBloodPressure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(bloodPressure);
        bloodPressureSearchRepository.save(bloodPressure);

        // Search the bloodPressure
        restBloodPressureMockMvc.perform(get("/api/_search/blood-pressures?query=id:" + bloodPressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodPressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }
}
