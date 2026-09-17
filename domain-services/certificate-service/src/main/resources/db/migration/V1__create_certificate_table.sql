CREATE TABLE tb_certificate (
                                id UUID PRIMARY KEY,
                                enrollment_id UUID NOT NULL,
                                student_id UUID NOT NULL,
                                course_id UUID NOT NULL,
                                certificate_code VARCHAR(50) NOT NULL,
                                issued_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                CONSTRAINT uk_certificate_enrollment UNIQUE (enrollment_id),
                                CONSTRAINT uk_certificate_code UNIQUE (certificate_code)
);