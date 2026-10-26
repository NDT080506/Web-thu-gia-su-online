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

### 2.3. Repository Layer (\
epository/\)
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
* **Tên kiểu Enum SQL**: Dạng \snake_case\ (ví dụ: \
equest_status\, \wallet_type\, \	ransaction_type\).
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
  * \
efactor(...)\: Tái cấu trúc code (đổi tên, chuyển đổi kiến trúc mà không đổi logic).
  * \style(...)\: Chạy format code Spotless.
  * \docs(...)\: Cập nhật tài liệu, file SQL hoặc file markdown quy chuẩn.
* **Nhánh làm việc**:
  * \main\: Nhánh triển khai sản phẩm ổn định (production).
  * \develop\: Nhánh tích hợp chính của đội ngũ phát triển (mọi tính năng hoàn thiện đều gộp vào đây).
  * \eature/<ten-tinh-nang>\: Nhánh phát triển tính năng riêng lẻ trước khi merge vào \develop\.

---

## 6. Quy Chuẩn Kiểm Thử Nghiệp Vụ (Business Logic Testing)

Mọi AI Agent khi kiểm tra, sửa lỗi hoặc hoàn thiện module nghiệp vụ phải xác minh tính đúng đắn của toàn bộ luồng nghiệp vụ, không chỉ kiểm tra cú pháp hoặc khả năng biên dịch.

Hai module bắt buộc được kiểm thử theo tài liệu này:

- **Option 1 — Private Tutoring:** Quy trình thương lượng và thuê gia sư riêng thông qua Conversation, Message và Booking.
- **Option 2 — Course Enrollment:** Quy trình đăng ký khóa học online thông qua Course, Lesson, CourseEnrollment và PaymentTransaction.

### 6.1. Quy Trình Kiểm Tra Bắt Buộc

Trước khi kết luận một module đã hoàn thiện, Agent phải thực hiện theo thứ tự:

1. **Khảo sát code hiện có:** Đọc Entity, Enum, DTO, Mapper, Repository, Service, Controller, cấu hình Spring Security và các migration liên quan.
2. **Đối chiếu nghiệp vụ:** So sánh code thực tế với các quy tắc tại mục 6.2 và 6.3. Không tự giả định nghiệp vụ chưa được xác định.
3. **Kiểm tra dữ liệu:** Xác minh quan hệ Entity, khóa ngoại, unique constraint, nullable, index và tính nhất quán giữa JPA với PostgreSQL.
4. **Viết hoặc cập nhật test:** Bổ sung Unit Test cho logic nghiệp vụ và Integration Test cho các luồng quan trọng.
5. **Chạy kiểm thử:** Chạy Spotless, compile và test. Phân biệt rõ test đã chạy, test bị bỏ qua và test không chạy được.
6. **Báo cáo kết quả:** Liệt kê các chức năng đã kiểm tra, test case thành công/thất bại, lỗi còn tồn tại và các phần chưa thể xác minh.

Agent không được tự ý thay đổi thiết kế nghiệp vụ chỉ để làm test pass. Nếu phát hiện quy tắc chưa rõ hoặc code hiện tại mâu thuẫn với tài liệu, phải nêu rõ vấn đề và chọn giải pháp phù hợp với kiến trúc hiện có.

### 6.2. Option 1 — Private Tutoring (Thuê Gia Sư Riêng)

#### 6.2.1. Luồng nghiệp vụ chuẩn

1. Học sinh tìm gia sư và tạo hoặc mở Conversation trực tiếp với gia sư.
2. Học sinh và gia sư trao đổi điều kiện thuê thông qua Message.
3. Hai bên thể hiện sự đồng ý với các điều kiện đã thương lượng.
4. Chỉ khi đủ điều kiện thỏa thuận, học sinh mới được chốt thuê và tạo Booking.
5. Backend tính toán giá gói dịch vụ, phí nền tảng và thu nhập gia sư; không tin các giá trị tài chính do frontend gửi.
6. Booking được xử lý theo trạng thái thanh toán, xác nhận liên hệ, thực hiện dịch vụ và hoàn tất theo quy tắc nghiệp vụ đã định nghĩa.
7. Khi đủ điều kiện, học sinh có thể tạo Review cho gia sư.

