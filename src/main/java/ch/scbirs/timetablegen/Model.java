package ch.scbirs.timetablegen;

import java.time.LocalTime;

public class Model {

    static class TimeRange {
        private final LocalTime start;
        private final LocalTime end;

        TimeRange(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalTime getEnd() {
            return end;
        }

        public LocalTime getStart() {
            return start;
        }
    }

    public static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private final TimeRange[] data = new TimeRange[DAYS.length];

    public Model() {
        for (int i = 0; i < data.length; i++) {
            data[i] = new TimeRange(LocalTime.of(18, 0), LocalTime.of(20, 0));
        }
    }

    public void setRange(TimeRange tr, int day) {
        data[day] = tr;
    }

    public TimeRange getRange(int day) {
        return data[day];
    }

}
