package ui.dialogs;

import model.ChiTietDonHang;
import javax.swing.*;
import java.awt.*;

public class ChiTietDonHangDialog extends JDialog {
    private JTextField tfMaMA, tfSoLuong;
    private JButton btnOK, btnCancel;
    private boolean saved = false;
    private ChiTietDonHang chiTiet;

    public ChiTietDonHangDialog(Window owner, ChiTietDonHang existing, boolean viewOnly) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setTitle(existing == null ? "Thêm món" : "Sửa món");
        setSize(300, 150);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(3, 2, 5, 5));

        tfMaMA = new JTextField();
        tfSoLuong = new JTextField();
        btnOK = new JButton(viewOnly ? "Đóng" : "Lưu");
        btnCancel = new JButton("Hủy");

        add(new JLabel("Mã món:"));
        add(tfMaMA);
        add(new JLabel("Số lượng:"));
        add(tfSoLuong);
        add(btnOK);
        add(viewOnly ? new JLabel("") : btnCancel);

        if (existing != null) {
            tfMaMA.setText(String.valueOf(existing.getMaMA()));
            tfSoLuong.setText(String.valueOf(existing.getSoLuong()));
        }

        btnOK.addActionListener(e -> {
            try {
                int maMA = Integer.parseInt(tfMaMA.getText().trim());
                int sl = Integer.parseInt(tfSoLuong.getText().trim());
                
                if (sl <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải > 0");
                    return;
                }
                
                chiTiet = new ChiTietDonHang(0, maMA, sl);
                saved = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ");
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isSaved() { return saved; }
    public ChiTietDonHang getChiTiet() { return chiTiet; }
}