package demo.account;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @Test
    public void getUserAccountsShouldReturnAccounts() throws Exception {
        String content = "[{\"username\": \"user\", \"accountNumber\":\"123456789\"}]";
        BDDMockito.given(this.accountService.getUserAccounts()).willReturn(
                Collections.singletonList(new Account("user","123456789")));

        this.mockMvc.perform(
                MockMvcRequestBuilders
                .get("/v1/accounts")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(content));
    }


}