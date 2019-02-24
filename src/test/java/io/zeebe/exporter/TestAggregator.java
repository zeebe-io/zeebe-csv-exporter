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
package io.zeebe.exporter;

import io.zeebe.exporter.aggregator.Aggregator;
import io.zeebe.exporter.aggregator.JobAggregator;
import io.zeebe.exporter.aggregator.WorkflowInstanceAggregator;
import io.zeebe.exporter.record.CsvRecord;
import io.zeebe.exporter.record.JobCsvRecord;
import io.zeebe.exporter.record.Record;
import io.zeebe.exporter.record.RecordValue;
import io.zeebe.exporter.record.WorkflowInstanceCsvRecord;
import io.zeebe.exporter.record.value.JobRecordValue;
import io.zeebe.exporter.record.value.WorkflowInstanceRecordValue;
import io.zeebe.exporter.writer.CsvWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestAggregator<
    T extends CsvRecord, R extends RecordValue, A extends Aggregator<T, R>> {

  private final Map<Integer, CsvListWriter<T>> writer = new HashMap<>();
  private final Map<Integer, A> aggregator = new HashMap<>();
  private final Function<CsvWriter, A> aggregatorFactory;

  public TestAggregator(Function<CsvWriter, A> aggregatorFactory) {
    this.aggregatorFactory = aggregatorFactory;
  }

  public void process(Record<R> record) {
    int partition = record.getMetadata().getPartitionId();
    A aggregator = this.aggregator.computeIfAbsent(partition, this::createAggregator);
    aggregator.process(record);
  }

  public List<List<T>> getRecords() {
    return writer.values().stream().map(CsvListWriter::getRecords).collect(Collectors.toList());
  }

  private A createAggregator(Integer partition) {
    CsvWriter csvWriter = writer.computeIfAbsent(partition, p -> new CsvListWriter<>());
    return aggregatorFactory.apply(csvWriter);
  }

  public static class WorkflowInstanceTestAggregator
      extends TestAggregator<
          WorkflowInstanceCsvRecord, WorkflowInstanceRecordValue, WorkflowInstanceAggregator> {
    public WorkflowInstanceTestAggregator() {
      super(WorkflowInstanceAggregator::new);
    }
  }

  public static class JobTestAggregator
      extends TestAggregator<JobCsvRecord, JobRecordValue, JobAggregator> {
    public JobTestAggregator() {
      super(JobAggregator::new);
    }
  }
}
