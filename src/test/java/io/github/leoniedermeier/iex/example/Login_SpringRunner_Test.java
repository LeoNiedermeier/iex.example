package io.github.leoniedermeier.iex.example;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest()
@DirtiesContext
@AutoConfigureMockMvc
public class Login_SpringRunner_Test {

    @Autowired
    MockMvc mvc;

    @Test
    public void testLogin_autenticated() throws Exception {
        mvc.perform(SecurityMockMvcRequestBuilders.formLogin("/login").user("admin").password("admin"))//
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    public void testLogin_unauthenticated() throws Exception {
        mvc.perform(formLogin("/login").user("XXX").password("XXX"))//
                .andExpect(unauthenticated());
    }

}
