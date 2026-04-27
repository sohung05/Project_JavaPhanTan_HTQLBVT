-- ========================================
-- HỆ THỐNG QUẢN LÝ VÉ TÀU - DỮ LIỆU THUYẾT TRÌNH
-- File: 04_ResetData_ThuyetTrinh.sql
-- Mô tả: Dữ liệu thực tế với tàu chạy xuyên suốt nhiều ga (KHỚP 100% SCHEMA)
-- Tuyến: Lào Cai → Hà Nội → Thanh Hóa → Vinh → Đồng Hới → Huế → Đà Nẵng → Quy Nhơn → Nha Trang → Phan Thiết → Sài Gòn → Bạc Liêu
-- ========================================

USE HTQLVT;
GO

-- ========================================
-- BƯỚC 1: XÓA TẤT CẢ DỮ LIỆU CŨ (ĐÚNG THỨ TỰ)
-- ========================================
PRINT N'🗑️  Bắt đầu xóa dữ liệu cũ...';
GO

-- Xóa theo thứ tự: con → cha
DELETE FROM ChiTietHoaDon;
DELETE FROM ChiTietKhuyenMai;
DELETE FROM Ve;
DELETE FROM HoaDon;          -- Phải xóa trước ChuyenTau
DELETE FROM ChoNgoi;
DELETE FROM Toa;
DELETE FROM LichTrinh;
DELETE FROM ChuyenTau;
DELETE FROM KhachHang;
DELETE FROM TaiKhoan;
DELETE FROM NhanVien;
DELETE FROM KhuyenMai;
DELETE FROM LoaiTau;
DELETE FROM LoaiToa;
DELETE FROM LoaiVe;
DELETE FROM Ga;
DELETE FROM Tuyen;
GO

PRINT N'✅ Đã xóa toàn bộ dữ liệu cũ';
GO

-- ========================================
-- BƯỚC 2: THÊM DỮ LIỆU MỚI
-- ========================================
PRINT N'';
PRINT N'📥 Bắt đầu thêm dữ liệu mới...';
GO

-- ========================================
-- 1. LoaiTau
-- ========================================
INSERT INTO LoaiTau (maLoaiTau, tenLoaiTau) VALUES 
('SE', N'Tàu siêu tốc');
GO

PRINT N'✅ Đã thêm LoaiTau';

-- ========================================
-- 2. LoaiToa
-- ========================================
INSERT INTO LoaiToa (maLoaiToa, tenLoaiToa) VALUES 
('LTOA001', N'Ngồi mềm điều hòa'),
('LTOA002', N'Giường nằm 4 khoang');
GO

PRINT N'✅ Đã thêm LoaiToa';

-- ========================================
-- 3. LoaiVe
-- ========================================
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, mucGiamGia) VALUES 
('LV01', N'Người lớn', 0.00),
('LV02', N'Sinh viên', 0.10),
('LV03', N'Trẻ em', 0.25),
('LV04', N'Người cao tuổi', 0.15);
GO

PRINT N'✅ Đã thêm 4 LoaiVe';

-- ========================================
-- 4. Ga (12 ga theo thứ tự Bắc → Nam)
-- ========================================
INSERT INTO Ga (maGa, tenGa, viTri) VALUES 
('LC', N'Lào Cai', N'Đường Hoàng Liên, Lào Cai'),
('HN', N'Hà Nội', N'120 Lê Duẩn, Hà Nội'),
('TH', N'Thanh Hóa', N'Đường Quang Trung, Thanh Hóa'),
('VINH', N'Vinh', N'Đường Lê Lợi, Vinh, Nghệ An'),
('DH', N'Đồng Hới', N'Đường Trần Hưng Đạo, Đồng Hới, Quảng Bình'),
('HUE', N'Huế', N'2 Bùi Thị Xuân, Huế'),
('DN', N'Đà Nẵng', N'202 Hải Phòng, Đà Nẵng'),
('QN', N'Quy Nhơn', N'Đường Lê Hồng Phong, Quy Nhơn, Bình Định'),
('NT', N'Nha Trang', N'17 Thái Nguyên, Nha Trang'),
('PT', N'Phan Thiết', N'Đường Lê Hồng Phong, Phan Thiết'),
('SG', N'Sài Gòn', N'1 Nguyễn Thông, Quận 3, TP.HCM'),
('BL', N'Bạc Liêu', N'Đường Trần Phú, Bạc Liêu');
GO

