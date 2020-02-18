create
    definer = `skip-grants user`@`skip-grants host` procedure proc_file_insert()
BEGIN
    DECLARE pre_name VARCHAR(50);
    DECLARE pre_localUrl VARCHAR(50);
    DECLARE size VARCHAR(50);
    DECLARE md5 VARCHAR(255);
    DECLARE suffix VARCHAR(50);
    DECLARE i INT;
    SET pre_name = 'test';
    SET pre_localUrl = '/home/sheva/test/real/';
    SET size = '876.35MB';
    SET md5 = 'test';
    SET suffix = '.mp4';
    SET i = 1;
    WHILE i <= 10000 DO
            INSERT INTO fud.file(`name`, `local_url`, size, user_id, md5, suffix) VALUES (CONCAT(pre_name, i), CONCAT(pre_localUrl, pre_name, i), size, i, md5, suffix);
            SET i = i + 1;
        END WHILE;
END;

