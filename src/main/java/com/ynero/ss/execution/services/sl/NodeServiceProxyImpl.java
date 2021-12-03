package com.ynero.ss.execution.services.sl;

import com.ynero.ss.execution.domain.dto.NodeDTO;
import com.ynero.ss.execution.domain.dto.NodeGetDTO;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class NodeServiceProxyImpl implements NodeServiceProxy {

    @Setter(onMethod_ = {@Autowired})
    private NodeService nodeService;

    @Setter(onMethod_ = {@Autowired})
    private UserService userService;

    @Override
    public List<NodeGetDTO> getAllUsersNodes(Authentication authentication) {
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        return nodeService.getAllUsersNodes(username);
    }

    @Override
    public List<NodeGetDTO> getAllTenantsNodes(String tenantId) {
        return nodeService.getAllTenantsNodes(tenantId);
    }

    @Override
    public boolean update(NodeDTO dto, String nodeId, Authentication authentication) {
        return false;
    }

    @Override
    public boolean delete(String nodeId, Authentication authentication) {
       /*nodeService.delete()*/
       return false;
    }

    @Override
    @SneakyThrows
    public String save(NodeDTO dto, Authentication authentication) {
        var username = ((UserDetails)authentication.getPrincipal()).getUsername();
        if (dto.getUsername() == null) {
            dto.setUsername(username);
        }
        if (dto.getTenantId() == null) {
            var user = userService.findByUsername(username);
            if (user.isPresent()) {
                dto.setTenantId(user.get().getTenantId());
            } else throw new Exception("No such user");
        }
        return nodeService.save(dto);
    }

    @Override
    public NodeGetDTO findById(String nodeId, Authentication authentication) {
        return null;
    }
}
