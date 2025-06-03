package ui.dialogs;

import dao.ChiTietDonHangDAO;
import dao.DonHangDAO;
import dao.MonAnDAO;
import dao.KhachHangDAO; 
import db.MySQLConnection;
import model.ChiTietDonHang;
import model.DonHang;
import model.MonAn;
import model.KhachHang; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonHangDialog extends JDialog {
    private JTextField tfMaKH;
    private JTable tblCT;
    private DefaultTableModel modelCT;
    private JButton btnAdd, btnEdit, btnDelete, btnSave, btnCancel;

    private DonHangDAO dhDAO = new DonHangDAO();
    private ChiTietDonHangDAO ctDAO = new ChiTietDonHangDAO();
    private MonAnDAO monAnDAO = new MonAnDAO();
    private KhachHangDAO khachHangDAO = new KhachHangDAO(); 
    private DonHang donHang;
    private Runnable onSaved;

    public DonHangDialog(Window owner, DonHang dh, boolean editable, Runnable onSaved) {
        super(owner, ModalityType.APPLICATION_MODAL);
        this.donHang = dh;
        this.onSaved = onSaved;

        setTitle(dh == null ? "Thêm Đơn Hàng" : "Sửa Đơn Hàng");
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel thông tin chung
        JPanel pnlTop = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlTop.add(new JLabel("Mã khách hàng:"));
        tfMaKH = new JTextField();
        pnlTop.add(tfMaKH);
        add(pnlTop, BorderLayout.NORTH);

        // Bảng chi tiết đơn hàng
        modelCT = new DefaultTableModel(new String[]{"Mã món", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblCT = new JTable(modelCT);
        add(new JScrollPane(tblCT), BorderLayout.CENTER);

        // Panel nút thao tác chi tiết
        JPanel pnlEast = new JPanel(new GridLayout(3, 1, 5, 5));
        btnAdd = new JButton("Thêm món");
        btnEdit = new JButton("Sửa món");
        btnDelete = new JButton("Xóa món");
        pnlEast.add(btnAdd);
        pnlEast.add(btnEdit);
        pnlEast.add(btnDelete);
        add(pnlEast, BorderLayout.EAST);

        // Panel nút lưu/hủy
        JPanel pnlBottom = new JPanel();
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        pnlBottom.add(btnSave);
        pnlBottom.add(btnCancel);
        add(pnlBottom, BorderLayout.SOUTH);

        // Nạp dữ liệu nếu là sửa đơn hàng
        if (donHang != null) {
            tfMaKH.setText(String.valueOf(donHang.getMaKH()));
            List<ChiTietDonHang> ds = ctDAO.getByDonHang(donHang.getMaDH());
            for (ChiTietDonHang ct : ds) {
                MonAn monAn = monAnDAO.getById(ct.getMaMA());
                if (monAn != null) {
                    double thanhTien = ct.getSoLuong() * monAn.getDonGia().doubleValue();
                    modelCT.addRow(new Object[]{
                        ct.getMaMA(),
                        monAn.getTenMA(),
                        ct.getSoLuong(),
                        monAn.getDonGia(),
                        thanhTien
                    });
                }
            }
        }

        // Sự kiện thêm món
        btnAdd.addActionListener(e -> {
            ChiTietDonHangDialog dlg = new ChiTietDonHangDialog(this, null, false);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                ChiTietDonHang ct = dlg.getChiTiet();
                MonAn monAn = monAnDAO.getById(ct.getMaMA());
                if (monAn != null) {
                    double thanhTien = ct.getSoLuong() * monAn.getDonGia().doubleValue();
                    modelCT.addRow(new Object[]{
                        ct.getMaMA(),
                        monAn.getTenMA(),
                        ct.getSoLuong(),
                        monAn.getDonGia(),
                        thanhTien
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Mã món không tồn tại");
                }
            }
        });

        // Sự kiện sửa món
        btnEdit.addActionListener(e -> {
            int r = tblCT.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Chọn món để sửa");
                return;
            }
            ChiTietDonHang current = new ChiTietDonHang(
                    0,
                    (int) modelCT.getValueAt(r, 0),
                    (int) modelCT.getValueAt(r, 2)
            );
            ChiTietDonHangDialog dlg = new ChiTietDonHangDialog(this, current, false);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                ChiTietDonHang updated = dlg.getChiTiet();
                MonAn monAn = monAnDAO.getById(updated.getMaMA());
                if (monAn != null) {
                    double thanhTien = updated.getSoLuong() * monAn.getDonGia().doubleValue();
                    modelCT.setValueAt(updated.getMaMA(), r, 0);
                    modelCT.setValueAt(monAn.getTenMA(), r, 1);
                    modelCT.setValueAt(updated.getSoLuong(), r, 2);
                    modelCT.setValueAt(monAn.getDonGia(), r, 3);
                    modelCT.setValueAt(thanhTien, r, 4);
                } else {
                    JOptionPane.showMessageDialog(this, "Mã món không tồn tại");
                }
            }
        });

        // Sự kiện xóa món
        btnDelete.addActionListener(e -> {
            int r = tblCT.getSelectedRow();
            if (r >= 0) {
                modelCT.removeRow(r);
            }
        });

        // Sự kiện lưu đơn hàng
        btnSave.addActionListener(e -> {
            Connection conn = null;
            try {
                conn = MySQLConnection.getConnection();
                conn.setAutoCommit(false);

                // Validate dữ liệu
                if (tfMaKH.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KH");
                    return;
                }
                if (modelCT.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất 1 món");
                    return;
                }

                int maKH = Integer.parseInt(tfMaKH.getText().trim());
                double tongTien = 0;
                List<ChiTietDonHang> chiTietList = new ArrayList<>();

                // Tính tổng tiền tạm thời (chưa giảm giá)
                for (int i = 0; i < modelCT.getRowCount(); i++) {
                    int maMA = (int) modelCT.getValueAt(i, 0);
                    int soLuong = (int) modelCT.getValueAt(i, 2);
                    double donGia = ((Number) modelCT.getValueAt(i, 3)).doubleValue();
                    
                    tongTien += soLuong * donGia;
                    chiTietList.add(new ChiTietDonHang(0, maMA, soLuong));
                }

                // Áp dụng giảm giá theo loại khách hàng
                KhachHang kh = khachHangDAO.findById(maKH);
                if (kh != null && kh.getLoaiKH() != null) {
                    switch (kh.getLoaiKH()) {
                        case 1: // Khách thân thiết - giảm 5%
                            tongTien *= 0.95;
                            break;
                        case 2: // Khách VIP - giảm 10%
                            tongTien *= 0.9;
                            break;
                    }
                }

                // Tạo đơn hàng
                DonHang dhToSave = donHang == null ? new DonHang() : donHang;
                dhToSave.setMaKH(maKH);
                dhToSave.setTongTien(tongTien);
                dhToSave.setNgayDat(new Date(System.currentTimeMillis()));

                // Lưu đơn hàng
                boolean saveSuccess;
                if (donHang == null) {
                    saveSuccess = dhDAO.insert(dhToSave, conn);
                } else {
                    saveSuccess = dhDAO.update(dhToSave, conn);
                }

                if (!saveSuccess) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu đơn hàng");
                    return;
                }

                // Xóa chi tiết cũ (nếu là sửa)
                if (donHang != null) {
                    if (!ctDAO.deleteByMaDH(dhToSave.getMaDH(), conn)) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Lỗi khi xóa chi tiết cũ");
                        return;
                    }
                }

                // Lưu chi tiết mới
                for (ChiTietDonHang ct : chiTietList) {
                    ct.setMaDH(dhToSave.getMaDH());
                    if (!ctDAO.insert(ct, conn)) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Lỗi khi lưu chi tiết");
                        return;
                    }
                }

                conn.commit();
                JOptionPane.showMessageDialog(this, "Lưu đơn hàng thành công!");
                if (onSaved != null) onSaved.run();
                dispose();

            } catch (NumberFormatException ex) {
                rollbackQuietly(conn);
                JOptionPane.showMessageDialog(this, "Mã KH phải là số");
            } catch (SQLException ex) {
                rollbackQuietly(conn);
                JOptionPane.showMessageDialog(this, "Lỗi database: " + ex.getMessage());
                ex.printStackTrace();
            } catch (Exception ex) {
                rollbackQuietly(conn);
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                closeQuietly(conn);
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } 
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try { 
                conn.setAutoCommit(true);
                conn.close(); 
            } 
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
