-- APTeN MA demo dummy data final
-- local-test-data.sql 실행 후 추가 실행용
-- 로그인 계정:
--   admin@gmail.com / admin1234
--   resident1@apt.com / test1234

ROLLBACK;
SET SQL_SAFE_UPDATES = 0;

START TRANSACTION;

-- =========================================================
-- 0. 공통 기준값
-- =========================================================

SET @resident_password := '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y';

-- =========================================================
-- 1. household
-- =========================================================

INSERT INTO household (dong, ho, created_at)
SELECT s.dong, s.ho, NOW()
FROM (
         SELECT '101동' AS dong, '101호' AS ho UNION ALL
         SELECT '101동', '102호' UNION ALL
         SELECT '101동', '103호' UNION ALL
         SELECT '101동', '104호' UNION ALL
         SELECT '102동', '101호' UNION ALL
         SELECT '102동', '102호' UNION ALL
         SELECT '102동', '103호' UNION ALL
         SELECT '103동', '101호' UNION ALL
         SELECT '103동', '102호' UNION ALL
         SELECT '103동', '103호' UNION ALL
         SELECT '104동', '101호' UNION ALL
         SELECT '104동', '102호' UNION ALL
         SELECT '105동', '101호'
     ) s
WHERE NOT EXISTS (
    SELECT 1
    FROM household h
    WHERE h.dong = s.dong
      AND h.ho = s.ho
);

-- =========================================================
-- 2. user
-- =========================================================

-- 기존 테스트 입주민 resident1 세대 연결 보정
UPDATE `user` u
    JOIN household h ON h.dong = '101동' AND h.ho = '101호'
    SET u.household_id = h.household_id,
        u.password = @resident_password,
        u.name = '김민수',
        u.phone = '010-1000-0001',
        u.role = 'RESIDENT',
        u.status = 'APPROVED',
        u.provider = 'LOCAL',
        u.is_deleted = 0,
        u.updated_at = NOW()
WHERE u.email = 'resident1@apt.com';

-- 추가 입주민 생성
INSERT INTO `user` (
    household_id, email, password, name, phone,
    role, status, provider, is_deleted, created_at, updated_at
)
SELECT
    h.household_id,
    s.email,
    @resident_password,
    s.name,
    s.phone,
    'RESIDENT',
    s.status,
    'LOCAL',
    0,
    NOW(),
    NOW()
FROM (
         SELECT 'resident2@apt.com' AS email, '이서연' AS name, '010-1000-0002' AS phone, '101동' AS dong, '102호' AS ho, 'APPROVED' AS status UNION ALL
         SELECT 'resident3@apt.com', '박지훈', '010-1000-0003', '101동', '103호', 'APPROVED' UNION ALL
         SELECT 'resident4@apt.com', '최유진', '010-1000-0004', '101동', '104호', 'APPROVED' UNION ALL
         SELECT 'resident5@apt.com', '정다은', '010-1000-0005', '102동', '101호', 'APPROVED' UNION ALL
         SELECT 'resident6@apt.com', '김하늘', '010-1000-0006', '102동', '102호', 'APPROVED' UNION ALL
         SELECT 'resident7@apt.com', '오지훈', '010-1000-0007', '102동', '103호', 'APPROVED' UNION ALL
         SELECT 'resident8@apt.com', '박소연', '010-1000-0008', '103동', '101호', 'APPROVED' UNION ALL
         SELECT 'resident9@apt.com', '이정훈', '010-1000-0009', '103동', '102호', 'APPROVED' UNION ALL
         SELECT 'resident10@apt.com', '한지민', '010-1000-0010', '103동', '103호', 'APPROVED' UNION ALL
         SELECT 'resident11@apt.com', '윤서준', '010-1000-0011', '104동', '101호', 'APPROVED' UNION ALL
         SELECT 'resident12@apt.com', '강다혜', '010-1000-0012', '104동', '102호', 'APPROVED' UNION ALL
         SELECT 'pending1@apt.com', '조민준', '010-1000-0013', '105동', '101호', 'PENDING'
     ) s
         JOIN household h ON h.dong = s.dong AND h.ho = s.ho
WHERE NOT EXISTS (
    SELECT 1
    FROM `user` u
    WHERE u.email = s.email
);

