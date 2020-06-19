package mystudy.Components.Table;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mystudy.Connector.DatabaseService;
import mystudy.Enum.RequestStatus;
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(request.getRequestTime());
            case 1:
                return request.getRegistration().getStudent().getStudentId();
            case 2:
                return request.getRegistration().getStudent().getName();
            case 3:
                return request.getRegistration().getCourse().getCourseName();
            case 4:
                return request.getStatus();
        }
        return request;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 4) {
            RequestStatus status = (RequestStatus) aValue;
            requests.get(rowIndex).setStatus(status);
            Session session = DatabaseService.getInstance().getSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(requests.get(rowIndex));
                transaction.commit();
            } catch (HibernateException ex) {
                transaction.rollback();
                ex.printStackTrace();
            }
        }

    }
}