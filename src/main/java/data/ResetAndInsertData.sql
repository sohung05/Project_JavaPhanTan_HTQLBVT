-- ========================================
-- HỆ THỐNG QUẢN LÝ VÉ TÀU - RESET & INSERT DỮ LIỆU
-- File: 02_ResetAndInsertData.sql
-- Mô tả: Xóa toàn bộ dữ liệu cũ và thêm dữ liệu mới (ĐÚNG FORMAT THEO TÀI LIỆU)
-- ========================================

USE HTQLVT;
GO

-- ========================================
-- BƯỚC 1: XÓA TẤT CẢ DỮ LIỆU CŨ
-- ========================================
PRINT N'🗑️  Bắt đầu xóa dữ liệu cũ...';
GO

DELETE FROM ChiTietHoaDon;
DELETE FROM ChiTietKhuyenMai;
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
-- 1. LoaiTau (mã viết in hoa, không vượt quá 10 ký tự)
-- ========================================
INSERT INTO LoaiTau (maLoaiTau, tenLoaiTau) VALUES 
('SE', N'Tàu siêu tốc'),
('TN', N'Tàu thống nhất'),
('SPT', N'Tàu Sài Gòn Phan Thiết');
GO

PRINT N'✅ Đã thêm 3 LoaiTau';

-- ========================================
-- 2. LoaiToa (mã không vượt quá 10 ký tự)
-- ========================================
INSERT INTO LoaiToa (maLoaiToa, tenLoaiToa) VALUES 
('LTOA001', N'Ngồi mềm điều hòa'),
('LTOA002', N'Giường nằm 4 khoang');
GO

PRINT N'✅ Đã thêm 2 LoaiToa';

-- ========================================
-- 3. LoaiVe (LVxx - xx từ 01, mucGiamGia từ 0-1)
-- ========================================
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, mucGiamGia) VALUES 
('LV01', N'Người lớn', 0.00),
('LV02', N'Sinh viên', 0.10),
('LV03', N'Trẻ em', 0.25),
('LV04', N'Người cao tuổi', 0.15);
GO

PRINT N'✅ Đã thêm 4 LoaiVe';

-- ========================================
-- 4. Ga (maGa viết tắt in hoa, không vượt quá 5 ký tự)
-- ========================================
INSERT INTO Ga (maGa, tenGa, viTri) VALUES 
('HN', N'Hà Nội', N'120 Lê Duẩn, Hà Nội'),
('SG', N'Sài Gòn', N'1 Nguyễn Thông, Quận 3, TP.HCM'),
('DN', N'Đà Nẵng', N'202 Hải Phòng, Đà Nẵng'),
('HUE', N'Huế', N'2 Bùi Thị Xuân, Huế'),
('NT', N'Nha Trang', N'17 Thái Nguyên, Nha Trang'),
('PT', N'Phan Thiết', N'Lê Hồng Phong, Phan Thiết'),
('QN', N'Quảng Ngãi', N'Đường Quang Trung, Quảng Ngãi'),
('VT', N'Vũng Tàu', N'Đường 3 Tháng 2, Vũng Tàu');
GO

PRINT N'✅ Đã thêm 8 Ga';

-- ========================================
-- 5. Tuyen (AA-BB, không vượt quá 10 ký tự)
-- ========================================
INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES 
-- Tuyến Bắc - Nam chính
('HN-SG', N'Tuyến Hà Nội TP.HCM', 1726.0),
('SG-HN', N'Tuyến TP.HCM Hà Nội', 1726.0),
-- Tuyến Bắc - Trung
('HN-DN', N'Tuyến Hà Nội Đà Nẵng', 791.0),
('DN-HN', N'Tuyến Đà Nẵng Hà Nội', 791.0),
-- Tuyến Bắc - Trung (Huế)
('HN-HUE', N'Tuyến Hà Nội Huế', 658.0),
('HUE-HN', N'Tuyến Huế Hà Nội', 658.0),
-- Tuyến Nam - Duyên hải
('SG-NT', N'Tuyến TP.HCM Nha Trang', 411.0),
('NT-SG', N'Tuyến Nha Trang TP.HCM', 411.0),
-- Tuyến Nam ngắn
('SG-PT', N'Tuyến TP.HCM Phan Thiết', 231.0),
('PT-SG', N'Tuyến Phan Thiết TP.HCM', 231.0);
GO

