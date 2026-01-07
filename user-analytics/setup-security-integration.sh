#!/bin/bash
#
# © 2026 Yevhen Tereshchenko
# All rights reserved.
#
#

# setup-security-integration.sh
# Script to automate security integration for all microservices

set -e

echo "════════════════════════════════════════════════════════════"
echo "  Healthcare Microservices - Security Integration Setup"
echo "════════════════════════════════════════════════════════════"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven first."
    exit 1
fi

print_success "Maven found: $(mvn --version | head -n 1)"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java 21."
    exit 1
fi

print_success "Java found: $(java -version 2>&1 | head -n 1)"

echo ""
print_status "Step 1: Building Common Security Library..."
echo "────────────────────────────────────────────────────────────"

cd common-security
mvn clean install -DskipTests
if [ $? -eq 0 ]; then
    print_success "Common Security Library built successfully"
else
    print_error "Failed to build Common Security Library"
    exit 1
fi
cd ..

echo ""
print_status "Step 2: Adding Security Dependency to Services..."
echo "────────────────────────────────────────────────────────────"

SERVICES=("patientservice" "appointment-service" "medicalrecordservice" "doctorservice" "billingservice" "prescriptionservice")

for service in "${SERVICES[@]}"; do
    if [ -d "$service" ]; then
        print_status "Processing $service..."

        # Check if dependency already exists
        if grep -q "common-security" "$service/pom.xml"; then
            print_warning "$service already has common-security dependency"
        else
            print_status "Adding common-security dependency to $service..."
            # Add dependency before </dependencies> tag
            sed -i.bak '/<\/dependencies>/i\
        <!-- Common Security Library -->\
        <dependency>\
            <groupId>com.example</groupId>\
            <artifactId>common-security</artifactId>\
            <version>1.0.0</version>\
        </dependency>\
' "$service/pom.xml"
            print_success "Added dependency to $service"
        fi
    else
        print_warning "Service directory not found: $service"
    fi
done

echo ""
print_status "Step 3: Updating Application Configurations..."
echo "────────────────────────────────────────────────────────────"

for service in "${SERVICES[@]}"; do
    if [ -d "$service/src/main/resources" ]; then
        CONFIG_FILE="$service/src/main/resources/application.yml"

        if [ -f "$CONFIG_FILE" ]; then
            # Check if auth config exists
            if grep -q "auth:" "$CONFIG_FILE"; then
                print_warning "$service already has auth configuration"
            else
                print_status "Adding auth configuration to $service..."
                cat >> "$CONFIG_FILE" << 'EOF'

# Auth Service Configuration
auth:
  service:
    url: http://auth-service:8087

# Feign Configuration
feign:
  circuitbreaker:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: BASIC
EOF
                print_success "Added auth configuration to $service"
            fi
        else
            print_warning "Config file not found: $CONFIG_FILE"
        fi
    fi
done

echo ""
print_status "Step 4: Building All Services..."
echo "────────────────────────────────────────────────────────────"

cd ..
mvn clean install -DskipTests
if [ $? -eq 0 ]; then
    print_success "All services built successfully"
else
    print_error "Failed to build services"
    exit 1
fi

echo ""
print_status "Step 5: Generating Integration Report..."
echo "────────────────────────────────────────────────────────────"

cat > integration-report.txt << EOF
Healthcare Microservices - Security Integration Report
Generated: $(date)
═══════════════════════════════════════════════════════════

✅ Completed Steps:
1. Built common-security library
2. Added security dependency to all services
3. Updated application configurations
4. Built all services successfully

📋 Service Status:
EOF

for service in "${SERVICES[@]}"; do
    if [ -d "$service" ]; then
        if grep -q "common-security" "$service/pom.xml"; then
            echo "  ✅ $service - Integrated" >> integration-report.txt
        else
            echo "  ❌ $service - Not Integrated" >> integration-report.txt
        fi
    else
        echo "  ⚠️  $service - Directory Not Found" >> integration-report.txt
    fi
done

cat >> integration-report.txt << EOF

📝 Next Steps:
1. Review and update Main Application classes with @EnableFeignClients
2. Add @RequirePermission annotations to controller methods
3. Test authentication with each service
4. Configure CORS settings if needed
5. Set up monitoring and logging

🔧 Manual Configuration Required:
- Update Main Application classes for each service
- Add security annotations to controllers
- Configure service-specific security rules
- Test with actual JWT tokens

