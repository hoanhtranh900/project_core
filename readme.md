java -Xms512m -Xmx2048m -jar xxx.jar <br/>
-Xms chỉ định bộ nhớ tối thiểu cho ứng dụng <br/>
-Xmx chỉ định bộ nhớ tối đa cho ứng dụng <br/>

**prometheus <br/>**
http://ip:port/actuator <br/>
http://ip:port/actuator/health <br/>
http://ip:port/actuator/prometheus <br/>



**Clean code** <br/>
app-base: Rest API <br/>
app-ckfinder: Trình editor <br/>
app-core: JPA, Utils, Model, Entity, Constants, Config, ... <br/>
app-quartz: Lập lịch JOB <br/>
app-service: Logic JPA, Logic nghiệp vụ (Srs...) <br/>



1. Sử dụng constant <br/>
Luôn sử dụng hằng số ở package com.osp.core.contants <br/>
cách tạo interface contant: tên_bảng_tên_cột <br/>
Logic nghiệp vụ tạo service Srs... <br/>
Thêm service sửa api /public/test <br/>
Khởi tạo dữ liệu ban đầu: com.osp.core.config.BeanInitDatabase <br/>
com.osp.core.repository Sử dụng Optional nếu trả ra 1 Object <br/>
com.osp.quartz.config startQuartzJob khởi tạo lập lịch JOB khi bắt đầu chạy project <br/>
com.osp.service Logic JPA, Logic nghiệp vụ (Srs...) bắt buộc phải comment (SrsSystemSercice) <br/>



**Spring data JPA <br/>**
Link tham khảo: https://www.baeldung.com/jpa-join-types <br/>
OneToMany bắt buộc sử dụng mappedBy <br/>
ManyToOne sử dụng JoinColumn <br/>
OneToOne sử dụng JoinColumn hoặc mappedBy <br/>
ManyToMany sử dụng JoinColumn hoặc mappedBy <br/>
<br/>
com.osp.core.entity.only là package view không có liên kết bảng <br/>
com.osp.core.entity.view là package view cho các màn danh sách hoặc chi tiết <br/>
<br/>
2. Cách tạo View bằng anotation <br/>
Sử dụng Subselect <br/>
@Builder <br/>
@Getter <br/>
@Setter <br/>
@NoArgsConstructor <br/>
@AllArgsConstructor <br/>
@Entity <br/>
@Subselect("select * from T_ADM_USERS") <br/>
public class AdmUserView extends Auditable implements Serializable {} <br/>



3. Cách viết query <br/>
@Entity <br/>
public class Employee { <br/>
   @Id <br/>
   @GeneratedValue(strategy = GenerationType.IDENTITY) <br/>
   private long id; <br/>
   private String name; <br/>
   private int age; <br/>
   @ManyToOne <br/>
   private Department department; <br/>
   @OneToMany(mappedBy = "employee") <br/>
   private List<Phone> phones; <br/>
} <br/> <br/>
@Entity <br/>
public class Department { <br/>
   @Id <br/>
   @GeneratedValue(strategy = GenerationType.AUTO) <br/>
   private long id; <br/>
   private String name; <br/>
   @OneToMany(mappedBy = "department") <br/>
   private List<Employee> employees; <br/>
} <br/> <br/>
@Entity <br/>
public class Phone { <br/>
   @Id <br/>
   @GeneratedValue(strategy = GenerationType.IDENTITY) <br/>
   private long id; <br/>
   private String number; <br/>
   @ManyToOne <br/>
   private Employee employee; <br/>
} <br/> <br/>
**Implicit Inner Join With Single-Valued Association Navigation  <br/>**
SELECT e.department FROM Employee e  <br/>
**Explicit Inner Join With Single-Valued Association <br/>**
SELECT d FROM Employee e JOIN e.department d   <br/>
SELECT d FROM Employee e INNER JOIN e.department d<br/>
**Explicit Inner Join With Collection-Valued Associations <br/>**
SELECT e.phones FROM Employee e <br/>
SELECT ph FROM Employee e JOIN e.phones ph <br/>
**Outer Join** <br/>
SELECT DISTINCT d FROM Department d LEFT JOIN d.employees e <br/>
**Multiple Joins  <br/>**
SELECT ph FROM Employee e JOIN e.department d JOIN e.phones ph <br/>



4. Sử dụng JsonIgnore <br/>
Sử dụng ở các class Entity <br/>
JsonIgnore để không bị lỗi could not parse json <br/>
Lưu ý không để bất cứ trường hợp nào xảy ra lỗi could not parse json <br/>



5. Hạn chế tạo model  <br/>
Nên sử dụng HashMap, JSONObject để trả response data body <br/>
Sử dụng View để trả response data body <br/>
