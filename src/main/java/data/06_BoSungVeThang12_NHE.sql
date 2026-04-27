-- ========================================
-- BỔ SUNG VÉ CHO THÁNG 12 (PHIÊN BẢN NHẸ)
-- File: 09_BoSungVeThang12_NHE.sql
-- Mô tả: Thêm ~600 vé cho tháng 12 (đủ cho biểu đồ, chạy nhanh ~20 giây)
-- ========================================

USE HTQLVT;
GO

PRINT N'🚀 Bắt đầu bổ sung vé cho tháng 12 (phiên bản nhẹ)...';
PRINT N'';

-- ========================================
-- BỔ SUNG VÉ CHO THÁNG 12
-- Thời gian: 1-22/12/2024
-- Số vé: ~27 vé/ngày × 22 ngày = ~600 vé (NHẸ)
-- Phân bố đều cho 10 tuyến Dashboard
-- ========================================

DECLARE @NgayBatDau DATE = CAST('2024-12-01' AS DATE);
DECLARE @NgayHienTai DATE = @NgayBatDau;
DECLARE @NgayKetThuc DATE = CAST(GETDATE() AS DATE);
DECLARE @soHD INT = 60000; -- Bắt đầu từ số lớn để không trùng
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
DECLARE @random INT;
DECLARE @randomTuyen INT;
DECLARE @gioTao DATETIME2(0);
DECLARE @thoiGianLenTau DATETIME2(0);
DECLARE @soVeNgay INT;
DECLARE @tongVeTao INT = 0;

WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    SET @ngayStr = FORMAT(@NgayHienTai, 'ddMMyy');
    
    -- Cuối tuần nhiều hơn (NHẸ HƠN)
    IF DATEPART(WEEKDAY, @NgayHienTai) IN (1, 7)
        SET @soVeNgay = 35; -- Cuối tuần: 35 vé (thay vì 130)
    ELSE
        SET @soVeNgay = 25; -- Ngày thường: 25 vé (thay vì 110)
    
    DECLARE @j INT = 1;
    WHILE @j <= @soVeNgay
    BEGIN
        -- Tạo mã hóa đơn duy nhất
        SET @maHoaDon = 'HDC' + @ngayStr + RIGHT('000000' + CAST(@soHD AS NVARCHAR(10)), 6);
        
        -- Random khách hàng (1-150)
        SET @random = 1 + (@soHD % 150);
        SET @maKhachHang = 'KH' + RIGHT('0000' + CAST(@random AS NVARCHAR(4)), 4);
        SET @maNhanVien = 'NV00' + CAST(1 + (@soHD % 3) AS NVARCHAR(1));
        
        -- Random tuyến (phân bố ĐỀU cho 10 tuyến DASHBOARD - DÙNG LỊCH TRÌNH TỪ FILE 10)
        SET @randomTuyen = 1 + (@soHD % 20);
        
        -- Tuyến 1: HN-SG (25%) - Dùng lịch trình toàn tuyến LC-BL
        IF @randomTuyen <= 5
        BEGIN
            SET @random = 1 + (@soHD % 3);
            IF @random = 1
                SET @maLichTrinh = 'LTSE1-' + @ngayStr + '01';
            ELSE IF @random = 2
                SET @maLichTrinh = 'LTSE3-' + @ngayStr + '01';
            ELSE
                SET @maLichTrinh = 'LTSE5-' + @ngayStr + '01';
        END
        -- Tuyến 2: SG-HN (25%) - Dùng lịch trình toàn tuyến BL-LC
        ELSE IF @randomTuyen <= 10
        BEGIN
            SET @random = 1 + (@soHD % 3);
            IF @random = 1
                SET @maLichTrinh = 'LTSE2-' + @ngayStr + '01';
            ELSE IF @random = 2
                SET @maLichTrinh = 'LTSE4-' + @ngayStr + '01';
            ELSE
                SET @maLichTrinh = 'LTSE6-' + @ngayStr + '01';
        END
        -- Tuyến 3: HN-DN (15%) - Dùng lịch trình toàn tuyến LC-BL
        ELSE IF @randomTuyen <= 13
        BEGIN
            SET @random = 1 + (@soHD % 3);
            IF @random = 1
                SET @maLichTrinh = 'LTSE1-' + @ngayStr + '01';
            ELSE IF @random = 2
                SET @maLichTrinh = 'LTSE3-' + @ngayStr + '01';
            ELSE
                SET @maLichTrinh = 'LTSE5-' + @ngayStr + '01';
        END
        -- Tuyến 4: DN-HN (10%) - Dùng lịch trình từ file 10
        ELSE IF @randomTuyen <= 15
            SET @maLichTrinh = 'LTSE2-' + @ngayStr + '-DN-HN';
        -- Tuyến 5: HN-HUE (7%) - Dùng lịch trình từ file 10
        ELSE IF @randomTuyen = 16
            SET @maLichTrinh = 'LTSE1-' + @ngayStr + '-HN-HUE';
        -- Tuyến 6: HUE-HN (7%) - Dùng lịch trình từ file 10
        ELSE IF @randomTuyen = 17
            SET @maLichTrinh = 'LTSE2-' + @ngayStr + '-HUE-HN';
        -- Tuyến 7: SG-NT (5%) - Dùng lịch trình từ file 10
        ELSE IF @randomTuyen = 18
            SET @maLichTrinh = 'LTSE2-' + @ngayStr + '-SG-NT';
        -- Tuyến 8: NT-SG (3%) - Dùng lịch trình từ file 10
        ELSE IF @randomTuyen = 19
            SET @maLichTrinh = 'LTSE1-' + @ngayStr + '-NT-SG';
        -- Tuyến 9: SG-PT (2%) - Dùng lịch trình từ file 10
        ELSE IF @randomTuyen = 20
            SET @maLichTrinh = 'LTSE2-' + @ngayStr + '-SG-PT';
        -- Dự phòng: PT-SG (1%) - Dùng lịch trình từ file 10
        ELSE
            SET @maLichTrinh = 'LTSE1-' + @ngayStr + '-PT-SG';
        
        -- Lấy chỗ ngồi CÓ THẬT từ database (thay vì random)
        -- Random loại ghế (50% ghế ngồi, 50% giường nằm)
        IF (@soHD % 2) = 0
        BEGIN
            -- Lấy ghế ngồi từ ChoNgoi (loại toa ghế ngồi)
            SELECT TOP 1 
                @maChoNgoi = cn.maChoNgoi,
                @maToa = cn.maToa,
                @giaGoc = cn.gia
            FROM ChoNgoi cn
            JOIN Toa t ON t.maToa = cn.maToa
            JOIN LoaiToa lt ON lt.maLoaiToa = t.maLoaiToa
            WHERE lt.tenLoaiToa LIKE N'%Ngồi%'
            ORDER BY NEWID(); -- Random
        END
        ELSE
        BEGIN
            -- Lấy giường nằm từ ChoNgoi (loại toa giường nằm)
            SELECT TOP 1 
                @maChoNgoi = cn.maChoNgoi,
                @maToa = cn.maToa,
                @giaGoc = cn.gia
            FROM ChoNgoi cn
            JOIN Toa t ON t.maToa = cn.maToa
            JOIN LoaiToa lt ON lt.maLoaiToa = t.maLoaiToa
            WHERE lt.tenLoaiToa LIKE N'%Giường%' OR lt.tenLoaiToa LIKE N'%Nằm%'
            ORDER BY NEWID(); -- Random
        END
        
        -- Random loại vé (80% người lớn, 15% trẻ em, 5% sinh viên)
        SET @random = 1 + (@soHD % 20);
        IF @random <= 16 
        BEGIN
            SET @maLoaiVe = 'LV01'; -- Người lớn
            SET @mucGiam = 0;
            SET @giaVe = @giaGoc;
        END
        ELSE IF @random <= 19
        BEGIN
            SET @maLoaiVe = 'LV02'; -- Trẻ em (-10%)
            SET @mucGiam = @giaGoc * 0.10;
            SET @giaVe = @giaGoc - @mucGiam;
        END
        ELSE
        BEGIN
            SET @maLoaiVe = 'LV03'; -- Sinh viên (-25%)
            SET @mucGiam = @giaGoc * 0.25;
            SET @giaVe = @giaGoc - @mucGiam;
        END
        
        SET @tongTien = @giaVe;
        
        -- Giờ tạo trong ngày (6h-22h)
        SET @gioTao = CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' ', 
                     RIGHT('00' + CAST(6 + (@soHD % 16) AS NVARCHAR(2)), 2), ':', 
                     RIGHT('00' + CAST((@soHD % 60) AS NVARCHAR(2)), 2), ':00') AS DATETIME2(0));
        
        -- Thời gian lên tàu (1-2 ngày sau)
        SET @thoiGianLenTau = DATEADD(DAY, 1 + (@soHD % 2), @gioTao);
        
        -- Kiểm tra lịch trình có tồn tại không
        IF EXISTS (SELECT 1 FROM LichTrinh WHERE maLichTrinh = @maLichTrinh)
        BEGIN
            -- Tạo hóa đơn
            INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, gioTao, ngayTao, tongTien, trangThai)
            VALUES (@maHoaDon, @maNhanVien, @maKhachHang, @gioTao, @gioTao, @tongTien, 1);
            
            -- Tạo vé
            SET @maVe = 'VEC' + @ngayStr + RIGHT('000000' + CAST(@soHD AS NVARCHAR(10)), 6);
            
            INSERT INTO Ve (maVe, maLoaiVe, maVach, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, maToa, trangThai, tenKhachHang, soCCCD)
            SELECT @maVe, @maLoaiVe, NULL, @thoiGianLenTau, @giaVe, @maKhachHang, @maChoNgoi, @maLichTrinh, @maToa, 1, hoTen, CCCD
            FROM KhachHang WHERE maKH = @maKhachHang;
            
            -- Tạo chi tiết hóa đơn
            INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam)
            VALUES (@maHoaDon, @maVe, 1, @giaGoc, @mucGiam);
            
            SET @tongVeTao = @tongVeTao + 1;
        END
        
        SET @soHD = @soHD + 1;
        SET @j = @j + 1;
    END
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END

