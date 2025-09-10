-- ResetData.sql
-- Xóa sạch dữ liệu (giữ nguyên cấu trúc bảng) cho CSDL QuanLyPhongGym

SET NOCOUNT ON;


DECLARE @db NVARCHAR(128) = N'QuanLyPhongGym';

IF DB_ID(@db) IS NULL
BEGIN
	RAISERROR('Database %s không tồn tại.', 16, 1, @db);
	RETURN;
END

DECLARE @sql NVARCHAR(MAX) = N'USE ' + QUOTENAME(@db) + N';
BEGIN TRY
	BEGIN TRAN;

	-- Xóa dữ liệu theo quan hệ khóa ngoại
	DELETE FROM LichTapPT;
	DELETE FROM ChamCong;
	DELETE FROM ThuPhi;
	DELETE FROM HoiVien;
	DELETE FROM HuanLuyenVien;
	DELETE FROM GoiTap;
	DELETE FROM ThietBi;

	-- Reseed các bảng có IDENTITY
	IF OBJECT_ID(N''ChamCong'', N''U'') IS NOT NULL
		DBCC CHECKIDENT (''ChamCong'', RESEED, 0) WITH NO_INFOMSGS;
	IF OBJECT_ID(N''LichTapPT'', N''U'') IS NOT NULL
		DBCC CHECKIDENT (''LichTapPT'', RESEED, 0) WITH NO_INFOMSGS;

	COMMIT TRAN;
	PRINT N''Đã xóa dữ liệu và reseed IDENTITY thành công.'';
END TRY
BEGIN CATCH
	IF @@TRANCOUNT > 0 ROLLBACK TRAN;
	DECLARE @msg NVARCHAR(4000) = ERROR_MESSAGE();
	RAISERROR(N''Reset thất bại: %s'', 16, 1, @msg);
END CATCH';

EXEC(@sql); 