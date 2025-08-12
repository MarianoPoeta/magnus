# 🔐 JWT Authentication Testing Guide

## 🚀 Quick Start

### **1. Start the Backend**

**Linux/macOS:**
```bash
chmod +x start-dev.sh
./start-dev.sh
```

**Windows:**
```powershell
.\start-dev.ps1
```

**Manual Start:**
```bash
./mvnw spring-boot:run
```

### **2. Default Test Users**

The application comes with pre-configured test users:

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin` | ADMIN | Full system access |
| `user` | `user` | USER | Regular user access |

## 🧪 Testing Authentication

### **1. Test Login via Swagger UI**

1. Open: http://localhost:8080/swagger-ui.html
2. Navigate to `Authentication Controller`
3. Use `/api/authenticate` endpoint
4. Test with:
   ```json
   {
     "username": "admin",
     "password": "admin",
     "rememberMe": false
   }
   ```

### **2. Test Login via cURL**

```bash
curl -X POST "http://localhost:8080/api/authenticate" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin",
    "rememberMe": false
  }'
```

**Expected Response:**
```json
{
  "id_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYxMjQwNjI4M30...."
}
```

### **3. Test Protected Endpoints**

Once you have the JWT token, test protected endpoints:

```bash
# Get the token from login response
TOKEN="your_jwt_token_here"

# Test protected endpoint
curl -X GET "http://localhost:8080/api/budgets" \
  -H "Authorization: Bearer $TOKEN"
```

### **4. Test with Frontend**

Your frontend test panel should now work correctly:

1. Navigate to: http://localhost:3000/workflow-test
2. Click "Test Backend Connection" ✅
3. Click "Test Login" ✅
4. Click "Load Budgets" ✅

## 🔧 JWT Configuration Details

### **Development Configuration**
- **Secret**: Custom development secret (256+ bits, Base64 encoded)
- **Token Validity**: 24 hours (86400 seconds)
- **Remember Me**: 30 days (2592000 seconds)
- **Algorithm**: HS512

### **Production Configuration**
- **Secret**: Uses environment variable `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET`
- **Token Validity**: Configurable via environment variables
- **Security**: Must use a unique, secure secret in production

## 🛡️ Security Best Practices

### **Development**
✅ Use the provided development secret  
✅ Enable CORS for localhost:3000  
✅ Use HTTPS in production  
✅ Rotate JWT secrets regularly  

### **Production**
```bash
# Generate a secure JWT secret for production
openssl rand -base64 64

# Set environment variables
export JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET="your_secure_secret_here"
export JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS=3600
```

## 🐛 Troubleshooting

### **Common Issues**

**1. "Unauthorized" Error**
- ✅ Check if backend is running on port 8080
- ✅ Verify default users exist (run liquibase migrations)
- ✅ Check JWT secret consistency
- ✅ Ensure CORS is properly configured

**2. "Invalid JWT Signature"**
- ✅ Check if JWT secret matches in all config files
- ✅ Restart the backend after changing JWT configuration
- ✅ Clear any cached tokens in frontend

**3. "Token Expired"**
- ✅ Login again to get a fresh token
- ✅ Check token validity settings
- ✅ Verify system time is correct

### **Debugging Commands**

```bash
# Check if backend is running
curl -I http://localhost:8080/api/authenticate

# Check database connection
mysql -u root -p -e "USE magnus; SELECT * FROM jhi_user LIMIT 2;"

# Check JWT token content (decode without verification)
echo "your_token_here" | cut -d. -f2 | base64 --decode
```

## 📊 Testing Checklist

- [ ] Backend starts without errors
- [ ] MySQL database is accessible
- [ ] Swagger UI loads at http://localhost:8080/swagger-ui.html
- [ ] Login with admin/admin works
- [ ] JWT token is returned in login response
- [ ] Protected endpoints accept JWT token
- [ ] Frontend can connect to backend
- [ ] CORS allows requests from localhost:3000

## 🔗 Useful Endpoints

| Endpoint | Method | Purpose | Auth Required |
|----------|--------|---------|---------------|
| `/api/authenticate` | POST | Login and get JWT token | No |
| `/api/authenticate` | GET | Check if authenticated | Yes |
| `/api/account` | GET | Get current user info | Yes |
| `/api/budgets` | GET | List budgets | Yes |
| `/api/users` | GET | List users (admin only) | Yes (Admin) |

## 🎯 Next Steps

1. **Test Authentication**: Follow the steps above to verify JWT is working
2. **Test Frontend Integration**: Use the workflow test panel
3. **Create Custom Users**: Use the admin panel to create users with different roles
4. **Implement Role-based Access**: Test different user roles (admin, sales, logistics, cook)
5. **Deploy to Production**: Configure secure JWT secrets for production

---

💡 **Pro Tip**: Keep this guide handy for troubleshooting authentication issues during development! 