-- QuanLyPhongGym.sql
-- Tạo mới CSDL, bảng và dữ liệu mẫu phù hợp với dự án hiện tại

/*
Lưu ý sử dụng:
1) Mở SSMS (SQL Server Management Studio)
2) Chạy toàn bộ file này để tạo DB, bảng và seed dữ liệu mẫu
*/

-- Tạo database nếu chưa có
IF DB_ID('QuanLyPhongGym') IS NULL
BEGIN
	CREATE DATABASE QuanLyPhongGym;
END
GO

USE QuanLyPhongGym;
GO

-- Xóa các bảng nếu tồn tại (theo thứ tự phụ thuộc)
IF OBJECT_ID(N'dbo.LichTapPT', N'U') IS NOT NULL DROP TABLE dbo.LichTapPT;
IF OBJECT_ID(N'dbo.ChamCong', N'U') IS NOT NULL DROP TABLE dbo.ChamCong;
IF OBJECT_ID(N'dbo.ThuPhi', N'U') IS NOT NULL DROP TABLE dbo.ThuPhi;
IF OBJECT_ID(N'dbo.HoiVien', N'U') IS NOT NULL DROP TABLE dbo.HoiVien;
IF OBJECT_ID(N'dbo.HuanLuyenVien', N'U') IS NOT NULL DROP TABLE dbo.HuanLuyenVien;
IF OBJECT_ID(N'dbo.GoiTap', N'U') IS NOT NULL DROP TABLE dbo.GoiTap;
IF OBJECT_ID(N'dbo.ThietBi', N'U') IS NOT NULL DROP TABLE dbo.ThietBi;
GO

-- Bảng Gói Tập
CREATE TABLE GoiTap (
	MaGoiTap NVARCHAR(10) PRIMARY KEY,
	TenGoiTap NVARCHAR(100) NOT NULL,
	MoTa NVARCHAR(255),
	GiaTien DECIMAL(18,2) NOT NULL,
	ThoiHan INT NOT NULL, -- tháng
	SoBuoi INT NOT NULL,
	TrangThai BIT DEFAULT 1
);

-- Bảng Huấn Luyện Viên
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

-- Bảng Hội Viên
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

-- Bảng Thiết Bị
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

-- Bảng Chấm Công
CREATE TABLE ChamCong (
	ID INT IDENTITY(1,1) PRIMARY KEY,
	MaHoiVien NVARCHAR(10),
	NgayTap DATE,
	GioVao TIME,
	GioRa TIME,
	GhiChu NVARCHAR(255),
	FOREIGN KEY (MaHoiVien) REFERENCES HoiVien(MaHoiVien)
);

-- Bảng Thu Phí
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

-- Bảng Lịch Tập PT
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

-- DỮ LIỆU MẪU PHÙ HỢP HIỆN TẠI
-- Gói tập
INSERT INTO GoiTap (MaGoiTap, TenGoiTap, MoTa, GiaTien, ThoiHan, SoBuoi, TrangThai) VALUES
('GT001', N'Gói 1 tháng', N'Cơ bản 1 tháng', 500000, 1, 30, 1),
('GT002', N'Gói 3 tháng', N'Tiết kiệm 3 tháng', 1350000, 3, 90, 1),
('GTVIP', N'Gói VIP 12 tháng', N'VIP 12 tháng', 4500000, 12, 365, 1);

-- Huấn luyện viên
INSERT INTO HuanLuyenVien (MaHLV, TenHLV, GioiTinh, NgaySinh, SDT, DiaChi, Email, ChuyenMon, Luong, NgayVaoLam, TrangThai) VALUES
('HLV001', N'Nguyễn Văn A', N'Nam', '1990-05-15', '0901234567', N'Quận 1, TP.HCM', 'hlv.a@gym.com', N'Thể hình', 15000000, '2023-01-15', 1),
('HLV002', N'Trần Thị B', N'Nữ', '1992-08-20', '0907654321', N'Quận 3, TP.HCM', 'hlv.b@gym.com', N'Yoga', 12000000, '2023-03-10', 1);

-- Hội viên (dùng ngày hiện tại 2025-09-10 ví dụ; có thể chỉnh khi chạy thực tế)
INSERT INTO HoiVien (MaHoiVien, TenHoiVien, GioiTinh, NgaySinh, SDT, DiaChi, Email, NgayDangKy, MaGoiTap, NgayBatDau, NgayKetThuc, MaHLV, TrangThai, SoBuoiConLai) VALUES
('HV001', N'Lê Văn C', N'Nam', '1995-10-10', '0909123456', N'Quận 2, TP.HCM', 'hv.c@email.com', '2025-09-10', 'GT001', '2025-09-10', '2025-10-10', 'HLV001', 1, 30),
('HV002', N'Phạm Thị D', N'Nữ', '1998-03-25', '0908765432', N'Quận 7, TP.HCM', 'hv.d@email.com', '2025-09-10', 'GT002', '2025-09-10', '2025-12-10', 'HLV002', 1, 90);

-- Thiết bị
INSERT INTO ThietBi (MaThietBi, TenThietBi, LoaiThietBi, TinhTrang, NgayMua, GiaMua, NgayBaoTriCuoi, NgayBaoTriTiep, GhiChu) VALUES
('TB001', N'Máy chạy bộ Technogym', N'Cardio', N'Tốt', '2024-01-15', 50000000, '2025-03-01', '2025-09-01', N''),
('TB002', N'Tạ đòn Olympic', N'Tạ tự do', N'Tốt', '2024-02-20', 5000000, '2025-03-15', '2025-09-15', N'');

-- Thu phí (có thể được tạo tự động khi thêm hội viên trong ứng dụng)
INSERT INTO ThuPhi (MaPhieu, MaHoiVien, LoaiPhi, SoTien, NgayThu, PhuongThucTT, NguoiThu, GhiChu) VALUES
('TP001', 'HV001', N'Gói tập', 500000, '2025-09-10', N'Tiền mặt', N'Admin', N'Đăng ký Gói 1 tháng'),
('TP002', 'HV002', N'Gói tập', 1350000, '2025-09-10', N'Chuyển khoản', N'Admin', N'Đăng ký Gói 3 tháng');

-- Lịch PT hôm nay (dùng để test No-Show/hiển thị lịch)
INSERT INTO LichTapPT (MaHoiVien, MaHLV, NgayTap, GioBatDau, GioKetThuc, TrangThai) VALUES
('HV001', 'HLV001', '2025-09-10', '08:00:00', '09:00:00', N'Đã đặt'),
('HV002', 'HLV002', '2025-09-10', '09:30:00', '10:30:00', N'Đã đặt');
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

