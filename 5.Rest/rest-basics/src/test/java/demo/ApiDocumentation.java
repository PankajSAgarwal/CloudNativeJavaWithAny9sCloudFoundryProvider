package demo;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir="target/generated.snippets")
@SpringBootTest(classes=Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ApiDocumentation {

    private MockMvc mockMvc;

    @Test
    public void errorExample() throws Exception{
        this.mockMvc.perform(get("/error")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE,400)
                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,"/customers")
                .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                        "The customer 'http://localhost:8443/v1/customers/123 does not exist'"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("timstamp", is(notNullValue())))
                .andExpect(jsonPath("status",is(400)))
                .andExpect(jsonPath("path",is(notNullValue())))
                .andDo(MockMvcRestDocumentation.document("index-example"));
    }
}
