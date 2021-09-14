package com.ynero.ss.execution.builder;

import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.dto.EdgeDTO;
import com.ynero.ss.execution.domain.dto.NodeBuildDTO;
import com.ynero.ss.execution.domain.dto.PortBuildDTO;
import com.ynero.ss.execution.services.sl.NodeService;
import com.ynero.ss.execution.services.sl.PipelineService;
import com.ynero.ss.pipeline.dto.proto.PipelinesMessage;
import dtos.ResultDTO;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class GraphBuilder {

    @Setter(onMethod_ = {@Autowired})
    private PipelineService pipelineService;

    @Setter(onMethod_ = {@Autowired})
    private NodeService nodeService;

    @Setter(onMethod_ = {@Autowired})
    private ModelMapper modelMapper;

    @Setter(onMethod_ = {@Autowired})
    private GroovyShell groovyShell;

    public List<ResultDTO> build(PipelinesMessage.PipelineDevices pipelineDevices, String tenantId) {
        var pipelineId = pipelineDevices.getPipelineId();
        var pipelineDTO = pipelineService.find(pipelineId);

        List<String> leaves = findLeaves(pipelineDTO.getEdges());
        List<Node> nodes = new ArrayList<>(); //list of nodes data from db

        //Search of node sequence
        pipelineDTO.getEdges().forEach(
                edgeDTO -> {
                    var nodeIdi = edgeDTO.getNodeIdi();
                    var getDTO = nodeService.findById(nodeIdi);
                    var node = modelMapper.map(getDTO, Node.class);
                    nodes.add(node);
                }
        );

        pipelineDTO.getEdges().forEach(
                edgeDTO -> {
                    var alreadyAddedNodeIds = nodes.stream()
                            .map(node -> node.getNodeId())
                            .collect(Collectors.toList());
                    var nodeIdj = edgeDTO.getNodeIdj();
                    if (!alreadyAddedNodeIds.contains(UUID.fromString(nodeIdj))) {
                        var getDTO = nodeService.findById(nodeIdj);
                        var node = modelMapper.map(getDTO, Node.class);
                        nodes.add(node);
                    }
                }
        );
        //End of search


        List<NodeBuildDTO> nodeBuildDTOS = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            var currentNode = nodes.get(i);
            var inputPortsOfCurrentNode = currentNode.getInputPortsName();
            List inputPorts = null;
            if (inputPortsOfCurrentNode != null) {
                inputPorts = inputPortsOfCurrentNode.stream().map(
                        portName -> {
                            var portBuild = PortBuildDTO.builder()
                                    .name(portName)
                                    .build();
                            return portBuild;
                        }
                ).collect(Collectors.toList());
            }
            var outputPorts = currentNode.getOutputPortsName().stream().map(
                    portName -> {
                        var portBuild = PortBuildDTO.builder()
                                .name(portName)
                                .build();
                        return portBuild;
                    }
            ).collect(Collectors.toList());

            var buildNode = NodeBuildDTO.builder()
                    .nodeId(currentNode.getNodeId());
            if (inputPorts != null) {
                buildNode.input(inputPorts);
            }
            buildNode.output(outputPorts)
                    .script(currentNode.getScript())
                    .build();

            nodeBuildDTOS.add(buildNode.build());
        }

        Stack<PipelinesMessage.DeviceData> deviceDataStack = new Stack<>();
        pipelineDevices.getDevicesDataList().forEach(
                deviceData -> {
                    deviceDataStack.push(deviceData);
                }
        );
        nodeBuildDTOS.forEach(
                nodeBuildDTO -> {
                    if (leaves.contains(nodeBuildDTO.getNodeId().toString())) {
                        var inputs = nodeBuildDTO.getInput();
                        if (inputs != null) {
                            inputs.forEach(
                                    input -> {
                                        for (int i = 0; i < deviceDataStack.size(); i++) {
                                            var checkingDeviceDataElement = deviceDataStack.get(i);
                                            if (input.getName().equals(checkingDeviceDataElement.getPort())) {
                                                input.setValue(checkingDeviceDataElement.getValue());
                                                deviceDataStack.remove(checkingDeviceDataElement);
                                            }
                                        }
                                    }
                            );
                        }
                    }

                }
        );

        var roots = findRoots(pipelineDTO.getEdges());
        var results = new ArrayList<ResultDTO>();
        var copy = List.copyOf(nodeBuildDTOS);
        roots.forEach(
                root -> {
                    copy.forEach(
                            nodeBuildDTO -> {
                                if (nodeBuildDTO.getNodeId().equals(UUID.fromString(root))) {
                                    var result = treeTraversal(nodeBuildDTO, nodeBuildDTOS, pipelineDTO.getEdges());
                                    result.getOutput().forEach(
                                            port -> {
                                                var resultDTO = ResultDTO.builder()
                                                        .value(port.getValue())
                                                        .portName(port.getName())
                                                        .tenantId(tenantId)
                                                        .pipelineId(UUID.fromString(pipelineId))
                                                        .deviceId(result.getNodeId())
                                                        .build();
                                                results.add(resultDTO);
                                            }
                                    );
                                }
                            }
                    );
                }
        );
        return results;
    }

    private NodeBuildDTO treeTraversal(NodeBuildDTO nodeBuildDTO, List<NodeBuildDTO> nodeBuildDTOS, List<EdgeDTO> edges) {
        nodeBuildDTO.getOutput().forEach(
                output -> {
                    if (output.getValue() == null) {
                        if (!(nodeBuildDTO.getInput() == null)) {
                            nodeBuildDTO.getInput().forEach(
                                    input -> {
                                        if (input.getValue() == null) {
                                            var portName = input.getName();
                                            var edgeWithAdjacentNode = findEdgeWithAdjacentNode(portName, nodeBuildDTO.getNodeId(), edges);
                                            var adjacentNode = nodeBuildDTOS.stream()
                                                    .filter(buildDTO -> buildDTO.getNodeId().equals(UUID.fromString(edgeWithAdjacentNode.getNodeIdi())))
                                                    .findFirst()
                                                    .get();
                                            nodeBuildDTOS.remove(adjacentNode);
                                            var outputsOfAdjacentNode = treeTraversal(adjacentNode, nodeBuildDTOS, edges).getOutput();
                                            var outputOfAdjacentNode = outputsOfAdjacentNode.stream()
                                                    .filter(adjacentNodeOutput -> adjacentNodeOutput.getName().equals(edgeWithAdjacentNode.getOutputPortNameOfNodeI()))
                                                    .map(adjacentNodeOutput -> adjacentNodeOutput.getValue()).findFirst().get();
                                            input.setValue(outputOfAdjacentNode);
                                        }

                                    }
                            );
                        }

                        var script = groovyShell.parse(nodeBuildDTO.getScript());
                        if (!(nodeBuildDTO.getInput() == null)) {
                            var binding = new Binding();
                            var params = new HashMap<String, Object>();
                            nodeBuildDTO.getInput().forEach(
                                    portBuildDTO -> params.put(portBuildDTO.getName(), portBuildDTO.getValue())
                            );
                            binding.setProperty("params", params);
                            script.setBinding(binding);
                        }
                        var scriptResult = ((Map<String, Object>) script.run()).get(output.getName());
                        output.setValue(scriptResult);
                    }
                }
        );
        return nodeBuildDTO;
    }

    private EdgeDTO findEdgeWithAdjacentNode(String portName, UUID nodeId, List<EdgeDTO> edges) {
        return edges.stream().filter(
                edgeDTO -> {
                    var isNeededId = edgeDTO.getNodeIdj().equals(nodeId.toString());
                    var isNeededPort = edgeDTO.getInputPortNameOfNodeJ().equals(portName);
                    if (isNeededId && isNeededPort)
                        return true;
                    return false;
                }
        ).findFirst().get();
    }

    private List<String> findLeaves(List<EdgeDTO> edges) {
        List<String> leaves = new ArrayList();

        for (int i = 0; i < edges.size(); i++) {
            var currentPi = edges.get(i).getNodeIdi();
            leaves.add(currentPi);

            for (int j = 0; j < edges.size(); j++) {
                if (currentPi.equals(edges.get(j).getNodeIdj())) {
                    leaves.remove(currentPi);
                    break;
                }
            }

        }
        return leaves;
    }

    private Set<String> findRoots(List<EdgeDTO> edges) {
        Set<String> roots = new HashSet<>();

        for (int i = 0; i < edges.size(); i++) {
            var currentPj = edges.get(i).getNodeIdj();
            roots.add(currentPj);

            for (int j = 0; j < edges.size(); j++) {
                if (currentPj.equals(edges.get(j).getNodeIdi())) {
                    roots.remove(currentPj);
                    break;
                }
            }

        }
        return roots;
    }

}