CREATE TABLE tb_enrollment (
                               id UUID PRIMARY KEY,
                               student_id UUID NOT NULL,
                               course_id UUID NOT NULL,
                               status VARCHAR(50) NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               version BIGINT NOT NULL DEFAULT 0,
                               CONSTRAINT uk_student_course UNIQUE (student_id, course_id)
);