PRINT N'✅ Đã thêm 10 Tuyen';

-- ========================================
-- 6. ChuyenTau (soHieuTau: PREFIXNN, không vượt quá 10 ký tự)
-- Số lẻ = từ HN đi, số chẵn = về HN
-- ========================================
INSERT INTO ChuyenTau (soHieuTau, tocDo, maLoaiTau, namSanXuat) VALUES 
-- HN-SG & SG-HN
('SE1', 90.0, 'SE', 2020), ('SE2', 90.0, 'SE', 2020),
('SE3', 85.0, 'SE', 2019), ('SE4', 85.0, 'SE', 2019),
('TN1', 70.0, 'TN', 2018), ('TN2', 70.0, 'TN', 2018),
-- HN-DN & DN-HN
('SE7', 90.0, 'SE', 2020), ('SE8', 90.0, 'SE', 2020),
('SE9', 85.0, 'SE', 2019), ('SE10', 85.0, 'SE', 2019),
('TN11', 70.0, 'TN', 2018), ('TN12', 70.0, 'TN', 2018),
-- HN-HUE & HUE-HN
('SE13', 90.0, 'SE', 2020), ('SE14', 90.0, 'SE', 2020),
('SE15', 85.0, 'SE', 2019), ('SE16', 85.0, 'SE', 2019),
('TN17', 70.0, 'TN', 2018), ('TN18', 70.0, 'TN', 2018),
-- SG-NT & NT-SG
('SE19', 90.0, 'SE', 2021), ('SE20', 90.0, 'SE', 2021),
('SE21', 85.0, 'SE', 2020), ('SE22', 85.0, 'SE', 2020),
('TN23', 70.0, 'TN', 2019), ('TN24', 70.0, 'TN', 2019),
-- SG-PT & PT-SG
('SPT25', 80.0, 'SPT', 2021), ('SPT26', 80.0, 'SPT', 2021),
('SPT27', 75.0, 'SPT', 2020), ('SPT28', 75.0, 'SPT', 2020),
('SPT29', 70.0, 'SPT', 2019), ('SPT30', 70.0, 'SPT', 2019);
GO

PRINT N'✅ Đã thêm 30 ChuyenTau';

-- ========================================
-- 7. Toa (maToa: Txx - không vượt quá 10 ký tự)
-- THEO FORMAT ĐÚNG: T01, T02,...T10
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

PRINT N'✅ Đã thêm 300 Toa (T001-T300)';

-- ========================================
-- 8. ChoNgoi (maChoNgoi: XXYY - XX là toa, YY là ghế)
-- VD: 0101 = toa 01, ghế 01
-- ========================================
DECLARE @maToa NVARCHAR(20);
DECLARE @soToa INT;
DECLARE @soGhe INT;
DECLARE @maxGhe INT;
DECLARE @giaGhe DECIMAL(10,2);
DECLARE @maToaNumber INT;

DECLARE toa_cursor CURSOR FOR 
SELECT maToa, soToa 
FROM Toa
ORDER BY maToa;

OPEN toa_cursor;
FETCH NEXT FROM toa_cursor INTO @maToa, @soToa;

-- Lấy số thứ tự toa từ maToa (T001 -> 1, T002 -> 2,...)
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
            N'Ghế số ' + CAST(@soGhe AS NVARCHAR(3))
        );
        SET @soGhe = @soGhe + 1;
    END
    
    SET @maToaNumber = @maToaNumber + 1;
    FETCH NEXT FROM toa_cursor INTO @maToa, @soToa;
END

CLOSE toa_cursor;
DEALLOCATE toa_cursor;
GO

PRINT N'✅ Đã thêm 15,000+ ChoNgoi (format XXYY)';

