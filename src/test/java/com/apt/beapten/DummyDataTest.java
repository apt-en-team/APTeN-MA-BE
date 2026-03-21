package com.apt.beapten;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 더미데이터 삽입 테스트
 * - @Transactional 없음 → 실행 후 DB에 영구 저장됨
 * - 한 번만 실행할 것 (중복 실행 시 오류 발생)
 * - 재실행이 필요하면 step1_truncate() 먼저 실행 후 step2_insertAll() 실행
 *
 * 계정 정보
 * 관리자  : admin@gmail.com   / admin1234
 * 입주민  : resident1@apt.com ~ resident14@apt.com / test1234
 * 승인대기: pending1@apt.com  / test1234
 *
 * 세대 구성: 101동~109동 / 1층~15층 / 1호~10호 = 총 1350세대
 */
@SpringBootTest
class DummyDataTest {

    @Autowired
    private JdbcTemplate db;

    // 전체 데이터 삭제 후 재삽입이 필요할 때만 실행
    @Test
    void step1_truncate() {
        db.execute("SET FOREIGN_KEY_CHECKS = 0");
        db.execute("TRUNCATE TABLE comment");
        db.execute("TRUNCATE TABLE board_image");
        db.execute("TRUNCATE TABLE board");
        db.execute("TRUNCATE TABLE reservation");
        db.execute("TRUNCATE TABLE gx_program");
        db.execute("TRUNCATE TABLE parking_log");
        db.execute("TRUNCATE TABLE fixed_visitor_vehicle");
        db.execute("TRUNCATE TABLE visitor_vehicle");
        db.execute("TRUNCATE TABLE vehicle");
        db.execute("TRUNCATE TABLE household_history");
        db.execute("TRUNCATE TABLE refresh_token");
        db.execute("TRUNCATE TABLE password_reset_token");
        db.execute("TRUNCATE TABLE user");
        db.execute("TRUNCATE TABLE facility");
        db.execute("TRUNCATE TABLE facility_type");
        db.execute("TRUNCATE TABLE parking_lot");
        db.execute("TRUNCATE TABLE household");
        db.execute("SET FOREIGN_KEY_CHECKS = 1");
        System.out.println("✅ 전체 테이블 초기화 완료");
    }

    // 더미데이터 전체 삽입
    @Test
    void step2_insertAll() {
        insertParkingLot();
        insertFacilityType();
        insertFacility();
        insertHousehold();
        insertUser();
        insertHouseholdHistory();
        insertGxProgram();
        insertVehicle();
        insertVisitorVehicle();
        insertFixedVisitorVehicle();
        insertParkingLog();
        insertReservation();
        insertBoard();
        insertComment();
        System.out.println("✅ 더미데이터 전체 삽입 완료");
    }

    // 1. 주차장
    void insertParkingLot() {
        db.execute("INSERT INTO parking_lot (name, total_spaces) VALUES ('본관 주차장', 100)");
    }

    // 2. 시설 타입
    void insertFacilityType() {
        db.execute("""
   INSERT INTO facility_type (name, description, category) VALUES
   ('독서실',     '독서실 좌석 12석',          '편의시설'),
   ('헬스장',     '피트니스 센터',              '편의시설'),
   ('골프연습장', '스크린 골프 연습장 5타석',    '편의시설'),
   ('GX',         'GX 그룹 운동 프로그램',       'GX프로그램')
  """);
    }

