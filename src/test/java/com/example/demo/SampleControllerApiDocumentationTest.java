package com.example.demo;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SampleController에 대한 Spec 테스트 클래스입니다.
 * REST Docs와 OpenAPI 문서화를 함께 수행합니다.
 */
@ExtendWith(RestDocumentationExtension.class)
public class SampleControllerApiDocumentationTest {

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
                // REST Docs 용
                .andDo(MockMvcRestDocumentation.document("get-all-samples",
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("샘플 이름"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("샘플 설명")
                        )
                ))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("get-all-samples",
                        resource(
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
                        )
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
                // REST Docs 용
                .andDo(MockMvcRestDocumentation.document("get-sample-by-id",
                        pathParameters(
                                parameterWithName("id").description("조회할 샘플의 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("샘플 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("샘플 설명")
                        )
                ))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("get-sample-by-id",
                        resource(
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
                        )
                ));
    }

    @Test
    public void getSamplesByDescription() throws Exception {
        // Mock response 설정
        when(sampleService.getSamplesByDescriptionNative(any())).thenReturn(List.of(
                Sample.builder()
                        .sampleId(1L)
                        .name("Sample Name")
                        .description("Sample Description")
                        .build()
        ));

        // 요청 수행 및 문서화
        mockMvc.perform(get("/api/v1/samples/searchByDescription")
                        .param("description", "Sample Description"))
                .andExpect(status().isOk())
                .andDo(print())
                // REST Docs 용
                .andDo(MockMvcRestDocumentation.document("get-samples-by-description",
                        queryParameters(
                                parameterWithName("description").description("검색할 설명")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("샘플 이름"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("샘플 설명")
                        )
                ))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("get-samples-by-description",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Sample API")
                                        .summary("설명으로 샘플 검색")
                                        .description("설명을 기준으로 샘플을 검색합니다.")
                                        .queryParameters(
                                                parameterWithName("description").description("검색할 설명")
                                        )
                                        .responseFields(
                                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("샘플 이름"),
                                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("샘플 설명")
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    public void getSamplesByName() throws Exception {
        // Mock response 설정
        when(sampleService.getSamplesByNameQueryDsl(any())).thenReturn(List.of(
                Sample.builder()
                        .sampleId(1L)
                        .name("Sample Name")
                        .description("Sample Description")
                        .build()
        ));

        // 요청 수행 및 문서화
        mockMvc.perform(get("/api/v1/samples/searchByName")
                        .param("name", "Sample Name"))
                .andExpect(status().isOk())
                .andDo(print())
                // REST Docs 용
                .andDo(MockMvcRestDocumentation.document("get-samples-by-name",
                        queryParameters(
                                parameterWithName("name").description("검색할 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("샘플 이름"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("샘플 설명")
                        )
                ))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("get-samples-by-name",
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Sample API")
                                        .summary("이름으로 샘플 검색")
                                        .description("이름을 기준으로 샘플을 검색합니다.")
                                        .queryParameters(
                                                parameterWithName("name").description("검색할 이름")
                                        )
                                        .responseFields(
                                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("샘플 ID"),
                                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("샘플 이름"),
                                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("샘플 설명")
                                        )
                                        .build()
                        )
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
                // REST Docs 용
                .andDo(MockMvcRestDocumentation.document("create-sample",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("샘플 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("샘플 설명")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("샘플 생성 결과 데이터, null일 수 있음")
                        )
                ))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("create-sample",
                        resource(
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
                                                fieldWithPath("data").type(JsonFieldType.NULL).description("샘플 생성 결과 데이터, null일 수 있음")
                                        )
                                        .build()
                        )
                ));
    }
}
