
-- Create database if not exists

CREATE DATABASE QuanLyPhongGym;
GO

USE QuanLyPhongGym;
GO

-- Drop dependent objects first (respect foreign keys)
IF OBJECT_ID(N'dbo.LichTapPT', N'U') IS NOT NULL DROP TABLE dbo.LichTapPT;
IF OBJECT_ID(N'dbo.ChamCong', N'U') IS NOT NULL DROP TABLE dbo.ChamCong;
IF OBJECT_ID(N'dbo.ThuPhi', N'U') IS NOT NULL DROP TABLE dbo.ThuPhi;
IF OBJECT_ID(N'dbo.HoiVien', N'U') IS NOT NULL DROP TABLE dbo.HoiVien;
IF OBJECT_ID(N'dbo.HuanLuyenVien', N'U') IS NOT NULL DROP TABLE dbo.HuanLuyenVien;
IF OBJECT_ID(N'dbo.GoiTap', N'U') IS NOT NULL DROP TABLE dbo.GoiTap;
IF OBJECT_ID(N'dbo.ThietBi', N'U') IS NOT NULL DROP TABLE dbo.ThietBi;
GO

-- Tables
CREATE TABLE GoiTap (
    MaGoiTap NVARCHAR(10) PRIMARY KEY,
    TenGoiTap NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(255),
    GiaTien DECIMAL(18,2) NOT NULL,
    ThoiHan INT NOT NULL,
    SoBuoi INT NOT NULL,
    TrangThai BIT DEFAULT 1
);

CREATE TABLE HuanLuyenVien (
    MaHLV NVARCHAR(10) PRIMARY KEY,
    TenHLV NVARCHAR(100) NOT NULL,
    GioiTinh NVARCHAR(10),
    NgaySinh DATE,
    SDT NVARCHAR(15),
    DiaChi NVARCHAR(255),
    Email NVARCHAR(100),
    ChuyenMon NVARCHAR(100),
    Luong DECIMAL(18,2),
    NgayVaoLam DATE,
    TrangThai BIT DEFAULT 1
);

CREATE TABLE HoiVien (
    MaHoiVien NVARCHAR(10) PRIMARY KEY,
    TenHoiVien NVARCHAR(100) NOT NULL,
    GioiTinh NVARCHAR(10),
    NgaySinh DATE,
    SDT NVARCHAR(15),
    DiaChi NVARCHAR(255),
    Email NVARCHAR(100),
    NgayDangKy DATE DEFAULT GETDATE(),
    MaGoiTap NVARCHAR(10),
    NgayBatDau DATE,
    NgayKetThuc DATE,
    MaHLV NVARCHAR(10),
    TrangThai BIT DEFAULT 1,
    SoBuoiConLai INT DEFAULT 0,
    FOREIGN KEY (MaGoiTap) REFERENCES GoiTap(MaGoiTap),
    FOREIGN KEY (MaHLV) REFERENCES HuanLuyenVien(MaHLV)
);

CREATE TABLE ThietBi (
    MaThietBi NVARCHAR(10) PRIMARY KEY,
    TenThietBi NVARCHAR(100) NOT NULL,
    LoaiThietBi NVARCHAR(50),
    TinhTrang NVARCHAR(50),
    NgayMua DATE,
    GiaMua DECIMAL(18,2),
    NgayBaoTriCuoi DATE,
    NgayBaoTriTiep DATE,
    GhiChu NVARCHAR(255)
);

CREATE TABLE ChamCong (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoiVien NVARCHAR(10),
    NgayTap DATE,
    GioVao TIME,
    GioRa TIME,
    GhiChu NVARCHAR(255),
    FOREIGN KEY (MaHoiVien) REFERENCES HoiVien(MaHoiVien)
);

CREATE TABLE ThuPhi (
    MaPhieu NVARCHAR(10) PRIMARY KEY,
    MaHoiVien NVARCHAR(10),
    LoaiPhi NVARCHAR(50),
    SoTien DECIMAL(18,2),
    NgayThu DATE DEFAULT GETDATE(),
    PhuongThucTT NVARCHAR(50),
    NguoiThu NVARCHAR(100),
    GhiChu NVARCHAR(255),
    FOREIGN KEY (MaHoiVien) REFERENCES HoiVien(MaHoiVien)
);

