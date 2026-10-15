# AGENTS.md - Quy Chuẩn Dự Án Web Thuê Gia Sư Online

Tài liệu này định nghĩa toàn bộ quy chuẩn kiến trúc, quy tắc đặt tên, cấu trúc thư mục và quy trình phát triển cho dự án **Web Thuê Gia Sư Online**. Mọi Developer và AI Agent khi tham gia phát triển dự án bắt buộc phải tuân thủ nghiêm ngặt các quy tắc này.

---

## 1. Công Nghệ & Môi Trường
* **Ngôn ngữ**: Java 21 (LTS)
* **Framework**: Spring Boot 3.x
* **Cơ sở dữ liệu**: PostgreSQL
* **ORM & Data Access**: Spring Data JPA, Hibernate
* **Mapping**: MapStruct (componentModel = "spring")
* **Boilerplate Reduction**: Lombok
* **Security & Auth**: Spring Security 6, Nimbus JOSE + JWT (Stateless Authentication)
* **Code Formatting**: Spotless Plugin (Google Java Format)
* **Giao tiếp & Trao đổi**: Tiếng Việt (theo \CONVENTIONS.md\)

---

## 2. Kiến Trúc Phân Tầng (Layered Architecture)

Dự án áp dụng mô hình phân tầng chuẩn N-Tier kết hợp tổ chức theo tính năng nghiệp vụ (**Package-by-Feature / Domain**):

\\\mermaid
flowchart TD
    Client([Client / Frontend]) --> Controller[Controller Layer\nREST API, Validation, ApiResponse]
    Controller --> Service[Service Layer\nBusiness Logic, Transaction Management]
    Service --> Mapper[Mapper Layer\nMapStruct DTO <-> Entity]
    Service --> Repository[Repository Layer\nSpring Data JPA, DB Operations]
    Repository --> Database[(PostgreSQL Database)]
\\\

### 2.1. Controller Layer (\controller/\)
* **Nhiệm vụ**: Đón nhận HTTP request, xác thực dữ liệu đầu vào (\@Valid\), điều phối gọi Service tương ứng và trả về response chuẩn.
* **Quy tắc**:
  * Không viết logic nghiệp vụ (business logic) tại Controller.
  * Mọi endpoint đều phải bọc kết quả trả về trong đối tượng chuẩn: \ApiResponse<T>\.
  * Đường dẫn endpoint (\@RequestMapping\) sử dụng danh từ số nhiều dạng \kebab-case\ (ví dụ: \/payment-methods\, \/course-enrollments\, \/tutor-applications\).
  * Sử dụng đúng HTTP Method:
    * \GET\: Truy vấn dữ liệu (không làm thay đổi trạng thái hệ thống).
    * \POST\: Tạo mới tài nguyên.
    * \PUT\: Cập nhật tài nguyên (toàn phần hoặc nhiều trường).
    * \PATCH\: Cập nhật một phần / trạng thái cụ thể.
    * \DELETE\: Xóa tài nguyên.

### 2.2. Service Layer (\service/\ & \service/.../impl/\)
* **Nhiệm vụ**: Xử lý toàn bộ logic nghiệp vụ, tính toán chi phí/hoa hồng, kiểm tra ràng buộc quyền hạn, quản lý giao dịch CSDL.
* **Quy tắc**:
  * Bắt buộc tách thành **Interface** (nằm trực tiếp trong package domain) và **Implementation class** (nằm trong thư mục con \impl/\).
  * Quản lý transaction bằng \@Transactional\ cho các thao tác ghi/sửa dữ liệu (create, update, delete).
  * Khi xảy ra lỗi logic, ném ngoại lệ chuẩn \AppException(ErrorCode....)\ để \GlobalExceptionHandler\ tự động bắt và trả về mã lỗi thích hợp.

### 2.3. Repository Layer (\epository/\)
* **Nhiệm vụ**: Tương tác trực tiếp với Database.
* **Quy tắc**:
  * Kế thừa \JpaRepository<EntityName, IdType>\.
  * Tận dụng tối đa Derived Query Methods của Spring Data JPA (ví dụ: \indByUserId\, \indByCourseId\, \indByUserIdAndStatus\).
  * Sử dụng \@Query\ (JPQL hoặc Native Query) khi truy vấn phức tạp hoặc cần tối ưu hiệu năng.