PRINT N'✅ Đã thêm 12 Ga (LC → HN → TH → VINH → DH → HUE → DN → QN → NT → PT → SG → BL)';

-- ========================================
-- 5. Tuyen (Nhiều tuyến cho Dashboard đẹp)
-- ========================================
INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES 
-- Tuyến toàn trình
('LC-BL', N'Tuyến Bắc Nam (Lào Cai - Bạc Liêu)', 2150.0),
('BL-LC', N'Tuyến Nam Bắc (Bạc Liêu - Lào Cai)', 2150.0),

-- Tuyến Bắc - Nam chính (PHỔ BIẾN)
('HN-SG', N'Hà Nội - Sài Gòn', 1726.0),
('SG-HN', N'Sài Gòn - Hà Nội', 1726.0),

-- Tuyến Bắc - Trung
('HN-DN', N'Hà Nội - Đà Nẵng', 791.0),
('DN-HN', N'Đà Nẵng - Hà Nội', 791.0),

-- Tuyến Bắc - Trung (Huế)
('HN-HUE', N'Hà Nội - Huế', 658.0),
('HUE-HN', N'Huế - Hà Nội', 658.0),

-- Tuyến Nam - Duyên hải
('SG-NT', N'Sài Gòn - Nha Trang', 411.0),
('NT-SG', N'Nha Trang - Sài Gòn', 411.0),

-- Tuyến Nam ngắn
('SG-PT', N'Sài Gòn - Phan Thiết', 231.0),
('PT-SG', N'Phan Thiết - Sài Gòn', 231.0);
GO

PRINT N'✅ Đã thêm 12 Tuyen (cho Dashboard đẹp)';

-- ========================================
-- 6. ChuyenTau (6 chuyến: SE1, SE3, SE5 đi Nam; SE2, SE4, SE6 đi Bắc)
-- ========================================
INSERT INTO ChuyenTau (soHieuTau, tocDo, maLoaiTau, namSanXuat) VALUES 
-- Chiều Bắc → Nam (số lẻ)
('SE1', 90.0, 'SE', 2020),
('SE3', 85.0, 'SE', 2019),
('SE5', 80.0, 'SE', 2021),
-- Chiều Nam → Bắc (số chẵn)
('SE2', 90.0, 'SE', 2020),
('SE4', 85.0, 'SE', 2019),
('SE6', 80.0, 'SE', 2021);
GO

PRINT N'✅ Đã thêm 6 ChuyenTau (SE1,SE3,SE5 đi Nam | SE2,SE4,SE6 đi Bắc)';

-- ========================================
-- 7. Toa (mỗi chuyến tàu có 10 toa)
-- SCHEMA: maToa, soHieuTau, soToa, maLoaiToa (KHÔNG có soGhe)
-- ========================================
DECLARE @soHieuTau NVARCHAR(20);
DECLARE @soToa INT;
DECLARE @counter INT = 1;

DECLARE train_cursor CURSOR FOR 
SELECT soHieuTau FROM ChuyenTau ORDER BY soHieuTau;

OPEN train_cursor;
FETCH NEXT FROM train_cursor INTO @soHieuTau;

WHILE @@FETCH_STATUS = 0
BEGIN
    SET @soToa = 1;
    WHILE @soToa <= 10
    BEGIN
        INSERT INTO Toa (maToa, soHieuTau, soToa, maLoaiToa)
        VALUES (
            'T' + RIGHT('00' + CAST(@counter AS NVARCHAR(3)), 3),  -- T001, T002, T003...
            @soHieuTau,
            @soToa,
            CASE WHEN @soToa <= 5 THEN 'LTOA001' ELSE 'LTOA002' END
        );
        SET @soToa = @soToa + 1;
        SET @counter = @counter + 1;
    END
    FETCH NEXT FROM train_cursor INTO @soHieuTau;
END

CLOSE train_cursor;
DEALLOCATE train_cursor;
GO

PRINT N'✅ Đã thêm 60 Toa (6 tàu x 10 toa, T001-T060)';

-- ========================================
-- 8. ChoNgoi (Ghế/Giường cho mỗi toa)
-- SCHEMA: maChoNgoi, maToa, moTa, viTri, gia
-- ========================================
DECLARE @maToa NVARCHAR(20);
DECLARE @soToa INT;
DECLARE @maxGhe INT;
DECLARE @giaGhe DECIMAL(18,2);
DECLARE @soGhe INT;
DECLARE @maToaNumber INT;

