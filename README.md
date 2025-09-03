# Aman Daily Routine - Android Task Manager

A personalized Android application for managing daily tasks with real-time features, smart notifications, and scheduled alarms.

## 📱 Features

### ✨ Core Functionality
- **Personalized Dashboard** - "Aman Daily Routine" with real-time clock and date
- **Task Management** - Add, edit, delete tasks with priority levels (High/Medium/Low)
- **Smart Completion** - Tasks automatically move to history when completed
- **Real-time Statistics** - Live counters for total, completed, and pending tasks
- **Progress Tracking** - Visual progress bar with motivational messages

### ⏰ Alarm & Notifications
- **Scheduled Reminders** - Set date and time for task reminders
- **Background Alarms** - Works even when app is closed
- **Instant Alerts** - Ringtone and vibration start immediately at scheduled time
- **Confirmation Dialog** - Popup asks for acknowledgment when alarm triggers
- **Smart Controls** - Stop alarm with YES or continue with notification controls

### 📊 Advanced Features
- **History Tracking** - Permanent record of completed tasks (cannot be deleted)
- **Navigation Tabs** - Switch between active tasks and history
- **Real-time Updates** - Live clock updates every second
- **Priority Color Coding** - Visual priority indicators
- **Motivational Messages** - Dynamic messages based on progress

## 🏗️ Architecture

- **MVVM Pattern** - Model-View-ViewModel architecture
- **Room Database** - Local data persistence with SQLite
- **LiveData** - Reactive UI updates
- **Repository Pattern** - Clean data layer separation
- **Background Services** - AlarmManager for scheduled notifications

## 🛠️ Technical Stack

- **Language:** Java
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Database:** Room (SQLite)
- **Architecture:** MVVM + Repository
- **UI:** Material Design Components

## 📦 Dependencies

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
```
## 📱 Permissions Required

- `POST_NOTIFICATIONS` - For task reminders
- `SCHEDULE_EXACT_ALARM` - For precise alarm scheduling
- `VIBRATE` - For alarm vibration
- `SYSTEM_ALERT_WINDOW` - For popup dialogs
- `WAKE_LOCK` - For lock screen notifications

## 🎯 How to Use

### Adding Tasks
1. Tap the **+** floating action button
2. Enter title and description
3. Select priority level
4. Optionally set reminder date/time
5. Tap **Add**

### Managing Alarms
1. When alarm time arrives → Ringtone + vibration start
2. Popup appears asking for acknowledgment
3. Tap **YES** → Alarm stops
4. Tap **NO** → Alarm continues with stop notification

### Viewing History
1. Tap **History** tab
2. View all completed tasks with completion timestamps
3. History is permanent and cannot be deleted

## 📂 Project Structure

```
app/src/main/java/com/umakant/dailyroutine/
├── MainActivity.java          # Main UI controller
├── Todo.java                  # Task entity
├── TodoHistory.java           # History entity
├── TodoDao.java              # Database access
├── TodoDatabase.java         # Room database
├── TodoRepository.java       # Data repository
├── TodoViewModel.java        # UI data management
├── TodoAdapter.java          # Tasks RecyclerView adapter
├── HistoryAdapter.java       # History RecyclerView adapter
├── AlarmReceiver.java        # Alarm broadcast receiver
├── AlarmActionReceiver.java  # Notification actions
├── AlarmPopupActivity.java   # Confirmation dialog
├── AlarmManager.java         # Alarm scheduling
└── NotificationHelper.java   # Notification management
```


## 👨‍💻 Developer

**Aman**
- GitHub: [@yourusername](https://github.com/amanjava01)
- Email: amanranahyd5546.email@example.com

## 🙏 Acknowledgments

- Material Design Components for UI elements
- Android Architecture Components for MVVM implementation
- Room Database for local data persistence
