-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 02, 2025 lúc 10:06 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `thegioianvat_db`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `maDH` int(11) NOT NULL,
  `maMA` int(11) NOT NULL,
  `soLuong` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`maDH`, `maMA`, `soLuong`) VALUES
(2, 1, 2),
(3, 1, 3),
(4, 1, 3),
(5, 1, 2),
(5, 3, 2),
(7, 1, 2),
(8, 1, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhmuc`
--

CREATE TABLE `danhmuc` (
  `maDM` int(11) NOT NULL,
  `tenDM` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `danhmuc`
--

INSERT INTO `danhmuc` (`maDM`, `tenDM`) VALUES
(1, 'Đồ uống'),
(2, 'Món chiên'),
(3, 'Đồ trộn'),
(4, 'Ăn vặt khác');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donhang`
--

CREATE TABLE `donhang` (
  `maDH` int(11) NOT NULL,
  `maKH` int(11) DEFAULT NULL,
  `ngayLap` datetime DEFAULT current_timestamp(),
  `tongTien` decimal(12,2) DEFAULT NULL,
  `ghiChu` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `donhang`
--

INSERT INTO `donhang` (`maDH`, `maKH`, `ngayLap`, `tongTien`, `ghiChu`) VALUES
(2, 1, '2025-05-27 15:11:12', 50000.00, 'Đã thanh toán'),
(3, 2, '2025-05-26 17:00:00', 75000.00, 'Đã thanh toán'),
(4, 2, '2025-05-27 17:00:00', 75000.00, 'Đã thanh toán'),
(5, 2, '2025-05-30 17:00:00', 80000.00, 'Đã thanh toán'),
(7, 4, '2025-05-31 05:56:52', 50000.00, NULL),
(8, 4, '2025-05-31 12:13:31', 45000.00, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `maKH` int(11) NOT NULL,
  `tenKH` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `soDienThoai` varchar(20) DEFAULT NULL,
  `diaChi` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `loaiKH` int(11) DEFAULT NULL COMMENT '0: Vãng lai, 1: Thân thiết, 2: VIP'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`maKH`, `tenKH`, `soDienThoai`, `diaChi`, `loaiKH`) VALUES
(1, 'Khách', '', '', 0),
(2, 'Trần Văn C', '0987654321', '456 Trần Hưng Đạo', 1),
(4, 'Nguyễn Văn D', '0888185858', '125 Mai Dịch', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `monan`
--

CREATE TABLE `monan` (
  `maMA` int(11) NOT NULL,
  `tenMA` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `maDM` int(11) DEFAULT NULL,
  `donGia` decimal(10,2) NOT NULL,
  `soLuong` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `monan`
--

INSERT INTO `monan` (`maMA`, `tenMA`, `maDM`, `donGia`, `soLuong`) VALUES
(1, 'Trà sữa truyền thống', 1, 25000.00, 50),
(2, 'Bánh tráng trộn', 3, 20000.00, 30),
(3, 'Khoai tây chiên', 2, 15000.00, 40),
(4, 'Cá viên chiên', 2, 12000.00, 50),
(6, 'Gà KFC', 2, 30000.00, 40);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `tenDangNhap` varchar(50) NOT NULL,
  `matKhau` varchar(100) NOT NULL,
  `hoTen` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `vaiTro` enum('Admin','NhanVien') DEFAULT 'NhanVien'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`tenDangNhap`, `matKhau`, `hoTen`, `vaiTro`) VALUES
('admin', '123', 'Quản Trị Viên', 'Admin'),
('nv01', '123456', 'Nguyễn Văn A', 'NhanVien'),
('nv02', '123', 'Dương Dương', 'NhanVien');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thanhtoan`
--

CREATE TABLE `thanhtoan` (
  `maTT` int(11) NOT NULL,
  `maDH` int(11) DEFAULT NULL,
  `hinhThuc` enum('Tien Mat','Chuyen Khoan','Momo','ZaloPay') NOT NULL,
  `soTien` decimal(12,2) DEFAULT NULL,
  `thoiGian` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `thanhtoan`
--

INSERT INTO `thanhtoan` (`maTT`, `maDH`, `hinhThuc`, `soTien`, `thoiGian`) VALUES
(1, 2, 'Tien Mat', 50000.00, '2025-05-29 13:46:11'),
(2, 3, 'Tien Mat', 75000.00, '2025-05-29 15:09:19'),
(3, 4, 'Momo', 75000.00, '2025-06-01 20:31:12'),
(4, 5, 'Tien Mat', 80000.00, '2025-06-01 21:18:47');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`maDH`,`maMA`),
  ADD KEY `maMA` (`maMA`);

--
-- Chỉ mục cho bảng `danhmuc`
--
ALTER TABLE `danhmuc`
  ADD PRIMARY KEY (`maDM`);

--
-- Chỉ mục cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`maDH`),
  ADD KEY `maKH` (`maKH`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`maKH`);

--
-- Chỉ mục cho bảng `monan`
--
ALTER TABLE `monan`
  ADD PRIMARY KEY (`maMA`),
  ADD KEY `maDM` (`maDM`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`tenDangNhap`);

--
-- Chỉ mục cho bảng `thanhtoan`
--
ALTER TABLE `thanhtoan`
  ADD PRIMARY KEY (`maTT`),
  ADD KEY `maDH` (`maDH`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `danhmuc`
--
ALTER TABLE `danhmuc`
  MODIFY `maDM` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `donhang`
--
ALTER TABLE `donhang`
  MODIFY `maDH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `maKH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `monan`
--
ALTER TABLE `monan`
  MODIFY `maMA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `thanhtoan`
--
ALTER TABLE `thanhtoan`
  MODIFY `maTT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `chitietdonhang_ibfk_1` FOREIGN KEY (`maDH`) REFERENCES `donhang` (`maDH`) ON DELETE CASCADE,
  ADD CONSTRAINT `chitietdonhang_ibfk_2` FOREIGN KEY (`maMA`) REFERENCES `monan` (`maMA`);

--
-- Các ràng buộc cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD CONSTRAINT `donhang_ibfk_1` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`);

--
-- Các ràng buộc cho bảng `monan`
--
ALTER TABLE `monan`
  ADD CONSTRAINT `monan_ibfk_1` FOREIGN KEY (`maDM`) REFERENCES `danhmuc` (`maDM`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `thanhtoan`
--
ALTER TABLE `thanhtoan`
  ADD CONSTRAINT `thanhtoan_ibfk_1` FOREIGN KEY (`maDH`) REFERENCES `donhang` (`maDH`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