#### 6.2.2. Quy tắc kiểm thử Conversation và Message

Agent phải kiểm tra các trường hợp sau:

- Người dùng chỉ được xem Conversation mà mình tham gia, trừ Admin có quyền phù hợp.
- Chỉ học sinh và gia sư thuộc Conversation mới được gửi hoặc nhận tin nhắn.
- Không cho phép giả mạo `senderId` bằng dữ liệu do client gửi.
- WebSocket phải xác thực JWT, kiểm tra quyền `SUBSCRIBE` và kiểm tra quyền gửi tin.
- Message phải được lưu thành công trước khi server phát tin nhắn mới.
- Không cho phép sửa hoặc xóa tin nhắn của người khác nếu không có quyền quản trị được quy định.
- Khi điều kiện thương lượng thay đổi, trạng thái đồng ý trước đó không được tiếp tục được sử dụng nếu các điều kiện đã đồng ý không còn đúng.
- Việc tạo Conversation lặp lại phải tuân thủ chính sách hội thoại hiện có; cần kiểm tra tình huống hai request đồng thời tạo Conversation cho cùng một cặp người dùng.

#### 6.2.3. Quy tắc kiểm thử Booking

- Người tạo Booking phải là học sinh đã đăng nhập.
- `studentId` phải lấy từ danh tính đã xác thực, không tin ID do frontend gửi.
- `tutorId` phải được xác định từ Conversation và dữ liệu liên quan đã được backend xác minh.
- Conversation phải thuộc đúng học sinh và gia sư.
- Không được tạo Booking nếu Conversation chưa đáp ứng điều kiện thỏa thuận.
- Booking phải liên kết đúng Conversation, BrokeragePackage, học sinh và gia sư theo thiết kế Entity.
- Kiểm tra gói dịch vụ tồn tại, đang hoạt động và có giá hợp lệ.
- Giá, phí nền tảng và thu nhập gia sư phải được tính ở backend.
- Bảo đảm:
  `totalPrice = platformFee + tutorIncome`.
- Không cho phép người ngoài xem, sửa, hủy hoặc thực hiện thao tác Booking không thuộc quyền của họ.
- Các chuyển trạng thái phải tuân thủ quy tắc nghiệp vụ; không cho phép chuyển tùy ý giữa các trạng thái.
- Các thao tác cập nhật Booking và ghi nhận tài chính liên quan phải đảm bảo tính nhất quán trong transaction.

**Lưu ý:** Không mặc định Booking phải có một trạng thái cụ thể như `PENDING_PAYMENT` hoặc `CONFIRMED` nếu Enum hiện tại quy định khác. Agent phải đối chiếu Enum và quy trình đã triển khai trước khi viết test.

#### 6.2.4. Test case tối thiểu

| Trường hợp | Kết quả mong đợi |
|---|---|
| Học sinh và gia sư hợp lệ đã đồng ý điều kiện | Cho phép tiến hành chốt thuê |
| Conversation chưa đủ điều kiện đồng ý | Từ chối tạo Booking |
| Học sinh cố tạo Booking cho Conversation của người khác | Từ chối |
| Frontend gửi `studentId` hoặc `tutorId` giả | Backend không sử dụng ID giả để xác định quyền |
| BrokeragePackage không tồn tại hoặc đã bị vô hiệu hóa | Từ chối tạo Booking |
| Client tự gửi `totalPrice`, `platformFee`, `tutorIncome` không hợp lệ | Backend tự tính lại hoặc từ chối theo hợp đồng API |
| Người ngoài truy cập Booking | Từ chối |
| Hai request đồng thời tạo Booking không hợp lệ hoặc trùng theo quy tắc nghiệp vụ | Không để dữ liệu vi phạm constraint |
| Cập nhật Booking thất bại giữa transaction | Rollback các thay đổi thuộc cùng transaction |

### 6.3. Option 2 — Course Enrollment (Đăng Ký Khóa Học Online)

#### 6.3.1. Luồng nghiệp vụ chuẩn

