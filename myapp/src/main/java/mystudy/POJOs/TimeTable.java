package mystudy.POJOs;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "timetable")
public class TimeTable {

    @EmbeddedId
    private TimeTablePK courseInClass;

    public TimeTable() {
    }

    public TimeTable(TimeTablePK courseInClass) {
        this.courseInClass = courseInClass;
    }

    public TimeTablePK getCourseInClass() {
        return this.courseInClass;
    }

    public void setCourseInClass(TimeTablePK courseInClass) {
        this.courseInClass = courseInClass;

    }

}
