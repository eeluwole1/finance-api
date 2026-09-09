-- ADMIN USER — email: admin@priscillatrust.com / password: Admin@12345
-- ADMIN can see every client below regardless of ownership; log in with this
-- account to explore the seeded data on a fresh deploy.
INSERT INTO users (first_name, last_name, email, password, role)
SELECT 'Priscilla', 'Trust', 'admin@priscillatrust.com',
    '$2a$10$ivg4EkSUBps4j6YzVGq0DOSGDFamXTNuSnCzhx8j.rT0URlXu.FfK', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@priscillatrust.com');

-- CLIENTS ─
INSERT INTO clients (first_name, last_name, email, phone, address, status, created_at)
SELECT 'John', 'Smith', 'john@email.com', '647-555-1234', '123 Main St Toronto', 'ACTIVE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE email = 'john@email.com');

INSERT INTO clients (first_name, last_name, email, phone, address, status, created_at)
SELECT 'Jane', 'Doe', 'jane@email.com', '416-555-5678', '456 King St Toronto', 'ACTIVE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE email = 'jane@email.com');

INSERT INTO clients (first_name, last_name, email, phone, address, status, created_at)
SELECT 'Michael', 'Brown', 'michael@email.com', '905-555-9012', '789 Queen St Toronto', 'ACTIVE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE email = 'michael@email.com');

-- POLICIES ─
INSERT INTO policies (policy_number, type, coverage_amount, premium_amount, start_date, end_date, status, created_at, client_id)
SELECT 'POL-001', 'LIFE', 500000.00, 120.00, '2024-01-01', '2034-01-01', 'ACTIVE', NOW(),
    (SELECT id FROM clients WHERE email = 'john@email.com' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-001');

INSERT INTO policies (policy_number, type, coverage_amount, premium_amount, start_date, end_date, status, created_at, client_id)
SELECT 'POL-002', 'HEALTH', 100000.00, 85.00, '2024-03-01', '2025-03-01', 'ACTIVE', NOW(),
    (SELECT id FROM clients WHERE email = 'jane@email.com' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-002');

INSERT INTO policies (policy_number, type, coverage_amount, premium_amount, start_date, end_date, status, created_at, client_id)
SELECT 'POL-003', 'AUTO', 50000.00, 210.00, '2023-06-01', '2024-06-01', 'EXPIRED', NOW(),
    (SELECT id FROM clients WHERE email = 'michael@email.com' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-003');

INSERT INTO policies (policy_number, type, coverage_amount, premium_amount, start_date, end_date, status, created_at, client_id)
SELECT 'POL-004', 'HOME', 750000.00, 175.00, '2024-07-01', '2025-07-01', 'ACTIVE', NOW(),
    (SELECT id FROM clients WHERE email = 'john@email.com' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-004');
