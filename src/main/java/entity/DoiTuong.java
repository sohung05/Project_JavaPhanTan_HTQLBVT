package entity;

public enum DoiTuong {
    SinhVien,
    TreEm,
    NguoiLon,
    NguoiCaoTuoi;

    // Hàm tiện ích để map từ chuỗi DB sang enum
    public static DoiTuong fromString(String str) {
        // Xử lý null hoặc chuỗi rỗng - trả về mặc định là NguoiLon
        if (str == null || str.trim().isEmpty()) {
            return NguoiLon; // Giá trị mặc định
        }
        
        switch (str.trim()) {
            case "Sinh viên":
            case "SinhVien":
                return SinhVien;
            case "Trẻ em":
            case "TreEm":
                return TreEm;
            case "Người lớn":
            case "NguoiLon":
                return NguoiLon;
            case "Người già":
            case "Người cao tuổi":
            case "NguoiCaoTuoi":
                return NguoiCaoTuoi;
            default:
                return NguoiLon; // Giá trị mặc định cho trường hợp không khớp
        }
    }
}