1. Gia sư tạo và quản lý Course của mình.
2. Gia sư tạo các Lesson tương ứng với từng buổi học online.
3. Học sinh xem thông tin công khai của khóa học và đăng ký.
4. Backend tạo CourseEnrollment với trạng thái ban đầu phù hợp.
5. Nếu khóa học yêu cầu thanh toán, hệ thống tạo PaymentTransaction và chỉ kích hoạt enrollment sau khi xác nhận thanh toán thành công.
6. Học sinh có enrollment hợp lệ được truy cập lịch học, Meeting URL và tài nguyên riêng tư của các Lesson.
7. Học sinh theo học khóa học. Việc theo dõi tiến độ từng buổi chỉ triển khai nếu có nghiệp vụ riêng được xác định.
8. Khi đủ điều kiện, học sinh có thể đánh giá gia sư theo chính sách Review.

#### 6.3.2. Quy tắc kiểm thử Course và Lesson

- Chỉ gia sư sở hữu Course mới được tạo, sửa hoặc xóa Lesson của Course đó, trừ quyền Admin được quy định rõ.
- Không tin `tutorId` hoặc `courseId` do client gửi để xác định quyền sở hữu.
- Lesson phải liên kết đúng Course.
- `lessonOrder` phải là số nguyên hợp lệ, bắt đầu từ 1 nếu quy định nghiệp vụ yêu cầu.
- Nếu mỗi Course không được có hai Lesson cùng thứ tự, phải kiểm tra cả Service và unique constraint tại database.
- Khi thay đổi thứ tự hai Lesson, phải kiểm tra xung đột unique constraint.
- Danh sách Lesson phải được sắp xếp theo `lessonOrder`, không dựa vào thứ tự mặc định của database.
- Không cho phép người chưa đủ quyền truy cập `meetingUrl`, video trả phí hoặc tài liệu riêng tư.
- Học sinh chỉ được truy cập nội dung khóa học khi CourseEnrollment đáp ứng điều kiện truy cập.
- Việc xóa Course hoặc Lesson phải tuân thủ ràng buộc dữ liệu và chính sách lưu giữ lịch sử học tập.

#### 6.3.3. Quy tắc kiểm thử CourseEnrollment

- `studentId` lấy từ người dùng đã xác thực.
- Course phải tồn tại và đáp ứng điều kiện cho phép đăng ký.
- Không cho phép học sinh đăng ký trùng cùng Course nếu chính sách chỉ cho phép một enrollment cho mỗi cặp học sinh và khóa học.
- Unique constraint trên `(student_id, course_id)` phải được giữ nếu phù hợp với thiết kế nghiệp vụ.
- `pricePaid` phải lấy từ giá trị đã được backend xác minh, không tin giá do frontend gửi.
- Enrollment mới phải có trạng thái ban đầu hợp lệ.
- Nếu cần thanh toán, không chuyển enrollment sang `ACTIVE` chỉ dựa trên phản hồi từ frontend.
- PaymentTransaction phải được xác minh trước khi kích hoạt enrollment.
- Xử lý thông báo thanh toán lặp lại phải có tính idempotent, tránh ghi nhận thanh toán hoặc kích hoạt enrollment nhiều lần.
- Nếu thanh toán thất bại, enrollment không được kích hoạt.
- Không cho phép học sinh xem enrollment của người khác.
- Gia sư chỉ được xem enrollment thuộc Course do mình sở hữu, nếu API hỗ trợ quyền này.
- Admin chỉ được truy cập và thực hiện thao tác theo quyền quản trị được cấu hình.
- Nếu CourseEnrollment chỉ quản lý đăng ký, không tự động gộp tiến độ học hoặc trạng thái hoàn thành từng Lesson vào trạng thái enrollment.
- Không cho phép người dùng tự đặt `progressPercent`, `completedAt` hoặc trạng thái hoàn thành nếu các trường này do backend quản lý.

#### 6.3.4. Test case tối thiểu