    // 3. 시설 (facility_id 1~8)
    void insertFacility() {
        db.execute("""
   INSERT INTO facility (type_id, name, description, max_capacity, price, open_time, close_time, slot_duration, is_active) VALUES
   (1, '독서실(남)',        '남성 전용 독서실, 좌석 12석',         12,     0, '05:00:00', '23:00:00', 1080, 1),
   (1, '독서실(여)',        '여성 전용 독서실, 좌석 12석',         12,     0, '05:00:00', '23:00:00', 1080, 1),
   (2, '헬스장',            '공용 헬스장',                        999,    0, '04:55:00', '23:00:00', 1085, 1),
   (3, '골프연습장',        '스크린 골프 5타석, 1시간 단위 예약',   5,     0, '06:00:00', '22:00:00',   60, 1),
   (4, 'GX-필라테스(오전)', 'GX 필라테스 오전 | 매주 월·수·금',   15, 50000, '10:00:00', '11:00:00',   60, 1),
   (4, 'GX-필라테스(오후)', 'GX 필라테스 오후 | 매주 월·수·금',   15, 50000, '20:00:00', '21:00:00',   60, 1),
   (4, 'GX-그룹PT(오전)',   'GX 그룹PT 오전 | 매주 화·목',        10, 60000, '10:00:00', '11:00:00',   60, 1),
   (4, 'GX-그룹PT(오후)',   'GX 그룹PT 오후 | 매주 화·목',        10, 60000, '20:00:00', '21:00:00',   60, 1)
  """);
    }

    // 4. 세대 (101동~109동 / 1층~15층 / 1호~10호 = 1350세대)
    void insertHousehold() {
        for (int dong = 101; dong <= 109; dong++) {
            for (int floor = 1; floor <= 15; floor++) {
                for (int ho = 1; ho <= 10; ho++) {
                    String hosu = (floor * 100 + ho) + "호";
                    db.update("INSERT INTO household (dong, ho) VALUES (?, ?)", dong + "동", hosu);
                }
            }
        }
        System.out.println("✅ 세대 1350개 삽입 완료");
    }

    // 5. 유저
    // user_id=1    : 관리자 (admin1234)
    // user_id=2~15 : 입주민 14명 (test1234)
    // user_id=16   : 승인대기 1명 (test1234)
    void insertUser() {
        db.execute("""
   INSERT INTO user (email, password, name, phone, role, status, provider, is_deleted) VALUES
   ('admin@gmail.com', '$2a$10$Nvr64lsMpaOnIOM64fo0ietsqTuExYlo0QZlFXjWiErpBG9w6cxgW', '관리자', '010-0000-0000', 'ADMIN', 'APPROVED', 'LOCAL', 0)
  """);
        db.execute("""
   INSERT INTO user (household_id, email, password, name, phone, role, status, provider, is_deleted) VALUES
   (1,  'resident1@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '김민수', '010-1000-0001', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (2,  'resident2@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '이서연', '010-1000-0002', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (3,  'resident3@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '박지훈', '010-1000-0003', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (4,  'resident4@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '최유진', '010-1000-0004', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (5,  'resident5@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '정다은', '010-1000-0005', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (6,  'resident6@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '김하늘', '010-1000-0006', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (7,  'resident7@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '오지훈', '010-1000-0007', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (8,  'resident8@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '박소연', '010-1000-0008', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (9,  'resident9@apt.com',  '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '이정훈', '010-1000-0009', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (10, 'resident10@apt.com', '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '한지민', '010-1000-0010', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (11, 'resident11@apt.com', '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '윤서준', '010-1000-0011', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (12, 'resident12@apt.com', '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '강다혜', '010-1000-0012', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (13, 'resident13@apt.com', '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '조민준', '010-1000-0013', 'RESIDENT', 'APPROVED', 'LOCAL', 0),
   (14, 'resident14@apt.com', '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '신유리', '010-1000-0014', 'RESIDENT', 'APPROVED', 'LOCAL', 0)
  """);
        db.execute("""
   INSERT INTO user (email, password, name, phone, role, status, provider, is_deleted) VALUES
   ('pending1@apt.com', '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y', '홍길동', '010-2000-0001', 'RESIDENT', 'PENDING', 'LOCAL', 0)
  """);
    }

    // 6. 세대 입주 이력
    void insertHouseholdHistory() {
        db.execute("""
   INSERT INTO household_history (household_id, user_id, status, changed_at)
   SELECT h.household_id, u.user_id, '입주', '2026-01-01 00:00:00'
   FROM user u
   JOIN household h ON h.household_id = u.household_id
   WHERE u.role = 'RESIDENT' AND u.status = 'APPROVED'
  """);
    }

