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
package io.zeebe.exporter.aggregator;

import io.zeebe.exporter.record.WorkflowInstanceCsvRecord;
import io.zeebe.exporter.writer.CsvWriter;
import io.zeebe.protocol.record.intent.WorkflowInstanceIntent;
import io.zeebe.protocol.record.value.WorkflowInstanceRecordValue;

public class WorkflowInstanceAggregator
    extends Aggregator<WorkflowInstanceCsvRecord, WorkflowInstanceRecordValue> {

  public WorkflowInstanceAggregator(CsvWriter csvWriter) {
    super(
        csvWriter,
        WorkflowInstanceIntent.ELEMENT_ACTIVATING,
        WorkflowInstanceIntent.ELEMENT_COMPLETED,
        WorkflowInstanceIntent.ELEMENT_TERMINATED);
  }

  @Override
  WorkflowInstanceCsvRecord create(WorkflowInstanceRecordValue recordValue) {
    return new WorkflowInstanceCsvRecord()
        .setWorkflowKey(recordValue.getWorkflowKey())
        .setBpmnProcessId(recordValue.getBpmnProcessId())
        .setVersion(recordValue.getVersion())
        .setElementId(recordValue.getElementId())
        .setBpmnElementType(recordValue.getBpmnElementType())
        .setWorkflowInstanceKey(recordValue.getWorkflowInstanceKey())
        .setFlowScopeKey(recordValue.getFlowScopeKey());
  }
}
