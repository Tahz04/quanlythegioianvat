package ui.panels;

import dao.DonHangDAO;
import dao.MonAnDAO;
import model.DonHang;
import ui.dialogs.ChiTietDonHangDialog;
import ui.dialogs.DonHangDialog;
import controller.DonHangController;
import dao.KhachHangDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.ChiTietDonHang;
import model.KhachHang;
import model.MonAn;
import ui.dialogs.ThanhToanDialog;

public class QuanLyDonHangPanel extends JPanel {
    private JTable tblDonHang;
    private DefaultTableModel tblModel;
    private DonHangDAO dhDAO = new DonHangDAO();
    private DonHangController controller = new DonHangController();
    private JTextField tfSearch;
    private JButton btnEdit, btnDelete, btnDetail, btnThanhToan; // Khai báo các nút ở cấp độ class

    public QuanLyDonHangPanel() {
        setLayout(new BorderLayout(10, 10));

        // Thanh tìm kiếm
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 5));
        tfSearch = new JTextField(20);
        JButton btnSearch = new JButton("Tìm kiếm");
        
        pnlSearch.add(new JLabel("Tìm kiếm (Mã ĐH/Tên KH):"), BorderLayout.WEST);
        pnlSearch.add(tfSearch, BorderLayout.CENTER);
        pnlSearch.add(btnSearch, BorderLayout.EAST);
        add(pnlSearch, BorderLayout.NORTH);

        // Bảng đơn hàng
        tblModel = new DefaultTableModel(new Object[]{"Mã ĐH", "Tên KH", "Ngày tạo", "Tổng tiền", "Ghi chú"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { 
                return false; 
            }
        };
        
        tblDonHang = new JTable(tblModel);
        add(new JScrollPane(tblDonHang), BorderLayout.CENTER);

        // Panel chứa các nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnDetail = new JButton("Chi tiết");
        btnThanhToan = new JButton("Thanh toán");

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnDetail);
        pnlButtons.add(btnThanhToan);
        
        add(pnlButtons, BorderLayout.SOUTH);

        // Tải dữ liệu ban đầu
        loadTableData();

        // Xử lý sự kiện
        btnAdd.addActionListener(e -> showDonHangDialog(null));
        btnEdit.addActionListener(e -> editSelectedDonHang());
        btnDelete.addActionListener(e -> deleteSelectedDonHang());
        btnDetail.addActionListener(e -> showChiTietDialog());
        btnSearch.addActionListener(e -> searchDonHang());
        tfSearch.addActionListener(e -> searchDonHang());
        btnThanhToan.addActionListener(e -> showThanhToanDialog());

        // Thêm ListSelectionListener cho bảng
        tblDonHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        // Cập nhật trạng thái ban đầu của các nút
        updateButtonStates();
    }

    // Phương thức cập nhật trạng thái các nút
    private void updateButtonStates() {
        boolean hasSelection = tblDonHang.getSelectedRow() != -1;
        btnEdit.setEnabled(hasSelection);
        btnDelete.setEnabled(hasSelection);
        btnDetail.setEnabled(hasSelection);
        btnThanhToan.setEnabled(hasSelection);
    }
    
    private void loadTableData() {
        SwingWorker<List<DonHang>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<DonHang> doInBackground() {
                return dhDAO.getAll();
            }

            @Override
            protected void done() {
                try {
                    updateTable(get());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(QuanLyDonHangPanel.this, 
                        "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void showThanhToanDialog() {
        int selectedRow = tblDonHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn đơn hàng cần thanh toán", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maDH = (int) tblModel.getValueAt(selectedRow, 0);
        DonHang donHang = controller.getById(maDH);

        if (controller.getSoTien(maDH) <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Đơn hàng đã được thanh toán đủ", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ThanhToanDialog dlg = new ThanhToanDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            maDH
        );
        dlg.setVisible(true);
        loadTableData();
    }

    private void searchDonHang() {
        String keyword = tfSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }

        SwingWorker<List<DonHang>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<DonHang> doInBackground() {
                return controller.searchDonHang(keyword);
            }

            @Override
            protected void done() {
                try {
                    List<DonHang> result = get();
                    if (result.isEmpty()) {
                        JOptionPane.showMessageDialog(QuanLyDonHangPanel.this, 
                            "Không tìm thấy đơn hàng phù hợp", 
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                    updateTable(result);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(QuanLyDonHangPanel.this, 
                        "Lỗi khi tìm kiếm: " + e.getMessage(), 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void updateTable(List<DonHang> dsDonHang) {
        tblModel.setRowCount(0);
        for (DonHang dh : dsDonHang) {
            tblModel.addRow(new Object[]{
                dh.getMaDH(),
                controller.getTenKhachHang(dh.getMaKH()),
                dh.getNgayDat(),
                String.format("%,.0f VND", dh.getTongTien()),
                dh.getGhiChu() != null ? dh.getGhiChu() : ""
            });
        }
    }

    private void showDonHangDialog(DonHang donHang) {
        DonHangDialog dlg = new DonHangDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            donHang,
            true,
            this::loadTableData
        );
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void editSelectedDonHang() {
        int selectedRow = tblDonHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn đơn hàng cần sửa", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int maDH = (int) tblModel.getValueAt(selectedRow, 0);
        DonHang donHang = dhDAO.getById(maDH);
        if (donHang != null) {
            showDonHangDialog(donHang);
        }
    }

    private void deleteSelectedDonHang() {
        int selectedRow = tblDonHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn đơn hàng cần xóa", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int maDH = (int) tblModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa đơn hàng #" + maDH + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return dhDAO.delete(maDH);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            loadTableData();
                            JOptionPane.showMessageDialog(QuanLyDonHangPanel.this,
                                "Xóa đơn hàng thành công",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(QuanLyDonHangPanel.this,
                                "Xóa đơn hàng thất bại",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }

    private void showChiTietDialog() {
        int selectedRow = tblDonHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn đơn hàng để xem chi tiết", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int maDH = (int) tblModel.getValueAt(selectedRow, 0);
        DonHang donHang = dhDAO.getById(maDH);
        
        // Tạo dialog mới để hiển thị chi tiết
        JDialog detailDialog = new JDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Chi tiết đơn hàng #" + maDH,
            true
        );
        detailDialog.setLayout(new BorderLayout());
        
        // Tạo bảng chi tiết
        DefaultTableModel detailModel = new DefaultTableModel(
            new String[]{"Mã món", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
        
        for (ChiTietDonHang ct : donHang.getChiTietDonHangs()) {
            MonAnDAO monAnDAO = new MonAnDAO();
            MonAn monAn = monAnDAO.getById(ct.getMaMA());
            if (monAn != null) {
                double thanhTien = ct.getSoLuong() * monAn.getDonGia().doubleValue();
                detailModel.addRow(new Object[]{
                    ct.getMaMA(),
                    monAn.getTenMA(),
                    ct.getSoLuong(),
                    String.format("%,.0f VND", monAn.getDonGia()),
                    String.format("%,.0f VND", thanhTien)
                });
            }
        }
        
        JTable detailTable = new JTable(detailModel);
        detailDialog.add(new JScrollPane(detailTable), BorderLayout.CENTER);
        
        // Panel thông tin khách hàng và giảm giá
        JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
        
        // Thông tin khách hàng
        KhachHang kh = new KhachHangDAO().findById(donHang.getMaKH());
        JPanel pnlCustomer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCustomer.add(new JLabel("Khách hàng: " + kh.getTenKH() + " - " + kh.getLoaiKHString()));
        pnlInfo.add(pnlCustomer);
        
        // Thông tin giảm giá
        JPanel pnlDiscount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDiscount.add(new JLabel("Chính sách giảm giá: " + kh.getThongTinGiamGia()));
        pnlInfo.add(pnlDiscount);
        
        detailDialog.add(pnlInfo, BorderLayout.NORTH);
        
        // Thêm tổng tiền
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTotal.add(new JLabel("Tổng tiền: " + String.format("%,.0f VND", donHang.getTongTien())));
        detailDialog.add(pnlTotal, BorderLayout.SOUTH);
        
        detailDialog.setSize(600, 400);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setVisible(true);
    }
}
