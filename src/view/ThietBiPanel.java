package view;

import dao.ThietBiDAO;
import model.ThietBi;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThietBiPanel extends JPanel {

    private final ThietBiDAO dao = new ThietBiDAO();
    private JTable table;

    public ThietBiPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    private void reload() {
        String[] cols = {"MaThietBi", "TenThietBi", "LoaiThietBi", "TinhTrang", "NgayMua", "GiaMua", "NgayBaoTriCuoi", "NgayBaoTriTiep", "GhiChu"};
        try {
            List<ThietBi> list = dao.getAll();
            Object[][] data = new Object[list.size()][cols.length];
            for (int i = 0; i < list.size(); i++) {
                ThietBi x = list.get(i);
                data[i][0] = x.getMaThietBi();
                data[i][1] = x.getTenThietBi();
                data[i][2] = x.getLoaiThietBi();
                data[i][3] = x.getTinhTrang();
                data[i][4] = x.getNgayMua();
                data[i][5] = x.getGiaMua();
                data[i][6] = x.getNgayBaoTriCuoi();
                data[i][7] = x.getNgayBaoTriTiep();
                data[i][8] = x.getGhiChu();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thiết bị. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }
}


