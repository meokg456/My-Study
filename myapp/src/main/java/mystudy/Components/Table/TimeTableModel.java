package mystudy.Components.Table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import mystudy.POJOs.Course;

public class TimeTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String[] columnNames = { "No.", "Course ID", "Course's name", "Room" };

    private List<Course> list;

    public TimeTableModel(List<Course> list) {
        this.list = list;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Course course = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return course.getCourseId();
            case 2:
                return course.getCourseName();
            case 3:
                return course.getRoomId();
        }
        return course;
    }

}