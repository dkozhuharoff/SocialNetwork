# 💻 Social Network Application

## 📖 About the Project

The Social Network application aims to provide a simplified version of a social networking platform with essential features such as user management, categorization, posting, and commenting.  
Each post can belong to one or multiple categories, allowing for structured and discoverable content. Users can also comment on posts to facilitate interaction.

### 👥 Roles and Permissions
There are three main roles in the platform:

- **Administrator** – Has full CRUD (Create, Read, Update, Delete) permissions across all entities.
- **Moderator** – Can perform CRUD operations, but only for categories, posts, and comments associated with individual users.
- **Regular User** – Can view all categories, posts, and comments, and manage only their own posts and comments.

---

## 🧰 Used Technologies

| Layer | Technology |
|--------|-------------|
| Backend | Java, Spring Boot |
| Database | MySQL (via Hibernate & Spring Data JPA) |
| Containerization | Docker, Docker Compose |
| Deployment | Kubernetes (Kind), Makefile Automation |
| CI/CD | GitHub Actions (Maven Build, Docker Image Build, Docker Hub Push) |

The **Dockerfile** builds the Spring Boot application into a container image.  
The **docker-compose.yml** (if used locally) orchestrates the application and database containers.  
For Kubernetes-based local deployment, the **k8sdeployment.yaml** and **Makefile** files are used.

---

## ⚙️ CI/CD Pipelines

Three main pipelines are configured in the Git repository:

1. **CI (Continuous Integration):**  
   Runs Maven build and tests on every branch and commit.

2. **Branch Image Build:**  
   Builds Docker images for all feature branches, ensuring early validation of environment consistency.

3. **Main Branch Deployment:**  
   When changes are merged into `main`, a new image is built and automatically pushed to Docker Hub for deployment.

---

## ☸️ Kubernetes Deployment (with Kind)

### 🗂️ Files
- **`k8sdeployment.yaml`** — Defines all Kubernetes resources:
    - `Secret` for MySQL credentials
    - `ConfigMap` for database configuration
    - `Deployment` and `Service` for MySQL
    - `Deployment` and `Service` for the Spring Boot application
- **`Makefile`** — Automates the entire deployment process in Kind.

### 🧾 k8sdeployment.yaml Overview

The manifest includes:

- **MySQL Secret & ConfigMap:** Store credentials and connection info
- **MySQL Deployment:** Runs a MySQL 8.0 instance
- **Spring Boot Deployment:** Runs the application and connects to MySQL using environment variables
- **Services:**
    - MySQL service on port **3306**
    - Spring Boot service (NodePort) on **30080**

---

## 🛠️ Deployment via Makefile

The provided **Makefile** simplifies deployment to a local Kind cluster.  
It automatically handles cluster recreation, image building, and manifest application.

### 🔧 Main Commands

| Command                 | Description                                                        |
|-------------------------|--------------------------------------------------------------------|
| `make all`              | Recreate Kind cluster, build image, load it, and deploy everything |
| `make recreate-cluster` | Delete existing and create a new Kind cluster                      |
| `make build-image`      | Build the Spring Boot Docker image                                 |
| `make load-image`       | Load the built image into the Kind cluster                         |
| `make deploy`           | Apply Kubernetes manifests and wait for pods to become ready       |
| `make delete-cluster`   | Delete the Kind cluster                                            |

---

## 🚀 How to Run Locally

### 1. Prerequisites
Ensure you have installed:
- [Docker](https://docs.docker.com/get-docker/)
- [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

### 2. Clone the Repository
```bash
git clone https://github.com/dkozhuharoff/social-network.git
cd social-network
```
### 3. Build and Deploy
```bash
make all
```
This will:
- **Recreate the Kind cluster**
- **Build and load the Docker image**
- **Deploy all resources defined in k8sdeployment.yaml**
### 4. Access the Application
```
http://localhost:30080
```
### 5. Check Pods and Services
To verify that everything is running correctly:
```bash
kubectl get pods
kubectl get svc
```
### 6. Clean Up
To delete the Kind cluster:
```bash
make delete-cluster
```

---

## 🧩 Notes

- **The Spring Boot container uses environment variables injected from Kubernetes ConfigMap and Secret.**
- **The MySQL data is stored in an ephemeral emptyDir volume. For production, replace this with a PersistentVolumeClaim (PVC).**
- **NodePort 30080 is exposed for local access; adjust if needed.**