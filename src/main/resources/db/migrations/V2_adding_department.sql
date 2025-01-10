CREATE TABLE department (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   title VARCHAR(255),
   CONSTRAINT pk_department PRIMARY KEY (id)
);

ALTER TABLE employee ADD department_id BIGINT;

ALTER TABLE employee ADD CONSTRAINT FK_EMPLOYEE_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES department (id);