package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
import mystudy.Components.ComboBox.StatusTableCellRenderer;
import mystudy.Components.DatePicker.DateLabelFormatter;
import mystudy.Components.Table.ReexamineRequestModel;
import mystudy.Connector.DatabaseService;
import mystudy.Enum.GradeColumn;
import mystudy.Enum.Permission;
import mystudy.Enum.RequestStatus;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Course;
import mystudy.POJOs.Reexamine;
import mystudy.POJOs.ReexamineRequest;
import mystudy.POJOs.RegistrationOfStudent;
import mystudy.POJOs.Student;
import mystudy.POJOs.User;
import mystudy.User.UserService;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class ReexamineFragment extends JPanel implements Fragment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private User user = UserService.getInstance().getLoggedUser();
    private Reexamine selectedReexamine = null;
    private int selectedRequestIndex = -1;

    public ReexamineFragment() {
        setBackground(Colors.getBackground());
    }

    @Override
    public void build() {
        removeAll();
        // Khởi tạo trước các session dữ liệu set border cho fragment
        Session session = DatabaseService.getInstance().getSession();
        Vector<Reexamine> reexamines = new Vector<>();
        List<ReexamineRequest> requests = new ArrayList<>();
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Reexamines");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));
        JPanel topPanel = new JPanel();
        add(topPanel, BorderLayout.PAGE_START);
        topPanel.setLayout(new BorderLayout(30, 0));
        topPanel.setBackground(topPanel.getParent().getBackground());

        reexamines.addAll(fetchReexamines(session));
        // Tạo Combobox chọn đợt phúc khảo
        JComboBox<Reexamine> reexaminesComboBox = new JComboBox<Reexamine>(reexamines);
        topPanel.add(reexaminesComboBox, BorderLayout.CENTER);
        ComboBoxRenderer classesComboBoxRenderer = new ComboBoxRenderer();
        reexaminesComboBox.setRenderer(classesComboBoxRenderer);
        reexaminesComboBox.setBackground(Colors.getPrimary());
        reexaminesComboBox.setForeground(Colors.getTextColor());
        reexaminesComboBox.setPreferredSize(new Dimension(200, 50));
        reexaminesComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));

        if (selectedReexamine == null) {
            selectedReexamine = (Reexamine) reexaminesComboBox.getSelectedItem();
        }

        requests.addAll(fetchReexamineRequests(session, selectedReexamine));
        reexaminesComboBox.setSelectedItem(selectedReexamine);

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());
        if (user.getPermission().equals(Permission.ADMIN)) {
            // Nút tạo đợt phúc khảo
            RoundedButton createReexamineButton = new RoundedButton("Create reexamine", 50, 24) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    buildCreateReexamineFormView();
                }
            };
            topPanel.add(createReexamineButton, BorderLayout.LINE_END);
            createReexamineButton.setPreferredSize(new Dimension(250, 50));
        } else {
            RoundedButton createReexamineButton = new RoundedButton("Request", 50, 24) {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    Date date = new Date();
                    if (date.compareTo(new Date(selectedReexamine.getEndDate().getTime())) < 0)
                        buildRequestFormView();
                    else {
                        JOptionPane.showMessageDialog(null, "The reexamine is expired", "Expired",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            topPanel.add(createReexamineButton, BorderLayout.LINE_END);
            createReexamineButton.setPreferredSize(new Dimension(250, 50));
        }
        ReexamineRequestModel model = new ReexamineRequestModel(requests);
        RoundedButton checkButton = new RoundedButton("Check", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                buildRequestInfoView(requests.get(selectedRequestIndex));
            }
        };
        if (user.getPermission().equals(Permission.ADMIN)) {
            // Giáo vụ xem thông tin yêu cầu phúc khảo của sinh viên

            checkButton.setPreferredSize(new Dimension(200, 50));
            checkButton.setEnabled(false);
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            add(bottomPanel, BorderLayout.PAGE_END);
            bottomPanel.setBackground(bottomPanel.getParent().getBackground());
            bottomPanel.add(checkButton);
        }
        // Tạo bảng hiển thị tất cả các yêu cầu phúc khảo của sinh viên
        MyTable requestTable = new MyTable(model) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selectedRequestIndex = getSelectedRow();

                if (selectedRequestIndex >= 0) {
                    checkButton.setEnabled(true);
                } else {
                    checkButton.setEnabled(false);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 4 && user.getPermission().equals(Permission.ADMIN)) {
                    return true;
                }
                return false;
            }
        };
        requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        requestTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        textRenderer.setBorder(new EmptyBorder(10, 10, 10, 10));
        requestTable.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        requestTable.getColumn("Date").setCellRenderer(textRenderer);
        requestTable.getColumn("Student ID").setCellRenderer(textRenderer);
        requestTable.getColumn("Full name").setCellRenderer(textRenderer);
        requestTable.getColumn("Course name").setCellRenderer(textRenderer);
        RequestStatus[] requestStatus = { RequestStatus.SENT, RequestStatus.NO_UPDATED, RequestStatus.UPDATED };

        StatusTableCellRenderer statusTableCellRenderer = new StatusTableCellRenderer(requestStatus);
        requestTable.getColumn("Status").setCellRenderer(textRenderer);
        requestTable.getColumn("Status").setCellEditor(new DefaultCellEditor(statusTableCellRenderer));
        requestTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        centerJPanel.add(scrollPane);

        reexaminesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReexamine = (Reexamine) reexaminesComboBox.getSelectedItem();
                requests.clear();
                requests.addAll(fetchReexamineRequests(session, selectedReexamine));
                model.fireTableDataChanged();
            }
        });

    }

    private void buildRequestInfoView(ReexamineRequest request) {
        removeAll();

        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                "Request infomation");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());
        centerJPanel.setBorder(new EmptyBorder(100, 100, 30, 30));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        JLabel requestDateLabel = new JLabel("Request date: " + sdf.format(request.getRequestTime()));
        centerJPanel.add(requestDateLabel);
        requestDateLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        requestDateLabel.setForeground(Colors.getTextColor());
        requestDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerJPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        Box requestInfoBox = Box.createHorizontalBox();
        centerJPanel.add(requestInfoBox);
        requestInfoBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        Student student = request.getRegistration().getStudent();

        Box firstColumnBox = Box.createVerticalBox();
        Box secondColumnBox = Box.createVerticalBox();
        Box thirdColumnBox = Box.createVerticalBox();
        requestInfoBox.add(firstColumnBox);
        requestInfoBox.add(Box.createRigidArea(new Dimension(50, 20)));
        requestInfoBox.add(secondColumnBox);
        requestInfoBox.add(Box.createRigidArea(new Dimension(50, 20)));
        requestInfoBox.add(thirdColumnBox);

        JLabel studentIdLabel = new JLabel("Student ID: " + student.getStudentId());
        firstColumnBox.add(studentIdLabel);
        studentIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        studentIdLabel.setForeground(Colors.getTextColor());
        studentIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        firstColumnBox.add(Box.createRigidArea(new Dimension(50, 20)));

        JLabel fullNameLabel = new JLabel("Full name: " + student.getName());
        secondColumnBox.add(fullNameLabel);
        fullNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        fullNameLabel.setForeground(Colors.getTextColor());
        fullNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        secondColumnBox.add(Box.createRigidArea(new Dimension(50, 20)));

        Course course = request.getRegistration().getCourse();

        JLabel courseIdLabel = new JLabel("Course ID: " + course.getCourseId());
        firstColumnBox.add(courseIdLabel);
        courseIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        courseIdLabel.setForeground(Colors.getTextColor());
        courseIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel courseNameLabel = new JLabel("Course's name: " + course.getCourseName());
        secondColumnBox.add(courseNameLabel);
        courseNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        courseNameLabel.setForeground(Colors.getTextColor());
        courseNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        thirdColumnBox.add(Box.createRigidArea(new Dimension(100, 20)));

        JLabel desiredGradeLabel = new JLabel("Desired grade: " + request.getDesireGrade());
        thirdColumnBox.add(desiredGradeLabel);
        desiredGradeLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        desiredGradeLabel.setForeground(Colors.getTextColor());
        desiredGradeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerJPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel reasonLabel = new JLabel("Reason: ");
        centerJPanel.add(reasonLabel);
        reasonLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        reasonLabel.setForeground(Colors.getTextColor());
        reasonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea reasonTextArea = new JTextArea(request.getReason());
        reasonTextArea.setLineWrap(true);
        reasonTextArea.setEditable(false);
        reasonTextArea.setBackground(Colors.getPrimary());
        reasonTextArea.setForeground(Colors.getTextColor());
        JScrollPane scrollPane = new JScrollPane(reasonTextArea);
        centerJPanel.add(scrollPane);
        reasonTextArea.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        reasonTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        backButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(backButton);
    }

    private void buildRequestFormView() {
        removeAll();
        Session session = DatabaseService.getInstance().getSession();

        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                "Request form");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());
        centerJPanel.setBorder(new EmptyBorder(100, 100, 30, 30));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        JLabel requestDateLabel = new JLabel("Request date: " + dtf.format(now));
        centerJPanel.add(requestDateLabel);
        requestDateLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        requestDateLabel.setForeground(Colors.getTextColor());
        requestDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerJPanel.add(Box.createRigidArea(new Dimension(50, 20)));
        // Điền thông tin cần cho yêu cầu phúc khảo
        Box studentInfoBox = Box.createHorizontalBox();
        centerJPanel.add(studentInfoBox);
        studentInfoBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        Student student = user.getStudent();

        JLabel studentIdLabel = new JLabel("Student ID: " + student.getStudentId());
        studentInfoBox.add(studentIdLabel);
        studentIdLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        studentIdLabel.setForeground(Colors.getTextColor());
        studentIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentInfoBox.add(Box.createRigidArea(new Dimension(50, 20)));

        JLabel fullNameLabel = new JLabel("Full name: " + student.getName());
        studentInfoBox.add(fullNameLabel);
        fullNameLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        fullNameLabel.setForeground(Colors.getTextColor());
        fullNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerJPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        Vector<Course> courses = new Vector<>();

        Query<Course> query = session.createQuery(
                "select r.registration.course from Registration r where r.registration.student = :Student",
                Course.class);
        query.setParameter("Student", student);
        courses.addAll(query.list());

        JComboBox<Course> coursesComboBox = new JComboBox<Course>(courses);
        centerJPanel.add(coursesComboBox);
        ComboBoxRenderer coursesComboBoxRenderer = new ComboBoxRenderer();
        coursesComboBox.setRenderer(coursesComboBoxRenderer);
        coursesComboBox.setBackground(Colors.getPrimary());
        coursesComboBox.setForeground(Colors.getTextColor());
        coursesComboBox.setMaximumSize(new Dimension(500, 50));
        coursesComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        coursesComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box gradeBox = Box.createHorizontalBox();
        centerJPanel.add(gradeBox);
        gradeBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        Vector<GradeColumn> gradeColumns = new Vector<>();

        gradeColumns.add(GradeColumn.MIDTERM);
        gradeColumns.add(GradeColumn.FINAL_EXAM);
        gradeColumns.add(GradeColumn.OTHER);
        gradeColumns.add(GradeColumn.TOTAL);

        JComboBox<GradeColumn> gradeColumnsComboBox = new JComboBox<GradeColumn>(gradeColumns);
        gradeBox.add(gradeColumnsComboBox);
        ComboBoxRenderer gradeColumnsComboBoxRenderer = new ComboBoxRenderer();
        gradeColumnsComboBox.setRenderer(gradeColumnsComboBoxRenderer);
        gradeColumnsComboBox.setBackground(Colors.getPrimary());
        gradeColumnsComboBox.setForeground(Colors.getTextColor());
        gradeColumnsComboBox.setMaximumSize(new Dimension(500, 50));
        gradeColumnsComboBox.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        gradeColumnsComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        gradeBox.add(Box.createRigidArea(new Dimension(30, 0)));

        MyTextField desireGradeTextField = new MyTextField("Desired grade", Colors.getTextColor(), 24);
        gradeBox.add(desireGradeTextField);
        desireGradeTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        desireGradeTextField.setMaximumSize(new Dimension(300, 100));

        JLabel reasonLabel = new JLabel("Reason:");
        centerJPanel.add(reasonLabel);
        reasonLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        reasonLabel.setForeground(Colors.getTextColor());
        reasonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea reasonTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(reasonTextArea);
        centerJPanel.add(scrollPane);
        reasonTextArea.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        reasonTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        reasonTextArea.setLineWrap(true);
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
        backButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(backButton);
        // Nút xác nhận
        RoundedButton confirmButton = new RoundedButton("Confirm", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (desireGradeTextField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "You must fill desired grade!", "Invalid grade",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                float desiredGrade = Float.parseFloat(desireGradeTextField.getText());

                ReexamineRequest request = new ReexamineRequest(
                        new RegistrationOfStudent(student, (Course) coursesComboBox.getSelectedItem()),
                        selectedReexamine, new java.sql.Date((new Date()).getTime()), RequestStatus.SENT,
                        (GradeColumn) gradeColumnsComboBox.getSelectedItem(), desiredGrade, reasonTextArea.getText());
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    session.save(request);
                    transaction.commit();
                    build();

                } catch (HibernateException ex) {
                    transaction.rollback();
                    ex.printStackTrace();
                }
            }
        };
        confirmButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(confirmButton);

    }

    // Tạo form tạo điểm phúc khảo
    private void buildCreateReexamineFormView() {
        removeAll();
        Session session = DatabaseService.getInstance().getSession();

        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                "New reexamine");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 300, 300, 400)))));

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());
        centerJPanel.setBorder(new EmptyBorder(100, 200, 30, 30));
        // Chọn ngày bắt đầu
        UtilDateModel fromDateModel = new UtilDateModel();
        JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromDateModel);

        JDatePickerImpl fromDatePicker = new JDatePickerImpl(fromDatePanel, new DateLabelFormatter());
        centerJPanel.add(fromDatePicker);
        fromDatePicker.setMaximumSize(new Dimension(300, 40));
        fromDatePicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        fromDatePicker.setBackground(fromDatePicker.getParent().getBackground());
        fromDatePicker.getComponent(0).setPreferredSize(new Dimension(100, 40)); // JFormattedTextField
        fromDatePicker.getComponent(0).setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        fromDatePicker.getComponent(0).setBackground(fromDatePicker.getParent().getBackground());
        fromDatePicker.getComponent(0).setForeground(Colors.getTextColor());
        fromDatePicker.getComponent(1).setPreferredSize(new Dimension(50, 40));// JButton
        fromDatePicker.getComponent(1).setBackground(fromDatePicker.getParent().getBackground());
        fromDatePicker.getComponent(1).setForeground(Colors.getTextColor());
        fromDatePicker.getJFormattedTextField().setText("Start date");

        centerJPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Chọn ngày kết thúc
        UtilDateModel toDateModel = new UtilDateModel();
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toDateModel);
        JDatePickerImpl toDatePicker = new JDatePickerImpl(toDatePanel, new DateLabelFormatter());
        centerJPanel.add(toDatePicker);
        toDatePicker.setMaximumSize(new Dimension(300, 40));
        toDatePicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        toDatePicker.setBackground(toDatePicker.getParent().getBackground());
        toDatePicker.getComponent(0).setPreferredSize(new Dimension(100, 40)); // JFormattedTextField
        toDatePicker.getComponent(0).setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        toDatePicker.getComponent(0).setBackground(toDatePicker.getParent().getBackground());
        toDatePicker.getComponent(0).setForeground(Colors.getTextColor());
        toDatePicker.getComponent(1).setPreferredSize(new Dimension(50, 40));// JButton
        toDatePicker.getComponent(1).setBackground(toDatePicker.getParent().getBackground());
        toDatePicker.getComponent(1).setForeground(Colors.getTextColor());
        toDatePicker.getJFormattedTextField().setText("End date");

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
        backButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(backButton);
        // Nút xác nhận
        RoundedButton confirmButton = new RoundedButton("Confirm", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Date startDate = (Date) fromDatePicker.getModel().getValue();
                Date endDate = (Date) toDatePicker.getModel().getValue();

                if (startDate == null) {
                    JOptionPane.showMessageDialog(null, "You must choose a start date!", "Invalid from date",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (endDate == null) {
                    JOptionPane.showMessageDialog(null, "You must choose an end date!", "Invalid to date",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Reexamine reexamine = new Reexamine(new java.sql.Date(startDate.getTime()),
                        new java.sql.Date(endDate.getTime()));
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    session.save(reexamine);
                    transaction.commit();
                    selectedReexamine = reexamine;
                    build();

                } catch (HibernateException ex) {
                    transaction.rollback();
                    ex.printStackTrace();
                }
            }
        };
        confirmButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(confirmButton);

    }

    private List<Reexamine> fetchReexamines(Session session) {
        Query<Reexamine> query = session.createQuery("from Reexamine", Reexamine.class);
        return query.list();
    }

    private List<ReexamineRequest> fetchReexamineRequests(Session session, Reexamine reexamine) {
        Query<ReexamineRequest> query = null;
        if (user.getPermission().equals(Permission.ADMIN)) {
            query = session.createQuery("select r from ReexamineRequest r where r.reexamine = :Reexamine",
                    ReexamineRequest.class);
            query.setParameter("Reexamine", reexamine);
        } else {
            query = session.createQuery(
                    "select r from ReexamineRequest r where r.reexamine = :Reexamine and r.registration.student = :Student",
                    ReexamineRequest.class);
            query.setParameter("Reexamine", reexamine);
            query.setParameter("Student", user.getStudent());
        }
        return query.list();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}