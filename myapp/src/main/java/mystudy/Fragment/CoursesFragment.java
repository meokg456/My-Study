package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

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
import mystudy.Components.Table.StudentListModel;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Class;
import mystudy.POJOs.Course;
import mystudy.POJOs.Registration;
import mystudy.POJOs.RegistrationOfStudent;
import mystudy.POJOs.Student;

public class CoursesFragment extends JPanel implements Fragment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Class selectedClass = null;
    private Course selectedCourse = null;
    private Student addingStudent = null;
    private int selectedStudentIndex = -1;

    public CoursesFragment() {
        setBackground(Colors.getBackground());
    }

    @Override
    public void build() {
        removeAll();
        // Khởi tạo trước các session dữ liệu set border cho fragment
        Session session = DatabaseService.getInstance().getSession();
        List<Student> students = new ArrayList<>();
        Vector<Class> classes = new Vector<>();
        Vector<Course> courses = new Vector<>();
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Courses");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        // Lấy danh sách lớp
        classes.addAll(session.createQuery("from Class c ORDER BY c.className", Class.class).list());

        StudentListModel model = new StudentListModel(students);

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
        // Tạo Combobox chọn môn
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

        students.addAll(fetchStudentFromCourse(session, selectedCourse));
        coursesComboBox.setSelectedItem(selectedCourse);

        // Tạo nút thêm sinh viên mới
        RoundedButton addButton = new RoundedButton("Add student", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                // Hiển thị màn hình tìm kiếm sinh viên
                buildAddStudentFormView();

            }
        };
        addButton.setPreferredSize(new Dimension(200, 50));

        RoundedButton removeButton = new RoundedButton("Remove", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Student selectedStudent = students.get(selectedStudentIndex);
                // Hiển thị form điền thông tin sinh viên mới
                Registration registration = session.find(Registration.class,
                        new RegistrationOfStudent(selectedStudent, selectedCourse));
                if (registration == null) {
                    JOptionPane.showMessageDialog(null, "Student doesn't existed!", "Student not found",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    session.remove(registration);
                    transaction.commit();
                    students.remove(selectedStudent);
                    model.fireTableRowsDeleted(selectedStudentIndex, selectedStudentIndex);
                    setEnabled(false);
                } catch (HibernateException ex) {
                    ex.printStackTrace();
                }
            }
        };
        removeButton.setPreferredSize(new Dimension(200, 50));
        removeButton.setEnabled(false);

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
                addButton.setEnabled(false);
                coursesComboBox.updateUI();
            }

        });

        coursesComboBox.addActionListener(new ActionListener() {
            // Xử lý sự kiện chọn môn học
            @Override
            public void actionPerformed(ActionEvent e) {
                // lấy danh sách sinh viên tham gia môn học hiển thị lên
                students.clear();
                selectedCourse = (Course) coursesComboBox.getSelectedItem();
                if (selectedCourse != null) {

                    addButton.setEnabled(true);
                    students.addAll(fetchStudentFromCourse(session, selectedCourse));
                }
                model.fireTableDataChanged();
            }

        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        add(bottomPanel, BorderLayout.PAGE_END);

        bottomPanel.add(removeButton);
        bottomPanel.add(addButton);
        bottomPanel.setBackground(bottomPanel.getParent().getBackground());
        // Hiển thị thông tin giáo vụ

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());

        // Tạo bảng hiển thị thông tin sinh viên
        MyTable studentTable = new MyTable(model) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                selectedStudentIndex = getSelectedRow();
                if (selectedStudentIndex >= 0)
                    removeButton.setEnabled(true);
            }
        };
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        textRenderer.setBorder(new EmptyBorder(10, 10, 10, 10));
        studentTable.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        studentTable.getColumn("No.").setCellRenderer(textRenderer);
        studentTable.getColumn("Student ID").setCellRenderer(textRenderer);
        studentTable.getColumn("Full name").setCellRenderer(textRenderer);
        studentTable.getColumn("Gender").setCellRenderer(textRenderer);
        studentTable.getColumn("Personal ID").setCellRenderer(textRenderer);
        studentTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(studentTable);
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

        CardPanel studentInfoPanel = new CardPanel(50);
        studentInfoPanel.setBorder(new EmptyBorder(30, 10, 10, 10));
        add(studentInfoPanel, BorderLayout.CENTER);
        studentInfoPanel.setBackground(Colors.getPrimary());
        studentInfoPanel.setLayout(new BoxLayout(studentInfoPanel, BoxLayout.Y_AXIS));
        // Hiển thị lớp và môn đang chọn đang chọn
        JLabel classNameLabel = new JLabel(selectedClass.getClassName() + " - " + selectedCourse.getCourseId());
        classNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 40));
        classNameLabel.setForeground(Colors.getTextColor());

        JLabel courseNameLabel = new JLabel(selectedCourse.getCourseName());
        courseNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        courseNameLabel.setForeground(Colors.getTextColor());
        // Điền mssv sinh viên
        MyTextField studentIDTextField = new MyTextField("Student ID", Colors.getTextColor(), 24);

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
                String studentId = studentIDTextField.getText();
                if (studentId.equals("")) {
                    JOptionPane.showMessageDialog(null, "Student ID can not be empty!", "Empty student ID",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Student student = session.find(Student.class, studentId);
                if (student == null) {
                    JOptionPane.showMessageDialog(null, "Student doesn't existed!", "Student not found",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                addingStudent = student;
                addButton.setEnabled(true);

                studentInfoPanel.add(Box.createRigidArea(new Dimension(10, 20)));
                Box studentInfoRow = Box.createHorizontalBox();
                studentInfoPanel.add(studentInfoRow);

                Box studentInfoFirstColumn = Box.createVerticalBox();
                studentInfoRow.add(studentInfoFirstColumn);

                studentInfoFirstColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

                studentInfoRow.add(Box.createRigidArea(new Dimension(40, 10)));

                Box studentInfoSecondColumn = Box.createVerticalBox();
                studentInfoRow.add(studentInfoSecondColumn);

                studentInfoSecondColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel studentIdLabel = new JLabel("Student ID: " + student.getStudentId());
                studentIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                studentIdLabel.setForeground(Colors.getTextColor());
                studentInfoFirstColumn.add(studentIdLabel);

                studentInfoFirstColumn.add(Box.createRigidArea(new Dimension(0, 10)));

                JLabel studentNameLabel = new JLabel("Full name: " + student.getName());
                studentNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                studentNameLabel.setForeground(Colors.getTextColor());
                studentInfoSecondColumn.add(studentNameLabel);

                studentInfoSecondColumn.add(Box.createRigidArea(new Dimension(0, 10)));

                JLabel studentGenderLabel = new JLabel("Gender: " + student.getGender());
                studentGenderLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                studentGenderLabel.setForeground(Colors.getTextColor());
                studentInfoFirstColumn.add(studentGenderLabel);

                JLabel studentPersonalIdLabel = new JLabel("Personal ID: " + student.getPersonalId());
                studentPersonalIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
                studentPersonalIdLabel.setForeground(Colors.getTextColor());
                studentInfoSecondColumn.add(studentPersonalIdLabel);

                studentInfoPanel.updateUI();

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
        searchBarBox.add(studentIDTextField);
        searchBarBox.add(Box.createRigidArea(new Dimension(20, 0)));
        searchBarBox.add(secondColumnBox);
        secondColumnBox.add(Box.createRigidArea(new Dimension(0, 18)));
        secondColumnBox.add(searchButton);
        topPanel.add(searchBarBox, BorderLayout.LINE_END);
        studentIDTextField.setMaximumSize(new Dimension(300, 95));
        studentIDTextField.setPreferredSize(new Dimension(300, 95));

        // Tiêu đề Information
        Box informationBox = Box.createHorizontalBox();
        studentInfoPanel.add(informationBox);
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

    private List<Student> fetchStudentFromCourse(Session session, Course fromCourse) {
        List<Student> students = new ArrayList<>();
        Query<Student> query = session.createQuery(
                "select r.registration.student from Registration r where r.registration.course = :Course",
                Student.class);
        query.setParameter("Course", fromCourse);

        students.addAll(query.list());
        session.clear();
        return students;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}