package com.example.demo;

import com.example.demo.example.controller.SampleController;
import com.example.demo.example.entity.Sample;
import com.example.demo.example.service.SampleService;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(RestDocumentationExtension.class)
public class SampleControllerOpenApiTest {

    private MockMvc mockMvc;

    @Mock
    private SampleService sampleService;

    @InjectMocks
    private SampleController sampleController;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sampleController)
                .apply(MockMvcRestDocumentationWrapper.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void getAllSamples() throws Exception {
        // Mock response 설정
        when(sampleService.getAllSamples()).thenReturn(List.of(
                Sample.builder()
                        .sampleId(1L)
                        .name("Sample Name")
                        .description("Sample Description")
                        .build()
        ));

        // 요청 수행 및 문서화
        mockMvc.perform(get("/api/v1/samples"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document("get-all-samples",
                        ResourceSnippetParameters.builder()
                                .tag("Sample API")
                                .summary("모든 샘플 조회")
                                .description("모든 샘플 목록을 조회합니다.")
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("샘플 이름"),
                                        fieldWithPath("data[].description").type(JsonFieldType.STRING).description("샘플 설명")
                                )
                                .build()
                ));
    }

    @Test
    public void getSampleById() throws Exception {
        // Mock response 설정
        Long sampleId = 1L;
        when(sampleService.getSampleById(sampleId)).thenReturn(
                Sample.builder()
                        .sampleId(sampleId)
                        .name("Sample Name")
                        .description("Sample Description")
                        .build()
        );

        // 요청 수행 및 문서화
        mockMvc.perform(get("/api/v1/samples/{id}", sampleId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document("get-sample-by-id",
                        ResourceSnippetParameters.builder()
                                .tag("Sample API")
                                .summary("샘플 ID로 조회")
                                .description("주어진 ID에 해당하는 샘플을 조회합니다.")
                                .pathParameters(
                                        parameterWithName("id").description("조회할 샘플의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("샘플 이름"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("샘플 설명")
                                )
                                .build()
                ));
    }

    @Test
    public void createSample() throws Exception {
        // 요청 수행 및 문서화
        mockMvc.perform(post("/api/v1/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Sample Name\", \"description\": \"Sample Description\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document("create-sample",
                        ResourceSnippetParameters.builder()
                                .tag("Sample API")
                                .summary("새로운 샘플 생성")
                                .description("새로운 샘플을 생성합니다.")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("샘플 이름"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("샘플 설명")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 샘플 ID"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("샘플 이름"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("샘플 설명")
                                )
                                .build()
                ));
    }
}
