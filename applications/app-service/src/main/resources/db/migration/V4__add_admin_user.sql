INSERT INTO crediya_user (
    name,
    lastname,
    id_role,
    email,
    identification,
    salary_base,
    birth_date,
    address,
    phone,
    password
) VALUES (
    'Admin',
    'Principal',
    (SELECT id FROM crediya_role WHERE name = 'ADMIN'),
    'admin@crediya.com',
    '00000000',
    NULL,
    NULL,
    NULL,
    NULL,
    '$2a$10$S9wQAKPc2K4BsmZhpv/Tk.pMk56jOTPRtQj0VGQ1yfS.yz5jF8LZy'
)