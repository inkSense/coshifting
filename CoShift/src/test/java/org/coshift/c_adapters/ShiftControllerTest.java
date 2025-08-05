package org.coshift.c_adapters;

import org.coshift.a_domain.Shift;
import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.UseCaseInteractor;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.c_adapters.security.AuthenticationProviderSpring;
import org.coshift.c_adapters.security.SpringPasswordChecker;
import org.coshift.c_adapters.web.ShiftController;
import org.coshift.d_frameworks.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShiftController.class)
@Import({SpringConfig.class,
         SpringPasswordChecker.class,       // echter Adapter, kein Mock
         AuthenticationProviderSpring.class})
class ShiftControllerTest {

    @Autowired MockMvc mvc;

    @MockBean
    UseCaseInteractor interactor;

    @MockBean PersonRepository  personRepo;  

    @MockBean org.coshift.b_application.ports.TimeAccountRepository timeAccountRepo;

    @Test
    void authenticatedRequestReturnsShifts() throws Exception {
        List<Shift> shifts = List.of(new Shift(1L, LocalDateTime.now(), 120, 10));
        when(interactor.getAllShifts()).thenReturn(shifts);

        when(interactor.authenticateUser("anton", "secret"))
            .thenReturn(new Person(99L, "anton", "secret", PersonRole.USER));
 
        mvc.perform(get("/api/shifts").with(httpBasic("anton", "secret")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        mvc.perform(get("/api/shifts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userCannotAddOtherUserToShift() throws Exception {
        when(interactor.authenticateUser("anton", "secret"))
                .thenReturn(new Person(1L, "anton", "secret", PersonRole.USER));

        mvc.perform(put("/api/shifts/5/participation")
                        .with(httpBasic("anton", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("2"))
                .andExpect(status().isForbidden());
    }

    @Test
    void userCannotRemoveOtherUserFromShift() throws Exception {
        when(interactor.authenticateUser("anton", "secret"))
                .thenReturn(new Person(1L, "anton", "secret", PersonRole.USER));

        mvc.perform(delete("/api/shifts/5/participation")
                        .with(httpBasic("anton", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("2"))
                .andExpect(status().isForbidden());
    }
}
