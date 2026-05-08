-- ========================================
-- HỆ THỐNG QUẢN LÝ VÉ TÀU - RESET & INSERT DỮ LIỆU TOÀN DIỆN (FULL VERSION - FIXED SCHEMA)
-- File: ResetAndInsertData.sql
-- ========================================

USE HTQLVT;
GO

-- ========================================
-- BƯỚC 1: XÓA TẤT CẢ DỮ LIỆU CŨ
-- ========================================
PRINT N'🗑️  Bắt đầu xóa dữ liệu cũ...';
GO
DELETE FROM ChiTietKhuyenMai;
DELETE FROM ChiTietHoaDon;
DELETE FROM ThongTinVeTam;
DELETE FROM DonTreoDat;
DELETE FROM Ve;
DELETE FROM ChoNgoi;
DELETE FROM Toa;
DELETE FROM LichTrinh;
DELETE FROM ChuyenTau;
DELETE FROM HoaDon;
DELETE FROM KhachHang;
DELETE FROM TaiKhoan;
DELETE FROM NhanVien;
DELETE FROM KhuyenMai;
DELETE FROM LoaiTau;
DELETE FROM LoaiToa;
DELETE FROM LoaiVe;
DELETE FROM BangGioGa;
DELETE FROM Ga;
DELETE FROM Tuyen;
GO

-- ========================================
-- BƯỚC 2: CẤU HÌNH HỆ THỐNG (LOOKUP)
-- ========================================

INSERT INTO LoaiTau VALUES ('SE', N'Tàu Khách Siêu Tốc'), ('TN', N'Tàu Thống Nhất'), ('SPT', N'Tàu Sài Gòn - Phan Thiết');
INSERT INTO LoaiToa VALUES ('LTOA001', N'Ngồi mềm điều hòa'), ('LTOA002', N'Giường nằm 4 khoang'), ('LTOA003', N'Giường nằm 6 khoang');
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, mucGiamGia) VALUES ('LV01', N'Người lớn', 0.00), ('LV02', N'Sinh viên', 0.10), ('LV03', N'Trẻ em', 0.25), ('LV04', N'Người cao tuổi', 0.15);

INSERT INTO Ga (maGa, tenGa, viTri) VALUES 
('HN', N'Hà Nội', N'120 Lê Duẩn, Hoàn Kiếm, Hà Nội'),
('ND', N'Nam Định', N'Trần Đăng Ninh, TP. Nam Định'),
('V', N'Vinh', N'01 Lệ Ninh, TP. Vinh, Nghệ An'),
('HUE', N'Huế', N'02 Bùi Thị Xuân, TP. Huế'),
('DN', N'Đà Nẵng', N'202 Hải Phòng, Q. Thanh Khê, Đà Nẵng'),
('QN', N'Quảng Ngãi', N'01 Nguyễn Chánh, TP. Quảng Ngãi'),
('NT', N'Nha Trang', N'17 Thái Nguyên, TP. Nha Trang'),
('SG', N'Sài Gòn', N'01 Nguyễn Thông, Quận 3, TP.HCM');

INSERT INTO Tuyen VALUES ('HN-SG', N'Tuyến Đường Sắt Bắc Nam (Xuôi)', 1726.0), ('SG-HN', N'Tuyến Đường Sắt Bắc Nam (Ngược)', 1726.0);
INSERT INTO BangGioGa VALUES ('HN-SG', 'HN', 1, 0), ('HN-SG', 'ND', 2, 87), ('HN-SG', 'V', 3, 232), ('HN-SG', 'HUE', 4, 369), ('HN-SG', 'DN', 5, 103), ('HN-SG', 'QN', 6, 131), ('HN-SG', 'NT', 7, 500), ('HN-SG', 'SG', 8, 304);
INSERT INTO BangGioGa VALUES ('SG-HN', 'SG', 1, 0), ('SG-HN', 'NT', 2, 304), ('SG-HN', 'QN', 3, 500), ('SG-HN', 'DN', 4, 131), ('SG-HN', 'HUE', 5, 103), ('SG-HN', 'V', 6, 369), ('SG-HN', 'ND', 7, 232), ('SG-HN', 'HN', 8, 87);
GO

