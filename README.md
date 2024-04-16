# About the project

The application, named Social Network, aims to create a simplified version of a social networking platform. It encompasses user management, categorization, posting, and commenting features. Posts can be associated with one or multiple categories, facilitating organized content creation. Additionally, users can leave comments on posts.

There are three distinct roles within the platform: administrators, moderators, and regular users. Administrators possess full permissions to conduct CRUD (Create, Read, Update, Delete) operations across all entities. Moderators, on the other hand, can perform similar operations but are limited to categories, posts, and comments associated with individual users. Regular users have viewing access to all categories, posts, and comments, with the capability to edit and delete only their own contributions.

# Used technologies

The technology stack employed includes Java, the Spring Framework, particularly Spring Data JPA, and Hibernate for database management, utilizing MySQL. Furthermore, Docker and Docker Compose are utilized through a Dockerfile and docker-compose file. The Dockerfile facilitates the conversion of the application into an image, while the docker-compose file orchestrates the deployment of both the application and its database within containers.

# CI/CD

In the Git repository, three pipelines are established. The Continuous Integration (CI) pipeline executes a Maven build upon each branch and commit. Another pipeline focuses on building a Docker image for every branch, ensuring compatibility with changes made outside the main branch. The final pipeline operates exclusively on the main branch, where changes from other branches are merged. Upon merging, an image is built and pushed to the Docker Hub repository automatically, streamlining the deployment process.
