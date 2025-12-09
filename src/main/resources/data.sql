INSERT INTO rules (mandt, oid, file_class, name, description, rule, importer ) VALUES (1, 1, 1, 'İŞ Bankası', 'İş Bankası Hesap Hareketleri', 'HesapOzeti', 'ExcelIsbankImporter');
INSERT INTO rules (mandt, oid, file_class, name, description, rule, importer ) VALUES (1, 2, 1, 'DenizBank', 'DenizBank Hesap Hareketleri', 'Hareketleri', 'ExcelDenizbankImporter');

INSERT INTO inflation_rates (inflation_year, turkey, europe, usa, switzerland, britain ) VALUES (2020, 12.28, 0.25, 1.20, -0.70, 0.99);
INSERT INTO inflation_rates (inflation_year, turkey, europe, usa, switzerland, britain ) VALUES (2021, 19.60, 2.59, 4.70,  0.58, 2.52);
INSERT INTO inflation_rates (inflation_year, turkey, europe, usa, switzerland, britain ) VALUES (2022, 72.31, 8.38, 8.00,  2.83, 9.07);
INSERT INTO inflation_rates (inflation_year, turkey, europe, usa, switzerland, britain ) VALUES (2023, 53.86, 5.42, 4.10,  2.14, 7.30);
INSERT INTO inflation_rates (inflation_year, turkey, europe, usa, switzerland, britain ) VALUES (2024, 58.51, 2.60, 2.95,  1.06, 2.53);
INSERT INTO inflation_rates (inflation_year, turkey, europe, usa, switzerland, britain ) VALUES (2025, 35.93, 2.10, 3.10,  0.23, 3.80);