-- 🚂 Đang khởi tạo đội tàu (SE1-SE10)
INSERT INTO ChuyenTau (soHieuTau, maLoaiTau, tocDo, namSanXuat) VALUES 
('SE1', 'SE', 120, 2020), ('SE3', 'SE', 120, 2021), ('SE5', 'SE', 120, 2020), ('SE7', 'SE', 120, 2022), ('SE9', 'SE', 120, 2021),
('SE2', 'SE', 120, 2020), ('SE4', 'SE', 120, 2021), ('SE6', 'SE', 120, 2020), ('SE8', 'SE', 120, 2022), ('SE10', 'SE', 120, 2021);

-- Khởi tạo Toa cho tất cả các tàu (mỗi tàu 10 toa)
INSERT INTO Toa (maToa, soHieuTau, soToa, maLoaiToa)
SELECT CONCAT('T', soHieuTau, '-', FORMAT(n, '00')), soHieuTau, n, 
       CASE 
            WHEN n <= 5 THEN 'LTOA001' -- Toa 1-5: Ngồi mềm
            ELSE 'LTOA003'             -- Toa 6-10: Giường nằm 6
       END
FROM ChuyenTau CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10)) AS T(n);

-- Tạo ghế/giường cho từng loại toa
DECLARE @t INT = 1;

-- 1. Tạo 64 ghế cho các Toa Ngồi Mềm (LTOA001)
WHILE @t <= 64
BEGIN
    INSERT INTO ChoNgoi (maChoNgoi, maToa, viTri, gia, moTa) 
    SELECT CONCAT(maToa, '-', FORMAT(@t, '00')), maToa, @t, 500000, N'Ghế ngồi mềm' 
    FROM Toa WHERE maLoaiToa = 'LTOA001';
    SET @t = @t + 1;
END

-- 2. Tạo 42 giường cho các Toa Khoang 6 (LTOA003) - 7 khoang x 6 giường
SET @t = 1;
WHILE @t <= 42
BEGIN
    INSERT INTO ChoNgoi (maChoNgoi, maToa, viTri, gia, moTa) 
    SELECT CONCAT(maToa, '-', FORMAT(@t, '00')), maToa, @t, 700000, N'Giường nằm khoang 6' 
    FROM Toa WHERE maLoaiToa = 'LTOA003';
    SET @t = @t + 1;
END

-- Lịch trình thực tế (5 chuyến/chiều/ngày)
DECLARE @Ngay DATE = '2026-04-01';
WHILE @Ngay <= '2026-05-31'
BEGIN
    DECLARE @ms NVARCHAR(10) = FORMAT(@Ngay, 'ddMMyy');
    
    -- HƯỚNG BẮC -> NAM (LẺ): SE1, SE3, SE5, SE7, SE9
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
    VALUES ('LT1-'+@ms, 'SE1', 'HN-SG', 'HN', 'SG', CAST(@Ngay AS DATETIME)+' 06:00', CAST(@Ngay AS DATETIME)+' 20:00', 1),
           ('LT3-'+@ms, 'SE3', 'HN-SG', 'HN', 'SG', CAST(@Ngay AS DATETIME)+' 09:00', CAST(@Ngay AS DATETIME)+' 23:30', 1),
           ('LT5-'+@ms, 'SE5', 'HN-SG', 'HN', 'SG', CAST(@Ngay AS DATETIME)+' 15:00', CAST(@Ngay AS DATETIME)+' 05:00', 1),
           ('LT7-'+@ms, 'SE7', 'HN-SG', 'HN', 'SG', CAST(@Ngay AS DATETIME)+' 19:00', CAST(@Ngay AS DATETIME)+' 09:00', 1),
           ('LT9-'+@ms, 'SE9', 'HN-SG', 'HN', 'SG', CAST(@Ngay AS DATETIME)+' 22:00', CAST(@Ngay AS DATETIME)+' 12:00', 1);
    
    -- HƯỚNG NAM -> BẮC (CHẴN): SE2, SE4, SE6, SE8, SE10
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
    VALUES ('LT2-'+@ms, 'SE2', 'SG-HN', 'SG', 'HN', CAST(@Ngay AS DATETIME)+' 06:00', CAST(@Ngay AS DATETIME)+' 20:00', 1),
           ('LT4-'+@ms, 'SE4', 'SG-HN', 'SG', 'HN', CAST(@Ngay AS DATETIME)+' 09:00', CAST(@Ngay AS DATETIME)+' 23:30', 1),
           ('LT6-'+@ms, 'SE6', 'SG-HN', 'SG', 'HN', CAST(@Ngay AS DATETIME)+' 15:00', CAST(@Ngay AS DATETIME)+' 05:00', 1),
           ('LT8-'+@ms, 'SE8', 'SG-HN', 'SG', 'HN', CAST(@Ngay AS DATETIME)+' 19:00', CAST(@Ngay AS DATETIME)+' 09:00', 1),
           ('LT10-'+@ms, 'SE10', 'SG-HN', 'SG', 'HN', CAST(@Ngay AS DATETIME)+' 22:00', CAST(@Ngay AS DATETIME)+' 12:00', 1);
    
    SET @Ngay = DATEADD(DAY, 1, @Ngay);
