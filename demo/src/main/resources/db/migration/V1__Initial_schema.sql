CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL -- e.g., 'ROLE_USER', 'ROLE_ADMIN'
);


CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL, -- Hashed password
                       email VARCHAR(255) UNIQUE NOT NULL,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_users_username ON users(username);


-- ===================================================================
-- ===================================================================

CREATE TABLE symbols (
                         id SERIAL PRIMARY KEY,
                         ticker VARCHAR(20) UNIQUE NOT NULL, -- e.g., 'AAPL'
                         name VARCHAR(255) NOT NULL, -- e.g., 'Apple Inc.'
                         description TEXT
);

-- ایجاد انواع داده‌ای سفارشی (ENUM) برای خوانایی بهتر
CREATE TYPE order_type AS ENUM ('BUY', 'SELL');
CREATE TYPE order_status AS ENUM ('PENDING', 'PARTIALLY_FILLED', 'FILLED', 'CANCELLED');

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL REFERENCES users(id),
                        symbol_id INT NOT NULL REFERENCES symbols(id),
                        type order_type NOT NULL,
                        status order_status NOT NULL DEFAULT 'PENDING',
                        quantity BIGINT NOT NULL,
                        price NUMERIC(19, 8) NOT NULL, -- NUMERIC for financial precision
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE trades (
                        id BIGSERIAL PRIMARY KEY,
                        symbol_id INT NOT NULL REFERENCES symbols(id),
                        buy_order_id BIGINT NOT NULL REFERENCES orders(id),
                        sell_order_id BIGINT NOT NULL REFERENCES orders(id),
                        quantity BIGINT NOT NULL,
                        price NUMERIC(19, 8) NOT NULL,
                        trade_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ایجاد ایندکس برای بهبود عملکرد کوئری‌ها روی جداول سفارشات و معاملات
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_symbol_id ON orders(symbol_id);
CREATE INDEX idx_trades_symbol_id ON trades(symbol_id);


-- ===================================================================
-- ===================================================================

CREATE TABLE wallets (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT UNIQUE NOT NULL REFERENCES users(id), -- Each user has one wallet
                         balance NUMERIC(19, 8) NOT NULL DEFAULT 0.00,
                         last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE portfolios (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL REFERENCES users(id),
                            symbol_id INT NOT NULL REFERENCES symbols(id),
                            quantity BIGINT NOT NULL,
    -- اطمینان از اینکه هر کاربر برای هر نماد فقط یک رکورد دارایی دارد
                            UNIQUE (user_id, symbol_id)
);

-- ایجاد ایندکس برای دسترسی سریع به دارایی‌های یک کاربر
CREATE INDEX idx_portfolios_user_id ON portfolios(user_id);