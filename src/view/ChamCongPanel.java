package view;

import dao.ChamCongDAO;
import model.ChamCong;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChamCongPanel extends JPanel {

    private final ChamCongDAO dao = new ChamCongDAO();
    private JTable table;

    public ChamCongPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    private void reload() {
        String[] cols = {"ID", "MaHoiVien", "NgayTap", "GioVao", "GioRa", "GhiChu"};
        try {
            List<ChamCong> list = dao.getToday();
            Object[][] data = new Object[list.size()][cols.length];
            for (int i = 0; i < list.size(); i++) {
                ChamCong x = list.get(i);
                data[i][0] = x.getId();
                data[i][1] = x.getMaHoiVien();
                data[i][2] = x.getNgayTap();
                data[i][3] = x.getGioVao();
                data[i][4] = x.getGioRa();
                data[i][5] = x.getGhiChu();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu chấm công. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }
}


