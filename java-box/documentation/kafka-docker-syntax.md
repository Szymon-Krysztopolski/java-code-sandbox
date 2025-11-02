# Docker Compose Syntax Explained: Kafka Cluster with KRaft and UI

This document provides a detailed explanation of each configuration key and environment variable used in the Docker
Compose setup for a **3-node Kafka cluster** running in **KRaft mode**, with an integrated **Kafka UI** for monitoring.

---

## 🧱 Common Docker Keys

| Key              | Description                                                                                                        |
|------------------|--------------------------------------------------------------------------------------------------------------------|
| `image`          | Specifies which Docker image to use. For Kafka, `confluentinc/cp-kafka:7.8.0` provides Kafka 7.8.0 from Confluent. |
| `hostname`       | Defines the internal hostname for the container (used for inter-container communication).                          |
| `container_name` | Sets a fixed name for the container instead of a random one.                                                       |
| `ports`          | Maps container ports to host ports in the format `HOST:CONTAINER`.                                                 |
| `environment`    | Defines environment variables passed into the container for configuration.                                         |
| `volumes`        | Mounts local directories or files into the container (for persistence).                                            |
| `networks`       | Connects the container to a Docker network. Containers on the same network can communicate via hostnames.          |
| `depends_on`     | Ensures one service starts only after the specified services are running.                                          |

---

## 🧩 Kafka Broker Containers

There are three Kafka broker containers — `kafka_1`, `kafka_2`, and `kafka_3`. Each container runs an identical
configuration except for **hostnames**, **IDs**, and **ports**.

---

### ⚙️ Basic Container Settings

| Key              | Example                       | Description                                           |
|------------------|-------------------------------|-------------------------------------------------------|
| `image`          | `confluentinc/cp-kafka:7.8.0` | The Kafka image with KRaft mode support.              |
| `hostname`       | `kafka1`                      | Internal DNS name used by other containers.           |
| `container_name` | `kafka1`                      | Fixed Docker name for easier reference and debugging. |

---

### 🔌 Port Mapping

| Host Port | Container Port | Purpose                                                         |
|-----------|----------------|-----------------------------------------------------------------|
| 9092      | 9092           | Kafka broker port (for clients and inter-broker communication). |
| 9093      | 9093           | Controller communication port (for KRaft metadata replication). |

Each container uses different host ports to avoid conflicts:

| Container | Host Ports | Internal Ports |
|-----------|------------|----------------|
| kafka_1   | 9092–9093  | 9092–9093      |
| kafka_2   | 9094–9095  | 9092–9093      |
| kafka_3   | 9096–9097  | 9092–9093      |

---

## 🌍 Environment Variables (Kafka Configuration)

Kafka configuration is primarily handled via environment variables.

### 🆔 Node and Broker Identification

| Variable          | Description                                               |
|-------------------|-----------------------------------------------------------|
| `KAFKA_NODE_ID`   | Unique ID for this Kafka node within the cluster.         |
| `KAFKA_BROKER_ID` | Traditional broker ID (still required for compatibility). |

Each container has its own unique ID:

* `kafka_1 → 1`
* `kafka_2 → 2`
* `kafka_3 → 3`

---

### ⚙️ Roles

`KAFKA_PROCESS_ROLES: 'broker,controller'`

Defines which roles this process performs:

* **broker** → Handles producers, consumers, and topic data.
* **controller** → Manages cluster metadata and elections.

Each Kafka node in **KRaft mode** can act as both a broker and a controller.

---

### 🗳️ Controller Quorum (KRaft Consensus)

`KAFKA_CONTROLLER_QUORUM_VOTERS: '1@kafka1:9093,2@kafka2:9093,3@kafka3:9093'`

Defines all nodes participating in the **Raft-based consensus** for metadata management.

**Format:** `<node.id>@<hostname>:<controller-port>`

All three brokers are part of the controller election process.

---

### 🔊 Listeners

`KAFKA_LISTENERS: 'PLAINTEXT://kafka1:9092,CONTROLLER://kafka1:9093'`

Specifies network listeners used by the broker:

* **PLAINTEXT://** → for client and inter-broker communication.
* **CONTROLLER://** → for internal KRaft communication.

Each listener has a descriptive name (e.g., `PLAINTEXT`, `CONTROLLER`).

