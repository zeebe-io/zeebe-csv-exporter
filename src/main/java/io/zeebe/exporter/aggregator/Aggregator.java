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

import io.zeebe.exporter.RecordMissingException;
import io.zeebe.exporter.record.CsvRecord;
import io.zeebe.exporter.writer.CsvWriter;
import io.zeebe.protocol.record.Record;
import io.zeebe.protocol.record.RecordValue;
import io.zeebe.protocol.record.intent.Intent;
import java.util.HashMap;
import java.util.Map;

public abstract class Aggregator<T extends CsvRecord, R extends RecordValue>
    implements AutoCloseable {

  private final Map<Long, T> records = new HashMap<>();

  private final CsvWriter csvWriter;
  private final Intent createIntent;
  private final Intent completeIntent;
  private final Intent terminateIntent;

  public Aggregator(
      final CsvWriter csvWriter,
      final Intent createIntent,
      final Intent completeIntent,
      final Intent terminateIntent) {
    this.csvWriter = csvWriter;
    this.createIntent = createIntent;
    this.completeIntent = completeIntent;
    this.terminateIntent = terminateIntent;
  }

  public void process(final Record<R> record) {
    final Intent intent = record.getIntent();
    if (createIntent == intent) {
      cache(record);
    } else if (completeIntent == intent) {
      update(record, true);
    } else if (terminateIntent != null && terminateIntent == intent) {
      update(record, false);
    }
  }

  abstract T create(R recordValue);

  private void cache(final Record<R> record) {
    final long key = record.getKey();

    final T csvRecord = create(record.getValue());

    csvRecord
        .setKey(key)
        .setStartPosition(record.getPosition())
        .setCreated(record.getTimestamp())
        .setPartition(record.getPartitionId());

    records.put(key, csvRecord);
  }

  private void update(final Record<R> record, final boolean completed) {
    final long key = record.getKey();

    final T csvRecord = records.remove(key);

    if (csvRecord == null) {
      throw new RecordMissingException(record.getValueType(), key);
    }

    csvRecord.setEndPosition(record.getPosition());
    csvRecord.setEnded(record.getTimestamp());
    csvRecord.setCompleted(completed);

    try {
      csvWriter.write(csvRecord);
    } catch (Exception e) {
      // return to cache to try again
      records.put(key, csvRecord);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws Exception {
    csvWriter.close();
  }
}