-- ========================================
-- 9. LichTrinh (maLichTrinh: LTxxx-ddMMyyxx)
-- VD: LTSE1-18122501 (tàu SE1, ngày 18/12/25, lần 01)
-- TĂNG TỪ 10 NGÀY LÊN 30 NGÀY
-- ========================================
DECLARE @NgayBatDau DATE = CAST(GETDATE() AS DATE);
DECLARE @NgayKetThuc DATE = DATEADD(DAY, 29, @NgayBatDau);  -- 30 ngày
DECLARE @NgayHienTai DATE = @NgayBatDau;
DECLARE @ngayStr NVARCHAR(6);
DECLARE @lanChay INT;

WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    SET @ngayStr = FORMAT(@NgayHienTai, 'ddMMyy');  -- ddMMyy format
    
    -- ===== TUYẾN 1: HN ↔ SG =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LTSE1-' + @ngayStr + '01', 'SE1', 'HN', 'SG', 
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)), 'HN-SG', 1),
    ('LTSE3-' + @ngayStr + '01', 'SE3', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)), 'HN-SG', 1),
    ('LTTN1-' + @ngayStr + '01', 'TN1', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 2, @NgayHienTai), 'yyyy-MM-dd'), ' 04:00:00') AS DATETIME2(0)), 'HN-SG', 1),
    ('LTSE2-' + @ngayStr + '01', 'SE2', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)), 'SG-HN', 1),
    ('LTSE4-' + @ngayStr + '01', 'SE4', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)), 'SG-HN', 1),
    ('LTTN2-' + @ngayStr + '01', 'TN2', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 2, @NgayHienTai), 'yyyy-MM-dd'), ' 05:00:00') AS DATETIME2(0)), 'SG-HN', 1);
    
    -- ===== TUYẾN 2: HN ↔ DN =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LTSE7-' + @ngayStr + '01', 'SE7', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)), 'HN-DN', 1),
    ('LTSE9-' + @ngayStr + '01', 'SE9', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 03:00:00') AS DATETIME2(0)), 'HN-DN', 1),
    ('LTTN11-' + @ngayStr + '01', 'TN11', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)), 'HN-DN', 1),
    ('LTSE8-' + @ngayStr + '01', 'SE8', 'DN', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)), 'DN-HN', 1),
    ('LTSE10-' + @ngayStr + '01', 'SE10', 'DN', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:00:00') AS DATETIME2(0)), 'DN-HN', 1),
    ('LTTN12-' + @ngayStr + '01', 'TN12', 'DN', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)), 'DN-HN', 1);
    
    -- ===== TUYẾN 3: HN ↔ HUE =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LTSE13-' + @ngayStr + '01', 'SE13', 'HN', 'HUE',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)), 'HN-HUE', 1),
    ('LTSE15-' + @ngayStr + '01', 'SE15', 'HN', 'HUE',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 03:00:00') AS DATETIME2(0)), 'HN-HUE', 1),
    ('LTTN17-' + @ngayStr + '01', 'TN17', 'HN', 'HUE',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 10:00:00') AS DATETIME2(0)), 'HN-HUE', 1),
    ('LTSE14-' + @ngayStr + '01', 'SE14', 'HUE', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 09:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)), 'HUE-HN', 1),
    ('LTSE16-' + @ngayStr + '01', 'SE16', 'HUE', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:00:00') AS DATETIME2(0)), 'HUE-HN', 1),
    ('LTTN18-' + @ngayStr + '01', 'TN18', 'HUE', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)), 'HUE-HN', 1);
    
    -- ===== TUYẾN 4: SG ↔ NT =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LTSE19-' + @ngayStr + '01', 'SE19', 'SG', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 06:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)), 'SG-NT', 1),
    ('LTSE21-' + @ngayStr + '01', 'SE21', 'SG', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:30:00') AS DATETIME2(0)), 'SG-NT', 1),
    ('LTTN23-' + @ngayStr + '01', 'TN23', 'SG', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0)), 'SG-NT', 1),
    ('LTSE20-' + @ngayStr + '01', 'SE20', 'NT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:30:00') AS DATETIME2(0)), 'NT-SG', 1),
    ('LTSE22-' + @ngayStr + '01', 'SE22', 'NT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0)), 'NT-SG', 1),
    ('LTTN24-' + @ngayStr + '01', 'TN24', 'NT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)), 'NT-SG', 1);
    
    -- ===== TUYẾN 5: SG ↔ PT =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LTSPT25-' + @ngayStr + '01', 'SPT25', 'SG', 'PT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)), 'SG-PT', 1),
    ('LTSPT27-' + @ngayStr + '01', 'SPT27', 'SG', 'PT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 17:00:00') AS DATETIME2(0)), 'SG-PT', 1),
    ('LTSPT29-' + @ngayStr + '01', 'SPT29', 'SG', 'PT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0)), 'SG-PT', 1),
    ('LTSPT26-' + @ngayStr + '01', 'SPT26', 'PT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)), 'PT-SG', 1),
    ('LTSPT28-' + @ngayStr + '01', 'SPT28', 'PT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)), 'PT-SG', 1),
    ('LTSPT30-' + @ngayStr + '01', 'SPT30', 'PT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:00:00') AS DATETIME2(0)), 'PT-SG', 1);
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END
GO

PRINT N'✅ Đã thêm 900 LichTrinh (format LTxxx-ddMMyyxx) - 30 ngày';

-- ========================================
-- 10. NhanVien (maNhanVien: NVaabbxxxx)
-- aa = 2 số cuối năm vào làm, bb = 2 số cuối năm sinh, xxxx = số tự tăng
-- TĂNG TỪ 3 LÊN 15 NHÂN VIÊN
-- ========================================
INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, SDT, email, diaChi, chucVu, ngaySinh, ngayVaoLam, trangThai, gioiTinh)
VALUES 
-- Quản lý (chucVu = 0)
('NV24900001', '001234567890', N'Nguyễn Văn An', '0901234567', 'nva@railway.vn', N'Hà Nội', 0, '1990-01-01', '2024-03-01', 1, N'Nam'),
('NV24880002', '001234567899', N'Phạm Văn Bình', '0901234577', 'pvb@railway.vn', N'Hà Nội', 0, '1988-03-15', '2024-03-01', 1, N'Nam'),
-- Nhân viên (chucVu = 1)
('NV24950003', '001234567891', N'Trần Thị Bình', '0901234568', 'ttb@railway.vn', N'TP.HCM', 1, '1995-05-15', '2024-03-01', 1, N'Nữ'),
('NV24920004', '001234567892', N'Lê Văn Cường', '0901234569', 'lvc@railway.vn', N'Đà Nẵng', 1, '1992-08-20', '2024-03-01', 1, N'Nam'),
('NV24930005', '001234567893', N'Hoàng Thị Dung', '0901234570', 'htd@railway.vn', N'Hà Nội', 1, '1993-02-10', '2024-03-01', 1, N'Nữ'),
('NV24940006', '001234567894', N'Vũ Văn Em', '0901234571', 'vve@railway.vn', N'TP.HCM', 1, '1994-07-25', '2024-03-01', 1, N'Nam'),
('NV24960007', '001234567895', N'Đỗ Thị Phượng', '0901234572', 'dtp@railway.vn', N'Huế', 1, '1996-11-30', '2024-03-01', 1, N'Nữ'),
('NV24910008', '001234567896', N'Bùi Văn Giang', '0901234573', 'bvg@railway.vn', N'Đà Nẵng', 1, '1991-04-18', '2024-03-01', 1, N'Nam'),
('NV24970009', '001234567897', N'Mai Thị Hoa', '0901234574', 'mth@railway.vn', N'Nha Trang', 1, '1997-09-05', '2024-03-01', 1, N'Nữ'),
('NV24890010', '001234567898', N'Lý Văn Khoa', '0901234575', 'lvk@railway.vn', N'TP.HCM', 1, '1989-12-22', '2024-03-01', 1, N'Nam'),
('NV24950011', '001234578901', N'Ngô Thị Lan', '0901234576', 'ntl@railway.vn', N'Hà Nội', 1, '1995-06-14', '2024-03-01', 1, N'Nữ'),
('NV24920012', '001234578902', N'Trương Văn Minh', '0901234578', 'tvm@railway.vn', N'Đà Nẵng', 1, '1992-01-08', '2024-03-01', 1, N'Nam'),
('NV24980013', '001234578903', N'Cao Thị Nga', '0901234579', 'ctn@railway.vn', N'Phan Thiết', 1, '1998-03-20', '2024-03-01', 1, N'Nữ'),
('NV24930014', '001234578904', N'Đinh Văn Oanh', '0901234580', 'dvo@railway.vn', N'TP.HCM', 1, '1993-10-12', '2024-03-01', 1, N'Nam'),
('NV24960015', '001234578905', N'Phan Thị Phương', '0901234581', 'ptp@railway.vn', N'Quảng Ngãi', 1, '1996-08-28', '2024-03-01', 1, N'Nữ');

