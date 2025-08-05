package org.coshift;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.UseCaseInteractor;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.c_adapters.web.PersonController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest(PersonController.class)
@Import(PersonControllerSecurityTest.TestConfig.class)
class PersonControllerSecurityTest {

    @Configuration
    @EnableMethodSecurity
    static class TestConfig {
    }

    @Autowired
    MockMvc mvc;

    @MockBean
    UseCaseInteractor interactor;

    @MockBean
    PersonRepository persons;

    @MockBean
    TimeAccountRepository accounts;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreatePerson() throws Exception {
        when(interactor.addPerson(anyString(), anyString()))
                .thenReturn(new Person(1L, "nick", "pw"));

        mvc.perform(post("/api/persons")
                .with(csrf())
                .contentType("application/json")
                .content("{\"nickname\":\"nick\",\"password\":\"pw\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void userCannotCreatePerson() throws Exception {
        mvc.perform(post("/api/persons")
                .with(csrf())
                .contentType("application/json")
                .content("{\"nickname\":\"nick\",\"password\":\"pw\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanChangeRole() throws Exception {
        when(interactor.updatePersonRole(anyLong(), ArgumentMatchers.any(PersonRole.class)))
                .thenReturn(new Person(1L, "nick", "pw", PersonRole.ADMIN));

        mvc.perform(put("/api/persons/1/role")
                .with(csrf())
                .contentType("application/json")
                .content("\"ADMIN\""))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void userCannotChangeRole() throws Exception {
        mvc.perform(put("/api/persons/1/role")
                .with(csrf())
                .contentType("application/json")
                .content("\"ADMIN\""))
                .andExpect(status().isForbidden());
    }
}

