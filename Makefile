# ============================
# Makefile for Kind + K8s Deployment
# ============================

CLUSTER_NAME = social-network-cluster
K8S_MANIFESTS = k8sdeployment.yaml
IMAGE_NAME = springapp1:latest
NODE_PORT = 30080

# Default target
.PHONY: all
all: recreate-cluster load-image deploy

# ============================
# Cluster management
# ============================

.PHONY: recreate-cluster
recreate-cluster:
	@echo "🔍 Checking for existing Kind cluster '$(CLUSTER_NAME)'..."
	@if kind get clusters | grep -q $(CLUSTER_NAME); then \
		echo "🧹 Deleting existing Kind cluster '$(CLUSTER_NAME)'..."; \
		kind delete cluster --name $(CLUSTER_NAME); \
	fi
	@echo "🚀 Creating new Kind cluster '$(CLUSTER_NAME)'..."
	kind create cluster --name $(CLUSTER_NAME)

# ============================
# Build & Load Docker image
# ============================

.PHONY: build-image
build-image:
	@echo "🏗️  Building Spring Boot Docker image..."
	docker build -t $(IMAGE_NAME) .

.PHONY: load-image
load-image: build-image
	@echo "📦 Loading image into Kind cluster..."
	kind load docker-image $(IMAGE_NAME) --name $(CLUSTER_NAME)

# ============================
# Deploy to Kubernetes
# ============================

.PHONY: deploy
deploy:
	@echo "📜 Applying Kubernetes manifests..."
	kubectl apply -f $(K8S_MANIFESTS)
	@echo "⏳ Waiting for pods to become ready..."
	kubectl wait --for=condition=Ready pods --all --timeout=120s
	@echo "✅ Deployment complete!"

# ============================
# Cleanup
# ============================

.PHONY: delete-cluster
delete-cluster:
	@echo "🧨 Deleting Kind cluster '$(CLUSTER_NAME)'..."
	kind delete cluster --name $(CLUSTER_NAME)