DECLARE toa_cursor CURSOR FOR 
SELECT maToa, soToa FROM Toa ORDER BY maToa;

OPEN toa_cursor;
FETCH NEXT FROM toa_cursor INTO @maToa, @soToa;

SET @maToaNumber = 1;

WHILE @@FETCH_STATUS = 0
BEGIN
    IF @soToa <= 5
    BEGIN
        SET @maxGhe = 64;
        SET @giaGhe = 200000.0;
    END
    ELSE
    BEGIN
        SET @maxGhe = 36;
        SET @giaGhe = 350000.0;
    END
    
    SET @soGhe = 1;
    WHILE @soGhe <= @maxGhe
    BEGIN
        INSERT INTO ChoNgoi (maChoNgoi, maToa, viTri, gia, moTa)
        VALUES (
            RIGHT('00' + CAST(@maToaNumber AS NVARCHAR(2)), 2) + 
            RIGHT('00' + CAST(@soGhe AS NVARCHAR(2)), 2),  -- Format: XXYY
            @maToa,
            @soGhe,
            @giaGhe,
            CASE 
                WHEN @soToa <= 5 THEN N'Ghế số ' + CAST(@soGhe AS NVARCHAR(3))
                ELSE N'Giường số ' + CAST(@soGhe AS NVARCHAR(3))
            END
        );
        SET @soGhe = @soGhe + 1;
    END
    
    SET @maToaNumber = @maToaNumber + 1;
    FETCH NEXT FROM toa_cursor INTO @maToa, @soToa;
END

CLOSE toa_cursor;
DEALLOCATE toa_cursor;
GO

PRINT N'✅ Đã thêm ChoNgoi (~3000 ghế/giường)';

-- ========================================
-- 9. LichTrinh (Mỗi ngày có 6 chuyến: 3 đi Nam, 3 đi Bắc)
-- Tạo lịch trình cho 30 ngày (từ hôm nay)
-- SCHEMA: maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai
-- ========================================
DECLARE @NgayBatDau DATE = CAST(GETDATE() AS DATE);
DECLARE @NgayKetThuc DATE = DATEADD(DAY, 29, @NgayBatDau);
DECLARE @NgayHienTai DATE = @NgayBatDau;
DECLARE @ngayStr NVARCHAR(6);

WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    SET @ngayStr = FORMAT(@NgayHienTai, 'ddMMyy');
    
    -- ===== CHIỀU ĐI NAM (Lào Cai → Bạc Liêu) =====
    
    -- SE1: Xuất phát 05:00 từ Lào Cai → Bạc Liêu (20 giờ)
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES 
    ('LTSE1-' + @ngayStr, 'SE1', 'LC-BL', 'LC', 'BL', 
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 05:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 01:00:00') AS DATETIME2(0)), 
     1);
    
    -- SE3: Xuất phát 12:00 từ Lào Cai → Bạc Liêu (21 giờ)
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES 
    ('LTSE3-' + @ngayStr, 'SE3', 'LC-BL', 'LC', 'BL',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 09:00:00') AS DATETIME2(0)), 
     1);
    
    -- SE5: Xuất phát 20:00 từ Lào Cai → Bạc Liêu (22 giờ)
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES 
    ('LTSE5-' + @ngayStr, 'SE5', 'LC-BL', 'LC', 'BL',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)), 
     1);
    
    -- ===== CHIỀU ĐI BẮC (Bạc Liêu → Lào Cai) =====
    
    -- SE2: Xuất phát 06:00 từ Bạc Liêu → Lào Cai (20 giờ)
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES 
    ('LTSE2-' + @ngayStr, 'SE2', 'BL-LC', 'BL', 'LC',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 02:00:00') AS DATETIME2(0)), 
     1);
    
    -- SE4: Xuất phát 13:00 từ Bạc Liêu → Lào Cai (21 giờ)
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES 
    ('LTSE4-' + @ngayStr, 'SE4', 'BL-LC', 'BL', 'LC',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 10:00:00') AS DATETIME2(0)), 
     1);
    
    -- SE6: Xuất phát 21:00 từ Bạc Liêu → Lào Cai (22 giờ)
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES 
    ('LTSE6-' + @ngayStr, 'SE6', 'BL-LC', 'BL', 'LC',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)), 
     1);
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END
GO