📚 Documentation:
See integration-guide.md for detailed instructions

EOF

cat integration-report.txt

echo ""
print_success "════════════════════════════════════════════════════════════"
print_success "  Security Integration Setup Complete!"
print_success "════════════════════════════════════════════════════════════"
echo ""
print_status "Report saved to: integration-report.txt"
print_status "Next: Review integration-guide.md for manual configuration steps"
echo ""

# test-security-integration.sh
cat > test-security-integration.sh << 'TESTEOF'
#!/bin/bash
# test-security-integration.sh
# Script to test security integration

set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

print_test() {
    echo -e "${BLUE}[TEST]${NC} $1"
}

print_pass() {
    echo -e "${GREEN}[PASS]${NC} $1"
}

print_fail() {
    echo -e "${RED}[FAIL]${NC} $1"
}

echo "════════════════════════════════════════════════════════════"
echo "  Security Integration Tests"
echo "════════════════════════════════════════════════════════════"
echo ""

AUTH_URL="http://localhost:8087"
PATIENT_URL="http://localhost:8081"

# Test 1: Auth Service Health
print_test "Testing Auth Service health..."
if curl -s -f "$AUTH_URL/api/auth/health" > /dev/null; then
    print_pass "Auth Service is running"
else
    print_fail "Auth Service is not accessible"
    exit 1
fi

# Test 2: Register User
print_test "Registering test user..."
REGISTER_RESPONSE=$(curl -s -X POST "$AUTH_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser_'$(date +%s)'",
    "email": "test'$(date +%s)'@example.com",
    "password": "Test123456",
    "firstName": "Test",
    "lastName": "User",
    "userType": "PATIENT"
  }')

if echo "$REGISTER_RESPONSE" | grep -q "token"; then
    print_pass "User registered successfully"
    TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.token')
else
    print_fail "User registration failed"
    echo "$REGISTER_RESPONSE"
    exit 1
fi

# Test 3: Validate Token
print_test "Validating JWT token..."
VALIDATE_RESPONSE=$(curl -s -X POST "$AUTH_URL/api/auth/validate" \
  -H "Content-Type: application/json" \
  -d "{\"token\": \"$TOKEN\"}")

if echo "$VALIDATE_RESPONSE" | grep -q '"valid":true'; then
    print_pass "Token validation successful"
else
    print_fail "Token validation failed"
    exit 1
fi

# Test 4: Access Protected Endpoint (Patient Service)
print_test "Testing protected endpoint access..."
if curl -s -f -H "Authorization: Bearer $TOKEN" "$PATIENT_URL/api/patients/me" > /dev/null 2>&1; then
    print_pass "Protected endpoint accessible with token"
else
    print_fail "Cannot access protected endpoint (Service may not be running)"
fi

# Test 5: Access Without Token
print_test "Testing access without token (should fail)..."
if curl -s -f "$PATIENT_URL/api/patients/1" > /dev/null 2>&1; then
    print_fail "Endpoint accessible without token (Security bypass!)"
else
    print_pass "Endpoint properly protected"
fi

echo ""
echo "════════════════════════════════════════════════════════════"
echo "  All Tests Completed"
echo "════════════════════════════════════════════════════════════"
TESTEOF

chmod +x test-security-integration.sh

print_success "Test script created: test-security-integration.sh"

# Create docker-compose for testing
cat > docker-compose-test.yml << 'DOCKEREOF'
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_test_data:/var/lib/postgresql/data
      - ./init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 5s
      timeout: 3s
      retries: 5

  eureka-server:
    build: ./eureka-server
    ports:
      - "8761:8761"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
      interval: 10s
      timeout: 5s
      retries: 3

  auth-service:
    build: ./auth-service
    ports:
      - "8087:8087"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/analytics_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
    depends_on:
      postgres:
        condition: service_healthy
      eureka-server:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8087/actuator/health"]
      interval: 10s
      timeout: 5s
      retries: 3

  patient-service:
    build: ./patientservice
    ports:
      - "8081:8081"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/patient_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
      - AUTH_SERVICE_URL=http://auth-service:8087
    depends_on:
      auth-service:
        condition: service_healthy

volumes:
  postgres_test_data:
DOCKEREOF

print_success "Docker Compose test configuration created: docker-compose-test.yml"

echo ""
print_success "Setup completed! Run './test-security-integration.sh' to test integration"