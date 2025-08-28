package view;

import dao.ThuPhiDAO;
import model.ThuPhi;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThuPhiPanel extends JPanel {

    private final ThuPhiDAO dao = new ThuPhiDAO();
    private JTable table;

    public ThuPhiPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    private void reload() {
        String[] cols = {"MaPhieu", "MaHoiVien", "TenHoiVien", "LoaiPhi", "SoTien", "NgayThu", "PhuongThucTT", "NguoiThu", "GhiChu"};
        try {
            List<ThuPhi> list = dao.getAll();
            Object[][] data = new Object[list.size()][cols.length];
            for (int i = 0; i < list.size(); i++) {
                ThuPhi x = list.get(i);
                data[i][0] = x.getMaPhieu();
                data[i][1] = x.getMaHoiVien();
                data[i][2] = x.getTenHoiVien();
                data[i][3] = x.getLoaiPhi();
                data[i][4] = x.getSoTien();
                data[i][5] = x.getNgayThu();
                data[i][6] = x.getPhuongThucTT();
                data[i][7] = x.getNguoiThu();
                data[i][8] = x.getGhiChu();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thu phí. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }
}


