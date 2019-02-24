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

import io.zeebe.exporter.record.JobCsvRecord;
import io.zeebe.exporter.record.value.JobRecordValue;
import io.zeebe.exporter.record.value.job.Headers;
import io.zeebe.exporter.writer.CsvWriter;
import io.zeebe.protocol.intent.JobIntent;

public class JobAggregator extends Aggregator<JobCsvRecord, JobRecordValue> {

  public JobAggregator(CsvWriter csvWriter) {
    super(csvWriter, JobIntent.CREATED, JobIntent.COMPLETED, JobIntent.CANCELED);
  }

  @Override
  JobCsvRecord create(JobRecordValue recordValue) {
    Headers headers = recordValue.getHeaders();
    return new JobCsvRecord()
        .setWorkflowKey(headers.getWorkflowKey())
        .setBpmnProcessId(headers.getBpmnProcessId())
        .setWorkflowDefinitionVersion(headers.getWorkflowDefinitionVersion())
        .setElementId(headers.getElementId())
        .setWorkflowInstanceKey(headers.getWorkflowInstanceKey())
        .setElementInstanceKey(headers.getElementInstanceKey())
        .setType(recordValue.getType());
  }
}
