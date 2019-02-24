/*
 * Copyright © 2019 camunda services GmbH (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zeebe.exporter.record;

import io.zeebe.protocol.BpmnElementType;
import java.util.Objects;

public class WorkflowInstanceCsvRecord extends CsvRecord {

  private long workflowKey;
  private String bpmnProcessId;
  private int version;

  private String elementId;
  private BpmnElementType bpmnElementType;

  private long workflowInstanceKey;
  private long flowScopeKey;

  public long getWorkflowKey() {
    return workflowKey;
  }

  public WorkflowInstanceCsvRecord setWorkflowKey(final long workflowKey) {
    this.workflowKey = workflowKey;
    return this;
  }

  public String getBpmnProcessId() {
    return bpmnProcessId;
  }

  public WorkflowInstanceCsvRecord setBpmnProcessId(final String bpmnProcessId) {
    this.bpmnProcessId = bpmnProcessId;
    return this;
  }

  public int getVersion() {
    return version;
  }

  public WorkflowInstanceCsvRecord setVersion(final int version) {
    this.version = version;
    return this;
  }

  public String getElementId() {
    return elementId;
  }

  public WorkflowInstanceCsvRecord setElementId(final String elementId) {
    this.elementId = elementId;
    return this;
  }

  public BpmnElementType getBpmnElementType() {
    return bpmnElementType;
  }

  public WorkflowInstanceCsvRecord setBpmnElementType(final BpmnElementType bpmnElementType) {
    this.bpmnElementType = bpmnElementType;
    return this;
  }

  public long getWorkflowInstanceKey() {
    return workflowInstanceKey;
  }

  public WorkflowInstanceCsvRecord setWorkflowInstanceKey(final long workflowInstanceKey) {
    this.workflowInstanceKey = workflowInstanceKey;
    return this;
  }

  public long getFlowScopeKey() {
    return flowScopeKey;
  }

  public WorkflowInstanceCsvRecord setFlowScopeKey(final long flowScopeKey) {
    this.flowScopeKey = flowScopeKey;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    WorkflowInstanceCsvRecord that = (WorkflowInstanceCsvRecord) o;
    return workflowKey == that.workflowKey
        && version == that.version
        && workflowInstanceKey == that.workflowInstanceKey
        && flowScopeKey == that.flowScopeKey
        && bpmnProcessId.equals(that.bpmnProcessId)
        && elementId.equals(that.elementId)
        && bpmnElementType == that.bpmnElementType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        workflowKey,
        bpmnProcessId,
        version,
        elementId,
        bpmnElementType,
        workflowInstanceKey,
        flowScopeKey);
  }

  @Override
  public String toString() {
    return "WorkflowInstanceCsvRecord{"
        + "workflowKey="
        + workflowKey
        + ", bpmnProcessId='"
        + bpmnProcessId
        + '\''
        + ", version="
        + version
        + ", elementId='"
        + elementId
        + '\''
        + ", bpmnElementType="
        + bpmnElementType
        + ", workflowInstanceKey="
        + workflowInstanceKey
        + ", flowScopeKey="
        + flowScopeKey
        + ", key="
        + key
        + ", partition="
        + partition
        + ", startPosition="
        + startPosition
        + ", endPosition="
        + endPosition
        + ", created="
        + created
        + ", ended="
        + ended
        + ", duration="
        + duration
        + ", completed="
        + completed
        + '}';
  }
}
