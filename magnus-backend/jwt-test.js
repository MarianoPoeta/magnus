const https = require('https');
const http = require('http');

// Test configuration
const TEST_CONFIG = {
  dev: {
    baseUrl: 'http://localhost:8080',
    jwtSecret: 'N2Y1NzE2ZjY3YzJmODFiZGI2ODM2ZjQzNDJkMjIwOWM4YTY3NzUzNjQ4YzQ5NmU4NjZmZTk3NDYyZTk1MzY4OTE4ZGFkZjNlYzBkMzZhYmE5NjEyZWNkODhlYzA1MmZlNGNhNzY1MGY1YjkwNGZjODg1ZDBkZWFhOTA4N2Q=',
    tokenValiditySeconds: 86400
  },
  prod: {
    baseUrl: process.env.PRODUCTION_URL || 'https://magnus.example.com',
    jwtSecret: process.env.JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET || 'ODQ2NWU4Zjg0OWFjNzM1ZDI5MGQ4ZGY2NGViYWUwNTJlNWNmNzc3MGU4MDBhZWZkYTFkN2NkOTI4ZWE4MTNhMDhhZGE2MzI5YjgxMTI1NTdkYmMzMjU2ODA4MWJmODI2MzZiODgxNzYwYzQ4ZDA3NTkwMjZlMzg1ODE4ZTUwYzQ=',
    tokenValiditySeconds: 86400
  }
};

// Test credentials (these should be in the database)
const TEST_USERS = {
  admin: { username: 'admin', password: 'admin' },
  user: { username: 'user', password: 'user' }
};

// JWT Testing Functions
function makeRequest(options, data = null) {
  return new Promise((resolve, reject) => {
    const protocol = options.protocol === 'https:' ? https : http;
    
    const req = protocol.request(options, (res) => {
      let body = '';
      res.on('data', chunk => body += chunk);
      res.on('end', () => {
        try {
          const result = {
            statusCode: res.statusCode,
            headers: res.headers,
            body: body ? JSON.parse(body) : null
          };
          resolve(result);
        } catch (e) {
          resolve({
            statusCode: res.statusCode,
            headers: res.headers,
            body: body
          });
        }
      });
    });

    req.on('error', reject);
    
    if (data) {
      req.write(JSON.stringify(data));
    }
    
    req.end();
  });
}

