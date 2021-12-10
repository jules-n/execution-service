package com.ynero.ss.execution.validators;

import com.ynero.ss.execution.domain.dto.PipelineDTO;
import com.ynero.ss.execution.services.sl.NodeService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TenantsAccordanceValidator implements
        ConstraintValidator<NodeInterconnection, PipelineDTO> {
    @Setter(onMethod_ = {@Autowired})
    private NodeService nodeService;

    @Override
    public boolean isValid(PipelineDTO pipelineDTO, ConstraintValidatorContext constraintValidatorContext) {
        var pipelineTenant = pipelineDTO.getTenantId();
        var result = pipelineDTO.getEdges().stream().map(
                edgeDTO -> {
                    var nodeI = nodeService.findById(edgeDTO.getNodeIdi());
                    if(!nodeI.getTenantId().equals(pipelineTenant)) return false;
                    var nodeJ = nodeService.findById(edgeDTO.getNodeIdj());
                    return nodeJ.getTenantId().equals(pipelineTenant);
                }
        ).anyMatch(res -> res==false);

        return result;
    }
}
