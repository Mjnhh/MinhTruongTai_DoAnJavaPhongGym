package view;

import javax.swing.*;

public class MainForm extends JFrame {

    private JTabbedPane tabbedPane;

    public MainForm() {
        setTitle("Quản Lý Phòng Gym");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Hội viên", new HoiVienPanel());
        tabbedPane.addTab("Gói tập", new GoiTapPanel());
        tabbedPane.addTab("Huấn luyện viên", new HLVPanel());
        tabbedPane.addTab("Thiết bị", new ThietBiPanel());
        tabbedPane.addTab("Thu phí & Hóa đơn", new ThuPhiPanel());
        tabbedPane.addTab("Lịch tập PT", new LichTapPTPanel());
        tabbedPane.addTab("Chấm công", new ChamCongPanel());
        tabbedPane.addTab("Thống kê", new ThongKePanel());

        add(tabbedPane);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MainForm().setVisible(true));
    }
}
