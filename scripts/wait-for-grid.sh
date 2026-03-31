#!/usr/bin/env sh

set -eu

grid_status_url="${1:-http://selenium-hub:4444/status}"
timeout_seconds="${2:-120}"
expected_nodes="${3:-1}"
poll_interval="${GRID_POLL_INTERVAL:-2}"
deadline=$(($(date +%s) + timeout_seconds))

echo "Waiting for Selenium Grid at ${grid_status_url}"

while [ "$(date +%s)" -lt "${deadline}" ]; do
  if status_json="$(curl -fsS "${grid_status_url}" 2>/dev/null)"; then
    ready="$(printf '%s' "${status_json}" | jq -r '.value.ready // .ready // false')"
    nodes="$(printf '%s' "${status_json}" | jq '(.value.nodes // .nodes // []) | length')"

    if [ "${ready}" = "true" ] && [ "${nodes}" -ge "${expected_nodes}" ]; then
      echo "Selenium Grid is ready with ${nodes} node(s)."
      exit 0
    fi

    echo "Grid status: ready=${ready}, nodes=${nodes}, expected>=${expected_nodes}"
  else
    echo "Grid status endpoint not reachable yet"
  fi

  sleep "${poll_interval}"
done

echo "Timed out waiting for Selenium Grid after ${timeout_seconds}s" >&2
exit 1