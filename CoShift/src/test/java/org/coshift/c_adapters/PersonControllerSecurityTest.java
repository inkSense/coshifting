package org.coshift.c_adapters;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.UseCaseInteractor;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.c_adapters.security.AuthenticationProviderSpring;
import org.coshift.c_adapters.security.SpringPasswordChecker;
import org.coshift.c_adapters.web.PersonController;
import org.coshift.d_frameworks.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@Import({SpringConfig.class,
         SpringPasswordChecker.class,
         AuthenticationProviderSpring.class})
class PersonControllerSecurityTest {

    @Autowired MockMvc mvc;

    @MockBean UseCaseInteractor interactor;
    @MockBean PersonRepository persons;
    @MockBean TimeAccountRepository accounts;

    @Test
    void create_nonAdmin_forbidden() throws Exception {
        when(interactor.authenticateUser("user", "pw"))
            .thenReturn(new Person(1L, "user", "pw", PersonRole.USER));

        String body = "{\"nickname\":\"nick\",\"password\":\"secret\"}";
        mvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(httpBasic("user", "pw")))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRole_nonAdmin_forbidden() throws Exception {
        when(interactor.authenticateUser("user", "pw"))
            .thenReturn(new Person(1L, "user", "pw", PersonRole.USER));

        mvc.perform(put("/api/persons/5/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"ADMIN\"")
                .with(httpBasic("user", "pw")))
                .andExpect(status().isForbidden());
    }
}
