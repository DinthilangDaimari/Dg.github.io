const express = require('express');
const cors = require('cors');

const app = express();

// Enable CORS for all domains and regions
app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization']
}));

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

let latestSms = null;

// Health check with IST time support
app.get('/', (req, res) => {
  const istTime = new Date().toLocaleString("en-US", { timeZone: "Asia/Kolkata" });
  res.status(200).json({
    status: 'online',
    region: 'India / Global',
    ist_time: istTime
  });
});

// Webhook endpoint supporting Indian & international payloads
app.post('/webhook/incoming-sms', (req, res) => {
  const sender = req.body.From || req.body.sender || req.body.from || req.body.mobile || 'Service';
  const text = req.body.Body || req.body.text || req.body.message || req.body.sms || '';
  
  // Extract 4-8 digit OTP codes (standard for Indian banks, IG, WhatsApp, etc.)
  const extractedCode = text.match(/\b\d{4,8}\b/)?.[0] || '123456';
  const istTime = new Date().toLocaleString("en-US", { timeZone: "Asia/Kolkata" });

  latestSms = {
    from: sender,
    body: text,
    code: extractedCode,
    timestamp_ist: istTime,
    country: sender.startsWith('+91') || sender.startsWith('91') ? 'India' : 'International'
  };

  res.status(200).json({ success: true, received: latestSms });
});

// API endpoint fetched by index.html
app.get('/api/get-latest-sms', (req, res) => {
  res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
  res.json(latestSms || { status: 'waiting', message: 'No SMS received yet' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running on port ${PORT} with India + Global webhook support`);
});
