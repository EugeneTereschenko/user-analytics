# Billing Module - Setup Instructions

## Overview
This billing module provides comprehensive invoice and payment management for the healthcare frontend application.

## Components Included

1. **billing.model.ts** - TypeScript interfaces and enums
2. **billing.service.ts** - API service with all endpoints
3. **invoice-list.component** - Main list view with filtering
4. **invoice-form.component** - Create/edit invoices
5. **payment-form.component** - Payment entry form
6. **billing-dashboard.component** - Statistics overview

## Features Implemented

✅ Complete billing/invoice management
✅ Invoice CRUD operations
✅ Payment processing
✅ Search and filtering (by status, patient)
✅ Pagination
✅ Dynamic invoice items with calculations
✅ Real-time totals calculation (subtotal, tax, discount)
✅ Payment history tracking
✅ Insurance information
✅ Status management with color coding
✅ Overdue invoice detection
✅ Print functionality
✅ Responsive Bootstrap 5 design
✅ Loading states and error handling
✅ Dashboard with statistics
✅ JWT token authentication

## Setup Instructions

### 1. Install Bootstrap Icons

The components use Bootstrap Icons. Add the following to your `index.html` file in the `<head>` section:

```html
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
```

Or install via npm:

```bash
npm install bootstrap-icons
```

### 2. Bootstrap Configuration

Ensure Bootstrap 5 is properly configured in your project. If not already set up:

```bash
npm install bootstrap
```

Add to your `angular.json` file in the `styles` array:

```json
"styles": [
  "node_modules/bootstrap/dist/css/bootstrap.min.css",
  "src/styles.css"
],
"scripts": [
  "node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"
]
```

### 3. Environment Configuration

Update the API URL in `billing.service.ts` to match your backend:

```typescript
private readonly API_URL = 'http://localhost:8082/billing-service/api/v1/billing';
```

Or create environment files for different configurations.

### 4. Routes Configuration

Routes have been automatically added to `app.routes.ts`:

- `/billing/dashboard` - Billing dashboard
- `/billing/invoices` - Invoice list
- `/billing/invoices/create` - Create new invoice
- `/billing/invoices/edit/:id` - Edit existing invoice

### 5. Navigation

Add billing links to your navigation menu:

```html
<a routerLink="/billing/dashboard" class="nav-link">
  <i class="bi bi-receipt me-2"></i>Billing
</a>
```

## API Endpoints Used

The service connects to the following backend endpoints:

- `POST /invoices` - Create invoice
- `GET /invoices/{id}` - Get invoice by ID
- `GET /invoices` - List all invoices (paginated)
- `GET /invoices/patient/{patientId}` - Get patient invoices
- `GET /invoices/status/{status}` - Filter by status
- `GET /invoices/overdue` - Get overdue invoices
- `GET /invoices/outstanding/total` - Get total outstanding
- `GET /invoices/search` - Search invoices
- `PUT /invoices/{id}` - Update invoice
- `PATCH /invoices/{id}/send` - Send invoice
- `POST /invoices/{invoiceId}/payments` - Add payment
- `PATCH /invoices/{id}/cancel` - Cancel invoice
- `DELETE /invoices/{id}` - Delete invoice

## Usage Examples

### Creating an Invoice

1. Navigate to `/billing/invoices/create`
2. Fill in patient information
3. Add invoice items (consultations, procedures, medications, etc.)
4. System automatically calculates totals
5. Add tax and discount if needed
6. Save the invoice

### Adding a Payment

1. Go to invoice list
2. Click the payment button (💰 icon) on any invoice
3. Enter payment details (amount, method, date)
4. Submit the payment
5. Invoice status updates automatically

### Dashboard View

The dashboard shows:
- Total revenue (paid invoices)
- Total outstanding balance
- Number of overdue invoices
- Number of pending invoices
- Recent invoices list
- Quick action buttons

## Invoice Statuses

- **DRAFT** - Invoice being prepared
- **PENDING** - Awaiting payment
- **SENT** - Sent to patient
- **PARTIALLY_PAID** - Partial payment received
- **PAID** - Fully paid
- **OVERDUE** - Past due date
- **CANCELLED** - Invoice cancelled
- **REFUNDED** - Payment refunded

## Item Types

- CONSULTATION
- PROCEDURE
- MEDICATION
- LAB_TEST
- IMAGING
- ROOM_CHARGE
- SURGERY
- EMERGENCY_SERVICE
- THERAPY
- VACCINATION
- EQUIPMENT
- OTHER

## Payment Methods

- CASH
- CREDIT_CARD
- DEBIT_CARD
- BANK_TRANSFER
- CHECK
- INSURANCE
- MOBILE_PAYMENT
- ONLINE_PAYMENT
- OTHER

## Troubleshooting

### Bootstrap Modal Not Working

Ensure Bootstrap JS is loaded. The payment form modal requires Bootstrap's JavaScript bundle.

### CORS Errors

Configure CORS on your backend to allow requests from your frontend origin.

### Authentication Issues

Ensure JWT token is stored in localStorage with key 'token'. The service automatically includes it in request headers.

### Data Not Loading

1. Check browser console for errors
2. Verify API URL is correct
3. Ensure backend service is running
4. Check network tab for failed requests

## Customization

### Changing Colors

Modify the status badge colors in `invoice-list.component.ts`:

```typescript
getStatusClass(status: InvoiceStatus): string {
  // Customize colors here
}
```

### Adding Fields

1. Update `billing.model.ts` interfaces
2. Add form fields in component HTML
3. Update service methods if needed

### Custom Calculations

Modify calculation logic in `invoice-form.component.ts`:

```typescript
calculateTotals(): void {
  // Add custom calculation logic
}
```

## Support

For issues or questions:
1. Check the browser console for errors
2. Verify all dependencies are installed
3. Ensure backend API is running and accessible
4. Review the component code for any custom modifications needed

## Future Enhancements

Potential improvements:
- PDF invoice generation
- Email invoice sending
- Payment reminders
- Recurring invoices
- Bulk operations
- Advanced reporting
- Insurance claim integration
- Multi-currency support
