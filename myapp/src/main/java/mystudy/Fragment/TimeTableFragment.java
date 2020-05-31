package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import mystudy.Colors.Colors;
import mystudy.Components.CardPanel;
import mystudy.Components.MyTable;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Components.ComboBox.ComboBoxRenderer;
import mystudy.Components.Table.TimeTableModel;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Class;
import mystudy.POJOs.Course;
import mystudy.POJOs.Registration;
import mystudy.POJOs.RegistrationPK;
import mystudy.POJOs.Student;
import mystudy.POJOs.TimeTable;
import mystudy.POJOs.TimeTablePK;

public class TimeTableFragment extends JPanel implements Fragment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Class selectedClass = null;

    public TimeTableFragment() {
        setBackground(Colors.getBackground());
    }

    @Override
    public void build() {
        removeAll();
        // Khởi tạo trước các session dữ liệu set border cho fragment
        Session session = DatabaseService.getInstance().getSession();
        Vector<Class> classes = new Vector<>();
        List<Course> courses = new ArrayList<>();
        TimeTableModel model = new TimeTableModel(courses);

        setLayout(new BorderLayout(0, 20));
        TitledBorder profiletitledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                "Time table");
        profiletitledBorder.setTitleJustification(TitledBorder.CENTER);
        profiletitledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        profiletitledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(profiletitledBorder, new EmptyBorder(30, 30, 30, 30)))));
        // Lấy danh sách lớp
        classes.addAll(session.createQuery("from Class c ORDER BY c.className", Class.class).list());

        JPanel topPanel = new JPanel(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);
        topPanel.setBackground(topPanel.getParent().getBackground());

        // Tạo Combobox chọn lớp
        JComboBox<Class> classesComboBox = new JComboBox<Class>(classes);
        topPanel.add(classesComboBox, BorderLayout.LINE_START);
        ComboBoxRenderer comboBoxRenderer = new ComboBoxRenderer();
        classesComboBox.setRenderer(comboBoxRenderer);
        classesComboBox.setBackground(Colors.getPrimary());
        classesComboBox.setForeground(Colors.getTextColor());
        classesComboBox.setPreferredSize(new Dimension(200, 50));
        classesComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));

        if (selectedClass == null)
            selectedClass = (Class) classesComboBox.getSelectedItem();

        courses.clear();
        // Lấy thời khóa biểu của lớp đã chọn
        courses.addAll(fetchCourses(session, selectedClass));
        classesComboBox.setSelectedItem(selectedClass);

        classesComboBox.addActionListener(new ActionListener() {
            // Xử lý sự kiện chọn lớp
            @Override
            public void actionPerformed(ActionEvent e) {
                // lấy danh sách lớp hiển thị lên
                courses.clear();
                selectedClass = (Class) classesComboBox.getSelectedItem();
                courses.addAll(fetchCourses(session, selectedClass));
                model.fireTableDataChanged();
            }

        });
        // Tạo nút import một lớp mới
        RoundedButton importButton = new RoundedButton("Import", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        InputStreamReader fileReader = new InputStreamReader(
                                new FileInputStream(fileChooser.getSelectedFile()), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(fileReader);
                        // Đọc tên lớp
                        String className = reader.readLine().split(",")[0].replaceAll("[^\\P{L}\\P{N}]", "")
                                .replace("\uFEFF", "");
                        Class addingClass = session.find(Class.class, className);
                        if (addingClass == null) {
                            JOptionPane.showMessageDialog(null, "Class " + className + " isn't existed!",
                                    "Class not found", JOptionPane.ERROR_MESSAGE);
                            reader.close();
                            return;
                        }
                        List<Course> newCourses = new ArrayList<>();
                        // Đọc bỏ dòng tên cột
                        String line = reader.readLine();
                        line = reader.readLine();
                        while (line != null) {
                            // Đọc từng dòng
                            String[] args = line.split(",");
                            newCourses.add(new Course(args[1], args[2], args[3]));
                            line = reader.readLine();
                        }
                        reader.close();
                        Transaction transaction = null;
                        try {
                            Session session = DatabaseService.getInstance().getSession();

                            for (Course course : newCourses) {
                                TimeTable timeTable = new TimeTable(new TimeTablePK(addingClass, course));
                                Query<Student> query = session.createQuery(
                                        "select s from Student s where s.className = :class ORDER BY s.studentId",
                                        Student.class);
                                query.setParameter("class", addingClass);

                                List<Student> students = query.list();
                                session.clear();

                                transaction = session.beginTransaction();
                                session.save(course);
                                transaction.commit();

                                transaction = session.beginTransaction();
                                session.save(timeTable);
                                transaction.commit();

                                for (Student student : students) {
                                    Registration registration = new Registration(new RegistrationPK(student, course));
                                    transaction = session.beginTransaction();
                                    session.save(registration);
                                    transaction.commit();
                                }

                            }

                        } catch (HibernateException ex) {
                            ex.printStackTrace();
                            System.out.print(ex.getMessage());
                            transaction.rollback();
                        }
                        for (Class tClass : classes) {
                            if (tClass.getClassName().equals(addingClass.getClassName())) {
                                classesComboBox.setSelectedItem(tClass);
                            }
                        }
                        session.clear();
                    } catch (IOException e1) {
                        e1.printStackTrace();

                    }

                }
            }
        };
        topPanel.add(importButton, BorderLayout.LINE_END);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottomPanel, BorderLayout.PAGE_END);

        importButton.setPreferredSize(new Dimension(200, 50));

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
            }
        };
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        textRenderer.setBorder(new EmptyBorder(10, 10, 10, 10));
        studentTable.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        studentTable.getColumn("No.").setCellRenderer(textRenderer);
        studentTable.getColumn("Course ID").setCellRenderer(textRenderer);
        studentTable.getColumn("Course's name").setCellRenderer(textRenderer);
        studentTable.getColumn("Room").setCellRenderer(textRenderer);
        studentTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        centerJPanel.add(scrollPane);
        validate();
        repaint();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    private List<Course> fetchCourses(Session session, Class fromClass) {
        List<Course> courses = new ArrayList<>();
        // Lấy thời khóa biểu của lớp đã chọn
        Query<TimeTable> query = session
                .createQuery("select t from TimeTable t where t.courseInClass.className = :class ", TimeTable.class);
        query.setParameter("class", fromClass);
        var timeTable = query.list();
        courses.clear();
        for (TimeTable courseInClass : timeTable) {
            courses.add(courseInClass.getCourseInClass().getCourse());
        }
        session.clear();
        return courses;
    }
}