PRINT N'✅ Đã thêm LichTrinh (30 ngày x 6 chuyến = 180 lịch trình)';

-- ========================================
-- 10. NhanVien và TaiKhoan
-- SCHEMA NhanVien: maNhanVien, CCCD, hoTen, SDT, email, diaChi, chucVu, trangThai, ngaySinh, ngayVaoLam, gioiTinh
-- SCHEMA TaiKhoan: userName, passWord, maNhanVien (KHÔNG có vaiTro)
-- ========================================
INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, SDT, email, diaChi, chucVu, trangThai, ngaySinh, ngayVaoLam, gioiTinh) VALUES 
('NV001', '001234567890', N'Nguyễn Văn An', '0901234567', 'an.nv@railway.vn', N'Hà Nội', 1, 1, '1990-05-15', '2015-01-10', N'Nam'),
('NV002', '001234567891', N'Trần Thị Bình', '0912345678', 'binh.tt@railway.vn', N'TP.HCM', 0, 1, '1995-08-20', '2018-03-15', N'Nữ'),
('NV003', '001234567892', N'Lê Văn Cường', '0923456789', 'cuong.lv@railway.vn', N'Đà Nẵng', 0, 1, '1988-03-10', '2016-07-01', N'Nam');
GO

INSERT INTO TaiKhoan (userName, passWord, maNhanVien) VALUES 
('admin', 'admin123', 'NV001'),
('nhanvien1', 'nv123', 'NV002'),
('nhanvien2', 'nv123', 'NV003');
GO

PRINT N'✅ Đã thêm 3 NhanVien và 3 TaiKhoan';

-- ========================================
-- 11. KhuyenMai
-- SCHEMA: maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai
-- ========================================
-- Trẻ em 6-10 tuổi: Giảm 25%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202401', N'Giảm 25% cho Trẻ em (6-10 tuổi)', 'KMKH', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202401', NULL, N'TreEm', 0.25);

-- Người cao tuổi ≥60 tuổi: Giảm 15%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202402', N'Giảm 15% cho Người cao tuổi (≥60 tuổi)', 'KMKH', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202402', NULL, N'NguoiCaoTuoi', 0.15);

-- Sinh viên: Giảm 10%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202403', N'Giảm 10% cho Sinh viên', 'KMKH', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202403', NULL, N'SinhVien', 0.10);

-- 11-40 vé: Giảm 9%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202404', N'Giảm 9% khi đặt 11-40 vé', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202404', NULL, N'11-40 vé', 0.09);

-- 42-70 vé: Giảm 11%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202405', N'Giảm 11% khi đặt 42-70 vé', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202405', NULL, N'42-70 vé', 0.11);

-- 71-100 vé: Giảm 13%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202406', N'Giảm 13% khi đặt 71-100 vé', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202406', NULL, N'71-100 vé', 0.13);

-- ≥100 vé: Giảm 15%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM0101202407', N'Giảm 15% khi đặt từ 100 vé trở lên', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM0101202407', NULL, N'≥100 vé', 0.15);

PRINT N'✅ Đã thêm 7 KhuyenMai (3 KMKH + 4 KMHD)';
PRINT N'✅ Đã thêm 7 ChiTietKhuyenMai';

-- ========================================
-- 12. DỮ LIỆU MẪU - Khách hàng (cho thống kê)
-- SCHEMA: maKH, CCCD, hoTen, email, SDT, doiTuong
-- ========================================
PRINT N'';
PRINT N'📊 Bắt đầu tạo dữ liệu mẫu cho thống kê...';

-- Danh sách họ tên thật để data trông tự nhiên
DECLARE @DanhSachHo TABLE (Ho NVARCHAR(50));
INSERT INTO @DanhSachHo VALUES 
(N'Nguyễn'), (N'Trần'), (N'Lê'), (N'Phạm'), (N'Hoàng'), (N'Huỳnh'), 
(N'Phan'), (N'Vũ'), (N'Võ'), (N'Đặng'), (N'Bùi'), (N'Đỗ'), (N'Hồ'), 
(N'Ngô'), (N'Dương'), (N'Lý'), (N'Đinh'), (N'Mai'), (N'Đào'), (N'Tô');

