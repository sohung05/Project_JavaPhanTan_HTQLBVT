-- ========================================
-- KIỂM TRA VÀ BỔ SUNG KHUYẾN MÃI
-- File: 12_KiemTraKhuyenMai.sql
-- Mô tả: Kiểm tra và thêm dữ liệu khuyến mãi nếu thiếu
-- ========================================

USE HTQLVT;
GO

PRINT N'🔍 Kiểm tra dữ liệu Khuyến mãi...';
PRINT N'';

-- Kiểm tra có khuyến mãi không
DECLARE @soKM INT = (SELECT COUNT(*) FROM KhuyenMai);

IF @soKM = 0
BEGIN
    PRINT N'⚠️  Không có dữ liệu Khuyến mãi! Đang thêm...';
    PRINT N'';
    
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
    
    PRINT N'   ✅ Đã thêm 3 Khuyến mãi Đối tượng (KMKH)';
    
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
    
    PRINT N'   ✅ Đã thêm 4 Khuyến mãi Hóa đơn (KMHD)';
    PRINT N'   ✅ Đã thêm 7 Chi tiết khuyến mãi';
    PRINT N'';
    PRINT N'✅ ĐÃ HOÀN THÀNH THÊM KHUYẾN MÃI!';
END
ELSE
BEGIN
    PRINT N'✅ Đã có ' + CAST(@soKM AS NVARCHAR(10)) + N' khuyến mãi trong database';
    PRINT N'';
    PRINT N'📋 Danh sách khuyến mãi:';
    SELECT 
        maKhuyenMai,
        tenKhuyenMai,
        loaiKhuyenMai,
        FORMAT(thoiGianBatDau, 'dd/MM/yyyy') AS batDau,
        FORMAT(thoiGianKetThuc, 'dd/MM/yyyy') AS ketThuc,
        CASE WHEN trangThai = 1 THEN N'Hoạt động' ELSE N'Tạm ngưng' END AS trangThai
    FROM KhuyenMai
    ORDER BY maKhuyenMai;
    
    PRINT N'';
    PRINT N'📋 Chi tiết khuyến mãi:';
    SELECT 
        km.maKhuyenMai,
        km.tenKhuyenMai,
        ct.dieuKien,
        CAST(ct.chietKhau * 100 AS INT) AS phanTramGiam
    FROM KhuyenMai km
    JOIN ChiTietKhuyenMai ct ON ct.maKhuyenMai = km.maKhuyenMai
    WHERE ct.maHoaDon IS NULL
    ORDER BY km.maKhuyenMai;
END

PRINT N'';
PRINT N'🎉 ========================================';
PRINT N'✅ HOÀN THÀNH KIỂM TRA KHUYẾN MÃI!';
PRINT N'🎉 ========================================';
GO