### 2.4. Entity Layer (\entity/\)
* **Nhiệm vụ**: Ánh xạ quan hệ thực thể với các bảng trong PostgreSQL.
* **Quy tắc**:
  * Sử dụng ID dạng UUID (String trong Java hoặc UUID type tùy bảng, chuẩn hóa theo schema DB).
  * Khai báo Lombok chuẩn trên từng Entity:
    \\\java
    @Entity
    @Table(name = "tên_bảng_số_nhiều")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class TenEntity { ... }
    \\\
  * Khóa ngoại / Quan hệ: Sử dụng \@ManyToOne(fetch = FetchType.LAZY)\ để tránh lỗi N+1 Query.

### 2.5. DTO Layer (\dto/\)
* **Nhiệm vụ**: Đảm bảo tính đóng gói, không để lộ cấu trúc Entity nội bộ ra bên ngoài API.
* **Phân chia thư mục**:
  * \dto/request/<domain>/\: Chứa các Request DTO nhận từ client (\XxxCreationRequest\, \XxxUpdateRequest\).
  * \dto/response/<domain>/\: Chứa các Response DTO trả về cho client (\XxxResponse\).
* **Validation**: Sử dụng Bean Validation (\@NotBlank\, \@NotNull\, \@Min\, \@Email\...).

### 2.6. Mapper Layer (\mapper/\)
* **Nhiệm vụ**: Chuyển đổi hai chiều giữa Entity và DTO.
* **Quy tắc**:
  * Sử dụng MapStruct với khai báo: \@Mapper(componentModel = "spring")\.
  * Hỗ trợ cập nhật từng phần:
    \\\java
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget EntityName entity, UpdateRequest request);
    \\\

---

## 3. Cấu Trúc Domain Nghiệp Vụ (Package-by-Feature)

Toàn bộ code trong \src/main/java/org/example/web_thue_gia_su_online/\ được phân chia đồng nhất theo các domain sau:

| Tên Domain | Ý Nghĩa Nghiệp Vụ | Các Thực Thể / Bảng Chính |
| :--- | :--- | :--- |
| **\uthentication\** | Đăng nhập, đăng ký, xác thực JWT, refresh token, thu hồi token | \InvalidatedToken\ |
| **\identity_and_administration\**| Quản lý tài khoản người dùng, hồ sơ gia sư, lịch sử kiểm toán (audit log) | \User\, \TutorProfile\, \ActionLog\ |
| **\learning_and_education\** | Đặt lịch gia sư, gói dịch vụ môi giới, khóa học, bài học, ghi danh, nhắn tin | \Booking\, \BrokeragePackage\, \Course\, \Lesson\, \CourseEnrollment\, \Message\ |
| **\inance_and_wallet\** | Quản lý ví tiền, giao dịch nạp/rút/ký quỹ, tài khoản ngân hàng liên kết | \Wallet\, \PaymentTransaction\, \PaymentMethod\ |
| **\	utoring\** | Quy trình nộp hồ sơ và duyệt đơn ứng tuyển làm gia sư | \TutorApplication\ |
| **\disputing_and_reporting\** | Giải quyết khiếu nại, tố cáo và tranh chấp giữa học sinh và gia sư | \Dispute\ |
| **\rticle_and_community\** | Diễn đàn chia sẻ kiến thức, bài viết blog, bình luận | \Article\, \ArticleComment\ |
| **\enums\** | Các kiểu liệt kê dùng chung toàn hệ thống | \RequestStatus\, \WalletType\, \UserRole\, \TransactionType\... |
| **\exception\** | Xử lý tập trung các lỗi ngoại lệ của ứng dụng | \GlobalExceptionHandler\, \AppException\, \ErrorCode\ |
| **\configuration\** | Cấu hình bảo mật, phân quyền, CORS, format JSON | \SecurityConfig\, \ApplicationInitConfig\... |

---

## 4. Quy Tắc Đặt Tên (Naming Conventions)