END
GO

-- ========================================
-- BƯỚC 3: NHÂN VIÊN & TÀI KHOẢN
-- ========================================
INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, chucVu, trangThai, SDT, email, diaChi, ngaySinh, ngayVaoLam, gioiTinh) VALUES 
('NV001', '001099123456', N'Nguyễn Văn Quản Lý', 0, 1, '0901234567', 'admin@railway.com', N'123 Lê Duẩn, Hà Nội', '1985-05-20', '2020-01-01', N'Nam'), 
('NV002', '001099654321', N'Trần Thị Bán Vé', 1, 1, '0907654321', 'nv01@railway.com', N'456 Trần Hưng Đạo, Đà Nẵng', '1995-10-15', '2022-03-10', N'Nữ'),
('NV003', '001099888777', N'Lê Minh Nhân Viên', 1, 1, '0912888777', 'nv02@railway.com', N'789 Nguyễn Thông, TP.HCM', '1990-02-28', '2021-06-15', N'Nam');

INSERT INTO TaiKhoan VALUES 
('admin', '123456', 'NV001'), 
('nv01', '123456', 'NV002'),
('nv02', '123456', 'NV003');

-- ========================================
-- BƯỚC 4: KHÁCH HÀNG
-- ========================================
INSERT INTO KhachHang (maKH, CCCD, hoTen, doiTuong, SDT, Email) VALUES 
('KH001', '079123456789', N'Phạm Minh Long', N'Người lớn', '0901234567', 'long@gmail.com'), 
('KH002', '079987654321', N'Lê Thị Hoa', N'Sinh viên', '0908887776', 'hoa@student.edu.vn'),
('KH003', '079111222333', N'Nguyễn Văn Hùng', N'Người cao tuổi', '0912345678', 'hung@gmail.com'),
('KH004', '079444555666', N'Trần Bảo Nam', N'Trẻ em', '0987654321', 'nam@gmail.com'),
('KH005', '079777888999', N'Hoàng Thùy Linh', N'Người lớn', '0909990001', 'linh@gmail.com');