---

### 📣 Advertised Listeners

`KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT://kafka1:9092'`

Defines the address advertised to clients connecting to the broker (from outside the Docker network).

---

### 🔐 Security Protocol Mapping

`KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT'`

Maps each listener name to its security protocol.

Here, both listeners use **PLAINTEXT**, meaning unencrypted communication (useful for local development).

---

### 🧭 Controller and Broker Listener Names

| Variable                           | Description                                                             |
|------------------------------------|-------------------------------------------------------------------------|
| `KAFKA_CONTROLLER_LISTENER_NAMES`  | Defines which listener is used by the controller (`CONTROLLER`).        |
| `KAFKA_INTER_BROKER_LISTENER_NAME` | Defines which listener brokers use to talk to each other (`PLAINTEXT`). |

---

### 🧬 Cluster Identity

`CLUSTER_ID: 'EmptNWtoR4GGWx-BH6nGLQ'`

A globally unique identifier for the Kafka cluster — **must be identical across all brokers**.

---

### 🔁 Replication and Fault Tolerance

| Variable                                 | Description                                                                              |
|------------------------------------------|------------------------------------------------------------------------------------------|
| `KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR` | Replication factor for the internal `__consumer_offsets` topic (commonly 3).             |
| `KAFKA_DEFAULT_REPLICATION_FACTOR`       | Default replication factor for newly created topics.                                     |
| `KAFKA_MIN_INSYNC_REPLICAS`              | Minimum number of replicas that must acknowledge a write for it to succeed (usually 2).  |
| `KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS` | Delay before consumer group rebalancing begins after membership changes (0 = immediate). |

These settings ensure **data durability** and **high availability**.

---

### 💾 Volumes

```yaml
volumes:
  - ./kafka1/data:/var/lib/kafka/data
```

Mounts a local directory to persist Kafka log data. This prevents data loss on container restart.

| Broker  | Host Path     | Container Path      |
|---------|---------------|---------------------|
| kafka_1 | ./kafka1/data | /var/lib/kafka/data |
| kafka_2 | ./kafka2/data | /var/lib/kafka/data |
| kafka_3 | ./kafka3/data | /var/lib/kafka/data |

---

### 🌐 Networks

```yaml
networks:
  - shared-network
```

All services share a single **Docker network**, allowing hostname-based communication (e.g., `kafka2` can connect to
`kafka1`).

---

## 📊 Kafka UI Service

The **Kafka UI** service provides a web interface to visualize topics, partitions, consumer groups, and cluster status.

### ⚙️ Configuration

| Key              | Example                         | Description                                     |
|------------------|---------------------------------|-------------------------------------------------|
| `image`          | `provectuslabs/kafka-ui:latest` | Official Kafka UI image.                        |
| `container_name` | `kafka-cluster-ui`              | Fixed name for the UI container.                |
| `ports`          | `8090:8080`                     | Exposes the UI on host port 8090.               |
| `depends_on`     | `kafka_1, kafka_2, kafka_3`     | Ensures brokers start before the UI.            |
| `networks`       | `shared-network`                | Connects the UI to the same network as brokers. |

### 🌍 Environment Variables

| Variable                            | Description                                                 |
|-------------------------------------|-------------------------------------------------------------|
| `KAFKA_CLUSTERS_0_NAME`             | Display name of the cluster in the UI (e.g., `local`).      |
| `KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS` | Comma-separated list of broker endpoints for UI connection. |

---

## 🧭 Summary

This Docker Compose configuration defines:

* A **3-node Kafka cluster** in **KRaft mode**.
* Each broker also serves as a **controller**.
* **Persistent storage** via Docker volumes.
* **Shared network** for broker communication.
* A **Kafka UI dashboard** for cluster visualization and management.

---

### ✅ Cluster Diagram (Conceptual)

```
        ┌────────────┐        ┌────────────┐        ┌────────────┐
        │  kafka_1   │        │  kafka_2   │        │  kafka_3   │
        │ Broker+Ctrl│        │ Broker+Ctrl│        │ Broker+Ctrl│
        │ Ports 9092 │        │ Ports 9094 │        │ Ports 9096 │
        └──────┬─────┘        └──────┬─────┘        └──────┬─────┘
               │                     │                     │
               └──────────────┬──────┴─────────────────────┘
                              │
                        Shared Network
                              │
                        ┌────────────┐
                        │  Kafka UI  │
                        │  (8090)    │
                        └────────────┘
```