    // 7. GX 프로그램 (3월 + 4월)
    void insertGxProgram() {
        db.execute("""
   INSERT INTO gx_program (facility_id, start_date, end_date, days_of_week, status) VALUES
   (5, '2026-03-01', '2026-03-31', 'MON,WED,FRI', 'OPEN'),
   (6, '2026-03-01', '2026-03-31', 'MON,WED,FRI', 'OPEN'),
   (7, '2026-03-01', '2026-03-31', 'TUE,THU',     'CLOSED'),
   (8, '2026-03-01', '2026-03-31', 'TUE,THU',     'OPEN'),
   (5, '2026-04-01', '2026-04-30', 'MON,WED,FRI', 'OPEN'),
   (6, '2026-04-01', '2026-04-30', 'MON,WED,FRI', 'OPEN'),
   (7, '2026-04-01', '2026-04-30', 'TUE,THU',     'OPEN'),
   (8, '2026-04-01', '2026-04-30', 'TUE,THU',     'OPEN')
  """);
    }

    // 8. 입주민 차량
    // vehicle_id 1~2  : user2 김민수 (APPROVED 2대)
    // vehicle_id 3    : user3 이서연 (APPROVED 1대)
    // vehicle_id 4~5  : user4 박지훈 (APPROVED 2대)
    // vehicle_id 6    : user5 최유진 (APPROVED 1대)
    // vehicle_id 7~8  : user6 정다은 (APPROVED 2대)
    // vehicle_id 9    : user7 오지훈 (APPROVED 1대)
    // vehicle_id 10~11: user8 박소연 (APPROVED 2대)
    // vehicle_id 12   : user9 이정훈 (APPROVED 1대)
    // vehicle_id 13~14: user10 한지민 (APPROVED 2대)
    // vehicle_id 15   : user11 윤서준 (APPROVED 1대)
    // vehicle_id 16~17: user12 강다혜 (APPROVED 2대)
    // vehicle_id 18   : user13 조민준 (APPROVED 1대)
    // vehicle_id 19~20: user14 신유리 (APPROVED 1대 + PENDING 1대)
    // vehicle_id 21   : user15 (PENDING 1대)
    void insertVehicle() {
        db.execute("""
   INSERT INTO vehicle (user_id, household_id, license_plate, car_model, car_type, status, approved_by, approved_at) VALUES
   (2,  1,  '12가3456', '현대 소나타',           '승용차', 'APPROVED', 1, NOW()),
   (2,  1,  '12나1111', '기아 레이',             '경차',   'APPROVED', 1, NOW()),
   (3,  2,  '34나5678', 'BMW 5시리즈',           '승용차', 'APPROVED', 1, NOW()),
   (4,  3,  '56다7890', '기아 스포티지',          'SUV',    'APPROVED', 1, NOW()),
   (4,  3,  '56라2222', '현대 아반떼',           '승용차', 'APPROVED', 1, NOW()),
   (5,  4,  '78라1234', '현대 그랜저',           '승용차', 'APPROVED', 1, NOW()),
   (6,  5,  '90마5678', '쌍용 티볼리',           'SUV',    'APPROVED', 1, NOW()),
   (6,  5,  '90바3333', '기아 모닝',             '경차',   'APPROVED', 1, NOW()),
   (7,  6,  '11바9012', '기아 카니발',           '승합차', 'APPROVED', 1, NOW()),
   (8,  7,  '22사1234', '현대 투싼',             'SUV',    'APPROVED', 1, NOW()),
   (8,  7,  '22아4444', '테슬라 모델3',          '승용차', 'APPROVED', 1, NOW()),
   (9,  8,  '33자5678', '기아 K5',               '승용차', 'APPROVED', 1, NOW()),
   (10, 9,  '44차9012', '현대 팰리세이드',        'SUV',    'APPROVED', 1, NOW()),
   (10, 9,  '44카5555', '현대 캐스퍼',           '경차',   'APPROVED', 1, NOW()),
   (11, 10, '55타1234', '기아 EV6',              'SUV',    'APPROVED', 1, NOW()),
   (12, 11, '66파5678', '쉐보레 트레일블레이저',  'SUV',    'APPROVED', 1, NOW()),
   (12, 11, '66하6666', '현대 i30',              '승용차', 'APPROVED', 1, NOW()),
   (13, 12, '77거9012', '기아 니로',             'SUV',    'APPROVED', 1, NOW()),
   (14, 13, '88너1234', '현대 베뉴',             'SUV',    'APPROVED', 1, NOW()),
   (14, 13, '88더7777', '기아 K3',               '승용차', 'PENDING',  NULL, NULL),
   (15, 14, '99러5678', '르노 QM6',              'SUV',    'PENDING',  NULL, NULL)
  """);
    }