-- =============================================
-- BƯỚC 5: KHUYẾN MÃI (KMKH & KMHD)
-- =============================================
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai) VALUES 
('KM0101202401', N'Giảm 25% cho Trẻ em (6-10 tuổi)', 'KMKH', '2024-01-01', '2099-12-31', 1),
('KM0101202402', N'Giảm 15% cho Người cao tuổi (≥60 tuổi)', 'KMKH', '2024-01-01', '2099-12-31', 1),
('KM0101202403', N'Giảm 10% cho Sinh viên', 'KMKH', '2024-01-01', '2099-12-31', 1),
('KM0101202404', N'Giảm 9% khi đặt 11-40 vé', 'KMHD', '2024-01-01', '2099-12-31', 1),
('KM0101202405', N'Giảm 11% khi đặt 42-70 vé', 'KMHD', '2024-01-01', '2099-12-31', 1),
('KM0101202406', N'Giảm 13% khi đặt 71-100 vé', 'KMHD', '2024-01-01', '2099-12-31', 1),
('KM0101202407', N'Giảm 15% khi đặt từ 100 vé trở lên', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau) VALUES 
('KM0101202401', NULL, N'TreEm', 0.25),
('KM0101202402', NULL, N'NguoiCaoTuoi', 0.15),
('KM0101202403', NULL, N'SinhVien', 0.10),
('KM0101202404', NULL, N'11-40 vé', 0.09),
('KM0101202405', NULL, N'42-70 vé', 0.11),
('KM0101202406', NULL, N'71-100 vé', 0.13),
('KM0101202407', NULL, N'≥100 vé', 0.15);

-- ========================================
-- BƯỚC 6: HÓA ĐƠN & VÉ (DỮ LIỆU LỊCH SỬ & THỐNG KÊ)
-- ========================================

-- --- THÁNG 4/2026 ---
PRINT N'📊 Đang tạo dữ liệu lịch sử tháng 4...';
INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, ngayTao, gioTao, tongTien, trangThai) VALUES 
('HD001', 'NV002', 'KH001', '2026-04-10', '2026-04-10 09:00', 1000000, 1),
('HD002', 'NV002', 'KH002', '2026-04-15', '2026-04-15 14:00', 2400000, 1),
('HD003', 'NV003', 'KH003', '2026-04-20', '2026-04-20 10:00', 500000, 1);

INSERT INTO Ve (maVe, maLoaiVe, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, trangThai, maGaDi, maGaDen, tenKhachHang, soCCCD) VALUES 
('V001', 'LV01', '2026-04-10 06:00', 500000, 'KH001', 'TSE1-01-01', 'LT1-100426', 1, 'HN', 'V', N'Phạm Minh Long', '079123456789'),
('V002', 'LV01', '2026-04-10 06:00', 500000, 'KH001', 'TSE1-01-02', 'LT1-100426', 1, 'HN', 'V', N'Nguyễn Thị Bé', '079111222333'),
('V003', 'LV02', '2026-04-15 06:00', 1200000, 'KH002', 'TSE1-04-01', 'LT1-150426', 1, 'HN', 'SG', N'Lê Thị Hoa', '079987654321'),
('V004', 'LV02', '2026-04-15 06:00', 1200000, 'KH002', 'TSE1-04-02', 'LT1-150426', 1, 'HN', 'SG', N'Trần Văn Tèo', '079000111222'),
('V005', 'LV04', '2026-04-20 06:00', 500000, 'KH003', 'TSE2-01-01', 'LT2-200426', 1, 'SG', 'HN', N'Nguyễn Văn Hùng', '079111222333');

INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam) VALUES 
('HD001', 'V001', 1, 500000, 0), ('HD001', 'V002', 1, 500000, 0),
('HD002', 'V003', 1, 1200000, 0), ('HD002', 'V004', 1, 1200000, 0),
('HD003', 'V005', 1, 500000, 0);

-- --- THÁNG 5/2026 (THÁNG HIỆN TẠI) ---
PRINT N'📊 Đang tạo dữ liệu tháng 5...';
INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, ngayTao, gioTao, tongTien, trangThai) VALUES 
('HD004', 'NV002', 'KH001', '2026-05-01', '2026-05-01 08:00', 450000, 1),
('HD005', 'NV002', 'KH002', '2026-05-02', '2026-05-02 10:00', 900000, 1),
('HD006', 'NV003', 'KH004', '2026-05-03', '2026-05-03 11:00', 150000, 1),
('HD007', 'NV002', 'KH005', '2026-05-05', '2026-05-05 14:00', 2100000, 1),
('HD008', 'NV002', 'KH001', '2026-05-07', '2026-05-07 16:00', 300000, 1);

