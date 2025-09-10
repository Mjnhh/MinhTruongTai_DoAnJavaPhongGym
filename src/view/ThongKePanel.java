package view;

import dao.ThongKeDAO;
import dao.ThuPhiDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThongKePanel extends JPanel {

    private final ThongKeDAO dao = new ThongKeDAO();
    private final ThuPhiDAO thuPhiDAO = new ThuPhiDAO();

    private JLabel lblTotalMembers;
    private JLabel lblActiveMembers;
    private JLabel lblExpiredMembers;
    private JLabel lblRevenueToday;
    private JLabel lblRevenueThisMonth;
    private JLabel lblNewRegsThisMonth;

    private JTable tblTopGoiTap;
    private JTable tblRevenueByType;

    public ThongKePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Header: Filters
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        JComboBox<Integer> cbMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbMonth.addItem(i);
        JComboBox<Integer> cbYear = new JComboBox<>();
        int currentYear = java.time.LocalDate.now().getYear();
        for (int y = currentYear - 5; y <= currentYear + 1; y++) cbYear.addItem(y);
        cbMonth.setSelectedItem(java.time.LocalDate.now().getMonthValue());
        cbYear.setSelectedItem(currentYear);
        JButton btnRefresh = new JButton("Cập nhật");
        header.add(new JLabel("Tháng:")); header.add(cbMonth);
        header.add(new JLabel("Năm:")); header.add(cbYear);
        header.add(btnRefresh);
        add(header, BorderLayout.NORTH);

        // Center: metrics + tables
        JPanel center = new JPanel(new BorderLayout(12, 12));
        add(center, BorderLayout.CENTER);

        // Metrics cards
        JPanel metrics = new JPanel(new GridLayout(2, 3, 12, 12));
        lblTotalMembers = createMetric(metrics, "Tổng hội viên", "0");
        lblActiveMembers = createMetric(metrics, "Đang hoạt động", "0");
        lblExpiredMembers = createMetric(metrics, "Hết hạn", "0");
        lblRevenueToday = createMetric(metrics, "Doanh thu hôm nay", "0");
        lblRevenueThisMonth = createMetric(metrics, "Doanh thu tháng", "0");
        lblNewRegsThisMonth = createMetric(metrics, "ĐK mới tháng", "0");
        center.add(metrics, BorderLayout.NORTH);

        // Split: Top gói tập + Doanh thu theo loại
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setDividerLocation(0.5);
        center.add(split, BorderLayout.CENTER);

        // Left: top gói tập
        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBorder(BorderFactory.createTitledBorder("Gói tập đăng ký nhiều"));
        tblTopGoiTap = new JTable();
        left.add(new JScrollPane(tblTopGoiTap), BorderLayout.CENTER);
        split.setLeftComponent(left);

        // Right: doanh thu theo loại phí
        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBorder(BorderFactory.createTitledBorder("Doanh thu theo loại phí (tháng chọn)"));
        tblRevenueByType = new JTable();
        right.add(new JScrollPane(tblRevenueByType), BorderLayout.CENTER);
        split.setRightComponent(right);

        // Footer: tips
        JTextArea footer = new JTextArea("Mẹo: Dùng bộ lọc Tháng/Năm để xem nhanh doanh thu và thống kê theo kỳ.");
        footer.setEditable(false);
        footer.setBackground(getBackground());
        footer.setForeground(Color.GRAY.darker());
        add(footer, BorderLayout.SOUTH);

        // Load initial
        btnRefresh.addActionListener(e -> reload((Integer) cbMonth.getSelectedItem(), (Integer) cbYear.getSelectedItem()));
        reload(java.time.LocalDate.now().getMonthValue(), currentYear);
    }

    private JLabel createMetric(JPanel container, String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 12f));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(lblValue.getFont().deriveFont(Font.BOLD, 18f));
        lblValue.setForeground(new Color(25, 118, 210));
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        container.add(card);
        return lblValue;
    }

    private void reload(int month, int year) {
        // Metrics: members overview
        Object[] hv = dao.thongKeHoiVien();
        lblTotalMembers.setText(String.valueOf(hv[0]));
        lblActiveMembers.setText(String.valueOf(hv[1]));
        lblExpiredMembers.setText(String.valueOf(hv[2]));

        // Revenue today/month
        lblRevenueToday.setText(String.valueOf(thuPhiDAO.getRevenueToday()));
        Object[] rev = dao.doanhThuTheoThang(month, year);
        lblRevenueThisMonth.setText(String.valueOf(rev[0]) + " (" + rev[1] + ")");

        // New registrations this month
        int newRegs = dao.soDangKyMoiTheoThang(month, year);
        lblNewRegsThisMonth.setText(String.valueOf(newRegs));

        // Top gói tập
        List<Object[]> topGoiTap = dao.goiTapDangKyNhieuNhat(5);
        String[] colsTop = {"Mã gói", "Lượt đăng ký"};
        Object[][] dataTop = new Object[topGoiTap.size()][2];
        for (int i = 0; i < topGoiTap.size(); i++) {
            dataTop[i][0] = topGoiTap.get(i)[0];
            dataTop[i][1] = topGoiTap.get(i)[1];
        }
        tblTopGoiTap.setModel(new javax.swing.table.DefaultTableModel(dataTop, colsTop));

        // Revenue by type (in selected month): build from ThuPhi panel DAO using a simple query via ThongKeDAO or ThuPhiDAO
        // Tạm: lấy theo khoảng thời gian tháng đã chọn
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.YEAR, year);
            cal.set(java.util.Calendar.MONTH, month - 1);
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            java.util.Date d1 = cal.getTime();
            cal.add(java.util.Calendar.MONTH, 1); cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
            java.util.Date d2 = cal.getTime();

            // ThuPhiDAO đã có getByDateRange, nhưng cần tổng theo loại => dùng getRevenueByType của ThuPhiDAO
            List<Object[]> byType = new dao.ThuPhiDAO().getRevenueByType(d1, d2);
            String[] colsType = {"Loại phí", "Số giao dịch", "Tổng tiền"};
            Object[][] dataType = new Object[byType.size()][3];
            for (int i = 0; i < byType.size(); i++) {
                dataType[i][0] = byType.get(i)[0];
                dataType[i][1] = byType.get(i)[1];
                dataType[i][2] = byType.get(i)[2];
            }
            tblRevenueByType.setModel(new javax.swing.table.DefaultTableModel(dataType, colsType));
        } catch (Exception ex) {
            tblRevenueByType.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Loại phí","Số giao dịch","Tổng tiền"}));
        }
    }
}


