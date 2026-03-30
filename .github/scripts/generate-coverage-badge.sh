#!/usr/bin/env bash

set -euo pipefail

CSV_FILE="${1:-target/site/jacoco/jacoco.csv}"
OUTPUT_FILE="${2:-.github/badges/jacoco.svg}"

if [[ ! -f "$CSV_FILE" ]]; then
  echo "JaCoCo CSV report not found: $CSV_FILE" >&2
  exit 1
fi

read -r MISSED COVERED < <(
  awk -F',' 'NR > 1 { missed += $4; covered += $5 } END { printf "%.0f %.0f\n", missed, covered }' "$CSV_FILE"
)

TOTAL=$((MISSED + COVERED))

if (( TOTAL == 0 )); then
  PERCENTAGE=100
else
  PERCENTAGE=$(((COVERED * 100 + TOTAL / 2) / TOTAL))
fi

COLOR="#e05d44"
if (( PERCENTAGE >= 90 )); then
  COLOR="#4c1"
elif (( PERCENTAGE >= 75 )); then
  COLOR="#97ca00"
elif (( PERCENTAGE >= 60 )); then
  COLOR="#dfb317"
fi

mkdir -p "$(dirname "$OUTPUT_FILE")"

cat > "$OUTPUT_FILE" <<EOF
<svg xmlns="http://www.w3.org/2000/svg" width="116" height="20" role="img" aria-label="coverage: ${PERCENTAGE}%">
  <title>coverage: ${PERCENTAGE}%</title>
  <linearGradient id="s" x2="0" y2="100%">
    <stop offset="0" stop-color="#bbb" stop-opacity=".1"/>
    <stop offset="1" stop-opacity=".1"/>
  </linearGradient>
  <clipPath id="r">
    <rect width="116" height="20" rx="3" fill="#fff"/>
  </clipPath>
  <g clip-path="url(#r)">
    <rect width="63" height="20" fill="#555"/>
    <rect x="63" width="53" height="20" fill="${COLOR}"/>
    <rect width="116" height="20" fill="url(#s)"/>
  </g>
  <g fill="#fff" text-anchor="middle" font-family="Verdana,Geneva,DejaVu Sans,sans-serif" font-size="11">
    <text x="32.5" y="15" fill="#010101" fill-opacity=".3">coverage</text>
    <text x="32.5" y="14">coverage</text>
    <text x="88.5" y="15" fill="#010101" fill-opacity=".3">${PERCENTAGE}%</text>
    <text x="88.5" y="14">${PERCENTAGE}%</text>
  </g>
</svg>
EOF

if [[ -n "${GITHUB_OUTPUT:-}" ]]; then
  echo "percentage=${PERCENTAGE}" >> "$GITHUB_OUTPUT"
fi

echo "Generated coverage badge: ${PERCENTAGE}%"
