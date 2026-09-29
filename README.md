# Dg.github.io
cat << 'EOF' > sms_listener.sh
#!/usr/bin/env bash

# Your live Render webhook URL
RENDER_URL="https://dg-github-io-u8dh.onrender.com/webhook/incoming-sms"

echo "Listening for incoming cellular SMS..."

LAST_SMS_ID=""

while true; do
  # Retrieve the latest single SMS from SIM card
  SMS_JSON=$(termux-sms-list -l 1)
  
  if [ "$SMS_JSON" != "[]" ] && [ -n "$SMS_JSON" ]; then
    BODY=$(echo "$SMS_JSON" | jq -r '.[0].body')
    SENDER=$(echo "$SMS_JSON" | jq -r '.[0].number')
    SMS_ID=$(echo "$SMS_JSON" | jq -r '.[0]._id')

    # Send to Render only if it's a new SMS
    if [ "$SMS_ID" != "$LAST_SMS_ID" ]; then
      echo "New SMS received from $SENDER: $BODY"
      
      curl -s -X POST "$RENDER_URL" \
        -H "Content-Type: application/json" \
        -d "{\"From\": \"$SENDER\", \"Body\": \"$BODY\"}"
      
      LAST_SMS_ID="$SMS_ID"
    fi
  fi
  sleep 3
done
EOF

chmod +x sms_listener.sh