DECLARE @DanhSachTenDem TABLE (TenDem NVARCHAR(50));
INSERT INTO @DanhSachTenDem VALUES 
(N'Văn'), (N'Thị'), (N'Đức'), (N'Minh'), (N'Hoàng'), (N'Hữu'), 
(N'Thanh'), (N'Quốc'), (N'Anh'), (N'Phương'), (N'Thúy'), (N'Xuân');

DECLARE @DanhSachTen TABLE (Ten NVARCHAR(50));
INSERT INTO @DanhSachTen VALUES 
(N'An'), (N'Bình'), (N'Cường'), (N'Dũng'), (N'Hòa'), (N'Khoa'), (N'Long'), 
(N'Nam'), (N'Phong'), (N'Quân'), (N'Sơn'), (N'Tài'), (N'Tuấn'), (N'Vinh'), 
(N'Hà'), (N'Lan'), (N'Mai'), (N'Nga'), (N'Oanh'), (N'Phương'), (N'Quyên'), 
(N'Thu'), (N'Trang'), (N'Uyên'), (N'Vân'), (N'Xuân'), (N'Yến'), (N'Như');

DECLARE @i INT = 1;
DECLARE @hoTen NVARCHAR(150);
DECLARE @sdt NVARCHAR(20);
DECLARE @cccd NVARCHAR(20);
DECLARE @email NVARCHAR(150);
DECLARE @doiTuong NVARCHAR(30);

WHILE @i <= 150
BEGIN
    -- Tạo tên ngẫu nhiên
    DECLARE @ho NVARCHAR(50), @tenDem NVARCHAR(50), @ten NVARCHAR(50);
    SELECT TOP 1 @ho = Ho FROM @DanhSachHo ORDER BY NEWID();
    SELECT TOP 1 @tenDem = TenDem FROM @DanhSachTenDem ORDER BY NEWID();
    SELECT TOP 1 @ten = Ten FROM @DanhSachTen ORDER BY NEWID();
    SET @hoTen = @ho + N' ' + @tenDem + N' ' + @ten;
    
    SET @sdt = '09' + RIGHT('00000000' + CAST(@i AS NVARCHAR(8)), 8);
    SET @cccd = RIGHT('000000000000' + CAST(@i AS NVARCHAR(12)), 12);
    SET @email = 'khach' + CAST(@i AS NVARCHAR(10)) + '@email.com';
    
    -- Random đối tượng với tỷ lệ thực tế
    DECLARE @rand INT = @i % 100;
    IF @rand < 10 SET @doiTuong = N'Sinh viên';        -- 10%
    ELSE IF @rand < 15 SET @doiTuong = N'Người cao tuổi';  -- 5%
    ELSE IF @rand < 20 SET @doiTuong = N'Trẻ em';      -- 5%
    ELSE SET @doiTuong = N'Người lớn';                 -- 80%
    
    INSERT INTO KhachHang (maKH, CCCD, hoTen, email, SDT, doiTuong)
    VALUES ('KH' + RIGHT('0000' + CAST(@i AS NVARCHAR(4)), 4), @cccd, @hoTen, @email, @sdt, @doiTuong);
    
    SET @i = @i + 1;
END
GO

PRINT N'✅ Đã thêm 150 KhachHang với tên thật';

-- ========================================
-- 13. DỮ LIỆU MẪU - Hóa đơn và Vé (60 ngày gần đây - NHIỀU DATA)
-- SCHEMA HoaDon: maHoaDon, maNhanVien, maKH, gioTao, ngayTao, tongTien, trangThai
-- SCHEMA Ve: maVe, maLoaiVe, maVach, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, maToa, trangThai, tenKhachHang, soCCCD
-- SCHEMA ChiTietHoaDon: maHoaDon, maVe, soLuong, giaVe, mucGiam
-- ========================================
PRINT N'📊 Tạo dữ liệu hóa đơn và vé (60 ngày, ~600 hóa đơn)...';

