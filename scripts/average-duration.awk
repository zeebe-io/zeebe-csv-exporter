#!/usr/bin/awk -f

BEGIN {
    FS=","
}

NR == 1 {
    for(i = 1; i <= NF; i++) {
        if ($i == "elementId") elementId = i
        if ($i == "duration") duration = i
    }
}

NR > 1 {
    time[$elementId] += $duration
    count[$elementId] += 1
}

END {
    for (key in time) {
        print key, count[key], time[key], time[key] / count[key]
    }
}