PRINT N'✅ Đã thêm 15 NhanVien (format NVaabbxxxx)';

-- ========================================
-- 11. TaiKhoan
-- TĂNG TỪ 3 LÊN 15 TÀI KHOẢN
-- ========================================
INSERT INTO TaiKhoan (userName, passWord, maNhanVien)
VALUES 
('admin', '123456', 'NV24900001'),
('quanly1', '123456', 'NV24880002'),
('nhanvien1', '123456', 'NV24950003'),
('nhanvien2', '123456', 'NV24920004'),
('nhanvien3', '123456', 'NV24930005'),
('nhanvien4', '123456', 'NV24940006'),
('nhanvien5', '123456', 'NV24960007'),
('nhanvien6', '123456', 'NV24910008'),
('nhanvien7', '123456', 'NV24970009'),
('nhanvien8', '123456', 'NV24890010'),
('nhanvien9', '123456', 'NV24950011'),
('nhanvien10', '123456', 'NV24920012'),
('nhanvien11', '123456', 'NV24980013'),
('nhanvien12', '123456', 'NV24930014'),
('nhanvien13', '123456', 'NV24960015');

PRINT N'✅ Đã thêm 15 TaiKhoan';

-- ========================================
-- 12. KhachHang (doiTuong: Trẻ em, Sinh viên, Người lớn, Người cao tuổi)
-- TĂNG TỪ 3 LÊN 50 KHÁCH HÀNG
-- ========================================
INSERT INTO KhachHang (maKH, CCCD, hoTen, email, SDT, doiTuong)
VALUES 
-- Người lớn (20 khách)
('KH001', '079123456789', N'Lê Minh Tuấn', 'lmt@gmail.com', '0987654321', N'Người lớn'),
('KH002', '079123456788', N'Trần Văn Bình', 'tvb@gmail.com', '0987654322', N'Người lớn'),
('KH003', '079123456787', N'Nguyễn Thị Cúc', 'ntc@gmail.com', '0987654323', N'Người lớn'),
('KH004', '079123456786', N'Hoàng Văn Dũng', 'hvd@gmail.com', '0987654324', N'Người lớn'),
('KH005', '079123456785', N'Vũ Thị Em', 'vte@gmail.com', '0987654325', N'Người lớn'),
('KH006', '079123456784', N'Đặng Văn Phong', 'dvp@gmail.com', '0987654326', N'Người lớn'),
('KH007', '079123456783', N'Bùi Thị Giang', 'btg@gmail.com', '0987654327', N'Người lớn'),
('KH008', '079123456782', N'Mai Văn Hải', 'mvh@gmail.com', '0987654328', N'Người lớn'),
('KH009', '079123456781', N'Cao Thị Hương', 'cth@gmail.com', '0987654329', N'Người lớn'),
('KH010', '079123456780', N'Lý Văn Khải', 'lvk@gmail.com', '0987654330', N'Người lớn'),
('KH011', '079123456779', N'Phan Thị Lan', 'ptl@gmail.com', '0987654331', N'Người lớn'),
('KH012', '079123456778', N'Trịnh Văn Minh', 'tvm@gmail.com', '0987654332', N'Người lớn'),
('KH013', '079123456777', N'Đỗ Thị Nga', 'dtn@gmail.com', '0987654333', N'Người lớn'),
('KH014', '079123456776', N'Võ Văn Oanh', 'vvo@gmail.com', '0987654334', N'Người lớn'),
('KH015', '079123456775', N'Lưu Thị Phượng', 'ltp@gmail.com', '0987654335', N'Người lớn'),
('KH016', '079123456774', N'Dương Văn Quân', 'dvq@gmail.com', '0987654336', N'Người lớn'),
('KH017', '079123456773', N'Tạ Thị Rơ', 'ttr@gmail.com', '0987654337', N'Người lớn'),
('KH018', '079123456772', N'Đinh Văn Sơn', 'dvs@gmail.com', '0987654338', N'Người lớn'),
('KH019', '079123456771', N'Ninh Thị Tâm', 'ntt@gmail.com', '0987654339', N'Người lớn'),
('KH020', '079123456770', N'Hồ Văn Út', 'hvu@gmail.com', '0987654340', N'Người lớn'),

