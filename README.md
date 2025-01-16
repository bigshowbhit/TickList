# TickList
 The TickList Application is a desktop-based to-do manager that streamlines task organization with an intuitive interface, priority colour coding, automated reminders, and persistent storage for seamless productivity.

**Features:**

Task Management:
- Add tasks with details such as title, description, due date, priority, and completion status.
- Delete individual tasks or all tasks at once.
- Mark tasks as completed using a checkbox in the task table.

Visual Enhancements:
- Priority-based color coding:
          - Red: Important tasks.
          - Green: Normal tasks.
          - Gray: Completed tasks.
- Intuitive  table-based UI for task visualization.

Reminders and Notifications:
- Automatically checks for tasks due within 24 hours.
- Notifies the user with a pop-up reminder for tasks nearing their deadline.

Persistence:
- Tasks are saved to a file (tasks.ser) using serialization.
- Tasks are reloaded on application startup to ensure no data loss.

Splash Screen:
- Displays a splash screen with the application logo and loading message during startup.