-- 기존 입주민 있으면 최신 상태로 보정
UPDATE `user` u
    JOIN (
    SELECT 'resident2@apt.com' AS email, '이서연' AS name, '010-1000-0002' AS phone, '101동' AS dong, '102호' AS ho, 'APPROVED' AS status UNION ALL
    SELECT 'resident3@apt.com', '박지훈', '010-1000-0003', '101동', '103호', 'APPROVED' UNION ALL
    SELECT 'resident4@apt.com', '최유진', '010-1000-0004', '101동', '104호', 'APPROVED' UNION ALL
    SELECT 'resident5@apt.com', '정다은', '010-1000-0005', '102동', '101호', 'APPROVED' UNION ALL
    SELECT 'resident6@apt.com', '김하늘', '010-1000-0006', '102동', '102호', 'APPROVED' UNION ALL
    SELECT 'resident7@apt.com', '오지훈', '010-1000-0007', '102동', '103호', 'APPROVED' UNION ALL
    SELECT 'resident8@apt.com', '박소연', '010-1000-0008', '103동', '101호', 'APPROVED' UNION ALL
    SELECT 'resident9@apt.com', '이정훈', '010-1000-0009', '103동', '102호', 'APPROVED' UNION ALL
    SELECT 'resident10@apt.com', '한지민', '010-1000-0010', '103동', '103호', 'APPROVED' UNION ALL
    SELECT 'resident11@apt.com', '윤서준', '010-1000-0011', '104동', '101호', 'APPROVED' UNION ALL
    SELECT 'resident12@apt.com', '강다혜', '010-1000-0012', '104동', '102호', 'APPROVED' UNION ALL
    SELECT 'pending1@apt.com', '조민준', '010-1000-0013', '105동', '101호', 'PENDING'
    ) s ON s.email = u.email
    JOIN household h ON h.dong = s.dong AND h.ho = s.ho
    SET u.household_id = h.household_id,
        u.password = @resident_password,
        u.name = s.name,
        u.phone = s.phone,
        u.role = 'RESIDENT',
        u.status = s.status,
        u.provider = 'LOCAL',
        u.is_deleted = 0,
        u.updated_at = NOW();

SET @admin_id := (
    SELECT user_id
    FROM `user`
    WHERE email = 'admin@gmail.com'
    LIMIT 1
);

-- =========================================================
-- 3. household_history
-- =========================================================

INSERT INTO household_history (household_id, user_id, status, changed_at)
SELECT u.household_id, u.user_id, '입주', DATE_SUB(NOW(), INTERVAL 20 DAY)
FROM `user` u
WHERE u.role = 'RESIDENT'
  AND u.status = 'APPROVED'
  AND u.is_deleted = 0
  AND u.household_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM household_history hh
    WHERE hh.household_id = u.household_id
      AND hh.user_id = u.user_id
      AND hh.status = '입주'
);

-- =========================================================
-- 4. board
-- =========================================================

-- 공지사항
INSERT INTO board (user_id, category, title, content, view_count, created_at)
SELECT @admin_id, 'NOTICE', s.title, s.content, s.view_count, s.created_at
FROM (
         SELECT '[공지] 5월 커뮤니티 시설 운영 안내' AS title, '5월 커뮤니티 시설 운영 시간과 휴무 일정을 안내드립니다.' AS content, 92 AS view_count, DATE_SUB(NOW(), INTERVAL 9 DAY) AS created_at UNION ALL
         SELECT '[공지] 주차장 야간 도색 작업 안내', '지하주차장 B1 구역 야간 도색 작업이 진행됩니다.', 81, DATE_SUB(NOW(), INTERVAL 8 DAY) UNION ALL
         SELECT '[공지] 엘리베이터 정기점검 안내', '101동, 102동 엘리베이터 정기점검 일정을 확인해주세요.', 76, DATE_SUB(NOW(), INTERVAL 7 DAY) UNION ALL
         SELECT '[공지] GX 프로그램 신규 모집', '필라테스와 그룹PT 신규 수강 신청이 시작되었습니다.', 120, DATE_SUB(NOW(), INTERVAL 6 DAY) UNION ALL
         SELECT '[공지] 독서실 좌석 이용수칙', '독서실 좌석 예약 후 미이용 시 이용 제한이 발생할 수 있습니다.', 58, DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT '[공지] 골프연습장 타석 예약 안내', '골프연습장 예약 가능 시간과 취소 기준을 안내드립니다.', 64, DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT '[공지] 방문차량 사전등록 권장', '원활한 출입을 위해 방문차량은 앱에서 사전등록해주세요.', 73, DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT '[공지] 재활용 분리배출 캠페인', '이번 주 토요일 재활용 분리배출 캠페인이 진행됩니다.', 41, DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT '[공지] 관리비 납부 일정 안내', '이번 달 관리비 납부 마감일을 확인해주세요.', 88, DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT '[공지] 어린이놀이터 안전점검', '어린이놀이터 안전점검으로 일부 시설 이용이 제한됩니다.', 39, NOW()
     ) s
