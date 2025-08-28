package view;

import dao.GoiTapDAO;
import model.GoiTap;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GoiTapPanel extends JPanel {

    private final GoiTapDAO goiTapDAO = new GoiTapDAO();
    private JTable table;

    public GoiTapPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        reload();
    }

    private void reload() {
        List<GoiTap> list = goiTapDAO.getAll();
        String[] cols = {"MaGoiTap", "TenGoiTap", "MoTa", "GiaTien", "ThoiHan", "SoBuoi", "TrangThai"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            GoiTap gt = list.get(i);
            data[i][0] = gt.getMaGoiTap();
            data[i][1] = gt.getTenGoiTap();
            data[i][2] = gt.getMoTa();
            data[i][3] = gt.getGiaTien();
            data[i][4] = gt.getThoiHan();
            data[i][5] = gt.getSoBuoi();
            data[i][6] = gt.isTrangThai();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }
}


