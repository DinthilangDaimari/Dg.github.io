const express = require('express');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// In-memory store for generated temporary inboxes and received emails
let inboxes = {};

// Helper: Generate a random temporary email address
function generateRandomEmail() {
  const chars = 'abcdefghijklmnopqrstuvwxyz0123456789';
  let prefix = '';
  for (let i = 0; i < 8; i++) {
    prefix += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return `${prefix}@mail-gateway.net`; // Public placeholder domain
}

// Endpoint 1: Generate a new temp email
app.get('/api/generate-email', (req, res) => {
  const newEmail = generateRandomEmail();
  inboxes[newEmail] = [];
  res.json({ email: newEmail });
});

// Endpoint 2: Incoming Webhook for emails (e.g., from SendGrid, Mailgun, or Cloudflare Email Routing)
app.post('/webhook/incoming-email', (req, res) => {
  const { recipient, sender, subject, body } = req.body;

  if (recipient && inboxes[recipient]) {
    // Extract potential 6-digit OTP codes from email body
    const otpMatch = body ? body.match(/\b\d{6}\b/) : null;
    const extractedOtp = otpMatch ? otpMatch[0] : null;

    const emailData = {
      id: Date.now(),
      sender: sender || 'Unknown Sender',
      subject: subject || 'No Subject',
      body: body || '',
      otp: extractedOtp,
      timestamp: new Date().toISOString()
    };

    inboxes[recipient].unshift(emailData);
    return res.status(200).json({ status: 'success', message: 'Email stored' });
  }

  res.status(400).json({ status: 'error', message: 'Inbox not found' });
});

// Endpoint 3: Fetch emails for a specific address
app.get('/api/inbox', (req, res) => {
  const { email } = req.query;
  if (!email || !inboxes[email]) {
    return res.json({ emails: [] });
  }
  res.json({ emails: inboxes[email] });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Temp Email Backend listening on port ${PORT}`);
});