-- Sinh viên (15 khách)
('KH021', '079123456769', N'Phạm Thu Hà', 'pth@gmail.com', '0987654341', N'Sinh viên'),
('KH022', '079123456768', N'Nguyễn Văn An', 'nva@gmail.com', '0987654342', N'Sinh viên'),
('KH023', '079123456767', N'Lê Thị Bảo', 'ltb@gmail.com', '0987654343', N'Sinh viên'),
('KH024', '079123456766', N'Trần Văn Cường', 'tvc@gmail.com', '0987654344', N'Sinh viên'),
('KH025', '079123456765', N'Hoàng Thị Duyên', 'htd@gmail.com', '0987654345', N'Sinh viên'),
('KH026', '079123456764', N'Vũ Văn Hùng', 'vvh@gmail.com', '0987654346', N'Sinh viên'),
('KH027', '079123456763', N'Đặng Thị Kim', 'dtk@gmail.com', '0987654347', N'Sinh viên'),
('KH028', '079123456762', N'Bùi Văn Long', 'bvl@gmail.com', '0987654348', N'Sinh viên'),
('KH029', '079123456761', N'Mai Thị Ngọc', 'mtn@gmail.com', '0987654349', N'Sinh viên'),
('KH030', '079123456760', N'Cao Văn Phát', 'cvp@gmail.com', '0987654350', N'Sinh viên'),
('KH031', '079123456759', N'Lý Thị Quỳnh', 'ltq@gmail.com', '0987654351', N'Sinh viên'),
('KH032', '079123456758', N'Phan Văn Rộng', 'pvr@gmail.com', '0987654352', N'Sinh viên'),
('KH033', '079123456757', N'Trịnh Thị Sương', 'tts@gmail.com', '0987654353', N'Sinh viên'),
('KH034', '079123456756', N'Đỗ Văn Thắng', 'dvt@gmail.com', '0987654354', N'Sinh viên'),
('KH035', '079123456755', N'Võ Thị Uyên', 'vtu@gmail.com', '0987654355', N'Sinh viên'),

