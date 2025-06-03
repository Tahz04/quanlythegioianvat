package ui.panels;

import controller.ThongKeController;
import util.FormatUtil;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Map;

public class ThongKePanel extends JPanel {
    private ThongKeController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> cboYear;
    private JLabel lblTongNam;
    private JButton btnThongKe;
    private JLabel lblTitle;
    private JPanel filterPanel;
    private JPanel resultPanel;

    public ThongKePanel() {
        controller = new ThongKeController();
        initComponents();
        initEvents();
        loadDefaultData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        lblTitle = new JLabel("THỐNG KÊ DOANH THU THEO THÁNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Filter panel
        filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("LỌC DỮ LIỆU"));
        filterPanel.setBackground(new Color(240, 240, 240));

        filterPanel.add(new JLabel("Năm:"));
        cboYear = new JComboBox<>();
        initYearComboBox();
        filterPanel.add(cboYear);

        btnThongKe = new JButton("THỐNG KÊ");
        styleButton(btnThongKe);
        filterPanel.add(btnThongKe);

        add(filterPanel, BorderLayout.CENTER);

        // Result panel
        resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBorder(BorderFactory.createTitledBorder("KẾT QUẢ THỐNG KÊ"));
        resultPanel.setBackground(Color.WHITE);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Tháng", "Doanh thu (VNĐ)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Double.class;
            }
        };

        table = new JTable(tableModel);
        styleTable();
        JScrollPane scrollPane = new JScrollPane(table);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBackground(Color.WHITE);
        lblTongNam = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTongNam.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongNam.setForeground(new Color(0, 153, 0));
        summaryPanel.add(lblTongNam);
        resultPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(resultPanel, BorderLayout.SOUTH);
    }

    private void initYearComboBox() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear; year >= currentYear - 10; year--) {
            cboYear.addItem(year);
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void styleTable() {
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setGridColor(new Color(220, 220, 220));
        
        // Khởi tạo RowSorter để có thể sắp xếp
        table.setAutoCreateRowSorter(true);

        // Center align month column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Right align revenue column with currency format
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = FormatUtil.formatCurrency(((Number) value).doubleValue());
                }
                return super.getTableCellRendererComponent(table, value, isSelected, 
                    hasFocus, row, column);
            }
        };
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
    }

    private void initEvents() {
        btnThongKe.addActionListener(e -> {
            int year = (Integer) cboYear.getSelectedItem();
            loadDataForYear(year);
        });
    }

    private void loadDefaultData() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        loadDataForYear(currentYear);
    }

    private void loadDataForYear(int year) {
        SwingWorker<List<Map<String, Object>>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Map<String, Object>> doInBackground() throws Exception {
                return controller.getDoanhThuTheoThang(year);
            }

            @Override
            protected void done() {
                try {
                    List<Map<String, Object>> data = get();
                    updateTable(data, year);
                } catch (Exception ex) {
                    String errorMsg = "Lỗi khi tải dữ liệu";
                    if (ex.getCause() != null) {
                        errorMsg = ex.getCause().getMessage();
                    }
                    JOptionPane.showMessageDialog(ThongKePanel.this,
                        errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);

                    // Hiển thị bảng trống khi có lỗi
                    tableModel.setRowCount(0);
                    lblTongNam.setText("Tổng doanh thu năm: 0 VNĐ");
                }
            }
        };
        worker.execute();
    }
    private void updateTable(List<Map<String, Object>> data, int year) {
        tableModel.setRowCount(0);
        double totalRevenue = 0;

        if (data != null && !data.isEmpty()) {
            for (Map<String, Object> row : data) {
                int month = (int) row.get("thang");
                double revenue = (double) row.get("doanhThu");
                totalRevenue += revenue;

                tableModel.addRow(new Object[]{
                    String.format("Tháng %02d", month),
                    revenue
                });
            }
            
            // Sắp xếp theo tháng (đảm bảo RowSorter đã được khởi tạo)
            if (table.getRowSorter() != null) {
                table.getRowSorter().toggleSortOrder(0);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Không có dữ liệu doanh thu cho năm " + year,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        lblTongNam.setText(String.format("Tổng doanh thu năm %d: %s VNĐ", 
            year, FormatUtil.formatCurrency(totalRevenue)));
    }
}