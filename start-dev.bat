@echo off
chcp 65001 > nul
echo ========================================================
echo   KHỞI ĐỘNG HỆ THỐNG WEB THUÊ GIA SƯ ONLINE (BE + FE)
echo ========================================================
echo.

:: 1. KIỂM TRA DOCKER VÀ POSTGRESQL
echo [1/4] Kiểm tra PostgreSQL Database (Docker Port 5434)...
docker info > nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [CẢNH BÁO] Docker Desktop chưa bật! Vui lòng bật Docker Desktop trước.
    pause
    exit /b 1
)

docker ps -a --format "{{.Names}}" | findstr /I "webthuegiasuonline" > nul 2>&1
if %ERRORLEVEL% equ 0 (
    docker start webthuegiasuonline > nul 2>&1
    echo   -> Container PostgreSQL "webthuegiasuonline" đã sẵn sàng!
) else (
    echo   -> Khởi tạo container PostgreSQL bằng docker-compose...
    docker compose up -d postgres
)

:: 2. GIẢI PHÓNG PORT NẾU BỊ TREO HOẶC CHẠY NGẦM
echo.
echo [2/4] Dọn dẹp các tiến trình chiếm dụng Port 8080 và 3000...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do (
    echo   -> Đang giải phóng cổng 8080 (PID %%a)...
    taskkill /F /PID %%a > nul 2>&1
)
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :3000 ^| findstr LISTENING') do (
    echo   -> Đang giải phóng cổng 3000 (PID %%a)...
    taskkill /F /PID %%a > nul 2>&1
)
echo   -> Các cổng 8080 và 3000 đã sạch sẽ!

:: 3. CHUẨN HÓA CODE VÀ FORMAT SPOTLESS
echo.
echo [3/4] Tự động định dạng code Java (mvn spotless:apply)...
call mvn spotless:apply > nul 2>&1

:: 4. KHỞI CHẠY BACKEND VÀ FRONTEND TRONG CỬA SỔ RIÊNG
echo.
echo [4/4] Khởi động Server Backend & Frontend...
start "BACKEND - Spring Boot (Port 8080)" cmd /k "cd /d C:\Users\Admin\Desktop\Web-thu-gia-su-online && mvn spring-boot:run"
start "FRONTEND - Vite Dev Server (Port 3000)" cmd /k "cd /d C:\Users\Admin\Desktop\web-thue-gia-su-fe && npm run dev"

echo.
echo ========================================================
echo   HỆ THỐNG ĐÃ ĐƯỢC BẬT THÀNH CÔNG!
echo   - Backend:  http://localhost:8080/giasu
echo   - Frontend: http://localhost:3000
echo ========================================================
echo Bạn có thể đóng cửa sổ này.
timeout /t 5 > nul
