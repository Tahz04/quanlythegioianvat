package ui.panels;

import dao.MonAnDAO;
import model.MonAn;
import ui.dialogs.MonAnDialog;
import util.DialogUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class QuanLyMonAnPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa;
    private JTextField txtTimKiem;
    private MonAnDAO monAnDAO = new MonAnDAO();
    private TableRowSorter<DefaultTableModel> sorter;

    public QuanLyMonAnPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSearch = new JPanel(new BorderLayout(5, 5));
        panelSearch.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(200, 25));
        panelSearch.add(new JLabel("Tìm kiếm ( Mã/ Tên món ăn):"), BorderLayout.WEST);
        panelSearch.add(txtTimKiem, BorderLayout.CENTER);
        add(panelSearch, BorderLayout.NORTH);

        // Tạo model cho table
        model = new DefaultTableModel(
            new Object[]{"Mã món", "Tên món", "Danh mục", "Đơn giá", "Số lượng"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 3: return Double.class;
                    case 4: return Integer.class;
                    default: return String.class;
                }
            }
        };

        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Panel chứa các nút chức năng
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");

        panelButtons.add(btnThem);
        panelButtons.add(btnSua);
        panelButtons.add(btnXoa);
        add(panelButtons, BorderLayout.SOUTH);

        // Khởi tạo trạng thái ban đầu cho các nút
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        // Sự kiện khi chọn hàng trong table
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() >= 0;
            btnSua.setEnabled(selected);
            btnXoa.setEnabled(selected);
        });

        // Sự kiện cho các nút
        btnThem.addActionListener(e -> onThem());
        btnSua.addActionListener(e -> onSua());
        btnXoa.addActionListener(e -> onXoa());

        // Sự kiện tìm kiếm khi nhập text
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });

        // Tải dữ liệu ban đầu
        loadData();
    }
    
    private void loadData() {
        model.setRowCount(0);
        List<MonAn> list = monAnDAO.getAll();
        for (MonAn m : list) {
            String tenDM = monAnDAO.getTenDanhMucById(m.getMaDM());
            model.addRow(new Object[]{
                    m.getMaMA(),
                    m.getTenMA(),
                    tenDM,
                    m.getDonGia(),
                    m.getSoLuong()
            });
        }
    }

    private void search() {
        String text = txtTimKiem.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1)); // chỉ lọc theo cột 0 và 1
        }
    }

    private void onThem() {
        MonAnDialog dialog = new MonAnDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        loadData();
    }

    private void onSua() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;
        int modelRow = table.convertRowIndexToModel(selectedRow);
        Integer maMA = (Integer) model.getValueAt(modelRow, 0);
        MonAn monAn = monAnDAO.getById(maMA);
        if (monAn == null) {
            DialogUtil.showError(this, "Không tìm thấy món ăn để sửa.");
            return;
        }

        MonAnDialog dialog = new MonAnDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setMonAn(monAn);
        dialog.setVisible(true);
        loadData();
    }

    private void onXoa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;
        int modelRow = table.convertRowIndexToModel(selectedRow);
        Integer maMA = (Integer) model.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa món ăn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (monAnDAO.delete(maMA)) {
                DialogUtil.showInfo(this, "Xóa món ăn thành công.");
                loadData();
            } else {
                DialogUtil.showError(this, "Xóa món ăn thất bại.");
            }
        }
    }
}