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

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.zeebe.exporter.TestAggregator.JobTestAggregator;
import io.zeebe.exporter.TestAggregator.WorkflowInstanceTestAggregator;
import io.zeebe.exporter.record.CsvRecord;
import io.zeebe.exporter.record.JobCsvRecord;
import io.zeebe.exporter.record.Record;
import io.zeebe.exporter.record.WorkflowInstanceCsvRecord;
import io.zeebe.exporter.record.value.JobRecordValue;
import io.zeebe.exporter.record.value.WorkflowInstanceRecordValue;
import io.zeebe.exporter.writer.CsvFileWriter;
import io.zeebe.protocol.clientapi.ValueType;
import io.zeebe.test.exporter.ExporterIntegrationRule;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CsvExporterIT {

  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private CsvExporterConfiguration configuration;
  private ExporterIntegrationRule integrationRule;

  @Before
  public void setUp() {
    configuration =
        new CsvExporterConfiguration().setOutput(temporaryFolder.getRoot().getAbsolutePath());
    integrationRule =
        new ExporterIntegrationRule().configure("csv-exporter", CsvExporter.class, configuration);
    integrationRule.start();
  }

  @After
  public void tearDown() {
    integrationRule.stop();
  }

  @Test
  public void shouldExportRecords() {
    // when
    integrationRule.performSampleWorkload();

    // then
    assertRecords();
  }

  private void assertRecords() {
    WorkflowInstanceTestAggregator workflowInstanceAggregator =
        new WorkflowInstanceTestAggregator();
    JobTestAggregator jobAggregator = new JobTestAggregator();

    integrationRule.visitExportedRecords(
        record -> {
          if (ValueType.WORKFLOW_INSTANCE == record.getMetadata().getValueType()) {
            workflowInstanceAggregator.process((Record<WorkflowInstanceRecordValue>) record);
          } else if (ValueType.JOB == record.getMetadata().getValueType()) {
            jobAggregator.process((Record<JobRecordValue>) record);
          }
        });

    for (List<WorkflowInstanceCsvRecord> records : workflowInstanceAggregator.getRecords()) {
      if (!records.isEmpty()) {
        List<WorkflowInstanceCsvRecord> expected =
            readExpectedRecords(
                WorkflowInstanceCsvRecord.class,
                CsvExporter.WORKFLOW_INSTANCE_PREFIX,
                records.get(0).getPartition());
        assertThat(records).containsOnlyElementsOf(expected);
      }
    }

    for (List<JobCsvRecord> records : jobAggregator.getRecords()) {
      if (!records.isEmpty()) {
        List<JobCsvRecord> expected =
            readExpectedRecords(
                JobCsvRecord.class, CsvExporter.JOB_PREFIX, records.get(0).getPartition());
        assertThat(records).containsOnlyElementsOf(expected);
      }
    }
  }

  private <T extends CsvRecord> List<T> readExpectedRecords(
      Class<T> csvRecordClass, String prefix, int partition) {
    Path path =
        CsvFileWriter.filePath(temporaryFolder.getRoot().toPath(), prefix, partition)
            .toAbsolutePath();

    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = CsvSchema.builder().setUseHeader(true).build();

    try {
      MappingIterator<T> objectMappingIterator =
          mapper.readerFor(csvRecordClass).with(schema).readValues(path.toFile());
      return objectMappingIterator.readAll();
    } catch (IOException e) {
      throw new AssertionError("Failed to read file: " + path.toString(), e);
    }
  }
}