WHERE @admin_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM board b
    WHERE b.title = s.title
);

-- 입주민 게시글
INSERT INTO board (user_id, category, title, content, view_count, created_at)
SELECT u.user_id, s.category, s.title, s.content, s.view_count, s.created_at
FROM (
         SELECT 'resident1@apt.com' AS email, 'FREE' AS category, '헬스장 아침 시간 이용 후기' AS title, '아침 시간대가 한산해서 운동하기 좋았습니다.' AS content, 35 AS view_count, DATE_SUB(NOW(), INTERVAL 11 DAY) AS created_at UNION ALL
         SELECT 'resident2@apt.com', 'FREE', '독서실 콘센트 자리 추천', '창가 쪽 좌석이 조용하고 집중하기 좋습니다.', 29, DATE_SUB(NOW(), INTERVAL 10 DAY) UNION ALL
         SELECT 'resident3@apt.com', 'FREE', '골프연습장 타석 상태 좋아요', '최근 타석 매트가 깔끔하게 관리되고 있습니다.', 21, DATE_SUB(NOW(), INTERVAL 9 DAY) UNION ALL
         SELECT 'resident4@apt.com', 'INQUIRY', '방문차량 등록 시간 문의', '방문차량은 당일에도 등록 가능한지 궁금합니다.', 14, DATE_SUB(NOW(), INTERVAL 8 DAY) UNION ALL
         SELECT 'resident5@apt.com', 'FREE', '분리수거장 이용시간 공유', '저녁 8시 이후에는 비교적 여유롭습니다.', 18, DATE_SUB(NOW(), INTERVAL 7 DAY) UNION ALL
         SELECT 'resident6@apt.com', 'INQUIRY', 'GX 프로그램 환불 문의', '개인 일정으로 수강 취소 시 환불 기준이 궁금합니다.', 12, DATE_SUB(NOW(), INTERVAL 6 DAY) UNION ALL
         SELECT 'resident7@apt.com', 'FREE', '주차장 입구 혼잡 시간대', '평일 8시 30분 전후가 가장 혼잡한 것 같습니다.', 44, DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT 'resident8@apt.com', 'FREE', '놀이터 주변 조명 개선 요청', '저녁 시간대 조명이 조금 어둡습니다.', 25, DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT 'resident9@apt.com', 'FREE', '택배 보관함 이용 팁', '알림 확인 후 빠르게 수령하면 분실 걱정이 줄어듭니다.', 31, DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident10@apt.com', 'INQUIRY', '골프 예약 취소 기준 문의', '예약 당일 취소 가능 시간이 궁금합니다.', 16, DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident11@apt.com', 'FREE', '커뮤니티센터 청결해서 좋아요', '최근 청소 상태가 좋아져서 만족스럽습니다.', 27, DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident12@apt.com', 'FREE', '입주민 앱 사용 후기', '예약과 방문차량 등록을 한 번에 할 수 있어 편합니다.', 50, NOW()
     ) s
         JOIN `user` u ON u.email = s.email
WHERE NOT EXISTS (
    SELECT 1
    FROM board b
    WHERE b.title = s.title
);

-- =========================================================
-- 5. comment
-- =========================================================

INSERT INTO `comment` (board_id, user_id, content, created_at)
SELECT b.board_id, u.user_id, s.content, s.created_at
FROM (
         SELECT '헬스장 아침 시간 이용 후기' AS board_title, 'resident2@apt.com' AS email, '저도 아침 시간 추천합니다.' AS content, DATE_SUB(NOW(), INTERVAL 10 DAY) AS created_at UNION ALL
         SELECT '독서실 콘센트 자리 추천', 'resident1@apt.com', '좋은 정보 감사합니다.', DATE_SUB(NOW(), INTERVAL 9 DAY) UNION ALL
         SELECT '골프연습장 타석 상태 좋아요', 'resident4@apt.com', '이번 주말에 예약해봐야겠네요.', DATE_SUB(NOW(), INTERVAL 8 DAY) UNION ALL
         SELECT '방문차량 등록 시간 문의', 'admin@gmail.com', '방문 당일에도 등록 가능합니다.', DATE_SUB(NOW(), INTERVAL 7 DAY) UNION ALL
         SELECT '분리수거장 이용시간 공유', 'resident7@apt.com', '저녁 시간 정보 유용하네요.', DATE_SUB(NOW(), INTERVAL 6 DAY) UNION ALL
         SELECT 'GX 프로그램 환불 문의', 'admin@gmail.com', '관리사무소로 문의하시면 안내드리겠습니다.', DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT '주차장 입구 혼잡 시간대', 'resident8@apt.com', '출근 시간에는 조금 일찍 나가야겠어요.', DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT '놀이터 주변 조명 개선 요청', 'admin@gmail.com', '시설팀에 전달하겠습니다.', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT '택배 보관함 이용 팁', 'resident10@apt.com', '알림 설정 켜두면 편합니다.', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT '골프 예약 취소 기준 문의', 'admin@gmail.com', '시설 예약 상세 화면의 취소 기준을 참고해주세요.', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT '[공지] 방문차량 사전등록 권장', 'resident1@apt.com', '확인했습니다.', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT '[공지] GX 프로그램 신규 모집', 'resident5@apt.com', '이번 달 프로그램 기대됩니다.', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT '[공지] 독서실 좌석 이용수칙', 'resident6@apt.com', '좌석 이용수칙 확인했습니다.', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT '커뮤니티센터 청결해서 좋아요', 'resident3@apt.com', '공감합니다. 관리가 잘 되고 있어요.', NOW() UNION ALL
         SELECT '입주민 앱 사용 후기', 'admin@gmail.com', '좋은 의견 감사합니다.', NOW()
     ) s
         JOIN board b ON b.title = s.board_title
         JOIN `user` u ON u.email = s.email