---

### Docker configuration
```yaml
  kafka_1:
    image: confluentinc/cp-kafka:7.8.0
    hostname: kafka1
    container_name: kafka1
    ports:
      - "9092:9092"
      - "9093:9093"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_BROKER_ID: 1
      KAFKA_PROCESS_ROLES: 'broker,controller'
      KAFKA_CONTROLLER_QUORUM_VOTERS: '1@kafka1:9093,2@kafka2:9093,3@kafka3:9093'
      KAFKA_LISTENERS: 'PLAINTEXT://kafka1:9092,CONTROLLER://kafka1:9093'
      KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT://kafka1:9092'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT'
      KAFKA_CONTROLLER_LISTENER_NAMES: 'CONTROLLER'
      KAFKA_INTER_BROKER_LISTENER_NAME: 'PLAINTEXT'
      CLUSTER_ID: 'EmptNWtoR4GGWx-BH6nGLQ'
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_DEFAULT_REPLICATION_FACTOR: 3
      KAFKA_MIN_INSYNC_REPLICAS: 2
    volumes:
      - ./kafka1/data:/var/lib/kafka/data
    networks:
      - shared-network

  kafka_2:
    image: confluentinc/cp-kafka:7.8.0
    hostname: kafka2
    container_name: kafka2
    ports:
      - "9094:9092"
      - "9095:9093"
    environment:
      KAFKA_NODE_ID: 2
      KAFKA_BROKER_ID: 2
      KAFKA_PROCESS_ROLES: 'broker,controller'
      KAFKA_CONTROLLER_QUORUM_VOTERS: '1@kafka1:9093,2@kafka2:9093,3@kafka3:9093'
      KAFKA_LISTENERS: 'PLAINTEXT://kafka2:9092,CONTROLLER://kafka2:9093'
      KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT://kafka2:9092'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT'
      KAFKA_CONTROLLER_LISTENER_NAMES: 'CONTROLLER'
      KAFKA_INTER_BROKER_LISTENER_NAME: 'PLAINTEXT'
      CLUSTER_ID: 'EmptNWtoR4GGWx-BH6nGLQ'
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_DEFAULT_REPLICATION_FACTOR: 3
      KAFKA_MIN_INSYNC_REPLICAS: 2
    volumes:
      - ./kafka2/data:/var/lib/kafka/data
    networks:
      - shared-network

  kafka_3:
    image: confluentinc/cp-kafka:7.8.0
    hostname: kafka3
    container_name: kafka3
    ports:
      - "9096:9092"
      - "9097:9093"
    environment:
      KAFKA_NODE_ID: 3
      KAFKA_BROKER_ID: 3
      KAFKA_PROCESS_ROLES: 'broker,controller'
      KAFKA_CONTROLLER_QUORUM_VOTERS: '1@kafka1:9093,2@kafka2:9093,3@kafka3:9093'
      KAFKA_LISTENERS: 'PLAINTEXT://kafka3:9092,CONTROLLER://kafka3:9093'
      KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT://kafka3:9092'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT'
      KAFKA_CONTROLLER_LISTENER_NAMES: 'CONTROLLER'
      KAFKA_INTER_BROKER_LISTENER_NAME: 'PLAINTEXT'
      CLUSTER_ID: 'EmptNWtoR4GGWx-BH6nGLQ'
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_DEFAULT_REPLICATION_FACTOR: 3
      KAFKA_MIN_INSYNC_REPLICAS: 2
    volumes:
      - ./kafka3/data:/var/lib/kafka/data
    networks:
      - shared-network

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: kafka-cluster-ui
    ports:
      - "8090:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka1:9092,kafka2:9092,kafka3:9092
    depends_on:
      - kafka_1
      - kafka_2
      - kafka_3
    networks:
      - shared-network
```

### Links
* [Setting Up a Kafka Cluster Using Docker Compose(Kraft Mode): A Step-by-Step Guide](https://medium.com/@darshak.kachchhi/setting-up-a-kafka-cluster-using-docker-compose-a-step-by-step-guide-a1ee5972b122)

