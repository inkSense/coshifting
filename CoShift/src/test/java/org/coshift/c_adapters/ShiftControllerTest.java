package org.coshift.c_adapters;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.UseCaseInteractor;
import org.coshift.c_adapters.web.ShiftController;
import org.coshift.d_frameworks.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShiftController.class)
@Import(SecurityConfig.class)
class ShiftControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UseCaseInteractor interactor;

    @Test
    void authenticatedRequestReturnsShifts() throws Exception {
        List<Shift> shifts = List.of(new Shift(1L, LocalDateTime.now(), 120, 10));
        when(interactor.getAllShifts()).thenReturn(shifts);

        mvc.perform(get("/api/shifts").with(httpBasic("mitglied", "secret")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        mvc.perform(get("/api/shifts"))
                .andExpect(status().isUnauthorized());
    }
}
