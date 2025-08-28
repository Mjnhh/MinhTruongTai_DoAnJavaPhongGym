package view;

import dao.HuanLuyenVienDAO;
import model.HuanLuyenVien;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HLVPanel extends JPanel {

    private final HuanLuyenVienDAO dao = new HuanLuyenVienDAO();
    private JTable table;

    public HLVPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    private void reload() {
        String[] cols = {"MaHLV", "TenHLV", "GioiTinh", "NgaySinh", "SDT", "DiaChi", "Email", "ChuyenMon", "Luong", "NgayVaoLam", "TrangThai"};
        try {
            List<HuanLuyenVien> list = dao.getAll();
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
}


