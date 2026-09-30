# J.A.R.V.I.S. AI Web Assistant

An interactive, voice-enabled AI assistant inspired by JARVIS, built with Next.js, FastAPI, and OpenAI.

## 🚀 Features
- **Voice-to-Text Command**: Built-in speech recognition interface.
- **Realistic Voice Output**: Speech synthesis returning custom responses.
- **Cyberpunk / Sci-Fi HUD**: Interactive orb visualizer and dark-themed UI.

## 🛠️ Setup Instructions

### 1. Backend (FastAPI)
```bash
cd backend
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```
Copy `.env.example` to `.env` and insert your `OPENAI_API_KEY`:
```bash
python main.py
```
*Backend runs on `http://localhost:8000`*

### 2. Frontend (Next.js)
```bash
cd frontend
npm install
npm run dev
```
*Frontend runs on `http://localhost:3000`*