CREATE TABLE LichTapPT (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoiVien NVARCHAR(10),
    MaHLV NVARCHAR(10),
    NgayTap DATE,
    GioBatDau TIME,
    GioKetThuc TIME,
    TrangThai NVARCHAR(20),
    FOREIGN KEY (MaHoiVien) REFERENCES HoiVien(MaHoiVien),
    FOREIGN KEY (MaHLV) REFERENCES HuanLuyenVien(MaHLV)
);
GO

-- Seed data
INSERT INTO GoiTap VALUES 
('GT001', N'Gói 1 tháng', N'Gói tập 1 tháng không giới hạn', 500000, 1, 30, 1),
('GT002', N'Gói 3 tháng', N'Gói tập 3 tháng không giới hạn', 1350000, 3, 90, 1),
('GT003', N'Gói 6 tháng', N'Gói tập 6 tháng không giới hạn', 2500000, 6, 180, 1),
('GT004', N'Gói VIP 12 tháng', N'Gói VIP 12 tháng + PT miễn phí', 4500000, 12, 365, 1);

INSERT INTO HuanLuyenVien VALUES 
('HLV001', N'Nguyễn Văn A', N'Nam', '1990-05-15', '0901234567', N'Quận 1, TP.HCM', 'hlv1@gym.com', N'Thể hình', 15000000, '2020-01-15', 1),
('HLV002', N'Trần Thị B', N'Nữ', '1992-08-20', '0907654321', N'Quận 3, TP.HCM', 'hlv2@gym.com', N'Yoga, Cardio', 12000000, '2021-03-10', 1);

INSERT INTO HoiVien VALUES 
('HV001', N'Lê Văn C', N'Nam', '1995-10-10', '0909123456', N'Quận 2, TP.HCM', 'hv1@email.com', '2024-01-15', 'GT002', '2024-01-15', '2024-04-15', 'HLV001', 1, 85),
('HV002', N'Phạm Thị D', N'Nữ', '1998-03-25', '0908765432', N'Quận 7, TP.HCM', 'hv2@email.com', '2024-02-01', 'GT001', '2024-02-01', '2024-03-01', 'HLV002', 0, 0);

INSERT INTO ThietBi VALUES 
('TB001', N'máy chạy bộ Technogym', N'Cardio', N'Tốt', '2023-01-15', 50000000, '2024-01-15', '2024-07-15', N''),
('TB002', N'Tạ đòn Olympic', N'Tạ tự do', N'Tốt', '2023-02-20', 5000000, '2024-02-20', '2024-08-20', N''),
('TB003', N'máy cáp kéo', N'Máy tập', N'Bảo trì', '2022-05-10', 30000000, '2024-01-10', '2024-07-10', N'Đang sửa chữa');

INSERT INTO ThuPhi VALUES 
('TP001', 'HV001', N'Gói tập', 1350000, '2024-01-15', N'Tiền mặt', N'Admin', N'Thu phí gói 3 tháng'),
('TP002', 'HV002', N'Gói tập', 500000, '2024-02-01', N'Chuyển khoản', N'Admin', N'Thu phí gói 1 tháng');
GO

-- Stored procedures
IF OBJECT_ID(N'dbo.sp_ThongKeHoiVien', N'P') IS NOT NULL DROP PROCEDURE dbo.sp_ThongKeHoiVien;
GO
CREATE PROCEDURE sp_ThongKeHoiVien
AS
BEGIN
    SELECT 
        COUNT(*) as TongHoiVien,
        SUM(CASE WHEN TrangThai = 1 THEN 1 ELSE 0 END) as HoiVienHoatDong,
        SUM(CASE WHEN TrangThai = 0 THEN 1 ELSE 0 END) as HoiVienHetHan
    FROM HoiVien;
END
GO

IF OBJECT_ID(N'dbo.sp_DoanhThuTheoThang', N'P') IS NOT NULL DROP PROCEDURE dbo.sp_DoanhThuTheoThang;
GO
CREATE PROCEDURE sp_DoanhThuTheoThang
    @Thang INT,
    @Nam INT
AS
BEGIN
    SELECT 
        SUM(SoTien) as TongDoanhThu,
        COUNT(*) as SoGiaoDich
    FROM ThuPhi 
    WHERE MONTH(NgayThu) = @Thang AND YEAR(NgayThu) = @Nam;
END
GO