| Trường hợp | Kết quả mong đợi |
|---|---|
| Học sinh đăng ký Course hợp lệ | Tạo enrollment đúng trạng thái ban đầu |
| Học sinh đăng ký cùng Course lần thứ hai | Từ chối hoặc xử lý theo chính sách đã định nghĩa, không tạo dữ liệu trùng trái phép |
| Course không tồn tại hoặc không được phép đăng ký | Từ chối |
| Client gửi `studentId` của người khác | Backend không sử dụng ID giả |
| Client tự gửi giá khóa học thấp hơn giá thật | Backend không chấp nhận giá giả |
| Thanh toán thành công đã được xác minh | Enrollment chuyển sang trạng thái phù hợp |
| Frontend tự thông báo thanh toán thành công nhưng giao dịch chưa được xác minh | Không kích hoạt enrollment |
| Webhook thanh toán bị gửi lặp | Không ghi nhận thanh toán hai lần |
| Học sinh chưa có enrollment hợp lệ truy cập Meeting URL | Từ chối |
| Học sinh truy cập enrollment của người khác | Từ chối |
| Gia sư sửa Lesson thuộc Course của gia sư khác | Từ chối |
| Hai Lesson cùng Course có `lessonOrder` trùng nhau | Từ chối nếu nghiệp vụ yêu cầu thứ tự duy nhất |

### 6.4. Quy Chuẩn Kiểm Thử Phân Quyền

Mọi endpoint liên quan đến Booking, Conversation, Message, Course, Lesson và CourseEnrollment phải được kiểm tra theo cả hai cấp:

1. **Role-based authorization:** Kiểm tra role phù hợp, chẳng hạn `STUDENT`, `TUTOR`, `ADMIN`.
2. **Resource-level authorization:** Kiểm tra quyền trên tài nguyên cụ thể, chẳng hạn chủ sở hữu Course, người tham gia Conversation hoặc chủ sở hữu Enrollment.

Chỉ sử dụng `@PreAuthorize("hasRole(...)")` là chưa đủ khi quyền phụ thuộc vào quan hệ giữa người dùng và tài nguyên.

Agent phải kiểm tra:

- Người dùng chưa xác thực.
- Người dùng có role không phù hợp.
- Người dùng đúng role nhưng không sở hữu tài nguyên.
- Người dùng có quyền hợp lệ.
- ID không tồn tại.
- ID có định dạng không hợp lệ.
- Tài nguyên đã bị hủy, vô hiệu hóa hoặc không còn cho phép thao tác.

Không được trả thông tin nhạy cảm từ Entity trực tiếp nếu Response DTO không được thiết kế cho mục đích đó.

### 6.5. Phân Loại Test Bắt Buộc

#### 6.5.1. Unit Test

Dùng JUnit 5 và Mockito để kiểm tra Service:

- Trường hợp thành công.
- Trường hợp dữ liệu không hợp lệ.
- Trường hợp không tìm thấy Entity.
- Trường hợp không đủ quyền.
- Trường hợp sai trạng thái nghiệp vụ.
- Trường hợp repository trả về dữ liệu biên.
- Trường hợp phát sinh exception và transaction phải được xử lý đúng.

Unit Test không được phụ thuộc vào một PostgreSQL đang chạy thực tế.

#### 6.5.2. Integration Test

Kiểm tra sự phối hợp giữa Controller, Service, Repository, JPA mapping, validation và database.

Các luồng cần ưu tiên:

- Tạo Conversation và Booking.
- Gửi Message và kiểm tra quyền.
- Tạo Course và Lesson.
- Đăng ký Course và kiểm tra unique constraint.
- Xác nhận thanh toán và kích hoạt Enrollment.
- Truy cập Lesson bằng các role khác nhau.
- Rollback khi một bước ghi dữ liệu thất bại.

Ưu tiên PostgreSQL test database hoặc Testcontainers khi môi trường cho phép. Không mặc định H2 hoàn toàn tương đương PostgreSQL, đặc biệt với UUID, enum, constraint và native query.

#### 6.5.3. Controller và Security Test

Sử dụng `MockMvc` hoặc công cụ tương đương để kiểm tra:

- HTTP method và endpoint đúng.
- Request DTO validation.
- HTTP status và cấu trúc `ApiResponse`.
- Xác thực JWT và phân quyền.
- Người dùng không thể truy cập tài nguyên ngoài quyền của mình.

Với WebSocket/STOMP, cần kiểm tra CONNECT, SUBSCRIBE, SEND và quyền tham gia Conversation. Nếu test không thể chạy vì thiếu hạ tầng, phải báo rõ thay vì tuyên bố đã kiểm thử thành công.

