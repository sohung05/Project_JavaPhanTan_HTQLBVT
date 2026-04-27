-- ========================================
-- XÓA LỊCH TRÌNH CŨ + TẠO LỊCH TRÌNH PHỔ BIẾN (6 THÁNG)
-- File: 07_v2_ResetLichTrinhPhoBien.sql
-- Mô tả: Xóa lịch trình cũ của file 07, tạo lại cho 6 tháng qua
-- ========================================

USE HTQLVT;
GO

PRINT N'🚀 Bắt đầu reset và tạo lịch trình phổ biến (6 tháng)...';
PRINT N'';

-- ========================================
-- BƯỚC 0: XÓA LỊCH TRÌNH CŨ (NẾU CÓ)
-- ========================================
PRINT N'🗑️  Bước 0: Xóa lịch trình cũ (nếu đã chạy file 07 trước đó)...';

DELETE FROM LichTrinh 
WHERE maLichTrinh LIKE 'LTSE1-%-HN-SG'
   OR maLichTrinh LIKE 'LTSE1-%-HN-DN'
   OR maLichTrinh LIKE 'LTSE1-%-HN-NT'
   OR maLichTrinh LIKE 'LTSE1-%-DN-SG'
   OR maLichTrinh LIKE 'LTSE1-%-VINH-SG'
   OR maLichTrinh LIKE 'LTSE2-%-SG-HN'
   OR maLichTrinh LIKE 'LTSE2-%-SG-DN'
   OR maLichTrinh LIKE 'LTSE2-%-NT-HN'
   OR maLichTrinh LIKE 'LTSE2-%-NT-DN'
   OR maLichTrinh LIKE 'LTSE3-%-HN-SG'
   OR maLichTrinh LIKE 'LTSE3-%-HN-DN'
   OR maLichTrinh LIKE 'LTSE4-%-SG-HN'
   OR maLichTrinh LIKE 'LTSE4-%-SG-DN'
   OR maLichTrinh LIKE 'LTSE5-%-HN-SG'
   OR maLichTrinh LIKE 'LTSE6-%-SG-HN';

PRINT N'✅ Đã xóa lịch trình cũ';
PRINT N'';

-- ========================================
-- BƯỚC 1: THÊM CÁC TUYẾN PHỔ BIẾN
-- ========================================
PRINT N'📍 Bước 1: Thêm tuyến phổ biến...';

DECLARE @soTuyenMoi INT = 0;

