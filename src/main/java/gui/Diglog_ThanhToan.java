/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import utils.SessionManager;

import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import utils.SessionManager;

/**
 * Dialog thanh toán
 * @author PC
 */
public class Diglog_ThanhToan extends JDialog {

    private double tongTien = 0;
    private double khuyenMai = 0;
    private int soLuongVe = 0;
    private String cccd = "";
    private String hoTen = "";
    private String sdt = "";
    private String email = "";
    private NumberFormat currencyFormat;
    
    private boolean isThanhToanThanhCong = false;
    private boolean isNhapLai = false;
    private boolean isTreoDon = false;
    
    private Gui_NhapThongTinBanVe previousGui; // Để quay lại
    private entity.DonTreoDat donTreo; // Đơn treo (nếu xử lý từ đơn tạm)
    
    /**
     * Creates new form Diglog_ThanhToan
     */
    public Diglog_ThanhToan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
    }
    
    /**
     * Constructor với dữ liệu (từ flow bán vé thường)
     */
    public Diglog_ThanhToan(java.awt.Frame parent, boolean modal, 
                             String cccd, String hoTen, String sdt, String email,
                             int soLuongVe, double tongTien, double khuyenMai,
                             Gui_NhapThongTinBanVe previousGui) {
        super(parent, modal);
        this.cccd = cccd;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
        this.khuyenMai = khuyenMai;
        this.previousGui = previousGui;
        this.donTreo = null; // Không phải đơn treo
        
        initComponents();
        initCustomComponents();
        loadData();
    }
    
    /**
     * Constructor với dữ liệu (từ xử lý đơn tạm)
     */
    public Diglog_ThanhToan(java.awt.Frame parent, boolean modal, 
                             String cccd, String hoTen, String sdt, String email,
                             int soLuongVe, double tongTien, double khuyenMai,
                             entity.DonTreoDat donTreo) {
        super(parent, modal);
        this.cccd = cccd;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
        this.khuyenMai = khuyenMai;
        this.previousGui = null; // Không có previousGui
        this.donTreo = donTreo; // Lưu đơn treo
        
        initComponents();
        initCustomComponents();
        loadData();
    }
    
    private void initCustomComponents() {
        currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        setLocationRelativeTo(null);
        
        // Thêm listener cho checkbox "Nhận đủ tiền"
        chkNhanDuTien.addActionListener(e -> {
            if (chkNhanDuTien.isSelected()) {
                // Tự động điền tổng tiền (đã trừ khuyến mãi) vào ô tiền khách đưa
                double tongTienThucTe = tongTien - khuyenMai;
                txtTienKhachDua.setText(String.valueOf((long)tongTienThucTe));
            } else {
                txtTienKhachDua.setText("");
            }
        });
        
        // Thêm listener cho textfield tiền khách đưa
        txtTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { tinhTienThua(); }
            @Override
            public void removeUpdate(DocumentEvent e) { tinhTienThua(); }
            @Override
            public void changedUpdate(DocumentEvent e) { tinhTienThua(); }
        });
        
        // Thêm ActionListener cho tất cả các nút giá gợi ý
        btn500K.addActionListener(e -> themTienGoiY(500000));
        btn200K.addActionListener(e -> themTienGoiY(200000));
        btn100K.addActionListener(e -> themTienGoiY(100000));
        btn50K.addActionListener(e -> themTienGoiY(50000));
        btn20K.addActionListener(e -> themTienGoiY(20000));
        btn10K.addActionListener(e -> themTienGoiY(10000));
        btn5K.addActionListener(e -> themTienGoiY(5000));
        btn2K.addActionListener(e -> themTienGoiY(2000));
        btn1K.addActionListener(e -> themTienGoiY(1000));
    }
    
    private void loadData() {
        lblCCCDValue.setText(cccd.isEmpty() ? "(Chưa nhập)" : cccd);
        lblHoTenValue.setText(hoTen.isEmpty() ? "(Chưa nhập)" : hoTen);
        lblSDTValue.setText(sdt.isEmpty() ? "(Chưa nhập)" : sdt);
        lblSoLuongVeValue.setText(String.valueOf(soLuongVe));
        
        // Tổng tiền sau khi trừ khuyến mãi
        double tongTienThucTe = tongTien - khuyenMai;
        lblTongTienValue.setText(currencyFormat.format(tongTienThucTe) + " ₫");
        
        lblKhuyenMaiValue.setText(khuyenMai > 0 ? currencyFormat.format(khuyenMai) + " ₫" : "0 ₫");
        lblTienThuaValue.setText("0 ₫");
    }
    
    /**
     * Thêm tiền gợi ý vào tiền khách đưa
     */
    private void themTienGoiY(double soTien) {
        try {
            String currentText = txtTienKhachDua.getText().trim();
            double currentAmount = 0;
            
            if (!currentText.isEmpty()) {
                // Xóa dấu phân cách nếu có
                currentText = currentText.replaceAll("[,.]", "");
                currentAmount = Double.parseDouble(currentText);
            }
            
            double newAmount = currentAmount + soTien;
            txtTienKhachDua.setText(String.valueOf((long)newAmount));
            
        } catch (NumberFormatException e) {
            txtTienKhachDua.setText(String.valueOf((long)soTien));
        }
    }
    
    /**
     * Tính tiền thừa
     */
    private void tinhTienThua() {
        try {
            String text = txtTienKhachDua.getText().trim();
            if (text.isEmpty()) {
                lblTienThuaValue.setText("0 ₫");
                return;
            }
            
            // Xóa dấu phân cách
            text = text.replaceAll("[,.]", "");
            double tienKhachDua = Double.parseDouble(text);
            double tienThua = tienKhachDua - (tongTien - khuyenMai);
            
            if (tienThua < 0) {
                lblTienThuaValue.setText("<html><font color='red'>" + 
                                         currencyFormat.format(tienThua) + " ₫</font></html>");
            } else {
                lblTienThuaValue.setText("<html><font color='green'>" + 
                                         currencyFormat.format(tienThua) + " ₫</font></html>");
            }
            
        } catch (NumberFormatException e) {
            lblTienThuaValue.setText("0 ₫");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new JLabel();
        lblCCCDTitle = new JLabel();
        lblHoTenTitle = new JLabel();
        jPanel1 = new JPanel();
        btn500K = new JButton();
        btn200K = new JButton();
        btn100K = new JButton();
        btn50K = new JButton();
        btn20K = new JButton();
        btn10K = new JButton();
        btn5K = new JButton();
        btn2K = new JButton();
        btn1K = new JButton();
        lblSDTTitle = new JLabel();
        lblSoLuongVeTitle = new JLabel();
        lblTongTienTitle = new JLabel();
        lblKhuyenMaiTitle = new JLabel();
        lblTienKhachDuaTitle = new JLabel();
        lblTienThuaTitle = new JLabel();
        txtTienKhachDua = new JTextField();
        btnNhapLai = new JButton();
        btnTreoDon = new JButton();
        btnThanhToan = new JButton();
        lblCCCDValue = new JLabel();
        lblHoTenValue = new JLabel();
        lblSDTValue = new JLabel();
        lblSoLuongVeValue = new JLabel();
        lblTongTienValue = new JLabel();
        lblKhuyenMaiValue = new JLabel();
        lblTienThuaValue = new JLabel();
        jButton1 = new JButton();
        chkNhanDuTien = new JCheckBox();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(234, 243, 251));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setText("Xác nhận thông tin mua vé tàu");

        lblCCCDTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblCCCDTitle.setText("CCCD:");

        lblHoTenTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblHoTenTitle.setText("Họ tên:");

        jPanel1.setBorder(BorderFactory.createTitledBorder("Giá gợi ý"));

        btn500K.setBackground(new java.awt.Color(234, 243, 251));
        btn500K.setText("500.000");
        btn500K.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn500KActionPerformed(evt);
            }
        });

        btn200K.setBackground(new java.awt.Color(234, 243, 251));
        btn200K.setText("200.000");

        btn100K.setBackground(new java.awt.Color(234, 243, 251));
        btn100K.setText("100.00");

        btn50K.setBackground(new java.awt.Color(234, 243, 251));
        btn50K.setText("50.000");

        btn20K.setBackground(new java.awt.Color(234, 243, 251));
        btn20K.setText("20.000");
        btn20K.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn20KActionPerformed(evt);
            }
        });

        btn10K.setBackground(new java.awt.Color(234, 243, 251));
        btn10K.setText("10.000");

        btn5K.setBackground(new java.awt.Color(234, 243, 251));
        btn5K.setText("5.000");

        btn2K.setBackground(new java.awt.Color(234, 243, 251));
        btn2K.setText("2.000");
        btn2K.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2KActionPerformed(evt);
            }
        });

        btn1K.setBackground(new java.awt.Color(234, 243, 251));
        btn1K.setText("1.000");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn500K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addComponent(btn200K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn100K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn50K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(btn20K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn5K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn1K, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn10K, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(btn2K, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btn500K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn200K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btn100K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn50K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btn20K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn10K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btn5K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(btn1K, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblSDTTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSDTTitle.setText("Số điện thoại:");

        lblSoLuongVeTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuongVeTitle.setText("Số lượng vé:");

        lblTongTienTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTongTienTitle.setText("Tổng tiền:");

        lblKhuyenMaiTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKhuyenMaiTitle.setText("Khuyến mãi:");

        lblTienKhachDuaTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTienKhachDuaTitle.setText("Tiền khách đưa:");

        lblTienThuaTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTienThuaTitle.setText("Tiền thừa:");

        txtTienKhachDua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienKhachDuaActionPerformed(evt);
            }
        });

        btnNhapLai.setIcon(new ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnNhapLai.setText("Nhập lại");
        btnNhapLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhapLaiActionPerformed(evt);
            }
        });

        btnTreoDon.setIcon(new ImageIcon(getClass().getResource("/icon/bill.png"))); // NOI18N
        btnTreoDon.setText("Treo đơn");
        btnTreoDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTreoDonActionPerformed(evt);
            }
        });

        btnThanhToan.setIcon(new ImageIcon(getClass().getResource("/icon/payment.png"))); // NOI18N
        btnThanhToan.setText("Thanh toán ");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        lblCCCDValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblCCCDValue.setText(" ");

        lblHoTenValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblHoTenValue.setText(" ");

        lblSDTValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSDTValue.setText(" ");

        lblSoLuongVeValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuongVeValue.setText(" ");

        lblTongTienValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTongTienValue.setText(" ");

        lblKhuyenMaiValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKhuyenMaiValue.setText(" ");

        lblTienThuaValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTienThuaValue.setText(" ");

        jButton1.setIcon(new ImageIcon(getClass().getResource("/icon/arrow.png"))); // NOI18N
        jButton1.setText("Quay Lại");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        chkNhanDuTien.setText("Nhận đủ tiền");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(btnNhapLai, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(btnTreoDon, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btnThanhToan)
                        .addGap(48, 48, 48))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lblCCCDTitle, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHoTenTitle, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSDTTitle)
                            .addComponent(lblSoLuongVeTitle, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKhuyenMaiTitle)
                            .addComponent(lblTienKhachDuaTitle, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTienThuaTitle)
                            .addComponent(lblTongTienTitle, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienKhachDua, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTongTienValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSoLuongVeValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSDTValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblHoTenValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCCCDValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTienThuaValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chkNhanDuTien)))
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblKhuyenMaiValue, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCCCDTitle, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCCCDValue))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHoTenTitle)
                            .addComponent(lblHoTenValue, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSDTTitle)
                            .addComponent(lblSDTValue))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSoLuongVeTitle)
                            .addComponent(lblSoLuongVeValue))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lblKhuyenMaiTitle)
                            .addComponent(lblKhuyenMaiValue, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTongTienValue)
                            .addComponent(lblTongTienTitle))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTienKhachDuaTitle)
                            .addComponent(txtTienKhachDua, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTienThuaTitle))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(chkNhanDuTien)
                                .addGap(18, 18, 18)
                                .addComponent(lblTienThuaValue)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTreoDon, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNhapLai, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNhapLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhapLaiActionPerformed
        // Chỉ reset/xóa field "Tiền khách đưa"
        txtTienKhachDua.setText("");
        lblTienThuaValue.setText("0 ₫");
        txtTienKhachDua.requestFocus();
    }//GEN-LAST:event_btnNhapLaiActionPerformed

    private void btnTreoDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTreoDonActionPerformed
        // ⚡ VALIDATE "ĐỐI TƯỢNG" TRƯỚC KHI TREO ĐƠN
        if (previousGui != null) {
            javax.swing.table.TableModel model = previousGui.getModelThongTinVe();
            for (int i = 0; i < model.getRowCount(); i++) {
                String doiTuong = model.getValueAt(i, 2) != null ? model.getValueAt(i, 2).toString().trim() : "";
                if (doiTuong.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn Đối tượng cho vé ở dòng " + (i + 1) + " trước khi treo đơn!",
                        "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        
        // Lưu đơn treo
        entity.DonTreoDat donTreo = new entity.DonTreoDat();
        donTreo.setCccdNguoiDat(cccd);
        donTreo.setHoTenNguoiDat(hoTen);
        donTreo.setSdtNguoiDat(sdt);
        donTreo.setEmailNguoiDat(email);
        donTreo.setSoLuongVe(soLuongVe);
        donTreo.setTongTien(tongTien);
        
        // ⚡ Set ngày lập và giờ lập
        donTreo.setNgayLap(java.time.LocalDateTime.now());
        donTreo.setGioLap(java.time.LocalDateTime.now());
        
        // ⚡ Lưu thông tin ga đi, ga đến và LichTrinh
        if (previousGui != null) {
            try {
                entity.LichTrinh lt = previousGui.getPreviousGuiBanVe().getLichTrinhDangChon();
                if (lt != null) {
                    String gaDi = lt.getGaDi() != null ? lt.getGaDi().getTenGa() : "";
                    String gaDen = lt.getGaDen() != null ? lt.getGaDen().getTenGa() : "";
                    donTreo.setGaDi(gaDi);
                    donTreo.setGaDen(gaDen);
                    
                    // ⚡ LƯU LICH TRINH (quan trọng để lưu vé vào database)
                    donTreo.setLichTrinh(lt);
                }
            } catch (Exception e) {
                System.out.println("Không lấy được thông tin ga: " + e.getMessage());
            }
        }
        
        // Lấy thông tin vé từ Gui_NhapThongTinBanVe
        if (previousGui != null) {
            // Lấy dữ liệu từ bảng vé
            javax.swing.table.TableModel model = previousGui.getModelThongTinVe();
            
            // ⚡ LẤY TỪ GUI_NHAPTHONGTINBANVE (không phải từ Gui_BanVe vì đã bị clear)
            java.util.List<entity.ChoNgoi> danhSachChoNgoi = previousGui.getDanhSachChoNgoi();
            
            // ⚡ Lấy danh sách lịch trình (quan trọng cho khứ hồi)
            java.util.List<entity.LichTrinh> danhSachLichTrinh = previousGui.getDanhSachLichTrinh();
            System.out.println("🟡 DEBUG TREO ĐƠN: Số vé trong table = " + model.getRowCount());
            System.out.println("🟡 DEBUG TREO ĐƠN: Số ghế (ChoNgoi) = " + (danhSachChoNgoi != null ? danhSachChoNgoi.size() : "null"));
            System.out.println("🟡 DEBUG TREO ĐƠN: Số lịch trình = " + (danhSachLichTrinh != null ? danhSachLichTrinh.size() : "null"));
            
            for (int i = 0; i < model.getRowCount(); i++) {
                entity.DonTreoDat.ThongTinVeTam veTam = new entity.DonTreoDat.ThongTinVeTam();
                
                Object soGiayTo = model.getValueAt(i, 0);
                Object hoTenVe = model.getValueAt(i, 1);
                Object doiTuong = model.getValueAt(i, 2);
                Object thongTinCho = model.getValueAt(i, 3);
                Object giaVe = model.getValueAt(i, 4);
                Object giamGia = model.getValueAt(i, 5);
                Object thanhTien = model.getValueAt(i, 6);
                
                veTam.setSoGiayTo(soGiayTo != null ? soGiayTo.toString() : "");
                veTam.setHoTen(hoTenVe != null ? hoTenVe.toString() : "");
                veTam.setDoiTuong(doiTuong != null ? doiTuong.toString() : "");
                veTam.setThongTinCho(thongTinCho != null ? thongTinCho.toString() : "");
                
                // ⚡ LƯU THÔNG TIN GHẾ (để khi thanh toán có thể lưu vào database)
                if (danhSachChoNgoi != null && i < danhSachChoNgoi.size()) {
                    veTam.setChoNgoi(danhSachChoNgoi.get(i));
                    System.out.println("🟡 Vé #" + i + ": ChoNgoi = " + danhSachChoNgoi.get(i).getMaChoNgoi());
                } else {
                    System.out.println("❌ Vé #" + i + ": KHÔNG CÓ ChoNgoi!");
                }
                
                // ⚡ LƯU LỊCH TRÌNH CHO TỪNG VÉ (quan trọng với khứ hồi)
                if (danhSachLichTrinh != null && i < danhSachLichTrinh.size()) {
                    veTam.setLichTrinh(danhSachLichTrinh.get(i));
                    System.out.println("🟡 Vé #" + i + ": LichTrinh = " + danhSachLichTrinh.get(i).getMaLichTrinh());
                } else {
                    System.out.println("❌ Vé #" + i + ": KHÔNG CÓ LichTrinh!");
                }
                
                try {
                    veTam.setGiaVe(giaVe != null ? parseDouble(giaVe.toString()) : 0);
                    veTam.setGiamGia(giamGia != null ? parseDouble(giamGia.toString()) : 0);
                    veTam.setThanhTien(thanhTien != null ? parseDouble(thanhTien.toString()) : 0);
                } catch (Exception e) {
                    veTam.setGiaVe(0);
                    veTam.setGiamGia(0);
                    veTam.setThanhTien(0);
                }
                
                // ⚡ LƯU THÔNG TIN GA CHẶNG (quan trọng cho quản lý chặng)
                if (previousGui != null && previousGui.getPreviousGuiBanVe() != null) {
                    entity.ChoNgoi choNgoi = veTam.getChoNgoi();
                    if (choNgoi != null) {
                        veTam.setGaDi(previousGui.getPreviousGuiBanVe().getMapGheGaDi().get(choNgoi));
                        veTam.setGaDen(previousGui.getPreviousGuiBanVe().getMapGheGaDen().get(choNgoi));
                        System.out.println("🟡 Vé #" + i + ": Chặng " + (veTam.getGaDi() != null ? veTam.getGaDi().getMaGa() : "null") + 
                                           " -> " + (veTam.getGaDen() != null ? veTam.getGaDen().getMaGa() : "null"));
                    }
                }

                donTreo.themVe(veTam);
            }
        }
        
        // ⚠️ QUAN TRỌNG: Thêm đơn treo VÀO DANH SÁCH TRƯỚC để tạo maDonTreo
        QuanLyDonTreo.themDonTreo(donTreo);
        
        // SAU ĐÓ mới lưu ghế vào danh sách giữ chỗ (với maDonTreo đã được set)
        // ⚡ Lưu ghế với maLichTrinh để hỗ trợ khứ hồi
        if (previousGui != null) {
            System.out.println("🟡 ============ BẮT ĐẦU TREO ĐƠN ============");
            for (entity.DonTreoDat.ThongTinVeTam veTam : donTreo.getDanhSachVe()) {
                if (veTam.getChoNgoi() != null) {
                    String maChoNgoi = veTam.getChoNgoi().getMaChoNgoi();
                    String maLichTrinh = veTam.getLichTrinh() != null ? veTam.getLichTrinh().getMaLichTrinh() : null;
                    String maGaDi = veTam.getGaDi() != null ? veTam.getGaDi().getMaGa() : null;
                    String maGaDen = veTam.getGaDen() != null ? veTam.getGaDen().getMaGa() : null;
                    System.out.println("🟡 TREO GHẾ: " + maChoNgoi + " | Lịch trình: " + maLichTrinh + " | Chặng: " + maGaDi + "-" + maGaDen);
                    QuanLyGheGiuCho.themGheGiuCho(maChoNgoi, donTreo.getMaDonTreo(), maLichTrinh, maGaDi, maGaDen);
                }
            }
            System.out.println("🟡 ============ KẾT THÚC TREO ĐƠN ============");
        }
        
        isTreoDon = true;
        isThanhToanThanhCong = false;
        isNhapLai = false;
        
        // ⚡ RELOAD SƠ ĐỒ GHẾ SAU KHI TREO ĐƠN để hiển thị ghế màu vàng
        Gui_BanVe guiBanVeToReload = null;
        try {
            if (previousGui != null && previousGui.getPreviousGuiBanVe() != null) {
                guiBanVeToReload = previousGui.getPreviousGuiBanVe();
            }
        } catch (Exception e) {
            System.err.println("Không tìm thấy Gui_BanVe: " + e.getMessage());
        }
        
        final Gui_BanVe finalGuiBanVe = guiBanVeToReload;
        if (finalGuiBanVe != null) {
                System.out.println("🟡 Đang reload sơ đồ ghế sau khi TREO ĐƠN...");
                finalGuiBanVe.reloadSoDoGhe();
                
                // ⚡ Cập nhật số lượng đơn treo lên nút ở Gui_BanVe
                finalGuiBanVe.capNhatSoLuongDonTreo();
                
                System.out.println("✅ Đã reload sơ đồ ghế - ghế giữ chỗ hiển thị màu vàng!");
        }
        
        dispose();
    }//GEN-LAST:event_btnTreoDonActionPerformed
    
    /**
     * Parse string thành double (loại bỏ dấu phân cách)
     */
    private double parseDouble(String str) {
        if (str == null || str.trim().isEmpty()) return 0;
        
        // Xóa tất cả ký tự không phải số (bao gồm cả dấu chấm phân cách nghìn, ₫, khoảng trắng, v.v.)
        str = str.replaceAll("[^0-9]", "");
        
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // Validate dữ liệu
        String tienKhachDuaText = txtTienKhachDua.getText().trim();
        
        if (tienKhachDuaText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập số tiền khách đưa!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtTienKhachDua.requestFocus();
            return;
        }
        
        try {
            // Xóa dấu phân cách
            tienKhachDuaText = tienKhachDuaText.replaceAll("[,.]", "");
            double tienKhachDua = Double.parseDouble(tienKhachDuaText);
            double tongThanhToan = tongTien - khuyenMai;
            
            if (tienKhachDua < tongThanhToan) {
                JOptionPane.showMessageDialog(this,
                    "Số tiền khách đưa không đủ!\n" +
                    "Cần thanh toán: " + currencyFormat.format(tongThanhToan) + " ₫\n" +
                    "Khách đưa: " + currencyFormat.format(tienKhachDua) + " ₫\n" +
                    "Thiếu: " + currencyFormat.format(tongThanhToan - tienKhachDua) + " ₫",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // ⚡ VALIDATE "ĐỐI TƯỢNG" - Kiểm tra tất cả vé đã chọn đối tượng
            if (previousGui != null) {
                javax.swing.table.TableModel model = previousGui.getModelThongTinVe();
                for (int i = 0; i < model.getRowCount(); i++) {
                    String doiTuong = model.getValueAt(i, 2) != null ? model.getValueAt(i, 2).toString().trim() : "";
                    if (doiTuong.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Vui lòng chọn Đối tượng cho vé ở dòng " + (i + 1) + "!",
                            "Thiếu thông tin",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }
            
            // ============ LƯU VÀO DATABASE ============
            String maHoaDon = "HD" + System.currentTimeMillis();
            boolean luuThanhCong = luuVaoDatabase(maHoaDon, cccd, hoTen, sdt, email, soLuongVe, tongTien, khuyenMai);
            
            if (!luuThanhCong) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi lưu dữ liệu vào database!\nVui lòng thử lại.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            isThanhToanThanhCong = true;
            isNhapLai = false;
            isTreoDon = false;
            
            // Lưu parentFrame trước khi dispose
            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
            
            // Đóng dialog thanh toán
            dispose();
            
            // ⚡ MỞ DIALOG IN và tự động in hóa đơn + vé
            Dialog_In dialogIn = new Dialog_In(parentFrame, false, maHoaDon); // false = non-modal để có thể in background
            dialogIn.setVisible(true);
            
            // ⚡ Tự động in hóa đơn và vé (hóa đơn trước, sau đó vé cách nhau 2s)
            dialogIn.autoInHoaDonVaVe();
            
            // ⚡ RELOAD sơ đồ ghế để cập nhật ghế đã bán (màu đỏ)
            Gui_BanVe guiBanVeToReload = null;
            
            if (previousGui != null) {
                // Trường hợp bán vé thường
                guiBanVeToReload = previousGui.getPreviousGuiBanVe();
                if (guiBanVeToReload != null) {
                    // ⚡ KHÔNG gọi xoaTatCaGheDaChon() ở đây vì nó có reload bên trong
                    // Sẽ gọi reloadSoDoGhe() sau để đảm bảo database đã commit
                    // reloadSoDoGhe() sẽ tự động clear danh sách ghế và giỏ vé
                    
                    // Quay về Gui_BanVe
                    gui.menu.form.MainForm mainForm = 
                        (gui.menu.form.MainForm) SwingUtilities.getAncestorOfClass(
                            gui.menu.form.MainForm.class, previousGui);
                    
                    if (mainForm != null) {
                        mainForm.showForm(guiBanVeToReload);
                    }
                }
            } else if (donTreo != null) {
                // ⚡ Trường hợp xử lý đơn tạm → Tìm Gui_BanVe để reload
                System.out.println("🔵 Xử lý đơn treo - Tìm Gui_BanVe để reload...");
                try {
                    // Tìm MainForm
                    gui.menu.form.MainForm mainForm = null;
                    for (java.awt.Window window : java.awt.Window.getWindows()) {
                        if (window instanceof JFrame) {
                            JFrame frame = (JFrame) window;
                            java.awt.Component comp = frame.getContentPane().getComponent(0);
                            if (comp instanceof gui.menu.form.MainForm) {
                                mainForm = (gui.menu.form.MainForm) comp;
                                break;
                            }
                        }
                    }
                    
                    if (mainForm != null) {
                        guiBanVeToReload = findGuiBanVeInMainForm(mainForm);
                        if (guiBanVeToReload != null) {
                            System.out.println("✅ Tìm thấy Gui_BanVe!");
                        } else {
                            System.out.println("⚠️ Không tìm thấy Gui_BanVe trong MainForm");
                        }
                    } else {
                        System.out.println("⚠️ Không tìm thấy MainForm");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Lỗi khi tìm Gui_BanVe: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Reload sơ đồ ghế SAU KHI lưu database
            // ⚡ Sử dụng invokeLater để đảm bảo reload sau khi database đã commit
            final Gui_BanVe finalGuiBanVeToReload = guiBanVeToReload;
            final boolean isFromDonTreo = (donTreo != null);
            
            if (finalGuiBanVeToReload != null) {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🔄 Đang reload sơ đồ ghế sau khi thanh toán...");
                    entity.LichTrinh ltHienTai = finalGuiBanVeToReload.getLichTrinhDangChon();
                    if (ltHienTai != null) {
                        System.out.println("📍 Lịch trình đang hiển thị: " + ltHienTai.getMaLichTrinh());
                    }
                    finalGuiBanVeToReload.reloadSoDoGhe();
                    System.out.println("✅ Đã reload sơ đồ ghế!");
                    System.out.println("💡 LƯU Ý: Nếu đơn khứ hồi, chuyển sang chiều kia sẽ tự động hiển thị màu đỏ khi query database!");
                });
            } else if (isFromDonTreo) {
                // ⚠️ Không tìm thấy Gui_BanVe → Thông báo cho user
                System.out.println("⚠️ Không thể reload sơ đồ ghế vì Gui_BanVe chưa mở");
                System.out.println("💡 Vé đã lưu vào database. Khi mở lại màn hình bán vé, ghế sẽ hiển thị màu đỏ.");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Số tiền không hợp lệ!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void txtTienKhachDuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKhachDuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTienKhachDuaActionPerformed

    private void btn500KActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn500KActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn500KActionPerformed

    private void btn20KActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn20KActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn20KActionPerformed

    private void btn2KActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2KActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn2KActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Quay lại Gui_NhapThongTinBanVe (giữ nguyên dữ liệu đã nhập)
        isNhapLai = true;
        isThanhToanThanhCong = false;
        isTreoDon = false;
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // ==================== HELPER METHODS ====================
    
    /**
     * Tìm Gui_BanVe trong component tree của window
     */
    private Gui_BanVe findGuiBanVe(java.awt.Container container) {
        for (java.awt.Component comp : container.getComponents()) {
            if (comp instanceof Gui_BanVe) {
                return (Gui_BanVe) comp;
            } else if (comp instanceof java.awt.Container) {
                Gui_BanVe result = findGuiBanVe((java.awt.Container) comp);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    
    /**
     * Tìm Gui_BanVe trong MainForm (dùng cho xử lý đơn treo)
     */
    private Gui_BanVe findGuiBanVeInMainForm(gui.menu.form.MainForm mainForm) {
        try {
            // MainForm thường có cấu trúc: MainForm -> Body (JPanel) -> CurrentForm
            java.awt.Component[] components = mainForm.getComponents();
            for (java.awt.Component comp : components) {
                if (comp instanceof java.awt.Container) {
                    Gui_BanVe result = findGuiBanVe((java.awt.Container) comp);
                    if (result != null) {
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm Gui_BanVe: " + e.getMessage());
        }
        return null;
    }
    
    // ==================== GETTERS ====================
    
    public boolean isThanhToanThanhCong() {
        return isThanhToanThanhCong;
    }
    
    public boolean isNhapLai() {
        return isNhapLai;
    }
    
    public boolean isTreoDon() {
        return isTreoDon;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Diglog_ThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Diglog_ThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Diglog_ThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Diglog_ThanhToan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Diglog_ThanhToan dialog = new Diglog_ThanhToan(new JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btn100K;
    private JButton btn10K;
    private JButton btn1K;
    private JButton btn200K;
    private JButton btn20K;
    private JButton btn2K;
    private JButton btn500K;
    private JButton btn50K;
    private JButton btn5K;
    private JButton btnNhapLai;
    private JButton btnThanhToan;
    private JButton btnTreoDon;
    private JCheckBox chkNhanDuTien;
    private JButton jButton1;
    private JPanel jPanel1;
    private JLabel lblCCCDTitle;
    private JLabel lblCCCDValue;
    private JLabel lblHoTenTitle;
    private JLabel lblHoTenValue;
    private JLabel lblKhuyenMaiTitle;
    private JLabel lblKhuyenMaiValue;
    private JLabel lblSDTTitle;
    private JLabel lblSDTValue;
    private JLabel lblSoLuongVeTitle;
    private JLabel lblSoLuongVeValue;
    private JLabel lblTienKhachDuaTitle;
    private JLabel lblTienThuaTitle;
    private JLabel lblTienThuaValue;
    private JLabel lblTitle;
    private JLabel lblTongTienTitle;
    private JLabel lblTongTienValue;
    private JTextField txtTienKhachDua;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Lưu dữ liệu vào database
     * @return true nếu thành công
     */
    private boolean luuVaoDatabase(String maHoaDon, String cccd, String hoTen, String sdt, String email, 
                                     int soLuongVe, double tongTien, double khuyenMai) {
        try {
            // ===== 1. TẠO/LẤY KHÁCH HÀNG =====
            dao.KhachHang_DAO khachHangDAO = new dao.KhachHang_DAO();
            entity.KhachHang kh = khachHangDAO.findByCCCD(cccd);
            
            if (kh == null) {
                // Tạo khách hàng mới
                kh = new entity.KhachHang();
                kh.setMaKH("KH" + System.currentTimeMillis());
                kh.setCCCD(cccd);
                kh.setHoTen(hoTen);
                kh.setSDT(sdt);
                kh.setEmail(email);
                
                // Lấy đối tượng từ vé đầu tiên (người mua vé đầu)
                String doiTuongKH = null;
                
                // Kiểm tra xem là flow bán vé thường hay đơn treo
                if (previousGui != null) {
                    // Flow bán vé thường - lấy từ table
                    javax.swing.table.TableModel model = previousGui.getModelThongTinVe();
                    if (model.getRowCount() > 0) {
                        doiTuongKH = model.getValueAt(0, 2) != null ? model.getValueAt(0, 2).toString() : null;
                    }
                } else if (donTreo != null) {
                    // Flow đơn treo - lấy từ danh sách vé
                    java.util.List<entity.DonTreoDat.ThongTinVeTam> danhSachVe = donTreo.getDanhSachVe();
                    if (!danhSachVe.isEmpty() && danhSachVe.get(0) != null) {
                        doiTuongKH = danhSachVe.get(0).getDoiTuong();
                    }
                }
                
                kh.setDoiTuong(doiTuongKH);
                
                System.out.println("✅ Tạo khách hàng mới - Đối tượng: " + doiTuongKH);
                
                if (!khachHangDAO.them(kh)) {
                    System.out.println("Lỗi: Không thể tạo khách hàng!");
                    return false;
                }
            }
            
            // ===== 2. TẠO HÓA ĐƠN =====
            entity.HoaDon hoaDon = new entity.HoaDon();
            hoaDon.setMaHoaDon(maHoaDon);
            
            // Lấy nhân viên từ session
            entity.NhanVien nv = new entity.NhanVien();
            nv.setMaNhanVien(SessionManager.getInstance().getMaNhanVienDangNhap());
            hoaDon.setNhanVien(nv);
            
            hoaDon.setKhachHang(kh);
            hoaDon.setNgayTao(java.time.LocalDateTime.now());
            hoaDon.setGioTao(java.time.LocalDateTime.now());
            hoaDon.setTongTien(tongTien - khuyenMai);
            hoaDon.setTrangThai(true);
            
            dao.HoaDon_DAO hoaDonDAO = new dao.HoaDon_DAO();
            if (!hoaDonDAO.insertHoaDon(hoaDon)) {
                System.out.println("Lỗi: Không thể tạo hóa đơn!");
                return false;
            }
            
            // ===== 3. LƯU VÉ VÀ CHI TIẾT HÓA ĐƠN =====
            dao.Ve_DAO veDAO = new dao.Ve_DAO();
            dao.ChiTietHoaDon_DAO chiTietDAO = new dao.ChiTietHoaDon_DAO();
            dao.LoaiVe_DAO loaiVeDAO = new dao.LoaiVe_DAO();
            
            // Lấy danh sách vé từ previousGui hoặc donTreo
            if (previousGui != null) {
                // Flow bán vé thường
                javax.swing.table.TableModel model = previousGui.getModelThongTinVe();
                Gui_BanVe guiBanVe = previousGui.getPreviousGuiBanVe();
                entity.LichTrinh lt = guiBanVe.getLichTrinhDangChon();
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    // Lấy thông tin từ table
                    String soCCCD = model.getValueAt(i, 0) != null ? model.getValueAt(i, 0).toString() : "";
                    String tenKH = model.getValueAt(i, 1) != null ? model.getValueAt(i, 1).toString() : "";
                    String doiTuong = model.getValueAt(i, 2) != null ? model.getValueAt(i, 2).toString() : "";
                    String thongTinCho = model.getValueAt(i, 3) != null ? model.getValueAt(i, 3).toString() : "";
                    double giaVe = parseDouble(model.getValueAt(i, 4) != null ? model.getValueAt(i, 4).toString() : "0");
                    double giamGia = parseDouble(model.getValueAt(i, 5) != null ? model.getValueAt(i, 5).toString() : "0");
                    double thanhTien = parseDouble(model.getValueAt(i, 6) != null ? model.getValueAt(i, 6).toString() : "0");
                    
                    // Lấy ChoNgoi và LichTrinh từ Gui_NhapThongTinBanVe
                    entity.ChoNgoi choNgoi = previousGui.getDanhSachChoNgoi().get(i);
                    entity.LichTrinh lichTrinhCuaVe = previousGui.getDanhSachLichTrinh().get(i);
                    
                    // Lấy LoaiVe
                    entity.LoaiVe loaiVe = loaiVeDAO.findByTenLoaiVe(doiTuong);
                    
                    // ⚡ KIỂM TRA NULL
                    if (loaiVe == null) {
                        System.out.println("❌ LỖI: Không tìm thấy loại vé '" + doiTuong + "' trong database!");
                        System.out.println("   → Dữ liệu từ table: CCCD=" + soCCCD + ", Tên=" + tenKH + ", Đối tượng=" + doiTuong);
                        return false;
                    }
                    
                    // Tạo Vé
                    entity.Ve ve = new entity.Ve();
                    ve.setMaVe("V" + System.currentTimeMillis() + "_" + i);
                    ve.setLoaiVe(loaiVe);
                    ve.setMaVach(null); // TODO: Generate mã vạch nếu cần
                    ve.setThoiGianLenTau(lichTrinhCuaVe != null ? lichTrinhCuaVe.getGioKhoiHanh() : null);
                    ve.setGiaVe(giaVe);
                    ve.setKhachHang(kh);
                    ve.setChoNgoi(choNgoi);
                    ve.setLichTrinh(lichTrinhCuaVe);
                    ve.setTrangThai(true);
                    ve.setTenKhachHang(tenKH);
                    ve.setSoCCCD(soCCCD);
                    
                    // ⚡ Lấy ga chặng từ map
                    ve.setGaDi(guiBanVe.getMapGheGaDi().get(choNgoi));
                    ve.setGaDen(guiBanVe.getMapGheGaDen().get(choNgoi));
                    
                    if (!veDAO.insert(ve)) {
                        System.out.println("Lỗi: Không thể lưu vé " + ve.getMaVe());
                        return false;
                    }
                    
                    // Tạo ChiTietHoaDon
                    entity.ChiTietHoaDon cthd = new entity.ChiTietHoaDon();
                    cthd.setHoaDon(hoaDon);
                    cthd.setVe(ve);
                    cthd.setSoLuong(1);
                    cthd.setGiaVe(giaVe);
                    cthd.setMucGiam(giamGia);
                    
                    if (!chiTietDAO.insert(cthd)) {
                        System.out.println("Lỗi: Không thể lưu chi tiết hóa đơn cho vé " + ve.getMaVe());
                        return false;
                    }
                }
                
            } else if (donTreo != null) {
                // Flow xử lý đơn tạm
                java.util.List<entity.DonTreoDat.ThongTinVeTam> danhSachVe = donTreo.getDanhSachVe();
                System.out.println("💳 ========== THANH TOÁN ĐƠN TREO ==========");
                System.out.println("💳 Số vé trong đơn: " + danhSachVe.size());
                
                for (int i = 0; i < danhSachVe.size(); i++) {
                    entity.DonTreoDat.ThongTinVeTam veTam = danhSachVe.get(i);
                    
                    // Lấy LoaiVe
                    entity.LoaiVe loaiVe = loaiVeDAO.findByTenLoaiVe(veTam.getDoiTuong());
                    
                    // ⚡ KIỂM TRA NULL
                    if (loaiVe == null) {
                        System.out.println("❌ LỖI: Không tìm thấy loại vé '" + veTam.getDoiTuong() + "' trong database!");
                        return false;
                    }
                    
                    // ⚡ LẤY LỊCH TRÌNH TỪ VÉ TẠM (quan trọng cho khứ hồi!)
                    entity.LichTrinh lichTrinhCuaVe = veTam.getLichTrinh();
                    if (lichTrinhCuaVe == null) {
                        System.out.println("❌ LỖI: Vé #" + i + " không có lịch trình!");
                        return false;
                    }
                    
                    // ⚡ Nạp lại LichTrinh từ DB để tránh lỗi "no session" (LazyInitializationException)
                    dao.LichTrinh_DAO ltDAO = new dao.LichTrinh_DAO();
                    lichTrinhCuaVe = ltDAO.findByMaLichTrinh(lichTrinhCuaVe.getMaLichTrinh());
                    
                    // Tạo Vé
                    entity.Ve ve = new entity.Ve();
                    String maVe = "V" + System.currentTimeMillis() + "_" + i;
                    ve.setMaVe(maVe);
                    ve.setLoaiVe(loaiVe);
                    ve.setMaVach(null);
                    ve.setThoiGianLenTau(lichTrinhCuaVe != null ? lichTrinhCuaVe.getGioKhoiHanh() : null);
                    ve.setGiaVe(veTam.getGiaVe());
                    ve.setKhachHang(kh);
                    ve.setChoNgoi(veTam.getChoNgoi()); // ⚡ Lấy từ ThongTinVeTam
                    ve.setLichTrinh(lichTrinhCuaVe); // ⚡⚡ FIX: Lấy từ VeTam, KHÔNG phải từ DonTreoDat!
                    ve.setTrangThai(true);
                    ve.setTenKhachHang(veTam.getHoTen());
                    ve.setSoCCCD(veTam.getSoGiayTo());
                    
                    // ⚡ Lấy ga chặng từ đơn treo
                    ve.setGaDi(veTam.getGaDi());
                    ve.setGaDen(veTam.getGaDen());
                    
                    System.out.println("🎫 INSERT Vé: maVe=" + maVe + ", maChoNgoi=" + veTam.getChoNgoi().getMaChoNgoi() + ", maLichTrinh=" + lichTrinhCuaVe.getMaLichTrinh() + ", trangThai=true");
                    
                    if (!veDAO.insert(ve)) {
                        System.out.println("Lỗi: Không thể lưu vé từ đơn treo");
                        return false;
                    }
                    
                    // Tạo ChiTietHoaDon
                    entity.ChiTietHoaDon cthd = new entity.ChiTietHoaDon();
                    cthd.setHoaDon(hoaDon);
                    cthd.setVe(ve);
                    cthd.setSoLuong(1);
                    cthd.setGiaVe(veTam.getGiaVe());
                    cthd.setMucGiam(veTam.getGiamGia());
                    
                    if (!chiTietDAO.insert(cthd)) {
                        System.out.println("Lỗi: Không thể lưu chi tiết hóa đơn từ đơn treo");
                        return false;
                    }
                }
            }
            
            System.out.println("✅ Lưu database thành công! Mã hóa đơn: " + maHoaDon);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi lưu database: " + e.getMessage());
            return false;
        }
    }
}
