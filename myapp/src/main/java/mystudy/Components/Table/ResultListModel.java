package mystudy.Components.Table;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mystudy.Connector.DatabaseService;
import mystudy.POJOs.Result;
import mystudy.POJOs.Student;

public class ResultListModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String[] columnNames = { "No.", "Student ID", "Full name", "Mid-term", "Final exam", "Other grade",
            "Total grade", "Result" };

    private List<Result> results;

    public ResultListModel(List<Result> results) {
        this.results = results;
    }

    @Override
    public int getRowCount() {
        return results.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Float newValue = Float.parseFloat((String) aValue);
        if (newValue < 0.0 || newValue > 10.0) {
            JOptionPane.showMessageDialog(null, "Grade must be greater than 0.0 and lower than 10.0", "Invalid grade",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Result result = results.get(rowIndex);
        switch (columnIndex) {
            case 3: {
                result.setMidTermGrade(newValue);
                break;
            }
            case 4: {
                result.setFinalExamGrade(newValue);
                break;
            }
            case 5: {
                result.setOtherGrade(newValue);
                break;
            }
            case 6: {
                result.setTotalGrade(newValue);
                break;
            }

        }
        Session session = DatabaseService.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(result);
            transaction.commit();
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (HibernateException ex) {
            ex.printStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Result result = results.get(rowIndex);
        Student student = result.getRegistration().getStudent();
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return student.getStudentId();
            case 2:
                return student.getName();
            case 3:
                return result.getMidTermGrade();
            case 4:
                return result.getFinalExamGrade();
            case 5:
                return result.getOtherGrade();
            case 6:
                return result.getTotalGrade();
            case 7:
                return result.getTotalGrade() < 5.0 ? "Failed" : "Passed";
        }
        return result;
    }

}