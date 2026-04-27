# 📋 HƯỚNG DẪN CHẠY SQL CHO BUỔI THUYẾT TRÌNH

## 🎯 Mục đích
Tạo dữ liệu mới cho buổi thuyết trình với mô hình tàu thực tế:
- Mỗi chuyến tàu chạy xuyên suốt nhiều ga
- Khách hàng chỉ chọn ga lên và ga xuống
- Có dữ liệu mẫu để hiển thị thống kê

---

## 🚀 CÁCH CHẠY

### Bước 1: Mở SQL Server Management Studio (SSMS)

### Bước 2: Mở file SQL
```
📁 UngDungBanVeTauTaiNhaGa/src/data/04_ResetData_ThuyetTrinh.sql
```

### Bước 3: Chạy script
- Nhấn **F5** hoặc click nút **Execute**
- Đợi khoảng 10-20 giây để hoàn thành

### Bước 4: Kiểm tra kết quả
- Xem console output để đảm bảo không có lỗi
- Kiểm tra dữ liệu bằng query:

```sql
-- Kiểm tra số lượng dữ liệu
SELECT 'Ga' AS Loai, COUNT(*) AS SoLuong FROM Ga
UNION ALL
SELECT 'ChuyenTau', COUNT(*) FROM ChuyenTau
UNION ALL
SELECT 'LichTrinh', COUNT(*) FROM LichTrinh
UNION ALL
SELECT 'KhachHang', COUNT(*) FROM KhachHang
UNION ALL
SELECT 'HoaDon', COUNT(*) FROM HoaDon
UNION ALL
SELECT 'Ve', COUNT(*) FROM Ve;
```

**Kết quả mong đợi:**
- Ga: 12
- ChuyenTau: 6
- LichTrinh: 180 (30 ngày × 6 chuyến)
- KhachHang: 50
- HoaDon: ~120
- Ve: ~120

---

## 📊 DỮ LIỆU ĐÃ TẠO

### 1. **12 Ga theo thứ tự Bắc → Nam:**
```
LC → HN → TH → VINH → DH → HUE → DN → QN → NT → PT → SG → BL
```

### 2. **6 Chuyến tàu:**
- **SE1, SE3, SE5**: Chiều Bắc → Nam (Lào Cai → Bạc Liêu)
- **SE2, SE4, SE6**: Chiều Nam → Bắc (Bạc Liêu → Lào Cai)

### 3. **Giờ khởi hành:**
| Chuyến | Ga xuất phát | Giờ khởi hành |
|--------|--------------|---------------|
| SE1 | Lào Cai | 05:00 |
| SE3 | Lào Cai | 12:00 |
| SE5 | Lào Cai | 20:00 |
| SE2 | Bạc Liêu | 06:00 |
| SE4 | Bạc Liêu | 13:00 |
| SE6 | Bạc Liêu | 21:00 |

### 4. **Dữ liệu mẫu:**
- 50 khách hàng
- ~120 hóa đơn trong 15 ngày gần đây
- Các loại vé: Người lớn (70%), Sinh viên (10%), Trẻ em (10%), Người cao tuổi (10%)

---

## 📖 TÀI LIỆU THAM KHẢO

### Chi tiết thời gian tàu đến từng ga:
Xem file: **`THOI_GIAN_TAU.md`**

### Ví dụ:
**SE1** xuất phát 05:00 từ Lào Cai:
- 07:00: Đến Hà Nội
- 16:00: Đến Đà Nẵng
- 23:30: Đến Sài Gòn
- 01:00 (+1 ngày): Đến Bạc Liêu

---

## ⚠️ LƯU Ý QUAN TRỌNG

### 1. **Mô hình mới khác mô hình cũ:**
- **Cũ**: Mỗi cặp ga có 1 lịch trình riêng (Hà Nội→Sài Gòn, Hà Nội→Đà Nẵng là 2 lịch trình khác nhau)
- **Mới**: 1 chuyến tàu = 1 lịch trình xuyên suốt, khách chọn ga lên/xuống

### 2. **Code cần cập nhật:**
Để hệ thống hoạt động đúng với mô hình mới, cần cập nhật:

#### a. **LichTrinh_DAO.java**
```java
// Method timLichTrinh() cần tìm lịch trình có đi qua cả ga đi và ga đến
// Ví dụ: Tìm Hà Nội → Đà Nẵng sẽ tìm chuyến LC→BL có đi qua HN và DN
```

#### b. **Tính giá vé**
- Hiện tại: Giá cố định theo loại toa
- Cần thêm: Tính giá theo khoảng cách (số ga hoặc km)

### 3. **Tạm thời chưa cập nhật code:**
Bạn vẫn có thể demo với dữ liệu mới này, nhưng:
- Khi tìm vé, nhập chính xác tên ga: "Lào Cai", "Hà Nội", "Đà Nẵng"...
- Chỉ có thể tìm vé từ ga đầu → ga cuối (LC→BL hoặc BL→LC)
- Chưa tìm được các đoạn trung gian (HN→DN)

### 4. **Để tìm đoạn trung gian:**
Cần update code trong `LichTrinh_DAO.java`:

```java
public List<LichTrinh> timLichTrinh(String tenGaDi, String tenGaDen, LocalDate ngayDi) {
    // TODO: Tìm lịch trình có đi qua cả ga đi và ga đến
    // Kiểm tra thứ tự ga (ga đi phải trước ga đến)
    // Ví dụ: HN→DN sẽ match với lịch trình LC→BL
}
```

---

## 🎬 DEMO CHO THUYẾT TRÌNH

### Demo 1: Xem Dashboard
✅ **Đã có dữ liệu sẵn**
- Mở Dashboard → Xem thống kê doanh thu 15 ngày gần đây
- Xem thống kê khách hàng
- Xem số chỗ trống theo tuyến

### Demo 2: Quản lý Khách hàng
✅ **Đã có 50 khách hàng mẫu**
- Tìm kiếm, xem danh sách khách hàng
- Thêm/sửa/xóa khách hàng

### Demo 3: Quản lý Hóa đơn
✅ **Đã có ~120 hóa đơn mẫu**
- Xem lịch sử hóa đơn
- Tìm kiếm hóa đơn theo ngày, khách hàng
- In hóa đơn

### Demo 4: Bán vé
⚠️ **Cần lưu ý**
- Hiện tại chỉ tìm được vé từ ga đầu → ga cuối
- Ví dụ có thể demo:
  - Lào Cai → Bạc Liêu (chuyến SE1, SE3, SE5)
  - Bạc Liêu → Lào Cai (chuyến SE2, SE4, SE6)

---

## 🔄 ROLLBACK (Nếu cần)

Nếu muốn quay lại dữ liệu cũ, chạy file:
```
📁 02_ResetAndInsertData.sql
```

---

## 📞 HỖ TRỢ

Nếu gặp lỗi khi chạy SQL:
1. Kiểm tra kết nối database
2. Đảm bảo database `HTQLVT` đã được tạo
3. Xem log lỗi trong SSMS
4. Kiểm tra quyền truy cập (cần quyền DELETE, INSERT)

---

📅 **Ngày tạo**: 22/12/2025  
🎯 **Mục đích**: Chuẩn bị data cho buổi thuyết trình  
✅ **Trạng thái**: Sẵn sàng sử dụng