-- Người cao tuổi (10 khách)
('KH036', '079123456754', N'Ngô Thị Mai', 'ntm@gmail.com', '0987654356', N'Người cao tuổi'),
('KH037', '079123456753', N'Trần Văn Bá', 'tvba@gmail.com', '0987654357', N'Người cao tuổi'),
('KH038', '079123456752', N'Lê Thị Chi', 'ltc@gmail.com', '0987654358', N'Người cao tuổi'),
('KH039', '079123456751', N'Nguyễn Văn Đạt', 'nvd@gmail.com', '0987654359', N'Người cao tuổi'),
('KH040', '079123456750', N'Hoàng Thị Hằng', 'hth@gmail.com', '0987654360', N'Người cao tuổi'),
('KH041', '079123456749', N'Vũ Văn Kiên', 'vvk@gmail.com', '0987654361', N'Người cao tuổi'),
('KH042', '079123456748', N'Đặng Thị Lệ', 'dtl@gmail.com', '0987654362', N'Người cao tuổi'),
('KH043', '079123456747', N'Bùi Văn Nam', 'bvn@gmail.com', '0987654363', N'Người cao tuổi'),
('KH044', '079123456746', N'Mai Thị Oanh', 'mto@gmail.com', '0987654364', N'Người cao tuổi'),
('KH045', '079123456745', N'Cao Văn Phúc', 'cvph@gmail.com', '0987654365', N'Người cao tuổi'),

