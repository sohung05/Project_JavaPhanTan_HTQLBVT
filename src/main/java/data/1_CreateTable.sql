-- ========================================
-- HỆ THỐNG QUẢN LÝ VÉ TÀU - TẠO BẢNG (UPGRADED)
-- File: 01_CreateTables.sql
-- Mô tả: Cấu trúc database hỗ trợ quản lý chặng (Segments)
-- ========================================

USE master;
GO
IF DB_ID('HTQLVT') IS NOT NULL
BEGIN
  ALTER DATABASE HTQLVT SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
  DROP DATABASE HTQLVT;
END
GO

CREATE DATABASE HTQLVT;
GO
USE HTQLVT;
GO

/* ============================================
   📚 BẢNG TRA CỨU (LOOKUP)
============================================ */
CREATE TABLE LoaiTau(
  maLoaiTau  NVARCHAR(20) PRIMARY KEY,
  tenLoaiTau NVARCHAR(100) NOT NULL
);

CREATE TABLE LoaiToa(
  maLoaiToa  NVARCHAR(20) PRIMARY KEY,
  tenLoaiToa NVARCHAR(100) NOT NULL
);

CREATE TABLE LoaiVe(
  maLoaiVe   NVARCHAR(20) PRIMARY KEY,
  tenLoaiVe  NVARCHAR(100) NOT NULL,
  mucGiamGia DECIMAL(5,2)  NOT NULL DEFAULT 0
);

CREATE TABLE Tuyen(
  maTuyen   NVARCHAR(20) PRIMARY KEY,
  tenTuyen  NVARCHAR(200) NOT NULL,
  doDai     DECIMAL(18,2) NULL
);

CREATE TABLE Ga(
  maGa   NVARCHAR(20) PRIMARY KEY,
  tenGa  NVARCHAR(200) NOT NULL,
  viTri  NVARCHAR(200) NULL
);

-- 🆕 Bảng lộ trình chi tiết (Thứ tự các ga trong tuyến)
CREATE TABLE BangGioGa (
    maTuyen NVARCHAR(20) NOT NULL,
    maGa NVARCHAR(20) NOT NULL,
    stt INT NOT NULL, -- Số thứ tự ga trong tuyến (1, 2, 3...)
    khoangCachTuGaTruoc DECIMAL(18,2) DEFAULT 0,
    PRIMARY KEY (maTuyen, maGa),
    FOREIGN KEY (maTuyen) REFERENCES Tuyen(maTuyen),
    FOREIGN KEY (maGa) REFERENCES Ga(maGa)
);

/* ============================================
   🚆 TÀU / TOA / CHỖ NGỒI
============================================ */
CREATE TABLE ChuyenTau(
  soHieuTau  NVARCHAR(20) PRIMARY KEY,
  tocDo      DECIMAL(10,2) NULL,
  maLoaiTau  NVARCHAR(20)  NOT NULL,
  namSanXuat INT NULL,
  FOREIGN KEY (maLoaiTau) REFERENCES LoaiTau(maLoaiTau)
);

CREATE TABLE Toa(
  maToa     NVARCHAR(20) PRIMARY KEY,
  soHieuTau NVARCHAR(20) NOT NULL,
  soToa     INT          NOT NULL,
  maLoaiToa NVARCHAR(20) NOT NULL,
  FOREIGN KEY (soHieuTau) REFERENCES ChuyenTau(soHieuTau),
  FOREIGN KEY (maLoaiToa) REFERENCES LoaiToa(maLoaiToa)
);

CREATE TABLE ChoNgoi(
  maChoNgoi NVARCHAR(30) PRIMARY KEY,
  maToa     NVARCHAR(20) NOT NULL,
  moTa      NVARCHAR(200) NULL,
  viTri     INT NULL,
  gia       DECIMAL(18,2) NULL,
  FOREIGN KEY (maToa) REFERENCES Toa(maToa)
);

/* ============================================
   🕒 LỊCH TRÌNH (Chuyến chạy thực tế)
============================================ */
CREATE TABLE LichTrinh(
  maLichTrinh  NVARCHAR(20) PRIMARY KEY,
  soHieuTau    NVARCHAR(20) NOT NULL,
  maTuyen      NVARCHAR(20) NOT NULL,
  maGaDi       NVARCHAR(20) NOT NULL, -- Ga xuất phát của cả chuyến
  maGaDen      NVARCHAR(20) NOT NULL, -- Ga kết thúc của cả chuyến
  gioKhoiHanh  DATETIME2(0) NOT NULL,
  gioDenDuKien DATETIME2(0) NULL,
  trangThai    BIT NOT NULL DEFAULT 1,
  FOREIGN KEY (soHieuTau) REFERENCES ChuyenTau(soHieuTau),
  FOREIGN KEY (maTuyen)   REFERENCES Tuyen(maTuyen),
  FOREIGN KEY (maGaDi)    REFERENCES Ga(maGa),
  FOREIGN KEY (maGaDen)   REFERENCES Ga(maGa)
);

