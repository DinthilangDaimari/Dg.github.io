cat << 'EOF' > sms_listener.sh
#!/usr/bin/env bash

# Live Render Webhook URL
RENDER_URL="[https://dg-github-io-u8dh.onrender.com/webhook/incoming-sms](https://dg-github-io-u8dh.onrender.com/webhook/incoming-sms)"

echo "=========================================="
echo " Starting Local SMS Gateway Listener... "
echo "=========================================="

LAST_SMS_ID=""

while true; do
  # Read the most recent incoming SMS from the physical SIM
  SMS_JSON=$(termux-sms-list -l 1)
  
  if [ "$SMS_JSON" != "[]" ] && [ -n "$SMS_JSON" ]; then
    BODY=$(echo "$SMS_JSON" | jq -r '.[0].body')
    SENDER=$(echo "$SMS_JSON" | jq -r '.[0].number')
    SMS_ID=$(echo "$SMS_JSON" | jq -r '.[0]._id')

    # Send payload to Render backend only if a new SMS is detected
    if [ "$SMS_ID" != "$LAST_SMS_ID" ]; then
      echo "[+] New SMS Received!"
      echo "    From: $SENDER"
      echo "    Message: $BODY"
      
      curl -s -X POST "$RENDER_URL" \
        -H "Content-Type: application/json" \
        -d "{\"From\": \"$SENDER\", \"Body\": \"$BODY\"}"
      
      echo "    [->] Forwarded to Render Webhook."
      LAST_SMS_ID="$SMS_ID"
    fi
  fi
  
  # Check for incoming SMS every 3 seconds
  sleep 3
done
EOF

# Grant execution permissions to the script
chmod +x sms_listener.sh
