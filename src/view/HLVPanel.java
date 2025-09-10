package view;

import dao.HuanLuyenVienDAO;
import model.HuanLuyenVien;
import util.ValidationUtil;
import util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HLVPanel extends JPanel {

    private final HuanLuyenVienDAO dao = new HuanLuyenVienDAO();
    private JTable table;
    private JTextField txtSearch;
    private JButton btnSearch, btnRefresh, btnAdd, btnEdit, btnDelete, btnSave, btnCancel;
    private JCheckBox chkActiveOnly;
    private JButton btnAssignMember, btnListMembers;

    // Form
    private JTextField txtMa, txtTen, txtNgaySinh, txtSDT, txtDiaChi, txtEmail, txtChuyenMon, txtLuong, txtNgayVaoLam;
    private JComboBox<String> cbGioiTinh;
    private JCheckBox chkTrangThai;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isEditing = false;
    private boolean isNew = false;

    public HLVPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout(8, 0));
        JPanel action = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        txtSearch = new JTextField(24);
        btnSearch = new JButton("Tìm kiếm");
        btnRefresh = new JButton("Tải lại");
        chkActiveOnly = new JCheckBox("Chỉ hoạt động", true);
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        btnAssignMember = new JButton("Phân công HV");
        btnListMembers = new JButton("Danh sách HV của HLV");
        action.add(new JLabel("Từ khóa:"));
        action.add(txtSearch);
        action.add(btnSearch);
        action.add(btnRefresh);
        action.add(chkActiveOnly);
        action.add(btnAdd);
        action.add(btnEdit);
        action.add(btnDelete);
        action.add(btnSave);
        action.add(btnCancel);
        action.add(btnAssignMember);
        action.add(btnListMembers);
        top.add(action, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin HLV"));
        form.setPreferredSize(new Dimension(360, 0));
        txtMa = new JTextField();
        txtTen = new JTextField();
        cbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});
        txtNgaySinh = new JTextField();
        txtSDT = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail = new JTextField();
        txtChuyenMon = new JTextField();
        txtLuong = new JTextField();
        txtNgayVaoLam = new JTextField();
        chkTrangThai = new JCheckBox("Đang làm việc");

        form.add(new JLabel("Mã HLV (vd: HLV001)"));
        form.add(txtMa);
        form.add(new JLabel("Tên HLV"));
        form.add(txtTen);
        form.add(new JLabel("Giới tính"));
        form.add(cbGioiTinh);
        form.add(new JLabel("Ngày sinh (yyyy-MM-dd)"));
        form.add(txtNgaySinh);
        form.add(new JLabel("SĐT"));
        form.add(txtSDT);
        form.add(new JLabel("Địa chỉ"));
        form.add(txtDiaChi);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Chuyên môn"));
        form.add(txtChuyenMon);
        form.add(new JLabel("Lương (VNĐ)"));
        form.add(txtLuong);
        form.add(new JLabel("Ngày vào làm (yyyy-MM-dd)"));
        form.add(txtNgayVaoLam);
        form.add(new JLabel("Trạng thái"));
        form.add(chkTrangThai);
        add(form, BorderLayout.EAST);

        btnSearch.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> reload());
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> onCancel());
        chkActiveOnly.addActionListener(e -> reload());
        btnAssignMember.addActionListener(e -> onAssignMember());
        btnListMembers.addActionListener(e -> onListMembers());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) fillFormFromTable(row);
            }
        });

        setEditing(false);
        reload();
    }

    private void reload() {
        String[] cols = {"MaHLV", "TenHLV", "GioiTinh", "NgaySinh", "SDT", "DiaChi", "Email", "ChuyenMon", "Luong", "NgayVaoLam", "TrangThai"};
        try {
            List<HuanLuyenVien> list = dao.getAll();
            if (chkActiveOnly.isSelected()) {
                list = list.stream().filter(HuanLuyenVien::isTrangThai).toList();
            }
            Object[][] data = new Object[list.size()][cols.length];
            for (int i = 0; i < list.size(); i++) {
                HuanLuyenVien x = list.get(i);
                data[i][0] = x.getMaHLV();
                data[i][1] = x.getTenHLV();
                data[i][2] = x.getGioiTinh();
                data[i][3] = x.getNgaySinh();
                data[i][4] = x.getSdt();
                data[i][5] = x.getDiaChi();
                data[i][6] = x.getEmail();
                data[i][7] = x.getChuyenMon();
                data[i][8] = x.getLuong();
                data[i][9] = x.getNgayVaoLam();
                data[i][10] = x.isTrangThai();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu HLV. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void doSearch() {
        String keyword = txtSearch.getText() != null ? txtSearch.getText().trim() : "";
        String[] cols = {"MaHLV", "TenHLV", "GioiTinh", "NgaySinh", "SDT", "DiaChi", "Email", "ChuyenMon", "Luong", "NgayVaoLam", "TrangThai"};
        try {
            List<HuanLuyenVien> list = keyword.isEmpty() ? dao.getAll() : dao.search(keyword);
            if (chkActiveOnly.isSelected()) {
                list = list.stream().filter(HuanLuyenVien::isTrangThai).toList();
            }
            Object[][] data = new Object[list.size()][cols.length];
            for (int i = 0; i < list.size(); i++) {
                HuanLuyenVien x = list.get(i);
                data[i][0] = x.getMaHLV();
                data[i][1] = x.getTenHLV();
                data[i][2] = x.getGioiTinh();
                data[i][3] = x.getNgaySinh();
                data[i][4] = x.getSdt();
                data[i][5] = x.getDiaChi();
                data[i][6] = x.getEmail();
                data[i][7] = x.getChuyenMon();
                data[i][8] = x.getLuong();
                data[i][9] = x.getNgayVaoLam();
                data[i][10] = x.isTrangThai();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols));
            JOptionPane.showMessageDialog(this, "Không thể tìm kiếm.", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onAdd() {
        clearForm();
        try { txtMa.setText(dao.getNextId()); } catch (Exception ignored) {}
        chkTrangThai.setSelected(true);
        isNew = true;
        setEditing(true);
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn một dòng để sửa."); return; }
        fillFormFromTable(row);
        isNew = false;
        setEditing(true);
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn một dòng để xóa."); return; }
        String ma = String.valueOf(table.getValueAt(row, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa (mềm) HLV " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            if (dao.delete(ma)) { reload(); JOptionPane.showMessageDialog(this, "Đã xóa."); }
            else JOptionPane.showMessageDialog(this, "Không xóa được.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
        }
    }

    private void onSave() {
        HuanLuyenVien hlv = buildFromForm();
        if (hlv == null) return;
        try {
            boolean ok = isNew ? dao.insert(hlv) : dao.update(hlv);
            if (ok) { reload(); setEditing(false); JOptionPane.showMessageDialog(this, "Đã lưu."); }
            else JOptionPane.showMessageDialog(this, "Không lưu được.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu: " + ex.getMessage());
        }
    }

    private void onCancel() {
        setEditing(false);
        clearForm();
    }

    private void setEditing(boolean editing) {
        this.isEditing = editing;
        btnSave.setEnabled(editing);
        btnCancel.setEnabled(editing);
        btnAdd.setEnabled(!editing);
        btnEdit.setEnabled(!editing);
        btnDelete.setEnabled(!editing);
        btnSearch.setEnabled(!editing);
        btnRefresh.setEnabled(!editing);
        btnAssignMember.setEnabled(!editing);
        btnListMembers.setEnabled(!editing);

        txtMa.setEnabled(false);
        txtTen.setEnabled(editing);
        cbGioiTinh.setEnabled(editing);
        txtNgaySinh.setEnabled(editing);
        txtSDT.setEnabled(editing);
        txtDiaChi.setEnabled(editing);
        txtEmail.setEnabled(editing);
        txtChuyenMon.setEnabled(editing);
        txtLuong.setEnabled(editing);
        txtNgayVaoLam.setEnabled(editing);
        chkTrangThai.setEnabled(editing);
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        cbGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtChuyenMon.setText("");
        txtLuong.setText("");
        txtNgayVaoLam.setText("");
        chkTrangThai.setSelected(false);
    }

    private void fillFormFromTable(int row) {
        txtMa.setText(String.valueOf(table.getValueAt(row, 0)));
        txtTen.setText(String.valueOf(table.getValueAt(row, 1)));
        String gt = String.valueOf(table.getValueAt(row, 2));
        cbGioiTinh.setSelectedItem(gt != null && gt.equalsIgnoreCase("Nữ") ? "Nữ" : "Nam");
        txtNgaySinh.setText(safeDate(table.getValueAt(row, 3)));
        txtSDT.setText(safeString(table.getValueAt(row, 4)));
        txtDiaChi.setText(safeString(table.getValueAt(row, 5)));
        txtEmail.setText(safeString(table.getValueAt(row, 6)));
        txtChuyenMon.setText(safeString(table.getValueAt(row, 7)));
        txtLuong.setText(safeString(table.getValueAt(row, 8)));
        txtNgayVaoLam.setText(safeDate(table.getValueAt(row, 9)));
        Object st = table.getValueAt(row, 10);
        chkTrangThai.setSelected(st instanceof Boolean ? (Boolean) st : "true".equalsIgnoreCase(String.valueOf(st)));
    }

    private void onAssignMember() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn một HLV."); return; }
        String maHLV = String.valueOf(table.getValueAt(row, 0));
        String maHV = JOptionPane.showInputDialog(this, "Nhập mã hội viên cần phân công (vd: HV001)");
        if (ValidationUtil.isEmpty(maHV)) return;
        try {
            // đơn giản: cập nhật trực tiếp trường MaHLV của hội viên
            DatabaseConnection.getConnection().createStatement().executeUpdate(
                "UPDATE HoiVien SET MaHLV='" + maHLV.replace("'","''") + "' WHERE MaHoiVien='" + maHV.replace("'","''") + "'");
            JOptionPane.showMessageDialog(this, "Đã phân công HLV " + maHLV + " cho hội viên " + maHV);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi phân công: " + ex.getMessage());
        }
    }

    private void onListMembers() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn một HLV."); return; }
        String maHLV = String.valueOf(table.getValueAt(row, 0));
        try {
            java.sql.Connection c = DatabaseConnection.getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement("SELECT MaHoiVien, TenHoiVien FROM HoiVien WHERE MaHLV = ? AND TrangThai = 1 ORDER BY MaHoiVien");
            ps.setString(1, maHLV);
            java.sql.ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder("Hội viên của ").append(maHLV).append(':').append('\n');
            while (rs.next()) sb.append(rs.getString(1)).append(" - ").append(rs.getString(2)).append('\n');
            JTextArea ta = new JTextArea(sb.toString(), 15, 40);
            ta.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Danh sách hội viên", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xem danh sách: " + ex.getMessage());
        }
    }

    private String safeString(Object v) { return v == null ? "" : String.valueOf(v); }
    private String safeDate(Object v) { if (v == null) return ""; if (v instanceof Date) return df.format((Date) v); return String.valueOf(v); }

    private HuanLuyenVien buildFromForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String luongStr = txtLuong.getText().trim();
        if (ValidationUtil.isEmpty(ten)) { JOptionPane.showMessageDialog(this, "Tên không được để trống"); return null; }
        if (!ValidationUtil.isEmpty(sdt) && !ValidationUtil.isValidPhone(sdt)) { JOptionPane.showMessageDialog(this, "SĐT không hợp lệ"); return null; }
        BigDecimal luong = null;
        if (!ValidationUtil.isEmpty(luongStr)) {
            try { luong = new BigDecimal(luongStr); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Lương không hợp lệ"); return null; }
        }
        HuanLuyenVien hlv = new HuanLuyenVien();
        hlv.setMaHLV(ma);
        hlv.setTenHLV(ten);
        hlv.setGioiTinh((String) cbGioiTinh.getSelectedItem());
        try {
            hlv.setNgaySinh(parseDate(txtNgaySinh.getText()));
            hlv.setNgayVaoLam(parseDate(txtNgayVaoLam.getText()));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Sai định dạng ngày (yyyy-MM-dd)");
            return null;
        }
        hlv.setSdt(sdt);
        hlv.setDiaChi(txtDiaChi.getText().trim());
        hlv.setEmail(txtEmail.getText().trim());
        hlv.setChuyenMon(txtChuyenMon.getText().trim());
        hlv.setLuong(luong);
        hlv.setTrangThai(chkTrangThai.isSelected());
        return hlv;
    }

    private Date parseDate(String s) throws ParseException { if (s == null || s.trim().isEmpty()) return null; return df.parse(s.trim()); }
}


