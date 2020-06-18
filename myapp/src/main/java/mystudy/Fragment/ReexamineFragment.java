package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
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
import mystudy.Components.DatePicker.DateLabelFormatter;
import mystudy.Components.Table.ReexamineRequestModel;
import mystudy.Components.Table.StudentListModel;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Class;
import mystudy.POJOs.Course;
import mystudy.POJOs.Reexamine;
import mystudy.POJOs.ReexamineRequest;
import mystudy.POJOs.Registration;
import mystudy.POJOs.RegistrationOfStudent;
import mystudy.POJOs.Student;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class ReexamineFragment extends JPanel implements Fragment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Reexamine selectedReexamine = null;
    private Course selectedCourse = null;
    private Student addingStudent = null;
    private int selectedStudentIndex = -1;

    public ReexamineFragment() {
        setBackground(Colors.getBackground());
    }

    @Override
    public void build() {
        removeAll();
        // Khởi tạo trước các session dữ liệu set border cho fragment
        Session session = DatabaseService.getInstance().getSession();
        Vector<Reexamine> reexamines = new Vector<>();
        Vector<Course> courses = new Vector<>();
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
        } else {
            reexaminesComboBox.setSelectedItem(selectedReexamine);
        }

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());

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

        ReexamineRequestModel model = new ReexamineRequestModel(requests);
        // Tạo bảng hiển thị tất cả các yêu cầu phúc khảo của sinh viên
        MyTable requestTable = new MyTable(model) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

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
        requestTable.getColumn("Status").setCellRenderer(textRenderer);
        requestTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        centerJPanel.add(scrollPane);

        reexaminesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReexamine = (Reexamine) reexaminesComboBox.getSelectedItem();
                requests.addAll(fetchReexamineRequests(session, selectedReexamine));
            }
        });
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
                    JOptionPane.showMessageDialog(null, "You need select a from date!", "Invalid from date",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (endDate == null) {
                    JOptionPane.showMessageDialog(null, "You need select a to date!", "Invalid to date",
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
        Query<ReexamineRequest> query = session
                .createQuery("select r from ReexamineRequest r where r.reexamine = :Reexamine", ReexamineRequest.class);
        query.setParameter("Reexamine", reexamine);
        return query.list();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}