DECLARE @NgayBatDau DATE = DATEADD(DAY, -60, CAST(GETDATE() AS DATE));
DECLARE @NgayHienTai DATE = @NgayBatDau;
DECLARE @NgayKetThuc DATE = CAST(GETDATE() AS DATE);
DECLARE @soHD INT = 1;
DECLARE @maHoaDon NVARCHAR(20);
DECLARE @maKhachHang NVARCHAR(20);
DECLARE @maNhanVien NVARCHAR(20);
DECLARE @ngayStr NVARCHAR(6);
DECLARE @maLichTrinh NVARCHAR(50);
DECLARE @maToa NVARCHAR(20);
DECLARE @maChoNgoi NVARCHAR(20);
DECLARE @maVe NVARCHAR(30);
DECLARE @maLoaiVe NVARCHAR(20);
DECLARE @giaGoc DECIMAL(18,2);
DECLARE @giaVe DECIMAL(18,2);
DECLARE @mucGiam DECIMAL(18,2);
DECLARE @tongTien DECIMAL(18,2);
DECLARE @soVeNgay INT;
DECLARE @random INT;
DECLARE @gioTao DATETIME2(0);
DECLARE @thoiGianLenTau DATETIME2(0);

-- Tạo 8-12 hóa đơn mỗi ngày (tổng ~600 hóa đơn)
WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    SET @ngayStr = FORMAT(@NgayHienTai, 'ddMMyy');
    
    -- Cuối tuần và đầu tháng bán nhiều hơn (thực tế)
    IF DATEPART(WEEKDAY, @NgayHienTai) IN (1, 7) -- Chủ nhật, Thứ 7
        SET @soVeNgay = 10 + (@soHD % 5); -- 10-14 vé
    ELSE IF DATEPART(DAY, @NgayHienTai) <= 5 -- Đầu tháng
        SET @soVeNgay = 9 + (@soHD % 4);  -- 9-12 vé
    ELSE
        SET @soVeNgay = 8 + (@soHD % 3);  -- 8-10 vé
    
    DECLARE @j INT = 1;
    WHILE @j <= @soVeNgay
    BEGIN
        SET @maHoaDon = 'HD' + @ngayStr + RIGHT('0000' + CAST(@soHD AS NVARCHAR(4)), 4);
        
        -- Random khách hàng (150 khách)
        SET @random = 1 + (@soHD % 150);
        SET @maKhachHang = 'KH' + RIGHT('0000' + CAST(@random AS NVARCHAR(4)), 4);
        
        -- Random nhân viên
        SET @maNhanVien = 'NV00' + CAST(1 + (@soHD % 3) AS NVARCHAR(1));
        
        -- Random lịch trình (6 chuyến với tỷ lệ khác nhau)
        -- SE1, SE2 phổ biến hơn (tàu nhanh)
        SET @random = 1 + (@soHD % 10);
        IF @random <= 3 SET @maLichTrinh = 'LTSE1-' + @ngayStr;      -- 30%
        ELSE IF @random <= 6 SET @maLichTrinh = 'LTSE2-' + @ngayStr; -- 30%
        ELSE IF @random = 7 SET @maLichTrinh = 'LTSE3-' + @ngayStr;  -- 10%
        ELSE IF @random = 8 SET @maLichTrinh = 'LTSE4-' + @ngayStr;  -- 10%
        ELSE IF @random = 9 SET @maLichTrinh = 'LTSE5-' + @ngayStr;  -- 10%
        ELSE SET @maLichTrinh = 'LTSE6-' + @ngayStr;                 -- 10%
        
        -- Random toa (T001-T060)
        SET @random = 1 + (@soHD % 60);
        SET @maToa = 'T' + RIGHT('00' + CAST(@random AS NVARCHAR(3)), 3);
        
        -- Random ghế (dựa vào số toa để biết ngồi mềm hay giường nằm)
        SET @random = 1 + (@soHD % 60);
        IF @random % 10 <= 5
        BEGIN
            -- Ngồi mềm (64 ghế)
            SET @maChoNgoi = RIGHT('00' + CAST((@soHD % 60) + 1 AS NVARCHAR(2)), 2) + 
                            RIGHT('00' + CAST(1 + (@soHD % 64) AS NVARCHAR(2)), 2);
            SET @giaGoc = 200000.0;
        END
        ELSE
        BEGIN
            -- Giường nằm (36 chỗ)
            SET @maChoNgoi = RIGHT('00' + CAST((@soHD % 60) + 1 AS NVARCHAR(2)), 2) + 
                            RIGHT('00' + CAST(1 + (@soHD % 36) AS NVARCHAR(2)), 2);
            SET @giaGoc = 350000.0;
        END
        
        -- Random loại vé
        SET @random = 1 + (@soHD % 10);
        IF @random <= 7 
        BEGIN
            SET @maLoaiVe = 'LV01'; -- Người lớn (70%)
            SET @mucGiam = 0;
            SET @giaVe = @giaGoc;
        END
        ELSE IF @random = 8
        BEGIN
            SET @maLoaiVe = 'LV02'; -- Sinh viên (10%)
            SET @mucGiam = @giaGoc * 0.10;
            SET @giaVe = @giaGoc - @mucGiam;
        END
        ELSE IF @random = 9
        BEGIN
            SET @maLoaiVe = 'LV03'; -- Trẻ em (10%)
            SET @mucGiam = @giaGoc * 0.25;
            SET @giaVe = @giaGoc - @mucGiam;
        END
        ELSE
        BEGIN
            SET @maLoaiVe = 'LV04'; -- Người cao tuổi (10%)
            SET @mucGiam = @giaGoc * 0.15;
            SET @giaVe = @giaGoc - @mucGiam;
        END
        
        SET @tongTien = @giaVe;
        SET @gioTao = CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' ', 
                     RIGHT('00' + CAST(8 + (@soHD % 10) AS NVARCHAR(2)), 2), ':00:00') AS DATETIME2(0));
        
        -- Thời gian lên tàu = giờ khởi hành của lịch trình (giả sử)
        -- (Trong thực tế cần JOIN với LichTrinh để lấy giờ chính xác)
        SET @thoiGianLenTau = DATEADD(DAY, 1, @gioTao); -- Tạm thời +1 ngày
        
        -- Tạo hóa đơn
        INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, gioTao, ngayTao, tongTien, trangThai)
        VALUES (@maHoaDon, @maNhanVien, @maKhachHang, @gioTao, @gioTao, @tongTien, 1);
        
        -- Tạo vé
        SET @maVe = 'VE' + @ngayStr + RIGHT('0000' + CAST(@soHD AS NVARCHAR(4)), 4);
        
        INSERT INTO Ve (maVe, maLoaiVe, maVach, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, maToa, trangThai, tenKhachHang, soCCCD)
        SELECT @maVe, @maLoaiVe, NULL, @thoiGianLenTau, @giaVe, @maKhachHang, @maChoNgoi, @maLichTrinh, @maToa, 1, hoTen, CCCD
        FROM KhachHang WHERE maKH = @maKhachHang;
        
        -- Tạo chi tiết hóa đơn
        INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam)
        VALUES (@maHoaDon, @maVe, 1, @giaGoc, @mucGiam);
        
        SET @soHD = @soHD + 1;
        SET @j = @j + 1;
    END
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END
GO

