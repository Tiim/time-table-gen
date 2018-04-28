package ch.scbirs.timetablegen;

import ch.scbirs.timetablegen.util.Lang;

import java.time.LocalTime;

public class Model {

    public static final String[] DAYS = Lang.translate("day.All").split(",");
    private final TimeRange[] data = new TimeRange[DAYS.length];
    private String name = "name.default";

    public Model() {
        for (int i = 0; i < data.length; i++) {
            data[i] = new TimeRange(LocalTime.of(18, 0), LocalTime.of(20, 0), false);
        }
    }

    public void setRange(TimeRange tr, int day) {
        data[day] = tr;
    }

    public TimeRange getRange(int day) {
        return data[day];
    }

    public int getWeekdayMaxHour() {
        int hour = -1;
        for (int i = 0; i <= 4; i++) {
            if (data[i].isEnabled()) {
                LocalTime end = data[i].getEnd();
                hour = Math.max(end.getHour() + (end.getMinute() > 0 ? 1 : 0), hour);
            }
        }
        hour = hour == -1 ? 20 : hour;
        return hour;
    }

    public int getWeekendMaxHour() {
        if (!data[5].isEnabled() && !data[6].isEnabled()) {
            return getWeekdayMaxHour();
        } else if (!data[5].isEnabled()) {
            return data[6].getEnd().getHour() + (data[6].getEnd().getMinute() > 0 ? 1 : 0);
        } else if (!data[6].isEnabled()) {
            return data[5].getEnd().getHour() + (data[5].getEnd().getMinute() > 0 ? 1 : 0);
        }
        return Math.max(
                data[5].getEnd().getHour() + (data[5].getEnd().getMinute() > 0 ? 1 : 0),
                data[6].getEnd().getHour() + (data[6].getEnd().getMinute() > 0 ? 1 : 0)
        );
    }

    public int getWeekdayMinHour() {
        int hour = 100;
        for (int i = 0; i <= 4; i++) {
            if (data[i].isEnabled()) {
                LocalTime start = data[i].getStart();
                hour = Math.min(start.getHour(), hour);
            }
        }
        hour = hour == 100 ? 18 : hour;
        return hour;
    }

    public int getWeekendMinHour() {
        if (!data[5].isEnabled() && !data[6].isEnabled()) {
            return getWeekdayMinHour();
        } else if (!data[5].isEnabled()) {
            return data[6].getStart().getHour();
        } else if (!data[6].isEnabled()) {
            return data[5].getStart().getHour();
        }
        return Math.min(
                data[5].getStart().getHour(),
                data[6].getStart().getHour()
        );
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class TimeRange {
        private final boolean enabled;
        private final LocalTime start;
        private final LocalTime end;

        public TimeRange(LocalTime start, LocalTime end, boolean enabled) {
            this.start = start;
            this.end = end;
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public LocalTime getEnd() {
            return end;
        }

        public LocalTime getStart() {
            return start;
        }
    }
}