INSERT INTO Ve (maVe, maLoaiVe, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, trangThai, maGaDi, maGaDen, tenKhachHang, soCCCD) VALUES 
('V006', 'LV01', '2026-05-01 06:00', 450000, 'KH001', 'TSE1-01-05', 'LT1-010526', 1, 'HN', 'V', N'Phạm Minh Long', '079123456789'),
('V007', 'LV02', '2026-05-02 06:00', 900000, 'KH002', 'TSE1-02-05', 'LT1-020526', 1, 'HN', 'HUE', N'Lê Thị Hoa', '079987654321'),
('V008', 'LV03', '2026-05-03 06:00', 150000, 'KH004', 'TSE1-03-05', 'LT1-030526', 1, 'HN', 'ND', N'Trần Bảo Nam', '079444555666'),
('V009', 'LV01', '2026-05-05 06:00', 700000, 'KH005', 'TSE1-05-05', 'LT1-050526', 1, 'HN', 'DN', N'Hoàng Thùy Linh', '0909990001'),
('V010', 'LV01', '2026-05-05 06:00', 700000, 'KH005', 'TSE1-05-06', 'LT1-050526', 1, 'HN', 'DN', N'Nguyễn Văn A', '123456789001'),
('V011', 'LV01', '2026-05-05 06:00', 700000, 'KH005', 'TSE1-05-07', 'LT1-050526', 1, 'HN', 'DN', N'Trần Văn B', '123456789002'),
('V012', 'LV01', '2026-05-07 06:00', 300000, 'KH001', 'TSE1-01-07', 'LT1-070526', 0, 'V', 'HUE', N'Phạm Minh Long', '079123456789');

INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam) VALUES 
('HD004', 'V006', 1, 450000, 0), ('HD005', 'V007', 1, 900000, 0),
('HD006', 'V008', 1, 150000, 0), ('HD007', 'V009', 1, 700000, 0),
('HD007', 'V010', 1, 700000, 0), ('HD007', 'V011', 1, 700000, 0),
('HD008', 'V012', 1, 300000, 0);

-- Gán khuyến mãi cho hóa đơn đã thanh toán
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau) VALUES 
('KM0101202403', 'HD002', N'Sinh viên', 240000),
('KM0101202404', 'HD007', N'11-40 vé', 189000);

-- --- DỮ LIỆU VÉ ĐẶT TRƯỚC (TEST SƠ ĐỒ GHẾ & CHẶNG) ---
PRINT N'📊 Đang tạo dữ liệu vé đặt trước...';
INSERT INTO Ve (maVe, maLoaiVe, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, trangThai, maGaDi, maGaDen, tenKhachHang, soCCCD) VALUES 
('V_TEST_1', 'LV01', '2026-05-10 06:00', 400000, 'KH001', 'TSE1-01-10', 'LT1-100526', 1, 'HN', 'V', N'Người Test 1', '111222333444'),
('V_TEST_2', 'LV01', '2026-05-10 06:00', 400000, 'KH003', 'TSE1-02-10', 'LT1-100526', 1, 'HUE', 'SG', N'Người Test 2', '555666777888');

PRINT N'✅ Đã hoàn tất nạp dữ liệu TOÀN DIỆN & FIX LỖI!';
GO

-- ========================================
-- DỮ LIỆU BỔ SUNG ĐỂ TEST BIỂU ĐỒ GHẾ TRỐNG (TODAY: 2026-05-08)
-- ========================================
PRINT N'📊 Đang nạp dữ liệu vé đa dạng cho TẤT CẢ các tàu hôm nay...';

-- Xóa dữ liệu vé test cũ nếu có để tránh trùng mã
DELETE FROM Ve WHERE maVe LIKE 'V_TEST_0805%';

DECLARE @t_id INT = 1;
DECLARE @tau_num INT = 1;
DECLARE @so_ve_ban INT;
DECLARE @ma_lt NVARCHAR(50);
DECLARE @so_hieu_tau NVARCHAR(10);

