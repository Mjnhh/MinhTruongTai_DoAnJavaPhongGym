package view;

import dao.LichTapPTDAO;
import model.LichTapPT;
import util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class LichTapPTPanel extends JPanel {

    private final LichTapPTDAO lichTapPTDAO = new LichTapPTDAO();
    private JTable table;
    private JTextField txtSearch; private JButton btnSearch, btnRefresh, btnAdd, btnEdit, btnDelete, btnSave, btnCancel, btnFilter;

    // Form
    private JTextField txtId, txtMaHV, txtMaHLV, txtNgayTap, txtGioBD, txtGioKT, txtTrangThai;

    private final SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isEditing=false, isNew=false;

    public LichTapPTPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout(8,0)); JPanel action = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        txtSearch=new JTextField(24); btnSearch=new JButton("Tìm kiếm"); btnRefresh=new JButton("Tải lại"); btnAdd=new JButton("Thêm"); btnEdit=new JButton("Sửa"); btnDelete=new JButton("Hủy lịch"); btnSave=new JButton("Lưu"); btnCancel=new JButton("Hủy"); btnFilter=new JButton("Lọc theo ngày");
        action.add(new JLabel("Từ khóa (mã HV/HLV)")); action.add(txtSearch); action.add(btnSearch); action.add(btnRefresh); action.add(btnAdd); action.add(btnEdit); action.add(btnDelete); action.add(btnSave); action.add(btnCancel); action.add(btnFilter);
        top.add(action, BorderLayout.WEST); add(top, BorderLayout.NORTH);

        table = new JTable(); add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0,2,8,6)); form.setBorder(BorderFactory.createTitledBorder("Lịch tập PT")); form.setPreferredSize(new Dimension(380,0));
        txtId=new JTextField(); txtMaHV=new JTextField(); txtMaHLV=new JTextField(); txtNgayTap=new JTextField(); txtGioBD=new JTextField(); txtGioKT=new JTextField(); txtTrangThai=new JTextField();
        form.add(new JLabel("ID")); form.add(txtId);
        form.add(new JLabel("Mã hội viên")); form.add(txtMaHV);
        form.add(new JLabel("Mã HLV")); form.add(txtMaHLV);
        form.add(new JLabel("Ngày tập (yyyy-MM-dd)")); form.add(txtNgayTap);
        form.add(new JLabel("Giờ bắt đầu (HH:mm)")); form.add(txtGioBD);
        form.add(new JLabel("Giờ kết thúc (HH:mm)")); form.add(txtGioKT);
        form.add(new JLabel("Trạng thái")); form.add(txtTrangThai);
        add(form, BorderLayout.EAST);

        btnSearch.addActionListener(e->doSearch()); btnRefresh.addActionListener(e->reload()); btnAdd.addActionListener(e->onAdd()); btnEdit.addActionListener(e->onEdit()); btnDelete.addActionListener(e->onDelete()); btnSave.addActionListener(e->onSave()); btnCancel.addActionListener(e->onCancel()); btnFilter.addActionListener(e->onFilter());
        table.addMouseListener(new MouseAdapter(){ @Override public void mouseClicked(MouseEvent e){ int r=table.getSelectedRow(); if(r>=0) fillFormFromTable(r);} });

        setEditing(false); reload();
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

    private void doSearch(){ String kw=txtSearch.getText()!=null?txtSearch.getText().trim():""; String[] cols={"ID","MaHoiVien","MaHLV","NgayTap","GioBatDau","GioKetThuc","TrangThai"}; try{ List<LichTapPT> list = lichTapPTDAO.getAll(); if(!kw.isEmpty()) list = list.stream().filter(x-> (x.getMaHoiVien()!=null && x.getMaHoiVien().contains(kw)) || (x.getMaHLV()!=null && x.getMaHLV().contains(kw))).toList(); Object[][] data=new Object[list.size()][cols.length]; for(int i=0;i<list.size();i++){ LichTapPT lt=list.get(i); data[i][0]=lt.getId(); data[i][1]=lt.getMaHoiVien(); data[i][2]=lt.getMaHLV(); data[i][3]=lt.getNgayTap(); data[i][4]=lt.getGioBatDau(); data[i][5]=lt.getGioKetThuc(); data[i][6]=lt.getTrangThai(); } table.setModel(new javax.swing.table.DefaultTableModel(data, cols)); } catch(Exception ex){ table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, cols)); }}

    private void onAdd(){ clearForm(); isNew=true; setEditing(true); }
    private void onEdit(){ int r=table.getSelectedRow(); if(r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng để sửa."); return;} fillFormFromTable(r); isNew=false; setEditing(true);} 
    private void onDelete(){ int r=table.getSelectedRow(); if(r<0){ JOptionPane.showMessageDialog(this,"Chọn một dòng để hủy."); return;} int id=Integer.parseInt(String.valueOf(table.getValueAt(r,0))); if (JOptionPane.showConfirmDialog(this,"Hủy lịch ID "+id+"?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return; try{ if(lichTapPTDAO.delete(id)){ reload(); JOptionPane.showMessageDialog(this,"Đã hủy."); } else JOptionPane.showMessageDialog(this,"Không hủy được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi: "+ex.getMessage()); } }
    private void onSave(){ LichTapPT lt=buildFromForm(); if(lt==null) return; try{ boolean ok = isNew? lichTapPTDAO.insert(lt): lichTapPTDAO.update(lt); if(ok){ reload(); setEditing(false); JOptionPane.showMessageDialog(this,"Đã lưu."); } else JOptionPane.showMessageDialog(this,"Không lưu được."); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi lưu: "+ex.getMessage()); } }
    private void onCancel(){ setEditing(false); clearForm(); }
    private void onFilter(){ String tu=JOptionPane.showInputDialog(this,"Từ ngày (yyyy-MM-dd)"); String den=JOptionPane.showInputDialog(this,"Đến ngày (yyyy-MM-dd)"); try{ Date d1=parseDate(tu), d2=parseDate(den); if(d1==null||d2==null){ JOptionPane.showMessageDialog(this,"Ngày không hợp lệ"); return;} String[] cols={"ID","MaHoiVien","MaHLV","NgayTap","GioBatDau","GioKetThuc","TrangThai"}; List<LichTapPT> list = lichTapPTDAO.getByDateRange(d1,d2); Object[][] data=new Object[list.size()][cols.length]; for(int i=0;i<list.size();i++){ LichTapPT lt=list.get(i); data[i][0]=lt.getId(); data[i][1]=lt.getMaHoiVien(); data[i][2]=lt.getMaHLV(); data[i][3]=lt.getNgayTap(); data[i][4]=lt.getGioBatDau(); data[i][5]=lt.getGioKetThuc(); data[i][6]=lt.getTrangThai(); } table.setModel(new javax.swing.table.DefaultTableModel(data, cols)); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Lỗi lọc: "+ex.getMessage()); }}

    private void setEditing(boolean editing){ this.isEditing=editing; btnSave.setEnabled(editing); btnCancel.setEnabled(editing); btnAdd.setEnabled(!editing); btnEdit.setEnabled(!editing); btnDelete.setEnabled(!editing); btnSearch.setEnabled(!editing); btnRefresh.setEnabled(!editing); txtId.setEnabled(false); txtMaHV.setEnabled(editing); txtMaHLV.setEnabled(editing); txtNgayTap.setEnabled(editing); txtGioBD.setEnabled(editing); txtGioKT.setEnabled(editing); txtTrangThai.setEnabled(editing); }
    private void clearForm(){ txtId.setText(""); txtMaHV.setText(""); txtMaHLV.setText(""); txtNgayTap.setText(""); txtGioBD.setText(""); txtGioKT.setText(""); txtTrangThai.setText(""); }
    private void fillFormFromTable(int r){ txtId.setText(String.valueOf(table.getValueAt(r,0))); txtMaHV.setText(String.valueOf(table.getValueAt(r,1))); txtMaHLV.setText(String.valueOf(table.getValueAt(r,2))); txtNgayTap.setText(safeDate(table.getValueAt(r,3))); txtGioBD.setText(safeTime(table.getValueAt(r,4))); txtGioKT.setText(safeTime(table.getValueAt(r,5))); txtTrangThai.setText(String.valueOf(table.getValueAt(r,6))); }
    private String safeDate(Object v){ if(v==null) return ""; if(v instanceof Date) return dfDate.format((Date)v); return String.valueOf(v);} private String safeTime(Object v){ return v==null?"":String.valueOf(v);} private Date parseDate(String s) throws ParseException{ if (s==null||s.trim().isEmpty()) return null; return dfDate.parse(s.trim()); }
    private LichTapPT buildFromForm(){ String hv=txtMaHV.getText().trim(); String hlv=txtMaHLV.getText().trim(); String ngay=txtNgayTap.getText().trim(); String bd=txtGioBD.getText().trim(); String kt=txtGioKT.getText().trim(); String tt=txtTrangThai.getText().trim(); if (ValidationUtil.isEmpty(hv)||ValidationUtil.isEmpty(hlv)||ValidationUtil.isEmpty(ngay)||ValidationUtil.isEmpty(bd)||ValidationUtil.isEmpty(kt)){ JOptionPane.showMessageDialog(this,"Vui lòng nhập đủ Mã HV/HLV, Ngày, Giờ bắt đầu/kết thúc"); return null; } try{ LichTapPT lt=new LichTapPT(); String idStr=txtId.getText().trim(); if(!ValidationUtil.isEmpty(idStr)) lt.setId(Integer.parseInt(idStr)); lt.setMaHoiVien(hv); lt.setMaHLV(hlv); lt.setNgayTap(parseDate(ngay)); lt.setGioBatDau(Time.valueOf(bd+":00")); lt.setGioKetThuc(Time.valueOf(kt+":00")); lt.setTrangThai(tt); return lt; } catch(Exception e){ JOptionPane.showMessageDialog(this,"Định dạng giờ phải HH:mm và ngày yyyy-MM-dd"); return null; } }
}