WHERE NOT EXISTS (
    SELECT 1
    FROM `comment` c
    WHERE c.board_id = b.board_id
      AND c.user_id = u.user_id
      AND c.content = s.content
);

-- =========================================================
-- 6. parking_lot
-- =========================================================

INSERT INTO parking_lot (lot_id, name, total_spaces)
SELECT 1, '본관 주차장', 100
    WHERE NOT EXISTS (
    SELECT 1
    FROM parking_lot
    WHERE lot_id = 1
);

UPDATE parking_lot
SET name = '본관 주차장',
    total_spaces = 100
WHERE lot_id = 1;

-- =========================================================
-- 7. visitor_vehicle
-- =========================================================

INSERT INTO visitor_vehicle (
    user_id, license_plate, visitor_name, visit_purpose,
    visit_date, status, created_at
)
SELECT u.user_id, s.license_plate, s.visitor_name, s.visit_purpose, s.visit_date, s.status, s.created_at
FROM (
         SELECT 'resident1@apt.com' AS email, '00가1111' AS license_plate, '홍길순' AS visitor_name, '가족 방문' AS visit_purpose, CURDATE() AS visit_date, 'APPROVED' AS status, DATE_SUB(NOW(), INTERVAL 5 DAY) AS created_at UNION ALL
         SELECT 'resident1@apt.com', '00나2222', '이택배', '택배 방문', CURDATE(), 'APPROVED', DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT 'resident2@apt.com', '00다3333', '김방문', '친구 방문', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'APPROVED', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident3@apt.com', '00라4444', '박수리', '수리 방문', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'APPROVED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident4@apt.com', '00마5555', '최과거', '지난 방문', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'APPROVED', DATE_SUB(NOW(), INTERVAL 8 DAY) UNION ALL
         SELECT 'resident5@apt.com', '00바6666', '정코치', 'GX 강사 방문', DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'APPROVED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident6@apt.com', '00사7777', '한청소', '정기 청소', DATE_ADD(CURDATE(), INTERVAL 4 DAY), 'APPROVED', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident7@apt.com', '00아8888', '오가족', '가족 방문', DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'APPROVED', NOW() UNION ALL
         SELECT 'resident8@apt.com', '00자9999', '문기사', '가전 설치', DATE_ADD(CURDATE(), INTERVAL 6 DAY), 'CANCELLED', NOW() UNION ALL
         SELECT 'resident9@apt.com', '00차1010', '배송원', '가구 배송', DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'APPROVED', NOW() UNION ALL
         SELECT 'resident10@apt.com', '00카1112', '장방문', '지인 방문', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'APPROVED', NOW() UNION ALL
         SELECT 'resident11@apt.com', '00타1314', '서수리', '인터넷 설치', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'APPROVED', NOW()
     ) s
         JOIN `user` u ON u.email = s.email
WHERE NOT EXISTS (
    SELECT 1
    FROM visitor_vehicle vv
    WHERE vv.license_plate = s.license_plate
      AND vv.visit_date = s.visit_date
      AND vv.is_deleted = 0
);

-- =========================================================
-- 8. fixed_visitor_vehicle
-- =========================================================

