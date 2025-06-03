package ui.dialogs;

import controller.DonHangController;
import model.ThanhToan;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThanhToanDialog extends JDialog {
    private JComboBox<String> cbHinhThuc;
    private JLabel lblSoTien;
    private JButton btnThanhToan;
    private JButton btnHuy;
    private JTextArea taLichSu;
    
    private int maDH;
    private DonHangController controller;

    public ThanhToanDialog(Window owner, int maDH) {
        super(owner, "Thanh toán đơn hàng", ModalityType.APPLICATION_MODAL);
        this.maDH = maDH;
        this.controller = new DonHangController();
        
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
    }

    private void initComponents() {
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlInfo.add(new JLabel("Hình thức thanh toán:"));
        cbHinhThuc = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Momo", "Zalopay"});
        pnlInfo.add(cbHinhThuc);
        
        pnlInfo.add(new JLabel("Số tiền thanh toán:"));
        lblSoTien = new JLabel(String.format("%,.0f VND", controller.getSoTien(maDH)));
        pnlInfo.add(lblSoTien);
        
        add(pnlInfo, BorderLayout.NORTH);
                
        JPanel pnlButtons = new JPanel();
        btnThanhToan = new JButton("Thanh toán");
        btnHuy = new JButton("Hủy");
        pnlButtons.add(btnThanhToan);
        pnlButtons.add(btnHuy);
        add(pnlButtons, BorderLayout.SOUTH);
        
        btnThanhToan.addActionListener(e -> xuLyThanhToan());
        btnHuy.addActionListener(e -> dispose());
    }


    private void xuLyThanhToan() {
        String hinhThuc = (String) cbHinhThuc.getSelectedItem();
        
        if (controller.thanhToanDonHang(maDH, hinhThuc)) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            lblSoTien.setText(String.format("%,.0f VND", controller.getSoTien(maDH))); // Cập nhật số tiền còn lại
        } else {
            JOptionPane.showMessageDialog(this, 
                "Thanh toán thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}