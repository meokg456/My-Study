package mystudy.Components.Table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import mystudy.POJOs.User;

public class UserAccountListModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String[] columnName = { "Username", "Password" };
    private List<User> users;

    public User getUser(int index) {
        return users.get(index);
    }

    @Override
    public String getColumnName(int column) {
        return columnName[column];
    }

    public UserAccountListModel(List<User> list) {
        users = list;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columnName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);

        switch (columnIndex) {
            case 0: {
                return user.getUsername();
            }
            case 1: {
                return String.format("%" + user.getPassword().length() + "s", "").replace(' ', '*');
            }
        }
        return user;
    }

}