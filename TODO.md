# Implementation TODO for Backend Integration and UI Cohesion in University ERP

## Step 1: Centralize Backend Service Initialization
- Refactor `MainApp.java` to instantiate all backend services centrally.
- Create a `ServiceRegistry` or `BackendFacade` singleton to hold service instances.
- Modify constructor signatures of UI panels (dashboards, login) to accept service instances.

## Step 2: Session and Global Context Setup
- Ensure `Session` is initialized on login with current user and semester context.
- Modify login flow to set session appropriately after successful authentication.
- Propagate session context to dashboards and any service consumers.

## Step 3: Refactor Login Flow and Handling
- Refactor `LoginController.java` and `LoginPanel.java` to integrate dependency injection of services.
- Add consistent error handling and UI feedback on login failures.
- Transition to appropriate dashboard smoothly after successful login.

## Step 4: Refactor UI Dashboards
- Change dashboards (e.g., `StudentDashboardPanel.java`) to accept injected backend services.
- Remove direct service instantiations inside UI components.
- Unify error handling, data fetching and usage of session state.

## Step 5: Enforce Access Control and Maintenance Mode
- Use `AccessControl` consistently to check permissions before UI actions.
- Ensure UI components react to maintenance mode (disable inputs, show warnings).

## Step 6: Verify DAO and Database Integration
- Validate connection pooling and transaction management in DAOs.
- Consider encapsulating database connection lifecycle if not present.

## Step 7: Testing and Validation
- Perform thorough testing of login, session, role-based access, maintenance mode.
- Test all dashboard loading and functional flows for each user role.
- Validate error handling and edge cases.

---

Please confirm if you would like me to proceed with the first step of this implementation plan (centralizing backend service initialization and refactoring MainApp), or propose any revisions to the above TODO.
