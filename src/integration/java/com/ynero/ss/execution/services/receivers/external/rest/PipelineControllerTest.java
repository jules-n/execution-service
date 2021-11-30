package com.ynero.ss.execution.services.receivers.external.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ynero.ss.execution.IntegrationTestSetUp;
import com.ynero.ss.execution.controllers.PipelineController;
import com.ynero.ss.execution.domain.dto.EdgeDTO;
import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.services.sl.NodeService;
import com.ynero.ss.pipeline.dto.proto.PipelinesMessage;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("integration-test")
@Testcontainers
@DirtiesContext
@AutoConfigureMockMvc
@Log4j2
public class PipelineControllerTest extends IntegrationTestSetUp {

    @Autowired
    private MockMvc mockMvc;

    private List<String> nodeIds = new ArrayList<>();
    private String pipelineId;
    private List<EdgeDTO> edges;
    private UUID userId;

    private ObjectMapper mapper = new ObjectMapper();
    ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
    private String requestJson;

    @Setter(onMethod_ = {@Autowired})
    private NodeService nodeService;

    @Setter(onMethod_ = {@Autowired})
    private PipelineController controller;

    private List<PipelinesMessage.DeviceData> deviceDataList = new ArrayList<>();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        userId = UUID.randomUUID();
        var dtos = List.of(
                NodeDTO.builder()
                        .script("def val = params[\"temperature-read\"]; Map<String, Object> result = new HashMap<>(); result.put(\"val\", val); return result;")
                        .inputPortsName(List.of("temperature-read"))
                        .outputPortsName(List.of("val"))
                        .description("Node for getting data from device")
                        .build(),
                NodeDTO.builder()
                        .script("def val = params[\"val\"]; def K = params[\"K\"]; def C = val.toBigDecimal()-K.toBigDecimal(); Map<String, Object> result = new HashMap<>(); result.put(\"C\", C); return result;")
                        .inputPortsName(List.of("val", "K"))
                        .outputPortsName(List.of("C"))
                        .description("Node for changing temperature in K to temperature in C")
                        .build(),
                NodeDTO.builder()
                        .script("Map<String, Object> result = new HashMap<>(); result.put(\"K\", 273.15); return result;")
                        .outputPortsName(List.of("K"))
                        .description("Constant node of diff between K and C")
                        .build(),
                NodeDTO.builder()
                        .script("def inp1 = params[\"input-1\"]; def inp2 = params[\"input-2\"]; def avg = (inp1.toBigDecimal()+inp2.toBigDecimal())/2; Map<String, Object> result = new HashMap<>(); result.put(\"avg\", avg); return result;")
                        .inputPortsName(List.of("input-1", "input-2"))
                        .description("Node of avg of two val")
                        .outputPortsName(List.of("avg"))
                        .build()
        );
        dtos.forEach(
                nodeDTO -> {
                    nodeIds.add(nodeService.save(nodeDTO));
                }
        );
        edges = List.of(
                EdgeDTO.builder()
                        .nodeIdi(nodeIds.get(0))
                        .nodeIdj(nodeIds.get(1))
                        .outputPortNameOfNodeI("val")
                        .inputPortNameOfNodeJ("val")
                        .build(),
                EdgeDTO.builder()
                        .nodeIdi(nodeIds.get(2))
                        .nodeIdj(nodeIds.get(1))
                        .outputPortNameOfNodeI("K")
                        .inputPortNameOfNodeJ("K")
                        .build(),
                EdgeDTO.builder()
                        .nodeIdi(nodeIds.get(1))
                        .nodeIdj(nodeIds.get(3))
                        .outputPortNameOfNodeI("C")
                        .inputPortNameOfNodeJ("input-2")
                        .build(),
                EdgeDTO.builder()
                        .nodeIdi(nodeIds.get(0))
                        .nodeIdj(nodeIds.get(1))
                        .outputPortNameOfNodeI("val")
                        .inputPortNameOfNodeJ("val")
                        .build(),
                EdgeDTO.builder()
                        .nodeIdi(nodeIds.get(2))
                        .nodeIdj(nodeIds.get(1))
                        .outputPortNameOfNodeI("K")
                        .inputPortNameOfNodeJ("K")
                        .build(),
                EdgeDTO.builder()
                        .nodeIdi(nodeIds.get(1))
                        .nodeIdj(nodeIds.get(3))
                        .outputPortNameOfNodeI("C")
                        .inputPortNameOfNodeJ("input-1")
                        .build()
        );
    }

    @Test
    public void methodCreateOnUrlPipelinesShouldReturnBadRequestWhenGivenWrongPortsDataInEdges() throws Exception {
        var wrongPort = "input-2 ";
        edges.get(2).setInputPortNameOfNodeJ(wrongPort);
        var pipelineDTO = PipelineDTO.builder()
                .tenantId("Volbeat")
                .edges(edges)
                .build();
        requestJson = objectWriter.writeValueAsString(pipelineDTO);
        this.mockMvc.perform(post("/pipelines/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(log())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void methodCreateOnUrlPipelinesShouldReturnStatusOkWhenGivenCorrectPortsDataInEdges() throws Exception {
        var pipelineDTO = PipelineDTO.builder()
                .tenantId("Volbeat")
                .edges(edges)
                .build();
        requestJson = objectWriter.writeValueAsString(pipelineDTO);
        this.mockMvc.perform(post("/pipelines/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(log())
                .andExpect(status().isOk());
    }

}