/* ============================================
   👩💼 NHÂN VIÊN / TÀI KHOẢN / KHÁCH HÀNG
============================================ */
CREATE TABLE NhanVien(
  maNhanVien NVARCHAR(20) PRIMARY KEY,
  CCCD NVARCHAR(20) NULL,
  hoTen NVARCHAR(150) NOT NULL,
  SDT NVARCHAR(20) NULL,
  email NVARCHAR(150) NULL,
  diaChi NVARCHAR(250) NULL,
  chucVu INT NULL,
  trangThai BIT NOT NULL DEFAULT 1,
  ngaySinh DATE NULL,
  ngayVaoLam DATE NULL,
  gioiTinh NVARCHAR(10) NULL
);

CREATE TABLE TaiKhoan(
  userName NVARCHAR(50) PRIMARY KEY,
  passWord NVARCHAR(200) NOT NULL,
  maNhanVien NVARCHAR(20) NOT NULL UNIQUE,
  FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

CREATE TABLE KhachHang(
  maKH NVARCHAR(20) PRIMARY KEY,
  CCCD NVARCHAR(20) NULL,
  hoTen NVARCHAR(150) NOT NULL,
  email NVARCHAR(150) NULL,
  SDT NVARCHAR(20) NULL,
  doiTuong NVARCHAR(30) NULL
);

/* ============================================
   💵 HÓA ĐƠN / VÉ / KHUYẾN MÃI
============================================ */
CREATE TABLE HoaDon(
  maHoaDon NVARCHAR(20) PRIMARY KEY,
  maNhanVien NVARCHAR(20) NOT NULL,
  maKH NVARCHAR(20) NOT NULL,
  gioTao DATETIME2(0) NOT NULL,
  ngayTao DATETIME2(0) NULL,
  tongTien DECIMAL(18,2) NOT NULL DEFAULT 0,
  trangThai BIT NOT NULL DEFAULT 1,
  FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
  FOREIGN KEY (maKH)       REFERENCES KhachHang(maKH)
);

CREATE TABLE KhuyenMai(
  maKhuyenMai NVARCHAR(20) PRIMARY KEY,
  tenKhuyenMai NVARCHAR(150) NOT NULL,
  loaiKhuyenMai NVARCHAR(50) NULL,
  thoiGianBatDau DATETIME2(0) NOT NULL,
  thoiGianKetThuc DATETIME2(0) NOT NULL,
  trangThai BIT NOT NULL DEFAULT 1
);

CREATE TABLE Ve(
  maVe NVARCHAR(30) PRIMARY KEY,
  maLoaiVe NVARCHAR(20) NOT NULL,
  maVach NVARCHAR(50) NULL,
  thoiGianLenTau DATETIME2(0) NOT NULL,
  giaVe DECIMAL(18,2) NOT NULL,
  maKH NVARCHAR(20) NULL,
  maChoNgoi NVARCHAR(30) NULL,
  maLichTrinh NVARCHAR(20) NULL,
  maToa NVARCHAR(20) NULL,
  trangThai BIT NOT NULL DEFAULT 1,
  tenKhachHang NVARCHAR(150) NULL,
  soCCCD NVARCHAR(20) NULL,
  -- 🆕 Cột mới hỗ trợ quản lý chặng
  maGaDi NVARCHAR(20) NULL,
  maGaDen NVARCHAR(20) NULL,
  FOREIGN KEY (maLoaiVe)   REFERENCES LoaiVe(maLoaiVe),
  FOREIGN KEY (maKH)       REFERENCES KhachHang(maKH),
  FOREIGN KEY (maChoNgoi)  REFERENCES ChoNgoi(maChoNgoi),
  FOREIGN KEY (maLichTrinh) REFERENCES LichTrinh(maLichTrinh),
  FOREIGN KEY (maToa)      REFERENCES Toa(maToa),
  FOREIGN KEY (maGaDi)     REFERENCES Ga(maGa),
  FOREIGN KEY (maGaDen)    REFERENCES Ga(maGa)
);

/* ============================================
   📄 CHI TIẾT HÓA ĐƠN & KHUYẾN MÃI
============================================ */
CREATE TABLE ChiTietHoaDon(
  maHoaDon NVARCHAR(20) NOT NULL,
  maVe     NVARCHAR(30) NOT NULL,
  soLuong  INT NOT NULL DEFAULT 1,
  giaVe    DECIMAL(18,2) NOT NULL,
  mucGiam  DECIMAL(18,2) NOT NULL DEFAULT 0,
  CONSTRAINT PK_CTHD PRIMARY KEY (maHoaDon, maVe),
  FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
  FOREIGN KEY (maVe)     REFERENCES Ve(maVe)
);

CREATE TABLE ChiTietKhuyenMai(
  maChiTiet   INT IDENTITY(1,1) PRIMARY KEY,
  maKhuyenMai NVARCHAR(20) NOT NULL,
  maHoaDon    NVARCHAR(20) NULL,
  dieuKien    NVARCHAR(200) NULL,
  chietKhau   DECIMAL(18,2) NOT NULL DEFAULT 0,
  FOREIGN KEY (maHoaDon)    REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
  FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
);

/* ============================================
   🖨️ LỊCH SỬ IN VÉ (AUDIT LOG)
============================================ */
CREATE TABLE LichSuInVe(
  maLichSu    INT IDENTITY(1,1) PRIMARY KEY,
  maVe        NVARCHAR(30) NOT NULL,
  maNhanVien  NVARCHAR(20) NOT NULL,
  thoiGianIn  DATETIME2(0) NOT NULL DEFAULT GETDATE(),
  loaiIn      NVARCHAR(50) NULL, -- 'In mới', 'In lại'
  ghiChu      NVARCHAR(250) NULL,
  FOREIGN KEY (maVe)       REFERENCES Ve(maVe),
  FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

/* ============================================
   📦 ĐƠN TREO TẠM
============================================ */
CREATE TABLE DonTreoDat(
  maDonTreo     NVARCHAR(20) PRIMARY KEY,
  cccdNguoiDat  NVARCHAR(20) NULL,
  hoTenNguoiDat NVARCHAR(100) NULL,
  sdtNguoiDat   NVARCHAR(15) NULL,
  emailNguoiDat NVARCHAR(100) NULL,
  ngayLap       DATETIME2(0) NULL,
  gioLap        DATETIME2(0) NULL,
  soLuongVe     INT DEFAULT 0,
  tongTien      DECIMAL(18,2) DEFAULT 0,
  ghiChu        NVARCHAR(255) NULL,
  gaDi          NVARCHAR(100) NULL,
  gaDen         NVARCHAR(100) NULL,
  ngayDi        NVARCHAR(50) NULL,
  maLichTrinh   NVARCHAR(20) NULL,
  FOREIGN KEY (maLichTrinh) REFERENCES LichTrinh(maLichTrinh)
);

CREATE TABLE ThongTinVeTam(
  id            BIGINT IDENTITY(1,1) PRIMARY KEY,
  maDonTreo     NVARCHAR(20) NOT NULL,
  soGiayTo      NVARCHAR(20) NULL,
  hoTen         NVARCHAR(100) NULL,
  doiTuong      NVARCHAR(50) NULL,
  thongTinCho   NVARCHAR(255) NULL,
  giaVe         DECIMAL(18,2) DEFAULT 0,
  giamGia       DECIMAL(18,2) DEFAULT 0,
  thanhTien     DECIMAL(18,2) DEFAULT 0,
  maChoNgoi     NVARCHAR(30) NULL,
  maLichTrinh   NVARCHAR(20) NULL,
  -- 🆕 Cột mới hỗ trợ quản lý chặng
  maGaDi NVARCHAR(20) NULL,
  maGaDen NVARCHAR(20) NULL,
  FOREIGN KEY (maDonTreo)   REFERENCES DonTreoDat(maDonTreo) ON DELETE CASCADE,
  FOREIGN KEY (maChoNgoi)   REFERENCES ChoNgoi(maChoNgoi),
  FOREIGN KEY (maLichTrinh) REFERENCES LichTrinh(maLichTrinh),
  FOREIGN KEY (maGaDi)     REFERENCES Ga(maGa),
  FOREIGN KEY (maGaDen)    REFERENCES Ga(maGa)
);

/* ============================================
   ⚙️ INDEX TỐI ƯU TRUY VẤN
============================================ */
CREATE INDEX IX_Toa_Tau        ON Toa(soHieuTau);
CREATE INDEX IX_ChoNgoi_Toa    ON ChoNgoi(maToa);
CREATE INDEX IX_LT_TuyenTau    ON LichTrinh(maTuyen, soHieuTau);
CREATE INDEX IX_LT_GaDi        ON LichTrinh(maGaDi);
CREATE INDEX IX_LT_GaDen       ON LichTrinh(maGaDen);
CREATE INDEX IX_Ve_LichTrinh   ON Ve(maLichTrinh);
CREATE INDEX IX_Ve_Segments    ON Ve(maLichTrinh, maGaDi, maGaDen);
CREATE INDEX IX_BGG_Tuyen      ON BangGioGa(maTuyen, stt);
GO

PRINT N'✅ Đã nâng cấp database HTQLVT thành công!';
GO