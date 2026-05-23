const express = require('express');
const fs = require('fs');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// ── Users ──────────────────────────────────────────────
const USERS_FILE = 'users.json';

let users = [];
if (fs.existsSync(USERS_FILE)) {
    users = JSON.parse(fs.readFileSync(USERS_FILE));
}

function saveUsers() {
    fs.writeFileSync(USERS_FILE, JSON.stringify(users));
}

// ── Rooms ──────────────────────────────────────────────
const ROOMS_FILE = 'rooms.json';

let rooms = [];
if (fs.existsSync(ROOMS_FILE)) {
    rooms = JSON.parse(fs.readFileSync(ROOMS_FILE));
} else {
    rooms = [
        { name: "301", building: "GLE", time: "8:30 AM - 12:30 PM", schedule: "Monday" },
        { name: "202", building: "RTL", time: "1:00 PM - 5:00 PM", schedule: "Tuesday" }
    ];
    fs.writeFileSync(ROOMS_FILE, JSON.stringify(rooms));
}

function saveRooms() {
    fs.writeFileSync(ROOMS_FILE, JSON.stringify(rooms));
}

// ── Routes ─────────────────────────────────────────────
app.get('/', (req, res) => {
    res.send("Backend is running!");
});

// Register
app.post('/save-profile', (req, res) => {
    const { username, password } = req.body;
    if (!username || !password) {
        return res.status(400).send("Missing username or password");
    }

    const exists = users.find(u => u.username === username);
    if (exists) {
        return res.status(409).send("Username already taken");
    }

    users.push({ username, password });
    saveUsers();
    res.status(200).send("User registered successfully");
});

// Login
app.post('/login', (req, res) => {
    const { username, password } = req.body;
    const user = users.find(u => u.username === username && u.password === password);
    if (user) {
        res.status(200).send("Login successful");
    } else {
        res.status(401).send("Invalid username or password");
    }
});

// Get all rooms
app.get('/get-rooms', (req, res) => {
    res.json(rooms);
});

// Add a room
app.post('/save-room', (req, res) => {
    const { name, building, time, schedule } = req.body;
    if (!name || !building || !time || !schedule) {
        return res.status(400).send("Missing room data");
    }
    rooms.push({ name, building, time, schedule });
    saveRooms();
    res.status(200).send("Room added successfully");
});

// Delete a room
app.post('/delete-room', (req, res) => {
    const { name } = req.body;
    rooms = rooms.filter(r => r.name !== name);
    saveRooms();
    res.status(200).send("Room deleted successfully");
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
