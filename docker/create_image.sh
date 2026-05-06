#!/usr/bin/env bash
# Builds Docker images for app-service and utility-service.
# Requirements: Docker must be installed and running. No JDK required.
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "==> Building app-service image..."
docker build \
  -t virtus-fitness/app-service:latest \
  -f "$SCRIPT_DIR/app-service.Dockerfile" \
  "$PROJECT_ROOT/app-service"

echo "==> Building utility-service image..."
docker build \
  -t virtus-fitness/utility-service:latest \
  -f "$SCRIPT_DIR/utility-service.Dockerfile" \
  "$PROJECT_ROOT/utility-service"

echo "Done. Images built:"
echo "  virtus-fitness/app-service:latest"
echo "  virtus-fitness/utility-service:latest"
