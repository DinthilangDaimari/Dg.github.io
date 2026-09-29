const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

let latestSms = null;

app.post('/webhook/incoming-sms', (req, res) => {
  const sender = req.body.From || req.body.sender || 'Service';
  const text = req.body.Body || req.body.text || '';
  const extractedCode = text.match(/\b\d{4,8}\b/)?.[0] || '123456';

  latestSms = {
    from: sender,
    body: text,
    code: extractedCode,
    timestamp: new Date()
  };

  res.status(200).send('<Response></Response>');
});

app.get('/api/get-latest-sms', (req, res) => {
  res.json(latestSms || { status: 'waiting' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
