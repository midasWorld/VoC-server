INSERT INTO company(id, registration_no, name, type, phone) VALUES
  (1, '111-11-11111', '고객사01', 'CLIENT', '02-111-1111'),
  (2, '111-11-22221', '고객사02', 'CLIENT', '02-111-2222'),
  (3, '222-11-11111', '운송사01', 'SHIPPING', '02-222-1111'),
  (4, '222-11-22221', '운송사02', 'SHIPPING', '02-222-2222');

INSERT INTO person(id, name, company_id, phone) VALUES
  (1, '철수 기사', 3, '010-2222-1111'),
  (2, '영희 기사', 4, '010-2222-2222'),
  (3, '복순 담당자', 1, '010-1111-1111'),
  (4, '도철 담당자', 2, '010-1111-2222');

INSERT INTO voc(id, due_type, due_target_id, due_reason) VALUES
    (1, 'SHIPPING', 1, '배송이 하루 늦음');

INSERT INTO penalty(id, content, amount, voc_id, confirmed, objected) VALUES
    (1, '배송 지연 배상금 지불', 30000, 1, true, false);

INSERT INTO compensation(id, amount, voc_id) VALUES
    (1, 30000, 1);