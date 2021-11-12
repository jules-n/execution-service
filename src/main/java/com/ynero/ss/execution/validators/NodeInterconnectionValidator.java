package com.ynero.ss.execution.validators;

import com.ynero.ss.execution.domain.dto.EdgeDTO;
import com.ynero.ss.execution.services.sl.NodeService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NodeInterconnectionValidator implements
        ConstraintValidator<NodeInterconnection, List<EdgeDTO>> {

    @Setter(onMethod_ = {@Autowired})
    private NodeService nodeService;

    @Override
    public void initialize(NodeInterconnection constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<EdgeDTO> edges, ConstraintValidatorContext constraintValidatorContext) {
        return edges.stream().map(
                edgeDTO -> {
                    var nodeJId = edgeDTO.getNodeIdj();
                    var nodeIId = edgeDTO.getNodeIdi();
                    var nodeInPort = edgeDTO.getInputPortNameOfNodeJ();
                    var nodeOutPort = edgeDTO.getOutputPortNameOfNodeI();
                    var nodeI = nodeService.findById(nodeIId);
                    var nodeJ = nodeService.findById(nodeJId);
                    if (!nodeI.getOutputPortsName().contains(nodeOutPort)) return false;
                    if (!nodeJ.getInputPortsName().contains(nodeInPort)) return false;
                    return true;
                }
        ).filter(r -> !r).findFirst().orElse(true);
    }
}
