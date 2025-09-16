package view;

import dao.ChamCongDAO;
import dao.HoiVienDAO;
import model.ChamCong;
import model.HoiVien;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChamCongPanel extends JPanel {

    private final ChamCongDAO chamCongDAO = new ChamCongDAO();
    private final HoiVienDAO hoiVienDAO = new HoiVienDAO();
    private JTable table;
    private JTextField txtMaHoiVien;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    private JButton btnRefresh;
    private JButton btnSearch;
    private JTextField txtSearch;
    private JLabel lblCurrentTime;
    private JLabel lblTotalToday;
    private JLabel lblCurrentInGym;
    private JTextArea txtWarnings;
    private JTabbedPane tabbedPane;
    private JTable tblNoShow;
    private JTable tblCurrent; // table for "Đang có mặt"
    
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Timer clockTimer;

    public ChamCongPanel() {
        setLayout(new BorderLayout());
        initComponents();
        startClock();
        reload();
        checkExpiryWarnings();
        reloadNoShow();
        loadCurrentMembers();
    }

    private void initComponents() {
        // Panel trên - Check-in/Check-out
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Panel check-in
        JPanel checkInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkInPanel.setBorder(BorderFactory.createTitledBorder("Check-in / Check-out"));
        
        checkInPanel.add(new JLabel("Mã hội viên:"));
        txtMaHoiVien = new JTextField(15);
        txtMaHoiVien.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        checkInPanel.add(txtMaHoiVien);
        
        btnCheckIn = new JButton("Check-in");
        btnCheckIn.setBackground(new Color(46, 125, 50));
        btnCheckIn.setForeground(Color.WHITE);
        btnCheckIn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        checkInPanel.add(btnCheckIn);
        
        btnCheckOut = new JButton("Check-out");
        btnCheckOut.setBackground(new Color(211, 47, 47));
        btnCheckOut.setForeground(Color.WHITE);
        btnCheckOut.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        checkInPanel.add(btnCheckOut);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblCurrentTime = new JLabel();
        lblCurrentTime.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        lblCurrentTime.setForeground(new Color(33, 150, 243));
        infoPanel.add(new JLabel("Thời gian hiện tại: "));
        infoPanel.add(lblCurrentTime);
        
        topPanel.add(checkInPanel, BorderLayout.WEST);
        topPanel.add(infoPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Tabbed pane chính
        tabbedPane = new JTabbedPane();
        
        // Tab 1: Chấm công hôm nay
        JPanel todayPanel = createTodayPanel();
        tabbedPane.addTab("Hôm nay", todayPanel);
        
        // Tab 2: Đang có mặt
        JPanel currentPanel = createCurrentPanel();
        tabbedPane.addTab("Đang có mặt", currentPanel);
        
        // Tab 3: Cảnh báo
        JPanel warningPanel = createWarningPanel();
        tabbedPane.addTab("Cảnh báo", warningPanel);
        
        // Tab 4: No-Show
        JPanel noshowPanel = createNoShowPanel();
        tabbedPane.addTab("Vắng (No-Show)", noshowPanel);
        
        add(tabbedPane, BorderLayout.CENTER);

        // Event handlers
        btnCheckIn.addActionListener(e -> doCheckIn());
        btnCheckOut.addActionListener(e -> doCheckOut());
        btnRefresh.addActionListener(e -> { reload(); reloadNoShow(); loadCurrentMembers(); });
        
        // Enter key cho check-in nhanh
        txtMaHoiVien.addActionListener(e -> doCheckIn());
    }

    private JPanel createTodayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Thanh công cụ
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        btnRefresh = new JButton("Tải lại");
        lblTotalToday = new JLabel();
        lblTotalToday.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        toolbar.add(new JLabel("Tìm kiếm:"));
        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnRefresh);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(lblTotalToday);
        
        panel.add(toolbar, BorderLayout.NORTH);
        
        // Bảng chấm công
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách chấm công hôm nay"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        btnSearch.addActionListener(e -> doSearch());
        
        return panel;
    }

    private JPanel createCurrentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        tblCurrent = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblCurrent);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hội viên đang có mặt trong gym"));
        
        lblCurrentInGym = new JLabel();
        lblCurrentInGym.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        lblCurrentInGym.setForeground(new Color(76, 175, 80));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(lblCurrentInGym);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Initial load
        loadCurrentMembers();
        
        return panel;
    }

    private JPanel createWarningPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        txtWarnings = new JTextArea(15, 50);
        txtWarnings.setEditable(false);
        txtWarnings.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtWarnings.setBackground(new Color(255, 248, 225));
        
        JScrollPane scrollPane = new JScrollPane(txtWarnings);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cảnh báo hội viên sắp hết hạn"));
        
        JButton btnRefreshWarnings = new JButton("Cập nhật cảnh báo");
        btnRefreshWarnings.addActionListener(e -> checkExpiryWarnings());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnRefreshWarnings);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createNoShowPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tblNoShow = new JTable();
        JScrollPane sp = new JScrollPane(tblNoShow);
        sp.setBorder(BorderFactory.createTitledBorder("Lịch PT hôm nay không điểm danh"));
        JButton btnReloadNoShow = new JButton("Tải lại danh sách vắng");
        btnReloadNoShow.addActionListener(e -> reloadNoShow());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(btnReloadNoShow);
        panel.add(top, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    private void reloadNoShow() {
        try {
            List<Object[]> ns = chamCongDAO.getNoShowByDate(new Date());
            String[] cols = {"Mã HV", "Mã HLV", "Giờ bắt đầu", "Giờ kết thúc"};
            Object[][] data = new Object[ns.size()][cols.length];
            for (int i = 0; i < ns.size(); i++) {
                Object[] r = ns.get(i);
                data[i][0] = r[0];
                data[i][1] = r[1];
                data[i][2] = r[2];
                data[i][3] = r[3];
            }
            tblNoShow.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            tblNoShow.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Mã HV","Mã HLV","Giờ bắt đầu","Giờ kết thúc"}));
        }
    }

    private void loadCurrentMembers() {
        try {
            List<ChamCong> currentMembers = chamCongDAO.getCurrentMembersInGym();
            lblCurrentInGym.setText("Hiện có " + currentMembers.size() + " hội viên đang tập");
            String[] columns = {"Mã HV", "Tên hội viên", "Giờ vào", "Thời gian tập"};
            Object[][] data = new Object[currentMembers.size()][columns.length];
            for (int i = 0; i < currentMembers.size(); i++) {
                ChamCong cc = currentMembers.get(i);
                data[i][0] = cc.getMaHoiVien();
                data[i][1] = cc.getTenHoiVien();
                data[i][2] = cc.getGioVao() != null ? cc.getGioVao().toString() : "";
                long diffMs = cc.getGioVao() != null ? System.currentTimeMillis() - cc.getGioVao().getTime() : 0L;
                long hours = diffMs / (1000 * 60 * 60);
                long minutes = (diffMs % (1000 * 60 * 60)) / (1000 * 60);
                data[i][3] = hours + "h " + minutes + "m";
            }
            if (tblCurrent != null) {
                tblCurrent.setModel(new javax.swing.table.DefaultTableModel(data, columns));
            }
        } catch (Exception ex) {
            if (tblCurrent != null) {
                tblCurrent.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Mã HV","Tên hội viên","Giờ vào","Thời gian tập"}));
            }
            lblCurrentInGym.setText("Lỗi tải dữ liệu");
        }
    }

    private void startClock() {
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    lblCurrentTime.setText(timeFormat.format(new Date()));
                });
            }
        }, 0, 1000); // Cập nhật mỗi giây
    }

    private void doCheckIn() {
        String maHoiVien = txtMaHoiVien.getText().trim().toUpperCase();
        if (ValidationUtil.isEmpty(maHoiVien)) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập mã hội viên!\nVí dụ: HV001, HV002...", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtMaHoiVien.requestFocus();
            return;
        }

        // Validation định dạng mã hội viên
        if (!maHoiVien.matches("^HV\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "❌ Mã hội viên không đúng định dạng!\nVui lòng nhập theo định dạng: HV001, HV002...", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtMaHoiVien.selectAll();
            txtMaHoiVien.requestFocus();
            return;
        }

        try {
            // Kiểm tra hội viên có tồn tại không
            HoiVien hoiVien = hoiVienDAO.getById(maHoiVien);
            if (hoiVien == null) {
                JOptionPane.showMessageDialog(this, "❌ Không tìm thấy hội viên với mã: " + maHoiVien + "\nVui lòng kiểm tra lại mã hội viên.", "Lỗi tìm kiếm", JOptionPane.ERROR_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Kiểm tra trạng thái hội viên
            if (!hoiVien.isTrangThai()) {
                JOptionPane.showMessageDialog(this, "⚠️ Hội viên " + hoiVien.getTenHoiVien() + " đã bị vô hiệu hóa!\nVui lòng liên hệ quản trị viên.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Kiểm tra đã check-in hôm nay chưa
            if (chamCongDAO.isCheckedInToday(maHoiVien)) {
                if (!chamCongDAO.isCheckedOutToday(maHoiVien)) {
                    JOptionPane.showMessageDialog(this, "ℹ️ " + hoiVien.getTenHoiVien() + " đã check-in rồi và chưa check-out!\nVui lòng check-out trước khi check-in lại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "ℹ️ " + hoiVien.getTenHoiVien() + " đã check-in và check-out hôm nay rồi!\nMỗi hội viên chỉ được check-in 1 lần/ngày.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Kiểm tra gói tập còn hạn không
            if (hoiVien.getNgayKetThuc() != null && hoiVien.getNgayKetThuc().before(new Date())) {
                JOptionPane.showMessageDialog(this, "⚠️ Gói tập của " + hoiVien.getTenHoiVien() + " đã hết hạn từ " + dateFormat.format(hoiVien.getNgayKetThuc()) + "!\nVui lòng gia hạn gói tập.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Kiểm tra số buổi còn lại
            if (hoiVien.getSoBuoiConLai() <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ " + hoiVien.getTenHoiVien() + " đã hết số buổi tập!\nSố buổi còn lại: " + hoiVien.getSoBuoiConLai() + "\nVui lòng mua thêm buổi hoặc gia hạn gói.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Thực hiện check-in
            if (chamCongDAO.checkIn(maHoiVien)) {
                JOptionPane.showMessageDialog(this, "✅ Check-in thành công!\nHội viên: " + hoiVien.getTenHoiVien() + "\nSố buổi còn lại: " + (hoiVien.getSoBuoiConLai() - 1), "Thành công", JOptionPane.INFORMATION_MESSAGE);
                txtMaHoiVien.setText("");
                reload();
                loadCurrentMembers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Check-in thất bại!\nVui lòng thử lại sau.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi check-in: " + ex.getMessage() + "\nVui lòng thử lại sau.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doCheckOut() {
        String maHoiVien = txtMaHoiVien.getText().trim().toUpperCase();
        if (ValidationUtil.isEmpty(maHoiVien)) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập mã hội viên!\nVí dụ: HV001, HV002...", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtMaHoiVien.requestFocus();
            return;
        }

        // Validation định dạng mã hội viên
        if (!maHoiVien.matches("^HV\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "❌ Mã hội viên không đúng định dạng!\nVui lòng nhập theo định dạng: HV001, HV002...", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtMaHoiVien.selectAll();
            txtMaHoiVien.requestFocus();
            return;
        }

        try {
            // Kiểm tra hội viên có tồn tại không
            HoiVien hoiVien = hoiVienDAO.getById(maHoiVien);
            if (hoiVien == null) {
                JOptionPane.showMessageDialog(this, "❌ Không tìm thấy hội viên với mã: " + maHoiVien + "\nVui lòng kiểm tra lại mã hội viên.", "Lỗi tìm kiếm", JOptionPane.ERROR_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Kiểm tra đã check-in hôm nay chưa
            if (!chamCongDAO.isCheckedInToday(maHoiVien)) {
                JOptionPane.showMessageDialog(this, "⚠️ " + hoiVien.getTenHoiVien() + " chưa check-in hôm nay!\nVui lòng check-in trước khi check-out.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Kiểm tra đã check-out chưa
            if (chamCongDAO.isCheckedOutToday(maHoiVien)) {
                JOptionPane.showMessageDialog(this, "ℹ️ " + hoiVien.getTenHoiVien() + " đã check-out rồi!\nMỗi hội viên chỉ được check-out 1 lần/ngày.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtMaHoiVien.selectAll();
                txtMaHoiVien.requestFocus();
                return;
            }

            // Thực hiện check-out
            if (chamCongDAO.checkOut(maHoiVien)) {
                JOptionPane.showMessageDialog(this, "✅ Check-out thành công!\nHội viên: " + hoiVien.getTenHoiVien() + "\nCảm ơn bạn đã tập luyện!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                txtMaHoiVien.setText("");
                reload();
                loadCurrentMembers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Check-out thất bại!\nVui lòng thử lại sau.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi check-out: " + ex.getMessage() + "\nVui lòng thử lại sau.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doSearch() {
        String keyword = txtSearch.getText().trim();
        String[] columns = {"ID", "Mã HV", "Tên hội viên", "Ngày tập", "Giờ vào", "Giờ ra", "Trạng thái", "Ghi chú"};
        
        try {
            List<ChamCong> list = keyword.isEmpty() ? chamCongDAO.getToday() : chamCongDAO.searchByMemberName(keyword);
            setTableData(columns, list);
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reload() {
        String[] columns = {"ID", "Mã HV", "Tên hội viên", "Ngày tập", "Giờ vào", "Giờ ra", "Trạng thái", "Ghi chú"};
        try {
            List<ChamCong> list = chamCongDAO.getToday();
            setTableData(columns, list);
            lblTotalToday.setText("Tổng: " + list.size() + " lượt");
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
            lblTotalToday.setText("Tổng: 0 lượt");
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu chấm công. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setTableData(String[] columns, List<ChamCong> list) {
        Object[][] data = new Object[list.size()][columns.length];
        for (int i = 0; i < list.size(); i++) {
            ChamCong cc = list.get(i);
            data[i][0] = cc.getId();
            data[i][1] = cc.getMaHoiVien();
            data[i][2] = cc.getTenHoiVien();
            data[i][3] = dateFormat.format(cc.getNgayTap());
            data[i][4] = cc.getGioVao() != null ? cc.getGioVao().toString() : "";
            data[i][5] = cc.getGioRa() != null ? cc.getGioRa().toString() : "";
            data[i][6] = cc.getGioRa() != null ? "Đã ra" : "Đang tập";
            data[i][7] = cc.getGhiChu();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        
        // Tô màu dòng
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = String.valueOf(table.getValueAt(row, 6));
                    if ("Đang tập".equals(status)) {
                        c.setBackground(new Color(232, 245, 233)); // Xanh nhạt
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
    }

    private void checkExpiryWarnings() {
        try {
            List<HoiVien> allMembers = hoiVienDAO.getAll();
            StringBuilder warnings = new StringBuilder();
            warnings.append("=== CẢNH BÁO HỘI VIÊN SẮP HẾT HẠN ===\n");
            warnings.append("Ngày kiểm tra: ").append(dateFormat.format(new Date())).append("\n\n");
            
            int expiredCount = 0;
            int nearExpiryCount = 0;
            
            for (HoiVien hv : allMembers) {
                if (hv.getNgayKetThuc() != null) {
                    Date today = new Date();
                    long diffMs = hv.getNgayKetThuc().getTime() - today.getTime();
                    long days = diffMs / (24 * 60 * 60 * 1000);
                    
                    if (days < 0) {
                        // Đã hết hạn
                        expiredCount++;
                        warnings.append("❌ HẾT HẠN: ").append(hv.getMaHoiVien())
                                .append(" - ").append(hv.getTenHoiVien())
                                .append(" (hết hạn ").append(Math.abs(days)).append(" ngày trước)\n");
                    } else if (days <= 7) {
                        // Sắp hết hạn trong 7 ngày
                        nearExpiryCount++;
                        warnings.append("⚠️  SẮP HẾT HẠN: ").append(hv.getMaHoiVien())
                                .append(" - ").append(hv.getTenHoiVien())
                                .append(" (còn ").append(days).append(" ngày)\n");
                    }
                }
            }
            
            warnings.append("\n=== TỔNG KẾT ===\n");
            warnings.append("Đã hết hạn: ").append(expiredCount).append(" hội viên\n");
            warnings.append("Sắp hết hạn (≤7 ngày): ").append(nearExpiryCount).append(" hội viên\n");
            
            if (expiredCount == 0 && nearExpiryCount == 0) {
                warnings.append("\n✅ Tất cả hội viên đều còn hạn sử dụng!");
            }
            
            txtWarnings.setText(warnings.toString());
            txtWarnings.setCaretPosition(0);
            
        } catch (Exception ex) {
            txtWarnings.setText("Lỗi khi kiểm tra cảnh báo: " + ex.getMessage());
        }
    }

    // Cleanup timer khi panel bị destroy
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (clockTimer != null) {
            clockTimer.cancel();
        }
    }
}






