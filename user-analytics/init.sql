CREATE DATABASE analytics_db;
CREATE DATABASE appointment_db;
CREATE DATABASE medical_record_db;
CREATE DATABASE doctor_db;
CREATE DATABASE billing_db;
CREATE DATABASE prescription_db;


-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE analytics_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE appointment_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE medical_record_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE doctor_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE billing_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE prescription_db TO postgres;
-- GRANT ALL PRIVILEGES ON DATABASE patient_db TO postgres;
