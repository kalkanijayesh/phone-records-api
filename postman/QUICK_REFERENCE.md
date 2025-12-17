# Postman Collection - Quick Reference

## 🚀 Quick Start (3 Steps)

1. **Import Collection & Environment**
   - Open Postman → Click **Import**
   - Select both JSON files from `/postman` folder
   - Click **Import**

2. **Select Environment**
   - Top-right dropdown → Select **Phone Records API - Local Environment**

3. **Run Tests**
   - Click on collection → Click **Run**
   - Or run individual requests

---

## 📊 Test Summary

| Category | Tests | Purpose |
|----------|-------|---------|
| ✅ Positive | 6 | Valid requests |
| ❌ Validation Errors | 9 | Invalid input |
| ❌ Not Found | 3 | Missing records |
| 🔍 Edge Cases | 4 | Boundaries |
| ⚡ Performance | 2 | Speed checks |
| **TOTAL** | **24** | **Complete coverage** |

---

## 🎯 Key Test Scenarios

### Positive ✅
```
1. Create Valid US Phone    → 201 Created
2. Create Valid UK Phone    → 201 Created
3. Get All Records          → 200 OK
4. Get Record by ID         → 200 OK
```

### Negative ❌
```
5. Invalid Phone Number     → 400 Bad Request
6. Missing Name             → 400 Bad Request
7. Missing Phone Number     → 400 Bad Request
8. Empty JSON Body          → 400 Bad Request
9. Non-Existing ID          → 404 Not Found
```

---

## 🔧 Variables

| Variable | Value | Auto-Set? |
|----------|-------|-----------|
| baseUrl | http://localhost:8080 | No |
| recordId | (dynamic) | Yes ✅ |

---

## ⚡ Quick Commands

### Run Full Collection
```
Collection → Run → Run Phone Records API
```

### Run Single Folder
```
Folder → Hover → Click ▶ → Run
```

### View Test Results
```
Collection Runner → View Results
```

---

## 🔍 Troubleshooting

| Issue | Solution |
|-------|----------|
| Connection refused | Start API: `docker compose up` |
| Wrong environment | Select environment from dropdown |
| Tests failing | Check baseUrl matches your setup |
| Validation not working | Set PHONE_VALIDATION_API_KEY |

---

## 📝 Expected Results

✅ **23-24 tests pass** (96-100%)

Most common failure: Very long name test (depends on DB constraints)

---

## 🎨 Test Features

- ✅ Automated assertions
- ✅ Response validation
- ✅ Status code checks
- ✅ Data structure verification
- ✅ Performance monitoring
- ✅ Environment variables
- ✅ Pre/post request scripts

---

## 💡 Pro Tips

1. Run **positive scenarios first** to create test data
2. Use **Collection Runner** for full test suite
3. Check **Console** (View → Show Postman Console) for debugging
4. Export results for reporting
5. Use **Newman** for CI/CD integration

---

## 📚 Full Documentation

See `POSTMAN_GUIDE.md` for complete details

---

**Version**: 1.0 | **Last Updated**: Dec 2024