INSERT INTO fixed_visitor_vehicle (
    user_id, vehicle_number, visitor_name, purpose,
    start_date, end_date, created_at
)
SELECT u.user_id, s.vehicle_number, s.visitor_name, s.purpose, s.start_date, s.end_date, s.created_at
FROM (
         SELECT 'resident1@apt.com' AS email, '11가2222' AS vehicle_number, '김택배' AS visitor_name, '정기 택배' AS purpose, DATE_SUB(CURDATE(), INTERVAL 10 DAY) AS start_date, DATE_ADD(CURDATE(), INTERVAL 30 DAY) AS end_date, DATE_SUB(NOW(), INTERVAL 10 DAY) AS created_at UNION ALL
         SELECT 'resident2@apt.com', '33나4444', '이가족', '가족 상시방문', DATE_SUB(CURDATE(), INTERVAL 30 DAY), NULL, DATE_SUB(NOW(), INTERVAL 9 DAY) UNION ALL
         SELECT 'resident3@apt.com', '55다6666', '박청소', '정기 청소', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY) UNION ALL
         SELECT 'resident4@apt.com', '77라8888', '최돌봄', '아이 돌봄', DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY) UNION ALL
         SELECT 'resident5@apt.com', '99마0000', '정강사', '개인 레슨', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY) UNION ALL
         SELECT 'resident6@apt.com', '22바3333', '송방문', '부모님 방문', DATE_SUB(CURDATE(), INTERVAL 2 DAY), NULL, DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT 'resident7@apt.com', '44사5555', '윤기사', '정기 배송', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)
     ) s
         JOIN `user` u ON u.email = s.email
WHERE NOT EXISTS (
    SELECT 1
    FROM fixed_visitor_vehicle f
    WHERE f.vehicle_number = s.vehicle_number
      AND f.user_id = u.user_id
      AND f.is_deleted = 0
);

-- =========================================================
-- 9. vehicle
-- =========================================================

INSERT INTO vehicle (
    user_id, household_id, license_plate, car_model, car_type,
    status, approved_by, approved_at, created_at
)
SELECT
    u.user_id,
    u.household_id,
    s.license_plate,
    s.car_model,
    s.car_type,
    s.status,
    CASE WHEN s.status = 'APPROVED' THEN @admin_id ELSE NULL END,
    CASE WHEN s.status = 'APPROVED' THEN DATE_SUB(NOW(), INTERVAL 2 DAY) ELSE NULL END,
    s.created_at
FROM (
         SELECT 'resident1@apt.com' AS email, '12가3456' AS license_plate, '현대 소나타' AS car_model, '승용차' AS car_type, 'APPROVED' AS status, DATE_SUB(NOW(), INTERVAL 15 DAY) AS created_at UNION ALL
         SELECT 'resident1@apt.com', '12나1111', '기아 레이', '경차', 'APPROVED', DATE_SUB(NOW(), INTERVAL 14 DAY) UNION ALL
         SELECT 'resident2@apt.com', '34나5678', 'BMW 5시리즈', '승용차', 'APPROVED', DATE_SUB(NOW(), INTERVAL 13 DAY) UNION ALL
         SELECT 'resident3@apt.com', '56다7890', '기아 스포티지', 'SUV', 'APPROVED', DATE_SUB(NOW(), INTERVAL 12 DAY) UNION ALL
         SELECT 'resident4@apt.com', '78라1234', '현대 그랜저', '승용차', 'APPROVED', DATE_SUB(NOW(), INTERVAL 11 DAY) UNION ALL
         SELECT 'resident5@apt.com', '90마5678', '쌍용 티볼리', 'SUV', 'APPROVED', DATE_SUB(NOW(), INTERVAL 10 DAY) UNION ALL
         SELECT 'resident6@apt.com', '11바9012', '기아 카니발', '승합차', 'APPROVED', DATE_SUB(NOW(), INTERVAL 9 DAY) UNION ALL
         SELECT 'resident7@apt.com', '22사1234', '현대 투싼', 'SUV', 'APPROVED', DATE_SUB(NOW(), INTERVAL 8 DAY) UNION ALL
         SELECT 'resident8@apt.com', '33자5678', '기아 K5', '승용차', 'APPROVED', DATE_SUB(NOW(), INTERVAL 7 DAY) UNION ALL
         SELECT 'resident9@apt.com', '44차9012', '현대 팰리세이드', 'SUV', 'APPROVED', DATE_SUB(NOW(), INTERVAL 6 DAY) UNION ALL
         SELECT 'resident10@apt.com', '55타1234', '기아 EV6', 'SUV', 'PENDING', DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT 'resident11@apt.com', '66파5678', '쉐보레 트레일블레이저', 'SUV', 'REJECTED', DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT 'resident12@apt.com', '77거9012', '기아 니로', 'SUV', 'APPROVED', DATE_SUB(NOW(), INTERVAL 3 DAY)
     ) s
         JOIN `user` u ON u.email = s.email
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle v
    WHERE v.license_plate = s.license_plate
      AND v.deleted_at IS NULL
);

