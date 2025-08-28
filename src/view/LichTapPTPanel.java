package view;

import dao.LichTapPTDAO;
import model.LichTapPT;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LichTapPTPanel extends JPanel {

    private final LichTapPTDAO lichTapPTDAO = new LichTapPTDAO();
    private JTable table;

    public LichTapPTPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    private void reload() {
        String[] cols = {"ID", "MaHoiVien", "MaHLV", "NgayTap", "GioBatDau", "GioKetThuc", "TrangThai"};
        try {
            List<LichTapPT> list = lichTapPTDAO.getAll();
            Object[][] data = new Object[list.size()][cols.length];
            for (int i = 0; i < list.size(); i++) {
                LichTapPT lt = list.get(i);
                data[i][0] = lt.getId();
                data[i][1] = lt.getMaHoiVien();
                data[i][2] = lt.getMaHLV();
                data[i][3] = lt.getNgayTap();
                data[i][4] = lt.getGioBatDau();
                data[i][5] = lt.getGioKetThuc();
                data[i][6] = lt.getTrangThai();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols));
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu lịch tập PT. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.WARNING_MESSAGE);
        }
    }
}


