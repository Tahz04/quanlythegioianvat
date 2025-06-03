package ui.panels;

import dao.DanhMucDAO;
import model.DanhMuc;
import util.DialogUtil;
import util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyDanhMucPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private DanhMucDAO dao = new DanhMucDAO();

    public QuanLyDanhMucPanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadTable();
    }

    private void initComponents() {
        model = new DefaultTableModel(new Object[]{"Mã", "Tên danh mục"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");

        btnThem.addActionListener(e -> themDanhMuc());
        btnSua.addActionListener(e -> suaDanhMuc());
        btnXoa.addActionListener(e -> xoaDanhMuc());
        
        // Khởi tạo trạng thái ban đầu cho các nút
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        // Sự kiện khi chọn hàng trong table
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() >= 0;
            btnSua.setEnabled(selected);
            btnXoa.setEnabled(selected);
        });

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        

        add(scrollPane, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void loadTable() {
        model.setRowCount(0);
        List<DanhMuc> list = dao.getAll();
        for (DanhMuc dm : list) {
            model.addRow(new Object[]{dm.getMaDM(), dm.getTenDM()});
        }
    }

    private void themDanhMuc() {
        String tenDM = JOptionPane.showInputDialog(this, "Nhập tên danh mục:");
        if (Validator.isEmpty(tenDM)) {
            DialogUtil.showError(this, "Tên danh mục không được để trống!");
            return;
        }
        DanhMuc dm = new DanhMuc(0, tenDM);
        if (dao.insert(dm)) {
            loadTable();
            DialogUtil.showSuccess(this, "Thêm thành công!");
        } else {
            DialogUtil.showError(this, "Lỗi thêm danh mục!");
        }
    }

    private void suaDanhMuc() {
        int row = table.getSelectedRow();
        if (row == -1) {
            DialogUtil.showError(this, "Vui lòng chọn danh mục để sửa.");
            return;
        }

        int maDM = (int) model.getValueAt(row, 0);
        String tenDMcu = (String) model.getValueAt(row, 1);
        String tenDMmoi = JOptionPane.showInputDialog(this, "Sửa tên danh mục:", tenDMcu);

        if (Validator.isEmpty(tenDMmoi)) {
            DialogUtil.showError(this, "Tên danh mục không được để trống!");
            return;
        }

        DanhMuc dm = new DanhMuc(maDM, tenDMmoi);
        if (dao.update(dm)) {
            loadTable();
            DialogUtil.showSuccess(this, "Cập nhật thành công!");
        } else {
            DialogUtil.showError(this, "Lỗi cập nhật danh mục!");
        }
    }

    private void xoaDanhMuc() {
        int row = table.getSelectedRow();
        if (row == -1) {
            DialogUtil.showError(this, "Vui lòng chọn danh mục để xóa.");
            return;
        }

        int maDM = (int) model.getValueAt(row, 0);
        if (DialogUtil.showConfirm(this, "Bạn có chắc muốn xóa danh mục này?")) {
            if (dao.delete(maDM)) {
                loadTable();
                DialogUtil.showSuccess(this, "Xóa thành công!");
            } else {
                DialogUtil.showError(this, "Không thể xóa danh mục (có món ăn liên quan)!");
            }
        }
    }
}
