#!/usr/bin/env bash
# Publishes docker_compose.yml to DockerHub as an OCI artifact using 'oras'.
# Requirements: oras CLI (https://oras.land) and Docker must be installed.
# Usage: DOCKERHUB_USERNAME=your_user ./publish_docker-compose.sh
#
# To run the full app after publishing (single command):
#   docker run --rm -v "$PWD":/workspace ghcr.io/oras-project/oras:latest \
#     pull $DOCKERHUB_USERNAME/virtus-fitness-compose:latest --output /workspace && \
#   DOCKERHUB_USERNAME=your_user docker compose -f docker_compose.yml up
set -e

if [ -z "$DOCKERHUB_USERNAME" ]; then
  echo "ERROR: DOCKERHUB_USERNAME environment variable is not set."
  echo "Usage: DOCKERHUB_USERNAME=your_user ./publish_docker-compose.sh"
  exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
IMAGE="$DOCKERHUB_USERNAME/virtus-fitness-compose:latest"

echo "==> Publishing docker_compose.yml as OCI artifact to $IMAGE ..."
oras push "$IMAGE" \
  --config /dev/null:application/vnd.oci.image.config.v1+json \
  "$SCRIPT_DIR/docker_compose.yml:application/yaml"

echo "Done. Artifact published to: $IMAGE"
echo ""
echo "To pull and run the application:"
echo "  oras pull $IMAGE"
echo "  DOCKERHUB_USERNAME=$DOCKERHUB_USERNAME docker compose -f docker_compose.yml up"