-- =========================================================
-- 10. facility_type
-- =========================================================

INSERT INTO facility_type (name, description, category)
SELECT '독서실', '독서실 좌석 12석', '편의시설'
    WHERE NOT EXISTS (SELECT 1 FROM facility_type WHERE name = '독서실');

INSERT INTO facility_type (name, description, category)
SELECT '헬스장', '피트니스 센터', '편의시설'
    WHERE NOT EXISTS (SELECT 1 FROM facility_type WHERE name = '헬스장');

INSERT INTO facility_type (name, description, category)
SELECT '골프연습장', '스크린 골프 연습장 5타석', '편의시설'
    WHERE NOT EXISTS (SELECT 1 FROM facility_type WHERE name = '골프연습장');

INSERT INTO facility_type (name, description, category)
SELECT 'GX', 'GX 그룹 운동 프로그램', 'GX프로그램'
    WHERE NOT EXISTS (SELECT 1 FROM facility_type WHERE name = 'GX');

SET @study_type_id := (SELECT type_id FROM facility_type WHERE name = '독서실' ORDER BY type_id LIMIT 1);
SET @gym_type_id := (SELECT type_id FROM facility_type WHERE name = '헬스장' ORDER BY type_id LIMIT 1);
SET @golf_type_id := (SELECT type_id FROM facility_type WHERE name = '골프연습장' ORDER BY type_id LIMIT 1);
SET @gx_type_id := (SELECT type_id FROM facility_type WHERE name = 'GX' ORDER BY type_id LIMIT 1);

-- =========================================================
-- 11. facility
-- =========================================================

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT @study_type_id, '독서실(남)', '남성 전용 독서실, 좌석 12석', 12, 0, '05:00:00', '23:00:00', 1080, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility WHERE name = '독서실(남)' AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT @study_type_id, '독서실(여)', '여성 전용 독서실, 좌석 12석', 12, 0, '05:00:00', '23:00:00', 1080, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility WHERE name = '독서실(여)' AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT @gym_type_id, '헬스장', '공용 헬스장', 999, 0, '04:55:00', '23:00:00', 1085, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility WHERE name = '헬스장' AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT @golf_type_id, '골프연습장', '스크린 골프 5타석, 1시간 단위 예약', 5, 0, '06:00:00', '22:00:00', 60, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility WHERE name = '골프연습장' AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT @gx_type_id, 'GX-필라테스(오전)', 'GX 필라테스 오전 | 매주 월·수·금', 15, 50000, '10:00:00', '11:00:00', 60, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility WHERE name = 'GX-필라테스(오전)' AND deleted_at IS NULL
);

SET @study_male_id := (SELECT facility_id FROM facility WHERE name = '독서실(남)' AND deleted_at IS NULL ORDER BY facility_id LIMIT 1);
SET @study_female_id := (SELECT facility_id FROM facility WHERE name = '독서실(여)' AND deleted_at IS NULL ORDER BY facility_id LIMIT 1);
SET @gym_id := (SELECT facility_id FROM facility WHERE name = '헬스장' AND deleted_at IS NULL ORDER BY facility_id LIMIT 1);
SET @golf_id := (SELECT facility_id FROM facility WHERE name = '골프연습장' AND deleted_at IS NULL ORDER BY facility_id LIMIT 1);
SET @gx_pilates_id := (SELECT facility_id FROM facility WHERE name = 'GX-필라테스(오전)' AND deleted_at IS NULL ORDER BY facility_id LIMIT 1);

-- =========================================================
-- 12. gx_program
-- =========================================================

INSERT INTO gx_program (
    facility_id, start_date, end_date, days_of_week, status, created_at
)
SELECT @gx_pilates_id, DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'MON,WED,FRI', 'OPEN', NOW()
    WHERE @gx_pilates_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM gx_program
      WHERE facility_id = @gx_pilates_id
        AND start_date = DATE_SUB(CURDATE(), INTERVAL 7 DAY)
        AND end_date = DATE_ADD(CURDATE(), INTERVAL 30 DAY)
  );

SET @gx_program_id := (
    SELECT program_id
    FROM gx_program
    WHERE facility_id = @gx_pilates_id
    ORDER BY program_id DESC
    LIMIT 1
);

-- =========================================================
-- 13. reservation
-- =========================================================

