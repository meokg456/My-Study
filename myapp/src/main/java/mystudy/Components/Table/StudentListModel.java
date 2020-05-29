package mystudy.Components.Table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import mystudy.POJOs.Student;

public class StudentListModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String[] columnNames = { "No.", "Student ID", "Full name", "Gender", "Personal ID" };

    private List<Student> list;

    public StudentListModel(List<Student> list) {
        this.list = list;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return student.getStudentId();
            case 2:
                return student.getName();
            case 3:
                return student.getGender();
            case 4:
                return student.getPersonalId();

        }
        return student;
    }

}