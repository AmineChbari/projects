# 🗳️ Real-Time Online Voting System

[![Node.js](https://img.shields.io/badge/Node.js-v14+-green?logo=node.js)](https://nodejs.org/)
[![Socket.IO](https://img.shields.io/badge/Socket.IO-4.x-blue?logo=socket.io)](https://socket.io/)
[![Webpack](https://img.shields.io/badge/Webpack-5.x-blue?logo=webpack)](https://webpack.js.org/)
[![Babel](https://img.shields.io/badge/Babel-7.x-yellow?logo=babel)](https://babeljs.io/)
[![License](https://img.shields.io/badge/License-MIT-green)](#license)

A real-time voting system built with modern JavaScript technologies. This project demonstrates full-stack development with a focus on clean architecture, real-time communication, and secure session management.

## 🎯 Features

- **Real-Time Communication**: WebSocket-based updates using Socket.IO for instant result synchronization
- **Dual Interface System**: Separate admin and voter interfaces with role-based access control
- **Live Admin Dashboard**: Launch/terminate votes, monitor participants, view aggregated results in real-time
- **Voter Interface**: Vote with four options (For / Against / No Position / Abstain), change vote while poll is open
- **Session Protection**: Single admin instance enforcement — only one admin can connect at a time
- **Broadcast Results**: Automatic result distribution to all participants upon vote completion

## 🏗️ Architecture

The system follows a **Client-Server architecture** with clear separation of concerns:

```
┌──────────────────────────────────────────────┐
│           Admin / Voter Clients              │
│         (ES6+ | Webpack | Babel)             │
└──────────────────┬───────────────────────────┘
                   │ Socket.IO Events (WebSocket)
┌──────────────────▼───────────────────────────┐
│              Node.js Server                  │
│  ├─ HTTP Static File Server                  │
│  ├─ Socket.IO (IOController)                 │
│  └─ Vote State Management (in-memory)        │
└──────────────────────────────────────────────┘
```

### Key Design Decisions

- **Private Class Fields**: JavaScript `#privateField` syntax for encapsulation in `IOController`
- **Event-Driven Server**: Socket.IO event handlers isolated in a dedicated controller class
- **Modular Client Code**: Separate modules for admin logic, voter logic, shared constants, and utilities
- **Build Pipeline**: Webpack + Babel for bundling and ES6+ → ES5 transpilation

## 🚀 Getting Started

### Prerequisites

- **Node.js** v14 or higher
- **npm** v6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/aminechbari/projects.git
   cd projects/others/vote
   ```

2. **Install & build the client**
   ```bash
   cd client
   npm install
   npm run build
   ```

3. **Install & start the server**
   ```bash
   cd ../server
   npm install
   npm start
   ```

4. **Open your browser** → `http://localhost:8080`

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT`   | `8080`  | Server listening port |

## 💻 Usage

| URL | Role | Description |
|-----|------|-------------|
| `/` | Everyone | Home page |
| `/admin-vote` | Admin | Launch/terminate votes, monitor results |
| `/votant` | Voter | Cast and update your vote |
| `/about` | Everyone | Project info |

**Admin flow**: Open `/admin-vote` → Enter a vote subject → Click Start → Watch votes come in → Click End to broadcast results.

**Voter flow**: Open `/votant` → Wait for vote to open → Click your choice → Change your mind anytime while the vote is open.

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend runtime | Node.js (HTTP + Socket.IO) |
| Real-time transport | Socket.IO 4.x |
| Frontend bundler | Webpack 5 + Babel 7 |
| Client language | JavaScript ES6+ (Modules) |
| Styling | CSS3 |

## 📂 Project Structure

```
vote/
├── client/
│   ├── scripts/
│   │   ├── admin.js              # Admin interface logic
│   │   ├── voter.js              # Voter interface logic
│   │   ├── tools.js              # Shared DOM utilities
│   │   └── messageConstants.js   # Socket event name constants
│   ├── style/                    # CSS stylesheets
│   ├── html/                     # HTML templates
│   └── webpack.config.js
│
└── server/
    ├── app.js                    # Entry point
    ├── controllers/
    │   ├── io.controller.js      # Socket.IO event handling (class-based)
    │   └── requestController.js  # Static file serving
    ├── utils/
    │   ├── messageConstants.js   # Shared event name constants
    │   └── contentType.utils.js  # MIME type resolution
    └── package.json
```

## 🚧 Future Improvements

- **Database persistence**: Store vote history (MongoDB / PostgreSQL)
- **Authentication**: JWT tokens for admin access
- **TypeScript**: Add type safety across the codebase
- **Tests**: Unit and integration tests with Jest
- **Docker**: Containerize for easy deployment
- **Rate limiting**: Prevent vote spam

## 👤 Author

**Amine Chbari** — Software Engineering Student (Mastère Ingénierie Logiciel, ISCOD)

- GitHub: [@aminechbari](https://github.com/aminechbari)
- LinkedIn: [amine-chbari](https://www.linkedin.com/in/amine-chbari/)

---

*Built to demonstrate real-time full-stack architecture with Node.js and Socket.IO.*