INSERT INTO reservation (
    user_id, facility_id, reservation_date,
    start_time, end_time, seat_no, program_id,
    status, created_at, approved_at, cancelled_at
)
SELECT
    u.user_id,
    s.facility_id,
    s.reservation_date,
    s.start_time,
    s.end_time,
    s.seat_no,
    s.program_id,
    s.status,
    s.created_at,
    CASE WHEN s.status = 'CONFIRMED' THEN s.created_at ELSE NULL END,
    CASE WHEN s.status = 'CANCELLED' THEN DATE_ADD(s.created_at, INTERVAL 1 HOUR) ELSE NULL END
FROM (
         SELECT 'resident1@apt.com' AS email, @study_male_id AS facility_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY) AS reservation_date, '05:00:00' AS start_time, '23:00:00' AS end_time, 1 AS seat_no, NULL AS program_id, 'CONFIRMED' AS status, DATE_SUB(NOW(), INTERVAL 6 DAY) AS created_at UNION ALL
         SELECT 'resident2@apt.com', @study_male_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '05:00:00', '23:00:00', 2, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT 'resident3@apt.com', @study_male_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '05:00:00', '23:00:00', 3, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT 'resident4@apt.com', @study_female_id, CURDATE(), '05:00:00', '23:00:00', 1, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident5@apt.com', @study_female_id, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '05:00:00', '23:00:00', 2, NULL, 'COMPLETED', DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT 'resident6@apt.com', @study_male_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '05:00:00', '23:00:00', 4, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident7@apt.com', @study_female_id, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '05:00:00', '23:00:00', 3, NULL, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident8@apt.com', @study_male_id, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '05:00:00', '23:00:00', 5, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident9@apt.com', @study_female_id, DATE_ADD(CURDATE(), INTERVAL 5 DAY), '05:00:00', '23:00:00', 4, NULL, 'CONFIRMED', NOW() UNION ALL
         SELECT 'resident10@apt.com', @study_male_id, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '05:00:00', '23:00:00', 6, NULL, 'CONFIRMED', NOW() UNION ALL
         SELECT 'resident1@apt.com', @golf_id, CURDATE(), '10:00:00', '11:00:00', 1, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 6 DAY) UNION ALL
         SELECT 'resident2@apt.com', @golf_id, CURDATE(), '10:00:00', '11:00:00', 2, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT 'resident3@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', 1, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 4 DAY) UNION ALL
         SELECT 'resident4@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', 2, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident5@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '13:00:00', '14:00:00', 1, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident6@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '13:00:00', '14:00:00', 2, NULL, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident7@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:00:00', '16:00:00', 1, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident8@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:00:00', '16:00:00', 2, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident9@apt.com', @golf_id, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '16:00:00', '17:00:00', 1, NULL, 'CONFIRMED', NOW() UNION ALL
         SELECT 'resident10@apt.com', @golf_id, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', 1, NULL, 'COMPLETED', DATE_SUB(NOW(), INTERVAL 5 DAY) UNION ALL
         SELECT 'resident1@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'PENDING', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident2@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'PENDING', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident3@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 3 DAY) UNION ALL
         SELECT 'resident4@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident5@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 2 DAY) UNION ALL
         SELECT 'resident6@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident7@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident8@apt.com', @gx_pilates_id, DATE_ADD(CURDATE(), INTERVAL 8 DAY), '10:00:00', '11:00:00', NULL, @gx_program_id, 'PENDING', NOW() UNION ALL
         SELECT 'resident9@apt.com', @gym_id, CURDATE(), '04:55:00', '23:00:00', NULL, NULL, 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 1 DAY) UNION ALL
         SELECT 'resident10@apt.com', @gym_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '04:55:00', '23:00:00', NULL, NULL, 'CONFIRMED', NOW()
     ) s
         JOIN `user` u ON u.email = s.email
WHERE s.facility_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM reservation r
    WHERE r.user_id = u.user_id
      AND r.facility_id = s.facility_id
      AND r.reservation_date = s.reservation_date
      AND r.start_time = s.start_time
      AND IFNULL(r.seat_no, -1) = IFNULL(s.seat_no, -1)
      AND IFNULL(r.program_id, -1) = IFNULL(s.program_id, -1)
);

-- =========================================================
-- 14. parking_log
-- =========================================================