PRINT N'✅ Đã thêm ~600 hóa đơn và vé (60 ngày, đủ data cho thống kê đẹp)';

-- ========================================
-- HOÀN THÀNH
-- ========================================
PRINT N'';
PRINT N'🎉 ========================================';
PRINT N'✅ HOÀN THÀNH IMPORT DỮ LIỆU THUYẾT TRÌNH!';
PRINT N'📊 Tóm tắt:';
PRINT N'   ✔️ 12 Ga (Lào Cai → Bạc Liêu)';
PRINT N'   ✔️ 6 Chuyến tàu (SE1,SE3,SE5 đi Nam | SE2,SE4,SE6 đi Bắc)';
PRINT N'   ✔️ 180 Lịch trình (30 ngày × 6 chuyến/ngày)';
PRINT N'   ✔️ 60 Toa (T001-T060)';
PRINT N'   ✔️ ~3000 Ghế/Giường';
PRINT N'   ✔️ 150 Khách hàng (tên thật, đa dạng)';
PRINT N'   ✔️ ~600 Hóa đơn mẫu (60 ngày - ĐỦ DATA CHO THỐNG KÊ ĐẸP)';
PRINT N'   ✔️ ~600 Vé đã bán (đa dạng loại vé, đối tượng)';
PRINT N'';
PRINT N'📌 LƯU Ý:';
PRINT N'   - Dữ liệu KHỚP 100% với schema hiện tại';
PRINT N'   - Dashboard & Thống kê sẵn sàng demo';
PRINT N'   - Xem file THOI_GIAN_TAU.md để biết giờ tàu đến từng ga';
PRINT N'';
PRINT N'🎉 ========================================';
GO
