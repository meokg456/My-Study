package mystudy.Components.Table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import mystudy.POJOs.ReexamineRequest;

public class ReexamineRequestModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String[] columnNames = { "Date", "Student ID", "Full name", "Course name", "Status" };

    private List<ReexamineRequest> requests = new ArrayList<>();

    public ReexamineRequestModel(List<ReexamineRequest> requests) {
        this.requests = requests;
    }

    @Override
    public int getRowCount() {
        return requests.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReexamineRequest request = requests.get(rowIndex);
        switch (columnIndex) {
            case 0:
                request.getRequestTime().toLocalDate().toString();
                break;
            case 1:
                request.getRegistration().getStudent().getStudentId();
                break;
            case 2:
                request.getRegistration().getStudent().getName();
                break;
            case 3:
                request.getRegistration().getCourse().getCourseName();
                break;
            case 4:
                request.getStatus();
                break;
        }
        return request;
    }

}