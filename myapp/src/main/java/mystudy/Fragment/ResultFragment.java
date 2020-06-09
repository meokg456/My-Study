package mystudy.Fragment;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                if (column > 2 && getSelectedRow() == row && isUpdating)
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
        resultTable.getColumn("No.").setCellRenderer(textRenderer);
        resultTable.getColumn("Student ID").setCellRenderer(textRenderer);
        resultTable.getColumn("Full name").setCellRenderer(textRenderer);
        resultTable.getColumn("Mid-term").setCellRenderer(textRenderer);
        resultTable.getColumn("Final exam").setCellRenderer(textRenderer);
        resultTable.getColumn("Other grade").setCellRenderer(textRenderer);
        resultTable.getColumn("Total grade").setCellRenderer(textRenderer);
        resultTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        centerJPanel.add(scrollPane);
        validate();
        repaint();
    }

    private void buildAddStudentFormView() {
        Session session = DatabaseService.getInstance().getSession();
        removeAll();
        // Set lại border
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Courses");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 30));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 250, 150, 250),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 50, 30, 50)))));

        CardPanel resultInfoPanel = new CardPanel(50);
        resultInfoPanel.setBorder(new EmptyBorder(30, 10, 10, 10));
        add(resultInfoPanel, BorderLayout.CENTER);
        resultInfoPanel.setBackground(Colors.getPrimary());
        resultInfoPanel.setLayout(new BoxLayout(resultInfoPanel, BoxLayout.Y_AXIS));
        // Hiển thị lớp và môn đang chọn đang chọn
        JLabel classNameLabel = new JLabel(selectedClass.getClassName() + " - " + selectedCourse.getCourseId());
        classNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 40));
        classNameLabel.setForeground(Colors.getTextColor());

        JLabel courseNameLabel = new JLabel(selectedCourse.getCourseName());
        courseNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        courseNameLabel.setForeground(Colors.getTextColor());
        // Điền mssv sinh viên
        MyTextField resultIDTextField = new MyTextField("Student ID", Colors.getTextColor(), 24);

        RoundedButton addButton = new RoundedButton("Confirm", 50, 24) {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Registration registration = session.find(Registration.class,
                        new RegistrationOfStudent(addingStudent, selectedCourse));
                if (registration != null) {
                    JOptionPane.showMessageDialog(null, "Student already registered this course!", "Student registered",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Registration newRegistration = new Registration(
                        new RegistrationOfStudent(addingStudent, selectedCourse));
                Transaction transaction = null;

                try {
                    transaction = session.beginTransaction();
                    session.save(newRegistration);
                    transaction.commit();
                    build();
                } catch (HibernateException ex) {
                    ex.printStackTrace();
                }
            }
        };
        addButton.setEnabled(false);

        RoundedButton searchButton = new RoundedButton("Search", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                String resultId = resultIDTextField.getText();
                if (resultId.equals("")) {
                    JOptionPane.showMessageDialog(null, "Student ID can not be empty!", "Empty result ID",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Student result = session.find(Student.class, resultId);
                if (result == null) {
                    JOptionPane.showMessageDialog(null, "Student doesn't existed!", "Student not found",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                addingStudent = result;
                addButton.setEnabled(true);

                resultInfoPanel.add(Box.createRigidArea(new Dimension(10, 20)));
                Box resultInfoRow = Box.createHorizontalBox();
                resultInfoPanel.add(resultInfoRow);

                Box resultInfoFirstColumn = Box.createVerticalBox();
                resultInfoRow.add(resultInfoFirstColumn);

                resultInfoFirstColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

                resultInfoRow.add(Box.createRigidArea(new Dimension(40, 10)));

                Box resultInfoSecondColumn = Box.createVerticalBox();
                resultInfoRow.add(resultInfoSecondColumn);

                resultInfoSecondColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel resultIdLabel = new JLabel("Student ID: " + result.getStudentId());
                resultIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                resultIdLabel.setForeground(Colors.getTextColor());
                resultInfoFirstColumn.add(resultIdLabel);

                resultInfoFirstColumn.add(Box.createRigidArea(new Dimension(0, 10)));

                JLabel resultNameLabel = new JLabel("Full name: " + result.getName());
                resultNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                resultNameLabel.setForeground(Colors.getTextColor());
                resultInfoSecondColumn.add(resultNameLabel);

                resultInfoSecondColumn.add(Box.createRigidArea(new Dimension(0, 10)));

                JLabel resultGenderLabel = new JLabel("Gender: " + result.getGender());
                resultGenderLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                resultGenderLabel.setForeground(Colors.getTextColor());
                resultInfoFirstColumn.add(resultGenderLabel);

                JLabel resultPersonalIdLabel = new JLabel("Personal ID: " + result.getPersonalId());
                resultPersonalIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                resultPersonalIdLabel.setForeground(Colors.getTextColor());
                resultInfoSecondColumn.add(resultPersonalIdLabel);

                resultInfoPanel.updateUI();

            }
        };
        searchButton.setPreferredSize(new Dimension(200, 50));
        searchButton.setMaximumSize(new Dimension(200, 50));

        // Hiển thị tên lớp và môn học
        JPanel topPanel = new JPanel(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);
        topPanel.setBackground(topPanel.getParent().getBackground());
        Box firstColumnBox = Box.createVerticalBox();
        firstColumnBox.add(classNameLabel);
        firstColumnBox.add(courseNameLabel);
        firstColumnBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(firstColumnBox, BorderLayout.PAGE_START);
        Box searchBarBox = Box.createHorizontalBox();
        Box secondColumnBox = Box.createVerticalBox();
        searchBarBox.add(resultIDTextField);
        searchBarBox.add(Box.createRigidArea(new Dimension(20, 0)));
        searchBarBox.add(secondColumnBox);
        secondColumnBox.add(Box.createRigidArea(new Dimension(0, 18)));
        secondColumnBox.add(searchButton);
        topPanel.add(searchBarBox, BorderLayout.LINE_END);
        resultIDTextField.setMaximumSize(new Dimension(300, 95));
        resultIDTextField.setPreferredSize(new Dimension(300, 95));

        // Tiêu đề Information
        Box informationBox = Box.createHorizontalBox();
        resultInfoPanel.add(informationBox);
        JLabel informationLabel = new JLabel("Student Information");
        informationBox.add(informationLabel);
        informationLabel.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 28));
        informationLabel.setForeground(Colors.getTextColor());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        add(bottomPanel, BorderLayout.PAGE_END);
        bottomPanel.setBackground(getParent().getBackground());
        // Nút quay lại
        RoundedButton backButton = new RoundedButton("Back", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                build();
            }
        };
        bottomPanel.add(backButton);
        backButton.setPreferredSize(new Dimension(200, 50));
        // Nút xác nhận

        bottomPanel.add(addButton);
        addButton.setPreferredSize(new Dimension(200, 50));

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