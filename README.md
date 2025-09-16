# Quản Lý Phòng Gym (Java Swing)

Chào thầy👋 Đây là đồ án nhỏ giúp quản lý hội viên, gói tập, thu phí, lịch PT, HLV và thiết bị. Ứng dụng viết bằng Java Swing (NetBeans/Maven), kết nối SQL Server.

## Tính năng nổi bật
- Hội viên: CRUD, tự tính hạn theo gói, tùy chọn tạo hóa đơn khi lưu, gia hạn gói tự sinh phiếu thu
- Gói tập: CRUD, giá tiền, thời hạn (tháng), số buổi
- Chấm công: Check-in/out, tự trừ buổi lần đầu trong ngày, danh sách đang có mặt, no-show theo lịch PT
- HLV: CRUD, phân công hội viên, xem danh sách hội viên theo HLV
- Thiết bị: CRUD, đổi tình trạng, lịch bảo trì
- Thu phí & hóa đơn: CRUD, lọc theo ngày, in hóa đơn (Print), xuất CSV
- Thống kê: Thẻ chỉ số (HV, doanh thu hôm nay/tháng, ĐK mới), top gói tập, doanh thu theo loại phí, bộ lọc Tháng/Năm

## Yêu cầu môi trường
- JDK 8 trở lên
- NetBeans (hoặc IntelliJ/Eclipse đều chạy được)
- SQL Server (Express/Developer/LocalDB)

## Cách chạy siêu nhanh (5 bước)
1) Mở project bằng NetBeans (Open Project) thư mục `QuanLyPhongGym/`.
2) Cập nhật kết nối DB nếu cần: `src/util/DatabaseConnection.java`
   - Mặc định: `jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongGym;encrypt=true;trustServerCertificate=true`
   - USER/PASS có thể chỉnh tại file trên cho phù hợp máy thầy/cô.
3) Tạo database + dữ liệu mẫu: mở SSMS → chạy `sql/QuanLyPhongGym.sql` (tạo bảng + seed data).
   - Muốn dữ liệu sạch: chạy `sql/ResetData.sql` (xóa dữ liệu, giữ cấu trúc, reseed ID).
4) Run Project (Main class: `view.MainForm`).
5) Thao tác theo thứ tự gợi ý: Gói tập → HLV → Hội viên → Lịch PT → Chấm công → Thu phí/Thiết bị → Thống kê.

## Gợi ý test nhanh
- Hội viên: thêm mới với `MaGoiTap`, bật “Tạo hóa đơn khi lưu” → mở Thu phí thấy phiếu phát sinh.
- Chấm công: nhập mã HV → Check-in → tab “Đang có mặt” hiển thị chi tiết; Check-out để cập nhật trạng thái.
- Lịch PT: tạo 1 lịch hôm nay, không check-in → tab “Vắng (No-Show)” sẽ xuất hiện.
- Thu phí: in hóa đơn (Print) và xuất CSV.
- Thống kê: đổi Tháng/Năm → “Cập nhật” để xem số liệu theo kỳ.

## Cấu trúc thư mục
- `src/` mã nguồn (model/dao/view/util)
- `sql/QuanLyPhongGym.sql` tạo DB + seed dữ liệu mới
- `sql/ResetData.sql` xóa sạch dữ liệu, giữ schema
- `pom.xml` Maven (đã thêm `mssql-jdbc`)

## Lỗi thường gặp & cách xử lý
- Không kết nối được DB: kiểm tra SQL Server đã chạy, cổng 1433, tài khoản; sửa `DatabaseConnection.java` cho đúng.
- Thiếu driver JDBC: Maven sẽ tự tải khi build; nếu NetBeans chưa, hãy `Clean and Build`.
- Ngày/giờ sai định dạng: dùng `yyyy-MM-dd` cho ngày, `HH:mm` cho giờ (đã có thông báo hướng dẫn trong UI).

Chúc thầy/cô dùng thử vui vẻ. Nếu cần dữ liệu sạch để demo lại: chạy `ResetData.sql` rồi nhập từ UI theo thứ tự ở trên là ổn ạ ✅




