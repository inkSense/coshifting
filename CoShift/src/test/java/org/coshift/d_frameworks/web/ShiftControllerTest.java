package org.coshift.d_frameworks.web;              // an deine Paketstruktur anpassen

import org.coshift.d_frameworks.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ShiftController.class)
@Import(SecurityConfig.class)   // falls SecurityConfig in anderem Package liegt
class ShiftControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void shouldReturnOneDemoShift() throws Exception {
        mvc.perform(get("/api/shifts")
                        .with(httpBasic("mitglied", "secret")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