-- Trẻ em (5 khách)
('KH046', '079123456744', N'Lý Văn Bé', 'lvb@gmail.com', '0987654366', N'Trẻ em'),
('KH047', '079123456743', N'Phan Thị Con', 'ptc@gmail.com', '0987654367', N'Trẻ em'),
('KH048', '079123456742', N'Trịnh Văn Đẹp', 'tvd@gmail.com', '0987654368', N'Trẻ em'),
('KH049', '079123456741', N'Đỗ Thị Út', 'dtu@gmail.com', '0987654369', N'Trẻ em'),
('KH050', '079123456740', N'Võ Văn Nhỏ', 'vvn@gmail.com', '0987654370', N'Trẻ em');

PRINT N'✅ Đã thêm 50 KhachHang (20 người lớn + 15 sinh viên + 10 người cao tuổi + 5 trẻ em)';

-- ========================================
-- 13. KHUYẾN MÃI (maKhuyenMai: KMddMMyyyyxx)
-- ========================================
PRINT N'';
PRINT N'🎉 Bắt đầu thêm dữ liệu Khuyến Mãi...';

-- =============================================
-- PHẦN 1: KHUYẾN MÃI THEO ĐỐI TƯỢNG (KMKH)
-- =============================================

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

PRINT N'✅ Đã thêm 3 Khuyến mãi Đối tượng (KMKH)';

-- =============================================
-- PHẦN 2: KHUYẾN MÃI THEO HÓA ĐƠN (KMHD)
-- =============================================

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

PRINT N'✅ Đã thêm 4 Khuyến mãi Hóa đơn (KMHD)';
PRINT N'✅ Đã thêm 7 Chi tiết khuyến mãi vào ChiTietKhuyenMai';

-- ========================================
-- HOÀN THÀNH
-- ========================================
PRINT N'';
PRINT N'🎉🎉🎉 HOÀN THÀNH! 🎉🎉🎉';
PRINT N'';
PRINT N'✅ Database: HTQLVT';
PRINT N'✅ Trains: 30 tàu (format PREFIXNN)';
PRINT N'✅ Coaches: 300 toa (format Txx: T001-T300)';
PRINT N'✅ Seats: 15,000+ chỗ (format XXYY: 0101, 0102,...)';
PRINT N'✅ Routes: 5 tuyến (format AA-BB)';
PRINT N'✅ Schedules: 900 lịch trình (format LTxxx-ddMMyyxx) - 30 NGÀY';
PRINT N'✅ Employees: 15 nhân viên (format NVaabbxxxx)';
PRINT N'✅ Accounts: 15 tài khoản';
PRINT N'✅ Customers: 50 khách hàng';
PRINT N'✅ Promotions: 7 khuyến mãi (format KMddMMyyyyxx)';
PRINT N'✅ Data: 30 NGÀY TỪ HÔM NAY (' + CONVERT(NVARCHAR(10), GETDATE(), 103) + ')';
PRINT N'';
PRINT N'📌 ĐÚNG FORMAT THEO TÀI LIỆU:';
PRINT N'  ✓ maNhanVien: NVaabbxxxx (VD: NV24900001)';
PRINT N'  ✓ maToa: Txx (VD: T001, T002)';
PRINT N'  ✓ maChoNgoi: XXYY (VD: 0101 = toa 01, ghế 01)';
PRINT N'  ✓ maLichTrinh: LTxxx-ddMMyyxx (VD: LTSE1-18122501)';
PRINT N'  ✓ maKhuyenMai: KMddMMyyyyxx (VD: KM0101202401)';
PRINT N'  ✓ maLoaiVe: LVxx (VD: LV01, LV02)';
PRINT N'  ✓ soHieuTau: PREFIXNN (VD: SE1, TN2)';
PRINT N'  ✓ maTuyen: AA-BB (VD: HN-SG)';
PRINT N'';
PRINT N'📌 Login: admin / 123456';
PRINT N'📌 Mỗi tuyến: 3 chuyến ĐI (lẻ) + 3 chuyến VỀ (chẵn)';
PRINT N'📌 Mỗi tàu: 500 chỗ (320 ngồi + 180 nằm)';
PRINT N'';
PRINT N'📋 KHUYẾN MÃI:';
PRINT N'  - Đối tượng: Trẻ em 25%, Người cao tuổi 15%, Sinh viên 10%';
PRINT N'  - Hóa đơn: 11-40 vé (9%), 42-70 vé (11%), 71-100 vé (13%), ≥100 vé (15%)';
PRINT N'';
PRINT N'📊 KIỂM TRA DỮ LIỆU:';
SELECT 
    'LoaiTau' AS [Bảng], COUNT(*) AS [Số dòng] FROM LoaiTau UNION ALL