### 6.6. Quy Chuẩn Transaction và Tính Nhất Quán

Agent phải kiểm tra các thao tác nhiều bước liên quan đến nhiều bảng:

- Tạo Booking và các bản ghi tài chính liên quan.
- Tạo PaymentTransaction và kích hoạt CourseEnrollment.
- Ghi nhận Message và phát sự kiện sau khi lưu thành công.
- Cập nhật trạng thái nghiệp vụ kèm các bản ghi liên quan.

Yêu cầu:

- Sử dụng `@Transactional` phù hợp tại Service.
- Không phát thông báo thành công trước khi thao tác lưu dữ liệu thành công.
- Không để một phần dữ liệu được ghi khi nghiệp vụ yêu cầu toàn bộ thao tác phải thành công hoặc rollback.
- Xem xét race condition khi có request đồng thời.
- Dùng unique constraint hoặc cơ chế đồng bộ phù hợp để bảo vệ các quy tắc không thể chỉ dựa vào kiểm tra Java.
- Với thanh toán và webhook, bảo đảm tính idempotent.

### 6.7. Quy Trình Chạy Test

Agent phải xem xét các script, profile và cấu hình hiện có trước khi chạy lệnh.

Thứ tự thực hiện:

```bash
mvn spotless:apply
mvn test-compile
mvn test
```

Nếu dự án đã có profile hoặc cấu hình riêng cho integration test, phải sử dụng đúng cấu hình đó.

Quy tắc:

- `mvn test-compile` chỉ chứng minh code và test có thể biên dịch; không chứng minh nghiệp vụ đúng.
- `mvn test` phải được chạy để thực thi test tự động.
- Nếu test cần database, phải xác minh database/profile đã sẵn sàng.
- Không tự ý xóa hoặc sửa test chỉ để làm build pass.
- Không được đánh dấu test thành công nếu test bị skip, không chạy được hoặc không thực sự kiểm tra hành vi cần xác minh.
- Nếu một test thất bại, phải xác định nguyên nhân và báo cáo; không che giấu lỗi bằng cách vô hiệu hóa test.

### 6.8. Tiêu Chí Nghiệm Thu Module

Chỉ kết luận module đạt yêu cầu khi:

- [ ] Code tuân thủ kiến trúc phân tầng và quy chuẩn đặt tên.
- [ ] DTO validation và Response DTO phù hợp.
- [ ] Service kiểm tra đúng quy tắc nghiệp vụ.
- [ ] Phân quyền theo role và quyền sở hữu tài nguyên hoạt động đúng.
- [ ] Entity mapping và database constraints nhất quán.
- [ ] Transaction, rollback và các trường hợp đồng thời quan trọng được xem xét.
- [ ] Có Unit Test cho các nhánh nghiệp vụ quan trọng.
- [ ] Có Integration Test cho các luồng chính khi môi trường cho phép.
- [ ] Có test xác minh quyền truy cập trái phép.
- [ ] `mvn spotless:apply`, `mvn test-compile` và `mvn test` hoàn tất thành công, hoặc mọi giới hạn môi trường được ghi rõ.
- [ ] Không còn lỗi nghiêm trọng hoặc lỗi nghiệp vụ chưa được giải quyết.

### 6.9. Mẫu Báo Cáo Sau Khi Agent Kiểm Thử

Sau mỗi lần kiểm tra module, Agent phải báo cáo theo mẫu:

1. **Phạm vi:** Các class, endpoint và luồng nghiệp vụ đã kiểm tra.
2. **Phát hiện:** Lỗi nghiệp vụ, lỗi bảo mật, lỗi mapping hoặc thiếu test.
3. **Đã sửa:** Các file và hành vi đã thay đổi.
4. **Test đã chạy:** Tên test, số test thành công/thất bại/bị bỏ qua.
5. **Kết quả build:** Kết quả từng lệnh kiểm tra.
6. **Còn tồn tại:** Các lỗi hoặc trường hợp chưa được kiểm tra.
7. **Kết luận:** Đạt, chưa đạt hoặc chưa đủ dữ liệu để kết luận.

Không được kết luận “module hoàn toàn đúng” chỉ dựa trên compile thành công hoặc một vài test đơn giản.