-- Tuyến Bắc → Nam
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'HN-SG')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('HN-SG', N'Hà Nội - Sài Gòn', 1726.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'HN-DN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('HN-DN', N'Hà Nội - Đà Nẵng', 791.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'HN-NT')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('HN-NT', N'Hà Nội - Nha Trang', 1200.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'DN-SG')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('DN-SG', N'Đà Nẵng - Sài Gòn', 800.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'DN-NT')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('DN-NT', N'Đà Nẵng - Nha Trang', 350.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'VINH-SG')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('VINH-SG', N'Vinh - Sài Gòn', 1446.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

-- Tuyến Nam → Bắc
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'SG-HN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('SG-HN', N'Sài Gòn - Hà Nội', 1726.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'SG-DN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('SG-DN', N'Sài Gòn - Đà Nẵng', 800.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'NT-HN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('NT-HN', N'Nha Trang - Hà Nội', 1200.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'NT-DN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('NT-DN', N'Nha Trang - Đà Nẵng', 350.0);
    SET @soTuyenMoi = @soTuyenMoi + 1;
END

IF @soTuyenMoi > 0
    PRINT N'✅ Đã thêm ' + CAST(@soTuyenMoi AS NVARCHAR(2)) + N' tuyến mới';
ELSE
    PRINT N'⚠️  Tất cả tuyến đã tồn tại, không thêm mới';

GO

-- ========================================
-- BƯỚC 2: TẠO LỊCH TRÌNH CHO 6 THÁNG QUÁ KHỨ
-- (180 ngày, mỗi ngày ~15 lịch trình)
-- ========================================
PRINT N'';
PRINT N'📅 Bước 2: Tạo lịch trình cho 6 tháng qua (180 ngày)...';
PRINT N'   Mỗi chuyến tàu mỗi ngày có ~5 lịch trình (các đoạn phổ biến)';
PRINT N'';

DECLARE @NgayBatDau DATE = DATEADD(DAY, -180, CAST(GETDATE() AS DATE));
DECLARE @NgayKetThuc DATE = CAST(GETDATE() AS DATE);
DECLARE @NgayHienTai DATE = @NgayBatDau;
DECLARE @ngayStr NVARCHAR(6);

WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    SET @ngayStr = FORMAT(@NgayHienTai, 'ddMMyy');
    
    -- ========================================
    -- SE1: Thêm các đoạn phổ biến
    -- ========================================
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES
    -- HN → SG (phổ biến nhất)
    ('LTSE1-' + @ngayStr + '-HN-SG', 'SE1', 'HN-SG', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:30:00') AS DATETIME2(0)), 1),
    
    -- HN → DN
    ('LTSE1-' + @ngayStr + '-HN-DN', 'SE1', 'HN-DN', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:00:00') AS DATETIME2(0)), 1),
    
    -- HN → NT
    ('LTSE1-' + @ngayStr + '-HN-NT', 'SE1', 'HN-NT', 'HN', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)), 1),
    
    -- DN → SG
    ('LTSE1-' + @ngayStr + '-DN-SG', 'SE1', 'DN-SG', 'DN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:30:00') AS DATETIME2(0)), 1),
    
    -- VINH → SG
    ('LTSE1-' + @ngayStr + '-VINH-SG', 'SE1', 'VINH-SG', 'VINH', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:30:00') AS DATETIME2(0)), 1);
    
    -- ========================================
    -- SE2: Thêm các đoạn phổ biến (chiều ngược)
    -- ========================================
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES
    -- SG → HN (phổ biến nhất)
    ('LTSE2-' + @ngayStr + '-SG-HN', 'SE2', 'SG-HN', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 00:00:00') AS DATETIME2(0)), 1),
    
    -- SG → DN
    ('LTSE2-' + @ngayStr + '-SG-DN', 'SE2', 'SG-DN', 'SG', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)), 1),
    
    -- NT → HN
    ('LTSE2-' + @ngayStr + '-NT-HN', 'SE2', 'NT-HN', 'NT', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 00:00:00') AS DATETIME2(0)), 1),
    
    -- NT → DN
    ('LTSE2-' + @ngayStr + '-NT-DN', 'SE2', 'NT-DN', 'NT', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)), 1);
    
    -- ========================================
    -- SE3, SE4, SE5, SE6: Chỉ thêm 2-3 đoạn chính
    -- ========================================
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai) VALUES
    -- SE3: HN-SG, HN-DN
    ('LTSE3-' + @ngayStr + '-HN-SG', 'SE3', 'HN-SG', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)), 1),
    ('LTSE3-' + @ngayStr + '-HN-DN', 'SE3', 'HN-DN', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 00:30:00') AS DATETIME2(0)), 1),
    
    -- SE4: SG-HN, SG-DN
    ('LTSE4-' + @ngayStr + '-SG-HN', 'SE4', 'SG-HN', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 08:30:00') AS DATETIME2(0)), 1),
    ('LTSE4-' + @ngayStr + '-SG-DN', 'SE4', 'SG-DN', 'SG', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0)), 1),
    
    -- SE5: HN-SG
    ('LTSE5-' + @ngayStr + '-HN-SG', 'SE5', 'HN-SG', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 17:30:00') AS DATETIME2(0)), 1),
    
    -- SE6: SG-HN
    ('LTSE6-' + @ngayStr + '-SG-HN', 'SE6', 'SG-HN', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 18:30:00') AS DATETIME2(0)), 1);
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END
GO

PRINT N'✅ Đã thêm ~2700 lịch trình cho các tuyến phổ biến (180 ngày × 15 lịch trình/ngày)';
PRINT N'';

-- ========================================
-- HOÀN THÀNH
-- ========================================
PRINT N'🎉 ========================================';
PRINT N'✅ HOÀN THÀNH TẠO LỊCH TRÌNH CHO 6 THÁNG!';
PRINT N'';
PRINT N'📊 Tóm tắt:';
PRINT N'   ✔️ 10 tuyến phổ biến (HN-SG, SG-HN, HN-DN, DN-SG...)';
PRINT N'   ✔️ ~2700 lịch trình bổ sung (180 ngày × 15/ngày)';
PRINT N'   ✔️ Thời gian: 6 tháng qua (26/06/2024 → 22/12/2024)';
PRINT N'   ✔️ Tổng lịch trình: ~2880 (180 từ file 04 + 2700 mới)';
PRINT N'';
PRINT N'⚠️  BƯỚC TIẾP THEO:';
PRINT N'   → Chạy file 08 để thêm vé cho các lịch trình mới';
PRINT N'   → File 08 sẽ tạo ~5000 vé cho 6 tháng qua';
PRINT N'';
PRINT N'🎉 ========================================';
GO