    // 9. 방문차량
    // visitor_vehicle_id 1~5
    void insertVisitorVehicle() {
        db.execute("""
   INSERT INTO visitor_vehicle (user_id, license_plate, visitor_name, visit_purpose, visit_date, status) VALUES
   (2, '00가1111', '홍길순', '가족 방문', CURDATE(),                           'APPROVED'),
   (3, '00나2222', '이택배', '택배 배달', CURDATE(),                           'APPROVED'),
   (4, '00다3333', '김방문', '친구 방문', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'APPROVED'),
   (5, '00라4444', '박수리', '수리 방문', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'APPROVED'),
   (2, '00마5555', '최과거', '지난 방문', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'APPROVED')
  """);
    }

    // 10. 고정 방문차량
    // fixed_visitor_vehicle_id 1~3
    void insertFixedVisitorVehicle() {
        db.execute("""
   INSERT INTO fixed_visitor_vehicle (user_id, vehicle_number, visitor_name, purpose, start_date, end_date) VALUES
   (2, '11가2222', '김택배', '정기 택배',     DATE_SUB(CURDATE(), INTERVAL  7 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY)),
   (3, '33나4444', '이가족', '가족 상시방문',  DATE_SUB(CURDATE(), INTERVAL 30 DAY), NULL),
   (4, '55다6666', '박청소', '정기 청소',     DATE_SUB(CURDATE(), INTERVAL  3 DAY), DATE_ADD(CURDATE(), INTERVAL 60 DAY))
  """);
    }

