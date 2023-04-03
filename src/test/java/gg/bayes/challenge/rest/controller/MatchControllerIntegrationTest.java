package gg.bayes.challenge.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroSpells;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Integration test template to get you started. Add tests and make modifications as you see fit.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MatchControllerIntegrationTest {

    private static final String COMBATLOG_FILE_1 = "/data/combatlog_1.log.txt";
    private static final String COMBATLOG_FILE_2 = "/data/combatlog_2.log.txt";

    private static final String HERO_ITEMS_RESPONSE_FILE_1 = "/mock/hero_items_combat_log_1.txt";
    private static final String HERO_SPELLS_RESPONSE_FILE_1 = "/mock/hero_spells_combat_log_1.txt";
    private static final String HERO_DAMAGE_RESPONSE_FILE_1 = "/mock/hero_damage_combat_log_1.txt";

    @Autowired
    private MockMvc mvc;

    private Map<String, Long> matchIds;

    private ObjectMapper mapper = new ObjectMapper();

    private List<HeroItem> heroItemsResponseCombatLog1;
    private List<HeroSpells> heroSpellsResponseCombatLog1;
    private List<HeroDamage> heroDamageResponseCombatLog1;

    @BeforeAll
    void setup() throws Exception {
        // Populate the database with all events from both sample data files and store the returned
        // match IDS.
        matchIds = Map.of(
                COMBATLOG_FILE_1, ingestMatch(COMBATLOG_FILE_1),
                COMBATLOG_FILE_2, ingestMatch(COMBATLOG_FILE_2));

        // Load mock responses for combatlog_1
        heroItemsResponseCombatLog1 = loadHeroItemsResponse(HERO_ITEMS_RESPONSE_FILE_1);
        heroSpellsResponseCombatLog1 = loadHeroSpellsResponse(HERO_SPELLS_RESPONSE_FILE_1);
        heroDamageResponseCombatLog1 = loadHeroDamageResponse(HERO_DAMAGE_RESPONSE_FILE_1);
    }

    /**
     * Test for Fetching the heroes and their kill counts for the given match. </Br>
     * Tests the response for GET api/match/$match_id
     */
    @SneakyThrows
    @Test
    void getMatchTest() {
        mvc.perform(get("/api/match/" + matchIds.get(COMBATLOG_FILE_1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$").isArray());
        assertThat(mvc).isNotNull();
    }

    /**
     * Test for the given match, fetches the items bought by the named hero. </Br>
     * Tests the response for GET /api/match/$match_id/$hero_name/items.
     */
    @SneakyThrows
    @Test
    void getHeroItemsTest() {
        List<HeroItem> expectedResponse = heroItemsResponseCombatLog1;

        String responseBody = mvc.perform(get("/api/match/"+ matchIds.get(COMBATLOG_FILE_1) + "/abyssal_underlord/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(30)))
                .andExpect(jsonPath("$").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<HeroItem> response = mapper.readValue(responseBody, new TypeReference<List<HeroItem>>(){});
        assertEquals(null, expectedResponse, response);
        assertThat(mvc).isNotNull();
    }

    /**
     * Test For the given match, fetches the spells cast by the named hero. </Br>
     * Tests the response for GET /api/match/$match_id/$hero_name/spells </Br>
     */
    @SneakyThrows
    @Test
    void getHeroSpells() {
        List<HeroSpells> expectedResponse = heroSpellsResponseCombatLog1;

        String responseBody = mvc.perform(get("/api/match/"+ matchIds.get(COMBATLOG_FILE_1) + "/abyssal_underlord/spells"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<HeroSpells> response = mapper.readValue(responseBody, new TypeReference<List<HeroSpells>>(){});
        assertEquals(null, expectedResponse, response);
        assertThat(mvc).isNotNull();
    }
    /**
     * Test for a given match, fetches damage done data for the named hero. </Br>
     * Tests the response for GET /api/match/$match_id/$hero_name/damage </Br>
     */
    @SneakyThrows
    @Test
    void getHeroDamages() {
        List<HeroDamage> expectedResponse = heroDamageResponseCombatLog1;

        String responseBody = mvc.perform(get("/api/match/"+ matchIds.get(COMBATLOG_FILE_1) + "/abyssal_underlord/damage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<HeroDamage> response = mapper.readValue(responseBody, new TypeReference<List<HeroDamage>>(){});
        assertEquals(null, expectedResponse, response);
        assertThat(mvc).isNotNull();
    }

    /**
     * Helper method that ingests a combat log file and returns the match id associated with all parsed events.
     *
     * @param file file path as a classpath resource, e.g.: /data/combatlog_1.log.txt.
     * @return the id of the match associated with the events parsed from the given file
     * @throws Exception if an error happens when reading or ingesting the file
     */
    private Long ingestMatch(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);

        return Long.parseLong(mvc.perform(post("/api/match")
                                         .contentType(MediaType.TEXT_PLAIN)
                                         .content(fileContent))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString());
    }

    /**
     * Helper method that loads a mock response for GET /api/match/$match_id/$hero_name/items
     *
     * @param file file path as a classpath resource, e.g.: /mock/hero_items_combat_log_1.log.txt.
     * @return a collection of items bought by the hero during the match
     * @throws Exception if an error happens when reading the mock response file
     */
    private List<HeroItem> loadHeroItemsResponse(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);
        List<HeroItem> heroItemsResponse = mapper.readValue(fileContent, new TypeReference<List<HeroItem>>(){});
        return heroItemsResponse;
    }

    /**
     * Helper method that loads a mock response for GET /api/match/$match_id/$hero_name/spells
     *
     * @param file file path as a classpath resource, e.g.: /mock/hero_spells_combat_log_1.log.txt.
     * @return a collection of spells cast by the hero and how many times they were cast
     * @throws Exception if an error happens when reading the mock response file
     */
    private List<HeroSpells> loadHeroSpellsResponse(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);
        List<HeroSpells> heroSpellsResponse = mapper.readValue(fileContent, new TypeReference<List<HeroSpells>>(){});
        return heroSpellsResponse;
    }

    /**
     * Helper method that loads a mock response for GET /api/match/$match_id/$hero_name/damage </Br>
     *
     * @param file file path as a classpath resource, e.g.: /mock/hero_damage_combat_log_1.log.txt.
     * @return a collection of "damage done" (target, number of times and total damage) elements
     * @throws Exception if an error happens when reading the mock response file
     */
    private List<HeroDamage> loadHeroDamageResponse(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);
        List<HeroDamage> heroDamageResponse = mapper.readValue(fileContent, new TypeReference<List<HeroDamage>>(){});
        return heroDamageResponse;
    }
}
