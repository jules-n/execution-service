package com.ynero.ss.excecution.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevicesPipelineDTO {
    private String pipelineId;
    private List<String> deviceId;
    private String portName;
}