    // 11. 주차 로그
    // 등록차량 / 방문차량 / 고정방문차량 / 미등록차량 골고루
    // 오늘은 일부 OUT 없음 → 현재 주차중 표시용
    // 과거 30일 + 미래 30일 데이터로 그래프 확인 가능
    void insertParkingLog() {

        // ── 과거 30일 (하루 10~15건씩, 4가지 유형 골고루) ──
        String[] pastDates = {
                "2026-02-20","2026-02-21","2026-02-22","2026-02-23","2026-02-24",
                "2026-02-25","2026-02-26","2026-02-27","2026-02-28","2026-03-01",
                "2026-03-02","2026-03-03","2026-03-04","2026-03-05","2026-03-06",
                "2026-03-07","2026-03-08","2026-03-09","2026-03-10","2026-03-11",
                "2026-03-12","2026-03-13","2026-03-14","2026-03-15","2026-03-16",
                "2026-03-17","2026-03-18","2026-03-19","2026-03-20","2026-03-21"
        };

        for (int idx = 0; idx < pastDates.length; idx++) {
            String d = pastDates[idx];

            // 등록차량 (vehicle_id 1~6 돌아가며)
            int v1 = (idx % 6) + 1;
            int v2 = (idx % 6) + 2 > 6 ? 1 : (idx % 6) + 2;
            String[] plates = {"12가3456","12나1111","34나5678","56다7890","78라1234","90마5678"};
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'IN', ?)",  v1, plates[v1-1], d+" 08:00:00");
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'OUT',?)",  v1, plates[v1-1], d+" 18:30:00");
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'IN', ?)",  v2, plates[v2-1], d+" 09:00:00");
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'OUT',?)",  v2, plates[v2-1], d+" 19:00:00");

            // 방문차량 (visitor_vehicle_id 1~2 교대)
            int vv = (idx % 2) + 1;
            String[] vplates = {"00가1111","00나2222"};
            db.update("INSERT INTO parking_log (lot_id, visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'IN', ?)",  vv, vplates[vv-1], d+" 10:00:00");
            db.update("INSERT INTO parking_log (lot_id, visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'OUT',?)",  vv, vplates[vv-1], d+" 14:00:00");

            // 고정방문차량 (fixed_id 1~3 돌아가며)
            int fv = (idx % 3) + 1;
            String[] fplates = {"11가2222","33나4444","55다6666"};
            db.update("INSERT INTO parking_log (lot_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'IN', ?)",  fv, fplates[fv-1], d+" 09:00:00");
            db.update("INSERT INTO parking_log (lot_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,?,?,'OUT',?)",  fv, fplates[fv-1], d+" 17:00:00");

            // 미등록차량 (vehicle_id/visitor 모두 NULL)
            String unregPlate = String.format("미등%03d호", idx + 1);
            db.update("INSERT INTO parking_log (lot_id, license_plate, entry_type, logged_at) VALUES (1,?,'IN', ?)",  unregPlate, d+" 11:00:00");
            db.update("INSERT INTO parking_log (lot_id, license_plate, entry_type, logged_at) VALUES (1,?,'OUT',?)",  unregPlate, d+" 13:00:00");
        }

        // ── 오늘 IN/OUT 완료 건들 ──
        db.execute("""
   INSERT INTO parking_log (lot_id, vehicle_id, visitor_vehicle_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES
   (1, 3, NULL, NULL, '34나5678', 'IN',  '2026-03-21 08:30:00'),
   (1, 3, NULL, NULL, '34나5678', 'OUT', '2026-03-21 12:10:00'),
   (1, 4, NULL, NULL, '56다7890', 'IN',  '2026-03-21 09:15:00'),
   (1, 4, NULL, NULL, '56다7890', 'OUT', '2026-03-21 15:40:00'),
   (1, NULL, 1,  NULL, '00가1111', 'IN',  '2026-03-21 10:05:00'),
   (1, NULL, 1,  NULL, '00가1111', 'OUT', '2026-03-21 12:20:00'),
   (1, NULL, 2,  NULL, '00나2222', 'IN',  '2026-03-21 14:00:00'),
   (1, NULL, 2,  NULL, '00나2222', 'OUT', '2026-03-21 14:40:00'),
   (1, NULL, NULL, 2,  '33나4444', 'IN',  '2026-03-21 11:00:00'),
   (1, NULL, NULL, 2,  '33나4444', 'OUT', '2026-03-21 16:30:00'),
   (1, NULL, NULL, 3,  '55다6666', 'IN',  '2026-03-21 08:00:00'),
   (1, NULL, NULL, 3,  '55다6666', 'OUT', '2026-03-21 13:00:00'),
   (1, NULL, NULL, NULL, '미등999호', 'IN',  '2026-03-21 10:30:00'),
   (1, NULL, NULL, NULL, '미등999호', 'OUT', '2026-03-21 11:30:00')
  """);

        // ── 오늘 현재 주차중 (OUT 없음) ─ 4가지 유형 골고루 ──
        db.execute("""
   INSERT INTO parking_log (lot_id, vehicle_id, visitor_vehicle_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES
   (1, 1,    NULL, NULL, '12가3456', 'IN', '2026-03-21 08:05:00'),
   (1, 6,    NULL, NULL, '90마5678', 'IN', '2026-03-21 09:00:00'),
   (1, 9,    NULL, NULL, '11바9012', 'IN', '2026-03-21 09:30:00'),
   (1, 10,   NULL, NULL, '22사1234', 'IN', '2026-03-21 10:00:00'),
   (1, NULL, 3,    NULL, '00다3333', 'IN', '2026-03-21 13:00:00'),
   (1, NULL, 4,    NULL, '00라4444', 'IN', '2026-03-21 15:00:00'),
   (1, NULL, NULL, 1,    '11가2222', 'IN', '2026-03-21 09:10:00'),
   (1, NULL, NULL, NULL, '미등100호', 'IN', '2026-03-21 14:00:00'),
   (1, NULL, NULL, NULL, '미등101호', 'IN', '2026-03-21 16:00:00')
  """);

        // ── 미래 30일 (하루 8~12건씩, 4가지 유형 골고루, 전부 IN/OUT 쌍) ──
        for (int i = 1; i <= 30; i++) {
            int totalDay = 21 + i;
            String month = totalDay > 31 ? "04" : "03";
            int day = totalDay > 31 ? totalDay - 31 : totalDay;
            String date = String.format("2026-%s-%02d", month, day);

            // 등록차량 (매일 2~3대)
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,1,'12가3456','IN', ?)",  date+" 08:00:00");
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,1,'12가3456','OUT',?)",  date+" 18:30:00");
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,5,'78라1234','IN', ?)",  date+" 09:00:00");
            db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,5,'78라1234','OUT',?)",  date+" 18:00:00");
            if (i % 2 == 0) {
                db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,7,'90마5678','IN', ?)",  date+" 10:00:00");
                db.update("INSERT INTO parking_log (lot_id, vehicle_id, license_plate, entry_type, logged_at) VALUES (1,7,'90마5678','OUT',?)",  date+" 17:00:00");
            }

            // 방문차량 (격일)
            if (i % 2 == 1) {
                db.update("INSERT INTO parking_log (lot_id, visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,1,'00가1111','IN', ?)",  date+" 10:00:00");
                db.update("INSERT INTO parking_log (lot_id, visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,1,'00가1111','OUT',?)",  date+" 14:00:00");
            } else {
                db.update("INSERT INTO parking_log (lot_id, visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,2,'00나2222','IN', ?)",  date+" 11:00:00");
                db.update("INSERT INTO parking_log (lot_id, visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,2,'00나2222','OUT',?)",  date+" 15:00:00");
            }

            // 고정방문차량 (매일 1~2대)
            db.update("INSERT INTO parking_log (lot_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,1,'11가2222','IN', ?)",  date+" 09:00:00");
            db.update("INSERT INTO parking_log (lot_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,1,'11가2222','OUT',?)",  date+" 17:30:00");
            if (i % 3 != 0) {
                db.update("INSERT INTO parking_log (lot_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,2,'33나4444','IN', ?)",  date+" 10:00:00");
                db.update("INSERT INTO parking_log (lot_id, fixed_visitor_vehicle_id, license_plate, entry_type, logged_at) VALUES (1,2,'33나4444','OUT',?)",  date+" 16:00:00");
            }

            // 미등록차량 (주 3회)
            if (i % 3 == 1) {
                String unregPlate = String.format("미래%03d호", i);
                db.update("INSERT INTO parking_log (lot_id, license_plate, entry_type, logged_at) VALUES (1,?,'IN', ?)",  unregPlate, date+" 11:00:00");
                db.update("INSERT INTO parking_log (lot_id, license_plate, entry_type, logged_at) VALUES (1,?,'OUT',?)",  unregPlate, date+" 13:00:00");
            }
        }
        System.out.println("✅ 주차 로그 삽입 완료");
    }

    // 12. 예약
    void insertReservation() {
        // 독서실(남) 오늘 3석
        db.execute("""
   INSERT INTO reservation (user_id, facility_id, reservation_date, start_time, end_time, seat_no, status) VALUES
   (2, 1, CURDATE(), '05:00:00', '23:00:00', 1, 'CONFIRMED'),
   (3, 1, CURDATE(), '05:00:00', '23:00:00', 2, 'CONFIRMED'),
   (4, 1, CURDATE(), '05:00:00', '23:00:00', 3, 'CONFIRMED')
  """);
        // 헬스장 오늘 3명 CONFIRMED, 1명 CANCELLED
        db.execute("""
   INSERT INTO reservation (user_id, facility_id, reservation_date, start_time, end_time, seat_no, status) VALUES
   (2, 3, CURDATE(), '04:55:00', '23:00:00', NULL, 'CONFIRMED'),
   (3, 3, CURDATE(), '04:55:00', '23:00:00', NULL, 'CONFIRMED'),
   (4, 3, CURDATE(), '04:55:00', '23:00:00', NULL, 'CONFIRMED'),
   (5, 3, CURDATE(), '04:55:00', '23:00:00', NULL, 'CANCELLED')
  """);
        // 헬스장 내일 2명
        db.execute("""
   INSERT INTO reservation (user_id, facility_id, reservation_date, start_time, end_time, seat_no, status) VALUES
   (6, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '04:55:00', '23:00:00', NULL, 'CONFIRMED'),
   (7, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '04:55:00', '23:00:00', NULL, 'CONFIRMED')
  """);
        // 골프연습장 오늘 06시 2타석, 10시 만석
        db.execute("""
   INSERT INTO reservation (user_id, facility_id, reservation_date, start_time, end_time, seat_no, status) VALUES
   (2, 4, CURDATE(), '06:00:00', '07:00:00', 1, 'CONFIRMED'),
   (3, 4, CURDATE(), '06:00:00', '07:00:00', 2, 'CONFIRMED'),
   (4, 4, CURDATE(), '10:00:00', '11:00:00', 1, 'CONFIRMED'),
   (5, 4, CURDATE(), '10:00:00', '11:00:00', 2, 'CONFIRMED'),
   (6, 4, CURDATE(), '10:00:00', '11:00:00', 3, 'CONFIRMED'),
   (7, 4, CURDATE(), '10:00:00', '11:00:00', 4, 'CONFIRMED'),
   (8, 4, CURDATE(), '10:00:00', '11:00:00', 5, 'CONFIRMED')
  """);
        // GX-필라테스(오후) 내일
        db.execute("""
   INSERT INTO reservation (user_id, facility_id, program_id, reservation_date, start_time, end_time, seat_no, status) VALUES
   (2, 6, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '20:00:00', '21:00:00', NULL, 'CONFIRMED'),
   (3, 6, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '20:00:00', '21:00:00', NULL, 'CONFIRMED'),
   (4, 6, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '20:00:00', '21:00:00', NULL, 'CANCELLED')
  """);
        // GX-그룹PT(오후) 모레
        db.execute("""
   INSERT INTO reservation (user_id, facility_id, program_id, reservation_date, start_time, end_time, seat_no, status) VALUES
   (5, 8, 4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '20:00:00', '21:00:00', NULL, 'CONFIRMED'),
   (6, 8, 4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '20:00:00', '21:00:00', NULL, 'CONFIRMED')
  """);
    }

    // 13. 게시글
    void insertBoard() {
        db.execute("""
   INSERT INTO board (user_id, category, title, content, view_count) VALUES
   (1, 'NOTICE', '[공지] 3월 시설 정기점검 안내',   '3월 20일(금) 오전 10시~12시 헬스장 정기점검이 진행됩니다.', 45),
   (1, 'NOTICE', '[공지] 주차장 도색 공사 안내',     '3월 25일(수)~26일(목) 주차장 바닥 도색 공사가 진행됩니다.', 30),
   (1, 'NOTICE', '[공지] GX 4월 프로그램 모집 안내', '4월 GX 프로그램 신청이 시작되었습니다. 앱에서 신청하세요.', 88),
   (2, 'FREE',   '헬스장 덤벨 추가 요청합니다',       '30kg 이상 덤벨이 없어서 불편합니다. 추가 구매 부탁드려요.', 12),
   (3, 'FREE',   '골프연습장 이용 후기',              '시설이 깔끔하고 좋았습니다! 다음에 또 이용할게요.',          8),
   (4, 'FREE',   '독서실 이용 팁 공유',               '05시 예약 열리자마자 신청하면 자리 잡기 쉬워요.',            25),
   (5, 'INQUIRY','GX 환불 문의',                      '개인 사정으로 3월 수업을 못 듣게 됐는데 환불이 가능한가요?', 3)
  """);
    }

    // 14. 댓글
    void insertComment() {
        db.execute("""
   INSERT INTO comment (board_id, user_id, content) VALUES
   (1, 2, '안내 감사합니다. 점검 잘 부탁드려요!'),
   (1, 3, '확인했습니다.'),
   (4, 1, '덤벨 구매 검토해보겠습니다. 의견 감사합니다.'),
   (4, 5, '저도 같은 생각이에요! 추가 구매 부탁드려요.'),
   (7, 1, '환불 관련해서는 관리실로 직접 문의 부탁드립니다.')
  """);
    }
}