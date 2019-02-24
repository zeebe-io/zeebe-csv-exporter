# Zeebe CSV Exporter

A simple aggregating [CSV] [exporter] for [Zeebe].

_**Note: This is a work in progress; you're welcome to contribute code or ideas, but no guarantees are made about the exporter itself.
Use at your own risks.**_

## Usage

1. Download the [latest release] from GitHub and copy it into the Zeebe `lib`
folder
2. Add the exporter configuration to the `conf/zeebe.cfg.toml` to enable the
   exporter
   ```
   [[exporters]]
   id = "csv"
   className = "io.zeebe.exporter.CsvExporter"

   [exporters.args]
   # output path to write csv files
   output = "csv/"
   ```

## Example

The exporter writes a file per partition for workflow instances and jobs.

With the exporter configuration shown above and a broker with 3 partitions the
following files will be created.

```
csv/
├── job-0.csv
├── job-1.csv
├── job-2.csv
├── workflow-instance-0.csv
├── workflow-instance-1.csv
└── workflow-instance-2.csv
```

The content of the files contains a header line with the field names and then
a line for every finished entity.

```
$ head -n10 csv/workflow-instance-0.csv
bpmnElementType,bpmnProcessId,completed,created,duration,elementId,endPosition,ended,flowScopeKey,key,partition,startPosition,version,workflowInstanceKey,workflowKey
START_EVENT,process,true,1551051793053,1,StartEvent_1,4295035184,1551051793054,155,156,0,4295034440,1,155,1
START_EVENT,process,true,1551051793073,1,StartEvent_1,4295038368,1551051793074,160,161,0,4295037624,1,160,1
START_EVENT,process,true,1551051793100,1,StartEvent_1,4295043072,1551051793101,166,167,0,4295042328,1,166,1
SERVICE_TASK,process,true,1551051793056,62,ServiceTask_0kt6c5i,4295046760,1551051793118,155,158,0,4295035704,1,155,1
SERVICE_TASK,process,true,1551051793074,50,ServiceTask_0kt6c5i,4295047024,1551051793124,160,163,0,4295038888,1,160,1
SERVICE_TASK,process,true,1551051793103,44,ServiceTask_0kt6c5i,4295051008,1551051793147,166,169,0,4295043592,1,166,1
END_EVENT,process,true,1551051793141,7,EndEvent_0crvjrk,4295051272,1551051793148,155,174,0,4295048872,1,155,1
END_EVENT,process,true,1551051793141,8,EndEvent_0crvjrk,4295051528,1551051793149,160,175,0,4295049128,1,160,1
PROCESS,process,true,1551051793046,108,process,4295053168,1551051793154,-1,155,0,4295033960,1,155,1
```

```
$ head -n10 csv/job-0.csv
bpmnProcessId,completed,created,duration,elementId,elementInstanceKey,endPosition,ended,key,partition,startPosition,type,"workflowDefinitionVersion",workflowInstanceKey,workflowKey
process,true,1551051793061,50,ServiceTask_0kt6c5i,158,4295045384,1551051793111,159,0,4295036576,task,1,155,1
process,true,1551051793075,39,ServiceTask_0kt6c5i,163,4295045728,1551051793114,164,0,4295039760,task,1,160,1
process,true,1551051793103,40,ServiceTask_0kt6c5i,169,4295049384,1551051793143,170,0,4295044464,task,1,166,1
process,true,1551051793174,4,ServiceTask_0kt6c5i,185,4295060624,1551051793178,186,0,4295058872,task,1,181,1
process,true,1551051793210,11,ServiceTask_0kt6c5i,199,4295070088,1551051793221,201,0,4295068512,task,1,196,1
process,true,1551051793320,19,ServiceTask_0kt6c5i,225,4295090616,1551051793339,226,0,4295085864,task,1,222,1
process,true,1551051793276,65,ServiceTask_0kt6c5i,215,4295092312,1551051793341,216,0,4295079496,task,1,212,1
process,true,1551051793301,40,ServiceTask_0kt6c5i,220,4295092920,1551051793341,221,0,4295082680,task,1,217,1
process,true,1551051793248,96,ServiceTask_0kt6c5i,210,4295097112,1551051793344,211,0,4295076312,task,1,207,1
```

## Code of Conduct

This project adheres to the Contributor Covenant [Code of
Conduct](/CODE_OF_CONDUCT.md). By participating, you are expected to uphold
this code. Please report unacceptable behavior to code-of-conduct@zeebe.io.

## License

[Apache License, Version 2.0](/LICENSE)

[Zeebe]: https://zeebe.io
[exporter]: https://docs.zeebe.io/basics/exporters.html
[CSV]: https://en.wikipedia.org/wiki/Comma-separated_values
[latest release]: https://github.com/zeebe-io/zeebe-csv-exporter/releases
