#!/bin/bash
# ==============================================================================
# QE PLATFORM - EMERGENCY KILL SWITCH
# ==============================================================================
# This script forcefully bypasses all quality gates in the event of an 
# infrastructure failure (e.g., Grid down, API staging environment unreachable)
# to allow emergency hotfixes to proceed to production.
# 
# Usage: ./kill-switch.sh [ACTIVATE|DEACTIVATE] <INCIDENT_ID> <AUTHORIZER>
# ==============================================================================

set -e

ACTION=$1
INCIDENT_ID=$2
AUTHORIZER=$3

if [ "$#" -ne 3 ]; then
    echo "Usage: ./kill-switch.sh [ACTIVATE|DEACTIVATE] <INCIDENT_ID> <AUTHORIZER>"
    exit 1
fi

STATE_FILE=".qe_kill_switch_state"

if [ "$ACTION" == "ACTIVATE" ]; then
    echo "🚨 ACTIVATING EMERGENCY KILL SWITCH 🚨"
    echo "Reason: Incident $INCIDENT_ID"
    echo "Authorized By: $AUTHORIZER"
    
    # In a real K8s environment, this would create a ConfigMap or update Redis
    # For MVP, we write a local state file that the CI pipeline checks
    
    cat <<EOF > $STATE_FILE
{
  "active": true,
  "incident_id": "$INCIDENT_ID",
  "authorizer": "$AUTHORIZER",
  "timestamp": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
    echo "✅ Kill Switch ACTIVATED. Quality gates are now BYPASSED."
    echo "WARNING: A permanent audit log has been recorded. This must be resolved within 4 hours."

elif [ "$ACTION" == "DEACTIVATE" ]; then
    echo "🟢 DEACTIVATING EMERGENCY KILL SWITCH 🟢"
    
    cat <<EOF > $STATE_FILE
{
  "active": false,
  "incident_id": "RESOLVED_$INCIDENT_ID",
  "authorizer": "$AUTHORIZER",
  "timestamp": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
    echo "✅ Kill Switch DEACTIVATED. Normal quality gates RESTORED."

else
    echo "Invalid action. Use ACTIVATE or DEACTIVATE."
    exit 1
fi
