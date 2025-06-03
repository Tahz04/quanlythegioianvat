package ui.panels;

import dao.TaiKhoanDAO;
import db.MySQLConnection;
import model.TaiKhoan;
import util.DialogUtil;
import ui.dialogs.TaiKhoanDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class TaiKhoanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnEdit, btnDelete;

    public TaiKhoanPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Tên đăng nhập","Họ tên", "Vai trò"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        add(pnlButtons, BorderLayout.SOUTH);

        // Thêm sự kiện cho nút Thêm
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });

        // Thêm sự kiện cho nút Sửa
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEdit();
            }
        });

        // Thêm sự kiện cho nút Xóa
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = MySQLConnection.getConnection()) {
            TaiKhoanDAO dao = new TaiKhoanDAO(conn);
            List<TaiKhoan> list = dao.layDanhSachTaiKhoan();
            for (TaiKhoan tk : list) {
                model.addRow(new Object[]{tk.getTenDangNhap(),tk.gethoTen(), tk.getVaiTro()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showError(this, "Lỗi tải danh sách tài khoản!");
        }
    }

    private void onAdd() {
        TaiKhoanDialog dialog = new TaiKhoanDialog(null);
        // Thiết lập mặc định vai trò là "NhanVien"
        dialog.setVaiTro("NhanVien");
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            TaiKhoan tk = dialog.getTaiKhoan();
            try (Connection conn = MySQLConnection.getConnection()) {
                TaiKhoanDAO dao = new TaiKhoanDAO(conn);
                if (dao.themTaiKhoan(tk)) {
                    DialogUtil.showSuccess(this, "Thêm tài khoản thành công!");
                    loadData();
                } else {
                    DialogUtil.showError(this, "Thêm tài khoản thất bại!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtil.showError(this, "Lỗi thêm tài khoản!");
            }
        }
    }
    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            DialogUtil.showError(this, "Vui lòng chọn tài khoản để sửa.");
            return;
        }
        String tenDN = (String) model.getValueAt(row, 0);
        try (Connection conn = MySQLConnection.getConnection()) {
            TaiKhoanDAO dao = new TaiKhoanDAO(conn);
            TaiKhoan tk = dao.layDanhSachTaiKhoan().stream()
                    .filter(t -> t.getTenDangNhap().equals(tenDN))
                    .findFirst()
                    .orElse(null);
            if (tk != null) {
                TaiKhoanDialog dialog = new TaiKhoanDialog(tk);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    TaiKhoan updatedTk = dialog.getTaiKhoan();
                    if (dao.capNhatTaiKhoan(updatedTk)) {
                        DialogUtil.showSuccess(this, "Cập nhật tài khoản thành công!");
                        loadData();
                    } else {
                        DialogUtil.showError(this, "Cập nhật tài khoản thất bại!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showError(this, "Lỗi cập nhật tài khoản!");
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            DialogUtil.showError(this, "Vui lòng chọn tài khoản để xóa.");
            return;
        }
        String tenDN = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa tài khoản " + tenDN + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = MySQLConnection.getConnection()) {
                TaiKhoanDAO dao = new TaiKhoanDAO(conn);
                if (dao.xoaTaiKhoan(tenDN)) {
                    DialogUtil.showSuccess(this, "Xóa thành công!");
                    loadData();
                } else {
                    DialogUtil.showError(this, "Xóa thất bại!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtil.showError(this, "Lỗi xóa tài khoản!");
            }
        }
    }
}