### 4.1. Java Code
| Thành phần | Quy tắc đặt tên | Ví dụ |
| :--- | :--- | :--- |
| **Entity Class** | PascalCase, Danh từ số ít | \User\, \Booking\, \PaymentMethod\ |
| **Controller Class** | \<Entity>Controller\ | \BookingController\, \PaymentMethodController\ |
| **Service Interface** | \<Entity>Service\ | \BookingService\, \LessonService\ |
| **Service Implementation** | \<Entity>ServiceImpl\ (đặt trong package \impl\) | \BookingServiceImpl\, \LessonServiceImpl\ |
| **Repository Interface**| \<Entity>Repository\ | \BookingRepository\, \CourseRepository\ |
| **Creation Request DTO** | \<Entity>CreationRequest\ | \BookingCreationRequest\, \LessonCreationRequest\ |
| **Update Request DTO** | \<Entity>UpdateRequest\ | \BookingUpdateRequest\, \LessonUpdateRequest\ |
| **Response DTO** | \<Entity>Response\ | \BookingResponse\, \LessonResponse\ |
| **Mapper Interface** | \<Entity>Mapper\ | \BookingMapper\, \PaymentMethodMapper\ |
| **Enum Class** | PascalCase cho tên Enum, UPPER_SNAKE_CASE cho giá trị | \RequestStatus\ (\PENDING\, \APPROVED\, \REJECTED\) |
| **Variables / Properties**| camelCase | \	utorIncome\, \platformFee\, \ccountNumber\ |
| **Service Methods** | Động từ + Tên thao tác (camelCase) | \createBooking\, \getBooking\, \updateStatus\ |

### 4.2. Database & SQL (PostgreSQL)
* **Tên bảng**: Danh từ số nhiều, dạng \snake_case\ (ví dụ: \users\, \	utor_applications\, \course_enrollments\, \lessons\, \ction_logs\).
* **Tên cột**: Dạng \snake_case\ (ví dụ: \user_id\, \cv_url\, \created_at\, \is_default\).
* **Tên kiểu Enum SQL**: Dạng \snake_case\ (ví dụ: \equest_status\, \wallet_type\, \	ransaction_type\).
* **Khóa chính**: Luôn đặt tên là \id\.
* **Khóa ngoại**: Tên thực thể trỏ tới + \_id\ (ví dụ: \user_id\, \course_id\, \student_id\).

### 4.3. REST API Endpoints
* Sử dụng danh từ số nhiều, chữ thường, cách nhau bằng gạch nối (\kebab-case\):
  * \/bookings\
  * \/tutor-applications\
  * \/payment-methods\
  * \/course-enrollments\
  * \/action-logs\
* Lồng endpoint theo quan hệ cha - con một cách hợp lý:
  * \/lessons/course/{courseId}\: Lấy các bài học của khóa học.
  * \/payment-methods/user/{userId}\: Lấy các thẻ/tài khoản của user.
  * \/course-enrollments/student/{studentId}\: Lấy các khóa học đã ghi danh của học sinh.

---

## 5. Quy Chuẩn Chất Lượng Code & Quy Trình Git

### 5.1. Định dạng Code (Spotless)
Trước khi commit bất kỳ thay đổi nào, bắt buộc phải chạy lệnh format code:
\\\ash
mvn spotless:apply
\\\
Đảm bảo 100% file tuân thủ chuẩn format và không bị lỗi checkstyle.

### 5.2. Kiểm tra Biên Dịch (Compile Check)
Trước khi tạo commit hoặc push lên repo:
\\\ash
mvn test-compile
\\\
Chỉ được phép commit khi kết quả trả về là **\BUILD SUCCESS\**.

### 5.3. Quy tắc Git Commit & Branching
* **Quy ước Commit Message**: Tuân thủ chuẩn Conventional Commits:
  * \eat(...)\: Thêm tính năng, API hoặc bảng mới.
  * \ix(...)\: Sửa lỗi logic, sửa import, giải quyết xung đột (conflict).
  * \efactor(...)\: Tái cấu trúc code (đổi tên, chuyển đổi kiến trúc mà không đổi logic).
  * \style(...)\: Chạy format code Spotless.
  * \docs(...)\: Cập nhật tài liệu, file SQL hoặc file markdown quy chuẩn.
* **Nhánh làm việc**:
  * \main\: Nhánh triển khai sản phẩm ổn định (production).
  * \develop\: Nhánh tích hợp chính của đội ngũ phát triển (mọi tính năng hoàn thiện đều gộp vào đây).
  * \eature/<ten-tinh-nang>\: Nhánh phát triển tính năng riêng lẻ trước khi merge vào \develop\.