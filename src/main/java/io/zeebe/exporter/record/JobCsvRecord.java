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

import java.util.Objects;

public class JobCsvRecord extends CsvRecord {

  private long workflowKey;
  private String bpmnProcessId;
  private int workflowDefinitionVersion;

  private String elementId;

  private long workflowInstanceKey;
  private long elementInstanceKey;

  private String type;

  public long getWorkflowKey() {
    return workflowKey;
  }

  public JobCsvRecord setWorkflowKey(final long workflowKey) {
    this.workflowKey = workflowKey;
    return this;
  }

  public String getBpmnProcessId() {
    return bpmnProcessId;
  }

  public JobCsvRecord setBpmnProcessId(final String bpmnProcessId) {
    this.bpmnProcessId = bpmnProcessId;
    return this;
  }

  public int getWorkflowDefinitionVersion() {
    return workflowDefinitionVersion;
  }

  public JobCsvRecord setWorkflowDefinitionVersion(final int workflowDefinitionVersion) {
    this.workflowDefinitionVersion = workflowDefinitionVersion;
    return this;
  }

  public String getElementId() {
    return elementId;
  }

  public JobCsvRecord setElementId(final String elementId) {
    this.elementId = elementId;
    return this;
  }

  public long getWorkflowInstanceKey() {
    return workflowInstanceKey;
  }

  public JobCsvRecord setWorkflowInstanceKey(final long workflowInstanceKey) {
    this.workflowInstanceKey = workflowInstanceKey;
    return this;
  }

  public long getElementInstanceKey() {
    return elementInstanceKey;
  }

  public JobCsvRecord setElementInstanceKey(final long elementInstanceKey) {
    this.elementInstanceKey = elementInstanceKey;
    return this;
  }

  public String getType() {
    return type;
  }

  public JobCsvRecord setType(final String type) {
    this.type = type;
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
    JobCsvRecord that = (JobCsvRecord) o;
    return workflowKey == that.workflowKey
        && workflowDefinitionVersion == that.workflowDefinitionVersion
        && workflowInstanceKey == that.workflowInstanceKey
        && elementInstanceKey == that.elementInstanceKey
        && bpmnProcessId.equals(that.bpmnProcessId)
        && elementId.equals(that.elementId)
        && type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        workflowKey,
        bpmnProcessId,
        workflowDefinitionVersion,
        elementId,
        workflowInstanceKey,
        elementInstanceKey,
        type);
  }

  @Override
  public String toString() {
    return "JobCsvRecord{"
        + "workflowKey="
        + workflowKey
        + ", bpmnProcessId='"
        + bpmnProcessId
        + '\''
        + ", workflowDefinitionVersion="
        + workflowDefinitionVersion
        + ", elementId='"
        + elementId
        + '\''
        + ", workflowInstanceKey="
        + workflowInstanceKey
        + ", elementInstanceKey="
        + elementInstanceKey
        + ", type='"
        + type
        + '\''
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
