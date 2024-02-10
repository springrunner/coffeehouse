# 13. Use UUID as Domain models Identifier for Persistence

Date: 2024-02-09

## Status

Accepted

## Context

In the development of the "Coffeehouse" online service, a critical architectural decision is the selection of an appropriate domain identifier strategy for entity persistence. Domain identifiers are crucial for uniquely identifying entities within the application, ensuring data consistency, and clarifying entity relationships. The choice between database auto-generated keys (such as auto-increment ID) and Universally Unique Identifiers (UUID) is significant due to their impact on the application's scalability, maintainability, and the flexibility of the domain model.

### Possible Solutions

#### 1. Database-Generated ID
- Pros
  - implicity: Easy to understand and implement.
  - Performance: Efficient in databases due to sequential nature, improving index performance.
- Cons
  - Dependency: Creates a strong coupling between the domain entities and the database layer.
  - Flexibility: Limits the ability to generate IDs before persistence, potentially complicating scenarios where an ID is needed immediately upon entity creation.

#### 2. Universally Unique Identifier (UUID)
- Pros
  - Universality: Can be generated independently of the database, supporting a decoupled architecture.
  - Flexibility: Allows for the immediate generation and use of an identifier, facilitating complex workflows and asynchronous operations.
  - Scalability: Supports distributed systems and services where entities might be created in different contexts or locations without direct database access.
- Cons
  - Space: Less space-efficient compared to auto-increment IDs, requiring more storage and memory.
  - Performance: Can lead to non-sequential access patterns in databases, potentially degrading index performance.

## Decision

After careful consideration of the trade-offs between database-generated ID and UUID, we have decided to use UUID as our domain identifier for entities within the "Coffeehouse" project.

Reasoning for the Decision
- Decoupling and Flexibility: The need for a loosely coupled architecture that allows domain objects to manage their lifecycle independently of the database is a priority. UUID provide the flexibility to generate identifiers at the point of entity creation, supporting a variety of workflows and asynchronous operations.
- Distributed Systems Compatibility: As the "Coffeehouse" service may evolve to include distributed systems and microservices, UUID offer a scalable solution for entity identification across different services and databases without collision.
- Operational Resilience: Using UUID minimizes dependency on specific database implementations, enhancing the project's resilience in scenarios of data migration, replication, and in environments where database access is not centralized.

## Consequences

- Storage and Performance Consideration: Adopting UUIDs will require careful database schema design to mitigate the impact on storage and index performance. This includes using optimized data types for UUID storage and considering index strategies that accommodate the random nature of UUIDs.
- Application Complexity: The team must manage the slightly increased complexity in handling UUIDs, including formatting and transmitting these identifiers efficiently in client-server communications.
- Migration and Integration Planning: Existing systems or future integrations that rely on sequential IDs will require adaptation strategies to work seamlessly with UUID-based identifiers.
