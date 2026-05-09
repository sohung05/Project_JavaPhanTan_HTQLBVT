-- ======================================================
-- DỮ LIỆU NGÀY 09/05/2026
-- ======================================================

PRINT N'📊 Đang tạo dữ liệu ngày 09/05/2026...';

DECLARE @NgayInput DATE = '2026-05-09';
DECLARE @ms NVARCHAR(10) = FORMAT(@NgayInput, 'ddMMyy');

DECLARE @tau_num INT = 1;

WHILE @tau_num <= 10
BEGIN

    DECLARE @so_hieu_tau NVARCHAR(10) = 'SE' + CAST(@tau_num AS NVARCHAR);

    DECLARE @ma_lt NVARCHAR(50) =
        'LT' + CAST(@tau_num AS NVARCHAR) + '-' + @ms;

    DECLARE @so_ve_ban INT =
        50 + (ABS(CHECKSUM(NEWID())) % 100);

    DECLARE @ma_hd NVARCHAR(50) =
        'HD_DEMO_' + @so_hieu_tau + '_' + @ms;

INSERT INTO HoaDon
(
    maHoaDon,
    maNhanVien,
    maKH,
    ngayTao,
    gioTao,
    tongTien,
    trangThai
)
VALUES
    (
        @ma_hd,
        'NV002',
        'KH001',
        @NgayInput,
        CAST(@NgayInput AS DATETIME) + ' 08:00',
        0,
        1
    );

DECLARE @v INT = 1;
    DECLARE @tong_tien_hd MONEY = 0;

    WHILE @v <= @so_ve_ban
BEGIN

        DECLARE @toa_idx INT =
            1 + (ABS(CHECKSUM(NEWID())) % 10);

        DECLARE @ghe_idx INT =
            1 + (ABS(CHECKSUM(NEWID())) % 40);

        DECLARE @ma_toa NVARCHAR(20) =
            'T' + @so_hieu_tau + '-' + FORMAT(@toa_idx, '00');

        DECLARE @ma_cho NVARCHAR(30) =
            @ma_toa + '-' + FORMAT(@ghe_idx, '00');

        DECLARE @ga_random INT =
            ABS(CHECKSUM(NEWID())) % 4;

        DECLARE @gDi NVARCHAR(10);
        DECLARE @gDen NVARCHAR(10);
        DECLARE @khoangCach INT;

        IF @tau_num % 2 <> 0
BEGIN
            SET @gDi =
                CASE
                    WHEN @ga_random = 0 THEN 'HN'
                    WHEN @ga_random = 1 THEN 'ND'
                    WHEN @ga_random = 2 THEN 'V'
                    ELSE 'HUE'
END;

            SET @gDen =
                CASE
                    WHEN @ga_random = 0 THEN 'SG'
                    WHEN @ga_random = 1 THEN 'NT'
                    WHEN @ga_random = 2 THEN 'DN'
                    ELSE 'QN'
END;

            SET @khoangCach =
                CASE
                    WHEN @gDi = 'HN' AND @gDen = 'SG' THEN 1726
                    WHEN @gDi = 'HN' AND @gDen = 'DN' THEN 791
                    WHEN @gDi = 'V' AND @gDen = 'SG' THEN 1407
                    ELSE 500
END;
END
ELSE
BEGIN
            SET @gDi =
                CASE
                    WHEN @ga_random = 0 THEN 'SG'
                    WHEN @ga_random = 1 THEN 'NT'
                    WHEN @ga_random = 2 THEN 'DN'
                    ELSE 'HUE'
END;

            SET @gDen =
                CASE
                    WHEN @ga_random = 0 THEN 'HN'
                    WHEN @ga_random = 1 THEN 'V'
                    WHEN @ga_random = 2 THEN 'ND'
                    ELSE 'HN'
END;

            SET @khoangCach =
                CASE
                    WHEN @gDi = 'SG' AND @gDen = 'HN' THEN 1726
                    WHEN @gDi = 'DN' AND @gDen = 'HN' THEN 791
                    WHEN @gDi = 'SG' AND @gDen = 'V' THEN 1407
                    ELSE 600
END;
END

        DECLARE @gia_thuc MONEY =
            @khoangCach *
            (CASE
                WHEN @toa_idx <= 5 THEN 800
                ELSE 1200
             END);

        DECLARE @ma_ve_new NVARCHAR(50) =
            CONCAT(
                'V_DEMO_',
                @so_hieu_tau,
                '_',
                @ms,
                '_',
                FORMAT(@v, '000')
            );

INSERT INTO Ve
(
    maVe,
    maLoaiVe,
    thoiGianLenTau,
    giaVe,
    maKH,
    maChoNgoi,
    maLichTrinh,
    trangThai,
    maGaDi,
    maGaDen,
    tenKhachHang,
    soCCCD
)
VALUES
    (
        @ma_ve_new,
        'LV01',
        CAST(@NgayInput AS DATETIME) + ' 08:00',
        @gia_thuc,
        'KH001',
        @ma_cho,
        @ma_lt,
        1,
        @gDi,
        @gDen,
        N'Khách Demo',
        '123456789'
    );

INSERT INTO ChiTietHoaDon
(
    maHoaDon,
    maVe,
    soLuong,
    giaVe,
    mucGiam
)
VALUES
    (
        @ma_hd,
        @ma_ve_new,
        1,
        @gia_thuc,
        0
    );

SET @tong_tien_hd =
            @tong_tien_hd + @gia_thuc;

        SET @v = @v + 1;

END

UPDATE HoaDon
SET tongTien = @tong_tien_hd
WHERE maHoaDon = @ma_hd;

SET @tau_num = @tau_num + 1;

END
GO
