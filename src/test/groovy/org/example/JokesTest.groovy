package org.example

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.models.api.ApiErrorRS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = [TestApplication])
class JokesTest extends Specification {
    @Autowired
    private MockMvc mockMvc
    @Autowired
    private ObjectMapper objectMapper

    @Unroll
    def "/jokes: validate response with incorrect request count = #count"() {
        when:
        def responseContent = mockMvc.perform(get("/jokes?count=" + count)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().response
        def response = objectMapper.readValue(responseContent.contentAsString, ApiErrorRS.class)
        then:
        response.error == errorMessage
        where:
        count | errorMessage
        0     | "Incorrect value"
        1     | "Incorrect value"
        101   | "За один раз можно получить не более 100 шуток."
        -1    | "Incorrect value"
    }

    @Unroll
    def "/jokes: success request"() {
        expect:
        def responseContent = mockMvc.perform(get("/jokes?count=" + count)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().response
        where:
        count = 5
    }

}