INSERT INTO parking_log (
    lot_id, vehicle_id, visitor_vehicle_id, fixed_visitor_vehicle_id,
    license_plate, entry_type, logged_at
)
SELECT 1, v.vehicle_id, NULL, NULL, s.license_plate, s.entry_type, s.logged_at
FROM (
         SELECT '12가3456' AS license_plate, 'IN' AS entry_type, TIMESTAMP(CURDATE(), '08:10:00') AS logged_at UNION ALL
SELECT '12가3456', 'OUT', TIMESTAMP(CURDATE(), '15:30:00') UNION ALL
SELECT '12나1111', 'IN', TIMESTAMP(CURDATE(), '08:40:00') UNION ALL
SELECT '34나5678', 'IN', TIMESTAMP(CURDATE(), '09:10:00') UNION ALL
SELECT '34나5678', 'OUT', TIMESTAMP(CURDATE(), '16:20:00') UNION ALL
SELECT '56다7890', 'IN', TIMESTAMP(CURDATE(), '10:00:00') UNION ALL
SELECT '78라1234', 'IN', TIMESTAMP(CURDATE(), '10:30:00') UNION ALL
SELECT '78라1234', 'OUT', TIMESTAMP(CURDATE(), '17:10:00') UNION ALL
SELECT '90마5678', 'IN', TIMESTAMP(CURDATE(), '11:00:00') UNION ALL
SELECT '11바9012', 'IN', TIMESTAMP(CURDATE(), '12:00:00')
    ) s
    JOIN vehicle v ON v.license_plate = s.license_plate AND v.deleted_at IS NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM parking_log p
    WHERE p.license_plate = s.license_plate
  AND p.entry_type = s.entry_type
  AND p.logged_at = s.logged_at
    );

INSERT INTO parking_log (
    lot_id, vehicle_id, visitor_vehicle_id, fixed_visitor_vehicle_id,
    license_plate, entry_type, logged_at
)
SELECT 1, NULL, vv.visitor_vehicle_id, NULL, s.license_plate, s.entry_type, s.logged_at
FROM (
         SELECT '00가1111' AS license_plate, 'IN' AS entry_type, TIMESTAMP(CURDATE(), '11:00:00') AS logged_at UNION ALL
SELECT '00가1111', 'OUT', TIMESTAMP(CURDATE(), '16:20:00') UNION ALL
SELECT '00나2222', 'IN', TIMESTAMP(CURDATE(), '12:00:00') UNION ALL
SELECT '00다3333', 'IN', TIMESTAMP(CURDATE(), '15:30:00')
    ) s
    JOIN visitor_vehicle vv ON vv.license_plate = s.license_plate AND vv.is_deleted = 0
WHERE NOT EXISTS (
    SELECT 1
    FROM parking_log p
    WHERE p.license_plate = s.license_plate
  AND p.entry_type = s.entry_type
  AND p.logged_at = s.logged_at
    );

INSERT INTO parking_log (
    lot_id, vehicle_id, visitor_vehicle_id, fixed_visitor_vehicle_id,
    license_plate, entry_type, logged_at
)
SELECT 1, NULL, NULL, f.fixed_id, s.license_plate, s.entry_type, s.logged_at
FROM (
         SELECT '11가2222' AS license_plate, 'IN' AS entry_type, TIMESTAMP(CURDATE(), '10:30:00') AS logged_at UNION ALL
SELECT '11가2222', 'OUT', TIMESTAMP(CURDATE(), '15:30:00') UNION ALL
SELECT '33나4444', 'IN', TIMESTAMP(CURDATE(), '11:00:00') UNION ALL
SELECT '55다6666', 'IN', TIMESTAMP(CURDATE(), '16:20:00')
    ) s
    JOIN fixed_visitor_vehicle f ON f.vehicle_number = s.license_plate AND f.is_deleted = 0
WHERE NOT EXISTS (
    SELECT 1
    FROM parking_log p
    WHERE p.license_plate = s.license_plate
  AND p.entry_type = s.entry_type
  AND p.logged_at = s.logged_at
    );

COMMIT;

SET SQL_SAFE_UPDATES = 1;

-- =========================================================
-- 확인용 조회
-- =========================================================

SELECT COUNT(*) AS user_count FROM `user`;
SELECT COUNT(*) AS household_count FROM household;
SELECT COUNT(*) AS board_count FROM board;
SELECT COUNT(*) AS comment_count FROM `comment`;
SELECT COUNT(*) AS vehicle_count FROM vehicle;
SELECT COUNT(*) AS visitor_vehicle_count FROM visitor_vehicle;
SELECT COUNT(*) AS fixed_visitor_vehicle_count FROM fixed_visitor_vehicle;
SELECT COUNT(*) AS facility_count FROM facility;
SELECT COUNT(*) AS reservation_count FROM reservation;
SELECT COUNT(*) AS parking_log_count FROM parking_log;

SELECT email, name, role, status, household_id
FROM `user`
ORDER BY user_id;

SELECT board_id, category, title, view_count, created_at
FROM board
ORDER BY board_id DESC
    LIMIT 20;