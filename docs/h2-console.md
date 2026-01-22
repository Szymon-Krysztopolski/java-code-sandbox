# Accessing H2 Console in Spring Boot

This guide explains how to access the **H2 Database Console** based on the provided Spring Boot configuration.

---

## ⚙️ Configuration Overview

```properties
server.port=8081
server.servlet.contextPath=/api

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=none

spring.h2.console.enabled=true
spring.h2.console.settings.web-allow-others=true
spring.h2.console.path=/h2-console
```

# 🌐 Accessing H2 Console

Once the application is running, open your browser and go to: [http://localhost:8081/api/h2-console](http://localhost:8081/api/h2-console)

---

## 🔑 Login Details

When you open the console, you might see default **Saved Settings** such as:

* Saved Settings: Generic H2 (Embedded)
* Setting Name: Generic H2 (Embedded)
* Driver Class: org.h2.Driver
* JDBC URL: jdbc:h2:~/test

⚠️ **Do not use** the default `jdbc:h2:~/test` URL — that refers to a **file-based database** located in your user home directory.

Instead, update it to match your application configuration:

| Field            | Correct Value        |
|------------------|----------------------|
| **Driver Class** | `org.h2.Driver`      |
| **JDBC URL**     | `jdbc:h2:mem:testdb` |
| **User Name**    | `sa`                 |
| **Password**     | *(leave empty)*      |

> 💡 Ensure that the JDBC URL matches the one set in `spring.datasource.url`.

---

## 🧭 Notes

- Console is **enabled** via `spring.h2.console.enabled=true`.
- Accessible from **other hosts** because `spring.h2.console.settings.web-allow-others=true` is set.
- All endpoints (including `/h2-console`) are prefixed with `/api`.
- The database is **in-memory** (`mem:testdb`), meaning data is cleared when the application stops.

---

## ✅ Example Access Steps

1. Run your Spring Boot application.
2. Open your browser at:  
   👉 [http://localhost:8081/api/h2-console](http://localhost:8081/api/h2-console)
3. In the login form, update the fields:
    - **Driver Class:** `org.h2.Driver`
    - **JDBC URL:** `jdbc:h2:mem:testdb`
    - **User Name:** `sa`
    - **Password:** *(blank)*
4. Click **Connect**.

You’ll now be connected to the in-memory database and can run SQL queries.

---

## 🧩 Troubleshooting

- **Page not found?** → Verify the app runs on port `8081` and context path `/api`.
- **Connection error?** → Make sure the JDBC URL is exactly `jdbc:h2:mem:testdb`.
- **Data missing after restart?** → The database is in-memory and resets each time.

---

✅ You’re now ready to use the H2 Console!
