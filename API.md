# Expense Tracker Backend API Contracts

Base URL: `http://localhost:8000`

## Common Models

### Expense

| Field        | Type   | Notes                                   |
|--------------|--------|-----------------------------------------|
| `id`         | number | Auto-generated, set by the backend      |
| `category`   | string |                                         |
| `amount`     | number | Integer                                 |
| `date`       | string |                                         |
| `expenseType`| string |                                         |

---

## Get All Expenses

Returns a list of all expenses.

- **Method:** `GET`
- **Path:** `/get-expenses`
- **Request body:** none
- **Response:** `200 OK`

```json
[
  {
    "id": 1,
    "category": "Food",
    "amount": 1500,
    "date": "05-09-2026,  Saturday",
    "expenseType": "debit"
  }
]
```

---

## Add Expense

Creates a new expense and returns the saved record.

- **Method:** `POST`
- **Path:** `/add-expense`
- **Content-Type:** `application/json`
- **Request body:**

```json
{
  "category": "Food",
  "amount": 1500,
  "date": "05-09-2026,  Saturday",
  "expenseType": "debit"
}
```

- **Response:** `200 OK`

```json
{
  "id": 1,
  "category": "Food",
  "amount": 1500,
  "date": "05-09-2026,  Saturday",
  "expenseType": "debit"
}
```
