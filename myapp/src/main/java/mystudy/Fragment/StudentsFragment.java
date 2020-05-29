package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.Session;
import org.hibernate.query.Query;

import mystudy.Colors.Colors;
import mystudy.Components.CardPanel;
import mystudy.Components.MyTable;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Components.ComboBox.ComboBoxRenderer;
import mystudy.Components.Table.StudentListModel;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Class;
import mystudy.POJOs.Student;

public class StudentsFragment extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public StudentsFragment() {
        Session session = DatabaseService.getInstance().getSession();
        List<Student> students = new ArrayList<>();
        Vector<Class> classes = new Vector<>();
        classes.addAll(session.createQuery("from Class c ORDER BY c.className", Class.class).list());

        StudentListModel model = new StudentListModel(students);

        setBackground(Colors.getBackground());
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Students");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 50),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        JPanel topPanel = new JPanel(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);
        topPanel.setBackground(topPanel.getParent().getBackground());

        JComboBox<Class> classesComboBox = new JComboBox<Class>(classes);
        topPanel.add(classesComboBox, BorderLayout.LINE_START);
        ComboBoxRenderer comboBoxRenderer = new ComboBoxRenderer();
        classesComboBox.setRenderer(comboBoxRenderer);
        classesComboBox.setBackground(Colors.getPrimary());
        classesComboBox.setForeground(Colors.getTextColor());
        classesComboBox.setPreferredSize(new Dimension(200, 50));
        classesComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));

        Class selectedClass = (Class) classesComboBox.getSelectedItem();
        Query<Student> query = session.createQuery("select s from Student s where s.className = :class", Student.class);
        query.setParameter("class", selectedClass);
        students.addAll(query.list());
        model.fireTableDataChanged();

        classesComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Class selectedClass = (Class) classesComboBox.getSelectedItem();
                Query<Student> query = session.createQuery("select s from Student s where s.className = :class",
                        Student.class);
                students.clear();
                query.setParameter("class", selectedClass);
                students.addAll(query.list());
                model.fireTableDataChanged();
            }

        });

        RoundedButton importButton = new RoundedButton("Import students", 50, 24);
        topPanel.add(importButton, BorderLayout.LINE_END);
        importButton.setPreferredSize(new Dimension(200, 50));
        RoundedButton resetButton = new RoundedButton("Add student", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        };
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottomPanel, BorderLayout.PAGE_END);
        resetButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(resetButton);

        bottomPanel.setBackground(bottomPanel.getParent().getBackground());
        // Hiển thị thông tin giáo vụ

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());

        centerJPanel.add(Box.createVerticalStrut(50));

        // Students account table
        MyTable studentTable = new MyTable(model) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        };
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        textRenderer.setBorder(new EmptyBorder(10, 10, 10, 10));
        studentTable.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        studentTable.getColumn("Student ID").setCellRenderer(textRenderer);
        studentTable.getColumn("Full name").setCellRenderer(textRenderer);
        studentTable.getColumn("Gender").setCellRenderer(textRenderer);
        studentTable.getColumn("Personal ID").setCellRenderer(textRenderer);
        studentTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        centerJPanel.add(scrollPane);

    }

}