package net.juancarlosfernandez.jhipster.web.rest;

import net.juancarlosfernandez.jhipster.Application;

import net.juancarlosfernandez.jhipster.domain.User;
import net.juancarlosfernandez.jhipster.domain.Weight;
import net.juancarlosfernandez.jhipster.repository.UserRepository;
import net.juancarlosfernandez.jhipster.repository.WeightRepository;
import net.juancarlosfernandez.jhipster.repository.search.WeightSearchRepository;

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
import org.springframework.web.context.WebApplicationContext;

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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Test class for the WeightResource REST controller.
 *
 * @see WeightResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WeightResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Inject
    private WeightRepository weightRepository;

    @Inject
    private WeightSearchRepository weightSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    @Inject
    private UserRepository userRepository;

    @Inject
    private WebApplicationContext context;

    private MockMvc restWeightMockMvc;

    private Weight weight;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeightResource weightResource = new WeightResource();
        ReflectionTestUtils.setField(weightResource, "weightSearchRepository", weightSearchRepository);
        ReflectionTestUtils.setField(weightResource, "weightRepository", weightRepository);
        this.restWeightMockMvc = MockMvcBuilders.standaloneSetup(weightResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createEntity(EntityManager em) {
        Weight weight = new Weight()
                .dateTime(DEFAULT_DATE_TIME)
                .weight(DEFAULT_WEIGHT);
        return weight;
    }

    @Before
    public void initTest() {
        weightSearchRepository.deleteAll();
        weight = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeight() throws Exception {
        int databaseSizeBeforeCreate = weightRepository.findAll().size();

        // Create the Weight

        restWeightMockMvc.perform(post("/api/weights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isCreated());

        // Validate the Weight in the database
        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeCreate + 1);
        Weight testWeight = weights.get(weights.size() - 1);
        assertThat(testWeight.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testWeight.getWeight()).isEqualTo(DEFAULT_WEIGHT);

        // Validate the Weight in ElasticSearch
        Weight weightEs = weightSearchRepository.findOne(testWeight.getId());
        assertThat(weightEs).isEqualToComparingFieldByField(testWeight);
    }

    @Test
    @Transactional
    public void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = weightRepository.findAll().size();
        // set the field null
        weight.setDateTime(null);

        // Create the Weight, which fails.

        restWeightMockMvc.perform(post("/api/weights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isBadRequest());

        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = weightRepository.findAll().size();
        // set the field null
        weight.setWeight(null);

        // Create the Weight, which fails.

        restWeightMockMvc.perform(post("/api/weights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weight)))
            .andExpect(status().isBadRequest());

        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeights() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get all the weights
        restWeightMockMvc.perform(get("/api/weights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    public void getWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get the weight
        restWeightMockMvc.perform(get("/api/weights/{id}", weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weight.getId().intValue()))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingWeight() throws Exception {
        // Get the weight
        restWeightMockMvc.perform(get("/api/weights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);
        weightSearchRepository.save(weight);
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Update the weight
        Weight updatedWeight = weightRepository.findOne(weight.getId());
        updatedWeight
                .dateTime(UPDATED_DATE_TIME)
                .weight(UPDATED_WEIGHT);

        restWeightMockMvc.perform(put("/api/weights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWeight)))
            .andExpect(status().isOk());

        // Validate the Weight in the database
        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeUpdate);
        Weight testWeight = weights.get(weights.size() - 1);
        assertThat(testWeight.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testWeight.getWeight()).isEqualTo(UPDATED_WEIGHT);

        // Validate the Weight in ElasticSearch
        Weight weightEs = weightSearchRepository.findOne(testWeight.getId());
        assertThat(weightEs).isEqualToComparingFieldByField(testWeight);
    }

    @Test
    @Transactional
    public void deleteWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);
        weightSearchRepository.save(weight);
        int databaseSizeBeforeDelete = weightRepository.findAll().size();

        // Get the weight
        restWeightMockMvc.perform(delete("/api/weights/{id}", weight.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean weightExistsInEs = weightSearchRepository.exists(weight.getId());
        assertThat(weightExistsInEs).isFalse();

        // Validate the database is empty
        List<Weight> weights = weightRepository.findAll();
        assertThat(weights).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);
        weightSearchRepository.save(weight);

        // Search the weight
        restWeightMockMvc.perform(get("/api/_search/weights?query=id:" + weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    private void createWeightByMonth(ZonedDateTime firstDate, ZonedDateTime firstDayOfLastMonth){
        User user = userRepository.findOneByLogin("user").get();

        weight = new Weight(firstDate,60,user);
        weightRepository.saveAndFlush(weight);
        weight = new Weight(firstDate.plusDays(10),70,user);
        weightRepository.saveAndFlush(weight);
        weight = new Weight(firstDate.plusDays(20),65,user);
        weightRepository.saveAndFlush(weight);

    }

    @Test
    @Transactional
    public void getWeightForLast30Days() throws Exception{
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twentyNineDaysAgo = now.minusDays(29);
        ZonedDateTime firstDayOfLastMonth = now.withDayOfMonth(1).minusMonths(1);
        createWeightByMonth(twentyNineDaysAgo,firstDayOfLastMonth);

        // create security-aware mockMvc
        restWeightMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Get all weights readings
        restWeightMockMvc.perform(get("/api/weights")
            .with(user("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$",hasSize(3)));

        restWeightMockMvc.perform(get("/api/weights-by-days/{days}", 30)
            .with(user("user").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.period").value("Last 30 Days"))
            .andExpect(jsonPath("$.weighIns[*].weight").value(hasItem(60)));

    }
}
