#!/usr/bin/env bash
# Tags and pushes app-service and utility-service images to DockerHub.
# Usage: DOCKERHUB_USERNAME=your_user ./publish_image.sh
set -e

if [ -z "$DOCKERHUB_USERNAME" ]; then
  echo "ERROR: DOCKERHUB_USERNAME environment variable is not set."
  echo "Usage: DOCKERHUB_USERNAME=your_user ./publish_image.sh"
  exit 1
fi

echo "==> Tagging and pushing app-service..."
docker tag virtus-fitness/app-service:latest "$DOCKERHUB_USERNAME/app-service:latest"
docker push "$DOCKERHUB_USERNAME/app-service:latest"

echo "==> Tagging and pushing utility-service..."
docker tag virtus-fitness/utility-service:latest "$DOCKERHUB_USERNAME/utility-service:latest"
docker push "$DOCKERHUB_USERNAME/utility-service:latest"

echo "Done. Images published to DockerHub under '$DOCKERHUB_USERNAME'."
