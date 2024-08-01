#!/bin/bash

# Variables
FRONT_REPO_URL="https://github.com/godaasinskaite/ergonomic-calculator" 
BACK_REPO_URL="https://github.com/godaasinskaite/ErgonomicCalculator" 
FRONT_DIR="ergonomic-calculator1" 
BACK_DIR="ErgonomicCalculator1" 

 Function to clone a repository
clone_repo() {
  local repo_url=$1
  local target_dir=$2

  if [ -d "$target_dir" ]; then
    echo "Directory $target_dir already exists. Skipping clone."
  else
    echo "Cloning repository $repo_url into $target_dir..."
    git clone "$repo_url" "$target_dir"

    if [ $? -ne 0 ]; then
      echo "Failed to clone repository $repo_url"
      exit 1
    fi
  fi
}

# Function to build and run Docker Compose
run_docker_compose() {
  local project_dir=$1

  echo "Navigating to $project_dir..."
  cd "$project_dir" || exit 1

  echo "Running Docker Compose in $project_dir..."
  docker-compose up --build -d

  if [ $? -ne 0 ]; then
    echo "Failed to run Docker Compose in $project_dir"
    exit 1
  fi

  # Navigate back to the original directory
  cd - || exit 1
}

# Clone repositories
clone_repo "$FRONT_REPO_URL" "$FRONT_DIR"
clone_repo "$BACK_REPO_URL" "$BACK_DIR"

# Build and run Docker Compose for both repositories
run_docker_compose "$FRONT_DIR"
run_docker_compose "$BACK_DIR"

# Open localhost in the default web browser
echo "Opening localhost..."

# Use xdg-open or open (fallback) for simplicity
if command -v xdg-open > /dev/null; then
  xdg-open "http://localhost:4200"
elif command -v open > /dev/null; then
  open "http://localhost:4200"
else
  echo "Open http://localhost:4200 in your web browser"
fi