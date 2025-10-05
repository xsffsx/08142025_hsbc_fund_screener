#!/usr/bin/env bash
set -euo pipefail

# Python venv (optional)
if [ ! -d "venv_api" ]; then
  python3 -m venv venv_api
fi
source venv_api/bin/activate
pip install --upgrade pip
pip install -r requirements_hsbc_fund.txt

# Environment
export HSBC_DB_URI=${HSBC_DB_URI:-"postgresql+psycopg://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund"}
export LMSTUDIO_BASE_URL=${LMSTUDIO_BASE_URL:-"http://localhost:1234/v1"}
export LMSTUDIO_MODEL=${LMSTUDIO_MODEL:-"qwen/qwen3-30b-a3b-2507"}
export OPENAI_API_KEY=${OPENAI_API_KEY:-"lm-studio"}

# Run
exec python hsbc_fund_api_server.py

