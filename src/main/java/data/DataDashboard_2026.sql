-- ========================================
-- DỮ LIỆU DEMO DASHBOARD 2026
-- File: DataDashboard_2026.sql
-- Mục tiêu: Tạo xu hướng doanh thu và số vé tăng dần từ Tháng 1 đến Tháng 5
-- ========================================

USE HTQLVT;
GO

PRINT N'📊 Đang làm sạch dữ liệu cũ và nạp dữ liệu xu hướng Dashboard 2026...';

-- Xóa dữ liệu demo cũ để tránh trùng khóa chính (PK)
DELETE FROM ChiTietHoaDon WHERE maHoaDon LIKE 'DBHD%';
DELETE FROM Ve WHERE maVe LIKE 'DBV%';
DELETE FROM HoaDon WHERE maHoaDon LIKE 'DBHD%';
DELETE FROM Ve WHERE maVe LIKE 'HD_DEMO%';
DELETE FROM HoaDon WHERE maHoaDon LIKE 'HD_DEMO%';

-- Biến cấu hình
DECLARE @NhanVien NVARCHAR(20) = 'NV002'; -- Nhân viên bán vé
DECLARE @KhachHang NVARCHAR(20) = 'KH001'; -- Khách hàng mặc định
DECLARE @LichTrinh NVARCHAR(20) = 'LT1-100526'; -- Lịch trình mẫu
DECLARE @LoaiVe NVARCHAR(20) = 'LV01'; -- Vé người lớn
DECLARE @MaGhi NVARCHAR(30) = 'TSE1-01-01'; -- Chỗ ngồi mẫu (chỉ dùng để link, không quan trọng sơ đồ)

DECLARE @Thang INT = 1;
DECLARE @SoHoaDonMoiThang INT;
DECLARE @GiaVeMoiThang DECIMAL(18,2);
DECLARE @NgayTao DATETIME;
DECLARE @GioTao DATETIME; -- Đã thêm khai báo biến này
DECLARE @ID_Count INT = 1000;

-- Vòng lặp tạo dữ liệu từ Tháng 1 đến Tháng 4 (Tháng 5 đã có dữ liệu mẫu nhưng sẽ thêm một ít)
WHILE @Thang <= 5
BEGIN
    -- Thiết lập xu hướng: Mỗi tháng tăng khoảng 20-30% số lượng
    SET @SoHoaDonMoiThang = CASE 
        WHEN @Thang = 1 THEN 80  -- Tháng 1: Thấp
        WHEN @Thang = 2 THEN 120 -- Tháng 2: Tăng nhẹ (Tết)
        WHEN @Thang = 3 THEN 100 -- Tháng 3: Giảm nhẹ
        WHEN @Thang = 4 THEN 180 -- Tháng 4: Tăng mạnh (Lễ 30/4)
        WHEN @Thang = 5 THEN 250 -- Tháng 5: Đỉnh điểm
    END;

    SET @GiaVeMoiThang = 800000; -- Giá trung bình

    PRINT N'   > Đang tạo ' + CAST(@SoHoaDonMoiThang AS NVARCHAR) + N' đơn hàng cho Tháng ' + CAST(@Thang AS NVARCHAR);

    DECLARE @i INT = 1;
    WHILE @i <= @SoHoaDonMoiThang
    BEGIN
        -- Tạo Ngày và Giờ (Dùng 00:00:01 cho các đơn trong tháng hiện tại để không đè đơn thật của USER)
        SET @NgayTao = DATETIMEFROMPARTS(2026, @Thang, (@i % 28) + 1, 0, 0, 1, 0);
        SET @GioTao = @NgayTao;
        
        DECLARE @MaHD NVARCHAR(20) = 'DBHD' + FORMAT(@Thang, '00') + FORMAT(@i, '000');
        DECLARE @MaVe NVARCHAR(30) = 'DBV' + FORMAT(@Thang, '00') + FORMAT(@i, '000');

        -- 1. Chèn Hóa Đơn
        INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, ngayTao, gioTao, tongTien, trangThai)
        VALUES (@MaHD, @NhanVien, @KhachHang, @NgayTao, @GioTao, @GiaVeMoiThang, 1);

        -- 2. Chèn Vé
        INSERT INTO Ve (maVe, maLoaiVe, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, trangThai, maGaDi, maGaDen, tenKhachHang, soCCCD)
        VALUES (@MaVe, @LoaiVe, @NgayTao, @GiaVeMoiThang, @KhachHang, @MaGhi, @LichTrinh, 1, 'HN', 'SG', N'Khách Demo ' + CAST(@i AS NVARCHAR), '123456789');

        -- 3. Chèn Chi Tiết Hóa Đơn
        INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam)
        VALUES (@MaHD, @MaVe, 1, @GiaVeMoiThang, 0);

        SET @i = @i + 1;
    END

    SET @Thang = @Thang + 1;
END

PRINT N'✅ Đã nạp xong dữ liệu xu hướng Dashboard 2026!';
GO