WHILE @tau_num <= 10
BEGIN
    SET @so_hieu_tau = 'SE' + CAST(@tau_num AS NVARCHAR);
    SET @ma_lt = (CASE WHEN @tau_num % 2 <> 0 THEN 'LT' ELSE 'LT' END) + CAST(@tau_num AS NVARCHAR) + '-080526';
    
    -- Thiết lập số lượng vé bán khác nhau cho mỗi tàu để biểu đồ có sự phân bậc
    SET @so_ve_ban = 320 - (@tau_num * 30); 
    IF @so_ve_ban < 0 SET @so_ve_ban = 10;

    -- Xác định Ga Đi/Ga Đến dựa trên số hiệu tàu (Lẻ: Xuôi Nam, Chẵn: Ngược Bắc)
    DECLARE @gDi NVARCHAR(10), @gDen NVARCHAR(10);
    IF @tau_num % 2 <> 0 
    BEGIN
        -- Chiều đi Nam
        SET @gDi = CASE WHEN @tau_num = 1 THEN 'HN' WHEN @tau_num = 3 THEN 'V' ELSE 'HUE' END;
        SET @gDen = CASE WHEN @tau_num = 1 THEN 'SG' WHEN @tau_num = 3 THEN 'DN' ELSE 'SG' END;
    END
    ELSE
    BEGIN
        -- Chiều đi Bắc
        SET @gDi = CASE WHEN @tau_num = 2 THEN 'SG' WHEN @tau_num = 4 THEN 'NT' ELSE 'DN' END;
        SET @gDen = CASE WHEN @tau_num = 2 THEN 'HN' WHEN @tau_num = 4 THEN 'V' ELSE 'HN' END;
    END

    PRINT N'   > Đang nạp ' + CAST(@so_ve_ban AS NVARCHAR) + N' vé cho tàu ' + @so_hieu_tau + ' (' + @gDi + ' -> ' + @gDen + ')';

    DECLARE @v INT = 1;
    DECLARE @current_toa INT = 1;
    DECLARE @current_ghe INT = 1;
    DECLARE @max_ghe_in_toa INT;

    WHILE @v <= @so_ve_ban
    BEGIN
        -- Xác định số ghế tối đa của toa hiện tại
        SET @max_ghe_in_toa = CASE 
            WHEN @current_toa <= 5 THEN 64 
            ELSE 42 END;

        -- Nếu ghế hiện tại vượt quá số ghế của toa, chuyển sang toa tiếp theo
        IF @current_ghe > @max_ghe_in_toa
        BEGIN
            SET @current_toa = @current_toa + 1;
            SET @current_ghe = 1;
            -- Cập nhật lại số ghế tối đa cho toa mới
            SET @max_ghe_in_toa = CASE 
                WHEN @current_toa <= 5 THEN 64 
                ELSE 42 END;
        END

        IF @current_toa > 10 BREAK; -- Dừng nếu hết 10 toa

        DECLARE @ma_toa NVARCHAR(20) = 'T' + @so_hieu_tau + '-' + FORMAT(@current_toa, '00');
        
        INSERT INTO Ve (maVe, maLoaiVe, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, trangThai, maGaDi, maGaDen, tenKhachHang, soCCCD)
        VALUES (
            CONCAT('V_TEST_0805_', @so_hieu_tau, '_', FORMAT(@v, '000')), 
            'LV01', '2026-05-08 08:00', 500000, 'KH001', 
            CONCAT(@ma_toa, '-', FORMAT(@current_ghe, '00')), 
            @ma_lt, 1, @gDi, @gDen, N'Khách Test', '123456789'
        );
        
        SET @current_ghe = @current_ghe + 1;
        SET @v = @v + 1;
    END

    SET @tau_num = @tau_num + 1;
END

PRINT N'✅ Đã nạp xong dữ liệu test đa dạng (Bắc - Nam & Ga trung gian)!';
GO
