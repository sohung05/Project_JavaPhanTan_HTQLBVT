-- ========================================
-- BẢNG PHỤ: BẢNG GIỜ GA (Không ảnh hưởng code cũ)
-- File: 05_ThemBangGioGa.sql
-- Mô tả: Lưu thông tin giờ tàu đến từng ga trung gian
-- ========================================

USE HTQLVT;
GO

-- ========================================
-- BƯỚC 1: TẠO BẢNG BẢNG GIỜ GA
-- ========================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'BangGioGa')
BEGIN
CREATE TABLE BangGioGa (
    id INT IDENTITY(1,1) PRIMARY KEY,
    maLichTrinh NVARCHAR(20) NOT NULL,
    maGa NVARCHAR(20) NOT NULL,
        thuTuGa INT NOT NULL,
        gioDen DATETIME2(0) NOT NULL,
        -- KHÔNG dùng FK để không ảnh hưởng code cũ
        -- Nhưng vẫn có index để query nhanh
        INDEX IX_BangGioGa_LichTrinh (maLichTrinh),
        INDEX IX_BangGioGa_Ga (maGa)
    );

PRINT N'✅ Đã tạo bảng BangGioGa';
END
ELSE
BEGIN
    -- Nếu bảng đã tồn tại, xóa dữ liệu cũ
    DELETE FROM BangGioGa;
    PRINT N'⚠️  Bảng BangGioGa đã tồn tại, xóa dữ liệu cũ';
END
GO

-- ========================================
-- BƯỚC 2: THÊM DỮ LIỆU GIỜ GA CHO 6 CHUYẾN TÀU
-- ========================================
PRINT N'';
PRINT N'📥 Bắt đầu thêm dữ liệu giờ ga...';
GO

-- ========================================
-- THỨ TỰ CÁC GA (BẮC → NAM)
-- ========================================
-- 1. LC (Lào Cai)
-- 2. HN (Hà Nội)
-- 3. TH (Thanh Hóa)
-- 4. VINH (Vinh)
-- 5. DH (Đồng Hới)
-- 6. HUE (Huế)
-- 7. DN (Đà Nẵng)
-- 8. QN (Quy Nhơn)
-- 9. NT (Nha Trang)
-- 10. PT (Phan Thiết)
-- 11. SG (Sài Gòn)
-- 12. BL (Bạc Liêu)

-- ========================================
-- INSERT DỮ LIỆU CHO 30 NGÀY
-- ========================================
DECLARE @NgayBatDau DATE = CAST(GETDATE() AS DATE);
DECLARE @NgayKetThuc DATE = DATEADD(DAY, 29, @NgayBatDau);
DECLARE @NgayHienTai DATE = @NgayBatDau;
DECLARE @ngayStr NVARCHAR(6);

WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    SET @ngayStr = FORMAT(@NgayHienTai, 'ddMMyy');
    
    -- ========================================
    -- SE1: Lào Cai → Bạc Liêu (05:00 khởi hành)
    -- ========================================
    INSERT INTO BangGioGa (maLichTrinh, maGa, thuTuGa, gioDen) VALUES
    ('LTSE1-' + @ngayStr, 'LC',   1,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 05:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'HN',   2,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'TH',   3,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 09:30:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'VINH', 4,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:30:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'DH',   5,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'HUE',  6,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'DN',   7,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'QN',   8,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'NT',   9,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'PT',   10, CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'SG',   11, CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:30:00') AS DATETIME2(0))),
    ('LTSE1-' + @ngayStr, 'BL',   12, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 01:00:00') AS DATETIME2(0)));
    
    -- ========================================
    -- SE3: Lào Cai → Bạc Liêu (12:00 khởi hành)
    -- ========================================
    INSERT INTO BangGioGa (maLichTrinh, maGa, thuTuGa, gioDen) VALUES
    ('LTSE3-' + @ngayStr, 'LC',   1,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'HN',   2,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'TH',   3,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 17:00:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'VINH', 4,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 19:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'DH',   5,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'HUE',  6,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'DN',   7,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 00:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'QN',   8,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 02:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'NT',   9,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'PT',   10, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 06:30:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'SG',   11, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0))),
    ('LTSE3-' + @ngayStr, 'BL',   12, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 09:00:00') AS DATETIME2(0)));
    
    -- ========================================
    -- SE5: Lào Cai → Bạc Liêu (20:00 khởi hành)
    -- ========================================
    INSERT INTO BangGioGa (maLichTrinh, maGa, thuTuGa, gioDen) VALUES
    ('LTSE5-' + @ngayStr, 'LC',   1,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'HN',   2,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:00:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'TH',   3,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 02:00:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'VINH', 4,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'DH',   5,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 06:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'HUE',  6,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 08:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'DN',   7,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 10:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'QN',   8,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 12:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'NT',   9,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'PT',   10, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 16:00:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'SG',   11, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 17:30:00') AS DATETIME2(0))),
    ('LTSE5-' + @ngayStr, 'BL',   12, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)));
    
    -- ========================================
    -- SE2: Bạc Liêu → Lào Cai (06:00 khởi hành)
    -- ========================================
    INSERT INTO BangGioGa (maLichTrinh, maGa, thuTuGa, gioDen) VALUES
    ('LTSE2-' + @ngayStr, 'BL',   1,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'SG',   2,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:30:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'PT',   3,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 09:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'NT',   4,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'QN',   5,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'DN',   6,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'HUE',  7,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:30:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'DH',   8,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'VINH', 9,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 19:30:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'TH',   10, CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'HN',   11, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 00:00:00') AS DATETIME2(0))),
    ('LTSE2-' + @ngayStr, 'LC',   12, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 02:00:00') AS DATETIME2(0)));
    
    -- ========================================
    -- SE4: Bạc Liêu → Lào Cai (13:00 khởi hành)
    -- ========================================
    INSERT INTO BangGioGa (maLichTrinh, maGa, thuTuGa, gioDen) VALUES
    ('LTSE4-' + @ngayStr, 'BL',   1,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'SG',   2,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'PT',   3,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'NT',   4,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'QN',   5,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'DN',   6,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'HUE',  7,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 00:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'DH',   8,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 02:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'VINH', 9,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'TH',   10, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 06:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'HN',   11, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 08:30:00') AS DATETIME2(0))),
    ('LTSE4-' + @ngayStr, 'LC',   12, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 10:00:00') AS DATETIME2(0)));
    
    -- ========================================
    -- SE6: Bạc Liêu → Lào Cai (21:00 khởi hành)
    -- ========================================
    INSERT INTO BangGioGa (maLichTrinh, maGa, thuTuGa, gioDen) VALUES
    ('LTSE6-' + @ngayStr, 'BL',   1,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'SG',   2,  CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'PT',   3,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 01:00:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'NT',   4,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 03:30:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'QN',   5,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 05:30:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'DN',   6,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'HUE',  7,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 10:00:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'DH',   8,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'VINH', 9,  CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'TH',   10, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 16:30:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'HN',   11, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 18:30:00') AS DATETIME2(0))),
    ('LTSE6-' + @ngayStr, 'LC',   12, CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)));
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END
GO

PRINT N'✅ Đã thêm dữ liệu BangGioGa (30 ngày × 6 chuyến × 12 ga = 2,160 records)';

-- ========================================
-- BƯỚC 3: TẠO VIEW HỖ TRỢ QUERY (TÙY CHỌN)
-- ========================================
IF EXISTS (SELECT * FROM sys.views WHERE name = 'V_LichTrinhChiTiet')
    DROP VIEW V_LichTrinhChiTiet;
GO

CREATE VIEW V_LichTrinhChiTiet AS
SELECT 
    lt.maLichTrinh,
    lt.soHieuTau,
    bgg.maGa,
    g.tenGa,
    bgg.thuTuGa,
    bgg.gioDen,
    LEAD(bgg.gioDen) OVER (PARTITION BY bgg.maLichTrinh ORDER BY bgg.thuTuGa) AS gioKhoiHanh
FROM LichTrinh lt
INNER JOIN BangGioGa bgg ON lt.maLichTrinh = bgg.maLichTrinh
INNER JOIN Ga g ON bgg.maGa = g.maGa;
GO

PRINT N'✅ Đã tạo view V_LichTrinhChiTiet';

-- ========================================
-- HOÀN THÀNH
-- ========================================
PRINT N'';
PRINT N'🎉 ========================================';
PRINT N'✅ HOÀN THÀNH TẠO BẢNG GIỜ GA!';
PRINT N'';
PRINT N'📊 Tóm tắt:';
PRINT N'   ✔️ Bảng BangGioGa: 2,160 records (30 ngày × 6 chuyến × 12 ga)';
PRINT N'   ✔️ View V_LichTrinhChiTiet: Hỗ trợ query dễ dàng';
PRINT N'   ✔️ KHÔNG ảnh hưởng code cũ (không FK, không sửa bảng gốc)';
PRINT N'';
PRINT N'📌 Cách dùng:';
PRINT N'   1. Code cũ vẫn chạy bình thường (không cần sửa gì)';
PRINT N'   2. Muốn tìm vé "Hà Nội → Đà Nẵng":';
PRINT N'      - Query BangGioGa tìm lịch trình có cả HN và DN';
PRINT N'      - Kiểm tra thuTuGa: HN < DN';
PRINT N'      - Lấy giờ đến từ BangGioGa';
PRINT N'   3. Demo cho giáo viên: Giải thích logic "tàu chạy xuyên tuyến"';
PRINT N'';
PRINT N'🎉 ========================================';
GO

-- ========================================
-- VÍ DỤ QUERY ĐỂ TEST
-- ========================================
PRINT N'';
PRINT N'🔍 VÍ DỤ QUERY:';
PRINT N'';
GO

-- Ví dụ 1: Xem giờ tàu SE1 ngày hôm nay đến từng ga
PRINT N'📌 Ví dụ 1: Xem giờ tàu SE1 ngày hôm nay:';
DECLARE @today NVARCHAR(6) = FORMAT(CAST(GETDATE() AS DATE), 'ddMMyy');
DECLARE @maLT NVARCHAR(50) = 'LTSE1-' + @today;

SELECT 
    bgg.maLichTrinh,
    bgg.thuTuGa,
    g.tenGa,
    bgg.gioDen AS N'Giờ đến'
FROM BangGioGa bgg
INNER JOIN Ga g ON bgg.maGa = g.maGa
WHERE bgg.maLichTrinh = @maLT
ORDER BY bgg.thuTuGa;
GO

PRINT N'';
PRINT N'📌 Ví dụ 2: Tìm chuyến tàu đi từ Hà Nội → Đà Nẵng ngày hôm nay:';
GO

-- Ví dụ 2: Tìm chuyến Hà Nội → Đà Nẵng
DECLARE @today NVARCHAR(6) = FORMAT(CAST(GETDATE() AS DATE), 'ddMMyy');

SELECT DISTINCT
    lt.maLichTrinh,
    lt.soHieuTau,
    hn.gioDen AS N'Giờ lên tàu (HN)',
    dn.gioDen AS N'Giờ xuống tàu (DN)',
    DATEDIFF(MINUTE, hn.gioDen, dn.gioDen) / 60.0 AS N'Thời gian (giờ)'
FROM LichTrinh lt
INNER JOIN BangGioGa hn ON lt.maLichTrinh = hn.maLichTrinh AND hn.maGa = 'HN'
INNER JOIN BangGioGa dn ON lt.maLichTrinh = dn.maLichTrinh AND dn.maGa = 'DN'
WHERE hn.thuTuGa < dn.thuTuGa  -- Đảm bảo HN trước DN
  AND lt.maLichTrinh LIKE '%' + @today + '%'
ORDER BY hn.gioDen;
GO

PRINT N'';
PRINT N'✅ Script hoàn tất! Kiểm tra kết quả phía trên.';
GO
