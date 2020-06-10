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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import mystudy.Components.MyTextField;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Components.ComboBox.ComboBoxRenderer;
import mystudy.Components.Table.StudentListModel;
import mystudy.Connector.DatabaseService;
import mystudy.Enum.Permission;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Class;
import mystudy.POJOs.Student;
import mystudy.POJOs.User;

public class StudentsFragment extends JPanel implements Fragment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Class selectedClass = null;

    public JPanel getPanel() {
        return this;
    }

    public StudentsFragment() {
        setBackground(Colors.getBackground());

    }

    public void build() {
        removeAll();
        // Khởi tạo trước các session dữ liệu set border cho fragment
        Session session = DatabaseService.getInstance().getSession();
        List<Student> students = new ArrayList<>();
        Vector<Class> classes = new Vector<>();
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Students");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        // Lấy danh sách lớp
        classes.addAll(session.createQuery("from Class c ORDER BY c.className", Class.class).list());

        StudentListModel model = new StudentListModel(students);

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
        // Tạo nút thêm sinh viên mới
        RoundedButton addButton = new RoundedButton("Add student", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                // Hiển thị form điền thông tin sinh viên mới
                buildAddStudentFormView();

            }
        };
        addButton.setPreferredSize(new Dimension(200, 50));
        if (selectedClass == null)
            selectedClass = (Class) classesComboBox.getSelectedItem();

        // Lấy danh sách sinh viên trong lớp đã chọn
        Query<Student> query = session
                .createQuery("select s from Student s where s.className = :class ORDER BY s.studentId", Student.class);
        query.setParameter("class", selectedClass);
        students.clear();
        students.addAll(query.list());
        session.clear();
        if (selectedClass == null)
            addButton.setEnabled(false);
        classesComboBox.setSelectedItem(selectedClass);
        classesComboBox.addActionListener(new ActionListener() {
            // Xử lý sự kiện chọn lớp
            @Override
            public void actionPerformed(ActionEvent e) {
                // lấy danh sách lớp hiển thị lên
                selectedClass = (Class) classesComboBox.getSelectedItem();
                Query<Student> query = session.createQuery(
                        "select s from Student s where s.className = :class ORDER BY s.studentId", Student.class);
                query.setParameter("class", selectedClass);
                addButton.setEnabled(true);
                students.clear();
                students.addAll(query.list());
                session.clear();
                model.fireTableDataChanged();
                addButton.setEnabled(true);
            }

        });
        // Tạo nút import một lớp mới
        RoundedButton importButton = new RoundedButton("Import students", 50, 24) {
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
                        Class newClass = new Class(reader.readLine().split(",")[0].replaceAll("[^\\P{L}\\P{N}]", "")
                                .replace("\uFEFF", ""));
                        // Đọc bỏ dòng tên cột
                        String line = reader.readLine();
                        line = reader.readLine();
                        List<Student> newStudents = new ArrayList<>();
                        while (line != null) {
                            // Đọc từng dòng
                            String[] args = line.split(",");
                            Student student = new Student(args[1], args[2], args[3], args[4], newClass);
                            newStudents.add(student);
                            line = reader.readLine();
                        }
                        reader.close();
                        Transaction transaction = null;
                        try {
                            Session session = DatabaseService.getInstance().getSession();
                            transaction = session.beginTransaction();

                            session.save(newClass);

                            transaction.commit();

                            for (Student student : newStudents) {
                                transaction = session.beginTransaction();
                                session.save(student);
                                transaction.commit();
                                transaction = session.beginTransaction();
                                session.save(new User(student.getStudentId(), student.getStudentId(),
                                        Permission.STUDENT, student));
                                transaction.commit();
                            }
                            classes.clear();
                            classes.addAll(
                                    session.createQuery("from Class c ORDER BY c.className", Class.class).list());

                        } catch (HibernateException ex) {
                            ex.printStackTrace();
                            System.out.print(ex.getMessage());
                            transaction.rollback();
                        }
                        classesComboBox.setSelectedItem(newClass);
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
        removeAll();
        // Set lại border
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Students");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 250, 150, 250),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        CardPanel formPanel = new CardPanel(50);
        formPanel.setBorder(new EmptyBorder(30, 10, 10, 10));
        add(formPanel, BorderLayout.CENTER);
        formPanel.setBackground(Colors.getPrimary());
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        // Hiển thị lớp đang chọn
        Box classLabelBox = Box.createHorizontalBox();
        JLabel classNameLabel = new JLabel(selectedClass.getClassName());
        classNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 60));
        classNameLabel.setForeground(Colors.getTextColor());
        classLabelBox.add(classNameLabel);
        formPanel.add(classLabelBox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        // Điền thông tin sinh viên
        MyTextField studentIDTextField = new MyTextField("Student ID", Colors.getTextColor(), 24);
        MyTextField studentNameTextField = new MyTextField("Full name", Colors.getTextColor(), 24);

        Box firstLineBox = Box.createHorizontalBox();
        formPanel.add(firstLineBox);
        firstLineBox.add(studentIDTextField);
        firstLineBox.add(Box.createRigidArea(new Dimension(50, 0)));
        firstLineBox.add(studentNameTextField);
        studentIDTextField.setMaximumSize(new Dimension(300, 100));
        studentNameTextField.setMaximumSize(new Dimension(400, 100));

        String[] gender = { "Nam", "Nữ" };

        JComboBox<String> genderComboBox = new JComboBox<>(gender);
        genderComboBox.setBackground(Colors.getPrimary());
        genderComboBox.setForeground(Colors.getTextColor());
        genderComboBox.setRenderer(new ComboBoxRenderer());
        genderComboBox.setMaximumSize(new Dimension(150, 50));
        genderComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));

        formPanel.add(Box.createVerticalStrut(20));

        Box secondLineBox = Box.createHorizontalBox();
        formPanel.add(secondLineBox);
        JLabel label = new JLabel("Gender: ");
        secondLineBox.add(label);
        secondLineBox.add(Box.createRigidArea(new Dimension(10, 0)));
        label.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        label.setForeground(Colors.getTextColor());
        MyTextField personalIDTextField = new MyTextField("Personal ID", Colors.getTextColor(), 24);
        personalIDTextField.setMaximumSize(new Dimension(300, 100));
        secondLineBox.add(genderComboBox);
        secondLineBox.add(Box.createRigidArea(new Dimension(100, 0)));
        secondLineBox.add(personalIDTextField);
        secondLineBox.add(Box.createRigidArea(new Dimension(100, 0)));

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
        RoundedButton addButton = new RoundedButton("Confirm", 50, 24) {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                String studentId = studentIDTextField.getText();

                if (studentId.equals("")) {
                    JOptionPane.showMessageDialog(null, "Student ID can not be empty!", "Empty student ID",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (studentNameTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Student's name can not be empty!", "Empty student's name",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (personalIDTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Personal ID can not be empty", "Empty personal ID!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (personalIDTextField.getText().length() > 10) {
                    JOptionPane.showMessageDialog(null, "Personal ID's length can't more than 10 characters!",
                            "Too long personal ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Session session = DatabaseService.getInstance().getSession();
                // Kiểm tra mã số sinh viên đã tồn tại hay chưa
                Student existedStudent = session.find(Student.class, studentId);

                if (existedStudent != null) {
                    JOptionPane.showMessageDialog(null, "Student ID has been existed!", "Student ID invalid",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student student = new Student(studentId, studentNameTextField.getText(),
                        (String) genderComboBox.getSelectedItem(), personalIDTextField.getText(), selectedClass);
                System.out.println(student);
                Transaction transaction = null;
                try {

                    transaction = session.beginTransaction();
                    session.save(student);
                    session.save(new User(studentId, studentId, Permission.STUDENT, student));
                    transaction.commit();
                    JOptionPane.showMessageDialog(null,
                            "New student has been add to class " + selectedClass.getClassName(), "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                    session.clear();
                    build();
                } catch (HibernateException ex) {
                    ex.printStackTrace();
                }
            }
        };
        bottomPanel.add(addButton);
        addButton.setPreferredSize(new Dimension(200, 50));

        validate();
        repaint();
    }
}