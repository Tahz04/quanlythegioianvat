package ui.dialogs;

import model.KhachHang;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Dialog thêm/sửa thông tin khách hàng
 */
public class ThemKhachHangDialog extends JDialog {
    private JTextField txtTenKH;
    private JTextField txtSDT;
    private JTextField txtDiaChi;
    private JComboBox<String> cboLoaiKH;

    private JButton btnSave;
    private JButton btnCancel;

    private KhachHang khachHang;
    private boolean isEdit;

    public ThemKhachHangDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 320);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Main form panel
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Tên khách hàng*:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTenKH = new JTextField(20);
        txtTenKH.setToolTipText("Nhập tên khách hàng (bắt buộc)");
        panelForm.add(txtTenKH, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Số điện thoại*:"), gbc);
        
        gbc.gridx = 1;
        txtSDT = new JTextField(20);
        txtSDT.setToolTipText("Nhập số điện thoại 10-11 số (bắt buộc)");
        panelForm.add(txtSDT, gbc);

        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Địa chỉ*:"), gbc);
        
        gbc.gridx = 1;
        txtDiaChi = new JTextField(20);
        txtDiaChi.setToolTipText("Nhập địa chỉ (bắt buộc)");
        panelForm.add(txtDiaChi, gbc);

        // Loại khách hàng
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Loại khách hàng:"), gbc);
        
        gbc.gridx = 1;
        cboLoaiKH = new JComboBox<>(new String[]{"Vãng lai", "Thân thiết", "VIP"});
        cboLoaiKH.setToolTipText("Chọn loại khách hàng");
        panelForm.add(cboLoaiKH, gbc);

        add(panelForm, BorderLayout.CENTER);

        // Button panel
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        // Set mnemonics
        btnSave.setMnemonic(KeyEvent.VK_S);
        btnCancel.setMnemonic(KeyEvent.VK_ESCAPE);

        // Set actions
        btnSave.addActionListener(this::saveAction);
        btnCancel.addActionListener(e -> dispose());

        // Add keyboard shortcuts
        bindKeyboardActions();

        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);

        add(panelButtons, BorderLayout.SOUTH);
    }

    private void bindKeyboardActions() {
        // Enter to save
        getRootPane().setDefaultButton(btnSave);

        // Escape to cancel
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    private void saveAction(ActionEvent e) {
        if (validateInput()) {
            saveCustomer();
            dispose();
        }
    }

    private boolean validateInput() {
        // Validate tên
        if (Validator.isEmpty(txtTenKH.getText())) {
            showError("Vui lòng nhập tên khách hàng!", txtTenKH);
            return false;
        }

        // Validate số điện thoại
        String phone = txtSDT.getText().trim();
        if (Validator.isEmpty(phone)) {
            showError("Vui lòng nhập số điện thoại!", txtSDT);
            return false;
        }
        if (!Validator.isValidPhoneNumber(phone)) {
            showError("Số điện thoại phải có 10 số!", txtSDT);
            return false;
        }

        // Validate địa chỉ
        if (Validator.isEmpty(txtDiaChi.getText())) {
            showError("Vui lòng nhập địa chỉ!", txtDiaChi);
            return false;
        }

        return true;
    }

    private void showError(String message, JComponent focusComponent) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Lỗi nhập liệu", 
            JOptionPane.ERROR_MESSAGE);
        focusComponent.requestFocus();
    }

    private void saveCustomer() {
        // Lấy giá trị từ các trường nhập liệu
        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        Integer loaiKH = cboLoaiKH.getSelectedIndex() == 0 ? null : cboLoaiKH.getSelectedIndex();

        if (isEdit) {
            khachHang.setTenKH(tenKH);
            khachHang.setSoDienThoai(sdt);
            khachHang.setDiaChi(diaChi);
            khachHang.setLoaiKH(loaiKH);
        } else {
            // Nếu là thêm mới, tạo khách hàng mới với constructor đầy đủ
            khachHang = new KhachHang(0, tenKH, sdt, diaChi, loaiKH);
        }
    }
    public void setKhachHang(KhachHang kh) {
        this.khachHang = kh;
        this.isEdit = true;

        txtTenKH.setText(kh.getTenKH());
        txtSDT.setText(kh.getSoDienThoai());
        txtDiaChi.setText(kh.getDiaChi());
        cboLoaiKH.setSelectedIndex(kh.getLoaiKH() == null ? 0 : kh.getLoaiKH());
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public boolean isEdit() {
        return isEdit;
    }
}