package view;

import dao.ThongKeDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThongKePanel extends JPanel {

    private final ThongKeDAO dao = new ThongKeDAO();

    public ThongKePanel() {
        setLayout(new BorderLayout());

        JPanel content = new JPanel(new GridLayout(0, 1, 8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        Object[] hv = dao.thongKeHoiVien();
        content.add(new JLabel("Tổng hội viên: " + hv[0]));
        content.add(new JLabel("Đang hoạt động: " + hv[1]));
        content.add(new JLabel("Hết hạn: " + hv[2]));

        Object[] rev = dao.doanhThuTheoThang(java.time.LocalDate.now().getMonthValue(), java.time.LocalDate.now().getYear());
        content.add(new JLabel("Doanh thu tháng hiện tại: " + rev[0] + " (" + rev[1] + " giao dịch)"));

        List<Object[]> topGoiTap = dao.goiTapDangKyNhieuNhat(5);
        StringBuilder sb = new StringBuilder("Gói tập phổ biến: ");
        for (int i = 0; i < topGoiTap.size(); i++) {
            Object[] row = topGoiTap.get(i);
            if (i > 0) sb.append("; ");
            sb.append(row[0]).append(" (" + row[1] + ")");
        }
        content.add(new JLabel(sb.toString()));

        add(new JScrollPane(content), BorderLayout.CENTER);
    }
}


