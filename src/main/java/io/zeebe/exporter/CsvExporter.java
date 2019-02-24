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

import io.zeebe.exporter.aggregator.JobAggregator;
import io.zeebe.exporter.aggregator.WorkflowInstanceAggregator;
import io.zeebe.exporter.context.Context;
import io.zeebe.exporter.context.Controller;
import io.zeebe.exporter.record.JobCsvRecord;
import io.zeebe.exporter.record.Record;
import io.zeebe.exporter.record.WorkflowInstanceCsvRecord;
import io.zeebe.exporter.spi.Exporter;
import io.zeebe.exporter.writer.CsvFileWriter;
import io.zeebe.protocol.clientapi.ValueType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;

public class CsvExporter implements Exporter {

  public static final String WORKFLOW_INSTANCE_PREFIX = "workflow-instance";
  public static final String JOB_PREFIX = "job";

  private Logger log;
  private Path output;
  private WorkflowInstanceAggregator workflowInstanceCsvWriter;
  private JobAggregator jobCsvWriter;

  @Override
  public void configure(final Context context) {
    log = context.getLogger();
    final CsvExporterConfiguration configuration =
        context.getConfiguration().instantiate(CsvExporterConfiguration.class);

    try {
      output = createOutput(configuration);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void open(final Controller controller) {
    workflowInstanceCsvWriter =
        new WorkflowInstanceAggregator(
            new CsvFileWriter(output, WORKFLOW_INSTANCE_PREFIX, WorkflowInstanceCsvRecord.class));
    jobCsvWriter = new JobAggregator(new CsvFileWriter(output, JOB_PREFIX, JobCsvRecord.class));
  }

  @Override
  public void export(final Record record) {
    final ValueType valueType = record.getMetadata().getValueType();
    switch (valueType) {
      case WORKFLOW_INSTANCE:
        workflowInstanceCsvWriter.process(record);
        break;
      case JOB:
        jobCsvWriter.process(record);
        break;
    }
  }

  @Override
  public void close() {
    try {
      workflowInstanceCsvWriter.close();
    } catch (Exception e) {
      log.warn("Failed to close CSV exporter workflow instance writer", e);
    }

    try {
      jobCsvWriter.close();
    } catch (Exception e) {
      log.warn("Failed to close CSV exporter job writer", e);
    }
  }

  private Path createOutput(CsvExporterConfiguration configuration) throws IOException {
    Path path = Paths.get(configuration.getOutput()).toAbsolutePath();
    return Files.createDirectories(path);
  }
}
