package com.example.demo;

import com.example.demo.example.controller.SampleController;
import com.example.demo.example.entity.Sample;
import com.example.demo.example.service.SampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
public class SampleControllerRestDocsTest {

    private MockMvc mockMvc;

    @Mock
    private SampleService sampleService;

    @InjectMocks
    private SampleController sampleController;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sampleController)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void getAllSamples() throws Exception {
        // Setup mock response
        when(sampleService.getAllSamples()).thenReturn(List.of(Sample.builder().sampleId(1L).name("Sample Name").description("Sample Description").build()));

        // Perform the request and document it
        mockMvc.perform(get("/api/v1/samples"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getAllSamples",
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("Status code of the response"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("Optional message"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("ID of the Sample"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("Name of the Sample"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("Description of the Sample")
                        )));
    }

    @Test
    public void getSampleById() throws Exception {
        // Setup mock response
        Long sampleId = 1L;
        when(sampleService.getSampleById(sampleId)).thenReturn(Sample.builder().sampleId(sampleId).name("Sample Name").description("Sample Description").build());

        // Perform the request and document it
        mockMvc.perform(get("/api/v1/samples/{id}", sampleId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getSampleById",
                        pathParameters(
                                parameterWithName("id").description("The ID of the Sample to retrieve")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("Status code of the response"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("Optional message"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("ID of the Sample"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("Name of the Sample"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("Description of the Sample")
                        )));
    }

    @Test
    public void getSamplesByDescription() throws Exception {
        // Setup mock response
        when(sampleService.getSamplesByDescriptionNative(any())).thenReturn(List.of(Sample.builder().sampleId(1L).name("Sample Name").description("Sample Description").build()));

        // Perform the request and document it
        mockMvc.perform(get("/api/v1/samples/searchByDescription").param("description", "Sample Description"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getSamplesByDescription",
                        queryParameters(
                                parameterWithName("description").description("Description to search for")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("Status code of the response"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("Optional message"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("ID of the Sample"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("Name of the Sample"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("Description of the Sample")
                        )));
    }

    @Test
    public void getSamplesByName() throws Exception {
        // Setup mock response
        when(sampleService.getSamplesByNameQueryDsl(any())).thenReturn(List.of(Sample.builder().sampleId(1L).name("Sample Name").description("Sample Description").build()));

        // Perform the request and document it
        mockMvc.perform(get("/api/v1/samples/searchByName").param("name", "Sample Name"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getSamplesByName",
                        queryParameters(
                                parameterWithName("name").description("Name to search for")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("Status code of the response"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("Optional message"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("ID of the Sample"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("Name of the Sample"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("Description of the Sample")
                        )));
    }

    @Test
    public void createSample() throws Exception {
        // Perform the request and document it
        mockMvc.perform(post("/api/v1/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Sample Name\", \"description\": \"Sample Description\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("createSample",
                        PayloadDocumentation.requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("Name of the Sample"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("Description of the Sample")
                        ),
                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("Status code of the response"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("Optional message"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("Sample creation result message, can be null")
                        )));
    }
}
