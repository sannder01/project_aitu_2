-- Database initialization script for reminder_db
-- Run this script to create all necessary tables

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS workers CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create workers table
CREATE TABLE workers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create categories table
-- Requirement #7: Add categories for entities
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    color VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
-- Requirement #5: Role Management
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- In production, store hashed passwords!
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'MANAGER', 'EDITOR', 'VIEWER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create tasks table with foreign keys
-- Requirement #1: Tables prepared for JOINs
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    worker_id INTEGER NOT NULL,
    category_id INTEGER,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    deadline DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Pending',
    priority VARCHAR(20) NOT NULL DEFAULT 'Medium',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (worker_id) REFERENCES workers(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CHECK (status IN ('Pending', 'In Progress', 'Completed', 'Cancelled')),
    CHECK (priority IN ('Low', 'Medium', 'High', 'Critical'))
);

-- Create indexes for better JOIN performance
-- Requirement #1: Optimize JOIN operations
CREATE INDEX idx_tasks_worker_id ON tasks(worker_id);
CREATE INDEX idx_tasks_category_id ON tasks(category_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_tasks_deadline ON tasks(deadline);

-- Insert sample data

-- Sample workers
INSERT INTO workers (name, position, email) VALUES
    ('John Doe', 'Software Engineer', 'john.doe@example.com'),
    ('Jane Smith', 'Project Manager', 'jane.smith@example.com'),
    ('Bob Johnson', 'Designer', 'bob.johnson@example.com'),
    ('Alice Williams', 'QA Engineer', 'alice.williams@example.com');

-- Sample categories
INSERT INTO categories (name, description, color) VALUES
    ('Development', 'Software development tasks', '#3498db'),
    ('Design', 'UI/UX design tasks', '#e74c3c'),
    ('Testing', 'Quality assurance tasks', '#2ecc71'),
    ('Documentation', 'Documentation and technical writing', '#f39c12'),
    ('Meeting', 'Meetings and presentations', '#9b59b6');

-- Sample users with different roles
-- Password is '12345678' for all (in production, use hashed passwords!)
INSERT INTO users (username, password, email, role) VALUES
    ('admin', '12345678', 'admin@example.com', 'ADMIN'),
    ('manager1', '12345678', 'manager1@example.com', 'MANAGER'),
    ('editor1', '12345678', 'editor1@example.com', 'EDITOR'),
    ('viewer1', '12345678', 'viewer1@example.com', 'VIEWER');

-- Sample tasks
INSERT INTO tasks (worker_id, category_id, title, description, deadline, status, priority) VALUES
    (1, 1, 'Implement login feature', 'Create user authentication system', '2026-02-15', 'In Progress', 'High'),
    (1, 1, 'Fix bug #234', 'Resolve null pointer exception in user service', '2026-02-10', 'Pending', 'Critical'),
    (2, 5, 'Sprint planning meeting', 'Plan next sprint tasks', '2026-02-08', 'Pending', 'Medium'),
    (3, 2, 'Design new dashboard', 'Create mockups for analytics dashboard', '2026-02-20', 'Pending', 'Medium'),
    (4, 3, 'Test payment module', 'Complete integration testing', '2026-02-12', 'In Progress', 'High'),
    (2, 4, 'Update API documentation', 'Document new endpoints', '2026-02-18', 'Pending', 'Low'),
    (1, 1, 'Code review', 'Review pull requests', '2026-02-05', 'Pending', 'Medium');

-- Print success message
SELECT 'Database initialized successfully!' as message;
SELECT COUNT(*) as worker_count FROM workers;
SELECT COUNT(*) as category_count FROM categories;
SELECT COUNT(*) as user_count FROM users;
SELECT COUNT(*) as task_count FROM tasks;
