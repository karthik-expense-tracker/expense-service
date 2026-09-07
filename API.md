# Expense Tracker Backend API Contracts

Base URL: `http://localhost:8000`

CORS allowed origins: `http://localhost:3000`, `http://localhost`, `http://localhost:80`

---

## Common Models

### Expense

| Field        | Type   | Notes                                   |
|--------------|--------|-----------------------------------------|
| `id`         | number | Auto-generated, set by the backend      |
| `category`   | string |                                         |
| `amount`     | number | Integer, must be > 0                    |
| `date`       | string | Format: `dd-MM-yyyy`                    |
| `expenseType`| string | Must be `"credit"` or `"debit"`         |

---

## Validation Rules

| Field        | Rule                                                 | Error Message                                              | Status |
|--------------|------------------------------------------------------|------------------------------------------------------------|--------|
| `amount`     | Must be greater than 0                               | `Invalid amount in expense`                                | 400    |
| `date`       | Must be valid `dd-MM-yyyy`, not in the future        | `Invalid date, expected format dd-MM-yyyy` or `Future dates are not allowed` | 400    |
| `expenseType`| Must be `"credit"` or `"debit"`                      | `Invalid expense type accepts only credit or debit`        | 400    |

---

## Endpoints

### Get All Expenses

Returns a list of all expenses.

- **Method:** `GET`
- **Path:** `/all-expenses`
- **Request body:** none

**Success — `200 OK`:**

```json
[
  {
    "id": 1,
    "category": "Food",
    "amount": 1500,
    "date": "05-09-2026",
    "expenseType": "debit"
  }
]
```

**Error — `500`:**

```
server error
```

---

### Add Expense

Creates a new expense and returns the saved record.

- **Method:** `POST`
- **Path:** `/create-expense`
- **Content-Type:** `application/json`

**Request body:**

```json
{
  "category": "Food",
  "amount": 1500,
  "date": "05-09-2026",
  "expenseType": "debit"
}
```

**Success — `200 OK`:**

```json
{
  "id": 1,
  "category": "Food",
  "amount": 1500,
  "date": "05-09-2026",
  "expenseType": "debit"
}
```

**Validation error — `400`:**

```
Invalid amount in expense
```

**Server error — `500`:**

```
server error
```

---

### Update Expense

Updates an existing expense by ID.

- **Method:** `POST`
- **Path:** `/update-expense/{id}`
- **Content-Type:** `application/json`

**Request body:**

```json
{
  "category": "Transport",
  "amount": 300,
  "date": "07-09-2026",
  "expenseType": "debit"
}
```

**Success — `200 OK`:**

```json
{
  "id": 1,
  "category": "Transport",
  "amount": 300,
  "date": "07-09-2026",
  "expenseType": "debit"
}
```

**Validation error — `400`:**

```
Invalid amount in expense
```

**Expense not found — `404`:**

```
Expense not found
```

**Server error — `500`:**

```
server error
```

---

### Delete Expense

Deletes an expense by ID.

- **Method:** `DELETE`
- **Path:** `/delete-expense/{id}`
- **Request body:** none

**Success — `200 OK`:**

```
Expense deleted successfully
```

**Server error — `500`:**

```
server error
```
