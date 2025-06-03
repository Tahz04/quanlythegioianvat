package ui.panels;

import dao.KhachHangDAO;
import model.KhachHang;
import ui.dialogs.ThemKhachHangDialog;
import util.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class QuanLyKhachHangPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private KhachHangDAO dao = new KhachHangDAO();
    private JTextField txtTimKiem;
    private TableRowSorter<DefaultTableModel> sorter;
    
    public QuanLyKhachHangPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadTable(); 
    }


    // Khởi tạo các thành phần UI
    private void initComponents() {
        // Panel tìm kiếm
        JPanel panelSearch = new JPanel(new BorderLayout(5, 5));
        panelSearch.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(200, 25));
        panelSearch.add(new JLabel("Tìm kiếm (Mã/Tên khách hàng):"), BorderLayout.WEST);
        panelSearch.add(txtTimKiem, BorderLayout.CENTER);
        add(panelSearch, BorderLayout.NORTH);

        // Tạo model cho table với cột mới "Loại KH"
        model = new DefaultTableModel(new Object[]{"Mã KH", "Tên khách hàng", "Số điện thoại", "Địa chỉ", "Loại KH"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên table
            }
        };
        table = new JTable(model);
        
        // Thiết lập bộ lọc cho table
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Sự kiện tìm kiếm khi nhập text
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Panel chứa các nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        
        // Ban đầu disable các nút Sửa/Xóa
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        // Bật nút Sửa/Xóa khi có dòng được chọn
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() >= 0;
            btnSua.setEnabled(selected);
            btnXoa.setEnabled(selected);
        });

        // Gán sự kiện cho các nút
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);

        add(scrollPane, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
    }
    
    // Tải dữ liệu vào table
    private void loadTable() {
        model.setRowCount(0); // Xóa dữ liệu cũ
        List<KhachHang> list = dao.getAll();
        for (KhachHang kh : list) {
            model.addRow(new Object[]{
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getSoDienThoai(),
                kh.getDiaChi(),
                kh.getLoaiKHString() // Hiển thị loại KH dạng chữ
            });
        }
    }

    // Mở dialog thêm khách hàng mới
    private void themKhachHang() {
        ThemKhachHangDialog dialog = new ThemKhachHangDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Thêm khách hàng", true);
        dialog.setVisible(true);

        KhachHang kh = dialog.getKhachHang();
        if (kh != null && !dialog.isEdit()) {
            if (dao.insert(kh)) {
                loadTable(); // Tải lại dữ liệu
                DialogUtil.showSuccess(this, "Thêm khách hàng thành công!");
            } else {
                DialogUtil.showError(this, "Lỗi thêm khách hàng!");
            }
        }
    }

    // Mở dialog sửa khách hàng
    private void suaKhachHang() {
        int row = table.getSelectedRow();
        if (row == -1) {
            DialogUtil.showError(this, "Vui lòng chọn khách hàng để sửa.");
            return;
        }

        // Lấy mã KH từ dòng được chọn (chú ý chuyển đổi index khi dùng sorter)
        int modelRow = table.convertRowIndexToModel(row);
        int maKH = (int) model.getValueAt(modelRow, 0);
        
        KhachHang kh = dao.findById(maKH);
        if (kh == null) {
            DialogUtil.showError(this, "Không tìm thấy khách hàng!");
            return;
        }

        // Mở dialog với dữ liệu khách hàng
        ThemKhachHangDialog dialog = new ThemKhachHangDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Sửa khách hàng", true);
        dialog.setKhachHang(kh);
        dialog.setVisible(true);

        // Cập nhật dữ liệu sau khi sửa
        KhachHang updatedKh = dialog.getKhachHang();
        if (updatedKh != null && dialog.isEdit()) {
            updatedKh.setMaKH(maKH); // Giữ nguyên mã KH
            if (dao.update(updatedKh)) {
                loadTable();
                DialogUtil.showSuccess(this, "Cập nhật khách hàng thành công!");
            } else {
                DialogUtil.showError(this, "Lỗi cập nhật khách hàng!");
            }
        }
    }

    // Xóa khách hàng
    private void xoaKhachHang() {
        int row = table.getSelectedRow();
        if (row == -1) {
            DialogUtil.showError(this, "Vui lòng chọn khách hàng để xóa.");
            return;
        }
        
        // Lấy mã KH từ dòng được chọn (chú ý chuyển đổi index khi dùng sorter)
        int modelRow = table.convertRowIndexToModel(row);
        int maKH = (int) model.getValueAt(modelRow, 0);
        
        // Xác nhận trước khi xóa
        if (DialogUtil.showConfirm(this, "Bạn có chắc muốn xóa khách hàng này?")) {
            if (dao.delete(maKH)) {
                loadTable();
                DialogUtil.showSuccess(this, "Xóa khách hàng thành công!");
            } else {
                DialogUtil.showError(this, "Không thể xóa khách hàng (có đơn hàng liên quan)!");
            }
        }
    }

    // Tìm kiếm khách hàng
    private void search() {
        String text = txtTimKiem.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null); // Hiển thị tất cả nếu không có từ khóa
        } else {
            // Lọc theo mã KH hoặc tên KH (không phân biệt hoa thường)
            sorter.setRowFilter(RowFilter.orFilter(
                java.util.Arrays.asList(
                    RowFilter.regexFilter("(?i)" + text, 0), // Cột mã KH
                    RowFilter.regexFilter("(?i)" + text, 1)  // Cột tên KH
                )
            ));
        }
    }
}