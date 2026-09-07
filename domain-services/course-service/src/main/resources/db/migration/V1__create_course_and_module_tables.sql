CREATE TABLE tb_course (
                           id UUID PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           description VARCHAR(1000),
                           level VARCHAR(50) NOT NULL,
                           version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE tb_module (
                           id UUID PRIMARY KEY,
                           course_id UUID NOT NULL,
                           title VARCHAR(255) NOT NULL,
                           description VARCHAR(1000),
                           duration INT NOT NULL,
                           module_order INT NOT NULL,
                           CONSTRAINT fk_module_course FOREIGN KEY (course_id) REFERENCES tb_course (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uk_module_course_order ON tb_module (course_id, module_order);