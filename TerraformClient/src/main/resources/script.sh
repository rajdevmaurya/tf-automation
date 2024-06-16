#!/bin/bash

set -x

param1=$1

echo "Parameter 1: $param1"


# Set the repository URL
#REPO_URL="https://<ACCESS-TOKEN>@dev.azure.com/<AZURE-DEVOPS-ORG-NAME>/voting-app/_git/voting-app"



#mkdir tmp_repo1
# Clone the git repository into the /tmp directory
git clone "$REPO_URL" /tmp/tmp_repo1
rm -rf /tmp/tmp_repo1/*.tf
cp -r /tmp/"$param1"/*_infra /tmp/tmp_repo1
cp -r /tmp/"$param1"/*.tf /tmp/tmp_repo1

# Navigate into the cloned repository directory
cd /tmp/tmp_repo1

# Make changes to the Kubernetes manifest file(s)
# For example, let's say you want to change the image tag in a deployment.yaml file
#sed -i "s|image:.*|image: <ACR-REGISTRY-NAME>/$2:$3|g" k8s-specifications/$1-deployment.yaml
#git remote set-url origin "$REPO_URL"
# Add the modified files
pwd
git add .

# Commit the changes
git commit -m "Update  manifest"

# Push the changes back to the repository
git push origin main

# Cleanup: remove the temporary directory
rm -rf /tmp/tmp_repo1
cd /tmp
rm -rf /tmp/"$param1"