package gui;

import service.IDashboardService;
import utils.ClientContext;

import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.time.LocalDate;

import java.util.Locale;

import java.util.Map;
import com.toedter.calendar.JDateChooser;
import java.util.Date;


// JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Gui_Dashboard extends JPanel {

    private JLabel lblDoanhThu, lblSoVe, lblKhachHang, lblKhuyenMai;
    private IDashboardService dashboardService;
    private JPanel panelChart;
    private JPanel pieChartVePanel;  // Panel chứa biểu đồ tròn tỷ lệ vé
    private JPanel rightPanel;       // Panel bên phải chứa các biểu đồ tròn

    private JTable tuyenTable;
    private JSpinner spinnerNgay;
    private JSpinner spinnerThang;
    private DefaultTableModel modelTuyen;


    public Gui_Dashboard() {
        setLayout(new BorderLayout());
        dashboardService = ClientContext.getDashboardService();

        // ================= PANEL THỐNG KÊ =================
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 20, 10));
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelStats.setBackground(Color.WHITE);

        lblDoanhThu = createStatCard("Tổng doanh thu", "0",
                new Color(230, 244, 234), new Color(46, 125, 50));
        lblSoVe = createStatCard("Vé đã bán", "0",
                new Color(227, 242, 253), new Color(21, 101, 192));
        lblKhuyenMai = createStatCard("Khuyến mãi sắp hết hạn", "0",
                new Color(227, 252, 242), new Color(81, 197, 192));
        lblKhachHang = createStatCard("Khách hàng", "0",
                new Color(255, 248, 225), new Color(245, 124, 0));


        panelStats.add(lblDoanhThu.getParent());
        panelStats.add(lblSoVe.getParent());
        panelStats.add(lblKhuyenMai.getParent());
        panelStats.add(lblKhachHang.getParent());

        panelChart = new JPanel(new BorderLayout(20, 0));
        panelChart.setPreferredSize(new Dimension(900, 350));
        panelChart.setBackground(Color.WHITE);
        panelChart.setBorder(BorderFactory.createTitledBorder("Thống kê tổng quan"));

        add(panelStats, BorderLayout.NORTH);
        add(panelChart, BorderLayout.CENTER);


        SwingUtilities.invokeLater(this::loadData);
    }

    // ================= LOAD DATA =================
    private void loadData() {
        try {
            Map<String, Double> thongKe = dashboardService.getThongKeTongQuan();
            int soKmSapHetHan = dashboardService.getSoKhuyenMaiSapHetHan(7);

            DecimalFormat df = new DecimalFormat("#,### ₫");
            lblDoanhThu.setText(df.format(thongKe.getOrDefault("doanhThu", 0.0)));
            lblSoVe.setText(String.format("%.0f vé", thongKe.getOrDefault("soVeBan", 0.0)));
            lblKhuyenMai.setText(String.format("%d khuyến mãi", soKmSapHetHan));

            loadChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void capNhatKhachHangMoi(Map<String, Double> thongKe, JLabel lblKhachHang) {
        // Lấy số khách hàng mới, nếu không có dữ liệu thì mặc định = 0
        double soKhachMoi = thongKe.getOrDefault("ptKhachHang", 0.0);

        // Cập nhật text cho label, hiển thị số nguyên
        lblKhachHang.setText(String.format("%+.0f khách hàng mới", soKhachMoi));
    }


    private void loadChart() {
        try {
            int nam = LocalDate.now().getYear();
            LocalDate today = LocalDate.now();

            // ================= DATA =================
            Map<Integer, Double> doanhThuTheoThang = dashboardService.getDoanhThuTheoThang(nam);
            Map<Integer, Integer> soVeTheoThang = dashboardService.getSoVeTheoThang(nam);
            Map<String, Double> thongKe = dashboardService.getThongKeNgay(today);
            capNhatKhachHangMoi(thongKe, lblKhachHang);


        DefaultCategoryDataset doanhThuDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset soVeDataset = new DefaultCategoryDataset();

        for (int thang = 1; thang <= 12; thang++) {
            doanhThuDataset.addValue(doanhThuTheoThang.getOrDefault(thang, 0.0), "Doanh thu", String.valueOf(thang));
            soVeDataset.addValue(soVeTheoThang.getOrDefault(thang, 0), "Số vé", String.valueOf(thang));
        }


        // ================= COMBO CHART (BAR + LINE) =================
        JFreeChart comboChart = ChartFactory.createBarChart(
                "Xu hướng doanh thu & Số vé theo theo tháng " + nam,
                "Tháng",
                "Doanh thu (VND)",
                doanhThuDataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        CategoryPlot plot = comboChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Bar Renderer - Doanh thu
        BarRenderer barRenderer = new BarRenderer();
        barRenderer.setSeriesPaint(0, new Color(79, 129, 189));
        barRenderer.setMaximumBarWidth(0.2);
        barRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator(
                "{0} tháng {1}: {2} ₫", new DecimalFormat("#,###")
        ));
        plot.setRenderer(0, barRenderer);

        // Trục trái
        NumberAxis doanhThuAxis = (NumberAxis) plot.getRangeAxis();
        doanhThuAxis.setNumberFormatOverride(new DecimalFormat("#,### ₫"));

        // Trục phải - Số vé
        NumberAxis soVeAxis = new NumberAxis("Số vé");
        soVeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.setRangeAxis(1, soVeAxis);
        plot.setDataset(1, soVeDataset);
        plot.mapDatasetToRangeAxis(1, 1);

        // Line Renderer - Số vé
        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
        lineRenderer.setSeriesPaint(0, new Color(192, 80, 77));
        lineRenderer.setSeriesStroke(0, new BasicStroke(3.0f));
        lineRenderer.setSeriesShapesVisible(0, true);
        lineRenderer.setSeriesShape(0, new Ellipse2D.Double(-5, -5, 10, 10));
        lineRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator(
                "{0} tháng {1}: {2} vé", new DecimalFormat("#")
        ));
        plot.setRenderer(1, lineRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.15);
        domainAxis.setLowerMargin(0.05);
        domainAxis.setUpperMargin(0.05);

        // ChartPanel với tooltip
        ChartPanel comboChartPanel = new ChartPanel(comboChart);
        comboChartPanel.setPreferredSize(new Dimension(900, 500));
        comboChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        comboChartPanel.setDisplayToolTips(true);

// ================= DANH SÁCH TUYẾN ================

        java.util.List<String> danhSachTuyen = Arrays.asList(
                "DN-HN",
                "HN-DN",
                "HN-HUE",
                "HN-SG",
                "HUE-HN",
                "NT-SG",
                "PT-SG",
                "SG-HN",
                "SG-NT",
                "SG-PT"
        );

// ================= LẤY DỮ LIỆU =================
        Map<String, Double> doanhThuTheoTuyen =
                dashboardService.getDoanhThuTheoTuyenTrongThang(today.getMonthValue(), today.getYear());

// ================= DATASET =================
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : doanhThuTheoTuyen.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
        }
// ================= BAR CHART (NẰM NGANG) =================
        JFreeChart chart = ChartFactory.createBarChart(
                "Doanh thu theo tuyến trong tháng",
                "Tuyến",
                "Số tiền (VND)",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

// ================= PLOT =================
        CategoryPlot bplot = chart.getCategoryPlot();
        bplot.setBackgroundPaint(Color.WHITE);
        bplot.setRangeGridlinePaint(Color.GRAY);

// ================= TRỤC X (TIỀN) =================
        NumberAxis xAxis = (NumberAxis) bplot.getRangeAxis();
        xAxis.setAutoRangeIncludesZero(true);
        xAxis.setNumberFormatOverride(
                NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
        );

// ================= TRỤC Y (TUYẾN) =================
        CategoryAxis yAxis = bplot.getDomainAxis();
        yAxis.setLabel(null); // Ẩn chữ "Tuyến" bên trái

// ================= LEGEND + TUYẾN DƯỚI =================
        LegendTitle legend = chart.getLegend();
        chart.removeLegend();                 // Gỡ legend mặc định
        legend.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legend);            // Thêm legend xuống dưới

        TextTitle tuyenTitle = new TextTitle("Tuyến");
        tuyenTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        tuyenTitle.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(tuyenTitle);        // "Tuyến" dưới cùng

// ================= RENDERER =================
        BarRenderer renderer = (BarRenderer) bplot.getRenderer();
        renderer.setSeriesPaint(0, new Color(90, 120, 159));
        renderer.setMaximumBarWidth(0.15);
        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator(
                        "{2}",
                        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
                )
        );
        renderer.setDefaultItemLabelsVisible(true);

        // Pie chart
        Map<String, Double> tk = dashboardService.getThongKeTongQuan();
        pieChartVePanel = createPieChartPanel(
                tk.getOrDefault("soVeBan", 0.0),
                tk.getOrDefault("soVeTra", 0.0)
        );



        // ================= PIE CHART: TỶ LỆ VÉ THEO TUYẾN =================
        Map<String, Integer> soVeTheoTuyen =
                dashboardService.getSoVeTheoTuyen(
                        LocalDate.now().getDayOfMonth(),
                        LocalDate.now().getMonthValue(),
                        8  // Lấy top 8 tuyến
                );

        JPanel pieChartTuyenPanel = createPieChartTuyenPanel(soVeTheoTuyen);

// Panel bên phải
        rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(pieChartVePanel, BorderLayout.NORTH);
        rightPanel.add(pieChartTuyenPanel, BorderLayout.CENTER);

// ================= CHART PANEL =================
        ChartPanel tuyenChartPanel = new ChartPanel(chart);
        tuyenChartPanel.setPreferredSize(new Dimension(900, 450));
        tuyenChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        tuyenChartPanel.setDisplayToolTips(true);

// ================= GHÉP GIAO DIỆN =================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(comboChartPanel);  // Combo chart doanh thu + số vé
        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(tuyenChartPanel);  // Biểu đồ tuyến ngang

        panelChart.removeAll();
        panelChart.setLayout(new BorderLayout(20, 0));
        panelChart.add(leftPanel, BorderLayout.CENTER);
        panelChart.add(rightPanel, BorderLayout.EAST);

        panelChart.revalidate();
        panelChart.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CARD =================
    private JLabel createStatCard(String title, String value, Color bg, Color fg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new Dimension(160, 80));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(fg);

        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(fg);
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return v;
    }

    /**
     * Tạo panel chứa filter + bảng số chỗ trống theo tuyến
     */
    private JPanel createTuyenPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 5));
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createTitledBorder("Số chỗ trống theo tuyến"));

        // ================= FILTER PANEL =================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filterPanel.setBackground(Color.WHITE);

        JComboBox<String> cboFilter = new JComboBox<>(new String[]{"Hôm nay", "Tuần này", "Tháng này"});
        cboFilter.setPreferredSize(new Dimension(120, 28));
        cboFilter.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cboFilter.setSelectedIndex(0); // Mặc định "Hôm nay"

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(130, 28));
        dateChooser.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(null); // Mặc định không chọn ngày (để người dùng tự chọn nếu muốn)

        JButton btnApply = new JButton("Áp dụng");
        btnApply.setPreferredSize(new Dimension(85, 28));
        btnApply.setBackground(new Color(0, 120, 215));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnApply.setFocusPainted(false);

        filterPanel.add(cboFilter);
        filterPanel.add(dateChooser);
        filterPanel.add(btnApply);

        // ================= TABLE =================
        // Mặc định load dữ liệu "Hôm nay"
        Map<String, Integer> soChoConTrong = new HashMap<>();
        try {
            soChoConTrong = dashboardService.getSoChoNgoiConTrongTheoTuyen(LocalDate.now());
        } catch (Exception e) { e.printStackTrace(); }
        JTable tuyenTable = createTuyenTable(soChoConTrong);

        JScrollPane scrollPaneTuyen = new JScrollPane(tuyenTable);
        scrollPaneTuyen.setPreferredSize(new Dimension(400, 250));

        // ================= EVENT: BẤM ÁP DỤNG =================
        btnApply.addActionListener(e -> {
            LocalDate ngayLoc;
            
            // Ưu tiên: Nếu người dùng chọn ngày trong date picker → dùng ngày đó
            if (dateChooser.getDate() != null) {
                ngayLoc = dateChooser.getDate().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                System.out.println("🔍 Lọc số chỗ trống | Chọn ngày: " + ngayLoc);
            } else {
                // Nếu không chọn ngày cụ thể → dùng combo box
                int filterIndex = cboFilter.getSelectedIndex();
                switch (filterIndex) {
                    case 0: // Hôm nay
                        ngayLoc = LocalDate.now();
                        break;
                    case 1: // Tuần này (tính từ hôm nay trở về trước 7 ngày)
                        ngayLoc = LocalDate.now().minusDays(7);
                        break;
                    case 2: // Tháng này (tính từ hôm nay trở về trước 1 tháng)
                        ngayLoc = LocalDate.now().minusMonths(1);
                        break;
                    default:
                        ngayLoc = LocalDate.now();
                }
                System.out.println("🔍 Lọc số chỗ trống | Filter: " + cboFilter.getSelectedItem() + " | Ngày: " + ngayLoc);
            }

            // Reload bảng với ngày lọc
            Map<String, Integer> newData = new HashMap<>();
            try {
                newData = dashboardService.getSoChoNgoiConTrongTheoTuyen(ngayLoc);
            } catch (Exception ex) { ex.printStackTrace(); }
            JTable newTable = createTuyenTable(newData);
            scrollPaneTuyen.setViewportView(newTable);
            scrollPaneTuyen.revalidate();
            scrollPaneTuyen.repaint();
        });

        // ================= LAYOUT =================
        wrapper.add(filterPanel, BorderLayout.NORTH);
        wrapper.add(scrollPaneTuyen, BorderLayout.CENTER);

        return wrapper;
    }

    private JTable createTuyenTable(Map<String, Integer> soChoNgoiTheoTuyen) {
        String[] columns = {"Tuyến", "Số ghế trống"};

        // Lấy tất cả tuyến, sắp xếp giảm dần theo số ghế
        java.util.List<Map.Entry<String, Integer>> allTuyen = soChoNgoiTheoTuyen.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .toList();

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Map.Entry<String, Integer> entry : allTuyen) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        return table;
    }


    private JPanel createPieChartPanel(double ban, double tra) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Vé đã bán", ban);
        dataset.setValue("Vé đã trả", tra);

        JFreeChart chart = ChartFactory.createPieChart("Tỷ lệ vé", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setBackgroundPaint(Color.WHITE);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(400, 300));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.add(panel);
        return wrap;
    }
    private void updateSoChoTrongTheoTuyen() {
        Date ngay = (Date) spinnerNgay.getValue();
        Date thang = (Date) spinnerThang.getValue();


        Calendar cal = Calendar.getInstance();
        cal.setTime(ngay);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(thang);
        int month = cal.get(Calendar.MONTH) + 1;

        modelTuyen.setRowCount(0);

        try {
            Map<String, Integer> data =
                    dashboardService.getSoChoNgoiConTrongTheoTuyen(day, month);

            for (Map.Entry<String, Integer> e : data.entrySet()) {
                modelTuyen.addRow(new Object[]{
                        e.getKey(),
                        e.getValue()
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JPanel createPieChartTuyenPanel(Map<String, Integer> soVeTheoTuyen) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Thêm dữ liệu vào dataset
        for (Map.Entry<String, Integer> entry : soVeTheoTuyen.entrySet()) {
            String tuyen = entry.getKey();
            int soVe = entry.getValue();
            dataset.setValue(tuyen, soVe);
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Tỷ lệ vé đặt theo tuyến", 
                dataset, 
                true,  // legend
                true,  // tooltips
                false  // urls
        );
        
        chart.setBackgroundPaint(Color.WHITE);
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}")); // Chỉ hiển thị %
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(380, 300));
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(chartPanel, BorderLayout.CENTER);
        
        return wrapper;
    }

    private void loadTableSoChoTrong(int day, int month) {
        DefaultTableModel model = (DefaultTableModel) tuyenTable.getModel();
        model.setRowCount(0);

        try {
            Map<String, Integer> data =
                    dashboardService.getSoChoNgoiConTrongTheoTuyen(day, month);

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                model.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue()
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Refresh toàn bộ dữ liệu Dashboard (gọi sau khi trả vé)
     */
    public void refreshData() {
        System.out.println("🔄 Refreshing Dashboard data...");
        
        // Reload dữ liệu thống kê tổng quan
        try {
            Map<String, Double> thongKe = dashboardService.getThongKeTongQuan();
            
            // Cập nhật lại pie chart tỷ lệ vé
            if (pieChartVePanel != null && rightPanel != null) {
                JPanel newPieChartVePanel = createPieChartPanel(
                        thongKe.getOrDefault("soVeBan", 0.0),
                        thongKe.getOrDefault("soVeTra", 0.0)
                );
                
                // Thay thế panel cũ bằng panel mới
                rightPanel.remove(pieChartVePanel);
                rightPanel.add(newPieChartVePanel, BorderLayout.NORTH);
                pieChartVePanel = newPieChartVePanel;
                
                // Refresh UI
                rightPanel.revalidate();
                rightPanel.repaint();
                
                System.out.println("✅ Dashboard refreshed - Vé bán: " + thongKe.getOrDefault("soVeBan", 0.0) 
                        + ", Vé trả: " + thongKe.getOrDefault("soVeTra", 0.0));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

}
