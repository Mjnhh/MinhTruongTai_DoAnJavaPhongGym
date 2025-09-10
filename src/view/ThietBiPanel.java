package view;

import dao.ThietBiDAO;
import model.ThietBi;
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

public class ThietBiPanel extends JPanel {

    private final ThietBiDAO dao = new ThietBiDAO();
    private JTable table;
    private JTextField txtSearch; private JButton btnSearch, btnRefresh, btnAdd, btnEdit, btnDelete, btnSave, btnCancel; private JButton btnUpdateStatus, btnUpdateMaintenance;

    // Form
    private JTextField txtMa, txtTen, txtLoai, txtTinhTrang, txtNgayMua, txtGiaMua, txtNgayBaoTriCuoi, txtNgayBaoTriTiep; private JTextArea txtGhiChu;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isEditing = false; private boolean isNew = false;

    public ThietBiPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout(8, 0));
        JPanel action = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        txtSearch = new JTextField(24);
        btnSearch = new JButton("Tìm kiếm"); btnRefresh = new JButton("Tải lại");
        btnAdd = new JButton("Thêm"); btnEdit = new JButton("Sửa"); btnDelete = new JButton("Xóa"); btnSave = new JButton("Lưu"); btnCancel = new JButton("Hủy");
        btnUpdateStatus = new JButton("Đổi tình trạng"); btnUpdateMaintenance = new JButton("Cập nhật bảo trì");
        action.add(new JLabel("Từ khóa:")); action.add(txtSearch); action.add(btnSearch); action.add(btnRefresh);
        action.add(btnAdd); action.add(btnEdit); action.add(btnDelete); action.add(btnSave); action.add(btnCancel); action.add(btnUpdateStatus); action.add(btnUpdateMaintenance);
        top.add(action, BorderLayout.WEST); add(top, BorderLayout.NORTH);

        table = new JTable(); add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6)); form.setBorder(BorderFactory.createTitledBorder("Thông tin thiết bị")); form.setPreferredSize(new Dimension(380, 0));
        txtMa = new JTextField(); txtTen = new JTextField(); txtLoai = new JTextField(); txtTinhTrang = new JTextField(); txtNgayMua = new JTextField(); txtGiaMua = new JTextField(); txtNgayBaoTriCuoi = new JTextField(); txtNgayBaoTriTiep = new JTextField(); txtGhiChu = new JTextArea(3,20); txtGhiChu.setLineWrap(true); txtGhiChu.setWrapStyleWord(true);
        form.add(new JLabel("Mã TB (vd: TB001)")); form.add(txtMa);
        form.add(new JLabel("Tên thiết bị")); form.add(txtTen);
        form.add(new JLabel("Loại thiết bị")); form.add(txtLoai);
        form.add(new JLabel("Tình trạng (Tốt/Hỏng/Bảo trì)")); form.add(txtTinhTrang);
        form.add(new JLabel("Ngày mua (yyyy-MM-dd)")); form.add(txtNgayMua);
        form.add(new JLabel("Giá mua (VNĐ)")); form.add(txtGiaMua);
        form.add(new JLabel("Ngày bảo trì cuối (yyyy-MM-dd)")); form.add(txtNgayBaoTriCuoi);
        form.add(new JLabel("Ngày bảo trì tiếp (yyyy-MM-dd)")); form.add(txtNgayBaoTriTiep);
        form.add(new JLabel("Ghi chú")); form.add(new JScrollPane(txtGhiChu));
        add(form, BorderLayout.EAST);

        btnSearch.addActionListener(e -> doSearch()); btnRefresh.addActionListener(e -> reload());
        btnAdd.addActionListener(e -> onAdd()); btnEdit.addActionListener(e -> onEdit()); btnDelete.addActionListener(e -> onDelete()); btnSave.addActionListener(e -> onSave()); btnCancel.addActionListener(e -> onCancel());
        btnUpdateStatus.addActionListener(e -> onUpdateStatus()); btnUpdateMaintenance.addActionListener(e -> onUpdateMaintenance());
        table.addMouseListener(new MouseAdapter(){ @Override public void mouseClicked(MouseEvent e){ int r = table.getSelectedRow(); if (r>=0) fillFormFromTable(r); }});

        setEditing(false); reload();
    }

    private void reload() {
        String[] cols = {"MaThietBi", "TenThietBi", "LoaiThietBi", "TinhTrang", "NgayMua", "GiaMua", "NgayBaoTriCuoi", "NgayBaoTriTiep", "GhiChu"};
        try {
            List<ThietBi> list = dao.getAll(); Object[][] data = new Object[list.size()][cols.length];
            for (int i=0;i<list.size();i++){ ThietBi x=list.get(i); data[i][0]=x.getMaThietBi(); data[i][1]=x.getTenThietBi(); data[i][2]=x.getLoaiThietBi(); data[i][3]=x.getTinhTrang(); data[i][4]=x.getNgayMua(); data[i][5]=x.getGiaMua(); data[i][6]=x.getNgayBaoTriCuoi(); data[i][7]=x.getNgayBaoTriTiep(); data[i][8]=x.getGhiChu(); }
            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (Exception ex) {
            table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols)); JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thiết bị.");
        }
    }

    private void doSearch(){ String kw = txtSearch.getText()!=null?txtSearch.getText().trim():""; String[] cols={"MaThietBi","TenThietBi","LoaiThietBi","TinhTrang","NgayMua","GiaMua","NgayBaoTriCuoi","NgayBaoTriTiep","GhiChu"};
        try { List<ThietBi> list = dao.search(kw); Object[][] data = new Object[list.size()][cols.length]; for (int i=0;i<list.size();i++){ ThietBi x=list.get(i); data[i][0]=x.getMaThietBi(); data[i][1]=x.getTenThietBi(); data[i][2]=x.getLoaiThietBi(); data[i][3]=x.getTinhTrang(); data[i][4]=x.getNgayMua(); data[i][5]=x.getGiaMua(); data[i][6]=x.getNgayBaoTriCuoi(); data[i][7]=x.getNgayBaoTriTiep(); data[i][8]=x.getGhiChu(); } table.setModel(new javax.swing.table.DefaultTableModel(data, cols)); } catch (Exception ex){ table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols)); }}

    private void onAdd(){ clearForm(); try{ txtMa.setText(dao.getNextId()); }catch(Exception ignored){} isNew=true; setEditing(true);}    
    private void onEdit(){ int r=table.getSelectedRow(); if (r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng để sửa."); return;} fillFormFromTable(r); isNew=false; setEditing(true);} 
    private void onDelete(){ int r=table.getSelectedRow(); if (r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng để xóa."); return;} String ma=String.valueOf(table.getValueAt(r,0)); if (JOptionPane.showConfirmDialog(this,"Xóa thiết bị "+ma+"?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return; try{ if(dao.delete(ma)){ reload(); JOptionPane.showMessageDialog(this,"Đã xóa."); } else JOptionPane.showMessageDialog(this,"Không xóa được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi xóa: "+ex.getMessage()); } }
    private void onSave(){ ThietBi tb=buildFromForm(); if (tb==null) return; try{ boolean ok = isNew? dao.insert(tb): dao.update(tb); if (ok){ reload(); setEditing(false); JOptionPane.showMessageDialog(this,"Đã lưu."); } else JOptionPane.showMessageDialog(this,"Không lưu được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi lưu: "+ex.getMessage()); } }
    private void onCancel(){ setEditing(false); clearForm(); }

    private void onUpdateStatus(){ int r=table.getSelectedRow(); if (r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng."); return;} String ma=String.valueOf(table.getValueAt(r,0)); String current=String.valueOf(table.getValueAt(r,3)); String next=(String)JOptionPane.showInputDialog(this,"Chọn tình trạng mới","Cập nhật tình trạng",JOptionPane.QUESTION_MESSAGE,null,new String[]{"Tốt","Hỏng","Bảo trì"},current); if (next==null||next.isEmpty()) return; try{ if (dao.updateStatus(ma,next)){ reload(); JOptionPane.showMessageDialog(this,"Đã cập nhật tình trạng."); } else JOptionPane.showMessageDialog(this,"Không cập nhật được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi cập nhật: "+ex.getMessage()); } }
    private void onUpdateMaintenance(){ int r=table.getSelectedRow(); if (r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng."); return;} String ma=String.valueOf(table.getValueAt(r,0)); String ngay1=JOptionPane.showInputDialog(this,"Ngày bảo trì cuối (yyyy-MM-dd)",safeDate(table.getValueAt(r,6))); String ngay2=JOptionPane.showInputDialog(this,"Ngày bảo trì tiếp (yyyy-MM-dd)",safeDate(table.getValueAt(r,7))); try{ Date d1=parseDate(ngay1), d2=parseDate(ngay2); if (d1==null||d2==null){ JOptionPane.showMessageDialog(this,"Vui lòng nhập ngày hợp lệ"); return;} if (dao.updateMaintenance(ma,d1,d2)){ reload(); JOptionPane.showMessageDialog(this,"Đã cập nhật bảo trì."); } else JOptionPane.showMessageDialog(this,"Không cập nhật được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi cập nhật: "+ex.getMessage()); } }

    private void setEditing(boolean editing){ this.isEditing=editing; btnSave.setEnabled(editing); btnCancel.setEnabled(editing); btnAdd.setEnabled(!editing); btnEdit.setEnabled(!editing); btnDelete.setEnabled(!editing); btnSearch.setEnabled(!editing); btnRefresh.setEnabled(!editing); txtMa.setEnabled(false); txtTen.setEnabled(editing); txtLoai.setEnabled(editing); txtTinhTrang.setEnabled(editing); txtNgayMua.setEnabled(editing); txtGiaMua.setEnabled(editing); txtNgayBaoTriCuoi.setEnabled(editing); txtNgayBaoTriTiep.setEnabled(editing); txtGhiChu.setEnabled(editing); }
    private void clearForm(){ txtMa.setText(""); txtTen.setText(""); txtLoai.setText(""); txtTinhTrang.setText(""); txtNgayMua.setText(""); txtGiaMua.setText(""); txtNgayBaoTriCuoi.setText(""); txtNgayBaoTriTiep.setText(""); txtGhiChu.setText(""); isNew=true; }
    private void fillFormFromTable(int r){ txtMa.setText(safeString(table.getValueAt(r,0))); txtTen.setText(safeString(table.getValueAt(r,1))); txtLoai.setText(safeString(table.getValueAt(r,2))); txtTinhTrang.setText(safeString(table.getValueAt(r,3))); txtNgayMua.setText(safeDate(table.getValueAt(r,4))); txtGiaMua.setText(safeString(table.getValueAt(r,5))); txtNgayBaoTriCuoi.setText(safeDate(table.getValueAt(r,6))); txtNgayBaoTriTiep.setText(safeDate(table.getValueAt(r,7))); txtGhiChu.setText(safeString(table.getValueAt(r,8))); }
    private String safeString(Object v){ return v==null?"":String.valueOf(v);} private String safeDate(Object v){ if (v==null) return ""; if (v instanceof Date) return df.format((Date)v); return String.valueOf(v);} private Date parseDate(String s) throws ParseException{ if (s==null||s.trim().isEmpty()) return null; return df.parse(s.trim()); }
    private ThietBi buildFromForm(){ String ma=txtMa.getText().trim(); String ten=txtTen.getText().trim(); String giaStr=txtGiaMua.getText().trim(); if (ValidationUtil.isEmpty(ten)){ JOptionPane.showMessageDialog(this,"Tên thiết bị không được để trống"); return null;} BigDecimal gia=null; if (!ValidationUtil.isEmpty(giaStr)){ try{ gia=new BigDecimal(giaStr);}catch(NumberFormatException e){ JOptionPane.showMessageDialog(this,"Giá mua không hợp lệ"); return null; } } ThietBi tb=new ThietBi(); tb.setMaThietBi(ma); tb.setTenThietBi(ten); tb.setLoaiThietBi(txtLoai.getText().trim()); tb.setTinhTrang(txtTinhTrang.getText().trim()); try{ tb.setNgayMua(parseDate(txtNgayMua.getText())); tb.setNgayBaoTriCuoi(parseDate(txtNgayBaoTriCuoi.getText())); tb.setNgayBaoTriTiep(parseDate(txtNgayBaoTriTiep.getText())); } catch(ParseException e){ JOptionPane.showMessageDialog(this,"Sai định dạng ngày (yyyy-MM-dd)"); return null;} tb.setGiaMua(gia); tb.setGhiChu(txtGhiChu.getText().trim()); return tb; }
}


