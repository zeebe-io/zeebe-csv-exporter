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
package io.zeebe.exporter.writer;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.zeebe.exporter.record.CsvRecord;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvFileWriter implements CsvWriter {

  private final Path output;
  private final String prefix;
  private final ObjectWriter objectWriter;

  private SequenceWriter sequenceWriter;

  public CsvFileWriter(Path output, String prefix, Class schemaClass) {
    this.output = output;
    this.prefix = prefix;
    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = mapper.schemaFor(schemaClass).withHeader();
    objectWriter = mapper.writer(schema);
  }

  @Override
  public void write(CsvRecord record) throws IOException {
    if (sequenceWriter == null) {
      Path path = filePath(output, prefix, record.getPartition());
      OpenOption openOption =
          Files.exists(path) ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.CREATE_NEW;
      BufferedWriter bufferedWriter =
          Files.newBufferedWriter(path, StandardCharsets.UTF_8, openOption);
      sequenceWriter = objectWriter.writeValues(bufferedWriter);
    }

    sequenceWriter.write(record);
  }

  @Override
  public void close() throws Exception {
    if (sequenceWriter != null) {
      sequenceWriter.close();
    }
  }

  public static Path filePath(Path base, String prefix, int partition) {
    return base.resolve(prefix + "-" + partition + ".csv");
  }
}
