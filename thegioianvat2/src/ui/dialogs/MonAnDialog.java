package ui.dialogs;

import dao.DanhMucDAO;
import dao.MonAnDAO;
import model.DanhMuc;
import model.MonAn;
import util.DialogUtil;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MonAnDialog extends JDialog {
    private JTextField txtTenMA, txtDonGia, txtSoLuong;
    private JComboBox<DanhMuc> cbbDanhMuc;
    private JButton btnSave, btnCancel;

    private MonAnDAO monAnDAO = new MonAnDAO();
    private DanhMucDAO danhMucDAO = new DanhMucDAO();

    private MonAn monAn;  // null nếu thêm mới, không null nếu sửa

    public MonAnDialog(Frame parent) {
        super(parent, true);
        setTitle("Thêm / Sửa Món Ăn");
        initComponents();
        loadDanhMuc();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên món ăn
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Tên món ăn:"), gbc);
        txtTenMA = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtTenMA, gbc);

        // Danh mục
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Danh mục:"), gbc);
        cbbDanhMuc = new JComboBox<>();
        gbc.gridx = 1;
        panelForm.add(cbbDanhMuc, gbc);

        // Đơn giá
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Đơn giá:"), gbc);
        txtDonGia = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtDonGia, gbc);

        // Số lượng
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Số lượng:"), gbc);
        txtSoLuong = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtSoLuong, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadDanhMuc() {
        List<DanhMuc> list = danhMucDAO.getAll();
        DefaultComboBoxModel<DanhMuc> model = new DefaultComboBoxModel<>();
        model.addElement(null); // để cho phép chọn không có danh mục
        for (DanhMuc dm : list) {
            model.addElement(dm);
        }
        cbbDanhMuc.setModel(model);
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
        if (monAn != null) {
            txtTenMA.setText(monAn.getTenMA());
            // Chọn danh mục theo maDM
            for (int i = 0; i < cbbDanhMuc.getItemCount(); i++) {
                DanhMuc dm = cbbDanhMuc.getItemAt(i);
                if (dm != null && monAn.getMaDM() != null && monAn.getMaDM().equals(dm.getMaDM())) {
                    cbbDanhMuc.setSelectedIndex(i);
                    break;
                }
            }
            txtDonGia.setText(monAn.getDonGia() != null ? monAn.getDonGia().toString() : "");
            txtSoLuong.setText(String.valueOf(monAn.getSoLuong()));
        }
    }

    private void onSave() {
        String tenMA = txtTenMA.getText().trim();
        DanhMuc dm = (DanhMuc) cbbDanhMuc.getSelectedItem();
        String donGiaStr = txtDonGia.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();

        if (!Validator.isValidName(tenMA)) {
            DialogUtil.showError(this, "Tên món ăn không hợp lệ.");
            return;
        }

        BigDecimal donGia;
        try {
            donGia = new BigDecimal(donGiaStr);
            if (donGia.compareTo(BigDecimal.ZERO) < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            DialogUtil.showError(this, "Đơn giá phải là số dương hợp lệ.");
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            DialogUtil.showError(this, "Số lượng phải là số nguyên không âm.");
            return;
        }

        if (monAn == null) {
            monAn = new MonAn();
        }

        monAn.setTenMA(tenMA);
        monAn.setMaDM(dm != null ? dm.getMaDM() : null);
        monAn.setDonGia(donGia);
        monAn.setSoLuong(soLuong);

        boolean success;
        if (monAn.getMaMA() == null) {
            success = monAnDAO.insert(monAn);
        } else {
            success = monAnDAO.update(monAn);
        }

        if (success) {
            DialogUtil.showInfo(this, "Lưu món ăn thành công.");
            dispose();
        } else {
            DialogUtil.showError(this, "Lỗi khi lưu món ăn.");
        }
    }
}