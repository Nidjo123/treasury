CREATE TABLE items (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
);

CREATE TABLE transactions (
  id SERIAL PRIMARY KEY,
  date DATE NOT NULL,
  item_id INTEGER NOT NULL REFERENCES items(id),
  description TEXT CONSTRAINT desc_length CHECK (CHAR_LENGTH(description) <= 2048),
  income NUMERIC(19, 2) DEFAULT 0 CONSTRAINT nonnegative_income CHECK(income >= 0.00),
  expense NUMERIC(19, 2) DEFAULT 0 CONSTRAINT nonnegative_expense CHECK(expense >= 0.00),
  remark TEXT CONSTRAINT remark_length CHECK (CHAR_LENGTH(remark) <= 2048),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT income_expense_check CHECK((income >= 0.00 AND expense = 0.00) OR (expense >= 0.00 AND income = 0.00) OR (income = 0.00 AND expense = 0.00))
);
