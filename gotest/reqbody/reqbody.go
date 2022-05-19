package reqbody

import (
	"encoding/json"
	"log"
)

type TimeSeries struct {
    Timestamp	string	`json:"timestamp"`
    Property	string	`json:"property"`
    Unit	string	`json:"unit"`
	Value	int	`json:"value"`
}

func (timeSeries TimeSeries) GetTSJson() string {
	ts_json, err := json.Marshal(timeSeries)

	if err != nil {
        log.Fatal(err)
    }
	return string(ts_json)
}

