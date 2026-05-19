-- APTeN local demo data final
-- 관리자: admin@gmail.com / admin1234
-- 입주민: resident1@apt.com / test1234

ROLLBACK;
SET SQL_SAFE_UPDATES = 0;

START TRANSACTION;

-- 1. 최소 세대 생성
INSERT INTO household (dong, ho, created_at)
SELECT '101동', '101호', NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM household
    WHERE dong = '101동'
      AND ho = '101호'
);

SET @resident_household_id := (
    SELECT household_id
    FROM household
    WHERE dong = '101동'
      AND ho = '101호'
    ORDER BY household_id
    LIMIT 1
);

-- 2. 최소 주차장 생성
INSERT INTO parking_lot (name, total_spaces)
SELECT '본관 주차장', 100
    WHERE NOT EXISTS (
    SELECT 1
    FROM parking_lot
    WHERE name = '본관 주차장'
);

UPDATE parking_lot
SET total_spaces = 100
WHERE name = '본관 주차장';

-- 3. 시설 타입 생성
INSERT INTO facility_type (name, description, category)
SELECT '독서실', '독서실 좌석 12석', '편의시설'
    WHERE NOT EXISTS (
    SELECT 1 FROM facility_type WHERE name = '독서실'
);

INSERT INTO facility_type (name, description, category)
SELECT '헬스장', '피트니스 센터', '편의시설'
    WHERE NOT EXISTS (
    SELECT 1 FROM facility_type WHERE name = '헬스장'
);

INSERT INTO facility_type (name, description, category)
SELECT '골프연습장', '스크린 골프 연습장 5타석', '편의시설'
    WHERE NOT EXISTS (
    SELECT 1 FROM facility_type WHERE name = '골프연습장'
);

INSERT INTO facility_type (name, description, category)
SELECT 'GX', 'GX 그룹 운동 프로그램', 'GX프로그램'
    WHERE NOT EXISTS (
    SELECT 1 FROM facility_type WHERE name = 'GX'
);

SET @study_type_id := (
    SELECT type_id FROM facility_type
    WHERE name = '독서실'
    ORDER BY type_id
    LIMIT 1
);

SET @gym_type_id := (
    SELECT type_id FROM facility_type
    WHERE name = '헬스장'
    ORDER BY type_id
    LIMIT 1
);

SET @golf_type_id := (
    SELECT type_id FROM facility_type
    WHERE name = '골프연습장'
    ORDER BY type_id
    LIMIT 1
);

SET @gx_type_id := (
    SELECT type_id FROM facility_type
    WHERE name = 'GX'
    ORDER BY type_id
    LIMIT 1
);

-- 4. 시설 생성
INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT
    @study_type_id, '독서실(남)', '남성 전용 독서실, 좌석 12석', 12, 0,
    '05:00:00', '23:00:00', 1080, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility
    WHERE name = '독서실(남)'
      AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT
    @study_type_id, '독서실(여)', '여성 전용 독서실, 좌석 12석', 12, 0,
    '05:00:00', '23:00:00', 1080, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility
    WHERE name = '독서실(여)'
      AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT
    @gym_type_id, '헬스장', '공용 헬스장', 999, 0,
    '04:55:00', '23:00:00', 1085, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility
    WHERE name = '헬스장'
      AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT
    @golf_type_id, '골프연습장', '스크린 골프 5타석, 1시간 단위 예약', 5, 0,
    '06:00:00', '22:00:00', 60, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility
    WHERE name = '골프연습장'
      AND deleted_at IS NULL
);

INSERT INTO facility (
    type_id, name, description, max_capacity, price,
    open_time, close_time, slot_duration, is_active, created_at
)
SELECT
    @gx_type_id, 'GX-필라테스(오전)', 'GX 필라테스 오전 | 매주 월·수·금', 15, 50000,
    '10:00:00', '11:00:00', 60, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM facility
    WHERE name = 'GX-필라테스(오전)'
      AND deleted_at IS NULL
);

-- 5. 관리자 계정 생성/갱신
-- admin@gmail.com / admin1234
INSERT INTO `user` (
    email, password, name, phone,
    role, status, provider, is_deleted,
    created_at, updated_at
)
SELECT
    'admin@gmail.com',
    '$2a$10$Nvr64lsMpaOnIOM64fo0ietsqTuExYlo0QZlFXjWiErpBG9w6cxgW',
    '관리자',
    '010-0000-0000',
    'ADMIN',
    'APPROVED',
    'LOCAL',
    0,
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM `user`
    WHERE email = 'admin@gmail.com'
);

UPDATE `user`
SET password = '$2a$10$Nvr64lsMpaOnIOM64fo0ietsqTuExYlo0QZlFXjWiErpBG9w6cxgW',
    name = '관리자',
    phone = '010-0000-0000',
    role = 'ADMIN',
    status = 'APPROVED',
    provider = 'LOCAL',
    is_deleted = 0,
    updated_at = NOW()
WHERE email = 'admin@gmail.com';

-- 6. 입주민 계정 생성/갱신
-- resident1@apt.com / test1234
INSERT INTO `user` (
    household_id, email, password, name, phone,
    role, status, provider, is_deleted,
    created_at, updated_at
)
SELECT
    @resident_household_id,
    'resident1@apt.com',
    '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y',
    '김민수',
    '010-1000-0001',
    'RESIDENT',
    'APPROVED',
    'LOCAL',
    0,
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM `user`
    WHERE email = 'resident1@apt.com'
);

UPDATE `user`
SET household_id = @resident_household_id,
    password = '$2a$10$Psl81RGfFTnQqK5prVwU.Ou6Y.DQMeYYfgJmBDGolGMKJHJcYu01y',
    name = '김민수',
    phone = '010-1000-0001',
    role = 'RESIDENT',
    status = 'APPROVED',
    provider = 'LOCAL',
    is_deleted = 0,
    updated_at = NOW()
WHERE email = 'resident1@apt.com';

SET @resident_user_id := (
    SELECT user_id
    FROM `user`
    WHERE email = 'resident1@apt.com'
    ORDER BY user_id
    LIMIT 1
);

-- 7. 입주 이력 생성
INSERT INTO household_history (
    household_id, user_id, status, changed_at
)
SELECT
    @resident_household_id,
    @resident_user_id,
    '입주',
    NOW()
    WHERE @resident_user_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM household_history
      WHERE household_id = @resident_household_id
        AND user_id = @resident_user_id
        AND status = '입주'
  );

COMMIT;

SET SQL_SAFE_UPDATES = 1;

-- 8. 확인용 조회
SELECT email, name, role, status, is_deleted, household_id
FROM `user`
WHERE email IN ('admin@gmail.com', 'resident1@apt.com');

SELECT household_id, dong, ho
FROM household
WHERE dong = '101동'
  AND ho = '101호';

SELECT name, total_spaces
FROM parking_lot
WHERE name = '본관 주차장';

SELECT name, description, category
FROM facility_type
WHERE name IN ('독서실', '헬스장', '골프연습장', 'GX');
