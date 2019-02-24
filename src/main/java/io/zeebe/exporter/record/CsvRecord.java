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

import java.time.Instant;
import java.util.Objects;

public abstract class CsvRecord {

  long key;
  int partition;

  long startPosition;
  long endPosition;

  long created;
  long ended;
  long duration;

  boolean completed;

  public long getKey() {
    return key;
  }

  public CsvRecord setKey(final long key) {
    this.key = key;
    return this;
  }

  public int getPartition() {
    return partition;
  }

  public CsvRecord setPartition(final int partition) {
    this.partition = partition;
    return this;
  }

  public long getStartPosition() {
    return startPosition;
  }

  public CsvRecord setStartPosition(final long startPosition) {
    this.startPosition = startPosition;
    return this;
  }

  public long getEndPosition() {
    return endPosition;
  }

  public CsvRecord setEndPosition(final long endPosition) {
    this.endPosition = endPosition;
    return this;
  }

  public long getCreated() {
    return created;
  }

  public CsvRecord setCreated(final Instant created) {
    return setCreated(created.toEpochMilli());
  }

  public CsvRecord setCreated(final long created) {
    this.created = created;
    return this;
  }

  public long getEnded() {
    return ended;
  }

  public CsvRecord setEnded(final Instant ended) {
    return setEnded(ended.toEpochMilli());
  }

  public CsvRecord setEnded(final long ended) {
    this.ended = ended;
    return setDuration(ended - created);
  }

  public long getDuration() {
    return duration;
  }

  public CsvRecord setDuration(long duration) {
    this.duration = duration;
    return this;
  }

  public boolean isCompleted() {
    return completed;
  }

  public CsvRecord setCompleted(final boolean completed) {
    this.completed = completed;
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
    CsvRecord csvRecord = (CsvRecord) o;
    return key == csvRecord.key
        && partition == csvRecord.partition
        && startPosition == csvRecord.startPosition
        && endPosition == csvRecord.endPosition
        && created == csvRecord.created
        && ended == csvRecord.ended
        && duration == csvRecord.duration
        && completed == csvRecord.completed;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        key, partition, startPosition, endPosition, created, ended, duration, completed);
  }
}
