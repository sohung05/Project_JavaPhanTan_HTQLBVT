-- ========================================
-- THÊM CÁC TUYẾN THIẾU CHO DASHBOARD
-- File: 10_ThemTuyenThieuChoDashboard.sql
-- Mô tả: Thêm 6 tuyến còn thiếu để Dashboard hiển thị đủ 10 tuyến
-- ========================================

USE HTQLVT;
GO

PRINT N'🚀 Bắt đầu thêm các tuyến thiếu cho Dashboard...';
PRINT N'';

-- ========================================
-- 1. THÊM 6 TUYẾN THIẾU
-- ========================================
PRINT N'📍 Bước 1: Thêm 6 tuyến còn thiếu...';

-- DN-HN
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'DN-HN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('DN-HN', N'Đà Nẵng - Hà Nội', 791.0);
    PRINT N'   ✅ Đã thêm tuyến DN-HN';
END

-- HN-HUE
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'HN-HUE')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('HN-HUE', N'Hà Nội - Huế', 658.0);
    PRINT N'   ✅ Đã thêm tuyến HN-HUE';
END

-- HUE-HN
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'HUE-HN')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('HUE-HN', N'Huế - Hà Nội', 658.0);
    PRINT N'   ✅ Đã thêm tuyến HUE-HN';
END

-- NT-SG
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'NT-SG')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('NT-SG', N'Nha Trang - Sài Gòn', 411.0);
    PRINT N'   ✅ Đã thêm tuyến NT-SG';
END

-- SG-NT
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'SG-NT')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('SG-NT', N'Sài Gòn - Nha Trang', 411.0);
    PRINT N'   ✅ Đã thêm tuyến SG-NT';
END

-- SG-PT
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'SG-PT')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('SG-PT', N'Sài Gòn - Phan Thiết', 230.0);
    PRINT N'   ✅ Đã thêm tuyến SG-PT';
END

-- PT-SG
IF NOT EXISTS (SELECT 1 FROM Tuyen WHERE maTuyen = 'PT-SG')
BEGIN
    INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES ('PT-SG', N'Phan Thiết - Sài Gòn', 230.0);
    PRINT N'   ✅ Đã thêm tuyến PT-SG';
END

PRINT N'';

-- ========================================
-- 2. THÊM LỊCH TRÌNH CHO 7 TUYẾN MỚI (THÁNG 12)
-- ========================================
PRINT N'📅 Bước 2: Thêm lịch trình cho 7 tuyến mới (1-22/12/2024)...';

DECLARE @ngay DATE = '2024-12-01';
DECLARE @ngayStr NVARCHAR(6);
DECLARE @soLichTrinhMoi INT = 0;

WHILE @ngay <= CAST(GETDATE() AS DATE)
BEGIN
    SET @ngayStr = FORMAT(@ngay, 'ddMMyy');
    
    -- DN-HN (SE2, SE4, SE6)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE2-' + @ngayStr + '-DN-HN')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE2-' + @ngayStr + '-DN-HN', 'SE2', 'DN-HN', 'DN', 'HN', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    -- HN-HUE (SE1, SE3)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE1-' + @ngayStr + '-HN-HUE')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE1-' + @ngayStr + '-HN-HUE', 'SE1', 'HN-HUE', 'HN', 'HUE', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 05:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 17:30:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    -- HUE-HN (SE2, SE4)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE2-' + @ngayStr + '-HUE-HN')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE2-' + @ngayStr + '-HUE-HN', 'SE2', 'HUE-HN', 'HUE', 'HN', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 18:30:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    -- NT-SG (SE1, SE3)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE1-' + @ngayStr + '-NT-SG')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE1-' + @ngayStr + '-NT-SG', 'SE1', 'NT-SG', 'NT', 'SG', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 21:30:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    -- SG-NT (SE2, SE4)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE2-' + @ngayStr + '-SG-NT')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE2-' + @ngayStr + '-SG-NT', 'SE2', 'SG-NT', 'SG', 'NT', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    -- SG-PT (SE2, SE4)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE2-' + @ngayStr + '-SG-PT')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE2-' + @ngayStr + '-SG-PT', 'SE2', 'SG-PT', 'SG', 'PT', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 09:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    -- PT-SG (SE1, SE3)
    IF NOT EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = 'LTSE1-' + @ngayStr + '-PT-SG')
    BEGIN
        INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, trangThai)
        VALUES ('LTSE1-' + @ngayStr + '-PT-SG', 'SE1', 'PT-SG', 'PT', 'SG', 
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 10:00:00') AS DATETIME2(0)),
                CAST(CONCAT(FORMAT(@ngay, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)), 1);
        SET @soLichTrinhMoi = @soLichTrinhMoi + 1;
    END
    
    SET @ngay = DATEADD(DAY, 1, @ngay);
END

PRINT N'   ✅ Đã thêm ' + CAST(@soLichTrinhMoi AS NVARCHAR(10)) + N' lịch trình mới';
PRINT N'';

-- ========================================
-- HOÀN THÀNH
-- ========================================
PRINT N'🎉 ========================================';
PRINT N'✅ HOÀN THÀNH THÊM TUYẾN CHO DASHBOARD!';
PRINT N'';
PRINT N'📊 Danh sách 10 tuyến Dashboard:';
PRINT N'   1. DN-HN       ✅ (Đà Nẵng - Hà Nội)';
PRINT N'   2. HN-DN       ✅ (Hà Nội - Đà Nẵng)';
PRINT N'   3. HN-HUE      ✅ (Hà Nội - Huế)';
PRINT N'   4. HN-SG       ✅ (Hà Nội - Sài Gòn)';
PRINT N'   5. HUE-HN      ✅ (Huế - Hà Nội)';
PRINT N'   6. NT-SG       ✅ (Nha Trang - Sài Gòn)';
PRINT N'   7. PT-SG       ✅ (Phan Thiết - Sài Gòn)';
PRINT N'   8. SG-HN       ✅ (Sài Gòn - Hà Nội)';
PRINT N'   9. SG-NT       ✅ (Sài Gòn - Nha Trang)';
PRINT N'   10. SG-PT      ✅ (Sài Gòn - Phan Thiết)';
PRINT N'';
PRINT N'💡 Bước tiếp theo:';
PRINT N'   ➡️ Chạy file 09_BoSungVeThang12.sql để tạo vé cho 10 tuyến';
PRINT N'   ➡️ Refresh Dashboard để thấy đầy đủ 10 tuyến!';
PRINT N'';
PRINT N'🎉 ========================================';
GO

