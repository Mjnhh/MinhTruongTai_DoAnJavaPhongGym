package view;

import dao.ThuPhiDAO;
import model.ThuPhi;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ThuPhiPanel extends JPanel {

    private final ThuPhiDAO dao = new ThuPhiDAO();
    private JTable table;
    private JTextField txtSearch; private JButton btnSearch, btnRefresh, btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnFilter;
    private JButton btnExportCsv;
    private JButton btnPrint;

    // Form
    private JTextField txtMaPhieu, txtMaHoiVien, txtLoaiPhi, txtSoTien, txtNgayThu, txtPTTT, txtNguoiThu;
    private JTextArea txtGhiChu;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isEditing=false, isNew=false;

    public ThuPhiPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout(8, 0));
        JPanel action = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        txtSearch = new JTextField(24);
        btnSearch = new JButton("Tìm kiếm"); btnRefresh = new JButton("Tải lại");
        btnAdd = new JButton("Thêm"); btnEdit = new JButton("Sửa"); btnDelete = new JButton("Xóa"); btnSave = new JButton("Lưu"); btnCancel = new JButton("Hủy"); btnFilter = new JButton("Lọc theo ngày");
        btnExportCsv = new JButton("Xuất CSV");
        btnPrint = new JButton("In hóa đơn");
        action.add(new JLabel("Từ khóa:")); action.add(txtSearch); action.add(btnSearch); action.add(btnRefresh); action.add(btnAdd); action.add(btnEdit); action.add(btnDelete); action.add(btnSave); action.add(btnCancel); action.add(btnFilter); action.add(btnExportCsv); action.add(btnPrint);
        top.add(action, BorderLayout.WEST); add(top, BorderLayout.NORTH);

        table = new JTable(); add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6)); form.setBorder(BorderFactory.createTitledBorder("Phiếu thu")); form.setPreferredSize(new Dimension(380, 0));
        txtMaPhieu=new JTextField(); txtMaHoiVien=new JTextField(); txtLoaiPhi=new JTextField(); txtSoTien=new JTextField(); txtNgayThu=new JTextField(); txtPTTT=new JTextField(); txtNguoiThu=new JTextField(); txtGhiChu=new JTextArea(3,20); txtGhiChu.setLineWrap(true); txtGhiChu.setWrapStyleWord(true);
        form.add(new JLabel("Mã phiếu (vd: TP001)")); form.add(txtMaPhieu);
        form.add(new JLabel("Mã hội viên")); form.add(txtMaHoiVien);
        form.add(new JLabel("Loại phí")); form.add(txtLoaiPhi);
        form.add(new JLabel("Số tiền (VNĐ)")); form.add(txtSoTien);
        form.add(new JLabel("Ngày thu (yyyy-MM-dd)")); form.add(txtNgayThu);
        form.add(new JLabel("Phương thức TT")); form.add(txtPTTT);
        form.add(new JLabel("Người thu")); form.add(txtNguoiThu);
        form.add(new JLabel("Ghi chú")); form.add(new JScrollPane(txtGhiChu));
        add(form, BorderLayout.EAST);

        btnSearch.addActionListener(e->doSearch()); btnRefresh.addActionListener(e->reload());
        btnAdd.addActionListener(e->onAdd()); btnEdit.addActionListener(e->onEdit()); btnDelete.addActionListener(e->onDelete()); btnSave.addActionListener(e->onSave()); btnCancel.addActionListener(e->onCancel()); btnFilter.addActionListener(e->onFilter());
        btnExportCsv.addActionListener(e->onExportCsv());
        btnPrint.addActionListener(e->onPrint());
        table.addMouseListener(new MouseAdapter(){ @Override public void mouseClicked(MouseEvent e){ int r=table.getSelectedRow(); if(r>=0) fillFormFromTable(r);} });

        setEditing(false); reload();
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

    private void doSearch(){ String kw = txtSearch.getText()!=null?txtSearch.getText().trim( ):""; String[] cols={"MaPhieu","MaHoiVien","TenHoiVien","LoaiPhi","SoTien","NgayThu","PhuongThucTT","NguoiThu","GhiChu"}; try{ List<ThuPhi> list = dao.search(kw); Object[][] data=new Object[list.size()][cols.length]; for(int i=0;i<list.size();i++){ ThuPhi x=list.get(i); data[i][0]=x.getMaPhieu(); data[i][1]=x.getMaHoiVien(); data[i][2]=x.getTenHoiVien(); data[i][3]=x.getLoaiPhi(); data[i][4]=x.getSoTien(); data[i][5]=x.getNgayThu(); data[i][6]=x.getPhuongThucTT(); data[i][7]=x.getNguoiThu(); data[i][8]=x.getGhiChu(); } table.setModel(new javax.swing.table.DefaultTableModel(data, cols)); } catch(Exception ex){ table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols)); }}

    private void onAdd(){ clearForm(); try{ txtMaPhieu.setText(dao.getNextId()); }catch(Exception ignored){} txtNgayThu.setText(df.format(new Date())); isNew=true; setEditing(true);}    
    private void onEdit(){ int r=table.getSelectedRow(); if(r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng để sửa."); return;} fillFormFromTable(r); isNew=false; setEditing(true);} 
    private void onDelete(){ int r=table.getSelectedRow(); if(r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng để xóa."); return;} String ma=String.valueOf(table.getValueAt(r,0)); if (JOptionPane.showConfirmDialog(this,"Xóa phiếu "+ma+"?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return; try{ if(dao.delete(ma)){ reload(); JOptionPane.showMessageDialog(this,"Đã xóa."); } else JOptionPane.showMessageDialog(this,"Không xóa được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi xóa: "+ex.getMessage()); } }
    private void onSave(){ ThuPhi tp=buildFromForm(); if(tp==null) return; try{ boolean ok=isNew?dao.insert(tp):dao.update(tp); if(ok){ reload(); setEditing(false); JOptionPane.showMessageDialog(this,"Đã lưu."); } else JOptionPane.showMessageDialog(this,"Không lưu được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi lưu: "+ex.getMessage()); } }
    private void onCancel(){ setEditing(false); clearForm(); }
    private void onFilter(){ String tu = JOptionPane.showInputDialog(this,"Từ ngày (yyyy-MM-dd)"); String den = JOptionPane.showInputDialog(this,"Đến ngày (yyyy-MM-dd)"); try{ Date d1=parseDate(tu), d2=parseDate(den); if(d1==null||d2==null){ JOptionPane.showMessageDialog(this,"Ngày không hợp lệ"); return;} String[] cols={"MaPhieu","MaHoiVien","TenHoiVien","LoaiPhi","SoTien","NgayThu","PhuongThucTT","NguoiThu","GhiChu"}; List<ThuPhi> list = dao.getByDateRange(d1,d2); Object[][] data=new Object[list.size()][cols.length]; for(int i=0;i<list.size();i++){ ThuPhi x=list.get(i); data[i][0]=x.getMaPhieu(); data[i][1]=x.getMaHoiVien(); data[i][2]=x.getTenHoiVien(); data[i][3]=x.getLoaiPhi(); data[i][4]=x.getSoTien(); data[i][5]=x.getNgayThu(); data[i][6]=x.getPhuongThucTT(); data[i][7]=x.getNguoiThu(); data[i][8]=x.getGhiChu(); } table.setModel(new javax.swing.table.DefaultTableModel(data, cols)); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi lọc: "+ex.getMessage()); }}

    private void onExportCsv(){
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn nơi lưu CSV");
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            writeTableToCsv(file);
            JOptionPane.showMessageDialog(this, "Đã xuất: " + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất CSV: " + ex.getMessage());
        }
    }

    private void onPrint(){
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn một hóa đơn để in."); return; }
        String ma = String.valueOf(table.getValueAt(r, 0));
        try {
            ThuPhi tp = dao.getById(ma);
            if (tp == null) { JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn."); return; }
            JTextArea area = new JTextArea();
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            area.setText(buildInvoiceText(tp));
            area.setEditable(false);
            boolean ok = area.print();
            if (!ok) JOptionPane.showMessageDialog(this, "Lệnh in đã bị hủy.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi in: " + ex.getMessage());
        }
    }

    private String buildInvoiceText(ThuPhi tp){
        StringBuilder sb = new StringBuilder();
        sb.append("=========== HÓA ĐƠN THU PHÍ ===========\n");
        sb.append("Mã phiếu   : ").append(tp.getMaPhieu()).append('\n');
        sb.append("Mã hội viên: ").append(tp.getMaHoiVien()).append('\n');
        sb.append("Tên hội viên: ").append(tp.getTenHoiVien()==null?"":tp.getTenHoiVien()).append('\n');
        sb.append("Loại phí   : ").append(tp.getLoaiPhi()).append('\n');
        sb.append("Số tiền    : ").append(tp.getSoTien()).append(" VNĐ\n");
        sb.append("Ngày thu   : ").append(tp.getNgayThu()).append('\n');
        sb.append("Phương thức: ").append(tp.getPhuongThucTT()).append('\n');
        sb.append("Người thu  : ").append(tp.getNguoiThu()).append('\n');
        sb.append("Ghi chú    : ").append(tp.getGhiChu()==null?"":tp.getGhiChu()).append('\n');
        sb.append("=======================================\n");
        sb.append("Cảm ơn quý khách!\n");
        return sb.toString();
    }

    private void writeTableToCsv(File file) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            javax.swing.table.TableModel model = table.getModel();
            // header
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) fw.append(',');
                fw.append(escapeCsv(model.getColumnName(c)));
            }
            fw.append('\n');
            // rows
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) fw.append(',');
                    Object val = model.getValueAt(r, c);
                    fw.append(escapeCsv(val == null ? "" : String.valueOf(val)));
                }
                fw.append('\n');
            }
        }
    }

    private String escapeCsv(String s) {
        String v = s.replace("\r", " ").replace("\n", " ");
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            v = '"' + v.replace("\"", "\"\"") + '"';
        }
        return v;
    }

    private void setEditing(boolean editing){ this.isEditing=editing; btnSave.setEnabled(editing); btnCancel.setEnabled(editing); btnAdd.setEnabled(!editing); btnEdit.setEnabled(!editing); btnDelete.setEnabled(!editing); btnSearch.setEnabled(!editing); btnRefresh.setEnabled(!editing); txtMaPhieu.setEnabled(false); txtMaHoiVien.setEnabled(editing); txtLoaiPhi.setEnabled(editing); txtSoTien.setEnabled(editing); txtNgayThu.setEnabled(editing); txtPTTT.setEnabled(editing); txtNguoiThu.setEnabled(editing); txtGhiChu.setEnabled(editing); }
    private void clearForm(){ txtMaPhieu.setText(""); txtMaHoiVien.setText(""); txtLoaiPhi.setText(""); txtSoTien.setText(""); txtNgayThu.setText(""); txtPTTT.setText(""); txtNguoiThu.setText(""); txtGhiChu.setText(""); }
    private void fillFormFromTable(int r){ txtMaPhieu.setText(safe(table.getValueAt(r,0))); txtMaHoiVien.setText(safe(table.getValueAt(r,1))); txtLoaiPhi.setText(safe(table.getValueAt(r,3))); txtSoTien.setText(safe(table.getValueAt(r,4))); txtNgayThu.setText(safeDate(table.getValueAt(r,5))); txtPTTT.setText(safe(table.getValueAt(r,6))); txtNguoiThu.setText(safe(table.getValueAt(r,7))); txtGhiChu.setText(safe(table.getValueAt(r,8))); }
    private String safe(Object v){ return v==null?"":String.valueOf(v);} private String safeDate(Object v){ if(v==null) return ""; if(v instanceof Date) return df.format((Date)v); return String.valueOf(v);} private Date parseDate(String s) throws ParseException{ if(s==null||s.trim().isEmpty()) return null; return df.parse(s.trim()); }
    private ThuPhi buildFromForm(){ String ma=txtMaPhieu.getText().trim(); String hv=txtMaHoiVien.getText().trim(); String loai=txtLoaiPhi.getText().trim(); String soStr=txtSoTien.getText().trim(); String pttt=txtPTTT.getText().trim(); String nguoi=txtNguoiThu.getText().trim(); BigDecimal soTien=null; try{ if(!ValidationUtil.isEmpty(soStr)) soTien=new BigDecimal(soStr); }catch(NumberFormatException e){ JOptionPane.showMessageDialog(this,"Số tiền không hợp lệ"); return null; } ThuPhi tp=new ThuPhi(); tp.setMaPhieu(ma); tp.setMaHoiVien(ValidationUtil.isEmpty(hv)?null:hv); tp.setLoaiPhi(loai); tp.setSoTien(soTien); try{ tp.setNgayThu(parseDate(txtNgayThu.getText())); }catch(ParseException e){ JOptionPane.showMessageDialog(this,"Sai định dạng ngày (yyyy-MM-dd)"); return null; } tp.setPhuongThucTT(pttt); tp.setNguoiThu(nguoi); tp.setGhiChu(txtGhiChu.getText().trim()); return tp; }
}