async function testAuthentication(environment, baseUrl) {
  console.log(`\n🔐 Testing JWT Authorization - ${environment.toUpperCase()} Environment`);
  console.log(`📍 Base URL: ${baseUrl}`);
  console.log('━'.repeat(80));

  const results = {
    environment,
    baseUrl,
    tests: []
  };

  // Test 1: Health check
  try {
    console.log('\n1️⃣  Testing health endpoint...');
    const healthOptions = {
      hostname: new URL(baseUrl).hostname,
      port: new URL(baseUrl).port || (baseUrl.startsWith('https') ? 443 : 80),
      path: '/management/health',
      method: 'GET',
      protocol: new URL(baseUrl).protocol,
      headers: { 'Content-Type': 'application/json' }
    };

    const healthResult = await makeRequest(healthOptions);
    const healthPassed = healthResult.statusCode === 200;
    
    console.log(`   Status: ${healthResult.statusCode} ${healthPassed ? '✅' : '❌'}`);
    results.tests.push({
      name: 'Health Check',
      passed: healthPassed,
      statusCode: healthResult.statusCode,
      details: healthResult.body
    });
  } catch (error) {
    console.log(`   Error: ${error.message} ❌`);
    results.tests.push({
      name: 'Health Check',
      passed: false,
      error: error.message
    });
  }

  // Test 2: Unauthenticated access to protected endpoint
  try {
    console.log('\n2️⃣  Testing unauthenticated access to protected endpoint...');
    const unAuthOptions = {
      hostname: new URL(baseUrl).hostname,
      port: new URL(baseUrl).port || (baseUrl.startsWith('https') ? 443 : 80),
      path: '/api/account',
      method: 'GET',
      protocol: new URL(baseUrl).protocol,
      headers: { 'Content-Type': 'application/json' }
    };

    const unAuthResult = await makeRequest(unAuthOptions);
    const unAuthPassed = unAuthResult.statusCode === 401;
    
    console.log(`   Status: ${unAuthResult.statusCode} ${unAuthPassed ? '✅' : '❌'}`);
    console.log(`   Expected: 401 Unauthorized`);
    
    results.tests.push({
      name: 'Unauthenticated Access Blocked',
      passed: unAuthPassed,
      statusCode: unAuthResult.statusCode,
      expected: 401
    });
  } catch (error) {
    console.log(`   Error: ${error.message} ❌`);
    results.tests.push({
      name: 'Unauthenticated Access Blocked',
      passed: false,
      error: error.message
    });
  }

  // Test 3: Authentication endpoint accessibility
  try {
    console.log('\n3️⃣  Testing authentication endpoint accessibility...');
    const authCheckOptions = {
      hostname: new URL(baseUrl).hostname,
      port: new URL(baseUrl).port || (baseUrl.startsWith('https') ? 443 : 80),
      path: '/api/authenticate',
      method: 'GET',
      protocol: new URL(baseUrl).protocol,
      headers: { 'Content-Type': 'application/json' }
    };

    const authCheckResult = await makeRequest(authCheckOptions);
    // Should return 401 or 204 depending on configuration
    const authCheckPassed = [204, 401].includes(authCheckResult.statusCode);
    
    console.log(`   Status: ${authCheckResult.statusCode} ${authCheckPassed ? '✅' : '❌'}`);
    console.log(`   Expected: 204 (No Content) or 401 (Unauthorized)`);
    
    results.tests.push({
      name: 'Authentication Endpoint Accessible',
      passed: authCheckPassed,
      statusCode: authCheckResult.statusCode,
      expected: '204 or 401'
    });
  } catch (error) {
    console.log(`   Error: ${error.message} ❌`);
    results.tests.push({
      name: 'Authentication Endpoint Accessible',
      passed: false,
      error: error.message
    });
  }

  // Test 4: Login attempt
  try {
    console.log('\n4️⃣  Testing login with admin credentials...');
    const loginOptions = {
      hostname: new URL(baseUrl).hostname,
      port: new URL(baseUrl).port || (baseUrl.startsWith('https') ? 443 : 80),
      path: '/api/authenticate',
      method: 'POST',
      protocol: new URL(baseUrl).protocol,
      headers: { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    };

    const loginData = {
      username: TEST_USERS.admin.username,
      password: TEST_USERS.admin.password,
      rememberMe: false
    };

    const loginResult = await makeRequest(loginOptions, loginData);
    const loginPassed = loginResult.statusCode === 200 && loginResult.body && loginResult.body.id_token;
    
    console.log(`   Status: ${loginResult.statusCode} ${loginPassed ? '✅' : '❌'}`);
    if (loginPassed) {
      console.log(`   Token received: ${loginResult.body.id_token.substring(0, 50)}...`);
      
      // Test 5: Access protected endpoint with token
      try {
        console.log('\n5️⃣  Testing protected endpoint access with valid token...');
        const protectedOptions = {
          hostname: new URL(baseUrl).hostname,
          port: new URL(baseUrl).port || (baseUrl.startsWith('https') ? 443 : 80),
          path: '/api/account',
          method: 'GET',
          protocol: new URL(baseUrl).protocol,
          headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${loginResult.body.id_token}`
          }
        };

        const protectedResult = await makeRequest(protectedOptions);
        const protectedPassed = protectedResult.statusCode === 200;
        
        console.log(`   Status: ${protectedResult.statusCode} ${protectedPassed ? '✅' : '❌'}`);
        if (protectedPassed && protectedResult.body) {
          console.log(`   User: ${protectedResult.body.login || 'N/A'}`);
          console.log(`   Authorities: ${(protectedResult.body.authorities || []).join(', ')}`);
        }
        
        results.tests.push({
          name: 'Protected Endpoint with Valid Token',
          passed: protectedPassed,
          statusCode: protectedResult.statusCode,
          userInfo: protectedResult.body
        });

        // Test 6: Token structure validation
        console.log('\n6️⃣  Validating JWT token structure...');
        const token = loginResult.body.id_token;
        const tokenParts = token.split('.');
        const tokenStructureValid = tokenParts.length === 3;
        
        console.log(`   Token parts: ${tokenParts.length} ${tokenStructureValid ? '✅' : '❌'}`);
        
        if (tokenStructureValid) {
          try {
            const header = JSON.parse(Buffer.from(tokenParts[0], 'base64').toString());
            const payload = JSON.parse(Buffer.from(tokenParts[1], 'base64').toString());
            
            console.log(`   Algorithm: ${header.alg}`);
            console.log(`   Subject: ${payload.sub}`);
            console.log(`   Expires: ${new Date(payload.exp * 1000).toISOString()}`);
            console.log(`   Authorities: ${payload.auth || 'N/A'}`);
            
            results.tests.push({
              name: 'JWT Token Structure Valid',
              passed: true,
              tokenInfo: { header, payload }
            });
          } catch (e) {
            console.log(`   Token parsing error: ${e.message} ❌`);
            results.tests.push({
              name: 'JWT Token Structure Valid',
              passed: false,
              error: e.message
            });
          }
        } else {
          results.tests.push({
            name: 'JWT Token Structure Valid',
            passed: false,
            error: 'Invalid token structure'
          });
        }

      } catch (error) {
        console.log(`   Error: ${error.message} ❌`);
        results.tests.push({
          name: 'Protected Endpoint with Valid Token',
          passed: false,
          error: error.message
        });
      }
    } else {
      console.log(`   Error: ${loginResult.body ? JSON.stringify(loginResult.body) : 'No response body'}`);
    }
    
    results.tests.push({
      name: 'Admin Login',
      passed: loginPassed,
      statusCode: loginResult.statusCode,
      response: loginResult.body
    });

  } catch (error) {
    console.log(`   Error: ${error.message} ❌`);
    results.tests.push({
      name: 'Admin Login',
      passed: false,
      error: error.message
    });
  }

  // Test 7: Invalid token test
  try {
    console.log('\n7️⃣  Testing access with invalid token...');
    const invalidTokenOptions = {
      hostname: new URL(baseUrl).hostname,
      port: new URL(baseUrl).port || (baseUrl.startsWith('https') ? 443 : 80),
      path: '/api/account',
      method: 'GET',
      protocol: new URL(baseUrl).protocol,
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': 'Bearer invalid.token.here'
      }
    };

    const invalidTokenResult = await makeRequest(invalidTokenOptions);
    const invalidTokenPassed = invalidTokenResult.statusCode === 401;
    
    console.log(`   Status: ${invalidTokenResult.statusCode} ${invalidTokenPassed ? '✅' : '❌'}`);
    console.log(`   Expected: 401 Unauthorized`);
    
    results.tests.push({
      name: 'Invalid Token Rejected',
      passed: invalidTokenPassed,
      statusCode: invalidTokenResult.statusCode,
      expected: 401
    });
  } catch (error) {
    console.log(`   Error: ${error.message} ❌`);
    results.tests.push({
      name: 'Invalid Token Rejected',
      passed: false,
      error: error.message
    });
  }

  return results;
}

// Main test runner
async function runJWTTests() {
  console.log('🚀 Magnus Backend JWT Authorization Test Suite');
  console.log('━'.repeat(80));
  
  const allResults = [];

  // Test Development Environment
  try {
    const devResults = await testAuthentication('development', TEST_CONFIG.dev.baseUrl);
    allResults.push(devResults);
  } catch (error) {
    console.log(`❌ Development environment test failed: ${error.message}`);
    allResults.push({
      environment: 'development',
      error: error.message,
      tests: []
    });
  }

  // Test Production Environment (if configured)
  if (process.env.TEST_PRODUCTION === 'true') {
    try {
      const prodResults = await testAuthentication('production', TEST_CONFIG.prod.baseUrl);
      allResults.push(prodResults);
    } catch (error) {
      console.log(`❌ Production environment test failed: ${error.message}`);
      allResults.push({
        environment: 'production',
        error: error.message,
        tests: []
      });
    }
  }

  // Summary Report
  console.log('\n📊 TEST SUMMARY REPORT');
  console.log('━'.repeat(80));
  
  allResults.forEach(envResult => {
    console.log(`\n${envResult.environment.toUpperCase()} Environment:`);
    console.log(`URL: ${envResult.baseUrl || 'N/A'}`);
    
    if (envResult.error) {
      console.log(`❌ Environment Error: ${envResult.error}`);
      return;
    }
    
    const passedTests = envResult.tests.filter(test => test.passed).length;
    const totalTests = envResult.tests.length;
    const successRate = totalTests > 0 ? Math.round((passedTests / totalTests) * 100) : 0;
    
    console.log(`Tests Passed: ${passedTests}/${totalTests} (${successRate}%)`);
    
    envResult.tests.forEach(test => {
      const status = test.passed ? '✅' : '❌';
      console.log(`  ${status} ${test.name}`);
      if (!test.passed && test.error) {
        console.log(`     Error: ${test.error}`);
      }
    });
  });

  // Configuration Analysis
  console.log('\n🔧 CONFIGURATION ANALYSIS');
  console.log('━'.repeat(80));
  
  Object.entries(TEST_CONFIG).forEach(([env, config]) => {
    console.log(`\n${env.toUpperCase()}:`);
    console.log(`  Base URL: ${config.baseUrl}`);
    console.log(`  JWT Secret Length: ${Buffer.from(config.jwtSecret, 'base64').length * 8} bits`);
    console.log(`  Token Validity: ${config.tokenValiditySeconds / 3600} hours`);
    
    // Validate JWT secret strength
    const secretBytes = Buffer.from(config.jwtSecret, 'base64').length;
    const isSecretStrong = secretBytes >= 32; // 256 bits minimum
    console.log(`  Secret Strength: ${isSecretStrong ? '✅ Strong' : '⚠️  Weak'} (${secretBytes * 8} bits)`);
  });

  console.log('\n🎯 RECOMMENDATIONS');
  console.log('━'.repeat(80));
  
  // Provide recommendations based on test results
  const devResults = allResults.find(r => r.environment === 'development');
  const prodResults = allResults.find(r => r.environment === 'production');
  
  if (devResults && devResults.tests.length > 0) {
    const devSuccessRate = devResults.tests.filter(t => t.passed).length / devResults.tests.length;
    if (devSuccessRate < 1) {
      console.log('📋 Development Issues:');
      devResults.tests.filter(t => !t.passed).forEach(test => {
        console.log(`  • ${test.name}: ${test.error || 'Check logs for details'}`);
      });
    } else {
      console.log('✅ Development environment JWT is working correctly');
    }
  }
  
  if (prodResults && prodResults.tests.length > 0) {
    const prodSuccessRate = prodResults.tests.filter(t => t.passed).length / prodResults.tests.length;
    if (prodSuccessRate < 1) {
      console.log('📋 Production Issues:');
      prodResults.tests.filter(t => !t.passed).forEach(test => {
        console.log(`  • ${test.name}: ${test.error || 'Check logs for details'}`);
      });
    } else {
      console.log('✅ Production environment JWT is working correctly');
    }
  }

  console.log('\n📋 General Recommendations:');
  console.log('• Ensure backend is running before testing');
  console.log('• Verify database connectivity and user accounts');
  console.log('• Check CORS configuration for frontend integration');
  console.log('• Monitor JWT token expiration and refresh logic');
  console.log('• Use environment variables for production secrets');
  console.log('• Implement proper error handling in frontend');
  
  console.log('\n🏁 Test Complete');
}

// Run the tests
if (require.main === module) {
  runJWTTests().catch(console.error);
}

module.exports = { runJWTTests, testAuthentication }; 