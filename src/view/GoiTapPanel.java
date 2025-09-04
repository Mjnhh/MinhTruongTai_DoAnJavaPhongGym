        package view;

import dao.GoiTapDAO;
import model.GoiTap;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class GoiTapPanel extends JPanel {

    private final GoiTapDAO goiTapDAO = new GoiTapDAO();
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
    private JTextArea txtMoTa;
    private JTextField txtGiaTien;
    private JTextField txtThoiHan;
    private JTextField txtSoBuoi;
    private JCheckBox chkTrangThai;

    private boolean isEditing = false;
    private boolean isNew = false;

    public GoiTapPanel() {
        setLayout(new BorderLayout());

        // Panel trên - thanh công cụ
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

        // Bảng hiển thị
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách gói tập"));
        add(scrollPane, BorderLayout.CENTER);

        // Form bên phải
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin gói tập"));
        form.setPreferredSize(new Dimension(350, 0));
        
        txtMa = new JTextField();
        txtTen = new JTextField();
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtGiaTien = new JTextField();
        txtThoiHan = new JTextField();
        txtSoBuoi = new JTextField();
        chkTrangThai = new JCheckBox("Hoạt động");

        form.add(new JLabel("Mã gói tập (vd: GT001)"));
        form.add(txtMa);
        form.add(new JLabel("Tên gói tập"));
        form.add(txtTen);
        form.add(new JLabel("Mô tả"));
        form.add(new JScrollPane(txtMoTa));
        form.add(new JLabel("Giá tiền (VNĐ)"));
        form.add(txtGiaTien);
        form.add(new JLabel("Thời hạn (tháng)"));
        form.add(txtThoiHan);
        form.add(new JLabel("Số buổi"));
        form.add(txtSoBuoi);
        form.add(new JLabel("Trạng thái"));
        form.add(chkTrangThai);

        add(form, BorderLayout.EAST);

        // Event handlers
        btnSearch.addActionListener(e -> doSearch());
        btnRefresh.addActionListener(e -> reload());
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> onCancel());
        chkActiveOnly.addActionListener(e -> reload());

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
        String[] columns = {"MaGoiTap", "TenGoiTap", "MoTa", "GiaTien", "ThoiHan", "SoBuoi", "TrangThai"};
        try {
            List<GoiTap> list = goiTapDAO.getAll();
            if (chkActiveOnly.isSelected()) {
                list = list.stream().filter(GoiTap::isTrangThai).toList();
            }
            setTableData(columns, list);
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu gói tập. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void doSearch() {
        String keyword = txtSearch.getText() != null ? txtSearch.getText().trim() : "";
        String[] columns = {"MaGoiTap", "TenGoiTap", "MoTa", "GiaTien", "ThoiHan", "SoBuoi", "TrangThai"};
        try {
            List<GoiTap> list = keyword.isEmpty() ? goiTapDAO.getAll() : goiTapDAO.search(keyword);
            if (chkActiveOnly.isSelected()) {
                list = list.stream().filter(GoiTap::isTrangThai).toList();
            }
            setTableData(columns, list);
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, columns));
            JOptionPane.showMessageDialog(this, "Không thể tìm kiếm. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setTableData(String[] columns, List<GoiTap> list) {
        Object[][] data = new Object[list.size()][columns.length];
        for (int i = 0; i < list.size(); i++) {
            GoiTap gt = list.get(i);
            data[i][0] = gt.getMaGoiTap();
            data[i][1] = gt.getTenGoiTap();
            data[i][2] = gt.getMoTa();
            data[i][3] = gt.getGiaTien();
            data[i][4] = gt.getThoiHan() + " tháng";
            data[i][5] = gt.getSoBuoi() + " buổi";
            data[i][6] = gt.isTrangThai() ? "Hoạt động" : "Ngừng";
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    // ==== CRUD Handlers ====
    private void onAdd() {
        clearForm();
        try { 
            txtMa.setText(goiTapDAO.getNextId()); 
        } catch (Exception ignored) {}
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
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa gói tập " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try {
            if (goiTapDAO.delete(ma)) {
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
        GoiTap gt = buildFromForm();
        if (gt == null) return;
        
        try {
            boolean ok = isNew ? goiTapDAO.insert(gt) : goiTapDAO.update(gt);
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

        txtMa.setEnabled(false); // Mã không cho sửa
        txtTen.setEnabled(editing);
        txtMoTa.setEnabled(editing);
        txtGiaTien.setEnabled(editing);
        txtThoiHan.setEnabled(editing);
        txtSoBuoi.setEnabled(editing);
        chkTrangThai.setEnabled(editing);
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtMoTa.setText("");
        txtGiaTien.setText("");
        txtThoiHan.setText("");
        txtSoBuoi.setText("");
        chkTrangThai.setSelected(false);
    }

    private void fillFormFromTable(int row) {
        txtMa.setText(String.valueOf(table.getValueAt(row, 0)));
        txtTen.setText(String.valueOf(table.getValueAt(row, 1)));
        txtMoTa.setText(safeString(table.getValueAt(row, 2)));
        txtGiaTien.setText(safeString(table.getValueAt(row, 3)));
        
        // Xử lý thời hạn (loại bỏ " tháng")
        String thoiHan = safeString(table.getValueAt(row, 4));
        txtThoiHan.setText(thoiHan.replace(" tháng", ""));
        
        // Xử lý số buổi (loại bỏ " buổi")
        String soBuoi = safeString(table.getValueAt(row, 5));
        txtSoBuoi.setText(soBuoi.replace(" buổi", ""));
        
        String trangThai = safeString(table.getValueAt(row, 6));
        chkTrangThai.setSelected("Hoạt động".equals(trangThai));
    }

    private String safeString(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    private GoiTap buildFromForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String giaTienStr = txtGiaTien.getText().trim();
        String thoiHanStr = txtThoiHan.getText().trim();
        String soBuoiStr = txtSoBuoi.getText().trim();

        // Validation
        if (ValidationUtil.isEmpty(ten)) {
            JOptionPane.showMessageDialog(this, "Tên gói tập không được để trống");
            return null;
        }
        if (ValidationUtil.isEmpty(giaTienStr)) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được để trống");
            return null;
        }
        if (ValidationUtil.isEmpty(thoiHanStr)) {
            JOptionPane.showMessageDialog(this, "Thời hạn không được để trống");
            return null;
        }
        if (ValidationUtil.isEmpty(soBuoiStr)) {
            JOptionPane.showMessageDialog(this, "Số buổi không được để trống");
            return null;
        }

        try {
            BigDecimal giaTien = new BigDecimal(giaTienStr);
            int thoiHan = Integer.parseInt(thoiHanStr);
            int soBuoi = Integer.parseInt(soBuoiStr);

            if (giaTien.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải lớn hơn 0");
                return null;
            }
            if (thoiHan <= 0) {
                JOptionPane.showMessageDialog(this, "Thời hạn phải lớn hơn 0");
                return null;
            }
            if (soBuoi <= 0) {
                JOptionPane.showMessageDialog(this, "Số buổi phải lớn hơn 0");
                return null;
            }

            GoiTap gt = new GoiTap();
            gt.setMaGoiTap(ma);
            gt.setTenGoiTap(ten);
            gt.setMoTa(txtMoTa.getText().trim());
            gt.setGiaTien(giaTien);
            gt.setThoiHan(thoiHan);
            gt.setSoBuoi(soBuoi);
            gt.setTrangThai(chkTrangThai.isSelected());

            return gt;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền, thời hạn và số buổi phải là số hợp lệ");
            return null;
        }
    }
}


