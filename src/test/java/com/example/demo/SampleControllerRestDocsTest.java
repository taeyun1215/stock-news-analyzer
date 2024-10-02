package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SampleControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration())
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    @DisplayName("모든 샘플 가져오기 테스트")
    void getAllSamples() throws Exception {
        this.mockMvc.perform(get("/api/v1/samples")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-all-samples"));
    }

    @Test
    @DisplayName("ID로 샘플 가져오기 테스트")
    void getSampleById() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/samples/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-sample-by-id"));
    }

    @Test
    @DisplayName("이름으로 샘플 검색 테스트")
    void getSamplesByName() throws Exception {
        this.mockMvc.perform(get("/api/v1/samples/search")
                        .param("name", "example")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-samples-by-name"));
    }

    @Test
    @DisplayName("샘플 생성 테스트")
    void createSample() throws Exception {
        String sampleRequestJson = "{\"name\": \"Sample Name\", \"description\": \"Sample Description\"}";

        this.mockMvc.perform(post("/api/v1/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleRequestJson))
                .andExpect(status().isCreated())
                .andDo(document("create-sample"));
    }
}
