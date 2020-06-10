package mystudy.Fragment;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import mystudy.Colors.Colors;
import mystudy.Components.CardPanel;
import mystudy.Components.MyTable;
import mystudy.Components.MyTextField;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Components.ComboBox.ComboBoxRenderer;
import mystudy.Components.Table.ResultListModel;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Class;
import mystudy.POJOs.Course;
import mystudy.POJOs.Registration;
import mystudy.POJOs.RegistrationOfStudent;
import mystudy.POJOs.Result;
import mystudy.POJOs.Student;
import mystudy.POJOs.TimeTable;
import mystudy.POJOs.TimeTablePK;

public class ResultFragment extends JPanel implements Fragment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Class selectedClass = null;
    private Course selectedCourse = null;
    private Student addingStudent = null;
    private int selectedStudentIndex = -1;
    private boolean isUpdating = false;
    private MyTable resultTable;

    public ResultFragment() {
        setBackground(Colors.getBackground());
    }

    @Override
    public void build() {
        removeAll();
        // Khởi tạo trước các session dữ liệu set border cho fragment
        Session session = DatabaseService.getInstance().getSession();
        List<Result> results = new ArrayList<>();
        Vector<Class> classes = new Vector<>();
        Vector<Course> courses = new Vector<>();
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Results");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        // Lấy danh sách lớp
        classes.addAll(session.createQuery("from Class c ORDER BY c.className", Class.class).list());

        ResultListModel model = new ResultListModel(results);

        JPanel topPanel = new JPanel(new BorderLayout(30, 0));
        add(topPanel, BorderLayout.PAGE_START);
        topPanel.setBackground(topPanel.getParent().getBackground());

        // Tạo Combobox chọn lớp
        JComboBox<Class> classesComboBox = new JComboBox<Class>(classes);
        topPanel.add(classesComboBox, BorderLayout.LINE_START);
        ComboBoxRenderer classesComboBoxRenderer = new ComboBoxRenderer();
        classesComboBox.setRenderer(classesComboBoxRenderer);
        classesComboBox.setBackground(Colors.getPrimary());
        classesComboBox.setForeground(Colors.getTextColor());
        classesComboBox.setPreferredSize(new Dimension(200, 50));
        classesComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        if (selectedClass == null) {
            selectedClass = (Class) classesComboBox.getSelectedItem();
        }
        // Lấy danh sách môn
        courses.addAll(fetchCourseFromClass(session, selectedClass));
        classesComboBox.setSelectedItem(selectedClass);
        // Tạo Combobox chọn chọn môn
        JComboBox<Course> coursesComboBox = new JComboBox<Course>(courses);
        topPanel.add(coursesComboBox, BorderLayout.CENTER);
        ComboBoxRenderer coursesComboBoxRenderer = new ComboBoxRenderer();
        coursesComboBox.setRenderer(coursesComboBoxRenderer);
        coursesComboBox.setBackground(Colors.getPrimary());
        coursesComboBox.setForeground(Colors.getTextColor());
        coursesComboBox.setPreferredSize(new Dimension(500, 50));
        coursesComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        if (selectedCourse == null) {
            selectedCourse = (Course) coursesComboBox.getSelectedItem();
        }

        results.addAll(fetchResultFromCourse(session, selectedCourse));
        coursesComboBox.setSelectedItem(selectedCourse);

        // Tạo nút import kết quả học tập
        RoundedButton importButton = new RoundedButton("Import results", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    try {
                        FileReader fileReader = new FileReader(file);
                        BufferedReader reader = new BufferedReader(fileReader);
                        String line = reader.readLine().split(",")[0].replace("\uFEFF", "");
                        String[] args = line.split("[-–]");
                        System.out.println(args.length);
                        System.out.println(args[0]);
                        if (args.length != 2) {
                            JOptionPane.showMessageDialog(null, "File content isn't have a correct format",
                                    "Invalid file", JOptionPane.ERROR_MESSAGE);
                            reader.close();
                            return;
                        }
                        Class importingClass = session.find(Class.class, args[0]);
                        if (importingClass == null) {
                            JOptionPane.showMessageDialog(null, "Class dosen't existed", "Class not found",
                                    JOptionPane.ERROR_MESSAGE);
                            reader.close();
                            return;
                        }
                        Course importingCourse = session.find(Course.class, args[1]);
                        if (importingCourse == null) {
                            JOptionPane.showMessageDialog(null, "Course dosen't existed", "Course not found",
                                    JOptionPane.ERROR_MESSAGE);
                            reader.close();
                            return;
                        }
                        TimeTablePK timeTablePK = new TimeTablePK(importingClass, importingCourse);
                        TimeTable timeTable = session.find(TimeTable.class, timeTablePK);
                        if (timeTable == null) {
                            JOptionPane.showMessageDialog(null,
                                    "Course dosen't existed in " + importingClass.getClassName(), "Course not found",
                                    JOptionPane.ERROR_MESSAGE);
                            reader.close();
                            return;
                        }

                        // Bỏ dòng title
                        line = reader.readLine();
                        List<Student> unregisteredStudents = new ArrayList<>();
                        while ((line = reader.readLine()) != null) {
                            args = line.split(",");
                            Student importingStudent = session.find(Student.class, args[1]);
                            RegistrationOfStudent registrationOfStudent = new RegistrationOfStudent(importingStudent,
                                    importingCourse);
                            Registration registration = session.find(Registration.class, registrationOfStudent);

                            if (registration == null) {
                                unregisteredStudents.add(importingStudent);
                            } else {
                                Result result = new Result(registrationOfStudent, Float.parseFloat(args[3]),
                                        Float.parseFloat(args[4]), Float.parseFloat(args[5]),
                                        Float.parseFloat(args[6]));
                                Transaction transaction = null;
                                try {
                                    transaction = session.beginTransaction();
                                    session.save(result);
                                    transaction.commit();

                                } catch (HibernateException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        if (unregisteredStudents.size() > 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Student unregisteredStudent : unregisteredStudents) {
                                stringBuilder.append(unregisteredStudent.getStudentId());
                                stringBuilder.append(" - ");
                                stringBuilder.append(unregisteredStudent.getName());
                                stringBuilder.append(" doesn't registered in ");
                                stringBuilder.append(importingClass.getClassName());
                                stringBuilder.append(" - ");
                                stringBuilder.append(importingCourse.getCourseId());
                                stringBuilder.append("\n");
                            }
                            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Student not found",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        };
        importButton.setPreferredSize(new Dimension(200, 50));
        topPanel.add(importButton, BorderLayout.LINE_END);

        RoundedButton updateButton = new RoundedButton("Edit", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isUpdating = true;
            }
        };
        updateButton.setPreferredSize(new Dimension(200, 50));
        updateButton.setEnabled(false);
        updateButton.setToolTipText("Edit inline");

        classesComboBox.addActionListener(new ActionListener() {
            // Xử lý sự kiện chọn lớp
            @Override
            public void actionPerformed(ActionEvent e) {
                // lấy danh sách môn học hiển thị lên
                selectedClass = (Class) classesComboBox.getSelectedItem();
                // Lấy danh sách môn học trong lớp đã chọn
                coursesComboBox.setSelectedItem(null);
                selectedCourse = null;
                courses.clear();
                courses.addAll(fetchCourseFromClass(session, selectedClass));
                coursesComboBox.updateUI();
            }

        });

        coursesComboBox.addActionListener(new ActionListener() {
            // Xử lý sự kiện chọn môn học
            @Override
            public void actionPerformed(ActionEvent e) {
                // lấy danh sách kết quả học tập của sinh viên tham gia môn học hiển thị lên
                results.clear();
                selectedCourse = (Course) coursesComboBox.getSelectedItem();
                if (selectedCourse != null) {
                    results.addAll(fetchResultFromCourse(session, selectedCourse));
                }
                model.fireTableDataChanged();
            }

        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        add(bottomPanel, BorderLayout.PAGE_END);

        bottomPanel.add(updateButton);
        bottomPanel.setBackground(bottomPanel.getParent().getBackground());

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());

        // Tạo bảng hiển thị kết quả học tập của sinh viên
        resultTable = new MyTable(model) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column > 2 && column < 7 && getSelectedRow() == row && isUpdating)
                    return true;
                return false;
            }

            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) {
                JTextComponent c = (JTextComponent) super.prepareEditor(editor, row, column);
                c.setBackground(Colors.getBackground());
                c.setForeground(Colors.getTextColor());
                c.setCaretColor(Colors.getTextColor());
                c.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                return c;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                updateButton.setEnabled(true);
                selectedStudentIndex = getSelectedRow();
                isUpdating = false;
            }
        };
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        textRenderer.setBorder(new EmptyBorder(10, 10, 10, 10));
        resultTable.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        var noColumn = resultTable.getColumn("No.");
        noColumn.setCellRenderer(textRenderer);
        noColumn.setMaxWidth(70);
        resultTable.getColumn("Student ID").setCellRenderer(textRenderer);
        resultTable.getColumn("Full name").setCellRenderer(textRenderer);
        resultTable.getColumn("Mid-term").setCellRenderer(textRenderer);
        resultTable.getColumn("Final exam").setCellRenderer(textRenderer);
        resultTable.getColumn("Other grade").setCellRenderer(textRenderer);
        resultTable.getColumn("Total grade").setCellRenderer(textRenderer);
        resultTable.getColumn("Result").setCellRenderer(new DefaultTableCellRenderer() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String result = (String) value;
                if (result.equals("Passed")) {
                    c.setForeground(Color.green);
                } else {
                    c.setForeground(Color.red);
                }
                return c;
            }
        });
        resultTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        centerJPanel.add(scrollPane);
        JPanel statisticPanel = new JPanel();
        statisticPanel.setBorder(new CompoundBorder(new RoundedBorder(Colors.getTextColor(), 2, true, 50),
                new EmptyBorder(10, 10, 10, 10)));
        centerJPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        centerJPanel.add(statisticPanel);
        statisticPanel.setBackground(statisticPanel.getParent().getBackground());
        int passedStudent = 0;
        int failedStudent = 0;
        // Thống kê số liệu
        for (int i = 0; i < results.size(); i++) {
            if (((String) model.getValueAt(i, 7)).equals("Passed")) {
                passedStudent++;
            } else {
                failedStudent++;
            }
        }
        JLabel passedLabel = new JLabel("Passed: " + passedStudent);
        statisticPanel.add(passedLabel);
        statisticPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        passedLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        passedLabel.setForeground(Colors.getTextColor());

        statisticPanel.add(Box.createRigidArea(new Dimension(50, 50)));

        JLabel passedRateLabel = new JLabel(
                "Passed rate: " + (results.size() == 0 ? "100" : passedStudent / results.size() * 100) + "%");
        statisticPanel.add(passedRateLabel);
        passedRateLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        passedRateLabel.setForeground(Colors.getTextColor());

        statisticPanel.add(Box.createRigidArea(new Dimension(50, 50)));

        JLabel failedLabel = new JLabel("Failed: " + failedStudent);
        statisticPanel.add(failedLabel);
        failedLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        failedLabel.setForeground(Colors.getTextColor());

        statisticPanel.add(Box.createRigidArea(new Dimension(50, 50)));

        JLabel failedRateLabel = new JLabel(
                "Failed rate: " + (results.size() == 0 ? "100" : failedStudent / results.size() * 100) + "%");
        statisticPanel.add(failedRateLabel);
        failedRateLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        failedRateLabel.setForeground(Colors.getTextColor());

        resultTable.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int passedStudent = 0;
                int failedStudent = 0;
                for (int i = 0; i < results.size(); i++) {
                    if (((String) model.getValueAt(i, 7)).equals("Passed")) {
                        passedStudent++;
                    } else {
                        failedStudent++;
                    }
                }
                failedLabel.setText("Failed: " + failedStudent);
                passedLabel.setText("Passed: " + passedStudent);
                failedRateLabel.setText("Failed rate: "
                        + (results.size() == 0 ? "100" : failedStudent * 1.0 / results.size() * 100) + "%");
                passedRateLabel.setText("Passed rate: "
                        + (results.size() == 0 ? "100" : passedStudent * 1.0 / results.size() * 100) + "%");
            }
        });
        validate();
        repaint();
    }

    private List<Course> fetchCourseFromClass(Session session, Class fromClass) {
        List<Course> courses = new ArrayList<>();
        Query<Course> query = session.createQuery(
                "select c from TimeTable t inner join Course c on c.courseId = t.courseInClass.course where t.courseInClass.className = :class ORDER BY c.courseId",
                Course.class);
        query.setParameter("class", fromClass);
        courses.addAll(query.list());
        session.clear();
        return courses;
    }

    private List<Result> fetchResultFromCourse(Session session, Course fromCourse) {
        List<Result> results = new ArrayList<>();
        Query<Result> query = session.createQuery("select r from Result r where r.registration.course = :Course",
                Result.class);
        query.setParameter("Course", fromCourse);

        results.addAll(query.list());
        session.clear();
        return results;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}