PRINT N'';
PRINT N'✅ Đã bổ sung vé cho tháng 12!';
PRINT N'';
PRINT N'📊 Thống kê:';
PRINT N'   💳 Tổng số vé tạo: ' + CAST(@tongVeTao AS NVARCHAR(10));
PRINT N'   📅 Thời gian: 1-22/12/2024';
PRINT N'   💰 Doanh thu ước tính: ~' + CAST(CAST(@tongVeTao * 350000 AS BIGINT)/1000000 AS NVARCHAR(10)) + N' triệu VNĐ';
PRINT N'';

-- Thống kê doanh thu theo tuyến (lấy từ HoaDon tháng 12)
PRINT N'📊 Thống kê doanh thu tháng 12/2024 (tất cả nguồn):';
SELECT 
    t.tenTuyen,
    COUNT(DISTINCT v.maVe) AS soVe,
    FORMAT(SUM(h.tongTien), 'N0') + N' VNĐ' AS doanhThu
FROM HoaDon h
JOIN Ve v ON v.maVe IN (
    SELECT maVe FROM ChiTietHoaDon WHERE maHoaDon = h.maHoaDon
)
JOIN LichTrinh lt ON lt.maLichTrinh = v.maLichTrinh
JOIN Tuyen t ON t.maTuyen = lt.maTuyen
WHERE MONTH(h.ngayTao) = 12 AND YEAR(h.ngayTao) = 2024
GROUP BY t.tenTuyen
ORDER BY SUM(h.tongTien) DESC;

PRINT N'';
PRINT N'💰 TỔNG DOANH THU THÁNG 12:';
SELECT FORMAT(SUM(tongTien), 'N0') + N' VNĐ' AS tongDoanhThu
FROM HoaDon
WHERE MONTH(ngayTao) = 12 AND YEAR(ngayTao) = 2024;

PRINT N'';
PRINT N'🎉 ========================================';
PRINT N'✅ HOÀN THÀNH BỔ SUNG VÉ (PHIÊN BẢN NHẸ)!';
PRINT N'⏱️  Thời gian chạy: ~15-20 giây';
PRINT N'📊 Bây giờ refresh Dashboard để xem kết quả!';
PRINT N'🎉 ========================================';
GO

