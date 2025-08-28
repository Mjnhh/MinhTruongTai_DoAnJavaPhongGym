package view;

import dao.HoiVienDAO;
import model.HoiVien;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HoiVienPanel extends JPanel {

    private final HoiVienDAO hoiVienDAO = new HoiVienDAO();
    private JTable table;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnSave;
    private JButton btnCancel;
    private JCheckBox chkActiveOnly;

    // Form nhập liệu
    private JTextField txtMa;
    private JTextField txtTen;
    private JComboBox<String> cbGioiTinh;
    private JTextField txtNgaySinh;
    private JTextField txtSDT;
    private JTextField txtDiaChi;
    private JTextField txtEmail;
    private JTextField txtNgayDangKy;
    private JTextField txtMaGoiTap;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JTextField txtMaHLV;
    private JCheckBox chkTrangThai;
    private JTextField txtSoBuoiConLai;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isEditing = false;
    private boolean isNew = false;

    public HoiVienPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout(8, 0));
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        txtSearch = new JTextField(24);
        btnSearch = new JButton("Tìm kiếm");
        btnRefresh = new JButton("Tải lại");
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        actionPanel.add(new JLabel("Từ khóa:"));
        actionPanel.add(txtSearch);
        actionPanel.add(btnSearch);
        actionPanel.add(btnRefresh);
        chkActiveOnly = new JCheckBox("Chỉ hiển thị hoạt động", true);
        actionPanel.add(chkActiveOnly);
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnSave);
        actionPanel.add(btnCancel);
        top.add(actionPanel, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hội viên"));
        add(scrollPane, BorderLayout.CENTER);

        // Form bên phải
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin"));
        txtMa = new JTextField();
        txtTen = new JTextField();
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtNgaySinh = new JTextField();
        txtSDT = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail = new JTextField();
        txtNgayDangKy = new JTextField();
        txtMaGoiTap = new JTextField();
        txtNgayBatDau = new JTextField();
        txtNgayKetThuc = new JTextField();
        txtMaHLV = new JTextField();
        chkTrangThai = new JCheckBox("Hoạt động");
        txtSoBuoiConLai = new JTextField();

        form.add(new JLabel("Mã HV (vd: HV001)"));
        form.add(txtMa);
        form.add(new JLabel("Tên hội viên"));
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
        form.add(new JLabel("Ngày đăng ký (yyyy-MM-dd)"));
        form.add(txtNgayDangKy);
        form.add(new JLabel("Mã gói tập"));
        form.add(txtMaGoiTap);
        form.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd)"));
        form.add(txtNgayBatDau);
        form.add(new JLabel("Ngày kết thúc (yyyy-MM-dd)"));
        form.add(txtNgayKetThuc);
        form.add(new JLabel("Mã HLV"));
        form.add(txtMaHLV);
        form.add(new JLabel("Trạng thái"));
        form.add(chkTrangThai);
        form.add(new JLabel("Số buổi còn lại"));
        form.add(txtSoBuoiConLai);

        add(form, BorderLayout.EAST);

        btnSearch.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> reload());
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> onCancel());

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
        String[] columns = {"MaHoiVien", "TenHoiVien", "GioiTinh", "NgaySinh", "SDT", "DiaChi", "Email", "NgayDangKy", "MaGoiTap", "NgayBatDau", "NgayKetThuc", "MaHLV", "TrangThai", "SoBuoiConLai"};
        try {
            List<HoiVien> list = hoiVienDAO.getAll();
            if (chkActiveOnly.isSelected()) {
                list = list.stream().filter(HoiVien::isTrangThai).toList();
            }
            setTableData(columns, list);
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu hội viên. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void doSearch() {
        String keyword = txtSearch.getText() != null ? txtSearch.getText().trim() : "";
        String[] columns = {"MaHoiVien", "TenHoiVien", "GioiTinh", "NgaySinh", "SDT", "DiaChi", "Email", "NgayDangKy", "MaGoiTap", "NgayBatDau", "NgayKetThuc", "MaHLV", "TrangThai", "SoBuoiConLai"};
        try {
            List<HoiVien> list = keyword.isEmpty() ? hoiVienDAO.getAll() : hoiVienDAO.search(keyword);
            if (chkActiveOnly.isSelected()) {
                list = list.stream().filter(HoiVien::isTrangThai).toList();
            }
            setTableData(columns, list);
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
            JOptionPane.showMessageDialog(this, "Không thể tìm kiếm. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setTableData(String[] columns, List<HoiVien> list) {
        Object[][] data = new Object[list.size()][columns.length];
        for (int i = 0; i < list.size(); i++) {
            HoiVien hv = list.get(i);
            data[i][0] = hv.getMaHoiVien();
            data[i][1] = hv.getTenHoiVien();
            data[i][2] = hv.getGioiTinh();
            data[i][3] = hv.getNgaySinh();
            data[i][4] = hv.getSdt();
            data[i][5] = hv.getDiaChi();
            data[i][6] = hv.getEmail();
            data[i][7] = hv.getNgayDangKy();
            data[i][8] = hv.getMaGoiTap();
            data[i][9] = hv.getNgayBatDau();
            data[i][10] = hv.getNgayKetThuc();
            data[i][11] = hv.getMaHLV();
            data[i][12] = hv.isTrangThai();
            data[i][13] = hv.getSoBuoiConLai();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    // ==== CRUD Handlers ====
    private void onAdd() {
        clearForm();
        try { txtMa.setText(hoiVienDAO.getNextId()); } catch (Exception ignored) {}
        txtNgayDangKy.setText(df.format(new Date()));
        chkTrangThai.setSelected(true);
        isNew = true;
        setEditing(true);
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một dòng để sửa.");
            return;
        }
        fillFormFromTable(row);
        isNew = false;
        setEditing(true);
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một dòng để xóa.");
            return;
        }
        String ma = String.valueOf(table.getValueAt(row, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa (mềm) hội viên " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            if (hoiVienDAO.delete(ma)) {
                reload();
                JOptionPane.showMessageDialog(this, "Đã xóa.");
            } else {
                JOptionPane.showMessageDialog(this, "Không xóa được.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
        }
    }

    private void onSave() {
        HoiVien hv = buildFromForm();
        if (hv == null) return;
        try {
            boolean ok = isNew ? hoiVienDAO.insert(hv) : hoiVienDAO.update(hv);
            if (ok) {
                reload();
                setEditing(false);
                JOptionPane.showMessageDialog(this, "Đã lưu.");
            } else {
                JOptionPane.showMessageDialog(this, "Không lưu được.");
            }
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

        txtMa.setEnabled(false);
        txtTen.setEnabled(editing);
        cbGioiTinh.setEnabled(editing);
        txtNgaySinh.setEnabled(editing);
        txtSDT.setEnabled(editing);
        txtDiaChi.setEnabled(editing);
        txtEmail.setEnabled(editing);
        txtNgayDangKy.setEnabled(editing);
        txtMaGoiTap.setEnabled(editing);
        txtNgayBatDau.setEnabled(editing);
        txtNgayKetThuc.setEnabled(editing);
        txtMaHLV.setEnabled(editing);
        chkTrangThai.setEnabled(editing);
        txtSoBuoiConLai.setEnabled(editing);
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        cbGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtNgayDangKy.setText("");
        txtMaGoiTap.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtMaHLV.setText("");
        chkTrangThai.setSelected(false);
        txtSoBuoiConLai.setText("");
    }

    private void fillFormFromTable(int row) {
        txtMa.setText(String.valueOf(table.getValueAt(row, 0)));
        txtTen.setText(String.valueOf(table.getValueAt(row, 1)));
        String gt = String.valueOf(table.getValueAt(row, 2));
        cbGioiTinh.setSelectedItem(gt != null && gt.equalsIgnoreCase("Nữ") ? "Nữ" : "Nam");
        txtNgaySinh.setText(safeFormatDate(table.getValueAt(row, 3)));
        txtSDT.setText(safeString(table.getValueAt(row, 4)));
        txtDiaChi.setText(safeString(table.getValueAt(row, 5)));
        txtEmail.setText(safeString(table.getValueAt(row, 6)));
        txtNgayDangKy.setText(safeFormatDate(table.getValueAt(row, 7)));
        txtMaGoiTap.setText(safeString(table.getValueAt(row, 8)));
        txtNgayBatDau.setText(safeFormatDate(table.getValueAt(row, 9)));
        txtNgayKetThuc.setText(safeFormatDate(table.getValueAt(row, 10)));
        txtMaHLV.setText(safeString(table.getValueAt(row, 11)));
        Object st = table.getValueAt(row, 12);
        chkTrangThai.setSelected(st instanceof Boolean ? (Boolean) st : "true".equalsIgnoreCase(String.valueOf(st)));
        txtSoBuoiConLai.setText(safeString(table.getValueAt(row, 13)));
    }

    private String safeString(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    private String safeFormatDate(Object v) {
        if (v == null) return "";
        if (v instanceof Date) return df.format((Date) v);
        return String.valueOf(v);
    }

    private Date parseDate(String s) throws ParseException {
        if (s == null || s.trim().isEmpty()) return null;
        return df.parse(s.trim());
    }

    private HoiVien buildFromForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        if (ValidationUtil.isEmpty(ten)) {
            JOptionPane.showMessageDialog(this, "Tên không được để trống");
            return null;
        }
        if (!ValidationUtil.isEmpty(sdt) && !ValidationUtil.isValidPhone(sdt)) {
            JOptionPane.showMessageDialog(this, "SĐT không hợp lệ");
            return null;
        }
        HoiVien hv = new HoiVien();
        hv.setMaHoiVien(ma);
        hv.setTenHoiVien(ten);
        hv.setGioiTinh((String) cbGioiTinh.getSelectedItem());
        try {
            hv.setNgaySinh(parseDate(txtNgaySinh.getText()));
            hv.setNgayDangKy(parseDate(txtNgayDangKy.getText()));
            hv.setNgayBatDau(parseDate(txtNgayBatDau.getText()));
            hv.setNgayKetThuc(parseDate(txtNgayKetThuc.getText()));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày sai (yyyy-MM-dd)");
            return null;
        }
        hv.setSdt(sdt);
        hv.setDiaChi(txtDiaChi.getText().trim());
        hv.setEmail(txtEmail.getText().trim());
        String maGoi = txtMaGoiTap.getText() != null ? txtMaGoiTap.getText().trim() : "";
        hv.setMaGoiTap(ValidationUtil.isEmpty(maGoi) ? null : maGoi);
        String maHlv = txtMaHLV.getText() != null ? txtMaHLV.getText().trim() : "";
        hv.setMaHLV(ValidationUtil.isEmpty(maHlv) ? null : maHlv);
        hv.setTrangThai(chkTrangThai.isSelected());
        try {
            hv.setSoBuoiConLai(ValidationUtil.isEmpty(txtSoBuoiConLai.getText()) ? 0 : Integer.parseInt(txtSoBuoiConLai.getText().trim()));
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Số buổi còn lại phải là số nguyên");
            return null;
        }
        return hv;
    }
}