SELECT 'LoaiToa', COUNT(*) FROM LoaiToa UNION ALL
SELECT 'LoaiVe', COUNT(*) FROM LoaiVe UNION ALL
SELECT 'Ga', COUNT(*) FROM Ga UNION ALL
SELECT 'Tuyen', COUNT(*) FROM Tuyen UNION ALL
SELECT 'ChuyenTau', COUNT(*) FROM ChuyenTau UNION ALL
SELECT 'Toa', COUNT(*) FROM Toa UNION ALL
SELECT 'ChoNgoi', COUNT(*) FROM ChoNgoi UNION ALL
SELECT 'LichTrinh', COUNT(*) FROM LichTrinh UNION ALL
SELECT 'NhanVien', COUNT(*) FROM NhanVien UNION ALL
SELECT 'TaiKhoan', COUNT(*) FROM TaiKhoan UNION ALL
SELECT 'KhachHang', COUNT(*) FROM KhachHang UNION ALL
SELECT 'KhuyenMai', COUNT(*) FROM KhuyenMai UNION ALL
SELECT 'ChiTietKhuyenMai', COUNT(*) FROM ChiTietKhuyenMai;

-- Hiển thị mẫu dữ liệu
PRINT N'';
PRINT N'📋 MẪU DỮ LIỆU:';
SELECT TOP 5 maToa AS [Mã Toa (Txx)] FROM Toa ORDER BY maToa;
SELECT TOP 5 maChoNgoi AS [Mã Chỗ Ngồi (XXYY)] FROM ChoNgoi ORDER BY maChoNgoi;
SELECT TOP 5 maLichTrinh AS [Mã Lịch Trình (LTxxx-ddMMyyxx)] FROM LichTrinh ORDER BY maLichTrinh;
SELECT maNhanVien AS [Mã Nhân Viên (NVaabbxxxx)], hoTen FROM NhanVien;
SELECT maKhuyenMai AS [Mã Khuyến Mãi (KMddMMyyyyxx)], tenKhuyenMai FROM KhuyenMai;

-- Hiển thị chi tiết khuyến mãi
PRINT N'';
PRINT N'🎁 CHI TIẾT KHUYẾN MÃI:';
SELECT 
    km.maKhuyenMai,
    km.tenKhuyenMai,
    km.loaiKhuyenMai,
    ctkm.dieuKien,
    ctkm.chietKhau,
    CAST(ctkm.chietKhau * 100 AS INT) AS [% Giảm]
FROM KhuyenMai km
LEFT JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
ORDER BY km.loaiKhuyenMai, ctkm.chietKhau;
GO
