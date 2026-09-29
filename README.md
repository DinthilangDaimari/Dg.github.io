# Dg.github.io
# Local SMS Gateway Setup Guide (Android + Termux)

This guide configures a physical Android phone running Termux as a local hardware SMS gateway to forward real carrier OTPs to your live Render backend and display them on your GitHub Pages dashboard.

---

## Prerequisites

1. An Android phone with an active SIM card capable of receiving SMS messages.
2. **Termux** and **Termux:API** installed on the device (available on F-Droid).

---

## Step 1: Install Termux Packages & Grant Permissions

Open **Termux** on your Android device and run:

```bash
# Update package repositories and install required tools
pkg update -y && pkg install termux-api curl jq -y

# Grant storage and system permissions
termux-setup-storage
# Local SMS Gateway Setup Guide (Android + Termux)

This guide configures a physical Android phone running Termux as a local hardware SMS gateway to forward real carrier OTPs to your live Render backend and display them on your GitHub Pages dashboard.

---

## Prerequisites

1. An Android phone with an active SIM card capable of receiving SMS messages.
2. **Termux** and **Termux:API** installed on the device (available on F-Droid).

---

## Step 1: Update Packages & Request Storage Access

Open **Termux** on your phone, copy this command, paste it, and hit Enter:

```bash
pkg update -y && pkg install termux-api curl jq -y
termux-setup-storage
